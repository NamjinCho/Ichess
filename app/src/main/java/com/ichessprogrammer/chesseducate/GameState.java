package com.ichessprogrammer.chesseducate;

/**
 * Created by 남지니 on 2016-07-13.
 */
public class GameState { // 고침
    public String enPassant;
    public String Castling;
    public int numOfTurn_None;
    public int numOfTurn_Game;
    public boolean turn;
    public GameState()
    {
        numOfTurn_Game=0;
        numOfTurn_None=0;
        turn =true;
    }
}
