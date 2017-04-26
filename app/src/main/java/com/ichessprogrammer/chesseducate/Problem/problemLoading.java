package com.ichessprogrammer.chesseducate.Problem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ichessprogrammer.chesseducate.ImageSampling;
import com.ichessprogrammer.chesseducate.R;
import com.ichessprogrammer.chesseducate.Server.PHPConnector;

/**
 * Created by 남지니 on 2016-08-13.
 */
public class problemLoading extends Activity {
    String result_easy;
    String result_normal;
    String result_hard;
    //public ArrayList<LectureListData> mCollection;
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
        //String Param="level="+"easy";
        //mCollection = new ArrayList<>();
        //Log.d("디버깅2",Param);
        final PHPConnector con1 = new PHPConnector();
        con1.ConnectServer("level=easy","getLectureListForProblem.php","easy");
        final PHPConnector con2 =new PHPConnector();
        con2.ConnectServer("level=normal","getLectureListForProblem.php","normal");
        final PHPConnector con3 =new PHPConnector();
        con3.ConnectServer("level=hard","getLectureListForProblem.php","hard");
        Thread tread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    result_easy=con1.MessageQue.get("easy");
                    if(result_easy!=null)
                    {
                        break;
                    }
                }
                while (true)
                {
                    result_normal=con2.MessageQue.get("normal");
                    if(result_normal!=null)
                    {
                        break;
                    }
                }
                while (true)
                {
                    result_hard=con3.MessageQue.get("hard");
                    if(result_hard!=null)
                    {
                        break;
                    }
                }
                con1.MessageQue.clear();
                con2.MessageQue.clear();
                con3.MessageQue.clear();
                Intent intent = new Intent(getApplicationContext(),lectureListFroProb.class);
                intent.putExtra("easy",result_easy);
                intent.putExtra("normal",result_normal);
                intent.putExtra("hard",result_hard);
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
