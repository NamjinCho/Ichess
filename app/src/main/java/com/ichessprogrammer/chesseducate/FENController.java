package com.ichessprogrammer.chesseducate;

import android.util.Log;

/**
 * Created by XNOTE on 2016-07-15.
 */
public class FENController {
    public ChessBoard mChessBoard;



    public FENController (ChessBoard chessBoard) {
        mChessBoard = chessBoard;
    }

    public FENController () {
       mChessBoard = null;
    }
    public ChessBoard readFEN() {
        mChessBoard.board = FEN.ReadFEN("rn2kb1r/ppp1pppp/8/8/4n1b1/6P1/PPPP1qBP/RNB3KR w kq - 6 11");
        mChessBoard.mState.Castling= FEN.ReadFEN_Castling("rn2kb1r/ppp1pppp/8/8/4n1b1/6P1/PPPP1qBP/RNB3KR w kq - 6 11");
        mChessBoard.mState.enPassant= FEN.ReadFEN_enPassant("rn2kb1r/ppp1pppp/8/8/4n1b1/6P1/PPPP1qBP/RNB3KR w kq - 6 11");
        mChessBoard.mState.turn= FEN.ReadFEN_Turn("rn2kb1r/ppp1pppp/8/8/4n1b1/6P1/PPPP1qBP/RNB3KR w kq - 6 11");
        mChessBoard.mState.numOfTurn_None= FEN.ReadFEN_NumOfTurnN("rn2kb1r/ppp1pppp/8/8/4n1b1/6P1/PPPP1qBP/RNB3KR w kq - 6 11");
        mChessBoard.mState.numOfTurn_Game= FEN.ReadFEN_NumOfTurnG("rn2kb1r/ppp1pppp/8/8/4n1b1/6P1/PPPP1qBP/RNB3KR w kq - 6 11");
        Log.d("FEN",mChessBoard.mState.turn+":"+mChessBoard.mState.enPassant+":"+mChessBoard.mState.Castling+":"+mChessBoard.mState.numOfTurn_None+":"
                +mChessBoard.mState.numOfTurn_Game);
        return  mChessBoard;
    }

}
