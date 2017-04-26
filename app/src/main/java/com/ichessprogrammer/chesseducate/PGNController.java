package com.ichessprogrammer.chesseducate;

import android.util.Log;

import com.ichessprogrammer.chesseducate.PGN.PgnData;

import java.util.LinkedList;

/**
 * Created by XNOTE on 2016-07-15.
 */
public class PGNController {
    public ChessBoard mChessBoard;
    public String mData;
    public Boolean WB;
    DoOrder doOrder = new DoOrder();

    public int number = 0;
    public int loop = 0;
    public boolean isLoopEnd = false;





    public String turn;
    public LinkedList<PgnData> pgnDataList;


    public PGNController() {
        mChessBoard = null;
        mData = null;
        WB = true;
        turn = "";
        number  = 0;
        loop = 0;
        isLoopEnd = false;
    }

    public String readPGN_FEN(String fen , String pgn,Boolean WB) {
        ChessBoard chessBoard2 = new ChessBoard();
        chessBoard2.board = FEN.ReadFEN(fen);
        Log.d("디버깅",fen);
        chessBoard2=readPGN(chessBoard2,pgn,WB);
        String mFen = "";
        mFen=FEN.SetFEN(chessBoard2);
        return mFen;
    }
    public ChessBoard readPGN(ChessBoard chessBoard, String pgn, boolean WB, int number, int loop, boolean isLoopEnd) {
        this.number = number;
        this.loop = loop;
        this.isLoopEnd = isLoopEnd;
        this.WB = WB;
        mData = pgn;
        ChessBoard mChessBoard;

        try {
            mChessBoard = (ChessBoard)chessBoard.clone();
        } catch (CloneNotSupportedException e) {
            mChessBoard = chessBoard;
            e.printStackTrace();
        }

        if(chessBoard.game) {
            mChessBoard = doOrder.Ordering(chessBoard,pgn,WB);
        }
        return mChessBoard;
    }

    public ChessBoard readPGN(ChessBoard chessBoard , String pgn,Boolean WB) {

        DoOrder doOrder = new DoOrder();
        chessBoard = doOrder.Ordering(chessBoard, pgn, WB);

        return chessBoard;
    }
    /*
    public ChessBoard readPGN(ChessBoard chessBoard) {
        if (chessBoard.game) {
            DoOrder doOrder = new DoOrder();
            turn = "";
            //String data = mData;
            if (WB) {
                number++;
                //흰색 읽기
                startPoint = mData.indexOf(". ", index) + 2;
                endPoint = mData.indexOf(" ", startPoint);

                turn = mData.substring(startPoint, endPoint);
                chessBoard = doOrder.Ordering(chessBoard, turn, true);
            } else {
                startPoint = mData.indexOf(" ", endPoint) + 1;
                endPoint = mData.indexOf(" ", startPoint);

                turn = mData.substring(startPoint, endPoint);

                chessBoard = doOrder.Ordering(chessBoard, turn, false);
                index = endPoint;
            }
            if (WB)
                Log.i("Turn", "WHITE: " + turn);
            else
                Log.i("Turn", "Black: " + turn);

            WB = !WB;

        }
        return chessBoard;
    } */
}
