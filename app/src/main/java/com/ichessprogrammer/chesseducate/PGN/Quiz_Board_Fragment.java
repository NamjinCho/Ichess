package com.ichessprogrammer.chesseducate.PGN;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ichessprogrammer.chesseducate.BoardDB;
import com.ichessprogrammer.chesseducate.ChessBoard;
import com.ichessprogrammer.chesseducate.FEN;
import com.ichessprogrammer.chesseducate.MyInformation;
import com.ichessprogrammer.chesseducate.PGNController;
import com.ichessprogrammer.chesseducate.PGNIndex;
import com.ichessprogrammer.chesseducate.R;
import com.ichessprogrammer.chesseducate.Server.PHPConnector;
import com.ichessprogrammer.chesseducate.lecture.LecAndProb;

import java.util.LinkedList;


/**
 * Created by 남지니 on 2016-07-09.
 */
public class Quiz_Board_Fragment extends Fragment implements PgnTagInterface {

    static int position = 0;
    public int allClear = 0;
    public int time = 500;
    QuizReceiver quizReceiver;
    ChessBoard mChessBoard;
    ChessDraw_Quiz myLayout;
    LinkedList<PGNIndex> mPGNIndex = new LinkedList<>();
    LinkedList<TextView> textViewsList = new LinkedList<>();
    boolean isBlackPlayer = false;
    int loop = 0;
    int current = 0;
    int pgnIndex = 0;
    PGNController pgnController;
    ImageView pre;
    ImageView next;
    ImageView hint;
    Spinner spinner;
    PgnQuizList pgnQuizList = new PgnQuizList();


    CheckBox blackPlayer;
    CheckBox managerMode;

    LinkedList<PgnData> pgnDataList;
    //LinearLayout showPgnLayout;
    LinkedList<Integer> stack = new LinkedList<>();
    boolean push = false;
    LayoutInflater mInflater;
    ViewGroup mContainer;
    int Point = 3;
    int ClearTime=0;



    //추가


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Fragment 1", "Broad_Fragment");

        //그리는 것.
        //init();
        myLayout = new ChessDraw_Quiz(getActivity().getApplicationContext());
        mChessBoard = new ChessBoard();
        return myLayout;
    }



    public void processPgnTitle(String str, int i) {
        final String type = str.substring(1, str.indexOf("\"")+1);
        final String string = str.substring(str.indexOf("\"") + 1, str.lastIndexOf("\""));
        Log.d("디벅짱",type+"/"+string);
        switch (type) {
            case PGN_EVENT : pgnQuizList.events.append(i, string);
                break;
            case PGN_SITE : pgnQuizList.sites.append(i, string);
                break;
            case PGN_DATE : pgnQuizList.dates.append(i, string);
                break;
            case PGN_ROUND : pgnQuizList.rounds.append(i, string);
                break;
            case PGN_WHITE : pgnQuizList.whites.append(i, string);
                break;
            case PGN_BLACK :  pgnQuizList.blacks.append(i, string);
                break;
            case PGN_RESULT : pgnQuizList.results.append(i, string);
                break;
            case PGN_FEN : pgnQuizList.fens.append(i, string);
                Log.d("디벅짱",string+"/");
                break;
            default:
                break;
        }
    }

    public void init(String result) {

        String stringFEN = "";
        boolean bt = false;
        boolean hasFEN = false;
        allClear = 0;
        int i = 0;
        boolean finishFlag = false;
        try {
            //InputStream in = this.getResources().openRawResource(R.raw.test1);
            if (true) {
                String str = "";
                //StringBuilder buf = new StringBuilder();
                StringBuffer pgnData = new StringBuffer();
                int start = 0;
                int last;

                last = result.indexOf("||", start);
                while (last != -1) {
                    str = result.substring(start, last);
                    Log.d("디버깅", str);
                    if (str.startsWith("||") && str.length() == 2)
                        continue;
                    if (str.startsWith("[")) {
                        if(bt) {
                            pgnQuizList.pgnFile.append(i, pgnData);
                            i++;
                            pgnData = new StringBuffer();
                        }
                        bt = false;
                        processPgnTitle(str, i);
                    } else bt = true;
                    if (bt) {
                        String s = str + " ";
                        pgnData.append(s);
                    }
                    start = last + 2;
                    last = result.indexOf("||", start);
                    if (last == -1 && !finishFlag) {
                        last = result.length();
                        finishFlag = true;
                    }
                }
                pgnQuizList.pgnFile.append(i, pgnData);
            }
        } catch (Throwable t) {
            Toast.makeText(getActivity(), "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
            Log.e("error", t.toString());
        }


        PgnAdapter2 adapter2 = new PgnAdapter2();

        /*
        spinner = (Spinner) getActivity().findViewById(R.id.spinner);
        spinner.setAdapter(adapter2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pgnIndex = position;
                setInitQuiz();
                myLayout.startQuiz(mPGNIndex);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        */
        setInitQuiz();

        myLayout.startQuiz(mPGNIndex);
    }

    @Override
    public void onDetach() {
        getActivity().unregisterReceiver(quizReceiver);
        super.onDetach();
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        IntentFilter intentFilter = new IntentFilter("isCorrect");
        quizReceiver = new QuizReceiver();
        getActivity().registerReceiver(quizReceiver, intentFilter);

        next = (ImageView) getActivity().findViewById(R.id.next);
        pre = (ImageView) getActivity().findViewById(R.id.pre);
        hint = (ImageView) getActivity().findViewById(R.id.hint);
        blackPlayer = (CheckBox) getActivity().findViewById(R.id.checkBlack);
        //managerMode = (CheckBox) getActivity().findViewById(R.id.managerMode);
       // showPgnLayout = (LinearLayout) getActivity().findViewById(R.id.showPgn);
       // showPgnLayout.setVisibility(View.INVISIBLE);
        if (MyInformation.level < 50) {
          //  managerMode.setVisibility(View.INVISIBLE);
        }
        //blackPlayer.setChecked(false);
        //managerMode.setChecked(false);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.getId() == pre.getId()) {

                    if (pgnIndex > 0) {
                        pgnIndex--;
                        setInitQuiz();
                        myLayout.startQuiz(mPGNIndex);
                    } else
                        Toast.makeText(getActivity(), "처음입니다.", Toast.LENGTH_SHORT).show();
                } else if (v.getId() == next.getId()) {


                    if (pgnIndex >= allClear) {
                        Toast.makeText(getActivity(), "아직 문제를 풀지 못했습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (pgnIndex < pgnQuizList.whites.size() - 1) {
                            pgnIndex++;
                            setInitQuiz();
                            myLayout.startQuiz(mPGNIndex);
                        } else
                            Toast.makeText(getActivity(), "마지막입니다.", Toast.LENGTH_SHORT).show();
                    }
                } else if (v.getId() == hint.getId()) {
                    myLayout.hint();
                }

            }
        };

        View.OnClickListener changeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox) v).isChecked();

                switch (v.getId()) {
                    case R.id.checkBlack:
                        isBlackPlayer = isChecked;
                        if (isChecked) {
                            myLayout.reverseDraw(true);
                        } else {
                            myLayout.reverseDraw(false);
                        }
                        break;
                    /*
                    case R.id.managerMode:
                        if (isChecked) {
                            //showPgnLayout.setVisibility(View.VISIBLE);
                        } else {
                            //showPgnLayout.setVisibility(View.INVISIBLE);
                        }*/
                }
            }
        };

        pre.setOnClickListener(onClickListener);
        next.setOnClickListener(onClickListener);
        hint.setOnClickListener(onClickListener);

        //blackPlayer.setOnClickListener(changeListener);
       // managerMode.setOnClickListener(changeListener);
    }

    //포탈1
    public void setPGNIndex() {
        ChessBoard chessBoard;
        try {
            chessBoard = (ChessBoard) mChessBoard.clone();
            PGNIndex pgnIndex = new PGNIndex(chessBoard);
            pgnIndex.addOrder(pgnController.mData);
            pgnIndex.addWB(pgnController.WB);
            pgnIndex.addIndex(pgnController.number);
            pgnIndex.addLoop(pgnController.loop);
            pgnIndex.addLoopEnd(pgnController.isLoopEnd);

            mPGNIndex.add(pgnIndex);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        position++;
    }
    public void setPGNIndex(String e, String m) {

        ChessBoard chessBoard;
        try {
            chessBoard = (ChessBoard) mChessBoard.clone();
            PGNIndex pgnIndex = new PGNIndex(chessBoard);
            pgnIndex.addOrder(pgnController.mData);
            pgnIndex.addWB(pgnController.WB);
            pgnIndex.addIndex(pgnController.number);
            pgnIndex.addLoop(pgnController.loop);
            pgnIndex.addLoopEnd(pgnController.isLoopEnd);


            pgnIndex.setExplain(e);
            pgnIndex.setMark(m);

            mPGNIndex.add(pgnIndex);
        } catch (CloneNotSupportedException er) {
            er.printStackTrace();
        }
        position++;
    }

    public void initValue() {
        mPGNIndex = new LinkedList<>();
        mChessBoard = new ChessBoard();
        pgnController = new PGNController();
        textViewsList = new LinkedList<>();
        position = 0;
        loop = 0;
        current = 0;
    }




    public void setInitQuiz() {
        initValue();
        String fenData;

        try {
            ChessBoard cb = (ChessBoard) mChessBoard.clone();
            fenData = pgnQuizList.fens.get(pgnIndex, "");
            Log.d("디벅스스",fenData+"/");
            if (fenData.length() > 0) {
                FEN fen = new FEN();
                cb.board = fen.ReadFEN(fenData);
            }
            myLayout.readChessBoard(cb, 0);
            mChessBoard = (ChessBoard) cb.clone();
            setPGNIndex(null, null);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        PgnProcessing pgnProcessing = new PgnProcessing(pgnQuizList.pgnFile.get(pgnIndex).toString());
        pgnDataList = pgnProcessing.getPgnData();

        setQuiz();
    }

    public void setQuiz() {

        boolean isLoopChanged = false;
        boolean isLoopStart = false;
        int pgnSize = (getActivity().getWindowManager().getDefaultDisplay().getWidth());
        pgnSize = ((pgnSize * 4) / 9) - 200;
        String number = "";
        boolean enter = false;
        int width = pgnSize;


        //showPgnLayout.removeAllViews();


        LinearLayout linearLayout = new LinearLayout(getActivity().getApplicationContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);


        String mark = "";
        String explain = "";
        for (final PgnData pgnData : pgnDataList) {
            TextView pgnText = new TextView(getActivity().getApplicationContext());
            TextView blankText = new TextView(getActivity().getApplicationContext());

            String string = number + pgnData.getData();
            number = "";

            if (pgnData.Comment()) {
                pgnText = setCommentText(string, pgnText);
                explain = string;
            }
            else if (pgnData.isCount()) {
                number = string + " ";
                continue;
            }
            //how to control and execute  mask
            else if(pgnData.isMark()) {
                mark = pgnData.getData();
                continue;
            }
            else if (!pgnData.Comment()) pgnText = setActionText(string, pgnText);


            if (pgnData.loop() == 0) pgnText.setTextColor(Color.BLACK);
            else pgnText.setTextColor(Color.GRAY);

            if (isLoopChanged || loop != pgnData.loop()) {
                if (pgnData.loop() != 0)
                    isLoopStart = true;

                if (loop > pgnData.loop()) {
                    for (int j = mPGNIndex.size() - 1; j >= 0; j--) {
                        PGNIndex pgnIndex = mPGNIndex.get(j);
                        if (j == 0) {
                            try {
                                mChessBoard = (ChessBoard) pgnIndex.mChessBoard.clone();
                                break;
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                        }
                        if (pgnData.loop() == pgnIndex.loop && pgnIndex.WB != pgnData.WB() && pgnData.getNumber() >= pgnIndex.index) {
                            try {
                                mChessBoard = (ChessBoard) pgnIndex.mChessBoard.clone();
                                break;
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }


                String blank = "";
                //showPgnLayout.addView(linearLayout);
                linearLayout = new LinearLayout(getActivity().getApplicationContext());
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                for (int j = 0; j < pgnData.loop(); j++) {
                    blank += " ";
                }
                if (pgnData.loop() != 0) blank += "(";

                blankText.setTextColor(Color.GRAY);
                blankText.setText(blank);
                width = pgnSize;
                float w = blankText.getPaint().measureText(blank);
                width -= w;

                linearLayout.addView(blankText);
                isLoopChanged = false;

                loop = pgnData.loop();
            }

            if (!pgnData.Comment() && isLoopStart) {

                for (int j = mPGNIndex.size() - 1; j >= 0; j--) {
                    PGNIndex pgnIndex = mPGNIndex.get(j);

                    if (j == 0) {
                        try {
                            mChessBoard = (ChessBoard) pgnIndex.mChessBoard.clone();
                            break;
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }

                    if (pgnData.loop() - 1 == pgnIndex.loop && pgnIndex.WB != pgnData.WB() && pgnData.getNumber() >= pgnIndex.index) {
                        try {
                            mChessBoard = (ChessBoard) pgnIndex.mChessBoard.clone();
                            break;
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                isLoopStart = false;
            } //if문 끝


            float w = pgnText.getPaint().measureText((String) pgnText.getText());
            width -= w;

            if (width <= 0) {
                width = pgnSize - (int) w;
                //showPgnLayout.addView(linearLayout);
                linearLayout = new LinearLayout(getActivity().getApplicationContext());
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            }
            linearLayout.addView(pgnText);

            if (pgnData.LoopEnd()) {
                isLoopChanged = true;
                blankText = new TextView(getActivity().getApplicationContext());
                blankText.setTextColor(Color.GRAY);
                blankText.setText(")");
                linearLayout.addView(blankText);
            }


            //포탈3
            if (!pgnData.Comment() && !pgnData.isCount() && !pgnData.isMark()) { //순수한 pgnData
                String pgn = pgnData.getData();
                mChessBoard = pgnController.readPGN(mChessBoard, pgn, pgnData.WB(),
                        pgnData.getNumber(), pgnData.loop(), pgnData.LoopEnd());
                mChessBoard.number = pgnData.getNumber();
                setPGNIndex(explain, mark);
                mark = "";
                    /*여기2*/
            }
        }
        //showPgnLayout.addView(linearLayout);
    }

    public TextView setCommentText(String s, TextView t) {
        t.setText("{} ");
        t.setTextSize(16);

        final String s1 = s;
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), s1, Toast.LENGTH_SHORT).show();
            }
        });
        return t;
    }

    //포탈2
    public TextView setActionText(String s, TextView t) {
        final int i = position;
        t.setTextSize(16);
        t.setText(s + " ");

        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PGNIndex pgnIndex;
                try {
                    pgnIndex = mPGNIndex.get(i);
                } catch (IndexOutOfBoundsException e) {
                    pgnIndex = mPGNIndex.get(i);
                    e.printStackTrace();
                }
                try {
                    mChessBoard = (ChessBoard) pgnIndex.mChessBoard.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }


                if (isBlackPlayer == !pgnIndex.WB) {
                    mChessBoard.WB = !pgnIndex.WB; //
                    mChessBoard.number = pgnIndex.index;
                    mChessBoard.loop = pgnIndex.loop;
                    mChessBoard.isLoopEnd = pgnIndex.isLoopEnd;
                    //myLayout.isManagerMode = true;
                    myLayout.readChessBoard(mChessBoard, i);
                    cursorP(i, current);
                    current = i;


                    if (current < mPGNIndex.size() - 1 && !pgnIndex.isLoopEnd) {
                        Intent intent = new Intent("isCorrect");
                        intent.putExtra("answer", "correct");

                        Bundle b = new Bundle();
                        b.putInt("sequence", current);


                        intent.putExtras(b);
                        getActivity().sendBroadcast(intent);
                    }

                    String s = pgnIndex.index + "." + pgnIndex.mOrder;
                }


            }
        });
        textViewsList.add(t);
        return t;
    }

    public void cursorP(int c, int p) {
        if (p > 0) {
            TextView pt = textViewsList.get(p - 1);
            pt.setBackgroundColor(Color.TRANSPARENT);
        }
        TextView nt = textViewsList.get(c - 1);
        nt.setBackgroundColor(Color.YELLOW);
    }

    public class QuizReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String tag = intent.getStringExtra("answer");

            if (tag.startsWith("correct")) {
                Bundle b = intent.getExtras();
                int i = b.getInt("sequence");

                cursorP(i, current);
                current = i;
                Log.i("Board_Fragment correct", "" + i);

                boolean clear = b.getBoolean("isClear");
                if (clear) {

                    if (allClear == pgnIndex)
                        allClear = pgnIndex + 1;
                    if (allClear >= pgnQuizList.whites.size()) {
                        Toast.makeText(getActivity().getApplicationContext(), "All Clear", Toast.LENGTH_SHORT).show();
                        if (PGNsave.caller) {//강의 문제.
                            String Parms = "";
                            Parms = "probID=" + LecAndProb.dataList.get(PGNsave.Pos).probID + "&id=" + MyInformation.ID +
                                    "&point=" + myLayout.Point + "&time=" + ClearTime +"&table=clearProblem_"+ BoardDB.Title.get(BoardDB.curPos);
                            new PHPConnector().ConnectServer(Parms,"insertClearProblem.php","icp");

                            if (MyInformation.ClearProbID.contains(LecAndProb.dataList.get(PGNsave.Pos).probID)) {
                                MyInformation.ClearProbID.add(LecAndProb.dataList.get(PGNsave.Pos).probID);
                                MyInformation.ClearProbPoint.put(LecAndProb.dataList.get(PGNsave.Pos).probID, Point + "");
                                if (LecAndProb.dataList.get(PGNsave.Pos).minLevel.equals("" + MyInformation.level)) {
                                    MyInformation.exp += 20;
                                    if (MyInformation.exp >= 100) {
                                        MyInformation.level++;
                                        MyInformation.exp = 0;
                                    }
                                    String Params = "ID=" + MyInformation.ID + "&level=" + MyInformation.level + "&exp=" + MyInformation.exp;
                                    new PHPConnector().ConnectServer(Params, "UpdateMyInfo.php", "Updates");
                                }
                            }
                        }else {//일반 문제

                        }
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Clear", Toast.LENGTH_SHORT).show();
                        pgnIndex++;
                        setInitQuiz();
                        myLayout.startQuiz(mPGNIndex);
                    }
                } else {
                    MThread t = new MThread();
                    t.start();
                }
            }
            // 포탈
            else if (tag.startsWith("enemy")) {
                myLayout.invalidate();

                Bundle b = intent.getExtras();
                int i = b.getInt("sequence");

                Log.i("Board_Fragment enemy", "" + i);
                cursorP(i, current);
                current = i;


                boolean clear = b.getBoolean("isClear");
                if (clear) {
                    allClear++;
                    if (allClear >= pgnQuizList.whites.size()) {
                        Toast.makeText(getActivity().getApplicationContext(), "All Clear", Toast.LENGTH_SHORT).show();

                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Clear", Toast.LENGTH_SHORT).show();
                        pgnIndex++;
                        setInitQuiz();
                        myLayout.startQuiz(mPGNIndex);
                    }
                }

            } else if (tag.startsWith("manage")) {
                myLayout.invalidate();
            }
        }
    }

    public class MThread extends Thread {
        public void run() {
            super.run();
            try {
                Thread.sleep(time); //일단  time  500 (0.5)초로 해놓고 변환 할 수 있도록 ..
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myLayout.enemyTurn();
        }
    }
    public class PgnAdapter2 extends BaseAdapter {


        SparseArray<String> mListData = pgnQuizList.events;
        //ArrayList<String> mListData = pgnQuizList.event;

        public PgnAdapter2() {
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Context context = parent.getContext();

            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.quiz_pgn_list, parent, false);
            }


            TextView whiteP = (TextView) convertView.findViewById(R.id.event);
            whiteP.setText(mListData.get(position));
            return convertView;
        }

        public void addItem(String w, String b, String d, String r) {

        }

    }
}
