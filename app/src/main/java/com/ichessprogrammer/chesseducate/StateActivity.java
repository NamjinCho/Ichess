package com.ichessprogrammer.chesseducate;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class StateActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ArrayList<ItemListData> datas;
    ItemListAdapter adapter;
    ListFragment frag1;
    ImageView setting , LectureC , PGNC ,QuizC,admin;
    ImageView backGround;
    Bitmap bm;
    Bitmap reSized;

    TestDialogFragment dialogFragment;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String returnedValue = (String) msg.obj;
            if (returnedValue.equals("success")) {
                SetData();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_state);
        SetInfo();
        BoardDB.normalBoard.clear();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("Toolbar");
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getTitle().toString().startsWith("메인")){}
                else if (item.getTitle().toString().startsWith("갤러리"))
                {
                    startActivity(new Intent(StateActivity.this,Gallery.class));
                }else if(item.getTitle().toString().startsWith("설정"))
                {
                    startActivity(new Intent(StateActivity.this,SettingActivity.class));
                }else if(item.getTitle().toString().startsWith("동영상"))
                {

                }else if(item.getTitle().toString().startsWith("약도"))
                {

                }else if(item.getTitle().toString().startsWith("로그아웃")){

                }
                return false;
            }
        });
        ViewDialog();
    }
    public void ViewDialog()
    {

        FragmentManager fm = getSupportFragmentManager();
        dialogFragment = new TestDialogFragment();
        dialogFragment.show(fm, "as");
    }
    public void viewGame(View v)
    {
        Intent intent = new Intent(StateActivity.this,CategoryList.class);
        intent.putExtra("Category","기보");
        startActivity(intent);

    }
    public void lecture(View v)
    {
        Intent intent = new Intent(StateActivity.this,CategoryList.class);
        intent.putExtra("Category","강의");
        startActivity(intent);
    }
    public void quiz(View v)
    {
        Intent intent = new Intent(StateActivity.this,CategoryList.class);
        intent.putExtra("Category","문제풀기");
        startActivity(intent);
    }

    @Override
    public void onStop()
    {
        super.onStop();

        //bm.recycle();
        //reSized.recycle();
    }
    public void onResume()
    {
        super.onResume();
        backGround = (ImageView)findViewById(R.id.background);
        backGround.setImageResource(R.drawable.bg_basic);
    }
    public void onPause()
    {
        super.onPause();

        backGround = (ImageView)findViewById(R.id.background);
        backGround.setImageBitmap(null);
        Log.d("포즈","이미지 관리중");
        System.gc();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 1000);
    }
    public void SetInfo()
    {
        TextView name,level,exp,battle;
        name = (TextView)findViewById(R.id.name_info_text);
        level = (TextView)findViewById(R.id.level_info_text);
        exp = (TextView)findViewById(R.id.exp_info_text);
        ImageView levelIcon = (ImageView)findViewById(R.id.levelIcon);
        name.setText(MyInformation.name);
        level.setText(MyInformation.level+"");
        exp.setText(MyInformation.exp+" %");
        levelIcon.setImageResource(getLevelIcon());
    }

    public void SetData() {
        /*
        datas = new ArrayList<>();
        Log.d("디버깅", "사이즈는?" + datas.size());
        for (int i = 0; i < BoardDB.Category.size(); i++) {
            ItemListData temp = new ItemListData(BoardDB.Title.get(i), BoardDB.Category.get(i),
                    imgID[BoardDB.ImgPos.get(BoardDB.Category.get(i) + "_" + BoardDB.Title.get(i))]);
            datas.add(temp);
            Log.d("디버깅", BoardDB.Title.get(i));
        }
        ItemListData temp1 = new ItemListData("갤러리", "갤러리", R.drawable.setting_icon);
        datas.add(temp1);
        if (MyInformation.level >= 999) {
            ItemListData temp = new ItemListData("항목 추가", "관리자", R.drawable.setting_icon);
            datas.add(temp);
        }*/
        /*
        ListView listview = (ListView) findViewById(R.id.lecturelistview);
        adapter = new ItemListAdapter(getLayoutInflater(), datas);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (MyInformation.level >=999 && position < adapter.getCount() - 2) {
                    BoardDB.curPos = position;
                    ShowDialog();
                }
                return false;
            }
        });
        */
    }

    protected void onStart() {
        super.onStart();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (BoardDB.curPos == -1) ;

                Message msg = mHandler.obtainMessage(1, "success");
                mHandler.sendMessage(msg);
            }
        });
        thread.start();


        //int reWidth=(int)(getWindowManager().getDefaultDisplay().getWidth());

        //int reHeight=(int)(getWindowManager().getDefaultDisplay().getHeight());



        //비트맵 이미지를 해상도 가로,세로만큼 변경.
        //bm = BitmapFactory.decodeResource(getResources(), R.drawable.main_background);
        //reSized = Bitmap.createScaledBitmap(bm, reWidth,reHeight, true);
        //backGround.setImageBitmap(reSized); //
    }
    public void CancleDialog(View v)
    {
        dialogFragment.dismiss();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //pos = 0-3 , easy , normal,hard,make
        //pos = 4-7 , easy, normal , hard , make
        //pos = 8 , battle
        Log.d("testing", "" + position);
        BoardDB.curPos = position;
    }
    public int getLevelIcon()
    {
        int result =R.drawable.level_01;
        int []arrays = {R.drawable.level_01,R.drawable.level_02,R.drawable.level_03,R.drawable.level_04,R.drawable.level_05
        ,R.drawable.level_06,R.drawable.level_07,R.drawable.level_08,R.drawable.level_09,R.drawable.level_10,R.drawable.level_11,R.drawable.level_12 ,
                R.drawable.level_13,R.drawable.level_14,R.drawable.level_15,R.drawable.level_16,R.drawable.level_17,R.drawable.level_18,R.drawable.level_19,
                R.drawable.level_20,R.drawable.level_21,R.drawable.level_22,R.drawable.level_23,R.drawable.level_24,R.drawable.level_25,R.drawable.level_26,
                R.drawable.level_27,R.drawable.level_28,R.drawable.level_29,R.drawable.level_30};
        if(MyInformation.level>30)
            result = arrays[29];
        else
            result=arrays[MyInformation.level-1];
        return result;
    }
    public static class TestDialogFragment extends DialogFragment {

        Dialog myLayout;

        public TestDialogFragment(){
            super();
        }
        @Override
        public void setArguments(Bundle args) {
            super.setArguments(args);
        }

        //int[] buttonId = {R.id.rook, R.id.knight, R.id.bishop, R.id.queen };

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            super.onCreateDialog(savedInstanceState);

            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
            LayoutInflater mLayoutInflater = getActivity().getLayoutInflater();


            mBuilder.setView(mLayoutInflater
                    .inflate(R.layout.dialog_level_info, null));


            myLayout=mBuilder.create();
            myLayout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            return myLayout;
        }

        public int getLevelIcon()
        {
            int result =R.drawable.level_01;
            int []arrays = {R.drawable.level_01,R.drawable.level_02,R.drawable.level_03,R.drawable.level_04,R.drawable.level_05
                    ,R.drawable.level_06,R.drawable.level_07,R.drawable.level_08,R.drawable.level_09,R.drawable.level_10,R.drawable.level_11,R.drawable.level_12 ,
                    R.drawable.level_13,R.drawable.level_14,R.drawable.level_15,R.drawable.level_16,R.drawable.level_17,R.drawable.level_18,R.drawable.level_19,
                    R.drawable.level_20,R.drawable.level_21,R.drawable.level_22,R.drawable.level_23,R.drawable.level_24,R.drawable.level_25,R.drawable.level_26,
                    R.drawable.level_27,R.drawable.level_28,R.drawable.level_29,R.drawable.level_30};
            if(MyInformation.level>30)
                result = arrays[29];
            else
                result=arrays[MyInformation.level-1];
            return result;
        }
        @Override
        public void onResume() {
            ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();


            params.height =  (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 277, getActivity().getApplicationContext().getResources().getDisplayMetrics()
            );
            params.width =  (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 464, getActivity().getApplicationContext().getResources().getDisplayMetrics()
            );
            TextView left,middle;
            ImageView icon;
            left=(TextView)myLayout.findViewById(R.id.level_info_left);
            middle=(TextView)myLayout.findViewById(R.id.level_info_middle);
            icon=(ImageView)myLayout.findViewById(R.id.level_icon);
            left.setText(MyInformation.name+"님의 레벨은");
            middle.setText(MyInformation.level+"단계");
            icon.setImageResource(getLevelIcon());

            getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams)params);

            super.onResume();
        }

        @Override
        public void onStop() {
            super.onStop();
        }


    }
}
