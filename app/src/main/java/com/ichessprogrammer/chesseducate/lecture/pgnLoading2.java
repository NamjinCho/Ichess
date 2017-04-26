package com.ichessprogrammer.chesseducate.lecture;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.ichessprogrammer.chesseducate.ChessBoard;
import com.ichessprogrammer.chesseducate.R;
import com.ichessprogrammer.chesseducate.Server.PHPConnector;

/**
 * Created by 남지니 on 2016-07-28.
 */
public class pgnLoading2 extends Activity{
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
        String pgnID=getIntent().getStringExtra("id");
        String Param = "ID="+pgnID;
        new PHPConnector().ConnectServer(Param,"getPGN.php","PGN");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(PHPConnector.MessageQue.get("PGN")==null)
                {
                }
                Intent intent = new Intent(getApplicationContext(),Make_Lec_Act2.class);
                intent.putExtra("opener","pgn");
                intent.putExtra("pgn",PHPConnector.MessageQue.get("PGN"));
                LecureSave.callFragment="Lec2";
                LecureSave.saveBoard=new ChessBoard();
                LecureSave.saveBoard.makeBlackPIN();
                LecureSave.saveBoard.makeWhitePIN();
                LecureSave.FEN="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
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
