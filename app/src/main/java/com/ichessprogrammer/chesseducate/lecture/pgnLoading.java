package com.ichessprogrammer.chesseducate.lecture;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import com.ichessprogrammer.chesseducate.R;
import com.ichessprogrammer.chesseducate.Server.PHPConnector;

/**
 * Created by 남지니 on 2016-07-28.
 */
public class pgnLoading extends Activity{
    //public ArrayList<LectureListData> mCollection;
    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        PHPConnector.MessageQue.clear();
        System.gc();
        getApplicationContext().getResources().flushLayoutCache();
        setContentView(R.layout.layout_chessloading);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        new PHPConnector().ConnectServer("","getPGNList.php","PGNList");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(PHPConnector.MessageQue.get("PGNList")==null)
                {
                }
                Log.d("디버깅",PHPConnector.MessageQue.get("PGNList"));
                Intent intent = new Intent(getApplicationContext(),make_lec_pgn_list.class);
                intent.putExtra("result",PHPConnector.MessageQue.get("PGNList"));
                startActivity(intent);
                finish();
            }
        });
        thread.start();
    }

// Call this method from onDestroy()

    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

}
