package com.ichessprogrammer.chesseducate.PGN;
import com.ichessprogrammer.chesseducate.ChessBoard;

/**
 * Created by XNOTE on 2016-08-29.
 */
public class HinCoordinateFiltering {

    final int MAX2 = 64;
    final int MIN = 1;
    final int SIZE = 8;

    final int MOVE = 100;
    final int ATTACK = 200;
    final int CASTLING = 300;


    private ChessBoard from;
    private ChessBoard to;

    private boolean WB;
    private String pgn = "";

    private int fromX = 0;
    private int fromY = 0;
    private int toX = 0;
    private int toY = 0;

    private boolean hint = false;

    public HinCoordinateFiltering() {
        from = null;
        to = null;
        //calculateCoordinate();
    }

    public boolean isThereHint() {
        return hint;
    }

    public HinCoordinateFiltering(ChessBoard f, ChessBoard t, String p, boolean WB) {

        try {
            from = (ChessBoard) f.clone();
            to = (ChessBoard) t.clone();
        } catch (CloneNotSupportedException e) {
            from = f;
            to = t;
            e.printStackTrace();
        }
        this.WB = WB;
        pgn = p;
        calculateCoordinate();
    }

    private void calculateCoordinate() { //doOrder.ordering

        int order = -1;

        if (pgn.contains("x")) //공격은 좋은데, 앙파상 일때를 생각해보자.
            order = ATTACK;
        else if (pgn.contains("O-O"))
            order = CASTLING;
        else
            order = MOVE;

        if (searching(order, true)) {
            if (searching(order, false)) {
                hint = true;

            }
        } else
            hint = false;
    }

    public int getFromX() {
        return fromX;
    }

    public int getFromY() {
        return fromY;
    }

    public int getToX() {
        return toX;
    }

    public int getToY() {
        return toY;
    }


    //coordinate


    private boolean getCoordinate(int x, int y, boolean isFrom, int order) {

        if (isFrom) {

            if (from.board[x][y].myPIN != null && to.board[x][y].myPIN == null) {
                if(order == CASTLING && !from.board[x][y].myPIN.Type.contains("K")) {
                    return false;
                }
                if(from.board[x][y].myPIN.WB != WB) {
                    return false;
                }
                fromX = x;
                fromY = y;

                return true;

            }
            return false;
        } else {
            switch (order) {
                case ATTACK:
                    if (from.board[x][y].myPIN != null && to.board[x][y].myPIN != null) {
                        if (from.board[x][y].myPIN.WB != to.board[x][y].myPIN.WB) {
                            toX = x;
                            toY = y;

                            return true;
                        }
                    }
                    else if(from.board[x][y].myPIN == null && to.board[x][y].myPIN != null) { //앙파상 일때...
                        toX = x;
                        toY = y;
                        return true;
                    }
                    return false;
                case MOVE:
                    if (from.board[x][y].myPIN == null && to.board[x][y].myPIN != null) {
                        toX = x;
                        toY = y;
                        return true;
                    }
                    return false;
                case CASTLING:
                    if (from.board[x][y].myPIN == null && to.board[x][y].myPIN != null) {
                        if (to.board[x][y].myPIN.Type.contains("K")) {
                            toX = x;
                            toY = y;
                            return true;
                        }
                    }
                    return false;

            }
            return false;
        }


    }

    private boolean searching(int order, boolean b) {


        for (int i = 0; i < MAX2; i++) {
            int y = MIN + (i % SIZE);
            int x = MIN + (i / SIZE);
            if (getCoordinate(x, y, b, order))
                return true;
        }
        return false;
    }

}