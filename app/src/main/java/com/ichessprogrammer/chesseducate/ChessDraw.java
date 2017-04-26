package com.ichessprogrammer.chesseducate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by 남지니 on 2016-07-09.
 */
public class ChessDraw extends View {

    final int MAX = 8;
    final int MIN = 1;

    ChessBoard mChessBoard;
    int mWidth;
    int mHeight;
    Context mContext;

    //일단 public으로 ...
    String pgn;
    String promotionPgn = "";


    Boolean WB;
    Boolean promotion = false;
    Boolean reversePlayer = false;

    int currentX;
    int currentY;

    int row;
    int col;
    float x;
    float y;
    Mask mask;

    public ChessDraw(Context context) {
        super(context);
        mContext = context;
        //mWidth = (int)(getWidth()*0.7);
        mask = new Mask();
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        x = event.getX();
        y = event.getY();

        pgn = "";

        PGNMaker pgnMaker = new PGNMaker(mChessBoard);

        int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:


                row = (int) (y / (getHeight() / 8));
                col = (int) (x / (getWidth() / 8));
                row = 7 - row;
                Log.d("what?", row + ":" + col);
                ArrayList<Integer> xList = mask.getList_X();
                ArrayList<Integer> yList = mask.getList_Y();
                for (int i = 0; i < xList.size(); i++) {

                    int tempX = xList.get(i) - 1;
                    int tempY = yList.get(i) - 1;
                    if (currentX == col && currentY == row)
                        continue;

                    if (tempX == col && tempY == row) { //이동이동.

                        //TODO 체크이면 움직이지 못하게 하기.

                        PIN tempPin = mChessBoard.board[col + 1][row + 1].myPIN;
                        mChessBoard.board[col + 1][row + 1].myPIN = mChessBoard.board[currentX + 1][currentY + 1].myPIN;
                        mChessBoard.board[currentX + 1][currentY + 1].myPIN = null;

                        boolean check = false;
                        boolean checkKing = false;
                        for (int j = 1; j <= 8; j++) {
                            if(checkKing)
                                break;
                            for (int k = 1; k <= 8; k++) {
                                if (mChessBoard.board[j][k].myPIN != null) {
                                    if (mChessBoard.board[j][k].myPIN.Type.compareTo("K") == 0 && mChessBoard.board[j][k].myPIN.WB == WB) {
                                        MoveRange moveRange = new MoveRange(mChessBoard, WB);
                                        moveRange.AddIndex("K", j, k);
                                        if (!moveRange.isSafe(j, j)) {
                                            Toast.makeText(getContext(), "Check", Toast.LENGTH_SHORT).show();
                                            check = true;
                                        }
                                        mChessBoard.board[currentX + 1][currentY + 1].myPIN = mChessBoard.board[col + 1][row + 1].myPIN;
                                        mChessBoard.board[col + 1][row + 1].myPIN = tempPin;
                                        checkKing = true;
                                        break;
                                    }
                                }
                            }
                        }

                        if(check) {
                            invalidate();
                            return true;
                        }

                        //pawn 앙파상 수정
                        if (mChessBoard.board[col + 1][row + 1].myPIN == null) { // 목적지에  아무도 없으면 이동.

                            pgn = pgnMaker.Move(currentX + 1, currentY + 1, col + 1, row + 1);
                            if (mChessBoard.board[currentX + 1][currentY + 1].myPIN.Type.compareTo("P") == 0) {
                                if (mChessBoard.board[col + 1][currentY + 1].myPIN != null)
                                    if (mChessBoard.board[col + 1][currentY + 1].myPIN.enPassantEnable) {
                                        pgn = pgnMaker.Attack(currentX + 1, currentY + 1, col + 1, row + 1);
                                        mChessBoard.board[col + 1][currentY + 1].myPIN = null;
                                    }
                            }
                            //이동.
                        } else {
                            pgn = pgnMaker.Attack(currentX + 1, currentY + 1, col + 1, row + 1); // 공격.
                        }

                        mChessBoard.board[col + 1][row + 1].myPIN = mChessBoard.board[currentX + 1][currentY + 1].myPIN;
                        mChessBoard.board[currentX + 1][currentY + 1].myPIN = null;

                        if (mChessBoard.board[col + 1][row + 1].myPIN.Type.compareTo("K") == 0) { //왕 일때,
                            if (currentX - col == -2) {//킹 캐슬링
                                pgn = pgnMaker.sideCastling(true);
                                mChessBoard.board[6][row + 1].myPIN = mChessBoard.board[8][row + 1].myPIN;
                                mChessBoard.board[6][row + 1].myPIN.move = true;
                                mChessBoard.board[8][row + 1].myPIN = null;
                            } else if (currentX - col == 2) { //퀸 캐슬링
                                pgn = pgnMaker.sideCastling(false);
                                mChessBoard.board[4][row + 1].myPIN = mChessBoard.board[1][row + 1].myPIN;
                                mChessBoard.board[4][row + 1].myPIN.move = true;
                                mChessBoard.board[1][row + 1].myPIN = null;
                            }
                        } else if (mChessBoard.board[col + 1][row + 1].myPIN.Type.compareTo("P") == 0) {//Pawn은 할게 많아요~


                            //TODO
                            if (row + 1 == 8 || row + 1 == 1) {
                                Intent intent = new Intent("main");
                                intent.putExtra("tag","Promotion");
                                mContext.sendBroadcast(intent);

                                promotionPgn = pgn;
                                pgn = "";
                                promotion =true;
                            }


                            if (currentY - row == 2 || currentY - row == -2) {
                                if (!mChessBoard.board[col + 1][row + 1].myPIN.move) { // 처음으로 이동할 때에
                                    mChessBoard.board[col + 1][row + 1].myPIN.enPassantEnable = true;
                                    mChessBoard.enPassant = true;

                                    mask = new Mask();
                                    WB = !WB;

                                    mChessBoard.board[currentX + 1][currentY + 1].myPIN = null;

                                    for (int j = 1; j <= 8; j++) {
                                        for (int k = 1; k <= 8; k++) {
                                            if (j != col + 1 || k != row + 1) {
                                                if (mChessBoard.board[j][k].myPIN != null) {
                                                    mChessBoard.board[j][k].myPIN.enPassantEnable = false;

                                                    if(!promotion) {
                                                        if (mChessBoard.board[j][k].myPIN.Type.compareTo("K") == 0 && mChessBoard.board[j][k].myPIN.WB == WB) {
                                                            MoveRange moveRange = new MoveRange(mChessBoard, WB);
                                                            moveRange.AddIndex("K", j, k);
                                                            if (!moveRange.isSafe(j, j)) {
                                                                CheckMateAlgorithm checkMateAlgorithm = new CheckMateAlgorithm(mChessBoard, WB);
                                                                if (checkMateAlgorithm.isCheckMate()) {
                                                                    pgn = pgn + "#";
                                                                } else
                                                                    pgn = pgn + "+";
                                                            }
                                                        }
                                                    }

                                                }
                                            }
                                        }
                                    }

                                    Log.d("PGN", pgn);
                                    Board_Fragment br = new Board_Fragment();

                                    if(!promotion)
                                        sendBroadCastReceiver(pgn);

                                    mChessBoard.board[col + 1][row + 1].myPIN.move = true;
                                    invalidate();
                                    return true;
                                }
                            } /*else if (mChessBoard.board[col + 1][currentY + 1].myPIN != null) { //앙파상
                                if (mChessBoard.board[col + 1][currentY + 1].myPIN.enPassantEnable) {
                                    pgn = pgnMaker.Attack(currentX + 1, currentY + 1, col + 1, row + 1);
                                    mChessBoard.board[col + 1][currentY + 1].myPIN = null;
                                }
                            }*/
                        }
                        WB = !WB;
                        for (int j = 1; j <= 8; j++) {
                            for (int k = 1; k <= 8; k++) {
                                if (mChessBoard.board[j][k].myPIN != null) {
                                    mChessBoard.board[j][k].myPIN.enPassantEnable = false;

                                    if(!promotion) {
                                        if (mChessBoard.board[j][k].myPIN.Type.compareTo("K") == 0 && mChessBoard.board[j][k].myPIN.WB == WB) {
                                            MoveRange moveRange = new MoveRange(mChessBoard, WB);
                                            moveRange.AddIndex("K", j, k);
                                            if (!moveRange.isSafe(j, j)) {
                                                CheckMateAlgorithm checkMateAlgorithm = new CheckMateAlgorithm(mChessBoard, WB);
                                                if (checkMateAlgorithm.isCheckMate()) {
                                                    pgn = pgn + "#";
                                                } else
                                                    pgn = pgn + "+";
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        mChessBoard.board[col + 1][row + 1].myPIN.move = true;
                        mChessBoard.enPassant = false;
                        mask = new Mask();


                        Log.d("PGN", pgn);
                        if(!promotion)
                            sendBroadCastReceiver(pgn);

                        invalidate();
                        return true;
                    }
                }
                //mask = new Mask();
                if (mChessBoard.board[col + 1][row + 1].myPIN != null) {
                    if (mChessBoard.board[col + 1][row + 1].myPIN.WB == WB) {
                        Log.d("Piece", mChessBoard.board[col + 1][row + 1].myPIN.Type + "(" + mChessBoard.board[col + 1][row + 1].myPIN.WB + ")");
                        mask = Logic.Path(mChessBoard.board[col + 1][row + 1].myPIN, mChessBoard, col + 1, row + 1);
                        currentX = col;
                        currentY = row;
                        invalidate();
                    }
                } else {
                    mask = new Mask();
                    invalidate();
                }
        }
        return true;
    }

    public void readChessBoard(ChessBoard chessBoard) {
        mChessBoard = chessBoard;
        WB = mChessBoard.WB;
        mask = new Mask();
        invalidate();
    }
    public void reverseDraw() {
        reversePlayer = !reversePlayer;
        invalidate();
    }

    public void readChessBoard(ChessBoard chessBoard, String type) {
        mChessBoard = chessBoard;
        WB = mChessBoard.WB;
        pgn = promotionPgn;
        promotionPgn = "";

        PGNMaker pgnMaker = new PGNMaker();

        pgn = pgn + pgnMaker.Promotion(type) ;
        for (int j = 1; j <= 8; j++) {
            for (int k = 1; k <= 8; k++) {
                if (mChessBoard.board[j][k].myPIN != null) {
                    if (mChessBoard.board[j][k].myPIN.Type.compareTo("K") == 0 && mChessBoard.board[j][k].myPIN.WB == WB) {
                        MoveRange moveRange = new MoveRange(mChessBoard, WB);
                        moveRange.AddIndex("K", j, k);
                        if (!moveRange.isSafe(j, j)){
                            CheckMateAlgorithm checkMateAlgorithm = new CheckMateAlgorithm(mChessBoard,WB);
                            if(checkMateAlgorithm.isCheckMate()) {
                                pgn = pgn + "#";
                            }
                            else
                                pgn = pgn + "+";

                        }
                    }
                }
            }
        }
        promotion = false;
        Log.d("PGN", pgn);
        sendBroadCastReceiver(pgn);
        mask = new Mask();
        invalidate();
    }


    public void sendBroadCastReceiver(String p) {
        Intent intent = new Intent("makePgn");
        Bundle bundle = new Bundle();

        intent.putExtra("pgn","makePgn");
        Log.i("안녕","안녕1");

        bundle.putString("pgnData",p);
        bundle.putInt("number",mChessBoard.number);
        bundle.putInt("loop",mChessBoard.loop);
        bundle.putBoolean("loopEnd",mChessBoard.isLoopEnd);
        bundle.putBoolean("WB", WB);
        intent.putExtras(bundle);
        mContext.sendBroadcast(intent);

    }


    public void init() {
        mChessBoard = new ChessBoard();


        WB = true;
        currentX = -1;
        currentY = -1;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //보드판 그리기
        drawBoard(canvas);
        drawPiece(canvas);
        drawCoordination(canvas);
    }

    //보드판 그리는 함수
    protected void drawBoard(Canvas canvas) {
        float width = getWidth() / 8;
        float height = getHeight() / 8;
        boolean WBflag = true;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Paint Pnt = new Paint();
                if (WBflag) {
                    Pnt.setColor(Color.YELLOW);
                    canvas.drawRect(j * width, i * height, (j + 1) * width, (i + 1) * height, Pnt);//왼쪽 위 오른쪽 아래
                } else {
                    Pnt.setColor(Color.GREEN);
                    canvas.drawRect(j * width, i * height, (j + 1) * width, (i + 1) * height, Pnt);

                }
                WBflag = !WBflag;
            }
            WBflag = !WBflag;
        }

        ArrayList<Integer> x = mask.getList_X();
        ArrayList<Integer> y = mask.getList_Y();

        for (int i = 0; i < x.size(); i++) {
            Paint Pnt = new Paint();
            Pnt.setColor(Color.RED);

            int tempX = x.get(i) - 1;
            int tempY = 8 - y.get(i);
            canvas.drawRect(tempX * width, tempY * height, (tempX + 1) * width, (tempY + 1) * height, Pnt);
        }
        Paint Pnt = new Paint();
        Pnt.setColor(Color.rgb(245, 34, 214));
        if (mask.mX != -1) {
            int pickedX = mask.mX - 1;
            int pickedY = 8 - mask.mY;
            canvas.drawRect(pickedX * width, pickedY * height, (pickedX + 1) * width, (pickedY + 1) * height, Pnt);
        }
    }

    protected void drawPiece(Canvas canvas) {

        final int MAX = 8;
        final int MIN = 1;


        int fromX;
        int toX;
        int fromY;
        int toY;


        float width = getWidth() / 8;
        float height = getHeight() / 8;


        DrawPin drawPin = new DrawPin();
        for (int x = MIN; x <= MAX; x++) {
            for (int y = MIN; y <= MAX; y++) {
                if (mChessBoard.board[x][y].myPIN != null) {
                    PIN p = mChessBoard.board[x][y].myPIN;
                    int pinId = drawPin.findPin(p.Type, p.WB);
                    Bitmap sm = BitmapFactory.decodeResource(getResources(), pinId);


                    if(!reversePlayer) {
                        fromX = (int)((x-1) * width);
                        fromY = (int)((MAX -y) * height);
                        toX = (int)((x) * width);
                        toY = (int)((MAX -(y-1)) * height);

                    } else {
                        fromX = (int)((MAX - (x)) * width);
                        fromY = (int)((y -1) * height);
                        toX = (int)((MAX - (x-1)) * width);
                        toY = (int)((y) * height);
                    }

                    canvas.drawBitmap(sm, null, new Rect(fromX, fromY, toX, toY), null);
                }
            }
        }
    }

    public void drawCoordination(Canvas canvas) {
        float width = getWidth() / 8;
        float height = getHeight() / 8;

        String x;
        int y;
        CoordinateFIltering c = new CoordinateFIltering();
        Paint Pnt = new Paint();
        Pnt.setTextSize(24);
        Pnt.setColor(Color.BLUE);
        for(int i = MIN; i <= MAX; i++) {
            for(int j = MIN; j <= MAX; j++) {
                if(!reversePlayer) {
                    x = c.getCoordinate(j);
                    y = MAX - (i - 1);
                }
                else {
                    x = c.getCoordinate(MAX - (j-1));
                    y = i;
                }
                x = x + y;
                canvas.drawText(x,  (j-1) * width, i * height, Pnt);
            }
        }
    }
}
