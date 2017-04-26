package com.ichessprogrammer.chesseducate.Problem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ichessprogrammer.chesseducate.BoardDB;
import com.ichessprogrammer.chesseducate.ChessBoard;
import com.ichessprogrammer.chesseducate.Diolog.Custom_Dialog2;
import com.ichessprogrammer.chesseducate.Diolog.Custom_Dialog3;
import com.ichessprogrammer.chesseducate.Diolog.PromotionFragment;
import com.ichessprogrammer.chesseducate.FEN;
import com.ichessprogrammer.chesseducate.MyInformation;
import com.ichessprogrammer.chesseducate.PGNController;
import com.ichessprogrammer.chesseducate.R;
import com.ichessprogrammer.chesseducate.Server.PHPConnector;
import com.ichessprogrammer.chesseducate.lecture.BoardMark;
import com.ichessprogrammer.chesseducate.lecture.LecAndProb;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 남지니 on 2016-08-19.
 */
public class ProblemView extends AppCompatActivity  {
    ArrayList<String>mAnswerList;
    ArrayList<String>mPreMoveList;
    ArrayList<String>mExplainList;
    String []mRankingList;
    TextView explainText;
    boolean mNextFlag=true;
    String CurFEN="";
    //ArrayList<String>mHintList;
    int curAnswerPos=0;
    HashMap<String,ArrayList<BoardMark>> mHintList;
    int getDataPos=0;
    int Point = 3;

    PromotionFragment promotionDialog;
    view_prob_board_fragment frag1;
    MyBroadcastReceiver receiver;
    int mClearTime = 0;
    clearTimeChecker checker;
    ListView mListView;
    Button buttonHint;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String returnedValue =(String)msg.obj;
            if(returnedValue.startsWith("updateRanking"))
            {
                mListView.setAdapter(new ArrayAdapter<String>(ProblemView.this,android.R.layout.simple_list_item_1,mRankingList));
                Log.d("업데이트","왜 안하냐");
            }
        }
    };
    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.layout_activity_view_prob);
        init();
        IntentFilter mainFilter = new IntentFilter("probView");
        receiver= new MyBroadcastReceiver();
        registerReceiver(receiver, mainFilter);
        frag1 = (view_prob_board_fragment)getFragmentManager().findFragmentById(R.id.boardfragment);
        frag1.Position=getDataPos;
        promotionDialog = new PromotionFragment();
        mListView = (ListView)findViewById(R.id.listViewRanking);
        CurFEN = LecAndProb.dataList.get(getDataPos).mFen;
        explainText = (TextView)findViewById(R.id.problemExplain);
        explainText.setText(mExplainList.get(curAnswerPos));
        checker = new clearTimeChecker();
        checker.start();
        buttonHint= (Button)findViewById(R.id.buttonHint);
        buttonHint.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mHintList.get((curAnswerPos+1)+"_Hint")!=null)
                        {
                            frag1.myLayout.markList=mHintList.get((curAnswerPos+1)+"_Hint");
                            frag1.myLayout.invalidate();
                            frag1.myLayout.movingFlag=true;
                            if(Point>1)
                                Point--;
                        }
                    }
                }
        );
    }
    protected void onStart()
    {
        super.onStart();
        movePrePiece();
    }
    public void movePrePiece()
    {

            String PreMove = mPreMoveList.get(curAnswerPos);
            if(PreMove!=null && !PreMove.contains("null"))
            {
                boolean wb;
                if(PreMove.contains("(W)"))
                    wb=true;
                else
                    wb=false;
                if(wb)
                    PreMove=PreMove.replace("(W)","");
                else
                    PreMove=PreMove.replace("(B)","");
                PGNController controller = new PGNController();
                frag1.myLayout.mChessBoard=controller.readPGN(frag1.myLayout.mChessBoard,PreMove,wb);
                frag1.myLayout.invalidate();
                frag1.myLayout.movingFlag=true;
        }
        CurFEN = FEN.SetFEN(frag1.myLayout.mChessBoard);
        frag1.myLayout.WB = mAnswerList.get(curAnswerPos).contains("(W)");
    }
    private void init()
    {
        mAnswerList = new ArrayList<>();
        mPreMoveList = new ArrayList<>();
        mHintList = new HashMap<>();
        mExplainList=new ArrayList<>();
        if(mNextFlag)
            getDataPos=Integer.parseInt(getIntent().getStringExtra("Pos"));
        LecAndProb.ProbData tempData = LecAndProb.dataList.get(getDataPos);
        getRanking ger = new getRanking();
        ger.start();
        int index=2;
        int start = tempData.answer.indexOf("("+index+".");
        while (start!=-1)
        {
            int last  = tempData.answer.indexOf(")/(",start);
            Log.d("디버깅",start+"/"+last);
            if(last==-1)
                last=tempData.answer.length()-2;
            if(index<10)
                start=start+3;
            else if(index<100)
                start=start+4;
            else
                start=start+5;
            String subTemp = tempData.answer.substring(start,last);

            String []subTemp2=subTemp.split("/");
            Log.d("디버깅",subTemp);
            mAnswerList.add(subTemp2[0].trim().trim());
            mPreMoveList.add(subTemp2[1].trim().trim());
            index++;
            start = tempData.answer.indexOf("("+index+".");
        }
        index=1;
        start = tempData.explain.indexOf("("+index+".");
        while (start!=-1)
        {
            int last  = tempData.explain.indexOf(")/",start);
            Log.d("디버깅",start+"/"+last);
            if(last==-1)
                last=tempData.explain.length()-2;
            if(index<10)
                start=start+3;
            else if(index<100)
                start=start+4;
            else
                start=start+5;
            String subTemp = tempData.explain.substring(start,last);
            mExplainList.add(subTemp);
            index++;
            start = tempData.explain.indexOf("("+index+".");
        }
        if(!tempData.hint.equals("")) {
            String[] subHint = tempData.hint.split("/");

            for (int i = 0; i < subHint.length; i++) {
                subHint[i] = subHint[i].replace("(", "");
                subHint[i] = subHint[i].replace(")", "");
                Log.d("디버깅", subHint[i]);
                String[] temp = subHint[i].split(". ");
                Log.d("디버깅", "디버깅" + temp[0]);
                int hIndex = Integer.parseInt(temp[0]);
                if (mHintList.get(hIndex + "_Hint") == null) {
                    ArrayList<BoardMark> tempHintList = new ArrayList<>();
                    mHintList.put(hIndex + "_Hint", tempHintList);
                }
                ArrayList<BoardMark> tempHintList = mHintList.get(hIndex + "_Hint");
                temp[1] = temp[1].trim();
                if (temp[1].charAt(0) != '6') {
                    BoardMark mark = new BoardMark();
                    mark.pos = Integer.parseInt(temp[1].charAt(0) + "");
                    mark.row = Integer.parseInt(temp[1].charAt(1) + "");
                    mark.col = Integer.parseInt(temp[1].charAt(2) + "");
                    tempHintList.add(mark);
                } else {
                    BoardMark mark = new BoardMark();
                    mark.pos = Integer.parseInt(temp[1].charAt(0) + "");
                    mark.row = Integer.parseInt(temp[1].charAt(1) + "");
                    mark.col = Integer.parseInt(temp[1].charAt(2) + "");
                    mark.row2 = Integer.parseInt(temp[1].charAt(3) + "");
                    mark.col2 = Integer.parseInt(temp[1].charAt(4) + "");
                    tempHintList.add(mark);
                }
                mHintList.put(hIndex + "_Hint", tempHintList);
            }
        }

    }
    protected void onDestroy()
    {
        super.onDestroy();
        checker.flag=false;
        unregisterReceiver(receiver);
    }
    public void ONCLICK(View view) {
        ChessBoard mChessBoard;

        final view_prob_board_fragment fragment = (view_prob_board_fragment)getFragmentManager().findFragmentById(R.id.boardfragment);
        mChessBoard = fragment.myLayout.mChessBoard;
        int x = (int)fragment.myLayout.col + 1;
        int y = (int)fragment.myLayout.row + 1;

        switch (view.getId()) {
            case R.id.rook:
                Log.i("onclick", " 1");
                mChessBoard.board[x][y].myPIN.Type = "R";
                fragment.myLayout.readChessBoard(mChessBoard, "R");

                promotionDialog.dismiss();
                break;
            case R.id.knight:
                Log.i("onclick", " 2");

                mChessBoard.board[x][y].myPIN.Type = "N";
                fragment.myLayout.readChessBoard(mChessBoard, "N");

                promotionDialog.dismiss();
                break;
            case R.id.bishop:
                Log.i("onclick", " 3");


                mChessBoard.board[x][y].myPIN.Type = "B";
                fragment.myLayout.readChessBoard(mChessBoard, "B");

                promotionDialog.dismiss();
                break;
            case R.id.queen:
                Log.i("onclick", " 4");

                mChessBoard.board[x][y].myPIN.Type = "Q";
                fragment.myLayout.readChessBoard(mChessBoard, "Q");

                promotionDialog.dismiss();
                break;
        }
    }
    public void showDialog2(String text)
    {
        final Custom_Dialog3 dialog2 = new Custom_Dialog3(ProblemView.this, R.style.AlertDialogCustom,ProblemView.this);
        dialog2.setContentView(R.layout.dialog_prompt2);
        final TextView textView = (TextView)dialog2.findViewById(R.id.promtext);
        textView.setText(text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.cancel();
            }
        });
        dialog2.show();
        if(text.startsWith("모든 정답"))
            dialog2.flag=true;
        else
            dialog2.flag=false;
    }
    public void mExit()
    {
        ProblemView.this.finish();
    }
    public void showDialog(String text)
    {
        final Custom_Dialog2 dialog2 = new Custom_Dialog2(ProblemView.this, R.style.AlertDialogCustom,ProblemView.this);
        dialog2.setContentView(R.layout.dialog_prompt2);
        final TextView textView = (TextView)dialog2.findViewById(R.id.promtext);
        textView.setText(text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.cancel();
            }
        });
        dialog2.show();
        if(text.startsWith("정답")) {
            dialog2.flag = true;
        }
    }
    public void updateFromDialog(boolean flag)
    {
        if(flag)
        {
            Log.d("디버깅","정답 입니다.");
            if(mAnswerList.size()>curAnswerPos) {
                frag1.myLayout.markList=null;
                explainText.setText(mExplainList.get(curAnswerPos));
                movePrePiece();
                frag1.myLayout.invalidate();
            }else
            {
                checker.flag=false;
                if(MyInformation.ClearProbID.contains(LecAndProb.dataList.get(getDataPos).probID)) {
                    String Parms = "";
                    Parms = "probID=" + LecAndProb.dataList.get(getDataPos).probID + "&id=" + MyInformation.ID +
                            "&point=" + Point + "&time=" + mClearTime+"&table=clearProblem_"+ BoardDB.Title.get(BoardDB.curPos);
                    new PHPConnector().ConnectServer(Parms,"updateClearProblem.php","icp");
                    MyInformation.ClearProbID.add(LecAndProb.dataList.get(getDataPos).probID);
                    MyInformation.ClearProbPoint.put(LecAndProb.dataList.get(getDataPos).probID,Point+"");
                }
                else{
                    String Parms = "";
                    Parms = "probID=" + LecAndProb.dataList.get(getDataPos).probID + "&id=" + MyInformation.ID +
                            "&point=" + Point + "&time=" + mClearTime +"&table=clearProblem_"+ BoardDB.Title.get(BoardDB.curPos);
                    new PHPConnector().ConnectServer(Parms,"insertClearProblem.php","icp");
                    MyInformation.ClearProbID.add(LecAndProb.dataList.get(getDataPos).probID);
                    MyInformation.ClearProbPoint.put(LecAndProb.dataList.get(getDataPos).probID,Point+"");
                    if(LecAndProb.dataList.get(getDataPos).minLevel.equals(""+MyInformation.level))
                    {
                        MyInformation.exp+=20;
                        if(MyInformation.exp>=100) {
                            MyInformation.level++;
                            MyInformation.exp=0;
                        }
                        String Params = "ID="+MyInformation.ID +"&level="+MyInformation.level+"&exp="+MyInformation.exp;
                        new PHPConnector().ConnectServer(Params,"UpdateMyInfo.php","Updates");
                    }
                }
                getDataPos++;
                curAnswerPos=0;
                boolean nextQuiz =false;
                if(getDataPos < LecAndProb.dataList.size())
                    nextQuiz=true;
                if (MyInformation.level>=50 && getDataPos == LecAndProb.dataList.size()-1)
                    nextQuiz=false;

                if(nextQuiz)
                {
                    mHintList=null;
                    mRankingList=null;
                    mAnswerList=null;
                    mPreMoveList=null;
                    mNextFlag = false;
                    init();
                    frag1.Position=getDataPos;
                    Log.d("펀값",CurFEN);
                    CurFEN = LecAndProb.dataList.get(getDataPos).mFen;
                    Log.d("펀값2",CurFEN);
                    frag1.myLayout.mChessBoard.board=FEN.ReadFEN(CurFEN);
                    frag1.myLayout.invalidate();
                    movePrePiece();
                    if(Integer.parseInt(LecAndProb.dataList.get(getDataPos).minLevel)>MyInformation.level)
                    {
                        Toast.makeText(getApplicationContext(),"레벨이 낮아 다음 문제를 볼 수 없습니다.",Toast.LENGTH_SHORT).show();
                        finish();
                    }else
                        showDialog2("모든 정답을 맞추셨습니다.\n다음 문제로 넘어갑니다.");
                }
                else {
                    showDialog2("모든 문제를 맞추셨습니다.\n처음 화면으로 돌아갑니다.");
                }
            }
        }
        else
        {
            if(Point>1)
                Point--;
            frag1.myLayout.mChessBoard.board= FEN.ReadFEN(CurFEN);
            frag1.myLayout.invalidate();
            frag1.myLayout.WB=!frag1.myLayout.WB;
            frag1.myLayout.movingFlag = true;
        }
    }
    public void newGame()
    {
        checker = new clearTimeChecker();
        checker.start();
    }
    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String pgn = intent.getStringExtra("pgn");
            String tag = intent.getStringExtra("tag");
            if(tag!=null && tag.equals("Promotion"))
            {
                FragmentManager fm = getSupportFragmentManager();

                promotionDialog.show(fm, "안녕?");
                return;
            }
            if(pgn!=null) {
                if (!frag1.myLayout.WB)
                    pgn += "(W)";
                else
                    pgn += "(B)";
                if(mAnswerList.get(curAnswerPos)!=null && mAnswerList.get(curAnswerPos).equals(pgn))
                {
                    showDialog("정답 입니다.");
                    curAnswerPos++;

                }
                else
                {
                    showDialog("오답 입니다.");
                }
            }
        }
    }
    public class getRanking extends Thread{

        public void run()
        {
            ArrayList<String>mRankingList2;
            new PHPConnector().ConnectServer("probID="+LecAndProb.dataList.get(getDataPos).probID
                    +"&table=clearProblem_"+ BoardDB.Title.get(BoardDB.curPos),"getProblemRanking.php","ranking");
            String result;
            while (true)
            {
                result = PHPConnector.MessageQue.get("ranking");
                if(result!=null)
                {
                    break;
                }
            }
            if(!result.startsWith("False")) {
                Log.d("안녕", "그래요");
                mRankingList2 = new ArrayList<>();
                String[] subResult = result.split(",");
                for (int i = 0; i < subResult.length; i = i + 2) {
                    String Data = "이름 : " + subResult[i] + "\n" + "시간 : " + subResult[i + 1];
                    mRankingList2.add(Data);
                }
                mRankingList = new String[mRankingList2.size()];
                for (int i = 0; i < mRankingList2.size(); i++) {
                    mRankingList[i] = mRankingList2.get(i);
                }
                Message msg = mHandler.obtainMessage(1, "updateRanking");
                mHandler.sendMessage(msg);
            }
        }
    }
    public class clearTimeChecker extends Thread{

        boolean flag=true;
        public void run()
        {
            while(flag)
            {
                try {
                    sleep(1000);
                    mClearTime++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
