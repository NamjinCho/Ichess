package com.ichessprogrammer.chesseducate.Problem;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ichessprogrammer.chesseducate.ChessBoard;
import com.ichessprogrammer.chesseducate.FEN;
import com.ichessprogrammer.chesseducate.Gallery;
import com.ichessprogrammer.chesseducate.KakaoTalkMainActivity;
import com.ichessprogrammer.chesseducate.R;
import com.ichessprogrammer.chesseducate.SettingActivity;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

/**
 * Created by 남지니 on 2016-07-24.
 */
public class MakeBoard extends Activity {
    public static MakeBoard me;
    public ChessDraw_prob myDraw;

    make_prob_piece_fragment frag2;
    MyBroadcastReceiver receiver;
    public FrameLayout setBackgroundColor;
    public FrameLayout chessboardBg;

    @Override
    protected void onCreate(Bundle ic) {
        super.onCreate(ic);
        setContentView(R.layout.layout_activity_make_prob_1);
        setBackgroundColor = (FrameLayout)findViewById(R.id.RootView);
        IntentFilter mainFilter = new IntentFilter("prob1");
        receiver = new MyBroadcastReceiver();
        registerReceiver(receiver, mainFilter);
        TextView textView = (TextView)findViewById(R.id.Title);
        textView.setText("문제 만들기");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().toString().startsWith("메인")) {
                    finish();
                } else if (item.getTitle().toString().startsWith("갤러리")) {
                    startActivity(new Intent(MakeBoard.this, Gallery.class));
                } else if (item.getTitle().toString().startsWith("설정")) {
                    startActivity(new Intent(MakeBoard.this, SettingActivity.class));
                } else if (item.getTitle().toString().startsWith("동영상")) {

                } else if (item.getTitle().toString().startsWith("약도")) {

                } else if (item.getTitle().toString().startsWith("로그아웃")) {

                    KakaoTalkMainActivity.trimCache(getApplicationContext());
                    SharedPreferences preferences = getSharedPreferences("Ichess", 0);
                    if (preferences.getString("Password", "null").startsWith("kakao")) {
                        UserManagement.requestLogout(new LogoutResponseCallback() {
                            @Override
                            public void onCompleteLogout() {
                                //
                                Log.i("로그아웃", "> onCompleteLogout()");
                            }
                        });
                    }
                    preferences.edit().putString("ID", "null").commit();
                    preferences.edit().remove("Password").commit();
                    //clearApplicationCache(null);
                    Intent intent = new Intent(MakeBoard.this, KakaoTalkMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
    }

    protected void onStart() {
        super.onStart();
        chessboardBg = (FrameLayout)findViewById(R.id.backgrounds);
        chessboardBg.setBackgroundResource(R.drawable.chessboardbg_06);
        make_prob_board_fragment frag1 = (make_prob_board_fragment) getFragmentManager().findFragmentById(R.id.boardfragment);
        myDraw = frag1.myLayout;
        frag2 = (make_prob_piece_fragment) getFragmentManager().findFragmentById(R.id.piecefragment);
    }

    protected void onStop() {
        chessboardBg = null;
        super.onStop();
        //finish();
    }

    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        ProblemSave.FEN = null;
        ProblemSave.datas = null;
        startActivity(new Intent(this,make_prob_select.class));
        finish();
    }
    public void BackButton(View v)
    {
        onBackPressed();
        //myCategory = getIntent().getStringExtra("Category");
    }
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(receiver);
        System.gc();
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            boolean fenUp = intent.getBooleanExtra("FEN", false);
            boolean flag;
            flag = intent.getBooleanExtra("flag", false);
            boolean pos = false;
            pos = intent.getBooleanExtra("posUpdate", false);
            boolean clear = intent.getBooleanExtra("Clear", false);
            boolean defaults = intent.getBooleanExtra("Default", false);
            if (fenUp) {

                String myFen = intent.getStringExtra("FENString");
                if (myFen.split("/").length >= 8) {
                    myDraw.mChessBoard.board = FEN.ReadFEN(myFen);
                    myDraw.invalidate();
                }
            }
            if (clear) {
                myDraw.mChessBoard = new ChessBoard();
                myDraw.invalidate();
            }
            if (defaults) {
                myDraw.mChessBoard=new ChessBoard();
                myDraw.mChessBoard.makeWhitePIN();
                myDraw.mChessBoard.makeBlackPIN();
                myDraw.invalidate();
            }
            if (pos) {
                frag2.donDrawPos = -1;
                frag2.update();
            }
            if (flag && myDraw.mChessBoard.wKing && myDraw.mChessBoard.bKing) {//TODO: React to the Intent received .
                Log.d("디버깅", "알로하2");
                ProblemSave.CallActivity = "prob2";
                ProblemSave.FEN = FEN.SetFEN(myDraw.mChessBoard);
                Intent intent1 = new Intent(getApplicationContext(), make_prob_2.class);
                startActivity(intent1);
                unregisterReceiver(this);
                finish();
            } else {
                myDraw.pos = intent.getIntExtra("Pos", 0);
                Log.d("디벅스", myDraw.pos + "");
            }
        }
    }
}
