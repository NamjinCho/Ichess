package com.ichessprogrammer.chesseducate;

/**
 * Created by XNOTE on 2016-07-23.
 */
public class PGNIndex {
    public ChessBoard mChessBoard;


    /*New Version*/
    public String mOrder; //pgnData
    private String mExplain;
    private String mark;

    public int index;
    public boolean WB;
    public int loop;

    //private boolean isMask = false;
    //private String maskText;

    public boolean isLoopEnd;


    public PGNIndex(ChessBoard chessBoard) throws CloneNotSupportedException {
        mChessBoard = (ChessBoard)chessBoard.clone();
    }
    public void addOrder(String order) {
        mOrder = order;
    }
    public void addWB(boolean WB) {
        this.WB = WB;
    }
    public void addIndex(int index) {
        this.index = index;
    }

    /*new version*/
    public void setExplain(String s) {
        mExplain = s;
    }
    public void setMark(String s) {
        mark = s;
    }

    public String getExplain() {
        return  mExplain;
    }
    public String getMark() {
        return  mark;
    }



    public void addLoop(int loop) {
        this.loop = loop;
    }
    public void addLoopEnd(boolean b) {
        isLoopEnd = b;
    }


    public PGNIndex (ChessBoard chessBoard, String order, int index, boolean WB) {
        mOrder = order;
        mChessBoard = chessBoard;
        this.index = index;
        this.WB = WB;
    }

}
