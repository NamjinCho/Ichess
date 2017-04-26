package com.ichessprogrammer.chesseducate;

/**
 * Created by XNOTE on 2016-07-11.
 * <p/>
 * PGN값에 따라 움직일 수 있도록 함.
 */

//정확히 PGN은 여기서 다루기로 하자.
public class MovePiece { //귀찮으니 모든 움직임은 여기서 다루기로 하자.

    ChessBoard m_ChessBoard;
    int m_StartX;
    int m_StartY;
    int m_DestX;
    int m_DestY;
    String m_Piece;
    boolean WB;
    Mask m_Mask;

    final int MAX = 8;
    final int MIN = 1;

    int tempX = 0;
    int tempY = 0;

    public MovePiece(ChessBoard chessBoard, int startX, int startY, int destX, int destY, String piece, boolean WB) {
        m_ChessBoard = chessBoard;
        m_StartX = startX; //DoOrder에서 i, j로 받은 것
        m_StartY = startY;
        m_DestX = destX; // CoordinateFiltering에서 바꾼 값.
        m_DestY = destY;
        m_Piece = piece;
        this.WB = WB;
    }

    public MovePiece(ChessBoard chessBoard, int startX, int startY, String piece, boolean WB, Mask mask) {
        m_ChessBoard = chessBoard;
        m_StartX = startX; //DoOrder에서 i, j로 받은 것
        m_StartY = startY;
        m_Piece = piece;
        this.WB = WB;
        m_Mask = mask;
    }

    public boolean WhichType(boolean isAttack) {

        m_ChessBoard.enPassant = false;
        if(tempX != 0)
            m_ChessBoard.board[tempX][tempY].myPIN.enPassantEnable = false;

        switch (m_Piece) {
            case "K":
                return CanMoveKing();
            case "Q":
                return CanMoveQueen();
            case "R":
                return CanMoveRook();
            case "B":
                return CanMoveBishop();
            case "N":
                return CanMoveKnight();
            case "P":
                return CanMovePawn(isAttack);
        }
        return false;
    }


    public boolean CheckLocation(int x, int y) {
        if (x == m_DestX && y == m_DestY) return true; //지정된 위치가 목적지와 같으면 true 그렇지 않으면 false.
        return false;
    }

    //x는 가로 y는 세로인데 이중 어레이 문에선 순서를 바꿔서 해야 한다.
    //아직 앙파상인가 그거 고려 안함.
    public boolean CanMovePawn(boolean isAttack) { //TODO 다시 수정하자. 쓸데없는 중복이 많다.
        int x = -1;
        int y = -1;
        if (WB != m_ChessBoard.board[m_StartX][m_StartY].myPIN.WB)
            return false;

        if (WB) { //고려사항 이동할 때 장애물 파악할 것
            x = m_StartX;
            y = m_StartY + 1; //가장 기본적인 이동.
            if (m_StartY == 2) { //처음에 시작할 경우.
                if (!isAttack) {
                    if (m_ChessBoard.board[x][y].myPIN == null) { //선택된 pawn 앞칸이 비었을 경우
                        if (CheckLocation(x, y)) return true;

                        if (m_ChessBoard.board[x][y + 1].myPIN == null) { // 선택된 pawn 두칸 앞이 비었을 경우
                            if (CheckLocation(x, y + 1)) {
                                m_ChessBoard.enPassant = true;
                                m_ChessBoard.board[x][y-1].myPIN.enPassantEnable =true;
                                tempX = x;
                                tempY = y+1;
                                return true;
                            }
                        }
                    }
                } else if (isAttack) {
                    if (x > MIN) {
                        if (CheckLocation(x - 1, y)) return true;
                    }

                    if (x < MAX) {
                        if (CheckLocation(x + 1, y)) return true;
                    }
                }
            } else { //pawn이 처음에 시작되지 않을 경우.
                if (!isAttack) {
                    if (CheckLocation(x, y)) return true;
                } else {
                    if (x > MIN) {
                        if (CheckLocation(x - 1, y)) return true;
                    }

                    if (x < MAX) {
                        if (CheckLocation(x + 1, y)) return true;
                    }
                }
            }
        } else {
            x = m_StartX;
            y = m_StartY - 1; // 흑은 아래로 내려간다.

            if (m_StartY == 7) { //처음에 시작할 경우. pawn의 위치는 7에서 시작함
                if (!isAttack) {
                    if (m_ChessBoard.board[x][y].myPIN == null) { //선택된 pawn 앞칸이 비었을 경우
                        if (CheckLocation(x, y)) return true;
                        if (m_ChessBoard.board[x][y - 1].myPIN == null) { // 선택된 pawn 두칸 앞이 비었을 경우\ m_ChessBoard.enPassant = true;
                            if (CheckLocation(x, y - 1)) {
                                m_ChessBoard.enPassant = true;
                                m_ChessBoard.board[x][y+1].myPIN.enPassantEnable =true;
                                tempX = x;
                                tempY = y-1;
                                return true;
                            }
                        }

                    }
                } else {
                    if (x > MIN) {
                        //  if (m_ChessBoard.board[x - 1][y].myPIN != null) { //나중에 뜯어 고쳐야지 ㅡㅡ;
                        //  if (m_ChessBoard.board[x - 1][y].myPIN.WB != WB)
                        if (CheckLocation(x - 1, y)) return true;
                    }

                    if (x < MAX) {
                        //  if (m_ChessBoard.board[x + 1][y].myPIN != null) { // 선택된 pawn 앞칸이 비어있지 않고 앞칸이 적일 경우 .. 말이 안됨 고쳐야함
                        //  if (m_ChessBoard.board[x + 1][y].myPIN.WB != WB)
                        if (CheckLocation(x + 1, y)) return true;
                    }
                }
            } else { //pawn이 처음에 시작되지 않을 경우.
                if (!isAttack) {
                    if (CheckLocation(x, y)) return true;
                } else {
                    if (x > MIN) {
                        //if (m_ChessBoard.board[x - 1][y].myPIN != null) { //나중에 뜯어 고쳐야지 ㅡㅡ;
                        // if (m_ChessBoard.board[x - 1][y].myPIN.WB != WB)
                        if (CheckLocation(x - 1, y)) return true;
                    }

                    if (x < MAX) {
                        // if (m_ChessBoard.board[x + 1][y].myPIN != null) { // 선택된 pawn 앞칸이 비어있지 않고 앞칸이 적일 경우 .. 말이 안됨 고쳐야함
                        //  if (m_ChessBoard.board[x + 1][y].myPIN.WB != WB)
                        if (CheckLocation(x + 1, y)) return true;
                    }
                }
            }


        }
        return false;
    }

    public boolean CanMoveRook() { //가로 세로만 움직일 수 있음.
        int x = m_StartX; //일단 초기화
        int y = m_StartY;

        // for문을 2개 돌리는 것과 4개 돌리는거...
        // 어떤게 더 효율적일까? 엄청 조금이지만 complexity를 줄이는 걸로.. 복잡해보이지만

        if (x != m_DestX && y != m_DestY) return false; //rook은 x 또는 y 하나라도 목적지 좌표와 일치를 해야 함.
        else if (y == m_DestY) {
            for (int i = x - 1; i >= 1; i--) { //상식적으로 현재 위치는 받을 필요가 없잖아? 밑의 내용 클래스화 시키고 싶은데 불필요해서 안함.
                if (m_ChessBoard.board[i][y].myPIN != null) { //
                    if (m_ChessBoard.board[i][y].myPIN.WB == WB)
                        break;
                }
                if (i == m_DestX) return true;
            }
            for (int i = x + 1; i <= 8; i++) {
                if (m_ChessBoard.board[i][y].myPIN != null) {
                    if (m_ChessBoard.board[i][y].myPIN.WB == WB)
                        break;
                }
                if (i == m_DestX) return true;

            }
        } else {
            for (int i = y - 1; i >= 1; i--) {
                if (m_ChessBoard.board[x][i].myPIN != null) {
                    if (m_ChessBoard.board[x][i].myPIN.WB == WB)
                        break;
                }
                if (i == m_DestY) return true;

            }
            for (int i = y + 1; i <= 8; i++) {
                if (m_ChessBoard.board[x][i].myPIN != null) {
                    if (m_ChessBoard.board[x][i].myPIN.WB == WB)
                        break;
                }
                if (i == m_DestY) return true;
            }
        }
        return false;
    }

    //특징 좌우로 한칸 이후 대각선으로 한칸, 막히는 것이 없기 때문에 그냥 가도 됨.
    public boolean CanMoveKnight() {  //상대
        int x = m_StartX;
        int y = m_StartY;

        if (CheckLocation(x + 2, y - 1) || CheckLocation(x + 2, y + 1)) return true;
        if (CheckLocation(x - 2, y - 1) || CheckLocation(x - 2, y + 1)) return true;
        if (CheckLocation(x + 1, y + 2) || CheckLocation(x - 1, y + 2)) return true;
        if (CheckLocation(x + 1, y - 2) || CheckLocation(x - 1, y - 2)) return true;

        return false;
    }

    //재 활 용
    public boolean CanMoveBishop() { // 대각선으로만... 규칙성을 찾으면..

        int x = m_StartX;
        int y = m_StartY;

        if ((x + y) % 2 == (m_DestX + m_DestY) % 2) //결코 비숍끼리 만날 수 없다.  PGN이 틀리지 않는다는 가정에 시작.
            return true;

        return false;
    }

    //재활용.
    public boolean CanMoveKing() { //PGN파일이 틀리지 않을 경우에는 반드시 움직일 수 있다.
        return true;
    }

    public boolean CanMoveQueen() { //마찬가지로 PGN파일이 틀리지 않았다면.
        return true;
    }


}
