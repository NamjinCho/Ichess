package com.ichessprogrammer.chesseducate.NormalBoard;

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
 * Created by 남지니 on 2016-08-30.
 */
public class BoardLoading extends Activity {
    String Category;

    @Override
    protected void onCreate(Bundle bundle) {
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


        Category=BoardDB.Category.get(BoardDB.curPos);
        if(Category.equals("게시판"))
        {

            new PHPConnector().ConnectServer("table=normalBoard_"+
                    BoardDB.Title.get(BoardDB.curPos),"getNormalBoardList.php","getListData");
        }else
        {
            new PHPConnector().ConnectServer("table=videoBoard_"+
                    BoardDB.Title.get(BoardDB.curPos),"getNormalBoardList.php","getListData");
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String result= PHPConnector.MessageQue.get("getListData");
                while (result==null) result= PHPConnector.MessageQue.get("getListData");
                Intent intent;
                intent = new Intent(getApplicationContext(),BoardList.class);
                intent.putExtra("result",result);
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
