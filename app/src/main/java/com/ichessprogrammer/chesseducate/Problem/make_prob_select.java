package com.ichessprogrammer.chesseducate.Problem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ichessprogrammer.chesseducate.Gallery;
import com.ichessprogrammer.chesseducate.ImageSampling;
import com.ichessprogrammer.chesseducate.KakaoTalkMainActivity;
import com.ichessprogrammer.chesseducate.PGN.UploadPGN;
import com.ichessprogrammer.chesseducate.R;
import com.ichessprogrammer.chesseducate.SettingActivity;
import com.ichessprogrammer.chesseducate.lecture.LectureLoading2;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

/**
 * Created by 남지니 on 2016-08-04.
 */
public class make_prob_select extends Activity {

    public static int caller = 1;
    ImageButton di;
    ImageButton pg;
    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.layout_make_select);
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
                    startActivity(new Intent(make_prob_select.this, Gallery.class));
                } else if (item.getTitle().toString().startsWith("설정")) {
                    startActivity(new Intent(make_prob_select.this, SettingActivity.class));
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
                    Intent intent = new Intent(make_prob_select.this, KakaoTalkMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
    }
    public void Direct(View v)
    {
        make_prob_2.caller=caller;
        startActivity(new Intent(getApplicationContext(),MakeBoard.class));
        finish();
    }
    public void WithPGN(View v)
    {
        UploadPGN.caller=caller;
        startActivity(new Intent(getApplicationContext(), UploadPGN.class));
        finish();
    }
    public void onResume()
    {
        super.onResume();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        ImageView img = (ImageView)findViewById(R.id.background);
        img.setImageBitmap(ImageSampling.decodeSampledBitmapFromResource(getResources(),R.drawable.bg_blue,
                width,height));
    }
    public void onPause()
    {
        super.onPause();
        ImageView img = (ImageView)findViewById(R.id.background);
        img.setImageBitmap(null);
        System.gc();
    }

    public void BackButton(View v)
    {
        if(caller==2){

            Intent intent = new Intent(make_prob_select.this, QuizLoading.class);
            //intent.putExtra("Category","문제풀기");
            startActivity(intent);
        }else
        {

            Intent intent = new Intent(make_prob_select.this, LectureLoading2.class);
            //intent.putExtra("Category","문제풀기");
            startActivity(intent);
        }
        finish();
        //myCategory = getIntent().getStringExtra("Category");
    }
}
