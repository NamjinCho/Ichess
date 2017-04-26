package com.ichessprogrammer.chesseducate.Problem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ichessprogrammer.chesseducate.BoardDB;
import com.ichessprogrammer.chesseducate.ChessBoard;
import com.ichessprogrammer.chesseducate.Diolog.Custom_Dialog;
import com.ichessprogrammer.chesseducate.Diolog.PromotionFragment;
import com.ichessprogrammer.chesseducate.FEN;
import com.ichessprogrammer.chesseducate.Gallery;
import com.ichessprogrammer.chesseducate.KakaoTalkMainActivity;
import com.ichessprogrammer.chesseducate.MyInformation;
import com.ichessprogrammer.chesseducate.PGNController;
import com.ichessprogrammer.chesseducate.R;
import com.ichessprogrammer.chesseducate.Server.PHPConnector;
import com.ichessprogrammer.chesseducate.SettingActivity;
import com.ichessprogrammer.chesseducate.lecture.BoardMark;
import com.ichessprogrammer.chesseducate.lecture.LecAndProb;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.util.ArrayList;

/**
 * Created by 남지니 on 2016-07-24.
 */
public class make_prob_2 extends AppCompatActivity implements View.OnClickListener {
    public static int caller = 1;
    public ChessDraw_prob myDraw;
    public make_prob_etc_fragment frag2;
    public int pgnIndex = 1;
    public String currentTurn;
    public String passMark = "";
    make_prob_board_fragment frag1;
    PromotionFragment promotionDialog;
    //public Context ;
    MyBroadcastReceiver receiver;
    Button[] buttons = new Button[5];
    ImageView[] imageView = new ImageView[5];
    GridView AnswerListView;
    ListAdapter_answer adapter;
    String mPgn = "null";
    String prePgn = "null";
    ArrayList<AnswerList> AnswerPGN = new ArrayList<>();
    String CurFen;
    int pos = 0;
    boolean loopChecker = false;
    boolean startWB = true;
    int curIndex = 0;
    ArrayList<BoardMark>saveMarkList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle ic) {
        super.onCreate(ic);
        setContentView(R.layout.layout_activity_make_prob_2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ProblemSave.CallActivity = "prob2";
        IntentFilter mainFilter = new IntentFilter("prob2");
        receiver = new MyBroadcastReceiver();
        registerReceiver(receiver, mainFilter);
        promotionDialog = new PromotionFragment();
        buttons[3] = (Button) findViewById(R.id.buttonComplete);
        imageView[0] = (ImageView) findViewById(R.id.circle_mark_icon);
        imageView[1] = (ImageView) findViewById(R.id.redqube_mark_icon);
        imageView[2] = (ImageView) findViewById(R.id.x_mark_icon);
        imageView[3] = (ImageView) findViewById(R.id.blueqube_mark_icon);
        imageView[4] = (ImageView) findViewById(R.id.direction_mark_icon);
        AnswerListView = (GridView) findViewById(R.id.answerList);
        //mContext=getApplicationContext();
        AnswerPGN.add(new AnswerList());
        CurFen = ProblemSave.FEN;
        adapter = new ListAdapter_answer(make_prob_2.this, AnswerPGN, getLayoutInflater());
        AnswerListView.setAdapter(adapter);
        for (int i = 0; i < 5; i++) {
            imageView[i].setOnClickListener(this);
        }
        buttons[3].setOnClickListener(this);
        TextView textView = (TextView) findViewById(R.id.Title);
        textView.setText("문제 만들기");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().toString().startsWith("메인")) {
                    finish();
                } else if (item.getTitle().toString().startsWith("갤러리")) {
                    startActivity(new Intent(make_prob_2.this, Gallery.class));
                } else if (item.getTitle().toString().startsWith("설정")) {
                    startActivity(new Intent(make_prob_2.this, SettingActivity.class));
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
                    Intent intent = new Intent(make_prob_2.this, KakaoTalkMainActivity.class);
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
        frag1 = (make_prob_board_fragment) getFragmentManager().findFragmentById(R.id.boardfragment);
        myDraw = frag1.myLayout;
        frag2 = (make_prob_etc_fragment) getFragmentManager().findFragmentById(R.id.etcFragment);


        AnswerListView = (GridView) findViewById(R.id.answerList);
        adapter = new ListAdapter_answer(make_prob_2.this, AnswerPGN, getLayoutInflater());
        AnswerListView.setAdapter(adapter);
        myDraw.mChessBoard.board = FEN.ReadFEN(CurFen);

        AnswerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    myDraw.movingFlag = false;
                    myDraw.premoveFlag = false;
                    myDraw.mChessBoard.board = FEN.ReadFEN(ProblemSave.FEN);
                    myDraw.invalidate();
                    pos = 0;
                    return;
                } else {
                    PGNController controller = new PGNController(); //pgn 값 만들어내는 컨트롤러?
                    Log.d("디버깅", "헬로" + position);
                    myDraw.mChessBoard.board = FEN.ReadFEN(ProblemSave.FEN); // 스타트 펀?
                    ArrayList<Integer> root = new ArrayList<Integer>();
                    pos = position;// 리스트에서 찍은 위치
                    AnswerList currentNode = AnswerPGN.get(pos);//현재 노드
                    while (currentNode.left != null) { // 계속 불러옴
                        root.add(0, currentNode.myIndex);//루트에 저장 스택개념인듯
                        currentNode = currentNode.left; // 이진 왼쪽?
                    }
                    root.add(0, currentNode.myIndex);//맨앞에 이제 마지막 경로 없는애
                    int index = 0;
                    for (int i = 0; i <= position; i++) {
                        if (AnswerPGN.get(i).myIndex == root.get(index)) {
                            if (!AnswerPGN.get(i).Answer.equals("null")) {
                                if (startWB) {
                                    myDraw.mChessBoard = controller.readPGN(myDraw.mChessBoard, AnswerPGN.get(i).Answer.replace("(W)", ""), true);
                                } else {
                                    myDraw.mChessBoard = controller.readPGN(myDraw.mChessBoard, AnswerPGN.get(i).Answer.replace("(B)", ""), false);
                                }
                            } else {
                                if (startWB) {
                                    myDraw.mChessBoard = controller.readPGN(myDraw.mChessBoard, AnswerPGN.get(i).preMove.replace("(B)", ""), false);
                                } else {
                                    myDraw.mChessBoard = controller.readPGN(myDraw.mChessBoard, AnswerPGN.get(i).preMove.replace("(W)", ""), true);
                                }
                            }
                            index++;
                        }
                    }
                    currentTurn = AnswerPGN.get(position).myTurn;
                    String tempfen = FEN.SetFEN(myDraw.mChessBoard);
                    CurFen = tempfen;
                    myDraw.mChessBoard.board = FEN.ReadFEN(tempfen);
                    Log.d("Fen", tempfen);
                    pos = position;
                    myDraw.markList = (ArrayList<BoardMark>) AnswerPGN.get(pos).hintList.clone();
                    myDraw.invalidate();
                    if (AnswerPGN.get(position).Answer.equals("null")) {
                        myDraw.WB = startWB;
                    } else
                        myDraw.WB = !startWB;


                    if (AnswerPGN.get(position).Answer.equals("null")) {
                        myDraw.movingFlag = true;
                        myDraw.premoveFlag = false;
                    } else {
                        myDraw.movingFlag = false;
                        myDraw.premoveFlag = true;
                    }

                }
            }
        });
    }

    protected void onDestory() {
        super.onDestroy();
        ProblemSave.CallActivity = "prob1";
        ProblemSave.datas = null;
        ProblemSave.FEN = null;
        unregisterReceiver(receiver);
    }

    private void divide() {
        ArrayList<AnswerList> mAnswerList = new ArrayList<>();
        int tempPos = Integer.parseInt(getIntent().getStringExtra("Pos"));
        LecAndProb.ProbData tempData = LecAndProb.dataList.get(tempPos);
        ProblemSave.FEN = tempData.mFen;
        int index = 1;
        int start = tempData.answer.indexOf("(" + index + ".");
        while (start != -1) {
            int last = tempData.answer.indexOf(")/(", start);
            Log.d("디버깅", start + "/" + last);
            if (last == -1)
                last = tempData.answer.length() - 2;
            if (index < 10)
                start = start + 3;
            else if (index < 100)
                start = start + 4;
            else
                start = start + 5;
            String subTemp = tempData.answer.substring(start, last);

            String[] subTemp2 = subTemp.split("/");
            Log.d("디버깅", subTemp);
            AnswerList tempList2 = new AnswerList();
            tempList2.Answer = subTemp2[0].trim();
            tempList2.preMove = subTemp2[1].trim();
            mAnswerList.add(tempList2);
            index++;
            start = tempData.answer.indexOf("(" + index + ".");
        }
        index = 1;
        start = tempData.explain.indexOf("(" + index + ".");
        while (start != -1) {
            int last = tempData.explain.indexOf(")/", start);
            Log.d("디버깅", start + "/" + last);
            if (last == -1)
                last = tempData.explain.length() - 2;
            if (index < 10)
                start = start + 3;
            else if (index < 100)
                start = start + 4;
            else
                start = start + 5;
            String subTemp = tempData.explain.substring(start, last);
            mAnswerList.get(index - 1).Explain = subTemp;
            index++;
            start = tempData.explain.indexOf("(" + index + ".");
        }
        if (!tempData.hint.equals("")) {
            String[] subHint = tempData.hint.split("/");

            for (int i = 0; i < subHint.length; i++) {
                subHint[i] = subHint[i].replace("(", "");
                subHint[i] = subHint[i].replace(")", "");
                Log.d("디버깅", subHint[i]);
                String[] temp = subHint[i].split(". ");
                Log.d("디버깅", "디버깅" + temp[0]);
                int hIndex = Integer.parseInt(temp[0]);
                hIndex--;
                ArrayList<BoardMark> tempHintList = new ArrayList<>();

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
                mAnswerList.get(hIndex).hintList = tempHintList;
            }
        }
        AnswerPGN = mAnswerList;

    }

    protected void onStop() {
        super.onStop();
        //finish();
    }

    protected void showPrompt() {
        final Custom_Dialog dialog2 = new Custom_Dialog(make_prob_2.this, R.style.AlertDialogCustom);
        dialog2.setContentView(R.layout.dialog_prompt1);
        final TextView textView = (TextView) dialog2.findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.cancel();
            }
        });
        dialog2.show();

    }

    protected void showDialog_AnswerList() {
        final Custom_Dialog dialog2 = new Custom_Dialog(make_prob_2.this, R.style.AlertDialogCustom);
        dialog2.setContentView(R.layout.dialog_list_answer);
        final ListView mListView = (ListView) dialog2.findViewById(R.id.editContents);
        final ListAdapter_answer adapter_answer = new ListAdapter_answer(make_prob_2.this, AnswerPGN, getLayoutInflater());
        mListView.setAdapter(adapter_answer);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PGNController controller = new PGNController();
                Log.d("디버깅", "헬로" + position);
                myDraw.mChessBoard.board = FEN.ReadFEN(ProblemSave.FEN);
                for (int i = 0; i <= position; i++) {
                    if (!AnswerPGN.get(i).equals("null")) {
                        if (!AnswerPGN.get(i).preMove.equals("null")) {
                            Log.d("디버깅", AnswerPGN.get(i).preMove);
                            if (AnswerPGN.get(i).preMove.contains("(W)"))
                                myDraw.mChessBoard = controller.readPGN(myDraw.mChessBoard, AnswerPGN.get(i).preMove.replace("(W)", ""), true);
                            else if (AnswerPGN.get(i).preMove.contains("(B)"))
                                myDraw.mChessBoard = controller.readPGN(myDraw.mChessBoard, AnswerPGN.get(i).preMove.replace("(B)", ""), false);
                        }
                        if (AnswerPGN.get(i).Answer.contains("(W)"))
                            myDraw.mChessBoard = controller.readPGN(myDraw.mChessBoard, AnswerPGN.get(i).Answer.replace("(W)", ""), true);
                        else if (AnswerPGN.get(i).Answer.contains("(B)"))
                            myDraw.mChessBoard = controller.readPGN(myDraw.mChessBoard, AnswerPGN.get(i).Answer.replace("(B)", ""), false);
                    }
                }
                pos = position;
                myDraw.markList = AnswerPGN.get(pos).hintList;
                myDraw.invalidate();
                if (position == AnswerPGN.size() - 1) {
                    myDraw.movingFlag = true;
                } else
                    myDraw.movingFlag = false;
                dialog2.cancel();
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == AnswerPGN.size() - 1) {
                    AnswerPGN.remove(position);
                    pos = AnswerPGN.size() - 1;
                    PGNController controller = new PGNController();
                    myDraw.mChessBoard.board = FEN.ReadFEN(ProblemSave.FEN);
                    adapter_answer.mListData = AnswerPGN;
                    adapter_answer.notifyDataSetChanged();
                    for (int i = 0; i <= pos; i++) {
                        if (!AnswerPGN.get(i).equals("null")) {
                            if (!AnswerPGN.get(i).preMove.equals("null")) {
                                Log.d("디버깅", AnswerPGN.get(i).preMove);
                                if (AnswerPGN.get(i).preMove.contains("(W)"))
                                    myDraw.mChessBoard = controller.readPGN(myDraw.mChessBoard, AnswerPGN.get(i).preMove.replace("(W)", ""), true);
                                else if (AnswerPGN.get(i).preMove.contains("(B)"))
                                    myDraw.mChessBoard = controller.readPGN(myDraw.mChessBoard, AnswerPGN.get(i).preMove.replace("(B)", ""), false);
                            }
                            if (AnswerPGN.get(i).Answer.contains("(W)"))
                                myDraw.mChessBoard = controller.readPGN(myDraw.mChessBoard, AnswerPGN.get(i).Answer.replace("(W)", ""), true);
                            else if (AnswerPGN.get(i).Answer.contains("(B)"))
                                myDraw.mChessBoard = controller.readPGN(myDraw.mChessBoard, AnswerPGN.get(i).Answer.replace("(B)", ""), false);
                        }
                    }
                    myDraw.markList = AnswerPGN.get(pos).hintList;
                    myDraw.invalidate();
                    myDraw.movingFlag = true;
                }
                return true;
            }
        });
        dialog2.show();

    }

    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }

    public void ONCLICK(View view) {
        ChessBoard mChessBoard;

        final make_prob_board_fragment fragment = (make_prob_board_fragment) getFragmentManager().findFragmentById(R.id.boardfragment);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == buttons[3].getId()) {
           /*
           if (AnswerPGN.size() > 1) {
                String params = ParameterGetter.getParmas(AnswerPGN);
                String level = "";
                params += "&table=problem_" + BoardDB.Title.get(BoardDB.curPos);
                if (ProblemSave.probID == -1)
                    new PHPConnector().ConnectServer(params + "&level=" + level, "insertProb.php", "inProb");
                else
                    new PHPConnector().ConnectServer(params + "&level=" + level, "insertProb2.php", "inProb");
                finish();
                Log.d("디버깅", params);
            } else {
                Toast.makeText(getApplicationContext(), "최소한 1개의 정답을 입력 해야 합니다.", Toast.LENGTH_SHORT).show();
            }
            */
            if (AnswerPGN.size() > 1) {
                String PGN_EVENT = "[Event \"test\"]";
                String PGN_SITE = "[Site \"test\"]";
                String PGN_DATE = "[Date \"\"test\"]";
                String PGN_ROUND = "[Round \"test\"]";
                String PGN_WHITE = "[White \"test\"]";
                String PGN_BLACK = "[Black \"test\"]";
                String PGN_RESULT = "[Result \"test\"]";
                String PGN_EVENTDATE = "[EventDate \"test\"]";
                String PGN_FEN = "[FEN \"" + ProblemSave.FEN + "\"]";
                String params = "" + PGN_EVENT + "||" + PGN_SITE + "||" + PGN_DATE + "||" + PGN_ROUND + "||"
                        + PGN_WHITE + "||" + PGN_BLACK + "||" + PGN_RESULT + "||" + PGN_EVENTDATE + "||" + PGN_FEN + "||";
                for (int i = 1; i < AnswerPGN.size(); i++) {
                    params += AnswerPGN.get(i).myPGNdata + " ";
                }
                Log.d("디벅스", params);
                /*
                *
                $prod =$_POST['producer'];
                $title=$_POST['title'];
                $level=$_POST['level'];
                $pgns=$_POST['pgns'];
                $tables=$_POST['table'];
                * */
                if(caller==1) {
                    params = "pgns=" + params + "&level=" + 1 + "&id=" + MyInformation.ID + "&table=" + "problem_" + BoardDB.Title.get(BoardDB.curPos)
                            + "&title=" + "test&lecID=" + LecAndProb.LectureData.lecID;
                    new PHPConnector().ConnectServer(params, "insertProb.php", "iP");
                }
                else{
                    params = "pgns=" + params + "&level=" + 1 + "&producer=" + MyInformation.ID + "&table=" + "PGNproblem_" + BoardDB.Title.get(BoardDB.curPos)
                            + "&title=" + "test";
                    new PHPConnector().ConnectServer(params, "insertPGNProblem.php", "iP");
                }
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "최소한 1개의 정답을 입력 해야 합니다.", Toast.LENGTH_SHORT).show();
            }
        }
/*        if (v.getId() == buttons[4].getId()) {
            myDraw.premoveFlag = true;
            myDraw.movingFlag = false;
        } else */
        if (v.getId() == imageView[0].getId()) {
            myDraw.markPos = 1;
        } else if (v.getId() == imageView[1].getId()) {
            myDraw.markPos = 3;
        } else if (v.getId() == imageView[2].getId()) {
            myDraw.markPos = 2;
        } else if (v.getId() == imageView[3].getId()) {
            myDraw.markPos = 4;
        } else if (v.getId() == imageView[4].getId()) {
            myDraw.markPos = 6;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(receiver);
        System.gc();
    }

    public void sortTree() {
        ArrayList<Integer> selected = new ArrayList<>();
        ArrayList<AnswerList> newList = new ArrayList<>();
        for (int i = 1; i < AnswerPGN.size(); i++) {
            if (selected.contains(AnswerPGN.get(i).myIndex)) {

                continue;
            } else {
                AnswerList trip = AnswerPGN.get(i);
                ArrayList<AnswerList> temps = new ArrayList<>();
                while (trip.right != null) {
                    temps.add(trip);
                    trip = trip.right;
                    selected.add(trip.myIndex);
                }
                temps.add(trip);
                selected.add(trip.myIndex);
                trip = temps.get(0);
                if (trip.left == null) {
                    while (temps.size() != 0) {
                        newList.add(temps.get(0));
                        temps.remove(0);
                    }
                } else {

                    int findIndex = trip.left.right.myIndex;

                    Log.d("디벅스", findIndex + "sss");
                    for (int t = 0; t < newList.size(); t++) {
                        Log.d("디벅스", findIndex + "/" + newList.get(t).myIndex);
                        if (findIndex == newList.get(t).myIndex) {
                            findIndex = t;
                            Log.d("디벅스", t + "s");
                            break;
                        }
                    }
                    Log.d("디벅스", findIndex + "ss");
                    while (temps.size() != 0) {
                        newList.add(findIndex + 1, temps.get(0));
                        findIndex++;
                        temps.remove(0);
                    }
                }
            }
        }
        newList.add(0, new AnswerList());
        AnswerPGN = null;
        AnswerPGN = newList;
        newList = null;
        for (int i = 1; i < AnswerPGN.size(); i++) {
            if (AnswerPGN.get(i).myIndex == pgnIndex - 1) {
                pos = i;
                break;
            }
        }
    }

    public void insertTree(AnswerList newNode) {
        //루트일떄
        if (AnswerPGN.size() == 1) {
            Log.d("디버깅", "루트");
            AnswerPGN.add(newNode);
            newNode.hintList = (ArrayList<BoardMark>) AnswerPGN.get(0).hintList.clone();
            AnswerPGN.get(0).hintList.clear();
            pos = 1;
            adapter.mListData = AnswerPGN;
            adapter.notifyDataSetChanged();
        } else {
            AnswerList currentNode = AnswerPGN.get(pos);
            if (currentNode.rightIndex == 0) {
                AnswerPGN.add(newNode);
                currentNode.rightIndex = newNode.myIndex;
                currentNode.right = newNode;
                newNode.left = currentNode;
                newNode.leftIndex = currentNode.myIndex;
                pos = AnswerPGN.size() - 1;
                adapter.mListData = AnswerPGN;
                adapter.notifyDataSetChanged();
                return;
            }
            if (currentNode.rightIndex != -1) {
                AnswerList tempNode = currentNode.right;
                newNode.left = currentNode;
                newNode.leftIndex = -1;
                newNode.rightIndex = -1;
                AnswerPGN.add(newNode);
            } else {

                AnswerList temp = AnswerPGN.get(pos);
                temp.rightIndex = newNode.myIndex;
                temp.right = newNode;
                newNode.leftIndex = temp.myIndex;
                newNode.left = temp;
                newNode.rightIndex = -1;
                AnswerPGN.add(newNode);
            }
            sortTree();
            sortTree();
        }
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        boolean promotionFlag = true;

        @Override
        public void onReceive(Context context, Intent intent) {
            String pgn = intent.getStringExtra("pgn");
            String tag = intent.getStringExtra("tag");
            if (tag != null && tag.equals("Promotion")) {
                FragmentManager fm = getSupportFragmentManager();

                promotionDialog.show(fm, "안녕?");
                promotionFlag = false;
                return;
            }
            if (tag != null && tag.equals("Hint")) {

                String hint = intent.getStringExtra("hint");
                BoardMark mark = new BoardMark();
                String[] subHint = hint.split(",");
                mark.pos = Integer.parseInt(subHint[0]);
                mark.row = Integer.parseInt(subHint[1]);
                mark.col = Integer.parseInt(subHint[2]);
                mark.row2 = Integer.parseInt(subHint[3]);
                mark.col2 = Integer.parseInt(subHint[4]);
                saveMarkList.add(mark);
                //AnswerPGN.get(pos).hintList.add(mark);
                return;
            }
            if (!myDraw.firstMove) {
                if (currentTurn.contains("w")) {
                    currentTurn = currentTurn.replace("w", "b");
                } else {
                    int tempI = 0;
                    tempI = currentTurn.indexOf("b");
                    tempI = Integer.parseInt(currentTurn.substring(0, tempI));
                    currentTurn = (tempI + 1) + "w";
                }
            }
            if (pgn != null && myDraw.movingFlag) {
                AnswerList newNode = new AnswerList();
                newNode.myIndex = pgnIndex;
                pgnIndex++;
                newNode.Answer = pgn;

                if (!myDraw.firstMove)
                    newNode.myTurn = currentTurn;

                if (myDraw.WB)
                    newNode.Answer += "(B)";
                else
                    newNode.Answer += "(W)";

                Log.d("디벅스", newNode.Answer);
                insertTree(newNode);
                for(int i=saveMarkList.size()-1;i>=0;i--)
                {
                    newNode.hintList.add(saveMarkList.get(i));
                    saveMarkList.remove(i);
                }
                myDraw.markList=new ArrayList<>();
                adapter.mListData = AnswerPGN;
                adapter.notifyDataSetChanged();
                myDraw.movingFlag = false;
                myDraw.premoveFlag = true;
                myDraw.invalidate();
            } else if (pgn != null && myDraw.premoveFlag) {
                AnswerList newNode = new AnswerList();
                newNode.myIndex = pgnIndex;
                pgnIndex++;
                newNode.preMove = pgn;
                if (!myDraw.firstMove)
                    newNode.myTurn = currentTurn;
                if (myDraw.WB)
                    newNode.preMove += "(B)";
                else
                    newNode.preMove += "(W)";
                insertTree(newNode);
                for(int i=saveMarkList.size()-1;i>=0;i--)
                {
                    newNode.hintList.add(saveMarkList.get(i));
                    saveMarkList.remove(i);
                }
                myDraw.markList=new ArrayList<>();
                adapter.mListData = AnswerPGN;
                adapter.notifyDataSetChanged();
                myDraw.movingFlag = true;
                myDraw.premoveFlag = false;
                myDraw.invalidate();
            }
            if (myDraw.firstMove) {
                if (AnswerPGN.get(1).Answer.contains("(W)"))
                    currentTurn = "1w";
                else
                    currentTurn = "1b";
                AnswerPGN.get(1).myTurn = currentTurn;
                adapter.mListData = AnswerPGN;
                adapter.notifyDataSetChanged();
            }

            CurFen = FEN.SetFEN(myDraw.mChessBoard);
            myDraw.firstMove = false;
        }
    }

    public class ListAdapter_answer extends BaseAdapter {
        ArrayList<AnswerList> mListData;
        Context mContext;
        LayoutInflater inflater;

        ListAdapter_answer(Context context, ArrayList<AnswerList> temp, LayoutInflater inf) {
            mContext = context;
            mListData = temp;
            inflater = inf;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public AnswerList getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public void notifyDataSetChanged() {
            for (int position = 1; position < mListData.size(); position++) {
                if (position != 0) {
                    String myPos = "";
                    String myPgn = "";
                    if (mListData.get(position).myTurn.contains("w"))
                        myPos = mListData.get(position).myTurn.replace("w", ". ");
                    else
                        myPos = mListData.get(position).myTurn.replace("b", "... ");

                    if (startWB) {
                        if (!mListData.get(position).Answer.equals("null")) {
                            myPgn = mListData.get(position).Answer.replace("(W)", "");
                        } else {
                            myPgn = mListData.get(position).preMove.replace("(B)", "");
                        }
                    } else {
                        if (!mListData.get(position).Answer.equals("null")) {
                            myPgn = mListData.get(position).Answer.replace("(B)", "");
                        } else {
                            myPgn = mListData.get(position).preMove.replace("(W)", "");
                        }
                    }
                    String resultPGN = myPos + myPgn;
                    String marks = "/_";

                    for (int i = 0; i < mListData.get(position).hintList.size(); i++) {
                        if (mListData.get(position).hintList.get(i).pos != 6) {
                            //ox빨파초
                            switch (mListData.get(position).hintList.get(i).pos) {
                                case 1:
                                    marks += "C";
                                    break;
                                case 2:
                                    marks += "X";
                                    break;
                                case 3:
                                    marks += "R";
                                    break;
                                case 4:
                                    marks += "B";
                                    break;
                                case 5:
                                    marks += "G";
                                    break;
                                default:
                                    break;
                            }
                            switch (mListData.get(position).hintList.get(i).col) {
                                case 0:
                                    marks += "a";
                                    break;
                                case 1:
                                    marks += "b";
                                    break;
                                case 2:
                                    marks += "c";
                                    break;
                                case 3:
                                    marks += "d";
                                    break;
                                case 4:
                                    marks += "e";
                                    break;
                                case 5:
                                    marks += "f";
                                    break;
                                case 6:
                                    marks += "g";
                                    break;
                                case 7:
                                    marks += "h";
                                    break;
                                default:
                                    break;
                            }
                            marks += (mListData.get(position).hintList.get(i).row + 1);
                            //abcdefgh
                        } else {
                            marks += "AR";
                            switch (mListData.get(position).hintList.get(i).col) {
                                case 0:
                                    marks += "a";
                                    break;
                                case 1:
                                    marks += "b";
                                    break;
                                case 2:
                                    marks += "c";
                                    break;
                                case 3:
                                    marks += "d";
                                    break;
                                case 4:
                                    marks += "e";
                                    break;
                                case 5:
                                    marks += "f";
                                    break;
                                case 6:
                                    marks += "g";
                                    break;
                                case 7:
                                    marks += "h";
                                    break;
                                default:
                                    break;
                            }
                            marks += (mListData.get(position).hintList.get(i).row + 1);
                            switch (mListData.get(position).hintList.get(i).col2) {
                                case 0:
                                    marks += "a";
                                    break;
                                case 1:
                                    marks += "b";
                                    break;
                                case 2:
                                    marks += "c";
                                    break;
                                case 3:
                                    marks += "d";
                                    break;
                                case 4:
                                    marks += "e";
                                    break;
                                case 5:
                                    marks += "f";
                                    break;
                                case 6:
                                    marks += "g";
                                    break;
                                case 7:
                                    marks += "h";
                                    break;
                                default:
                                    break;
                            }
                            marks += (mListData.get(position).hintList.get(i).row2 + 1);
                        }
                        marks += " ";
                    }
                    marks += "_/ ";
                    String exp = mListData.get(position).Explain;
                    if (exp.equals("null"))
                        exp = "";
                    String res = exp;
                    //res = res.replace("/_ _/", "");
                    res = "{ " + res + "}";
                    res = res.replace("{ }", "");
                    marks=marks.replace("/__/","");
                    res = res + marks;
                    if (res.length() >= 1)
                        resultPGN = res +" "+ resultPGN;
                    if (mListData.get(position).leftIndex == -1)
                        resultPGN = "(" + resultPGN;
                    if (mListData.get(position).rightIndex == -1)
                        resultPGN += ")";

                    mListData.get(position).myPGNdata = resultPGN;
                    if (mListData.get(position - 1).myPGNdata.endsWith(")")) {
                        if (mListData.get(position - 1).rightIndex != -1) {
                            mListData.get(position - 1).myPGNdata = mListData.get(position - 1).myPGNdata.replace(")", "");
                            mListData.get(position).myPGNdata += ")";
                            Log.d("디벅스", "여기?");
                        }
                    }
                    if (mListData.get(position).myPGNdata.startsWith("(")) {
                        if (mListData.get(position - 1).myPGNdata.endsWith(")") &&
                                mListData.get(position - 1).myTurn.equals(mListData.get(position).myTurn) &&
                                mListData.get(position - 1).leftIndex != mListData.get(position).leftIndex) {
                            mListData.get(position - 1).myPGNdata = mListData.get(position - 1).myPGNdata.replace(")", "");
                            mListData.get(position).myPGNdata += ")";
                            Log.d("디벅스", "여기?2");
                        }
                    }

                }
            }
            super.notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.make_prob_list_view, null);
            }
            TextView pgnData = (TextView) convertView.findViewById(R.id.pgns);
            final int thisPos = position;
            if (position != 0) {
                pgnData.setText(mListData.get(position).myPGNdata);
            }

            return convertView;

        }
    }
}
