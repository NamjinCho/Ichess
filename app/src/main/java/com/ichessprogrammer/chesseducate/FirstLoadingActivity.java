package com.ichessprogrammer.chesseducate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ichessprogrammer.chesseducate.Server.PHPConnector;

import java.util.ArrayList;

/**
 * Created by 남지니 on 2016-07-23.
 */
public class FirstLoadingActivity extends Activity {

    static boolean OneAct = false;
    EditText text1;
    EditText text2;
    Intent intent;
    SharedPreferences preferences;
    String ID;
    String Password;
    PHPConnector phpConnector;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String returnedValue = (String) msg.obj;
            if (returnedValue.equals("join")) {
                showDialog();
            } else if (returnedValue.equals("Succes")) {
                Log.d("디버깅", "몇번 불리나?");
                intent = new Intent(getApplicationContext(), StateActivity.class);
                startActivity(intent);
                FirstLoadingActivity.this.finish();
                OneAct = false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle saveBundle) {
        super.onCreate(saveBundle);
        setContentView(R.layout.layout_chessloading);
        if (OneAct == false) {

            RelativeLayout root = (RelativeLayout)findViewById(R.id.RootView);
            OneAct = true;
            preferences = getSharedPreferences("Ichess", Activity.MODE_PRIVATE);
            ID = preferences.getString("ID", "Don't exist");
            Password = preferences.getString("Password", "Don't exist");
            ChessPieaceIcon.imgPosition = preferences.getInt("pieaceThema", 0);
            Log.d("체스아이콘", ChessPieaceIcon.imgPosition + "");
            ChessPieaceIcon.boardImgPosition = preferences.getInt("boardThema", 0);
            Log.d("체스아이콘", ChessPieaceIcon.boardImgPosition + "");
            Log.d("디버깅", ID + "/" + Password);
            if (ID.equals("Don't exist") && Password.equals("Don't exist")) {
                return;
            }
            new PHPConnector().ConnectServer("attr=ID&table=Account&ID=" + ID + "&Password=" + Password, "isIDExist.php", "isExistID");

            DataPasser passer = new DataPasser();
            passer.start();
            getBoardDB();
            Log.d("디버깅", "몇번 불리나?2");
        }
    }
    public void onResume()
    {
        super.onResume();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        ImageView img = (ImageView)findViewById(R.id.img_view);
        img.setImageBitmap(ImageSampling.decodeSampledBitmapFromResource(getResources(),R.drawable.loading,
                width,height));

    }
    public void setMyData(String data) {
        String[] subData = data.split(",");
        MyInformation.ID = subData[1];
        MyInformation.name = subData[2];
        MyInformation.age = Integer.parseInt(subData[3]);
        MyInformation.level = Integer.parseInt(subData[4]);
        MyInformation.exp = Integer.parseInt(subData[5]);
        MyInformation.battle_win = Integer.parseInt(subData[6]);
        MyInformation.battle_lose = Integer.parseInt(subData[7]);
        //여기다 가져온다 보드디비
        //여기다 클리어 리스트 만든다.
    }
    public void onPause()
    {
        super.onPause();
    }
    public void getBoardDB() {
        new PHPConnector().ConnectServer("", "getBoardDB.php", "getBoardDB");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String result = PHPConnector.MessageQue.get("getBoardDB");

                while (result == null) {
                    result = PHPConnector.MessageQue.get("getBoardDB");
                }
                if (result.equals("False") == true) {
                } else {
                    String[] subResult = result.split(",");
                    for (int i = 0; i < subResult.length; i = i + 3) {
                        if (!BoardDB.ImgPos.containsKey(subResult[i] + "_" + subResult[i + 1])) {

                            if (!subResult[i].equals("게시판")) {
                                BoardDB.Category.add(subResult[i]);
                                BoardDB.Title.add(subResult[i + 1]);
                            } else
                                BoardDB.normalBoard.add(subResult[i + 1]);
                            BoardDB.ImgPos.put(subResult[i] + "_" + subResult[i + 1], Integer.parseInt(subResult[i + 2]));
                            final String tempTitle = subResult[i + 1];

                            final String tempCategory = subResult[i];

                            Log.d("디버깅", tempCategory);
                            if (tempCategory.equals("강의")) {
                                Log.d("디버깅", tempCategory);
                                Thread thread2 = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new PHPConnector().ConnectServer("table=clearLecture_" + tempTitle + "&id=" +
                                                ID, "getClearLectureList.php", "clearLec_" + tempTitle);
                                        String clearlist = PHPConnector.MessageQue.get("clearLec_" + tempTitle);
                                        while (clearlist == null) {
                                            clearlist = PHPConnector.MessageQue.get("clearLec_" + tempTitle);
                                        }
                                        Log.d("클리어 리스트", clearlist);
                                        if (clearlist.equals("False")) return;
                                        String[] subcl = clearlist.split(",");
                                        ArrayList<String> tempset = new ArrayList<>();
                                        for (int j = 0; j < subcl.length; j++) {
                                            tempset.add(subcl[j]);
                                        }
                                        Log.d("맞춘목록", tempset.toString());
                                        MyInformation.ClearLecID.put(tempTitle, tempset);
                                    }
                                });
                                thread2.start();
                            }

                        }
                    }
                    for (int k = 0; k < BoardDB.normalBoard.size(); k++) {
                        BoardDB.Category.add("게시판");
                        BoardDB.Title.add(BoardDB.normalBoard.get(k));
                    }
                }
                BoardDB.curPos = 0;
            }
        });
        thread.start();
    }

    public void showDialog() {
        startActivityForResult(new Intent(FirstLoadingActivity.this, JoinActivity.class), 1253);
    }

    protected void onDestroy() {
        ImageView img = (ImageView)findViewById(R.id.img_view);
        ((BitmapDrawable) img.getDrawable()).getBitmap().recycle();
        img.setImageBitmap(null);
        System.gc();
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1253) {
            PHPConnector.MessageQue.clear();
            new PHPConnector().ConnectServer("ID=" + ID, "getMyInformation.php", "getMyInformation");
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String result = PHPConnector.MessageQue.get("getMyInformation");
                    while (result == null) result = PHPConnector.MessageQue.get("getMyInformation");
                    setMyData(result);
                    Message msg = mHandler.obtainMessage(1, "Succes");
                    mHandler.sendMessage(msg);
                }
            });
            thread.start();
        } else
            finish();
    }

    public class DataPasser extends Thread {
        @Override
        public void run() {
            while (true) {
                if (PHPConnector.MessageQue.get("isExistID") != null)
                    break;
            }
            if (PHPConnector.MessageQue.get("isExistID").startsWith("False")) {
                //처음 접속하는 유저의경우 가벼운 회원가입창을 연다.
                Message msg;
                msg = mHandler.obtainMessage(1, "join");
                mHandler.sendMessage(msg);

            } else {

                new PHPConnector().ConnectServer("ID=" + ID, "getMyInformation.php", "getMyInformation");
                while (PHPConnector.MessageQue.get("getMyInformation") == null)
                    Log.d("안녕","로그인좀22");

                if (PHPConnector.MessageQue.get("getMyInformation").startsWith("True")) {
                    setMyData(PHPConnector.MessageQue.get("getMyInformation"));
                    Message msg;
                    msg = mHandler.obtainMessage(1, "Succes");
                    mHandler.sendMessage(msg);
                }
                Log.d("안녕","로그인좀");
            }
        }
    }

}
