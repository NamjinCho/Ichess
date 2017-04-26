package com.ichessprogrammer.chesseducate;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by 남지니 on 2016-09-01.
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    RadioGroup objects,boards;
    RadioButton object1,object2,object3;
    RadioButton board1,board2,board3;
    Button ok,cancle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_setting);
        objects = (RadioGroup)findViewById(R.id.groupObject);
        boards=(RadioGroup)findViewById(R.id.groupBoard);
        object1=(RadioButton)findViewById(R.id.pieace1);
        object2=(RadioButton)findViewById(R.id.pieace2);
        object3=(RadioButton)findViewById(R.id.pieace3);
        board1=(RadioButton)findViewById(R.id.board1);
        board2=(RadioButton)findViewById(R.id.board2);
        board3=(RadioButton)findViewById(R.id.board3);
        ok=(Button)findViewById(R.id.buttonComplete);
        cancle=(Button)findViewById(R.id.buttonCancle);
        if(ChessPieaceIcon.imgPosition==0)
        {
            object1.setChecked(true);
        }else if( ChessPieaceIcon.imgPosition==1)
        {
            object2.setChecked(true);
        }else if(ChessPieaceIcon.imgPosition==2)
        {
            object3.setChecked(true);
        }

        if(ChessPieaceIcon.boardImgPosition==0)
        {
            board1.setChecked(true);
        }else if( ChessPieaceIcon.boardImgPosition==1)
        {
            board2.setChecked(true);
        }else if(ChessPieaceIcon.boardImgPosition==2)
        {
            board3.setChecked(true);
        }
        ok.setOnClickListener(this);
        cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==ok.getId())
        {
            SharedPreferences sharedPreferences = getSharedPreferences("Ichess", Activity.MODE_WORLD_WRITEABLE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if(objects.getCheckedRadioButtonId()==object1.getId()){
                editor.putInt("pieaceThema",0);
                ChessPieaceIcon.imgPosition=0;
                editor.commit();
            }else if(objects.getCheckedRadioButtonId()==object2.getId()){
                editor.putInt("pieaceThema",1);
                ChessPieaceIcon.imgPosition=1;
                editor.commit();
            }else if(objects.getCheckedRadioButtonId()==object3.getId()){
                editor.putInt("pieaceThema",2);
                ChessPieaceIcon.imgPosition=2;
                editor.commit();
            }
            editor = sharedPreferences.edit();
            if(boards.getCheckedRadioButtonId()==board1.getId()){
                editor.putInt("boardThema",0);
                ChessPieaceIcon.boardImgPosition=0;
                editor.commit();
            }else if(boards.getCheckedRadioButtonId()==board2.getId()){
                editor.putInt("boardThema",1);
                ChessPieaceIcon.boardImgPosition=1;
                editor.commit();
            }else if(boards.getCheckedRadioButtonId()==board3.getId()){
                editor.putInt("boardThema",2);
                ChessPieaceIcon.boardImgPosition=2;
                editor.commit();
            }
            editor.commit();
            finish();
        }else
        {
            finish();
        }
    }
}
