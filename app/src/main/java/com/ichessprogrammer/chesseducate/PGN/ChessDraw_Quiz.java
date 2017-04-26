package com.ichessprogrammer.chesseducate.PGN;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.ichessprogrammer.chesseducate.CheckMM;
import com.ichessprogrammer.chesseducate.CheckMateAlgorithm;
import com.ichessprogrammer.chesseducate.ChessBoard;
import com.ichessprogrammer.chesseducate.ChessPieaceIcon;
import com.ichessprogrammer.chesseducate.CoordinateFIltering;
import com.ichessprogrammer.chesseducate.DoOrder;
import com.ichessprogrammer.chesseducate.DrawPin;
import com.ichessprogrammer.chesseducate.Logic;
import com.ichessprogrammer.chesseducate.Mask;
import com.ichessprogrammer.chesseducate.MoveRange;
import com.ichessprogrammer.chesseducate.PGNIndex;
import com.ichessprogrammer.chesseducate.PGNMaker;
import com.ichessprogrammer.chesseducate.PIN;
import com.ichessprogrammer.chesseducate.R;

import java.util.ArrayList;
import java.util.LinkedList;

public class ChessDraw_Quiz extends SurfaceView implements SurfaceHolder.Callback {

    public SurfaceHolder holder;
    boolean Surfaceflag=false;
    final int MAX = 8;
    final int MIN = 1;
    public boolean isAnswerCorrect = false;
    public boolean isManagerMode = false;
    public boolean isClear = false;
    public boolean isHint = false;
    ChessBoard mChessBoard;
    ChessBoard backUpChessBoard;
    Context mContext;
    Handler mHandler;
    HinCoordinateFiltering hintAlgorithm;
    int number = 1;
    int sequence = 0;
    LinkedList<PGNIndex> mPGNIndex = new LinkedList<>();
    //일단 public으로 ...
    String pgn;
    String promotionPgn = "";
    Boolean WB;
    Boolean promotion = false;
    Boolean reversePlayer = false;
    int Point=3;
    int currentX;
    int currentY;

    private String marks[];

    int row;
    int col;
    float x;
    float y;
    Mask mask;

    public ChessDraw_Quiz(Context context) {
        super(context);
        mContext = context;
        //mWidth = (int)(getWidth()*0.7);
        mask = new Mask();
        mHandler = new Handler();
        holder=getHolder();
        holder.addCallback(this);
        init();
    }

    public ChessDraw_Quiz(Context context, AttributeSet a) {
        super(context, a);
        mContext = context;
        //mWidth = (int)(getWidth()*0.7);
        mask = new Mask();
        mHandler = new Handler();
        init();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        if (isClear)
            return true;

        pgn = "";

        PGNMaker pgnMaker = new PGNMaker(mChessBoard);
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            x = event.getX();
            y = event.getY();
            row = (int) (y / (getHeight() / 8));
            col = (int) (x / (getWidth() / 8));

            row = CheckMM.check(row);
            col = CheckMM.check(col);
            row = 7 - row;

            //이 부분은 화면에 움직이고 싶은 말을 처음 터치했을 때 나오는 조건문.
            if (mChessBoard.board[col + 1][row + 1].myPIN != null) {
                if (mChessBoard.board[col + 1][row + 1].myPIN.WB == WB) { //현재  턴이 흰색인지 검정색인지를 파악함.
                    Log.d("Piece", mChessBoard.board[col + 1][row + 1].myPIN.Type + "(" + mChessBoard.board[col + 1][row + 1].myPIN.WB + ")");
                    mask = Logic.Path(mChessBoard.board[col + 1][row + 1].myPIN, mChessBoard, col + 1, row + 1);
                    currentX = col;
                    currentY = row;
                    invalidate();
                    return true;
                }
            } else {
                if (!mask.getList_X().contains(col + 1) || !mask.getList_Y().contains(row + 1)) {
                    mask = new Mask();
                    invalidate();
                    return true;
                }
            }
        }

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                x = event.getX();
                y = event.getY();
                row = (int) (y / (getHeight() / 8));
                col = (int) (x / (getWidth() / 8));

                row = CheckMM.check(row);
                col = CheckMM.check(col);
                row = 7 - row;

                Log.d("what?", row + ":" + col);


                if (reversePlayer) {
                    row = 7 - row;
                    col = 7 - col;
                }

                Log.i("위치 좌표", row + ", " + col);


                ArrayList<Integer> xList = mask.getList_X();
                ArrayList<Integer> yList = mask.getList_Y();
                for (int i = 0; i < xList.size(); i++) {

                    int tempX = xList.get(i) - 1;
                    int tempY = yList.get(i) - 1;


                    if (currentX == col && currentY == row)
                        continue;

                    if (tempX == col && tempY == row) { //이동이동.


                        try {
                            backUpChessBoard = (ChessBoard) mChessBoard.clone();
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                            backUpChessBoard = mChessBoard;
                        }

                        //TODO 체크이면 움직이지 못하게 하기.
                        //이동할 때
                        PIN tempPin = mChessBoard.board[col + 1][row + 1].myPIN;
                        mChessBoard.board[col + 1][row + 1].myPIN = mChessBoard.board[currentX + 1][currentY + 1].myPIN;
                        mChessBoard.board[currentX + 1][currentY + 1].myPIN = null;


                        boolean check = false;
                        boolean checkKing = false;
                        for (int j = 1; j <= 8; j++) {
                            if (checkKing)
                                break;
                            for (int k = 1; k <= 8; k++) {
                                if (mChessBoard.board[j][k].myPIN != null) {
                                    if (mChessBoard.board[j][k].myPIN.Type.compareTo("K") == 0 && mChessBoard.board[j][k].myPIN.WB == WB) {
                                        MoveRange moveRange = new MoveRange(mChessBoard, WB);
                                        moveRange.AddIndex("K", j, k);
                                        if (!moveRange.isSafe(j, j)) {
                                            //Toast.makeText(getContext(), "Check", Toast.LENGTH_SHORT).show();
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

                            if (row + 1 == 8 || row + 1 == 1) { //promotion event
                                Intent intent = new Intent("Quiz_PGN");
                                intent.putExtra("tag", "Promotion");
                                mContext.sendBroadcast(intent);

                                promotionPgn = pgn;
                                pgn = ""; //pgn 값을 초기화
                                promotion = true;
                            }


                            if (currentY - row == 2 || currentY - row == -2) { //앙파상 event , 처음에 pawn 이 2칸을 이동했을 때 발동
                                if (!mChessBoard.board[col + 1][row + 1].myPIN.move) { // 처음 이동한 거라면, 생각해보면 필요 없는 조건문.
                                    mChessBoard.board[col + 1][row + 1].myPIN.enPassantEnable = true; // 앙파상 할 수 있게 바꿈.
                                    mChessBoard.enPassant = true; //다음 상대방이 앙파상 가능한 상태로 바뀜

                                    mask = new Mask();
                                    WB = !WB;

                                    mChessBoard.board[currentX + 1][currentY + 1].myPIN = null;

                                    for (int j = 1; j <= 8; j++) { //더 효율적인 loop 문을 생각해보자.
                                        for (int k = 1; k <= 8; k++) {
                                            if (j != col + 1 || k != row + 1) {
                                                if (mChessBoard.board[j][k].myPIN != null) { //혹시라도 모르니 현재 움직인 말을 제외한 모든 말들을 앙파상 불가 상태로 바꿔줌
                                                    mChessBoard.board[j][k].myPIN.enPassantEnable = false;

                                                    if (!promotion) {  // <- 내 생각엔 이것도 필요없는 조건문.
                                                        //check 상태 또는 checkmate 상태를 파악하기 위한 알고리즘.
                                                        //현재 WB는 상대로 넘겨졌기 때문에 WB == myPIN.WB가 맞다.
                                                        if (mChessBoard.board[j][k].myPIN.Type.compareTo("K") == 0 && mChessBoard.board[j][k].myPIN.WB == WB) {
                                                            MoveRange moveRange = new MoveRange(mChessBoard, WB); // 다음에 움직일 수 있는 말들의 범위를 구하기 위한 호출인데, 엄청 비효율적임
                                                            moveRange.AddIndex("K", j, k); //왕의 움직임을 확인
                                                            if (!moveRange.isSafe(j, j)) { //우리가 움직일 수 있는 말들의 범위를 구해 왕을 공격할 수 있는지 파악. 그래서 false 이면 왕이 안전하지 않기 때문에 공격 받을 수 있는 상태
                                                                CheckMateAlgorithm checkMateAlgorithm = new CheckMateAlgorithm(mChessBoard, WB); //체크인 상태가 확인 되면 이게 체크메이트인지 확인
                                                                if (checkMateAlgorithm.isCheckMate()) { //체크당한 후에 대처가 가능한지 파악. true 는  결국 체크메이트 상태, 즉 대처 불가
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


                                    if (!promotion)
                                        //sendBroadCastReceiver(pgn);

                                        mChessBoard.board[col + 1][row + 1].myPIN.move = true;

                                    if (isAnswerCorrect = isAnswer(pgn)) {
                                        invalidate();
                                    }
                                    return true;
                                }
                            }
                        }
                        //왕과 pawn 을 제외한 다른 말들의 움직임.
                        //거의 왠만하면 위의 알고리즘과 비슷함.
                        WB = !WB;
                        for (int j = 1; j <= 8; j++) {
                            for (int k = 1; k <= 8; k++) {
                                if (mChessBoard.board[j][k].myPIN != null) {
                                    mChessBoard.board[j][k].myPIN.enPassantEnable = false;

                                    if (!promotion) {
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
                        if (!promotion)
                            // sendBroadCastReceiver(pgn);

                            if (isAnswerCorrect = isAnswer(pgn))
                                invalidate();
                        return true;
                    }
                }


        }
        return true;
    }

    public void readChessBoard(ChessBoard chessBoard, int sequence) {
        mChessBoard = chessBoard;
        WB = mChessBoard.WB;
        mask = new Mask();
        this.sequence = sequence + 1;

        number = mChessBoard.number;

        if (WB)
            number++;

        invalidate();
    }


    public void sendMessage() {
        Intent intent = new Intent("isCorrect");
        intent.putExtra("answer", "correct");

        Bundle b = new Bundle();
        b.putBoolean("isClear",isClear);
        b.putInt("sequence",sequence-1);

        intent.putExtras(b);

        mContext.sendBroadcast(intent);

    }

    public void hint() {
        if (!isClear) { //시간을 약간 지연시켜 줌
            int size = 1;
            int hintSequence = sequence + 1;

            PGNIndex pgnIndex = mPGNIndex.get(hintSequence);
            PGNIndex prePgnIndex = mPGNIndex.get(hintSequence - 1);

            for (int i = hintSequence; i < mPGNIndex.size(); i++) {
                pgnIndex = mPGNIndex.get(i);
                if (prePgnIndex.loop == pgnIndex.loop && WB == pgnIndex.WB) {

                    if (pgnIndex.index == number) {
                        hintSequence = i;
                        break;
                    }

                }
            }

            ArrayList<Integer> sort = new ArrayList<>();
            sort.add(hintSequence);

            for (int i = hintSequence + 1; i < mPGNIndex.size(); i++) {
                PGNIndex temp = mPGNIndex.get(i);
                if (temp.loop == pgnIndex.loop + 1 && WB == temp.WB) {

                    if (temp.index == number) {
                        size++;
                        sort.add(i);
                    }

                }

            }

            int nextTurn = sort.get(((int) (Math.random() * size)));
            pgnIndex = mPGNIndex.get(nextTurn);

            String pgn = pgnIndex.mOrder;
            Log.i("힌트", pgn + "," + sequence);

            hintAlgorithm = new HinCoordinateFiltering(mChessBoard, pgnIndex.mChessBoard, pgn, WB);
            isHint = hintAlgorithm.isThereHint();


            invalidate();
        }
    }


    //quiz 를 시작할 때 설정.
    public void startQuiz(LinkedList<PGNIndex> linkedList) {

        mPGNIndex = linkedList;
        number = mPGNIndex.get(1).index;
        try {
            mChessBoard = (ChessBoard) mPGNIndex.get(0).mChessBoard.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        WB = mPGNIndex.get(1).WB;
        isClear = false;
        isHint = false;
        sequence = 0;
    }

    public void enemyTurn() {
        if (!isClear) { //시간을 약간 지연시켜 줌

            int size = 1;

            PGNIndex pgnIndex;
            PGNIndex prePgnIndex;
            try {
                pgnIndex = mPGNIndex.get(sequence);
                prePgnIndex = mPGNIndex.get(sequence - 1);
            } catch (IndexOutOfBoundsException e) {
                pgnIndex = mPGNIndex.get(1);
                prePgnIndex = mPGNIndex.get(0);
                sequence = 1;
                e.printStackTrace();
            }

            for (int i = sequence; i < mPGNIndex.size(); i++) {
                pgnIndex = mPGNIndex.get(i);
                if (prePgnIndex.loop == pgnIndex.loop) {
                    sequence = i;
                    break;
                }
            }
            ArrayList<Integer> sort = new ArrayList<>();
            sort.add(sequence);

            for (int i = sequence + 1; i < mPGNIndex.size(); i++) {
                PGNIndex temp = mPGNIndex.get(i);
                if (temp.loop == pgnIndex.loop + 1) {
                    if (!WB) {
                        if (temp.index == number) {
                            size++;
                            sort.add(i);
                        }
                    } else if (WB) {
                        if (temp.index == number + 1) {
                            size++;
                            sort.add(i);
                        }
                    }
                }
            }

            int nextTurn = sort.get(((int) (Math.random() * size)));
            pgnIndex = mPGNIndex.get(nextTurn);


            if (pgnIndex.isLoopEnd || sequence == mPGNIndex.size() - 1)
                isClear = true;

            number++;

            sequence = nextTurn;

            try {
                mChessBoard = (ChessBoard) pgnIndex.mChessBoard.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            WB = !WB;

            Intent intent = new Intent("isCorrect");
            Bundle bundle = new Bundle();
            bundle.putInt("sequence", sequence);
            bundle.putBoolean("isClear", isClear);
            intent.putExtras(bundle);
            intent.putExtra("answer", "enemy");


            getContext().sendBroadcast(intent);

        }
    }



    public boolean isAnswer(String p) {

        PGNIndex prePgnIndex = mPGNIndex.get(sequence);
        PGNIndex pgnindex;
        int loop = prePgnIndex.loop;
        boolean isLoopStart = false;

        DoOrder doOrder = new DoOrder();



        for (int i = sequence + 1; i < mPGNIndex.size(); i++) {
            pgnindex = mPGNIndex.get(i);


            if (loop != pgnindex.loop)
                isLoopStart = true;

            if (pgnindex.index == number && doOrder.Ordering(p, pgnindex.mOrder) && pgnindex.loop == prePgnIndex.loop && WB != pgnindex.WB) {
                Toast.makeText(getContext(), "정답", Toast.LENGTH_SHORT).show();
                sequence = i + 1;
                if (pgnindex.isLoopEnd)
                    isClear = true;
                if (sequence == mPGNIndex.size())
                    isClear = true;

                isHint = false;
                return true;
            }

            if (isLoopStart) {
                if (pgnindex.loop == prePgnIndex.loop + 1 && pgnindex.index == number && doOrder.Ordering(p, pgnindex.mOrder) && WB != pgnindex.WB) {
                    Toast.makeText(getContext(), "정답", Toast.LENGTH_SHORT).show();
                    sequence = i + 1;
                    if (pgnindex.isLoopEnd)
                        isClear = true;
                    if (sequence == mPGNIndex.size())
                        isClear = true;
                    isHint = false;
                    return true;
                }
                isLoopStart = false;
                if (pgnindex.isLoopEnd)
                    isLoopStart = true;
                loop = pgnindex.loop;
            }
            if (prePgnIndex.loop != 0 && pgnindex.isLoopEnd)
                break;
        }
        if(Point>1)
        {
            Point--;
        }
        Toast.makeText(getContext(), "오답", Toast.LENGTH_SHORT).show();
        mChessBoard = backUpChessBoard;
        WB = !WB;
        invalidate();
        return false;
    }


    public void reverseDraw(boolean b) {
        reversePlayer = b;
        invalidate();
    }

    public void readChessBoard(ChessBoard chessBoard, String type) {
        mChessBoard = chessBoard;

        pgn = promotionPgn;
        promotionPgn = "";

        PGNMaker pgnMaker = new PGNMaker();

        pgn = pgn + pgnMaker.Promotion(type);
        for (int j = 1; j <= 8; j++) {
            for (int k = 1; k <= 8; k++) {
                if (mChessBoard.board[j][k].myPIN != null) {
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
        promotion = false;
        Log.d("PGN", pgn);
        //sendBroadCastReceiver(pgn);
        mask = new Mask();
        if (isAnswerCorrect = isAnswer(pgn))
            invalidate();
    }




    public void init() {
        mChessBoard = new ChessBoard();
        WB = true;
        currentX = -1;
        currentY = -1;
    }


    protected void doDraw() {
       // super.onDraw(canvas);
        //보드판 그리기
        if(Surfaceflag) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Canvas canvas = holder.lockCanvas();
                    drawBoard(canvas);
                    if (isHint) {
                        drawHint(canvas);
                    }
                    drawRange(canvas);
                    drawPiece(canvas);

                    if (isAnswerCorrect) {
                        isAnswerCorrect = false;
                        sendMessage();
                    }


                    if(isHint) {
                        drawMark(canvas);
                    }
                    holder.unlockCanvasAndPost(canvas);
                }
            });
            thread.start();

        }
    }
    public void invalidate()
    {
        super.invalidate();
        doDraw();
    }




    protected void drawMark(Canvas canvas) {
        final String GREEN = "GR";
        final String RED = "RE";
        final String BLUE = "BL";
        final String CIRCLE = "CM";
        final String CROSS = "XM";
        final String ARROW = "AR";

        float width = getWidth() / 8;
        float height = getHeight() / 8;


        String mask =  mPGNIndex.get(sequence + 1).getMark();
        marks = mPGNIndex.get(sequence + 1).getMark().split(" ");

        int fromX;
        int fromY;
        int toX;
        int toY;

        CoordinateFIltering coordinateFIltering;

        for(String m : marks) {

            Log.i("Mark", m);
            if(m.length() < 3)
                continue;

            String type = m.substring(0 ,2);
            String where = m.substring(2, 4);
            String to = "";

            if(m.length() > 4)  to = m.substring(4, 6);


            coordinateFIltering = new CoordinateFIltering(where);
            int row = coordinateFIltering.getX()-1;
            int col = coordinateFIltering.getY()-1;




            if (!reversePlayer) {
                fromX = (int)((row) * width);
                fromY = (int)((7 - (col)) * height);
                toX = (int)((row + 1) * width);
                toY = (int)((8 - (col)) * height);

            } else {
                fromX = (int) ((7 - row) * width);
                fromY = (int) ((col) * height);
                toX = (int) ((8 - (row)) * width);
                toY = (int) ((col+1) * height);
            }

            Bitmap sm ;
            Paint paint = new Paint();

            switch(type) {
                case CIRCLE :
                    sm = BitmapFactory.decodeResource(getResources(), R.drawable.circle_icon);
                    canvas.drawBitmap(sm, null, new Rect(fromX, fromY, toX, toY), null);
                    break;
                case CROSS :
                    sm = BitmapFactory.decodeResource(getResources(), R.drawable.x_icon);
                    canvas.drawBitmap(sm, null, new Rect(fromX, fromY, toX, toY), null);
                    break;
                case RED :
                    paint.setColor(Color.RED);
                    canvas.drawRect(fromX, fromY, toX, toY, paint);
                    break;
                case GREEN :
                    paint.setColor(Color.GREEN);
                    canvas.drawRect(fromX, fromY, toX, toY, paint);
                    break;
                case BLUE :
                    paint.setColor(Color.BLUE);
                    canvas.drawRect(fromX, fromY, toX, toY, paint);
                    break;
                case ARROW:
                    drawArrow(canvas, paint, fromX, fromY, to);
            }

        }
    }
    public void drawArrow(Canvas canvas, Paint paint, int fromX, int fromY, String to) {

        float width = getWidth() / 8;
        float height = getHeight() / 8;

        paint.setColor(Color.RED);

        float halfWidth = (float)(width * 0.5);
        float halfHeight = (float)(height * 0.5);


        CoordinateFIltering coordinateFIltering  = new CoordinateFIltering(to);

        int toX = coordinateFIltering.getX()-1;
        int toY = coordinateFIltering.getY()-1;

        float startX = fromX + halfWidth;
        float startY = fromY + halfHeight;
        float stopX = ((toX * width) + halfWidth);
        float stopY = (((7 - toY) * height)+ halfHeight);
        if(reversePlayer) {
            stopX = (((7 - toX) * width) + halfWidth);
            stopY = ((toY * height)+ halfHeight);
        }


        paint.setStrokeWidth((int)(width*0.1)); //선의 굵기
        canvas.drawLine(startX,startY,stopX,stopY,paint);

        float moveX = stopX - startX;
        float moveY = stopY - startY;

        double radian = (Math.atan2(moveY, moveX));


        double degree = radian * 180 / 3.1415f;

        float r = (float)Math.sqrt(Math.pow((double)(moveX),2) +  Math.pow((double)(moveY),2));

        float x1 = (float)(stopX - Math.cos(radian+0.5)*(width));
        float y1 = (float)(stopY - Math.sin(radian+0.5)*(height));

        float x2 = (float)(stopX - Math.cos(radian-0.5)*(width));
        float y2 = (float)(stopY - Math.sin(radian-0.5)*(height));

        canvas.drawLine(stopX,stopY,x1,y1,paint);
        canvas.drawLine(stopX,stopY,x2,y2,paint);
    }



    //보드판 그리는 함수
    protected void drawBoard(Canvas canvas) {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chessboard_03);
        canvas.drawBitmap(bitmap , null,new Rect(0 ,0 ,getWidth(), getHeight()),null);


        /*
        float width = getWidth() / 8;
        float height = getHeight() / 8;
        boolean WBflag = true;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Paint Pnt = new Paint();

                if (WBflag) {
                    Bitmap sm = BitmapFactory.decodeResource(getResources(), ChessPieaceIcon.img2[ChessPieaceIcon.boardImgPosition][0]);
                    canvas.drawBitmap(sm, null, new Rect((int) (j * width), (int) (i * height), (int) ((j + 1) * width), (int) ((i + 1) * height)), null);
                    sm.recycle();
                } else {
                    Bitmap sm = BitmapFactory.decodeResource(getResources(), ChessPieaceIcon.img2[ChessPieaceIcon.boardImgPosition][1]);
                    canvas.drawBitmap(sm, null, new Rect((int) (j * width), (int) (i * height), (int) ((j + 1) * width), (int) ((i + 1) * height)), null);
                    sm.recycle();
                }
                WBflag = !WBflag;
            }
            WBflag = !WBflag;
        }
        */
    }

    protected void drawRange(Canvas canvas) {
        float width = getWidth() / 8;
        float height = getHeight() / 8;

        ArrayList<Integer> x = mask.getList_X();
        ArrayList<Integer> y = mask.getList_Y();

        int fromX;
        int toX;
        int fromY;
        int toY;

        for (int i = 0; i < x.size(); i++) {
            Paint Pnt = new Paint();
            Pnt.setColor(Color.RED);


            if (!reversePlayer) {
                fromX = (int) ((x.get(i) - 1) * width);
                fromY = (int) ((MAX - y.get(i)) * height);
                toX = (int) ((x.get(i)) * width);
                toY = (int) ((MAX - (y.get(i) - 1)) * height);

            } else {
                fromX = (int) ((MAX - (x.get(i))) * width);
                fromY = (int) ((y.get(i) - 1) * height);
                toX = (int) ((MAX - (x.get(i) - 1)) * width);
                toY = (int) ((y.get(i)) * height);
            }

            canvas.drawRect(fromX, fromY, toX, toY, Pnt);
            //canvas.drawRect(tempX * width, tempY * height, (tempX + 1) * width, (tempY + 1) * height, Pnt);
        }
        Paint Pnt = new Paint();
        Pnt.setColor(Color.rgb(245, 34, 214));
        if (mask.mX != -1) {
            int pickedX = mask.mX - 1;
            int pickedY = 8 - mask.mY;
            if (!reversePlayer) {
                fromX = (int) ((mask.mX - 1) * width);
                fromY = (int) ((MAX - mask.mY) * height);
                toX = (int) ((mask.mX) * width);
                toY = (int) ((MAX - (mask.mY - 1)) * height);

            } else {
                fromX = (int) ((MAX - mask.mX) * width);
                fromY = (int) ((mask.mY - 1) * height);
                toX = (int) ((MAX - (mask.mX - 1)) * width);
                toY = (int) ((mask.mY) * height);
            }
            canvas.drawRect(fromX, fromY, toX, toY, Pnt);
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
                    int pinId = ChessPieaceIcon.img[ChessPieaceIcon.imgPosition][drawPin.findPin(p.Type, p.WB)];
                    Bitmap sm = BitmapFactory.decodeResource(getResources(), pinId);


                    if (!reversePlayer) {
                        fromX = (int) ((x - 1) * width);
                        fromY = (int) ((MAX - y) * height);
                        toX = (int) ((x) * width);
                        toY = (int) ((MAX - (y - 1)) * height);

                    } else {
                        fromX = (int) ((MAX - (x)) * width);
                        fromY = (int) ((y - 1) * height);
                        toX = (int) ((MAX - (x - 1)) * width);
                        toY = (int) ((y) * height);
                    }

                    canvas.drawBitmap(sm, null, new Rect(fromX, fromY, toX, toY), null);
                }
            }
        }
    }

    public void drawCoordination(Canvas canvas) { //보여줄 때에는 지울거에요.
        float width = getWidth() / 8;
        float height = getHeight() / 8;

        String x;
        int y;
        CoordinateFIltering c = new CoordinateFIltering();
        Paint Pnt = new Paint();
        Pnt.setTextSize(24);
        Pnt.setColor(Color.BLUE);
        for (int i = MIN; i <= MAX; i++) {
            for (int j = MIN; j <= MAX; j++) {
                if (!reversePlayer) {
                    x = c.getCoordinate(j);
                    y = MAX - (i - 1);
                } else {
                    x = c.getCoordinate(MAX - (j - 1));
                    y = i;
                }
                x = x + y;
                canvas.drawText(x, (j - 1) * width, i * height, Pnt);
            }
        }
    }

    public void drawHint(Canvas canvas) {
        float width = getWidth() / 8;
        float height = getHeight() / 8;

        int whichX = hintAlgorithm.getFromX();
        int whichY = hintAlgorithm.getFromY();
        int whereX = hintAlgorithm.getToX();
        int whereY = hintAlgorithm.getToY();

        Paint p = new Paint();

        int fromX;
        int fromY;
        int toX;
        int toY;

        if (!reversePlayer) {
            fromX = (int) ((whichX - 1) * width);
            fromY = (int) ((MAX - whichY) * height);
            toX = (int) ((whichX) * width);
            toY = (int) ((MAX - (whichY - 1)) * height);
        } else {
            fromX = (int) ((MAX - (whichX)) * width);
            fromY = (int) ((whichY - 1) * height);
            toX = (int) ((MAX - (whichX - 1)) * width);
            toY = (int) ((whichY) * height);
        }
        p.setColor(Color.rgb(212, 212, 212));
        canvas.drawCircle((fromX + toX) / 2, (fromY + toY) / 2, 110, p);
        //canvas.drawRect(fromX,fromY,toX,toY, p);


        if (!reversePlayer) {
            fromX = (int) ((whereX - 1) * width);
            fromY = (int) ((MAX - whereY) * height);
            toX = (int) ((whereX) * width);
            toY = (int) ((MAX - (whereY - 1)) * height);
        } else {
            fromX = (int) ((MAX - (whereX)) * width);
            fromY = (int) ((whereY - 1) * height);
            toX = (int) ((MAX - (whereX - 1)) * width);
            toY = (int) ((whereY) * height);
        }
        p.setColor(Color.rgb(242, 212, 212));
        canvas.drawRect(fromX, fromY, toX, toY, p);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Surfaceflag =true;
        invalidate();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /*

    public Runnable enemyTurn = new Runnable() {
        @Override
        public void run() {
            try{
                Thread.sleep(500);
            }catch(InterruptedException e) {
                e.printStackTrace();
            }

        }
    };
    */
}