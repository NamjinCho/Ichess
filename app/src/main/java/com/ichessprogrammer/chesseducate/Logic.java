package com.ichessprogrammer.chesseducate;

/**
 * Created by 남지니 on 2016-07-13.
 */
public class Logic {

    public static ChessBoard mChessBoard;
    public static Mask mMask;
    public static MoveRange moveRange;

    public static String type;
    public static boolean WB;


    static public Mask Path(PIN pin, ChessBoard chessBoard, int x, int y) {
        mChessBoard = chessBoard;
        mMask = new Mask();

        type = pin.Type; //알아내고,
        WB = pin.WB; // 알아냈다면,

        moveRange = new MoveRange(chessBoard, x, y, type, WB, mMask);
        mMask = moveRange.CheckRange();

        return mMask;
    }
}

    /*
    static private void PAWN(PIN pin, int x, int y) {
        //White path
        if (pin.WB) {
            //흰색 우측 대각선
            if ((x + 1 < 9 && y + 1 < 9) && mBoard[x+1][y + 1].myPIN != null && (x + 1 < 8 && y + 1 < 8) && !mBoard[x + 1][y + 1].myPIN.WB) {
                mMask.x[mMask.xCount++] = (x-1) + 1;
                mMask.y[mMask.yCount++] = 8-(y-1+1);
                Log.d("tag2","우측대각선");
            }
            //흰색 좌측 대각선
            if ((y + 1 < 8 && x -1  >0) && mBoard[x - 1][y + 1].myPIN != null && ((y + 1 < 9 && x -1 > 0) && !mBoard[x - 1][y + 1].myPIN.WB)) {
                mMask.x[mMask.xCount++] = (x-1) - 1;
                mMask.y[mMask.yCount++] = 8-(y-1 + 1);
                Log.d("tag2","좌측대각선");
            }
            //바로위에 말이 없을때
            if (y+1<9&&mBoard[x][y+1].myPIN == null) {
                mMask.x[mMask.xCount++] = (x-1);
                mMask.y[mMask.yCount++] = 8-(y+1);
                Log.d("tag2","바로위");
                //흰색 폰 한번도 안움직이고 바로위위까지 없을때
                if ((!mBoard[x][y].myPIN.move)&&mBoard[x][y+2].myPIN == null) {
                    mMask.x[mMask.xCount++] = x-1;
                    mMask.y[mMask.yCount++] = 8-(y+ 2);
                    Log.d("tag2","바로위위");
                }
            }
            Log.d("tag",""+mMask.xCount +""+mMask.yCount);
        }
        /*
        //Black path
        if (pin.WB == "B") {
            //흑색 우측 대각선
            if ((row -1 > 0 && col + 1 < 9) && mBoard[row + 1][col + 1].myPIN != null &&
                    (row + 1 < 8 && col + 1 < 9) && mBoard[row + 1][col + 1].myPIN.WB.equals("W")) {
                mMask.x[mMask.xCount++] = col + 1;
                mMask.y[mMask.yCount++] = row + 1;
            }
            //흑색 좌측 대각선
            if ((row + 1 < 8 && col -1 > 0) && mBoard[row + 1][col - 1].myPIN != null &&
                    ((row + 1 < 8 && col -1 > 0) && mBoard[row + 1][col - 1].myPIN.WB.equals("W"))) {
                mMask.x[mMask.xCount++] = col - 1;
                mMask.y[mMask.yCount++] = row + 1;
            }
            //바로위에 말이 없을때
            if (row-1>0&&mBoard[row -1][col].myPIN != null) {
                mMask.x[mMask.xCount++] = col;
                mMask.y[mMask.yCount++] = row + 1;
                //흑색 폰 한번도 안움직이고 바로위위까지 없을때
                if (mBoard[row][col].myPIN.move&&(mBoard[row -2][col].myPIN.move&&mBoard[row -2][col].myPIN == null)) {
                    mMask.x[mMask.xCount++] = col;
                    mMask.y[mMask.yCount++] = row + 2;
                }
            }
        }
        */
