package com.ichessprogrammer.chesseducate.lecture;

import com.ichessprogrammer.chesseducate.PIN;

/**
 * Created by XNOTE on 2016-07-09.
 */
public class Board implements Cloneable{
    com.ichessprogrammer.chesseducate.PIN myPIN;
    boolean move;

    Board() {
        myPIN = null;
        move = false;
    }
    public Object clone() throws CloneNotSupportedException {
        com.ichessprogrammer.chesseducate.Board board = (com.ichessprogrammer.chesseducate.Board)super.clone();

        if(myPIN != null)
            board.myPIN = (PIN)myPIN.clone();

        return board;
    }
}