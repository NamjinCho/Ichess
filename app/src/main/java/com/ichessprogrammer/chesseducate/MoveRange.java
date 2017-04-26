package com.ichessprogrammer.chesseducate;

import java.util.ArrayList;

/**
 * Created by XNOTE on 2016-07-16.
 *
 *
 * 기물을 움직일 때 사용.
 */
public class MoveRange {

    public final int MAX = 8;
    public final int MIN = 1;


    public ChessBoard m_ChessBoard;
    public int m_StartX;
    public int m_StartY;
    public String m_Piece;
    public boolean WB;
    public Mask m_Mask;

    public MoveRange(ChessBoard chessBoard, boolean WB) {
        m_ChessBoard = chessBoard;
        this.WB = WB;
        m_Mask = new Mask();
    }
    public void AddIndex(String piece, int x, int y) {
        m_Piece = piece;
        m_StartX = x;
        m_StartY = y;
    }




    public MoveRange(ChessBoard chessBoard, int startX, int startY, String piece, boolean WB, Mask mask) {
        m_ChessBoard = chessBoard;
        m_StartX = startX; //DoOrder에서 i, j로 받은 것
        m_StartY = startY;
        m_Piece = piece;
        this.WB = WB;
        m_Mask = mask;
        m_Mask.AddPickedIndex(startX, startY);
    }

    public Mask CheckRange() {
        switch (m_Piece) {
            case "K":
                return KingMoveRange();
            case "Q":
                return QueenMoveRange();
            case "R":
                return RookMoveRange();
            case "B":
                return BishopMoveRange();
            case "N":
                return KnightMoveRange();
            case "P":
                return PawnMoveRange();
        }
        return m_Mask;
    }
    public Mask CheckRange(boolean bol) { //TODO
        switch (m_Piece) {
            case "K":
                return KingMoveRange(bol);
            case "Q":
                return QueenMoveRange();
            case "R":
                return RookMoveRange();
            case "B":
                return BishopMoveRange();
            case "N":
                return KnightMoveRange();
            case "P":
                return PawnMoveRange();
        }
        return m_Mask;
    }

    public Mask KingMoveRange(boolean bol) { // 총 8방향
        for (int i = m_StartX - 1; i <= m_StartX + 1; i++) { //범위 설정
            for (int j = m_StartY - 1; j <= m_StartY + 1; j++) {
                if (i <= MAX && i >= MIN && j <= MAX && j >= MIN) { //범위 초과 제한
                    if (m_ChessBoard.board[i][j].myPIN == null)
                        m_Mask.AddMask(i, j);
                    else if (m_ChessBoard.board[i][j].myPIN.WB != WB)
                        m_Mask.AddMask(i, j);
                }
            }
        }
        return m_Mask;
    }


    public Mask KingMoveRange() { // 총 8방향
        for (int i = m_StartX - 1; i <= m_StartX + 1; i++) { //범위 설정
            for (int j = m_StartY - 1; j <= m_StartY + 1; j++) {
                if (i <= MAX && i >= MIN && j <= MAX && j >= MIN) { //범위 초과 제한
                    if (m_ChessBoard.board[i][j].myPIN == null)
                        m_Mask.AddMask(i, j);
                    else if (m_ChessBoard.board[i][j].myPIN.WB != WB)
                        m_Mask.AddMask(i, j);
                }
            }
        }
        if(m_ChessBoard.board[m_StartX][m_StartY].myPIN.move == false) {
            if (CanKingSideCastling()) {
                m_Mask.AddMask(m_StartX + 2, m_StartY);
            }
            if (CanQueenSideCastling()) {
                m_Mask.AddMask(m_StartX - 2, m_StartY);
            }
        }
        return m_Mask;
    }
    public boolean CanKingSideCastling() { //start X , start Y;
        PIN pin;
        if(m_ChessBoard.board[8][m_StartY].myPIN != null) { // 기본 조건
            pin = m_ChessBoard.board[8][m_StartY].myPIN;
            if(pin.Type.compareTo("R") == 0 && !pin.move ) {// Rook이 움직이지 않았을 경우.
                for(int i = m_StartX + 1; i <= m_StartX + 2; i ++) {
                    if(m_ChessBoard.board[i][m_StartY].myPIN != null)
                        return  false;
                }
                if(isSafe(m_StartX, m_StartX + 2))
                    return  true;

            }
        }
        return false;
    }
    public boolean CanQueenSideCastling() { //start X , start Y;
        PIN pin;
        if(m_ChessBoard.board[1][m_StartY].myPIN != null) {// 기본조건
            pin = m_ChessBoard.board[1][m_StartY].myPIN;
            if(pin.Type.compareTo("R") == 0 && !pin.move ) {
                for(int i = m_StartX - 1; i >= m_StartX - 3; i--) { // 4, 3, 2
                    if(m_ChessBoard.board[i][m_StartY].myPIN != null)
                        return  false;
                }
                if(isSafe(m_StartX-2, m_StartX))
                    return  true;
            }
        }
        return false;
    }

    public boolean isAnyPieceCanDo(String type, int fromX, int fromY, int toX, int toY) {
        Mask mask = new Mask();
        mask = AttackRange(mask, type, fromX, fromY);
        ArrayList<Integer> x_list = mask.getList_X();
        ArrayList<Integer> y_list = mask.getList_Y();

        for(int j = 0; j < x_list.size(); j++) {
            int x = x_list.get(j);
            int y = y_list.get(j);

            if(toX == x && toY == y)
                return true;
        }
        return false;
    }

    public Mask AttackRange(Mask mask, String type, int x, int y) { //there is anything to attack destination except me?
        try {

            ChessBoard chessBoard = (ChessBoard) m_ChessBoard.clone();

            MoveRange moveRange = new MoveRange(chessBoard, WB);
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    if (chessBoard.board[i][j].myPIN != null) {
                        if(x == i && y == j)
                            continue;
                        if (chessBoard.board[i][j].myPIN.WB == WB && chessBoard.board[i][j].myPIN.Type.compareTo(type) == 0) {
                            moveRange.AddIndex(chessBoard.board[i][j].myPIN.Type, i, j);
                            mask = moveRange.CheckRange(true);
                        }
                    }
                }
            }
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return mask;
    }





    public boolean isSafe(int from, int to)  {
        Mask mask = new Mask();
        mask = AttackRange(mask);

        ArrayList<Integer> x_list = mask.getList_X();
        ArrayList<Integer> y_list = mask.getList_Y();
        for(int i = from; i <= to; i++) {
            for(int j = 0; j < x_list.size(); j++) {
                int x = x_list.get(j);
                int y = y_list.get(j);

                if(m_StartX == x && m_StartY == y)
                    return false;
            }
        }
        return true;
    }
    public Mask AttackRange(Mask mask) { //king castling
        try {
            ChessBoard chessBoard = (ChessBoard) m_ChessBoard.clone();

            MoveRange moveRange = new MoveRange(chessBoard, !WB);
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    if (chessBoard.board[i][j].myPIN != null) {
                        if (chessBoard.board[i][j].myPIN.WB != WB) {
                            moveRange.AddIndex(chessBoard.board[i][j].myPIN.Type, i, j);
                            mask = moveRange.CheckRange(true);
                        }
                    }
                }
            }
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return mask;
    }







    public Mask QueenMoveRange() { // Rook과 Bishop의 융합.
        m_Mask = RookMoveRange();
        m_Mask = BishopMoveRange();

        return m_Mask;
    }
    public Mask RookMoveRange() {

        // for문을 2개 돌리는 것과 4개 돌리는거...
        // 어떤게 더 효율적일까? 엄청 조금이지만 complexity를 줄이는 걸로.. 복잡해보이지만
        int x = m_StartX;
        int y = m_StartY;

        for (int i = x - 1; i >= MIN; i--) { //상식적으로 현재 위치는 받을 필요가 없잖아? 밑의 내용 클래스화 시키고 싶은데 불필요해서 안함.
            if (m_ChessBoard.board[i][y].myPIN != null) { //
                if (m_ChessBoard.board[i][y].myPIN.WB == WB)
                    break;
                else {
                    m_Mask.AddMask(i, y);
                    break;
                }
            }
            m_Mask.AddMask(i, y);
            if (i == MIN)
                break;
        }
        for (int i = x + 1; i <= MAX; i++) {
            if (m_ChessBoard.board[i][y].myPIN != null) {
                if (m_ChessBoard.board[i][y].myPIN.WB == WB)
                    break;
                else {
                    m_Mask.AddMask(i, y);
                    break;
                }

            }
            m_Mask.AddMask(i, y);
            if (i == MAX)
                break;
        }
        for (int i = y - 1; i >= MIN; i--) {
            if (m_ChessBoard.board[x][i].myPIN != null) {
                if (m_ChessBoard.board[x][i].myPIN.WB == WB)
                    break;
                else {
                    m_Mask.AddMask(x, i);
                    break;
                }
            }
            m_Mask.AddMask(x, i);
            if (i == MIN)
                break;
        }
        for (int i = y + 1; i <= MAX; i++) {
            if (m_ChessBoard.board[x][i].myPIN != null) {
                if (m_ChessBoard.board[x][i].myPIN.WB == WB)
                    break;
                else {
                    m_Mask.AddMask(x, i);
                    break;
                }
            }
            m_Mask.AddMask(x, i);
            if (i == MAX)
                return m_Mask;
        }
        return m_Mask;
    }

    public Mask BishopMoveRange() {
        int x = m_StartX;
        int y = m_StartY;

        for (int i = MIN; i < MAX; i++) { //0부터 시작하나 if 문을 넣어서 걸려내나 complexity 는 비슷할 것.

            if ((x + i) > MAX || (y + i) > MAX)
                break;
            if (m_ChessBoard.board[x + i][y + i].myPIN != null) {
                if (m_ChessBoard.board[x + i][y + i].myPIN.WB == WB)
                    break;
                else {
                    m_Mask.AddMask(x + i, y + i);
                    break;
                }
            }
            m_Mask.AddMask(x + i, y + i);


        }
        for (int i = MIN; i < MAX; i++) { //0부터 시작하나 if 문을 넣어서 걸려내나 complexity 는 비슷할 것.

            if ((x + i) > MAX || (y - i) < MIN)
                break;
            if (m_ChessBoard.board[x + i][y - i].myPIN != null) {
                if (m_ChessBoard.board[x + i][y - i].myPIN.WB == WB)
                    break;
                else {
                    m_Mask.AddMask(x + i, y - i);
                    break;
                }
            }
            m_Mask.AddMask(x + i, y - i);

        }
        for (int i = MIN; i < MAX; i++) { //0부터 시작하나 if 문을 넣어서 걸려내나 complexity 는 비슷할 것.
            if ((x - i) < MIN || (y + i) > MAX)
                break;

            if (m_ChessBoard.board[x - i][y + i].myPIN != null) {
                if (m_ChessBoard.board[x - i][y + i].myPIN.WB == WB)
                    break;
                else {
                    m_Mask.AddMask(x - i, y + i);
                    break;
                }
            }
            m_Mask.AddMask(x - i, y + i);


        }
        for (int i = MIN; i < MAX; i++) { //0부터 시작하나 if 문을 넣어서 걸려내나 complexity 는 비슷할 것.
            if ((x - i) < MIN || (y - i) < MIN)
                break;

            if (m_ChessBoard.board[x - i][y - i].myPIN != null) {
                if (m_ChessBoard.board[x - i][y - i].myPIN.WB == WB)
                    break;
                else {
                    m_Mask.AddMask(x - i, y - i);
                    break;
                }
            }
            m_Mask.AddMask(x - i, y - i);


        }
        return m_Mask;
    }


    public void CheckKnightMove(int x, int y) {
        if (x <= MAX && x >= MIN && y <= MAX && y >= MIN) {
            if (m_ChessBoard.board[x][y].myPIN != null) {
                if (m_ChessBoard.board[x][y].myPIN.WB != WB)
                    m_Mask.AddMask(x, y);
            } else
                m_Mask.AddMask(x, y);
        }
    }

    public Mask KnightMoveRange() {
        int x = m_StartX;
        int y = m_StartY;

        CheckKnightMove(x + 2, y + 1);
        CheckKnightMove(x + 2, y - 1);
        CheckKnightMove(x - 2, y + 1);
        CheckKnightMove(x - 2, y - 1);
        CheckKnightMove(x + 1, y + 2);
        CheckKnightMove(x + 1, y - 2);
        CheckKnightMove(x - 1, y + 2);
        CheckKnightMove(x - 1, y - 2);



        return m_Mask;
    }

    //앙파상 준비
    public Mask PawnMoveRange() {
        int x = m_StartX;
        int y = m_StartY;


        //끝까지 도착하면 다른 형태로 변환되기 때문에 굳이 y 끝값을 처리해 줄 필요 없음.
        if (WB) { //고려사항 이동할 때 장애물 파악할 것
            y += 1; //가장 기본적인 이동.
            if (m_StartY == 2) { //처음에 시작할 경우.
                if (m_ChessBoard.board[x][y].myPIN == null) { //선택된 pawn 앞칸이 비었을 경우
                    m_Mask.AddMask(x, y);

                    if (m_ChessBoard.board[x][y + 1].myPIN == null) { // 선택된 pawn 두칸 앞이 비었을 경우
                        m_Mask.AddMask(x, y + 1);
                    }

                }
            }
        }
        else {
            y -= 1; //가장 기본적인 이동.
            if (m_StartY == 7) { //처음에 시작할 경우.
                if (m_ChessBoard.board[x][y].myPIN == null) { //선택된 pawn 앞칸이 비었을 경우
                    m_Mask.AddMask(x, y);

                    if (m_ChessBoard.board[x][y - 1].myPIN == null) { // 선택된 pawn 두칸 앞이 비었을 경우
                        m_Mask.AddMask(x, y - 1);
                    }
                }
            }
        }

        if (m_ChessBoard.board[x][y].myPIN == null)  //선택된 pawn 앞칸이 비었을 경우
            m_Mask.AddMask(x, y);
        if (x > MIN) {
            if (m_ChessBoard.board[x - 1][y].myPIN != null) { //나중에 뜯어 고쳐야지 ㅡㅡ;
                if (m_ChessBoard.board[x - 1][y].myPIN.WB != WB)
                    m_Mask.AddMask(x - 1, y);
            }
        }
        if (x < MAX) {
            if (m_ChessBoard.board[x + 1][y].myPIN != null) { // 선택된 pawn 앞칸이 비어있지 않고 앞칸이 적일 경우 .. 말이 안됨 고쳐야함
                if (m_ChessBoard.board[x + 1][y].myPIN.WB != WB)
                    m_Mask.AddMask(x + 1, y);
            }
        }

        if(m_ChessBoard.enPassant) { //앙파상 고려
            if (x > MIN) {
                if (m_ChessBoard.board[x - 1][m_StartY].myPIN != null) { //나중에 뜯어 고쳐야지 ㅡㅡ;
                    PIN pin = m_ChessBoard.board[x - 1][m_StartY].myPIN;
                    if (pin.WB != WB) {
                        if(pin.Type.compareTo("P") == 0 && pin.enPassantEnable)
                            m_Mask.AddMask(x - 1, y);
                    }
                }
            }
            if (x < MAX) {
                if (m_ChessBoard.board[x + 1][m_StartY].myPIN != null) { // 선택된 pawn 앞칸이 비어있지 않고 앞칸이 적일 경우 .. 말이 안됨 고쳐야함
                    PIN pin = m_ChessBoard.board[x + 1][m_StartY].myPIN;
                    if (pin.WB != WB) {
                        if(pin.Type.compareTo("P") == 0 && pin.enPassantEnable) //앙파상 되나요?
                            m_Mask.AddMask(x + 1, y);
                    }
                }
            }
        }



        return m_Mask;
    }
}
