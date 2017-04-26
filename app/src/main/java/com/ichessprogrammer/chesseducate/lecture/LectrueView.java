package com.ichessprogrammer.chesseducate.lecture;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.ichessprogrammer.chesseducate.BoardDB;
import com.ichessprogrammer.chesseducate.ChessBoard;
import com.ichessprogrammer.chesseducate.FEN;
import com.ichessprogrammer.chesseducate.MyInformation;
import com.ichessprogrammer.chesseducate.PGNController;
import com.ichessprogrammer.chesseducate.R;
import com.ichessprogrammer.chesseducate.Server.PHPConnector;

import java.util.ArrayList;

/**
 * Created by 남지니 on 2016-07-25.
 */
public class LectrueView extends Activity {
    public LectureViewChessDraw myDraw;
    public ArrayList<String> backup;
    MyBroadcastReceiver receiver;
    view_lec_board_fragment frag1;
    view_lec_explain_fragment frag2;
    String fen;
    String level;
    String lecID;
    String marks;
    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.layout_activity_view_lec);
        backup = new ArrayList<>();
        IntentFilter mainFilter= new IntentFilter("ViewLec");
        receiver= new MyBroadcastReceiver();
        registerReceiver(receiver, mainFilter);
        String passingData = getIntent().getStringExtra("passData");
        fen = passingData.split("@#!")[0];
        String pgn = passingData.split("@#!")[1];
        String explain = passingData.split("@#!")[2];
        level = passingData.split("@#!")[4];
        lecID = passingData.split("@#!")[3];
        if(passingData.split("@#!").length>5)
            marks = passingData.split("@#!")[5];
        else
            marks="";
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.board = FEN.ReadFEN(fen);
        frag1=(view_lec_board_fragment)getFragmentManager().findFragmentById(R.id.boardfragment);
        frag2=(view_lec_explain_fragment)getFragmentManager().findFragmentById(R.id.explainfragment);
        frag1.myLayout.readChessBoard(chessBoard);
        frag2.mPgn = pgn;
        frag2.mExplain=explain;
        frag2.fen = fen;
        frag2.mMarks=marks;
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }
    public void onPause()
    {
        super.onPause();

    }
    public void clearLecture()
    {
        if(MyInformation.ClearLecID.containsKey(lecID))
        {
            //아무것도안함
        }
        else
        {

            //클리어 렉 테이블 업데이트
            String Params = "ID="+MyInformation.ID + "&lecID="+lecID+"&table=clearLecture_"+ BoardDB.Title.get(BoardDB.curPos);
            //MyInformation.ClearLecID.put("",lecID);
            ArrayList<String> tempList = MyInformation.ClearLecID.get(BoardDB.Title.get(BoardDB.curPos));
            if(tempList==null)
                tempList = new ArrayList<String>();
            tempList.add(lecID);
            MyInformation.ClearLecID.put(BoardDB.Title.get(BoardDB.curPos),tempList);
            new PHPConnector().ConnectServer(Params,"clearLectureUpdate.php","Clear");
            Intent intent = new Intent(getApplicationContext(),LectureLoading2.class);
            startActivity(intent);

            LectrueView.this.finish();
        }
    }
    protected void onStart()
    {
        super.onStart();
    }
    protected void onRestart()
    {
        super.onRestart();
        IntentFilter mainFilter= new IntentFilter("ViewLec");
        receiver= new MyBroadcastReceiver();
        registerReceiver(receiver, mainFilter);

    }
    protected void onStop()
    {
        super.onStop();
        unregisterReceiver(receiver);
    }
    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getBooleanExtra("Update",false))
            {
                int pos = intent.getIntExtra("Pos",0);
                String pgn = intent.getStringExtra("PGN");
                if(!pgn.equals("null")) {
                    boolean wb = intent.getBooleanExtra("WB", false);
                    PGNController controller = new PGNController();
                    String fen = FEN.SetFEN(frag1.myLayout.mChessBoard);
                    backup.add(fen);
                    pgn=pgn.trim();
                    frag1.myLayout.readChessBoard(controller.readPGN(frag1.myLayout.mChessBoard, pgn, wb));
                    controller = null;
                    System.gc();
                }else
                {
                    String fen = FEN.SetFEN(frag1.myLayout.mChessBoard);
                    backup.add(fen);
                }
                frag1.myLayout.markList=frag2.mDatas.get(pos).marks;
                if(frag1.myLayout.markList!=null)
                    Log.d("디버깅",pos+"사이즈"+frag1.myLayout.markList.size());
                frag1.myLayout.invalidate();
                Log.d(backup.get(backup.size()-1)+"ㄴㄴ","디버깅"+pgn);
            }else if(intent.getBooleanExtra("Back",false))
            {
                int pos = intent.getIntExtra("Pos",0);
                frag1.myLayout.mChessBoard.board=FEN.ReadFEN(backup.get(pos));
                frag1.myLayout.markList=frag2.mDatas.get(pos).marks;
                frag1.myLayout.invalidate();
                backup.remove(backup.size()-1);
            }else if(intent.getBooleanExtra("Clear",false))
            {
                Log.d("디버깅","중입니다");
                clearLecture();
            }

        }
    }
}
