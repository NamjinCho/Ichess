package com.ichessprogrammer.chesseducate;

import java.util.ArrayList;

/**
 * Created by XNOTE on 2016-08-05.
 */
public class CheckMateAlgorithm {

    private ChessBoard mChessBoard;
    private boolean WB;

    static final int MAX = 8;
    static final int MIN = 1;

    public int kingX;
    public int kingY;

    public CheckMateAlgorithm() {
        mChessBoard = null;
    }
    public CheckMateAlgorithm(ChessBoard chessBoard, boolean WB) {

        try {
            mChessBoard = (ChessBoard) chessBoard.clone();
            this.WB = WB;
            kingX = 0;
            kingY = 0;
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

    }
    //효율성 문제 고려해야 할듯.
    //체크일 때에 이 함수 사용.
    public boolean isCheckMate() {

        Mask mask = new Mask();

        //8 * 8 * 8 * 8 = 2^12 = 4096 complexity. 큰건가?
        findKingLocation();

        for(int x = MIN; x <= MAX; x++) {
            for(int y = MIN; y <= MAX; y++) {
                if(mChessBoard.board[x][y].myPIN != null) {
                    if(mChessBoard.board[x][y].myPIN.WB == WB) {
                        mask = Logic.Path(mChessBoard.board[x][y].myPIN,mChessBoard, x, y);

                        //킹이 아무리 움직여도 생존할 수 없다면 true 생존한다면 false
                        if(!isKingIsNotSafe(mask, x, y)) {// 메소드 이름도 잘 지워야하는데 ㅜn
                            return false;
                        }
                    } // second if
                } // first if
            } // second for
        } //first for

        return true;
    }
    private void findKingLocation() { //킹 위치가 바뀌는거 빼고는 한번만 찾으면 되니까.
        for(int x = MIN; x <= MAX; x++) {
            for(int y = MIN; y <= MAX; y++) {
                if(mChessBoard.board[x][y].myPIN != null) {
                    if(mChessBoard.board[x][y].myPIN.WB == WB && mChessBoard.board[x][y].myPIN.Type.compareTo("K") == 0) {
                        kingX = x;
                        kingY = y;
                        return;
                    } // second if
                } // first if
            } // second for loop
        } //first for loop
    }

    private boolean isKingIsNotSafe(Mask mask, int cX, int cY) { //cX와 cY는 각각 current X, Y를 의미.
        ArrayList<Integer> list_x = mask.getList_X();
        ArrayList<Integer> list_y = mask.getList_Y();

        boolean check = true;

        for(int i = 0; i < list_x.size(); i++) {

            int x = list_x.get(i);
            int y = list_y.get(i);

            if(x == cX && y == cY)
                continue;
            PIN pin = mChessBoard.board[x][y].myPIN;
            mChessBoard.board[x][y].myPIN = mChessBoard.board[cX][cY].myPIN;
            mChessBoard.board[cX][cY].myPIN = null;

            MoveRange moveRange = new MoveRange(mChessBoard, WB);
            int tempX;
            if( mChessBoard.board[x][y].myPIN.Type.compareTo("K") == 0 && mChessBoard.board[x][y].myPIN.WB == WB) {
                moveRange.AddIndex("K",x,y);
                tempX = x;
            }
            else {
                moveRange.AddIndex("K", kingX, kingY);
                tempX = kingX;
            }

            if(moveRange.isSafe(tempX,tempX))  //왕이 안전하면
                check = false;
            //원상복귀시키자~
            mChessBoard.board[cX][cY].myPIN = mChessBoard.board[x][y].myPIN;
            mChessBoard.board[x][y].myPIN = pin;

            if(!check)
                return false;
        }
        return true;
    }
}

