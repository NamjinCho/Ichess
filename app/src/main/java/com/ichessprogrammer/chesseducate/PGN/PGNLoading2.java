package com.ichessprogrammer.chesseducate.PGN;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ichessprogrammer.chesseducate.Edit_PGN;
import com.ichessprogrammer.chesseducate.ImageSampling;
import com.ichessprogrammer.chesseducate.R;
import com.ichessprogrammer.chesseducate.Server.PHPConnector;

/**
 * Created by 남지니 on 2016-08-13.
 */
public class PGNLoading2 extends Activity {
    //public ArrayList<LectureListData> mCollection;
    public static String caller;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        System.gc();
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

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
               String result=PHPConnector.MessageQue.get("resultPGN");
                while(result==null)result=PHPConnector.MessageQue.get("resultPGN");

                Intent intent ;
                if(caller.contains("2"))
                {
                    intent =  new Intent(getApplicationContext(),Edit_PGN.class);
                }
                else if(caller.equals("Not"))
                    intent =  new Intent(getApplicationContext(),PGN_View.class);
                else
                    intent =  new Intent(getApplicationContext(),Quiz_PGN_View.class);//여기에 프라블럼 보는것
                PGNsave.saveData=result;
                startActivity(intent);
                Log.d("디버깅리절트","여기도2");
                PGNLoading2.this.finish();
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
