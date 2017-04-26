package com.ichessprogrammer.chesseducate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.ichessprogrammer.chesseducate.Server.PHPConnector;

/**
 * Created by 남지니 on 2016-09-15.
 */
public class JoinActivity extends AppCompatActivity {

    EditText idBox;
    EditText paBox;
    String []checker={"abcdefghijklmnopqrstuvwxyz","ABCDEFGHIJKLMNOPQRSTUVWXYJ","1234567890"};
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            String returnedValue = (String) msg.obj;
            if(returnedValue.startsWith("success"))
            {
                setResult(RESULT_OK,getIntent());
                finish();
            }
        }
    };
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_join_acitivty);
        idBox=(EditText)findViewById(R.id.idBox);
        paBox=(EditText)findViewById(R.id.passwordBox);
    }

    public void ok_login(View v)
    {
        String MyName = idBox.getText().toString();
        String Myage = paBox.getText().toString();
        SharedPreferences sf = getSharedPreferences("Ichess",MODE_PRIVATE);
        String ID = sf.getString("ID","Don't");
        String Password = sf.getString("Password","Don't");
        String Params = "ID=" + ID +
                "&name=" +MyName + "&age=" + Integer.parseInt(Myage) + "&Password=" + Password;
       // PHPConnector.MessageQue.clear();
        new PHPConnector().ConnectServer(Params, "join.php", "join");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String result = PHPConnector.MessageQue.get("join");
                while (result==null)result=PHPConnector.MessageQue.get("join");

                if(result.startsWith("True"))
                {
                    Message msg = mHandler.obtainMessage(1,"success");
                    mHandler.sendMessage(msg);
                }
            }
        });
        thread.start();

    }
    public void cancle_login(View v)
    {
        setResult(RESULT_CANCELED);
        finish();
    }
}
