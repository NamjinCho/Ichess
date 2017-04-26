package com.ichessprogrammer.chesseducate;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ichessprogrammer.chesseducate.Server.PHPConnector;

/**
 * Created by 남지니 on 2016-09-15.
 */
public class OtherLoginActivity extends AppCompatActivity {

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

                SharedPreferences sharedPreferences = getSharedPreferences("Ichess", Activity.MODE_WORLD_WRITEABLE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("ID",idBox.getText().toString());
                editor.putString("Password",paBox.getText().toString());
                editor.commit();
                setResult(RESULT_OK,getIntent());
                finish();
            }else if(returnedValue.startsWith("NotPass"))
            {
                Toast.makeText(OtherLoginActivity.this,"비밀번호가 옳바르지 않습니다.",Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_login_acitivty);
        idBox=(EditText)findViewById(R.id.idBox);
        paBox=(EditText)findViewById(R.id.passwordBox);
    }

    public void ok_login(View v)
    {
        String temp = idBox.getText().toString();

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < checker[j].length(); i++) {
                temp=temp.replaceAll(checker[j].charAt(i) + "", "");
            }
            Log.d("디버깅","/"+temp);
        }
        if (temp.length() > 0) {
            Toast.makeText(OtherLoginActivity.this, "영어와 숫자로만 아이디를 입력해주시기 바랍니다", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (!idBox.getText().toString().equals("") && !paBox.getText().toString().equals("")) {
                new PHPConnector().ConnectServer("attr=ID&table=Account&ID=" + idBox.getText().toString()
                        + "&Password=" + paBox.getText().toString(), "isIDExist.php", "isExistID");
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = PHPConnector.MessageQue.get("isExistID");
                        while (result==null)result = PHPConnector.MessageQue.get("isExistID");
                        PHPConnector.MessageQue.clear();
                        if(result.startsWith("True"))
                        {
                            Message msg = mHandler.obtainMessage(1,"success");
                            mHandler.sendMessage(msg);
                        }
                        else
                        {
                            if(result.contains("True"))
                            {
                                Message msg = mHandler.obtainMessage(1,"NotPass");
                                mHandler.sendMessage(msg);
                            }else
                            {
                                Message msg = mHandler.obtainMessage(1,"success");
                                mHandler.sendMessage(msg);
                            }
                        }
                    }
                });
                thread.start();
            }
        }
    }
    public void cancle_login(View v)
    {
        setResult(RESULT_CANCELED);
        finish();
    }
}
