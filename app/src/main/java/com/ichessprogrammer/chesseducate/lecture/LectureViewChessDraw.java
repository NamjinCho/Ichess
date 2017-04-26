package com.ichessprogrammer.chesseducate.lecture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ichessprogrammer.chesseducate.ChessBoard;
import com.ichessprogrammer.chesseducate.ChessPieaceIcon;
import com.ichessprogrammer.chesseducate.Mask;
import com.ichessprogrammer.chesseducate.R;

import java.util.ArrayList;

/**
 * Created by 남지니 on 2016-07-09.
 */
public class LectureViewChessDraw extends SurfaceView implements SurfaceHolder.Callback {
    ChessBoard mChessBoard;
    Mask mask;
    Context mContext;
    SurfaceHolder holder = getHolder();
    boolean surfaceFlag = false;
    public ArrayList<BoardMark> markList = new ArrayList<>();
    boolean movingFlag=true;
    public LectureViewChessDraw(Context context) {
        super(context);
        //mWidth = (int)(getWidth()*0.7);
        mask = new Mask();
        mContext = context;
        holder.addCallback(this);
        init();
    }
    public void readChessBoard(ChessBoard chessBoard) {
        mChessBoard = chessBoard;
        invalidate();
    }
    public void init() {
    }
    public ChessBoard getmChessBoard() throws CloneNotSupportedException {
        return (ChessBoard) mChessBoard.clone();
    }
    public void invalidate()
    {
        super.invalidate();
        doDraw();
    }
    protected void doDraw() {
        if(surfaceFlag) {
         Thread thread = new Thread(new Runnable() {
             @Override
             public void run() {
                 Canvas canvas = holder.lockCanvas(null);
                 drawBoard(canvas);
                 drawMarks(canvas);
                 drawPiece(canvas);
                 holder.unlockCanvasAndPost(canvas);
             }
         });
            thread.start();
        }
    }

    //보드판 그리는 함수
    protected void drawBoard(Canvas canvas) {
        float width = getWidth() / 8;
        float heigth = getHeight() / 8;
        boolean WBflag = true;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (WBflag) {
                    Bitmap sm = BitmapFactory.decodeResource(getResources(), ChessPieaceIcon.img2[ChessPieaceIcon.boardImgPosition][0]);
                    canvas.drawBitmap(sm, null, new Rect((int) (j * width), (int) (i * heigth), (int) ((j + 1) * width), (int) ((i + 1) * heigth)), null);
                    sm.recycle();
                } else {
                    Bitmap sm = BitmapFactory.decodeResource(getResources(), ChessPieaceIcon.img2[ChessPieaceIcon.boardImgPosition][1]);
                    canvas.drawBitmap(sm, null, new Rect((int) (j * width), (int) (i * heigth), (int) ((j + 1) * width), (int) ((i + 1) * heigth)), null);
                    sm.recycle();
                }
                WBflag = !WBflag;
            }
            WBflag = !WBflag;
        }
    }

    protected void drawMarks(Canvas canvas)
    {
        if(markList!=null)
        {
            float width = getWidth() / 8;
            float heigth = getHeight() / 8;
            for(int i=0;i<markList.size();i++)
            {
                Bitmap sm;
                int row = markList.get(i).row;
                int col = markList.get(i).col;
                if (markList.get(i).pos==1) {
                    sm = BitmapFactory.decodeResource(getResources(), R.drawable.circle_icon);
                    canvas.drawBitmap(sm, null, new Rect((int) (col * width), (int) ((7 - row) * heigth), (int) ((col + 1) * width), (int) ((7 - row + 1) * heigth)), null);
                    sm.recycle();
                }
                if (markList.get(i).pos==2) {
                    sm = BitmapFactory.decodeResource(getResources(), R.drawable.x_icon);
                    canvas.drawBitmap(sm, null, new Rect((int) (col * width), (int) ((7 - row) * heigth), (int) ((col + 1) * width), (int) ((7 - row + 1) * heigth)), null);
                    sm.recycle();
                }
                Paint paint = new Paint();
                if (markList.get(i).pos==3) {//빨간색 큐브
                    paint.setColor(Color.RED);
                    canvas.drawRect((col * width), ((7 - row) * heigth), ((col + 1) * width), ((7 - row + 1) * heigth),paint);
                }
                if (markList.get(i).pos==4) {//파란색 큐브
                    paint.setColor(Color.BLUE);
                    canvas.drawRect((col * width), ((7 - row) * heigth), ((col + 1) * width), ((7 - row + 1) * heigth),paint);
                }
                if (markList.get(i).pos==5) {//초록색 큐브
                    paint.setColor(Color.GREEN);
                    canvas.drawRect((col * width), ((7 - row) * heigth), ((col + 1) * width), ((7 - row + 1) * heigth),paint);
                }if(markList.get(i).pos==6)
            {
                paint.setColor(Color.RED);
                float startX = (float)((col * (float)width) + ((float)width*0.5));
                float startY = (float)(((7 - row) * (float)heigth)+((float)heigth*0.5));
                float stopX = (float)((markList.get(i).col2 * (float)width) + ((float)width*0.5));;
                float stopY = (float)(((7 - markList.get(i).row2) * (float)heigth)+((float)heigth*0.5));
                Log.d("디버깅","그려지나");
                paint.setStrokeWidth((int)(width*0.1)); //선의 굵기
                paint.setAntiAlias(true);//경계면을 부드럽게 처리하기


                canvas.drawLine(startX,startY,stopX,stopY,paint);


                float moveX = stopX - startX;
                float moveY = stopY - startY;

                double radian = (Math.atan2(moveY, moveX));


                double degree = radian * 180 / 3.1415f;

                float r = (float)Math.sqrt(Math.pow((double)(moveX),2) +  Math.pow((double)(moveY),2));

                float x1 = (float)(stopX - Math.cos(radian+0.5)*(r/4));
                float y1 = (float)(stopY - Math.sin(radian+0.5)*(r/4));

                float x2 = (float)(stopX - Math.cos(radian-0.5)*(r/4));
                float y2 = (float)(stopY - Math.sin(radian-0.5)*(r/4));

                canvas.drawLine(stopX,stopY,x1,y1,paint);
                canvas.drawLine(stopX,stopY,x2,y2,paint);
            }
            }
        }
    }
    protected void drawPiece(Canvas canvas) {

        float width = getWidth() / 8;
        float heigth = getHeight() / 8;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (mChessBoard.board[i + 1][j + 1].myPIN != null) {
                    //폰그리기
                    if (mChessBoard.board[i + 1][j + 1].myPIN.Type.equals("P")) {
                        Bitmap sm;
                        if (mChessBoard.board[i + 1][j + 1].myPIN.WB)
                            sm = BitmapFactory.decodeResource(getResources(), ChessPieaceIcon.img[ChessPieaceIcon.imgPosition][6]);
                        else
                            sm = BitmapFactory.decodeResource(getResources(), ChessPieaceIcon.img[ChessPieaceIcon.imgPosition][0]);
                        canvas.drawBitmap(sm, null, new Rect((int) (i * width), (int) ((7 - j) * heigth), (int) ((i + 1) * width), (int) ((7 - j + 1) * heigth)), null);
                        sm.recycle();
                    }
                    //룩그리기
                    if (mChessBoard.board[i + 1][j + 1].myPIN.Type.equals("R")) {
                        Bitmap sm;
                        if (mChessBoard.board[i + 1][j + 1].myPIN.WB)
                            sm = BitmapFactory.decodeResource(getResources(), ChessPieaceIcon.img[ChessPieaceIcon.imgPosition][7]);
                        else
                            sm = BitmapFactory.decodeResource(getResources(), ChessPieaceIcon.img[ChessPieaceIcon.imgPosition][1]);
                        canvas.drawBitmap(sm, null, new Rect((int) (i * width), (int) ((7 - j) * heigth), (int) ((i + 1) * width), (int) ((7 - j + 1) * heigth)), null);
                        sm.recycle();
                        sm = null;
                    }
                    //킹 그리기
                    if (mChessBoard.board[i + 1][j + 1].myPIN.Type.equals("K")) {
                        Bitmap sm;
                        if (mChessBoard.board[i + 1][j + 1].myPIN.WB)
                            sm = BitmapFactory.decodeResource(getResources(), ChessPieaceIcon.img[ChessPieaceIcon.imgPosition][11]);
                        else
                            sm = BitmapFactory.decodeResource(getResources(), ChessPieaceIcon.img[ChessPieaceIcon.imgPosition][5]);
                        canvas.drawBitmap(sm, null, new Rect((int) (i * width), (int) ((7 - j) * heigth), (int) ((i + 1) * width), (int) ((7 - j + 1) * heigth)), null);
                        sm.recycle();
                        sm = null;
                    }
                    //퀸 그리기
                    if (mChessBoard.board[i + 1][j + 1].myPIN.Type.equals("Q")) {
                        Bitmap sm;
                        if (mChessBoard.board[i + 1][j + 1].myPIN.WB)
                            sm = BitmapFactory.decodeResource(getResources(), ChessPieaceIcon.img[ChessPieaceIcon.imgPosition][10]);
                        else
                            sm = BitmapFactory.decodeResource(getResources(), ChessPieaceIcon.img[ChessPieaceIcon.imgPosition][4]);
                        canvas.drawBitmap(sm, null, new Rect((int) (i * width), (int) ((7 - j) * heigth), (int) ((i + 1) * width), (int) ((7 - j + 1) * heigth)), null);
                        sm.recycle();
                        sm = null;
                    }
                    //나이트 그리기
                    if (mChessBoard.board[i + 1][j + 1].myPIN.Type.equals("N")) {
                        Bitmap sm;
                        if (mChessBoard.board[i + 1][j + 1].myPIN.WB)
                            sm = BitmapFactory.decodeResource(getResources(), ChessPieaceIcon.img[ChessPieaceIcon.imgPosition][9]);
                        else
                            sm = BitmapFactory.decodeResource(getResources(), ChessPieaceIcon.img[ChessPieaceIcon.imgPosition][3]);
                        canvas.drawBitmap(sm, null, new Rect((int) (i * width), (int) ((7 - j) * heigth), (int) ((i + 1) * width), (int) ((7 - j + 1) * heigth)), null);
                        sm.recycle();
                        sm = null;
                    }
                    //비숍 그리기
                    if (mChessBoard.board[i + 1][j + 1].myPIN.Type.equals("B")) {
                        Bitmap sm;
                        if (mChessBoard.board[i + 1][j + 1].myPIN.WB)
                            sm = BitmapFactory.decodeResource(getResources(), ChessPieaceIcon.img[ChessPieaceIcon.imgPosition][8]);
                        else
                            sm = BitmapFactory.decodeResource(getResources(), ChessPieaceIcon.img[ChessPieaceIcon.imgPosition][2]);
                        canvas.drawBitmap(sm, null, new Rect((int) (i * width), (int) ((7 - j) * heigth), (int) ((i + 1) * width), (int) ((7 - j + 1) * heigth)), null);
                        sm.recycle();
                    }
                    System.gc();
                }
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceFlag=true;
        invalidate();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
