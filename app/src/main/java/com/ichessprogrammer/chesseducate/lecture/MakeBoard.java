package com.ichessprogrammer.chesseducate.lecture;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
    public ChessDraw myDraw;
    public static MakeBoard me;
    public static final String NEXT_PAGE = "NEXT_PAGE";
    MyBroadcastReceiver receiver;
    make_lec_piece_fragment frag2;

    FrameLayout chessboardBg;
    FrameLayout setBackgroundColor;
    ScrollView showPgnScroll;
    LinearLayout showMarkLayout;

    LinearLayout blackBoardLayout, whiteBoardLayout, inputFenLayout;

    Drawable previousImage, nextImage;



    boolean clickButton = false;
    FrameLayout multiButton; // solutionAndLectureStartButton 의미. 너무 길어서

    TextView[] textViews = new TextView[13];
    TextView[] locationTexts = new TextView[16];
    TextView lectureStart, fenTextBackground;

    int[] textViewsId = {R.id.inputText, R.id.defaultText1, R.id.defaultText2, R.id.flipText1, R.id.flipText2, R.id.resetText1, R.id.resetText2,
            R.id.solutionText1, R.id.solutionText2, R.id.previousText1, R.id.previousText2, R.id.nextText1, R.id.nextText2};
    int[] locationTextsId = {R.id.location8, R.id.location7, R.id.location6, R.id.location5, R.id.location4, R.id.location3, R.id.location2, R.id.location1,
            R.id.locationA, R.id.locationB, R.id.locationC, R.id.locationD, R.id.locationE, R.id.locationF, R.id.locationG, R.id.locationH};
    String[] locationTextsName = {};


   // TextView solutionText1, solutionText2, lectureText;

    @Override
    protected void onCreate(Bundle ic) {
        super.onCreate(ic);
        setContentView(R.layout.layout_activity_make_lec_1);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        IntentFilter mainFilter = new IntentFilter(NEXT_PAGE);
         receiver= new MyBroadcastReceiver();
        registerReceiver(receiver, mainFilter);

        //기본적인 세팅

        multiButton = (FrameLayout)findViewById(R.id.solutionSetting);
        setBackgroundColor = (FrameLayout) findViewById(R.id.RootView);
        fenTextBackground = (TextView)findViewById(R.id.editFEN);
        lectureStart=(TextView)findViewById(R.id.lectureText);


        previousImage = ((ImageView)findViewById(R.id.previousImage)).getDrawable();
        nextImage = ((ImageView)findViewById(R.id.nextImage)).getDrawable();
        previousImage.setAlpha(100);
        nextImage.setAlpha(100);


        for(int i = 0; i < 13; i++) {
            textViews[i] = (TextView)findViewById(textViewsId[i]);
        }
        for(int i = 9; i < 13; i++) { //pre 랑 next 에만 해당
            textViews[i].setTextColor(Color.argb(128,110,154,202));
        }

        blackBoardLayout = (LinearLayout)findViewById(R.id. black_board);
        whiteBoardLayout = (LinearLayout)findViewById(R.id.white_board);
        inputFenLayout =(LinearLayout)findViewById(R.id.input_fen_layout);
        showPgnScroll = (ScrollView) findViewById(R.id.showPgnScrollView);
        showMarkLayout = (LinearLayout)findViewById(R.id.showMarkLayout);


        multiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MakeBoard","makeboard");
                if(!clickButton) {
                    clickButton = true;
                    setBackgroundColor.setBackgroundColor(Color.rgb(77, 129, 118));

                    for (int i = 0; i < 13; i++) {
                        textViews[i].setTextColor(Color.rgb(77, 129, 118));
                    }
                    for(int i = 9; i < 13; i++) { //pre 랑 next 에만 해당
                        textViews[i].setTextColor(Color.argb(255, 77, 129, 118));
                    }
                    textViews[7].setVisibility(View.INVISIBLE);
                    textViews[8].setVisibility(View.INVISIBLE);
                    lectureStart.setTextColor(Color.rgb(77, 129, 118));
                    lectureStart.setVisibility(View.VISIBLE);
                    fenTextBackground.setBackgroundResource(R.drawable.bg_input_green);



                    blackBoardLayout.setVisibility(View.INVISIBLE);
                    whiteBoardLayout.setVisibility(View.INVISIBLE);
                    inputFenLayout.setVisibility(View.INVISIBLE);

                    showPgnScroll.setVisibility(View.VISIBLE);
                    showMarkLayout.setVisibility(View.VISIBLE);

                    previousImage.setAlpha(255);
                    nextImage.setAlpha(255);

                    LecureSave.saveBoard = myDraw.mChessBoard;
                    LecureSave.FEN = FEN.SetFEN(myDraw.mChessBoard);
                    LecureSave.firstFEN=LecureSave.FEN;
                    LecureSave.callFragment="Lec2";
                    Log.d("FEN", LecureSave.FEN);
                }
                else {

                }
            }
        });
        TextView textView = (TextView)findViewById(R.id.Title);
        textView.setText("강의 만들기");
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
        make_lec_board_fragment frag1 = (make_lec_board_fragment) getFragmentManager().findFragmentById(R.id.boardfragment);
        myDraw = frag1.myLayout;
        frag2 = (make_lec_piece_fragment) getFragmentManager().findFragmentById(R.id.piecefragment);
        chessboardBg = (FrameLayout) findViewById(R.id.backgrounds);
        chessboardBg.setBackgroundResource(R.drawable.chessboardbg_06);
        //myPieceDraw = frag2.myLayout;
        LecureSave.callFragment = "Lec1";
    }
    protected void onStop()
    {
        chessboardBg = null;
        super.onStop();
        //finish();
    }
    protected void onRestart() {
        super.onRestart();
        LecureSave.callFragment = "Lec1";
    }

    public void BackButton(View v)
    {
        onBackPressed();
        //myCategory = getIntent().getStringExtra("Category");
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();
        LecureSave.callFragment="";
        LecureSave.FEN="";
        LecureSave.saveBoard=null;
        LecureSave.PGN_Explain=null;
        LecureSave.firstFEN="";
        startActivity(new Intent(this,LecturLoading.class));
        finish();

    }
    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            boolean fenUp=intent.getBooleanExtra("FEN",false);
            boolean flag;
            flag = intent.getBooleanExtra("flag", false);
            boolean pos = false;
            pos=intent.getBooleanExtra("posUpdate",false);
            boolean clear =intent.getBooleanExtra("Clear",false);
            boolean defaults = intent.getBooleanExtra("Default",false);
            if(fenUp)
            {
                String myFen=intent.getStringExtra("FENString");
                if(myFen.split("/").length >= 8) {
                    myDraw.mChessBoard.board = FEN.ReadFEN(myFen);
                    myDraw.invalidate();
                }
            }
            if(clear)
            {
                myDraw.mChessBoard=new ChessBoard();
                myDraw.invalidate();
            }
            if(defaults)
            {
                myDraw.mChessBoard=null;
                System.gc();
                myDraw.mChessBoard = new ChessBoard();
                myDraw.mChessBoard.makeWhitePIN();
                myDraw.mChessBoard.makeBlackPIN();
                myDraw.invalidate();
            }
            if(pos)
            {
                frag2.donDrawPos=-1;
                frag2.update();
            }
            if (flag && myDraw.mChessBoard.wKing &&myDraw.mChessBoard.bKing) {//TODO: React to the Intent received.
                Intent intent1 = new Intent(getApplicationContext(), Make_Lec_Act2.class);
                LecureSave.saveBoard = myDraw.mChessBoard;
                startActivity(intent1);
                LecureSave.FEN = FEN.SetFEN(myDraw.mChessBoard);
                LecureSave.firstFEN=LecureSave.FEN;
                Log.d("FEN", LecureSave.FEN);
                unregisterReceiver(this);
                finish();
            } else
                myDraw.pos = intent.getIntExtra("Pos", 0);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(receiver);
        System.gc();
    }
}
