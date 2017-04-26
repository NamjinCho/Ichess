package com.ichessprogrammer.chesseducate.PGN;

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
 * Created by 남지니 on 2016-08-13.
 */
public class PGNLoading1 extends Activity {
    //public ArrayList<LectureListData> mCollection;
    public static String caller;
    /*
            DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        RelativeLayout root = (RelativeLayout)findViewById(R.id.RootView);
        ImageView img = (ImageView)findViewById(R.id.img_view);
        img.setImageBitmap(ImageSampling.decodeSampledBitmapFromResource(getResources(),R.drawable.loading,
                width,height));

        ImageView img = (ImageView)findViewById(R.id.img_view);
        ((BitmapDrawable) img.getDrawable()).getBitmap().recycle();
        img.setImageBitmap(null);
        System.gc();

     */
    @Override
    protected void onCreate(Bundle bundle)
    {
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

        new PHPConnector().ConnectServer("table=PGN_"+ BoardDB.Title.get(BoardDB.curPos),"getPGNList.php","resultPGNLIST");


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
               String result=PHPConnector.MessageQue.get("resultPGNLIST");
                while(result==null)result=PHPConnector.MessageQue.get("resultPGNLIST");
                Intent intent = new Intent(getApplicationContext(),PGNFileListView.class);
                intent.putExtra("result",result);
                startActivity(intent);
                PGNLoading1.this.finish();
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

// Call this method from onDestroy()

    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}
