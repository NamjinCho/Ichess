package com.ichessprogrammer.chesseducate;

/**
 * Created by 남지니 on 2016-07-11.
 */
public class FEN {

    public static final String WN = "PNBQKR";
    public static final String BN = "pnbqkr";
    public static final String EMPTY = "12345678";
    public static final int MAX_OF_ROW = 8;
    public static final int MAX_OF_COL = 8;
    public static final int NONE = -1;
    public static String FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
    public static Board[][] myBoard = new Board[9][9];

    public static Board[][] ReadFEN(String fen) {
        int row = 8;
        int col;
        for (int i = 0; i <= MAX_OF_ROW; i++) {
            myBoard[i] = new Board[MAX_OF_COL + 1];
            for (int j = 0; j <= MAX_OF_COL; j++) {
                myBoard[i][j] = new Board();
            }
        }
        String[] dividedFEN = fen.split("/");
        String temp = dividedFEN[dividedFEN.length - 1];
        int end = temp.indexOf(" ");
        if (end != -1)
            dividedFEN[dividedFEN.length - 1] = temp.substring(0, end);
        for (int i = 0; i < MAX_OF_ROW; i++) {
            int Length = dividedFEN[i].length();
            col = 1;
            for (int j = 0; j < Length; j++) {
                String piece = "" + dividedFEN[i].charAt(j);
                if (WN.contains(piece)) {
                    // System.out.println(piece + " : " +WN.contains(piece) );
                    PIN pin = generatePiece(row, col, true, piece);
                    myBoard[col][row].myPIN = pin;
                    myBoard[col][row].myPIN.move=pin.move;

                    col++;
                } else if (BN.contains(piece)) {
                    PIN pin = generatePiece(row, col, false, piece);
                    myBoard[col][row].myPIN = pin;
                    myBoard[col][row].myPIN.move=pin.move;
                    col++;
                } else if (EMPTY.contains(piece)) {
                    System.out.println("number: " + piece);
                    col = col + Integer.parseInt(piece);

                } else {
                    System.out.println("ERROR");
                    //exit();
                }
            }
            row--;
        }
        return myBoard;
    }

    public static String SetFEN(ChessBoard chessBoard) {
        String result = "";
        int count = 0;
        for (int i = 8; i > 0; i--) {
            for (int j = 1; j < 9; j++) {
                if (chessBoard.board[j][i].myPIN != null) {
                    if (count != 0)
                        result += count;
                    if (chessBoard.board[j][i].myPIN.WB) {
                        result += chessBoard.board[j][i].myPIN.Type;
                    } else
                        result += chessBoard.board[j][i].myPIN.Type.toLowerCase();
                    count = 0;
                } else
                    count++;
            }
            if (count == 0)
                result += "/";
            else {
                result = result + count + "/";
                count = 0;
            }
        }
        return result;
    }

    static PIN generatePiece(int row, int col, boolean WB, String Type) {
        //boolean wb ="";
        String mType = Type.toUpperCase();
        PIN pin = new PIN();
        pin.move = false;
        if (mType.equals("P")) {
            mType = "P";
            if (WB && row != 2) {
                pin.move = true;
            } else if (!WB && row != 7) {
                pin.move = true;
            }
        } else if (mType.equals("K")) {
            mType = "K";
            if(WB)
                ChessBoard.wKing = true;
            if(!WB)
                ChessBoard.bKing = true;
            if (WB && (row != 1 && col != 5)) {
                pin.move = true;
            } else if (!WB && (row != 8 && col != 5)) {
                pin.move = true;
            }
        } else if (mType.equals("Q")) mType = "Q";
        else if (mType.equals("N")) mType = "N";
        else if (mType.equals("R")) {
            mType = "R";
            if (WB && (row != 1 && (col != 1 || col != 8))) {
                pin.move = true;
            } else if (!WB && (row != 8 && (col != 1 || col != 8))) {
                pin.move = true;
            }
        } else if (mType.equals("B")) mType = "B";
        pin.Type = mType;
        pin.WB = WB;
        pin.x = col;
        pin.y = row;
        //  System.out.println(row +" : " + col + " : " + WB + " : "+mType);
        return pin;
    }

    static public boolean ReadFEN_Turn(String fen) {
        String[] subfen = fen.split("/");
        String Temp;
        String[] temp = subfen[subfen.length - 1].split(" ");
        if (temp.length > 1) {
            Temp = temp[1].toUpperCase();
            if (Temp.equals("W"))
                return true;
            else return false;
        }
        return true;
    }

    static public String ReadFEN_Castling(String fen) {
        String[] subfen = fen.split("/");
        String Temp;
        String[] temp = subfen[subfen.length - 1].split(" ");
        if (temp.length > 2) {
            Temp = temp[2];
            return Temp;
        }
        //
        return null;
    }

    static public String ReadFEN_enPassant(String fen) {
        String[] subfen = fen.split("/");
        String Temp;
        String[] temp = subfen[subfen.length - 1].split(" ");
        if (temp.length > 3) {
            Temp = temp[3].toUpperCase();
            return Temp;
        }
        return null;
    }

    static public int ReadFEN_NumOfTurnN(String fen) {
        String[] subfen = fen.split("/");
        String[] temp = subfen[subfen.length - 1].split(" ");
        if (temp.length > 4) {
            return Integer.parseInt(temp[4]);
        }
        return 0;
    }

    static public int ReadFEN_NumOfTurnG(String fen) {
        String[] subfen = fen.split("/");
        String[] temp = subfen[subfen.length - 1].split(" ");
        if (temp.length > 5) {
            return Integer.parseInt(temp[5]);
        }
        return 0;
    }
}