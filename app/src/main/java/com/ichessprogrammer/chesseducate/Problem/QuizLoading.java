package com.ichessprogrammer.chesseducate.Problem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ichessprogrammer.chesseducate.BoardDB;
import com.ichessprogrammer.chesseducate.ImageSampling;
import com.ichessprogrammer.chesseducate.R;
import com.ichessprogrammer.chesseducate.Server.PHPConnector;

/**
 * Created by 남지니 on 2016-09-12.
 */
public class QuizLoading extends Activity{
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        System.gc();
        PHPConnector.MessageQue.clear();
        getApplicationContext().getResources().flushLayoutCache();
        setContentView(R.layout.layout_chessloading);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        RelativeLayout root = (RelativeLayout)findViewById(R.id.RootView);
        ImageView img = (ImageView)findViewById(R.id.img_view);
        img.setImageBitmap(ImageSampling.decodeSampledBitmapFromResource(getResources(),R.drawable.loading,
                width,height));
        new PHPConnector().ConnectServer("table=PGNproblem_"+ BoardDB.Title.get(BoardDB.curPos),"getPGNProblemList.php","getQuizList");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String result = PHPConnector.MessageQue.get("getQuizList");
                while (result==null)result=PHPConnector.MessageQue.get("getQuizList");
                DataTransfer.result = result;
                startActivity(new Intent(QuizLoading.this,QuizList.class));
                QuizLoading.this.finish();
            }
        });
        thread.start();
    }
    public void onStop()
    {
        super.onStop();
        ImageView img = (ImageView)findViewById(R.id.img_view);
        ((BitmapDrawable) img.getDrawable()).getBitmap().recycle();
        img.setImageBitmap(null);
        System.gc();
    }
    public void onDestory()
    {
        super.onDestroy();
        System.gc();
    }
}
