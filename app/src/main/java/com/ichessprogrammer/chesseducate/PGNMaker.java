package com.ichessprogrammer.chesseducate;

/**
 * Created by XNOTE on 2016-07-24.
 */
public class PGNMaker {
    public  String pgn;
    public ChessBoard mChessBoard;

    public int startX;
    public int startY;

    public int destX;
    public int destY;


    public PGNMaker() {
        pgn = "";
        mChessBoard = null;
    }
    public PGNMaker(ChessBoard chessBoard) {
        pgn = "";
        mChessBoard = chessBoard;
    }

    /*
    고려해야 할 것
    프로모션으로 바뀌는 경우
    이동, 공격 시 다른 말에도 가능성


    */

    //이동
    public String Move(int fromX, int fromY, int toX, int toY) {


        pgn = "";
        PIN pin = mChessBoard.board[fromX][fromY].myPIN;
        String from = "";
        MoveRange moveRange = new MoveRange(mChessBoard, pin.WB);


        CoordinateFIltering coordinateFIltering = new CoordinateFIltering(toX, toY);

        pgn = pgn + coordinateFIltering.getCoordinate();

        if(moveRange.isAnyPieceCanDo(pin.Type ,fromX, fromY, toX, toY))
            from =  coordinateFIltering.getCoordinate(fromX);

        if(pin.Type.compareTo("P") == 0)
            return from + pgn;
        else
            return  pin.Type + from + pgn;



    }
    //공격
    public String Attack(int fromX, int fromY, int toX, int toY) {

        pgn = "";
        PIN pin = mChessBoard.board[fromX][fromY].myPIN;
        String from = "";
        MoveRange moveRange = new MoveRange(mChessBoard, pin.WB);


        CoordinateFIltering coordinateFIltering = new CoordinateFIltering(toX, toY);

        pgn = pgn + coordinateFIltering.getCoordinate();

        if(moveRange.isAnyPieceCanDo(pin.Type ,fromX, fromY, toX, toY))
            from =  coordinateFIltering.getCoordinate(fromX);



        if(pin.Type.compareTo("P") == 0)
            return from + "x" + pgn;
        else
            return  pin.Type + from +"x"+ pgn;

    }
    //side
    public String sideCastling(boolean kingOrQueen){

        if(kingOrQueen)
            return "0-0";
        else
            return "0-0-0";
    }
    //전직??
    public String Promotion(String type) {
        return "" + "=" + type;
    }
    //체크
    public String check(int fromX, int fromY, int toX, int toY) {
        return ""+ "+";
    }
    //체크메이트 <- 좀 오래걸리겠네?
    public String CheckMake() {
        return ""+ "#";
    }










}
