package com.ichessprogrammer.chesseducate.lecture;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.ichessprogrammer.chesseducate.BoardDB;
import com.ichessprogrammer.chesseducate.ImageSampling;
import com.ichessprogrammer.chesseducate.R;
import com.ichessprogrammer.chesseducate.Server.PHPConnector;

/**
 * Created by 남지니 on 2016-07-28.
 */
public class LecturLoading extends Activity{
     String result;
    //public ArrayList<LectureListData> mCollection;
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
        PHPConnector.MessageQue.clear();
        System.gc();
        getApplicationContext().getResources().flushLayoutCache();
        setContentView(R.layout.layout_chessloading);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        ImageView img = (ImageView)findViewById(R.id.img_view);
        img.setImageBitmap(ImageSampling.decodeSampledBitmapFromResource(getResources(),R.drawable.loading,
                width,height));

        //String level = getIntent().getStringExtra("level");
        String Param="table="+"lecture"+"_"+BoardDB.Title.get(BoardDB.curPos);
        //mCollection = new ArrayList<>();
        Log.d("디버깅2",Param);
        new PHPConnector().ConnectServer(Param,"getLectureList.php","getLectureList");
        Thread tread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    if(PHPConnector.MessageQue.get("getLectureList")!=null&&!PHPConnector.MessageQue.get("getLectureList").equals(""))
                    {
                        result = PHPConnector.MessageQue.get("getLectureList");
                        PHPConnector.MessageQue.put("geLecutreList","");
                        PHPConnector.MessageQue.clear();
                        break;
                    }
                }
                Intent intent = new Intent(getApplicationContext(),LectureList.class);
                intent.putExtra("passData",result);
                startActivity(intent);
                finish();
            }
        });
        tread.start();
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
