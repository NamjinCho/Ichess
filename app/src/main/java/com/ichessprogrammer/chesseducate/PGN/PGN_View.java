package com.ichessprogrammer.chesseducate.PGN;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ichessprogrammer.chesseducate.R;

public class PGN_View extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_pgn_view);
        Log.d("디버깅","여기까지5");
        //frag1.spinner=(Spinner)findViewById(R.id.pgnlist_spinner);
    }

    @Override
    protected void onStart() {

        super.onStart();
        Log.d("디버깅","여기까지4");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("디버깅","여기까지");
                Board_Fragment frag1 = (Board_Fragment)getFragmentManager().findFragmentById(R.id.fragment1);
                if(PGNsave.saveData!=null)
                    frag1.initiate(PGNsave.saveData);
            }
        });
        thread.start();
    }
    protected void onDestroy()
    {

        PGNsave.saveData=null;
        super.onDestroy();
    }
}




