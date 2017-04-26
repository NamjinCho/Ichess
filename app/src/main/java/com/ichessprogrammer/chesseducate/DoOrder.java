package com.ichessprogrammer.chesseducate;

import android.util.Log;

import com.ichessprogrammer.chesseducate.PGN.ChessLogic;

/**
 * Created by XNOTE on 2016-07-11.
 */
public class DoOrder { //PGN 관리 해줌

    public boolean isAttack = false;
    public ChessBoard mChessBoard;

    public DoOrder() {

    }

    public boolean Ordering(String playerAnswer, String pgnAnswer) {

        if(pgnAnswer.contains(playerAnswer))  return true;

        if(playerAnswer.contains("+")) {
            playerAnswer = playerAnswer.substring(0, playerAnswer.indexOf("+"));
        }
        else if (playerAnswer.contains("#")) {
            playerAnswer = playerAnswer.substring(0, playerAnswer.indexOf("#"));
        }
        if(pgnAnswer.contains("+")) {
            pgnAnswer = pgnAnswer.substring(0, pgnAnswer.indexOf("+"));
        }
        else if (pgnAnswer.contains("#")) {
            pgnAnswer = pgnAnswer.substring(0, pgnAnswer.indexOf("#"));
        }

        if(pgnAnswer.contains(playerAnswer))  return true;


        String playerType;
        String pgnType;

        if(IsPawn(playerAnswer.charAt(0))) {
            playerType = "P";
        }
        else {
            playerType = "" + playerAnswer.charAt(0);
            playerAnswer = playerAnswer.substring(1);
        }

        if(IsPawn(pgnAnswer.charAt(0))) {
            pgnType = "P";
        }
        else {
            pgnType = "" + pgnAnswer.charAt(0);
            pgnAnswer = pgnAnswer.substring(1);
        }

        String playerWhere ="";
        String playerTo = "";

        String pgnWhere = "";
        String pgnTo = "";

        String changePlayer ="";
        String changePgn ="";


        boolean isPromotion = false;
        if(pgnAnswer.contains("x")) {
            if (pgnAnswer.indexOf("x") == 0) { // 가장 무난한 방식 xd4일경우
                pgnTo = pgnAnswer.substring(1, 3);
            } else if (pgnAnswer.indexOf("x") == 1) { // 위치가 정해져 있을 경우. exd4
                pgnWhere =  pgnAnswer.substring(0, 1);
                pgnTo  =   pgnAnswer.substring(2, 4);
            } else if (pgnAnswer.indexOf("x") == 2) { // 혹시라도 위치좌표가 모두 정해져 있을 경우.
                pgnWhere =  pgnAnswer.substring(0, 2);
                pgnTo  =  pgnAnswer.substring(3, 5);
            }
            if (pgnAnswer.contains("=")) {
                isPromotion = true;
                changePgn =  pgnAnswer.substring(pgnAnswer.indexOf("=") + 1);
            }
        }
        else { //공격이 아닌 이동.;
            if (pgnAnswer.contains("=")) {
                isPromotion = true;
                changePgn = pgnAnswer.substring(pgnAnswer.indexOf("=") + 1);
                pgnAnswer = pgnAnswer.substring(0, pgnAnswer.indexOf("="));
            }
            if (pgnAnswer.length() == 2) { //Nd2
                pgnTo = pgnAnswer;
            } else if (pgnAnswer.length() == 3) {  //Nfd2
                pgnWhere = pgnAnswer.substring(0, 1);
                pgnTo = pgnAnswer.substring(1);
            } else {
                pgnWhere = pgnAnswer.substring(0, 2); //Nf2d2 같은 경우? 아직 이런 경우 본적이 없음.
                pgnTo = pgnAnswer.substring(2);
            }
        }

        if(playerAnswer.contains("x")) {
            if (playerAnswer.indexOf("x") == 0) { // 가장 무난한 방식 xd4일경우
                playerTo = playerAnswer.substring(1, 3);
            } else if (playerAnswer.indexOf("x") == 1) { // 위치가 정해져 있을 경우. exd4
                playerWhere =  playerAnswer.substring(0, 1);
                playerTo  =   playerAnswer.substring(2, 4);
            } else if (playerAnswer.indexOf("x") == 2) { // 혹시라도 위치좌표가 모두 정해져 있을 경우.
                playerWhere =  playerAnswer.substring(0, 2);
                playerTo  =  playerAnswer.substring(3, 5);
            }
            if (playerAnswer.contains("=")) {
                changePlayer =  playerAnswer.substring(playerAnswer.indexOf("=") + 1);
            }
        }
        else { //공격이 아닌 이동.
            if (playerAnswer.contains("=")) {
                isPromotion = true;
                changePlayer = playerAnswer.substring(playerAnswer.indexOf("=") + 1);
                playerAnswer = playerAnswer.substring(0, playerAnswer.indexOf("="));
            }
            if (playerAnswer.length() == 2) { //Nd2
                playerTo = playerAnswer;
            } else if (playerAnswer.length() == 3) {  //Nfd2
                playerWhere = playerAnswer.substring(0, 1);
                playerTo = playerAnswer.substring(1);
            } else {
                playerWhere = playerAnswer.substring(0, 2); //Nf2d2 같은 경우? 아직 이런 경우 본적이 없음.
                playerTo = playerAnswer.substring(2);
            }
        }
        if(isPromotion && !changePgn.contains(changePlayer))
            return  false;



        if(playerWhere.length() == 0) {
            if (playerTo.contains(pgnTo) && playerType.contains(pgnType))
                return true;
        }
        else {
            if(pgnWhere.contains(playerWhere) && playerTo.contains(pgnTo) && playerType.contains(pgnType)) {
                return true;
            }
        }
        return false;
    }

    // 명령대로 할 건데.
    public ChessBoard Ordering(ChessBoard chessboard, String order, boolean WB) {
        isAttack = false;
        String location = "";
        mChessBoard = chessboard;

        if (order.contains("+")) // 현재로선 의미가 없지만.. 그래서 일단은 제거하기로..
            order = order.substring(0, order.indexOf("+"));
        else if (order.contains("#")) { //이거 처리좀 해줘야 겠다.
            order = order.substring(0, order.indexOf("#"));
            chessboard.GameSet();
        }


        if (order.contains("O-O-O") || order.contains("0-0-0"))
            return QueenSideCastling(chessboard, WB);

        if (order.compareTo("O-O") == 0 || order.compareTo("0-0") == 0) //교차 할 때
            return KingSideCastling(chessboard, WB);


        String type;
        char piece = order.charAt(0); //일단 String으로 바꿀 건데. 일단 char

        if (IsPawn(piece)) { //pawn이라면
            location = order;
            type = "P";
        } else {
            location = order.substring(1);
            type = "" + piece;
        }

        if (location.contains("x")) { // 공격 명령. exd4 or Kxd4 같은경우  또는 xd4 Nfxd4도 생각

            String where = "";
            String to = "";

            isAttack = true;
            if (location.indexOf("x") == 0) { // 가장 무난한 방식 xd4일경우
                to = location.substring(1, 3);
                chessboard = FIndPieceCanMove(chessboard, to, type, WB);
            } else if (location.indexOf("x") == 1) { // 위치가 정해져 있을 경우. exd4
                where = location.substring(0, 1);
                to = location.substring(2, 4);
                chessboard = FIndPieceCanMove(chessboard, where, to, type, WB);
            } else if (location.indexOf("x") == 2) { // 혹시라도 위치좌표가 모두 정해져 있을 경우.
                where = location.substring(0, 2);
                to = location.substring(3, 5);
                chessboard = FIndPieceCanMove(chessboard, where, to, type, WB);
            }

            if (location.contains("=")) {
                type = order.substring(order.indexOf("=") + 1);
                return ChangePawntoWhat(chessboard, null, to, type, WB);
            }

        } else { //공격이 아닌 이동.
            Boolean isPromotion = false;
            String to = location;
            String where = "";
            String changetype = "";
            if (location.contains("=")) {
                isPromotion = true;
                changetype = order.substring(order.indexOf("=") + 1);
                location = location.substring(0, location.indexOf("="));
            }
            if (location.length() == 2) { //Nd2
                to = location;
                chessboard = FIndPieceCanMove(chessboard, location, type, WB);
            } else if (location.length() == 3) {  //Nfd2
                where = location.substring(0, 1);
                to = location.substring(1);
                chessboard = FIndPieceCanMove(chessboard, where, to, type, WB);
            } else {
                where = location.substring(0, 2); //Nf2d2 같은 경우? 아직 이런 경우 본적이 없음.
                to = location.substring(2);
                chessboard = FIndPieceCanMove(chessboard, where, to, type, WB);
            }
            if (isPromotion) {
                return ChangePawntoWhat(chessboard, null, to, changetype, WB);
            }
        }
        return chessboard;
    }

    protected boolean IsPawn(char c) {
        if (c != 'K' && c != 'Q' && c != 'R' && c != 'B' && c != 'N') return true;
        else return false;
    }

    //어떤 말이 움직일 건지 찾아보자~
    protected ChessBoard FIndPieceCanMove(ChessBoard chessboard, String order, String piece, boolean WB) {

        CoordinateFIltering coordinateFiltering = new CoordinateFIltering(order); //e5 = > 5, 5 , e4 => 5,  4
        int x = coordinateFiltering.getX();
        int y = coordinateFiltering.getY();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                if (chessboard.board[i][j].myPIN != null) {
                    if (chessboard.board[i][j].myPIN.WB == WB) {
                        if (chessboard.board[i][j].myPIN.Type.compareTo(piece) == 0) { // 명령대로
                            MovePiece movePiece = new MovePiece(chessboard, i, j, x, y, piece, WB);
                            if (movePiece.WhichType(isAttack)) {
                                return changeMove(i, j, x, y, chessboard, piece);
                            }
                        }
                    }
                }
            }
        }

        Log.i("없다고??", order);
        return chessboard;
    }

    protected ChessBoard FIndPieceCanMove(ChessBoard chessboard, String where, String order, String piece, boolean WB) { //이거 고치면 되는데.

        CoordinateFIltering coordinateFiltering = new CoordinateFIltering(order);
        int x = coordinateFiltering.getX();
        int y = coordinateFiltering.getY();


        int fromY = -1;
        int fromX = -1;

        if (where.length() == 1) {
            if (ChessLogic.hasNotationNumber(where.charAt(0))) { // 숫자
                fromY = coordinateFiltering.getY(where);
                for (int i = 1; i <= 8; i++) {
                    if (chessboard.board[i][fromY].myPIN != null) {
                        if (chessboard.board[i][fromY].myPIN.WB == WB) {
                            if (chessboard.board[i][fromY].myPIN.Type.compareTo(piece) == 0) { // PGN이 정확해도 찾아야 함. 오류 발견해서..
                                MovePiece movePiece = new MovePiece(chessboard, i, fromY, x, y, piece, WB);
                                if (movePiece.WhichType(isAttack)) {

                                    chessboard = changeMove(i, fromY, x, y, chessboard, piece);
                                    break;

                                }
                            }
                        }
                    }
                }
            } else {
                fromX = coordinateFiltering.getX(where);
                for (int i = 1; i <= 8; i++) {
                    if (chessboard.board[fromX][i].myPIN != null) {
                        if (chessboard.board[fromX][i].myPIN.WB == WB) {
                            if (chessboard.board[fromX][i].myPIN.Type.compareTo(piece) == 0) {


                                MovePiece movePiece = new MovePiece(chessboard, fromX, i, x, y, piece, WB);
                                if (movePiece.WhichType(isAttack)) {
                                    chessboard = changeMove(fromX, i, x, y, chessboard, piece);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } else if (where.length() == 2) {
            coordinateFiltering = new CoordinateFIltering(where);

            fromX = coordinateFiltering.getX();
            fromY = coordinateFiltering.getY();

            chessboard = changeMove(fromX, fromY, x, y, chessboard, piece);
        }
        return chessboard;
    }

    protected ChessBoard changeMove(int fromX, int fromY, int toX, int toY, ChessBoard chessboard, String piece) {

        if (isAttack && piece.contains("P") && chessboard.board[toX][toY].myPIN == null) {
            chessboard.board[toX][fromY].myPIN = null;
        }
        chessboard.board[toX][toY].myPIN = chessboard.board[fromX][fromY].myPIN;
        chessboard.board[toX][toY].myPIN.move = true;
        chessboard.board[fromX][fromY].myPIN = null;

        return chessboard;
    }


    protected ChessBoard KingSideCastling(ChessBoard chessBoard, boolean WB) {

        if (WB) { // y = 8 x = 5, 8 => 7, 6
            chessBoard.board[7][1].myPIN = chessBoard.board[5][1].myPIN;
            chessBoard.board[6][1].myPIN = chessBoard.board[8][1].myPIN;

            chessBoard.board[7][1].myPIN.move = true;
            chessBoard.board[6][1].myPIN.move = true;


            chessBoard.board[8][1].myPIN = null;
            chessBoard.board[5][1].myPIN = null;
        } else { //
            chessBoard.board[7][8].myPIN = chessBoard.board[5][8].myPIN;
            chessBoard.board[6][8].myPIN = chessBoard.board[8][8].myPIN;

            chessBoard.board[7][8].myPIN.move = true;
            chessBoard.board[6][8].myPIN.move = true;

            chessBoard.board[8][8].myPIN = null;
            chessBoard.board[5][8].myPIN = null;
        }
        return chessBoard;
    }

    protected ChessBoard QueenSideCastling(ChessBoard chessBoard, boolean WB) {

        if (WB) { // y == 1 x == 5  -> 3, x == 1 -> 4
            chessBoard.board[3][1].myPIN = chessBoard.board[5][1].myPIN;
            chessBoard.board[4][1].myPIN = chessBoard.board[1][1].myPIN;

            chessBoard.board[3][1].myPIN.move = true;
            chessBoard.board[4][1].myPIN.move = true;

            chessBoard.board[5][1].myPIN = null;
            chessBoard.board[1][1].myPIN = null;
        } else { //
            chessBoard.board[3][8].myPIN = chessBoard.board[5][8].myPIN;
            chessBoard.board[4][8].myPIN = chessBoard.board[1][8].myPIN;

            chessBoard.board[3][8].myPIN.move = true;
            chessBoard.board[4][8].myPIN.move = true;

            chessBoard.board[1][8].myPIN = null;
            chessBoard.board[5][8].myPIN = null;
        }
        return chessBoard;
    }


    //String locate는 과연 쓰일까?
    protected ChessBoard ChangePawntoWhat(ChessBoard chessBoard, String locate, String destination, String type, boolean WB) { //도대체 적당한 이름이 생각이 나지 않아서..
        int x;
        int y;
        CoordinateFIltering coordinateFiltering = new CoordinateFIltering(destination);

        x = coordinateFiltering.getX();
        y = coordinateFiltering.getY();


        try {
            chessBoard.board[x][y].myPIN.Type = type;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        /*
        if (WB) {// 흰색이면, 7에서 8일테니,
            chessBoard.board[x][y].myPIN = chessBoard.board[x][y - 1].myPIN;
            chessBoard.board[x][y].myPIN.Type = type;
            chessBoard.board[x][y - 1].myPIN = null;
        } else { //흑색이면 2에서 1로 이동할테니..
            chessBoard.board[x][y].myPIN = chessBoard.board[x][y + 1].myPIN;
            chessBoard.board[x][y].myPIN.Type = type;
            chessBoard.board[x][y + 1].myPIN = null;
        }
        */

        return chessBoard;
    }

}
