package com.ichessprogrammer.chesseducate;

import android.os.Bundle;

/**
 * Created by 남지니 on 2016-07-08.
 */
public class ChessBoard implements Cloneable {

    public Boolean game = true;
    public GameState mState= new GameState();
    public Board[][] board = new Board[9][9];
    public Bundle mapping = new Bundle();
    public boolean enPassant = false;
    public static boolean wKing = false;
    public static boolean bKing = false;
    public void GameSet() {
        game = !game;
    }
    public void EnableEnPassant() {
        enPassant = true;
    }

    public int number = 0;
    public int loop = 0;
    public boolean isLoopEnd = false;
    public boolean WB=true;
    public ChessBoard()
    {

        wKing=false;
        bKing=false;
        for(int i=1;i<=8;i++)
        {
            board[i] = new Board[9];
            for(int j=1;j<=8;j++)
            {
                board[i][j]=new Board();
            }
        }

    }

    public Object clone() throws CloneNotSupportedException {

        ChessBoard chessBoard = (ChessBoard)super.clone();
        chessBoard.board = new Board[9][9];

        for(int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                if(board[i][j] != null) {
                    chessBoard.board[i][j] = (Board) board[i][j].clone();
                }
            }
        }
        //chessBoard.board = this.board.clone();

        return chessBoard;
    }

    public int getXPosition(String key)
    {
        return mapping.getInt(key);
    }
    public void makeWhitePIN()
    {
        // init pieces
        PIN[]Rook =new PIN[2];
        PIN[]Bishop =new PIN[2];
        PIN[]Knight = new PIN[2];
        PIN King;
        PIN Queen;
        PIN[] Pawn = new PIN[8];

        for(int i = 1; i <= 8; i++) {
            Pawn[i-1]=new PIN();
            Pawn[i-1].x = i; Pawn[i-1].y = 2;
            board[Pawn[i-1].x][Pawn[i-1].y].myPIN = Pawn[i-1];
            Pawn[i-1].Type = "P";
            Pawn[i-1].WB = true;
        }
        for(int i = 0; i <2;i++)
        {
            Knight[i]=new PIN();
            Bishop[i]=new PIN();
            Rook[i]=new PIN();
            Knight[i].Type="N";
            Knight[i].WB= true;
            Bishop[i].Type="B";
            Bishop[i].WB= true;
            Rook[i].Type="R";
            Rook[i].WB= true;
        }
        King = new PIN();
        Queen = new PIN();
        King.Type="K";
        King.WB= true;
        Queen.Type="Q";
        Queen.WB= true;
        wKing =true;
        // init location
        Rook[0].x=0;Rook[1].x=8;
        Rook[0].y=0;Rook[1].y=1;
        Knight[0].x = 1;Knight[1].x = 7;
        Knight[0].y = 0;Knight[1].y = 1;
        Bishop[0].x=2;Bishop[1].x=6;
        Bishop[0].y=0;Bishop[1].y=1;
        King.x=5; King.y=1;
        Queen.x=4; Queen.y=1;

        board[2][1].myPIN = Knight[0];
        board[7][1].myPIN = Knight[1];
        board[3][1].myPIN = Bishop[0];
        board[6][1].myPIN = Bishop[1];
        board[1][1].myPIN = Rook[0];
        board[8][1].myPIN = Rook[1];
        board[5][1].myPIN = King;
        board[4][1].myPIN = Queen;

    }
    public void makeBlackPIN()
    {
        // init pieces
        PIN[]Rook =new PIN[2];
        PIN[]Bishop =new PIN[2];
        PIN[]Knight = new PIN[2];
        PIN King = new PIN();
        PIN Queen = new PIN();
        PIN[] Pawn = new PIN[8];
        for(int i = 1; i <= 8; i++) {
            Pawn[i-1]=new PIN();
            Pawn[i-1].x = i; Pawn[i-1].y = 7;
            board[Pawn[i-1].x][Pawn[i-1].y].myPIN = Pawn[i-1];
            Pawn[i-1].Type = "P";
            Pawn[i-1].WB = false;
        }
        for(int i = 0; i <2;i++)
        {
            Knight[i]=new PIN();
            Bishop[i]=new PIN();
            Rook[i]=new PIN();
            Knight[i].Type="N";
            Knight[i].WB= false;
            Bishop[i].Type="B";
            Bishop[i].WB= false;
            Rook[i].Type="R";
            Rook[i].WB= false;
        }
        // init location
        Rook[0].x=1;Rook[1].x=8;
        Rook[0].y=8;Rook[1].y=8;
        Knight[0].x = 2;Knight[1].x = 7;
        Knight[0].y = 8;Knight[1].y = 8;
        Bishop[0].x=3;Bishop[1].x=6;
        Bishop[0].y=8;Bishop[1].y=8;
        King.x=5; King.y=8;
        Queen.x=4; Queen.y=8;
        board[2][8].myPIN = Knight[0];
        board[7][8].myPIN = Knight[1];
        board[3][8].myPIN = Bishop[0];
        board[6][8].myPIN = Bishop[1];
        board[1][8].myPIN = Rook[0];
        board[8][8].myPIN = Rook[1];
        board[5][8].myPIN = King;
        board[4][8].myPIN = Queen;
        King.Type="K";
        King.WB= false;
        Queen.Type="Q";
        Queen.WB= false;
        bKing=true;
    }
}