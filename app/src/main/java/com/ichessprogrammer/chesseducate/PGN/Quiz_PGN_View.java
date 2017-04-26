package com.ichessprogrammer.chesseducate.PGN;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.ichessprogrammer.chesseducate.ChessBoard;
import com.ichessprogrammer.chesseducate.Diolog.PromotionFragment;
import com.ichessprogrammer.chesseducate.R;

public class Quiz_PGN_View extends AppCompatActivity {

    MyBroadcastReceiver receiver;
    PromotionFragment promotionDialog;
    Button button;
    Quiz_Board_Fragment frag1;
    public clearTimeChecker Checker = new clearTimeChecker();
    private FrameLayout chessboardBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_pgn_quiz);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        IntentFilter mainFilter = new IntentFilter("Quiz_PGN");
        receiver = new MyBroadcastReceiver();
        registerReceiver(receiver, mainFilter);

        promotionDialog = new PromotionFragment();

        chessboardBg = (FrameLayout) findViewById(R.id.backgrounds);
        chessboardBg.setBackgroundResource(R.drawable.chessboardbg_03);

        /*
        */
    }

    @Override
    protected void onStart() {

        super.onStart();
        frag1 = (Quiz_Board_Fragment) getFragmentManager().findFragmentById(R.id.fragment1);
        if (PGNsave.saveData != null)
            frag1.init(PGNsave.saveData);
        Checker = new clearTimeChecker();
        Checker.start();
    }
    protected void onStop()
    {
        super.onStop();
        Checker.flag=false;
    }

    public void onWindowFocusChanged(boolean hasFocus) {

        //Fragment fragment = (Fragment)findViewById(R.layout.explain_fragment);

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);

        PGNsave.saveData = null;
        super.onDestroy();
    }

    public void ONCLICK(View view) {
        ChessBoard mChessBoard;

        final Quiz_Board_Fragment fragment = (Quiz_Board_Fragment) getFragmentManager().findFragmentById(R.id.fragment1);
        mChessBoard = fragment.myLayout.mChessBoard;
        int x = (int) fragment.myLayout.col + 1;
        int y = (int) fragment.myLayout.row + 1;

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

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //TODO: React to the Intent received.
            String tag = intent.getStringExtra("tag");
            if (tag.startsWith("Promotion")) {
                FragmentManager fm = getSupportFragmentManager();

                promotionDialog.show(fm, "안녕?");
            } else if (tag.startsWith("clear")) {
                Quiz_PGN_View.this.finish();
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
                    frag1.ClearTime++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
