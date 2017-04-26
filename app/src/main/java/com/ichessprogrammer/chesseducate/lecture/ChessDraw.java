package com.ichessprogrammer.chesseducate.lecture;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ichessprogrammer.chesseducate.CheckMM;
import com.ichessprogrammer.chesseducate.CheckMateAlgorithm;
import com.ichessprogrammer.chesseducate.ChessBoard;
import com.ichessprogrammer.chesseducate.ChessPieaceIcon;
import com.ichessprogrammer.chesseducate.Logic;
import com.ichessprogrammer.chesseducate.Mask;
import com.ichessprogrammer.chesseducate.MoveRange;
import com.ichessprogrammer.chesseducate.PGNMaker;
import com.ichessprogrammer.chesseducate.PIN;
import com.ichessprogrammer.chesseducate.R;

import java.util.ArrayList;

/**
 * Created by 남지니 on 2016-07-09.
 */
public class ChessDraw extends SurfaceView implements SurfaceHolder.Callback {
    public boolean promotion = false;
    public String promotionPgn;
    public int promCol = -1;
    public int promRow = -1;
    public int temprow, tempcol;
    public int markPos = -1;//원 =1 엑스 = 2 빨파초 = 3 4 5 화살표 는 6
    public ArrayList<BoardMark> markList = new ArrayList<>();
    public boolean direction_flag = false;
    public ChessBoard backUpChessBoard;
    ChessBoard mChessBoard;
    Mask mask;
    Context mContext;
    int pos = -1;
    int currentX;
    int currentY;
    boolean movingFlag = true;
    String pgn;
    Boolean WB;
    SurfaceHolder holder = getHolder();
    boolean surfaceFlag = false;
    boolean firstMove = true;
    int row;
    int col;
    float x;
    float y;

    public ChessDraw(Context context) {
        super(context);
        //mWidth = (int)(getWidth()*0.7);
        mask = new Mask();
        mContext = context;
        pgn = "null";
        WB = true;
        holder.addCallback(this);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        /*
        * Lec1 이 호출되면 강의세팅 모드가 실행한다.
         */
        if (LecureSave.callFragment.startsWith("Lec1")) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {//보드판 셋팅

                x = event.getX();
                y = event.getY();
                row = (int) (y / (getHeight() / 8));
                col = (int) (x / (getWidth() / 8));
                row = CheckMM.check(row);
                col = CheckMM.check(col);
                row = 7 - row;
                Log.d("what?", row + ":" + col + ":" + pos);
                if (mChessBoard.board[col + 1][row + 1].myPIN != null) {
                    Log.d("Piece", mChessBoard.board[col + 1][row + 1].myPIN.Type + "(" + mChessBoard.board[col + 1][row + 1].myPIN.WB + ")");
                    mChessBoard.board[col + 1][row + 1].myPIN = null;
                    //mask = Logic.Path(mChessBoard.board[col+1][row+1].myPIN, mChessBoard.board);

                } else {
                    if (pos != -1) {
                        boolean wb = false;
                        if (pos > 5)
                            wb = true;
                        int intType = pos % 6;
                        String Type = "P";
                        switch (intType) {
                            case 0:
                                Type = "P";
                                break;
                            case 1:
                                Type = "R";
                                break;
                            case 2:
                                Type = "B";
                                break;
                            case 3:
                                Type = "N";
                                break;
                            case 4:
                                Type = "Q";
                                break;
                            case 5:
                                Type = "K";
                                break;
                        }
                        mChessBoard.board[col + 1][row + 1].myPIN = new PIN();
                        mChessBoard.board[col + 1][row + 1].myPIN.move = false;
                        mChessBoard.board[col + 1][row + 1].myPIN.Type = Type;
                        mChessBoard.board[col + 1][row + 1].myPIN.WB = wb;
                        mChessBoard.board[col + 1][row + 1].myPIN.x = col + 1;
                        mChessBoard.board[col + 1][row + 1].myPIN.y = row + 1;
                        if (mChessBoard.board[col + 1][row + 1].myPIN.Type.equals("P")) {
                            if (mChessBoard.board[col + 1][row + 1].myPIN.WB) {
                                if (mChessBoard.board[col + 1][row + 1].myPIN.y == 2)
                                    mChessBoard.board[col + 1][row + 1].myPIN.move = false;
                                else
                                    mChessBoard.board[col + 1][row + 1].myPIN.move = true;
                            } else {
                                if (mChessBoard.board[col + 1][row + 1].myPIN.y == 7)
                                    mChessBoard.board[col + 1][row + 1].myPIN.move = false;
                                else
                                    mChessBoard.board[col + 1][row + 1].myPIN.move = true;
                            }
                        } else if (mChessBoard.board[col + 1][row + 1].myPIN.Type.equals("R")) {
                            if (mChessBoard.board[col + 1][row + 1].myPIN.WB) {
                                if (mChessBoard.board[col + 1][row + 1].myPIN.y == 1 &&
                                        (mChessBoard.board[col + 1][row + 1].myPIN.x == 8 || mChessBoard.board[col + 1][row + 1].myPIN.x == 1))
                                    mChessBoard.board[col + 1][row + 1].myPIN.move = false;
                                else
                                    mChessBoard.board[col + 1][row + 1].myPIN.move = true;
                                Log.d("디버깅", mChessBoard.board[col + 1][row + 1].myPIN.move + "");
                            } else {
                                if (mChessBoard.board[col + 1][row + 1].myPIN.y == 8 &&
                                        (mChessBoard.board[col + 1][row + 1].myPIN.x == 8 || mChessBoard.board[col + 1][row + 1].myPIN.x == 1))
                                    mChessBoard.board[col + 1][row + 1].myPIN.move = false;
                                else
                                    mChessBoard.board[col + 1][row + 1].myPIN.move = true;

                                Log.d("디버깅", mChessBoard.board[col + 1][row + 1].myPIN.move + "");
                            }
                        } else if (mChessBoard.board[col + 1][row + 1].myPIN.Type.equals("K")) {
                            if (mChessBoard.board[col + 1][row + 1].myPIN.WB) {
                                mChessBoard.wKing = true;
                                if (mChessBoard.board[col + 1][row + 1].myPIN.y == 1 &&
                                        (mChessBoard.board[col + 1][row + 1].myPIN.x == 5))
                                    mChessBoard.board[col + 1][row + 1].myPIN.move = false;
                                else
                                    mChessBoard.board[col + 1][row + 1].myPIN.move = true;
                            } else {
                                mChessBoard.bKing = true;
                                if (mChessBoard.board[col + 1][row + 1].myPIN.y == 8 &&
                                        (mChessBoard.board[col + 1][row + 1].myPIN.x == 5))
                                    mChessBoard.board[col + 1][row + 1].myPIN.move = false;
                                else
                                    mChessBoard.board[col + 1][row + 1].myPIN.move = true;
                            }

                            Log.d("디버깅", mChessBoard.board[col + 1][row + 1].myPIN.move + "");
                        }
                        mChessBoard.board[col + 1][row + 1].move = mChessBoard.board[col + 1][row + 1].myPIN.move;
                    }
                }
                Intent intent = new Intent(MakeBoard.NEXT_PAGE);
                intent.putExtra("posUpdate", true);
                mContext.sendBroadcast(intent);
                pos = -1;
                invalidate();
                return true;
            }

            /*
            * lec2 가 호출되면 본격적인 강의를 할 수 있습니다.
             */
        } else if (LecureSave.callFragment.startsWith("Lec2")) {
            pgn = "";

            PGNMaker pgnMaker = new PGNMaker(mChessBoard);
            int action = event.getAction();

            if (MotionEvent.ACTION_DOWN == event.getAction()) {
                x = event.getX();
                y = event.getY();
                row = (int) (y / (getHeight() / 8));
                col = (int) (x / (getWidth() / 8));
                row = CheckMM.check(row);
                col = CheckMM.check(col);
                row = 7 - row;
                if (markPos != -1 && markPos == 6 && !direction_flag) {

                    temprow = row;
                    tempcol = col;
                    Log.d("디버깅", "한번더 눌러여");
                    direction_flag = !direction_flag;

                    return true;
                }
                //이 부분은 화면에 움직이고 싶은 말을 처음 터치했을 때 나오는 조건문.
                if (mChessBoard.board[col + 1][row + 1].myPIN != null && movingFlag) {
                    if (!firstMove && mChessBoard.board[col + 1][row + 1].myPIN.WB == WB) { //현재  턴이 흰색인지 검정색인지를 파악함.
                        Log.d("Piece", mChessBoard.board[col + 1][row + 1].myPIN.Type + "(" + mChessBoard.board[col + 1][row + 1].myPIN.WB + ")");
                        mask = Logic.Path(mChessBoard.board[col + 1][row + 1].myPIN, mChessBoard, col + 1, row + 1);
                        currentX = col;
                        currentY = row;
                        invalidate();
                        return true;
                    } else if (firstMove) {
                        Log.d("Piece", mChessBoard.board[col + 1][row + 1].myPIN.Type + "(" + mChessBoard.board[col + 1][row + 1].myPIN.WB + ")");
                        mask = Logic.Path(mChessBoard.board[col + 1][row + 1].myPIN, mChessBoard, col + 1, row + 1);
                        currentX = col;
                        currentY = row;
                        invalidate();
                        WB = mChessBoard.board[col + 1][row + 1].myPIN.WB;
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

                    if (markPos != -1) {

                        if (markPos < 6) {
                            BoardMark boardMark = new BoardMark();
                            boardMark.pos = markPos;
                            boardMark.row = row;
                            boardMark.col = col;
                            Log.d("디버깅","왜지?");
                            if (markList != null)
                                markList.add(boardMark);
                            else {
                                markList = new ArrayList<>();
                                markList.add(boardMark);
                            }
                            markPos = -1;
                        } else {
                            if (direction_flag && (temprow != row) && (tempcol != col)) {
                                BoardMark boardMark = new BoardMark();
                                boardMark.pos = 6;
                                boardMark.row2 = row;
                                boardMark.col2 = col;
                                boardMark.row = temprow;
                                boardMark.col = tempcol;
                                Log.d("디버깅", "등록 됬어용" + row + "" + col + "" + temprow + "" + tempcol);
                                if (markList != null)
                                    markList.add(boardMark);
                                else {
                                    markList = new ArrayList<>();
                                    markList.add(boardMark);
                                }
                                markPos = -1;
                                direction_flag = !direction_flag;
                            }
                        }
                        invalidate();
                        return true;
                    }

                    Log.d("what?", row + ":" + col);


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

                            if (check) {
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
                                    Intent intent = new Intent("Lec2");
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


                                        if (!promotion) {
                                            //sendBroadCastReceiver(pgn);
                                            mChessBoard.board[col + 1][row + 1].myPIN.move = true;
                                            Intent intent = new Intent("Lec2");
                                            intent.putExtra("pgn", pgn);
                                            mContext.sendBroadcast(intent);

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

                            if(!promotion) {
                                mChessBoard.board[col + 1][row + 1].myPIN.move = true;
                                mChessBoard.enPassant = false;
                                mask = new Mask();


                                Log.d("PGN", pgn);
                                Intent intent = new Intent("Lec2");
                                intent.putExtra("pgn", pgn);
                                mContext.sendBroadcast(intent);
                                invalidate();
                            }
                            return true;
                        }
                    }

            }
            return true;


        }
        return false;
    }

    public void readChessBoard(ChessBoard chessBoard) {
        mChessBoard = chessBoard;
        invalidate();
    }

    public void readChessBoard(ChessBoard chessBoard, String type) {
        mChessBoard = chessBoard;
        pgn = promotionPgn;
        promotionPgn = "";

        PGNMaker pgnMaker = new PGNMaker();

        pgn = pgn + pgnMaker.Promotion(type);
        if (ChessBoard.wKing || ChessBoard.bKing) {
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
        }
        promotion = false;
        Intent intent = new Intent("Lec2");
        intent.putExtra("pgn", pgn);
        pgn = "null";
        mContext.sendBroadcast(intent);
        mask = new Mask();
        invalidate();
    }

    public void init() {
        if (LecureSave.saveBoard == null)
            mChessBoard = new ChessBoard();
        else
            mChessBoard = LecureSave.saveBoard;

    }

    protected void doDraw() {

        if (surfaceFlag) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    Canvas canvas = holder.lockCanvas(null);
                    //보드판 그리기
                    if (LecureSave.callFragment.startsWith("Lec1"))
                        drawBoard(canvas);
                    else if (LecureSave.callFragment.startsWith("Lec2"))
                        drawBoard2(canvas);
                    drawMarks(canvas);
                    drawPiece(canvas);
                    holder.unlockCanvasAndPost(canvas);
                }
            });
            thread.start();
        }
    }

    public void invalidate() {
        super.invalidate();
        doDraw();
    }

    //마크 그리는 함수
    protected void drawMarks(Canvas canvas) {
        if (markList != null) {
            float width = getWidth() / 8;
            float heigth = getHeight() / 8;
            for (int i = 0; i < markList.size(); i++) {
                Bitmap sm;
                int row = markList.get(i).row;
                int col = markList.get(i).col;
                Log.d("디버깅", "마크사이즈는" + markList.size());
                if (markList.get(i).pos == 1) {
                    sm = BitmapFactory.decodeResource(getResources(), R.drawable.circle_icon);
                    canvas.drawBitmap(sm, null, new Rect((int) (col * width), (int) ((7 - row) * heigth), (int) ((col + 1) * width), (int) ((7 - row + 1) * heigth)), null);
                    sm.recycle();
                }
                if (markList.get(i).pos == 2) {
                    sm = BitmapFactory.decodeResource(getResources(), R.drawable.x_icon);
                    canvas.drawBitmap(sm, null, new Rect((int) (col * width), (int) ((7 - row) * heigth), (int) ((col + 1) * width), (int) ((7 - row + 1) * heigth)), null);
                    sm.recycle();
                    Log.d("디버깅", "엑스그려짐");
                }
                Paint paint = new Paint();
                if (markList.get(i).pos == 3) {//빨간색 큐브
                    paint.setColor(Color.RED);
                    canvas.drawRect((col * width), ((7 - row) * heigth), ((col + 1) * width), ((7 - row + 1) * heigth), paint);
                }
                if (markList.get(i).pos == 4) {//파란색 큐브
                    paint.setColor(Color.BLUE);
                    canvas.drawRect((col * width), ((7 - row) * heigth), ((col + 1) * width), ((7 - row + 1) * heigth), paint);
                }
                if (markList.get(i).pos == 5) {//초록색 큐브
                    paint.setColor(Color.GREEN);
                    canvas.drawRect((col * width), ((7 - row) * heigth), ((col + 1) * width), ((7 - row + 1) * heigth), paint);
                }
                if(markList.get(i).pos==6)
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

    //보드판 그리는 함수
    protected void drawBoard(Canvas canvas) {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chessboard_03);
        canvas.drawBitmap(bitmap , null,new Rect(0 ,0 ,getWidth(), getHeight()),null);



        float width = getWidth() / 8;
        float heigth = getHeight() / 8;
        boolean WBflag = true;
        /*
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Paint Pnt = new Paint();
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
        }*/
    }

    //보드판 그리는 함수
    protected void drawBoard2(Canvas canvas) {
        float width = getWidth() / 8;
        float heigth = getHeight() / 8;
        boolean WBflag = true;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chessboard_03);
        canvas.drawBitmap(bitmap , null,new Rect(0 ,0 ,getWidth(), getHeight()),null);

        /*
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
        }*/
        if (movingFlag) {
            ArrayList<Integer> x = mask.getList_X();
            ArrayList<Integer> y = mask.getList_Y();

            for (int i = 0; i < x.size(); i++) {
                Paint Pnt = new Paint();
                Pnt.setColor(Color.RED);

                int tempX = x.get(i) - 1;
                int tempY = 8 - y.get(i);
                canvas.drawRect(tempX * width, tempY * heigth, (tempX + 1) * width, (tempY + 1) * heigth, Pnt);
            }
            Paint Pnt = new Paint();
            Pnt.setColor(Color.rgb(245, 34, 214));
            if (mask.mX != -1) {
                int pickedX = mask.mX - 1;
                int pickedY = 8 - mask.mY;
                canvas.drawRect(pickedX * width, pickedY * heigth, (pickedX + 1) * width, (pickedY + 1) * heigth, Pnt);
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
        surfaceFlag = true;
        invalidate();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
