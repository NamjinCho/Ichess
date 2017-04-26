package com.ichessprogrammer.chesseducate.lecture;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ichessprogrammer.chesseducate.ChessBoard;
import com.ichessprogrammer.chesseducate.Diolog.Custom_Dialog;
import com.ichessprogrammer.chesseducate.Diolog.PromotionFragment;
import com.ichessprogrammer.chesseducate.FEN;
import com.ichessprogrammer.chesseducate.PGNController;
import com.ichessprogrammer.chesseducate.R;

import java.util.ArrayList;

;

/**
 * Created by 남지니 on 2016-07-25.
 */
public class Make_Lec_Act2 extends AppCompatActivity {
    public ChessDraw myDraw;
    public make_lec_explain_fragment frag2;
    public make_lec_board_fragment frag1;
    public ArrayList<ListData> backup=null;
    MyBroadcastReceiver receiver;
    Context context = this;
    int pos = -1;
    PromotionFragment promotionDialog = new PromotionFragment();
    public static int caller=1;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_activity_make_lec_2);
        LecureSave.callFragment = "Lec2";
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        IntentFilter mainFilter = new IntentFilter("Lec2");
        receiver = new MyBroadcastReceiver();
        registerReceiver(receiver, mainFilter);
    }

    protected void onStart() {
        super.onStart();
        frag1 = (make_lec_board_fragment) getFragmentManager().findFragmentById(R.id.boardfragment);
        frag2 = (make_lec_explain_fragment) getFragmentManager().findFragmentById(R.id.explainfragment);
        myDraw = frag1.myLayout;
        if(caller==2){
            String passingData = getIntent().getStringExtra("passing");
            LecureSave.FEN=passingData.split("@#!")[0];
            LecureSave.firstFEN = passingData.split("@#!")[0];
            String temppgn = passingData.split("@#!")[1];
            String tempexplain = passingData.split("@#!")[2];
            String templevel = passingData.split("@#!")[4];
            LecureSave.lecID = Integer.parseInt(passingData.split("@#!")[3]);
            String tempmarks;
            if(passingData.split("@#!").length>5)
                tempmarks = passingData.split("@#!")[5];
            else
                tempmarks="";
            frag1.myLayout.mChessBoard.board=FEN.ReadFEN(LecureSave.firstFEN);
            if(frag2.dividedatas(temppgn,tempexplain,tempmarks))
                frag1.myLayout.invalidate();
        }

    }

    protected void onRestart() {
        super.onRestart();
        IntentFilter mainFilter = new IntentFilter("Lec2");
        receiver = new MyBroadcastReceiver();
        registerReceiver(receiver, mainFilter);
        if (backup != null) {
            Log.d("디버깅", "2사이즈는 " + backup.size());
            frag2.backUpList(backup);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregisterReceiver(receiver);
    }

    // Call this method from onDestroy()
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    public void onBackPressed() {
        super.onBackPressed();
        LecureSave.callFragment = "";
        LecureSave.FEN = "";
        LecureSave.saveBoard = null;
        LecureSave.PGN_Explain = null;
        finish();
    }
    public void ONCLICK(View view) {
        ChessBoard mChessBoard;

        final make_lec_board_fragment fragment = (make_lec_board_fragment)getFragmentManager().findFragmentById(R.id.boardfragment);
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

    public void markDialog() {

        final Custom_Dialog dialog2 = new Custom_Dialog(context, R.style.AlertDialogCustom);
        dialog2.setContentView(R.layout.dialog_select_mark);
        //다이얼로그 마크 아이템 셋팅
        final ImageView circle_mark = (ImageView) dialog2.findViewById(R.id.circle_mark_icon);
        final ImageView x_mark = (ImageView) dialog2.findViewById(R.id.x_mark_icon);
        final ImageView direction_mark = (ImageView) dialog2.findViewById(R.id.direction_mark_icon);
        final ImageView red_qube = (ImageView) dialog2.findViewById(R.id.red_qube_icon);
        final ImageView blue_qube = (ImageView) dialog2.findViewById(R.id.blue_qube_icon);
        final ImageView green_qube = (ImageView) dialog2.findViewById(R.id.green_qube_icon);

        //온클릭 리스너
        circle_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "원하시는 위치를 클릭하세요~", Toast.LENGTH_SHORT).show();
                frag1.myLayout.markPos = 1;
                dialog2.cancel();
            }
        });
        x_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "원하시는 위치를 클릭하세요~", Toast.LENGTH_SHORT).show();
                frag1.myLayout.markPos = 2;
                dialog2.cancel();
            }
        });
        red_qube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "원하시는 위치를 클릭하세요~", Toast.LENGTH_SHORT).show();
                frag1.myLayout.markPos = 3;
                dialog2.cancel();
            }
        });
        blue_qube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "원하시는 위치를 클릭하세요~", Toast.LENGTH_SHORT).show();
                frag1.myLayout.markPos = 4;
                dialog2.cancel();
            }
        });
        green_qube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "원하시는 위치를 클릭하세요~", Toast.LENGTH_SHORT).show();
                frag1.myLayout.markPos = 5;
                dialog2.cancel();
            }
        });
        direction_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "원하시는 위치를 클릭하세요~", Toast.LENGTH_SHORT).show();
                frag1.myLayout.markPos = 6;
                dialog2.cancel();
            }
        });
        dialog2.show();
    }
    public void addExplainDialog() {

        final Custom_Dialog dialog3 = new Custom_Dialog(context, R.style.AlertDialogCustom);
        dialog3.setContentView(R.layout.dialog_add_explain);
        //다이얼로그 마크 아이템 셋팅
        final EditText exp = (EditText) dialog3.findViewById(R.id.editContents);
        final Button ok = (Button)dialog3.findViewById(R.id.buttonOK);
        final Button can=(Button)dialog3.findViewById(R.id.buttonCancle);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "";
                text=exp.getText().toString();
                frag2.mAdapter.getItem(pos).explain=text;
                frag2.mListView.setAdapter(frag2.mAdapter);
                frag2.mListView.deferNotifyDataSetChanged();
                dialog3.cancel();
            }
        });
        can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog3.cancel();
            }
        });

        dialog3.show();
    }

    public void showDialog() {
        final Custom_Dialog dialog = new Custom_Dialog(this, R.style.AlertDialogCustom);
        dialog.setContentView(R.layout.dialog_select_lec_menu);
        // 다이얼로그 아이템 셋팅
        final ImageView addMark = (ImageView) dialog.findViewById(R.id.add_mark);
        final ImageView delMark = (ImageView) dialog.findViewById(R.id.del_mark);
        final ImageView addExp = (ImageView) dialog.findViewById(R.id.add_exp);
        final ImageView delMenu = (ImageView) dialog.findViewById(R.id.del_menu);
        addMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markDialog();
                dialog.cancel();
            }
        });
        delMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListData temp = frag2.mAdapter.getListData().get(pos);
                for(int i=temp.marks.size()-1;i>=0;i--)
                {
                    temp.marks.remove(i);
                }
                dialog.cancel();
                boolean fa=false;
                if(frag1.myLayout.movingFlag)
                    fa=true;
                frag1.myLayout.invalidate();
                frag1.myLayout.movingFlag=fa;
            }
        });
        addExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                addExplainDialog();
            }
        });
        delMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = frag2.mAdapter.getCount() - 1;

                if (pos == i) {
                    String lastPGN = frag2.mAdapter.getItem(pos).PGN;
                    frag2.mAdapter.remove(pos);
                    if (lastPGN.contains("(W)") || lastPGN.contains("(B)") || lastPGN.contains("(F)")) {
                        PGNController pgnController = new PGNController();
                        String starFen = LecureSave.FEN + "";
                        for (int j = 0; j < frag2.mAdapter.getCount(); j++) {
                            ListData temp = (ListData) frag2.mAdapter.getItem(j);
                            if (temp.PGN.contains("(W)") || temp.PGN.contains("(B)")) {
                                boolean wb = true;
                                String pgns = "";
                                if (temp.PGN.contains("(W)")) {
                                    wb = true;
                                    pgns = temp.PGN.replace("(W)", "");
                                } else if (temp.PGN.contains("(B)")) {
                                    wb = false;
                                    pgns = temp.PGN.replace("(B)", "");
                                }
                                pgns = pgns.trim();
                                Log.d("디버깅" + j, "/" + pgns);
                                starFen = pgnController.readPGN_FEN(starFen, pgns, wb);
                            }
                        }
                        frag1.myLayout.mChessBoard.board = FEN.ReadFEN(starFen);
                        frag1.myLayout.invalidate();
                    }
                } else if (pos == 0) {
                    ListData temp = (ListData) frag2.mAdapter.getItem(0);
                    if (temp.PGN.contains("(W)") || temp.PGN.contains("(B)")) {
                        boolean wb = true;
                        if (temp.PGN.contains("(W)")) {
                            wb = true;
                            String pgns = temp.PGN.replace("(W)", "");
                            PGNController pgnController = new PGNController();
                            LecureSave.FEN = pgnController.readPGN_FEN(LecureSave.FEN, pgns, wb);
                            frag2.mAdapter.remove(0);
                        } else if (temp.PGN.contains("(B)")) {
                            wb = false;
                            String pgns = temp.PGN.replace("(B)", "");
                            PGNController pgnController = new PGNController();
                            LecureSave.FEN = pgnController.readPGN_FEN(LecureSave.FEN, pgns, wb);
                            frag2.mAdapter.remove(0);
                        }
                    } else {
                        frag2.mAdapter.remove(0);
                    }
                }
                pos = -1;
                if (frag2.mPgn.startsWith("null"))
                    frag1.myLayout.movingFlag = true;
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            myDraw.movingFlag = intent.getBooleanExtra("flag", false);

            String tag = intent.getStringExtra("tag");
            if (tag != null && tag.startsWith("Promotion")) {
                FragmentManager fm = getSupportFragmentManager();

                promotionDialog.show(fm, "안녕?");
                return;
            }
            if(intent.getStringExtra("bakupList")!=null)
            {
                backup = (ArrayList<ListData>) frag2.mAdapter.getListData().clone();
                Log.d("디버깅","4사이즈는 "+backup.size());
            }
            if (intent.getStringExtra("next") != null) {
                Intent mIntent = new Intent(getApplicationContext(), make_lec_finish.class);
                startActivity(mIntent);
                LecureSave.PGN_Explain = (ArrayList<ListData>) frag2.mAdapter.getListData().clone();
                finish();
            }
            if (intent.getStringExtra("pre") != null) {
                Intent mIntent = new Intent(getApplicationContext(), MakeBoard.class);
                startActivity(mIntent);
                frag1.myLayout.mChessBoard.board = FEN.ReadFEN(LecureSave.firstFEN);
                finish();
            }
            if (intent.getStringExtra("pgn") != null) {
                if (intent.getStringExtra("pgn").equals("null") || intent.getStringExtra("pgn").equals(""))
                    frag2.mPgn = "null";
                else {
                    frag2.mPgn = intent.getStringExtra("pgn");
                    if (!myDraw.WB)
                        frag2.mPgn += "(W)";
                    else
                        frag2.mPgn += "(B)";
                }
                frag2.plusList(new ArrayList<BoardMark>());
                frag1.myLayout.markList = new ArrayList<>();
                frag1.myLayout.movingFlag = true;
                frag1.myLayout.invalidate();
                frag1.myLayout.firstMove=false;
                Log.d("피지엔", "ㅍ" + frag2.mPgn);
            }
            if (intent.getStringExtra("plus") != null && intent.getStringExtra("plus").equals("plus")) {
                frag2.plusList(new ArrayList<BoardMark>());
                frag1.myLayout.markList = new ArrayList<>();
                frag1.myLayout.movingFlag = true;
                frag1.myLayout.invalidate();
                frag1.myLayout.firstMove=false;
            }
            if (intent.getStringExtra("popup") != null && intent.getStringExtra("popup").equals("popup")) {
                pos = intent.getIntExtra("pos", -1);
                frag1.myLayout.movingFlag = false;
                PGNController pgnController = new PGNController();
                String starFen = LecureSave.FEN + "";
                for (int j = 0; j <= pos; j++) {
                    ListData temp = frag2.mAdapter.getItem(j);
                    if (temp.PGN.contains("(W)") || temp.PGN.contains("(B)")) {
                        boolean wb = true;
                        String pgns = "";
                        if (temp.PGN.contains("(W)")) {
                            wb = true;
                            pgns = temp.PGN.replace("(W)", "");
                        } else if (temp.PGN.contains("(B)")) {
                            wb = false;
                            pgns = temp.PGN.replace("(B)", "");
                        }
                        pgns = pgns.trim();
                        Log.d("디버깅" + j, "/" + pgns);
                        starFen = pgnController.readPGN_FEN(starFen, pgns, wb);
                    }
                }
                frag1.myLayout.mChessBoard.board = FEN.ReadFEN(starFen);
                frag1.myLayout.markList = frag2.mAdapter.getItem(pos).marks;
                Log.d("디버깅", "마크사이즈" + frag2.mAdapter.getItem(pos).marks.size());
                frag1.myLayout.invalidate();

                if (pos == frag2.mAdapter.getCount() - 1)
                    frag1.myLayout.movingFlag = true;
                showDialog();
            }
        }
    }
}
