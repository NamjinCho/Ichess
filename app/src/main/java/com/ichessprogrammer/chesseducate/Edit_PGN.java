package com.ichessprogrammer.chesseducate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.ichessprogrammer.chesseducate.PGN.PGNLoading1;
import com.ichessprogrammer.chesseducate.PGN.PGNsave;
import com.ichessprogrammer.chesseducate.Problem.QuizLoading;
import com.ichessprogrammer.chesseducate.Server.PHPConnector;
import com.ichessprogrammer.chesseducate.lecture.LectureLoading2;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

/**
 * Created by 남지니 on 2016-09-24.
 */
public class Edit_PGN extends AppCompatActivity {
    public static int caller = 0;
    public static int dataID = 0;
    public static int level = 1;
    public static String producer = "";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_activity_edit_pgn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().toString().startsWith("메인")) {
                    finish();
                } else if (item.getTitle().toString().startsWith("갤러리")) {
                    startActivity(new Intent(Edit_PGN.this, Gallery.class));
                } else if (item.getTitle().toString().startsWith("설정")) {
                    startActivity(new Intent(Edit_PGN.this, SettingActivity.class));
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
                    Intent intent = new Intent(Edit_PGN.this, KakaoTalkMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
        //PGNsave.saveData = PGNsave.saveData.replaceAll("||","\n");
        EditText pgnEdit = (EditText) findViewById(R.id.editText);
        pgnEdit.setText(PGNsave.saveData);
        PGNsave.saveData = null;
    }

    public void ButtonCreate(View v) {

        EditText titleEdit = (EditText) findViewById(R.id.titlebox);
        EditText pgnEdit = (EditText) findViewById(R.id.editText);
        String title = titleEdit.getText().toString();
        String pgns = pgnEdit.getText().toString();
        //String level=;

        if (!title.equals("")) {
            if (caller == 3) {
                new PHPConnector().ConnectServer("table=PGN_" + BoardDB.Title.get(BoardDB.curPos)
                        + "&where=pgnID=" + dataID + "&att=title&newVal=" + title, "update.php", "insertPGN");
                new PHPConnector().ConnectServer("table=PGN_" + BoardDB.Title.get(BoardDB.curPos)
                        + "&where=pgnID=" + dataID + "&att=pgns&newVal=" + pgns, "update.php", "insertPGN");
            }
            if (caller == 1) {
                new PHPConnector().ConnectServer("table=PGNproblem_" + BoardDB.Title.get(BoardDB.curPos)
                        + "&where=pgnID=" + dataID + "&att=title&newVal=" + title, "update.php", "insertPGN");
                new PHPConnector().ConnectServer("table=PGNproblem_" + BoardDB.Title.get(BoardDB.curPos)
                        + "&where=pgnID=" + dataID + "&att=pgns&newVal=" + pgns, "update.php", "insertPGN");
            }
            if (caller == 2) {
                new PHPConnector().ConnectServer("table=problem_" + BoardDB.Title.get(BoardDB.curPos)
                        + "&where=problemID=" + dataID + "&att=pgns&newVal=" + pgns, "update.php", "insertPGN");
            }
            if (caller == 1) {
                Intent intent = new Intent(Edit_PGN.this, QuizLoading.class);
                //intent.putExtra("Category", myCategory);
                startActivity(intent);
                System.gc();
                finish();
            } else if (caller == 2) {
                Intent intent = new Intent(Edit_PGN.this, LectureLoading2.class);
                //intent.putExtra("Category", myCategory);
                startActivity(intent);
                System.gc();
                finish();
            } else {

                Intent intent = new Intent(Edit_PGN.this, PGNLoading1.class);
                //intent.putExtra("Category", myCategory);
                startActivity(intent);
                System.gc();
                finish();
            }
        }
    }


    public void onResume() {
        super.onResume();
        ImageView back = (ImageView) findViewById(R.id.background);
        back.setImageResource(R.drawable.bg_basic);
    }

    public void onPause() {
        super.onPause();
        ImageView back = (ImageView) findViewById(R.id.background);
        back.setImageBitmap(null);
        System.gc();
    }

    public void onBackPressed() {
        BackButton(null);
        super.onBackPressed();
    }

    public void BackButton(View v) {
        if (caller == 1) {

            Intent intent = new Intent(Edit_PGN.this, QuizLoading.class);
            //intent.putExtra("Category","문제풀기");
            startActivity(intent);
        } else if (caller == 2) {

            Intent intent = new Intent(Edit_PGN.this, LectureLoading2.class);
            //intent.putExtra("Category","문제풀기");
            startActivity(intent);
        } else {

            Intent intent = new Intent(Edit_PGN.this, PGNLoading1.class);
            //intent.putExtra("Category","문제풀기");
            startActivity(intent);
        }
        finish();
        //myCategory = getIntent().getStringExtra("Category");
    }
}
