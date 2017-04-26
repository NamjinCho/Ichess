package com.ichessprogrammer.chesseducate.lecture;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ichessprogrammer.chesseducate.BoardDB;
import com.ichessprogrammer.chesseducate.ImageSampling;
import com.ichessprogrammer.chesseducate.MyInformation;
import com.ichessprogrammer.chesseducate.R;
import com.ichessprogrammer.chesseducate.Server.PHPConnector;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 남지니 on 2016-08-18.
 */
public class LectureLoading2 extends Activity {

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
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

        new PHPConnector().ConnectServer("lecID="+LecAndProb.LectureData.lecID+"&table=problem_"+
                BoardDB.Title.get(BoardDB.curPos),"getProb.php","prob");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String result;
                while (true)
                {
                  result=PHPConnector.MessageQue.get("prob");
                    if(result!=null) {
                        Log.d("디버깅","?"+result);
                        break;
                    }
                }
                new PHPConnector().ConnectServer("id="+ MyInformation.ID+"&table=clearProblem_"+
                        BoardDB.Title.get(BoardDB.curPos),"getClearProblemList.php","clearProblem");
                String result2=PHPConnector.MessageQue.get("clearProblem");
                while (result2==null)
                    result2=PHPConnector.MessageQue.get("clearProblem");
                PHPConnector.MessageQue.clear();
                MyInformation.ClearProbID = new ArrayList<>();
                MyInformation.ClearProbPoint = new HashMap<>();
                if(!result2.equals("False")) {
                    String[] subR = result2.split(",");
                    for (int i = 0; i < subR.length; i=i+2)
                    {
                        MyInformation.ClearProbID.add(subR[i]);
                        MyInformation.ClearProbPoint.put(subR[i],subR[i+1]);
                    }
                }
                Intent intent =  new Intent(getApplicationContext(),LecturWithProblem.class);
                intent.putExtra("prob",result);
                startActivity(intent);
                finish();
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
}
