package com.ichessprogrammer.chesseducate;

/**
 * Created by XNOTE on 2016-07-09.
 */
public class Board implements Cloneable{
    public PIN myPIN;
    public boolean move;

    public Board() {
        myPIN = null;
        move = false;
    }
    public Object clone() throws CloneNotSupportedException {
        Board board = (Board)super.clone();

        if(myPIN != null)
            board.myPIN = (PIN)myPIN.clone();

        return board;
    }
}