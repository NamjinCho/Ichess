package com.ichessprogrammer.chesseducate;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ichessprogrammer.chesseducate.PGN.PgnAdapter;
import com.ichessprogrammer.chesseducate.PGN.PgnData;
import com.ichessprogrammer.chesseducate.PGN.PgnProcessing;
import com.ichessprogrammer.chesseducate.PGN.PgnTagInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


/**
 * Created by 남지니 on 2016-07-09.
 */
public class Board_Fragment extends Fragment implements PgnTagInterface{

    ChessBoard mChessBoard;
    MyBroadCastReceiver receiver;

    ChessBoard preChessBoard;
    ChessDraw myLayout;

    Spinner spinner;
    LinkedList<TextView> textViewsList = new LinkedList<>();
    LinkedList<PGNIndex> mPGNIndex = new LinkedList<>();


    int loop = 0;

    int current = 0;


    PGNController pgnController;
    FENController fenController;

    Button startPGN;
    Button loopPGN;
    Button nextPGN;
    TextView txtView;
    CheckBox blackPlayer;

    LinkedList<PgnData> pgnDataList;

    LinkedList<Integer> stack = new LinkedList<>();

    ArrayList<String> item;
    String CurrentPGN;

    static int position = 0;

    /**
     * 여기는 음 스피너를 위한 변수들
     */
    String dataPGN;

    HashMap<Integer, String> dataFEN = new HashMap<>();
    Intent intent;

    ArrayList<String> values;
    ArrayList<String> values2;
    ArrayList<String> values3;
    ArrayList<String> values4;
    String stringFEN = "";
    String curPGN="";
    String curFEN="";
    ArrayList<StringBuffer> pgnFile;

    int pos = 0;

    PgnAdapter pgnAdapter = new PgnAdapter();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Fragment 1", "Broad_Fragment");

        //그리는 것.
        myLayout = new ChessDraw(getActivity().getApplicationContext());
        mChessBoard = new ChessBoard();



        return myLayout;




    }

    boolean push = false;


    @Override
    public void onDetach() {
        getActivity().unregisterReceiver(receiver);
        super.onDetach();
    }

    public void onStart() {
        super.onStart();

        //loopPGN = (Button)getActivity().findViewById(R.id.loop);
        nextPGN = (Button) getActivity().findViewById(R.id.next);
        txtView = (TextView)getActivity().findViewById(R.id.current);
        //spinner = (Spinner)getActivity().findViewById(R.id.pgnlist_spinner);
        blackPlayer = (CheckBox)getActivity().findViewById(R.id.checkBlack);

        IntentFilter subFilter = new IntentFilter("makePgn");
        receiver = new MyBroadCastReceiver();

        getActivity().registerReceiver(receiver, subFilter);



        if(!push) {
            nextPGN.setVisibility(View.INVISIBLE);
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (v.getId() == nextPGN.getId()) {
                    PGNIndex cPgnIndex;
                    int p = current;


                    if(current < mPGNIndex.size())
                        current++;
                    if(current < mPGNIndex.size())
                        cPgnIndex = mPGNIndex.get(current);
                    else
                        cPgnIndex = mPGNIndex.get(mPGNIndex.size()-1);

                    while(cPgnIndex.loop != 0 && current < mPGNIndex.size()) {
                        current++;
                        cPgnIndex = mPGNIndex.get(current);
                    }
                    if(current < mPGNIndex.size()) {
                        mChessBoard = cPgnIndex.mChessBoard;

                        if(current+1 < mPGNIndex.size()) {
                            PGNIndex nPgnIndex = mPGNIndex.get(current + 1);
                            if (nPgnIndex.loop != 0)
                                loopPGN.setVisibility(View.VISIBLE);
                            else
                                loopPGN.setVisibility(View.INVISIBLE);
                        }

                        cursorP(current,p);
                        String s = cPgnIndex.index + "." + cPgnIndex.mOrder;
                        txtView.setText(s);

                        //Toast.makeText(getActivity().getApplicationContext(), mPGNIndex.get(current).index + "." + mPGNIndex.get(current).mOrder, Toast.LENGTH_SHORT).show();
                        myLayout.readChessBoard(mChessBoard);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "It's Over", Toast.LENGTH_SHORT).show();
                        current --;
                    }
                }
                else if (v.getId() == loopPGN.getId()) {
                    PGNIndex cPgnIndex;
                    int p = current;

                    if(current < mPGNIndex.size())
                        current++;

                    if(current < mPGNIndex.size())
                        cPgnIndex = mPGNIndex.get(current);
                    else
                        cPgnIndex = mPGNIndex.get(mPGNIndex.size()-1);


                    if(current < mPGNIndex.size()) {
                        mChessBoard = cPgnIndex.mChessBoard;

                        txtView.setTextColor(Color.GRAY);
                        if(current+1 < mPGNIndex.size()) {
                            PGNIndex nPgnIndex = mPGNIndex.get(current + 1);
                            if (nPgnIndex.loop == 0)
                                loopPGN.setVisibility(View.INVISIBLE);
                        }
                        cursorP(current,p);
                        String s = cPgnIndex.index + "." + cPgnIndex.mOrder;
                        txtView.setText(s);


                        //Toast.makeText(getActivity().getApplicationContext(), mPGNIndex.get(current).index + "." + mPGNIndex.get(current).mOrder, Toast.LENGTH_SHORT).show();
                        myLayout.readChessBoard(mChessBoard);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "It's Over", Toast.LENGTH_SHORT).show();
                        current --;
                    }
                }
                else if(v.getId() == blackPlayer.getId()) {

                    //myLayout.reverseDraw();
                }
            }
        };


        startPGN.setOnClickListener(onClickListener);
        nextPGN.setOnClickListener(onClickListener);
        loopPGN.setOnClickListener(onClickListener);
        blackPlayer.setOnClickListener(onClickListener);
    }

    public class MyBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            String tag = intent.getStringExtra("pgn");
            if(tag.startsWith("makePgn"))
            {

                Bundle bundle = intent.getExtras();
                String p = bundle.getString("pgnData");
                int num = bundle.getInt("number");
                int loop = bundle.getInt("loop");
                boolean WB = bundle.getBoolean("WB");
                boolean isLoopEnd = bundle.getBoolean("loopEnd");

                int i = 0;


                Log.i("안녕",""+current);
            }
        }
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
        while (stack.size() > loop) {
            stack.removeLast();
        }
        stack.addLast(position);

        position++;
    }


    public void setInitPgnTextView(String dataPGN, String fenData) {

        //(init)
        mPGNIndex = new LinkedList<>();
        textViewsList = new LinkedList<>();
        mChessBoard = new ChessBoard();
        txtView.setText("");
        position = 0;
        loop = 0;
        current = 0;

        try {
            ChessBoard cb = (ChessBoard) mChessBoard.clone();
            if (fenData != null) {

                FEN fen = new FEN();
                cb.board = fen.ReadFEN(fenData);
            }

            myLayout.readChessBoard(cb);
            mChessBoard = (ChessBoard) cb.clone();
            setPGNIndex();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        PgnProcessing pgnProcessing = new PgnProcessing(dataPGN);
        pgnDataList = pgnProcessing.getPgnData();

        setPgnTextView();

    }

    public void setPgnTextView() {



        //init setting
        boolean isLoopChanged = false;
        boolean isLoopStart = false;
        int pgnSize = (getActivity().getWindowManager().getDefaultDisplay().getWidth());
        pgnSize = ((pgnSize * 4) / 9) - 200;
        String number = "";
        boolean enter = false;
        int width = pgnSize;


        LinearLayout showPgnLayout;
        showPgnLayout = (LinearLayout) getActivity().findViewById(R.id.showPgn);
        showPgnLayout.removeAllViews();



        LinearLayout linearLayout = new LinearLayout(getActivity().getApplicationContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);


        for (final PgnData pgnData : pgnDataList) {

            TextView pgnText = new TextView(getActivity().getApplicationContext());
            TextView blankText = new TextView(getActivity().getApplicationContext());

            String string = number + pgnData.getData();
            number = "";

            if (pgnData.Comment()) pgnText = setCommentText(string, pgnText);
            else if (pgnData.isCount()) {
                number = string + " ";
                continue;
            } //이것도 그냥 처리 해버릴까?
            else if (!pgnData.Comment()) pgnText = setActionText(string, pgnText);


            if (pgnData.loop() == 0) pgnText.setTextColor(Color.BLACK);
            else pgnText.setTextColor(Color.GRAY);

            if (isLoopChanged || loop != pgnData.loop()) {
                if(pgnData.loop() != 0)
                    isLoopStart = true;

                if(loop > pgnData.loop()) {
                    for (int j = mPGNIndex.size() - 1; j >= 0; j--) {
                        PGNIndex pgnIndex = mPGNIndex.get(j);
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
                showPgnLayout.addView(linearLayout);
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

            if (enter || width <= 0) {
                width = pgnSize - (int) w;
                showPgnLayout.addView(linearLayout);
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
            if (!pgnData.Comment() && !pgnData.isCount()) { //순수한 pgnData
                String pgn = pgnData.getData();
                mChessBoard = pgnController.readPGN(mChessBoard, pgn, pgnData.WB(),
                        pgnData.getNumber(), pgnData.loop(), pgnData.LoopEnd());
                mChessBoard.number = pgnData.getNumber();
                setPGNIndex();
                    /*여기2*/
            }
        }
        showPgnLayout.addView(linearLayout);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("디벅스","세여"+requestCode+"/"+Activity.RESULT_OK+"/"+resultCode);

        if ((requestCode == 1234) && (resultCode == Activity.RESULT_OK)) {
            values = new ArrayList<String>();
            values2 = new ArrayList<String>();
            values3 = new ArrayList<String>();
            values4 = new ArrayList<String>();
            dataFEN = new HashMap<>();
            pgnFile = new ArrayList<>();
            pos=0;
            stringFEN="";
            PgnAdapter pgnAdapter = new PgnAdapter();
            Log.d("디벅스","세여");
            boolean bt = false;
            boolean hasFEN = false;
            View v;
            File file = new File(data.getStringExtra("result"));

            try {

                int readcount=0;
                //InputStream in = this.getResources().openRawResource(R.raw.hello);
                FileInputStream in = new FileInputStream(file);
                if (in != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String str = "";
                    //StringBuilder buf = new StringBuilder();
                    StringBuffer pgnData =  new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        if(str.startsWith("\n") && str.length()==1)
                            continue;
                        if(str.startsWith("[")) {
                            bt=false;
                            String string =  "";

                            if(str.contains(PGN_EVENTDATE)){

                            }
                            else if(str.contains(PGN_EVENT)){

                            }
                            else if (str.contains(PGN_WHITE)) {
                                string = str.substring(str.indexOf("\"")+ 1, str.lastIndexOf("\""));
                                values.add(string);
                            } else if (str.contains(PGN_BLACK)) {
                                string = str.substring(str.indexOf("\"")+ 1, str.lastIndexOf("\""));
                                values2.add(string);

                            } else if (str.contains(PGN_DATE)) {
                                string = str.substring(str.indexOf("\"")+ 1, str.lastIndexOf("\""));
                                values3.add(string);

                            } else if (str.contains(PGN_RESULT)) {
                                string = str.substring(str.indexOf("\"")+ 1, str.lastIndexOf("\""));
                                values4.add(string);
                            } else if (str.contains(PGN_FEN)) {
                                stringFEN  = str.substring(str.indexOf("\"")+ 1, str.lastIndexOf("\""));
                                Log.d("펀확",stringFEN);
                                hasFEN = true;
                            }
                        }
                        else
                            bt=true;
                        if(bt) {
                            String s = str + " ";
                            pgnData.append(s);
                        } else if(pgnData.length() > 1) {

                            if(hasFEN) {
                                dataFEN.put(pos, stringFEN);
                                stringFEN = "";
                                hasFEN = false;
                            }
                            pos++;
                            pgnFile.add(pgnData);
                            pgnData = new StringBuffer();
                        }
                    }
                    if(hasFEN) {
                        dataFEN.put(pos, stringFEN);
                        stringFEN = "";
                        hasFEN = false;
                    }
                    pgnFile.add(pgnData);
                    in.close();
                }
            } catch (java.io.FileNotFoundException e) { // that's OK, we probably haven't created it yet
            } catch (Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
                Log.e("error", t.toString());
            }
            Log.d("디벅스",values.size()+"?"+values2.size()+"?"+values3.size()+"?"+values4.size());
            for(int i = 0; i < values.size(); i++) {
                pgnAdapter.addItem(values.get(i),values2.get(i),values3.get(i),values4.get(i));
            }

            spinner.setAdapter(pgnAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int positionview, long id) {
                    Log.d("안녕","하세요");
                    curPGN=pgnFile.get(positionview).toString();
                    Log.d("안녕","하세요2");
                    String fen="";
                    curFEN=null;
                    if(dataFEN.containsKey(positionview)) {
                        Log.d("안녕","하세요3");
                        fen = dataFEN.get(positionview);
                        curFEN=fen;
                    }
                    nextPGN.setVisibility(View.VISIBLE);
                    if (curFEN!=null) {
                        Log.d("펀확",curFEN+"_");
                        setInitPgnTextView(curPGN, curFEN);
                    }
                    else
                        setInitPgnTextView(curPGN, "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //초기화 장면,
            curPGN=pgnFile.get(0).toString();
            Log.d("안녕","하세요2");
            String fen="";
            curFEN=null;
            if(dataFEN.containsKey(0)) {
                Log.d("안녕","하세요3");
                fen = dataFEN.get(0);
                curFEN=fen;
            }
            nextPGN.setVisibility(View.VISIBLE);
            if (curFEN!=null) {
                Log.d("펀확",curFEN+"_");
                setInitPgnTextView(curPGN, curFEN);
            }
            else
                setInitPgnTextView(curPGN, "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        }

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
                }catch (IndexOutOfBoundsException e) {
                    pgnIndex = mPGNIndex.get(0);
                    e.printStackTrace();
                }try{
                    mChessBoard = (ChessBoard)pgnIndex.mChessBoard.clone();
                    mChessBoard.WB = !pgnIndex.WB; //
                    mChessBoard.number = pgnIndex.index;
                    mChessBoard.loop = pgnIndex.loop;
                    mChessBoard.isLoopEnd = pgnIndex.isLoopEnd;
                    myLayout.readChessBoard(mChessBoard);


                    cursorP(i, current);

                    current = i;


                    String s = pgnIndex.index +"."+pgnIndex.mOrder;
                    txtView.setText(s);

                    if(pgnIndex.loop != 0)
                        txtView.setTextColor(Color.GRAY);
                    else
                        txtView.setTextColor(Color.BLACK);

                    if(current+1 < mPGNIndex.size()) {
                        PGNIndex nPgnIndex = mPGNIndex.get(i+1);
                        if(nPgnIndex.loop != 0)
                            loopPGN.setVisibility(View.VISIBLE);
                        else
                            loopPGN.setVisibility(View.INVISIBLE);
                    }

                }catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        });

        textViewsList.add(t);
        return t;
    }

    public void cursorP(int c, int p) {


        if(p > 0) {
            TextView pt = textViewsList.get(p - 1);
            pt.setBackgroundColor(Color.WHITE);
        }
        TextView nt = textViewsList.get(c-1);
        nt.setBackgroundColor(Color.YELLOW);
    }
}
