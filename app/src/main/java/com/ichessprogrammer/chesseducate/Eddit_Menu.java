package com.ichessprogrammer.chesseducate;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.ichessprogrammer.chesseducate.Server.PHPConnector;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.util.ArrayList;

/**
 * Created by 남지니 on 2016-07-27.
 */
public class Eddit_Menu extends AppCompatActivity {

    static boolean editFlag = false;
    //EditText title;
    int imgCursor = 0;
    String myCategory = "";
    TestDialogFragment dialogFragment;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //if(myCategory.startsWith("강의"))
        myCategory = getIntent().getStringExtra("Category");
        Log.d("카테고리", myCategory);
        if(myCategory.startsWith("문제"))
        {
            setContentView(R.layout.layout_activity_eddit2);
        }else if(myCategory.startsWith("강의"))
        {
            setContentView(R.layout.layout_activity_eddit);
        }else
        {
            setContentView(R.layout.layout_activity_eddit3);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().toString().startsWith("메인")) {
                    finish();
                } else if (item.getTitle().toString().startsWith("갤러리")) {
                    startActivity(new Intent(Eddit_Menu.this, Gallery.class));
                } else if (item.getTitle().toString().startsWith("설정")) {
                    startActivity(new Intent(Eddit_Menu.this, SettingActivity.class));
                } else if (item.getTitle().toString().startsWith("동영상")) {

                } else if (item.getTitle().toString().startsWith("약도")) {

                } else if (item.getTitle().toString().startsWith("로그아웃")) {

                    KakaoTalkMainActivity.trimCache(getApplicationContext());
                    SharedPreferences preferences = getSharedPreferences("Ichess", 0);
                    if (preferences.getString("Password", "null").startsWith("kakao")) {
                        UserManagement.requestLogout(new LogoutResponseCallback() {
                            @Override
                            public void onCompleteLogout() {
                                //
                                Log.i("로그아웃", "> onCompleteLogout()");
                            }
                        });
                    }
                    preferences.edit().putString("ID", "null").commit();
                    preferences.edit().remove("Password").commit();
                    //clearApplicationCache(null);
                    Intent intent = new Intent(Eddit_Menu.this, KakaoTalkMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
    }
    public void onResume()
    {
        super.onResume();
        ImageView back =(ImageView)findViewById(R.id.background);
        if(myCategory.startsWith("문제"))
        {
            back.setImageResource(R.drawable.bg_blue);
        }else if(myCategory.startsWith("강의"))
        {
            back.setImageResource(R.drawable.bg_green);
        }else
        {

            back.setImageResource(R.drawable.bg_orange);
        }
    }
    public void onStart() {
        super.onStart();
    }

    public void ButtonCreate(View v) {
        String titles ="" ;
        EditText text = (EditText)findViewById(R.id.titlebox);
        titles=text.getText().toString();
        if (myCategory.startsWith("강의")) {
            new PHPConnector().ConnectServer("category=" + "강의&title=" + titles + "&img=" + imgCursor, "insertNewBoard.php", "insertNewBoard");
            new PHPConnector().ConnectServer("original=lecture&newtable=lecture" + "_" + titles, "CreateNewTable.php", "s");
            new PHPConnector().ConnectServer("original=clearLecture&newtable=clearLecture" + "_" + titles, "CreateNewTable.php", "ss");
            new PHPConnector().ConnectServer("original=problem&newtable=problem" + "_" + titles, "CreateNewTable.php", "d");
            new PHPConnector().ConnectServer("original=clearProblem&newtable=clearProblem" + "_" + titles, "CreateNewTable.php", "dd");
            BoardDB.Title.add(titles);
            BoardDB.Category.add("강의");
            BoardDB.ImgPos.put("강의_" + titles, imgCursor);

        } else if (myCategory.startsWith("기보")) {
            new PHPConnector().ConnectServer("category=" + "기보&title=" + titles + "&img=" + imgCursor, "insertNewBoard.php", "insertNewBoard");
            new PHPConnector().ConnectServer("original=PGN&newtable=PGN" + "_" + titles, "CreateNewTable.php", "s");
            BoardDB.Title.add(titles);
            BoardDB.Category.add("기보");
            BoardDB.ImgPos.put("기보_" + titles, imgCursor);

        } else if (myCategory.startsWith("게시판")) {
            new PHPConnector().ConnectServer("category=" + "게시판&title=" + titles + "&img=" + imgCursor, "insertNewBoard.php", "insertNewBoard");
            new PHPConnector().ConnectServer("original=normalBoard&newtable=normalBoard" + "_" + titles, "CreateNewTable.php", "s");
            BoardDB.Title.add(titles);
            BoardDB.Category.add("게시판");
            BoardDB.ImgPos.put("게시판_" + titles, imgCursor);

        } else if (myCategory.startsWith("동영상")) {
            new PHPConnector().ConnectServer("category=" + "동영상&title=" + titles + "&img=" + imgCursor, "insertNewBoard.php", "insertNewBoard");
            new PHPConnector().ConnectServer("original=videoBoard&newtable=videoBoard" + "_" + titles, "CreateNewTable.php", "s");
            BoardDB.Title.add(titles);
            BoardDB.Category.add("동영상");
            BoardDB.ImgPos.put("동영상_" + titles, imgCursor);

        } else if (myCategory.startsWith("문제")) {
            new PHPConnector().ConnectServer("category=" + "문제풀기&title=" + titles + "&img=" + imgCursor, "insertNewBoard.php", "insertNewBoard");
            new PHPConnector().ConnectServer("original=PGNproblem&newtable=PGNproblem" + "_" + titles, "CreateNewTable.php", "s");
            new PHPConnector().ConnectServer("original=clearPGNproblem&newtable=clearPGNproblem" + "_" + titles, "CreateNewTable.php", "s");
            BoardDB.Title.add(titles);
            BoardDB.Category.add("문제풀기");
            BoardDB.ImgPos.put("문제풀기_" + titles, imgCursor);
        }
        BackButton(null);
        //super.onBackPressed();
    }
    public void onBackPressed()
    {
        BackButton(null);
    }
    public void onPause()
    {
        super.onPause();
        ImageView back=(ImageView)findViewById(R.id.background);
        back.setImageBitmap(null);
        System.gc();
    }
    public void selectIcon(View v)
    {
        imgCursor = dialogFragment.img_pos;
        dialogFragment.dismiss();
        EditText text = (EditText)findViewById(R.id.editText);
        if(myCategory.startsWith("문제")){
            String imgname = "thumb_blue_";
            if(imgCursor+1<10);
                imgname+="0";
            imgname+=(imgCursor+1);
            text.setText(imgname+".png");
        }else if(myCategory.startsWith("강의")){
            String imgname = "thumb_green_";
            if(imgCursor+1<10);
            imgname+="0";
            imgname+=(imgCursor+1);
            text.setText(imgname+".png");
        }else{
            String imgname = "thumb_orange_";
            if(imgCursor+1<10);
            imgname+="0";
            imgname+=(imgCursor+1);
            text.setText(imgname+".png");
        }
    }
    public void cancleIcon(View v)
    {
        dialogFragment.dismiss();
        EditText text = (EditText)findViewById(R.id.editText);
        if(myCategory.startsWith("문제")){
            String imgname = "thumb_blue_";
            if(imgCursor+1<10);
            imgname+="0";
            imgname+=(imgCursor+1);
            text.setText(imgname+".png");
        }else if(myCategory.startsWith("강의")){
            String imgname = "thumb_green_";
            if(imgCursor+1<10);
            imgname+="0";
            imgname+=(imgCursor+1);
            text.setText(imgname+".png");
        }else{
            String imgname = "thumb_orange_";
            if(imgCursor+1<10);
            imgname+="0";
            imgname+=(imgCursor+1);
            text.setText(imgname+".png");
        }
    }
    public void BackButton(View v) {
        Intent intent = new Intent(Eddit_Menu.this, CategoryList.class);
        intent.putExtra("Category", myCategory);
        startActivity(intent);
        System.gc();
        finish();
    }

    public void ButtonFind(View v) {
        FragmentManager fm = getSupportFragmentManager();
        dialogFragment = new TestDialogFragment();
        dialogFragment.setting(myCategory,imgCursor);
        dialogFragment.show(fm, "as");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    public static class TestDialogFragment extends DialogFragment {

        ListViewAdapter mAdpater;
        ThumnailSave1 thums;
        ThumnailSave2 thums2;
        ThumnailSave3 thums3;
        Dialog myLayout;
        int img_pos=0;
        String myCategory;
        public TestDialogFragment(){
            super();
        };
        public void setting(String t , int img)
        {
            myCategory = t;
            if (myCategory.startsWith("강의")) {

                thums2 = new ThumnailSave2();
            } else if (myCategory.startsWith("문제")) {
                thums = new ThumnailSave1();
            } else {
                thums3 = new ThumnailSave3();
            }
            img_pos=img;
        }

        //int[] buttonId = {R.id.rook, R.id.knight, R.id.bishop, R.id.queen };

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            super.onCreateDialog(savedInstanceState);

            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
            LayoutInflater mLayoutInflater = getActivity().getLayoutInflater();


            mBuilder.setView(mLayoutInflater
                    .inflate(R.layout.dialog_select_icon, null));


            myLayout=mBuilder.create();
            myLayout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            return myLayout;
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

            GridView list = (GridView) myLayout.findViewById(R.id.fileView);
            mAdpater = new ListViewAdapter(getContext(),getActivity().getLayoutInflater());//(LayoutInflater)context.getSystemService(Context.LAYOUT_INFL‌​ATER_SERVICE)
            ArrayList<thumData> datas = new ArrayList<>();
            for (int i = 0; i < 21; i++) {
                thumData temp = new thumData();
                temp.pos = i;
                if(img_pos==i)
                    temp.check=true;
                datas.add(temp);
            }
            mAdpater.mListData = datas;
            list.setAdapter(mAdpater);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mAdpater.mListData.get(position).check = true;
                    img_pos=position;
                    for (int i = 0; i < 21; i++) {
                        if (i != position) {
                            mAdpater.mListData.get(i).check = false;
                        }
                    }
                    mAdpater.dataChange();
                }
            });

            getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams)params);

            super.onResume();
        }

        @Override
        public void onStop() {
            super.onStop();
        }

        public class thumData {
            int pos = 0;
            boolean check = false;
        }

        public class ListViewAdapter extends BaseAdapter {
            LayoutInflater inflater;
            private Context mContext = null;
            private ArrayList<thumData> mListData;

            public ListViewAdapter(Context mContext, LayoutInflater inflater) {
                super();
                this.mContext = mContext;
                this.inflater = inflater;
            }

            @Override
            public int getCount() {
                return mListData.size();
            }

            @Override
            public Object getItem(int position) {
                return mListData.get(position);
            }

            public ArrayList<thumData> getListData() {
                return mListData;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            public void remove(int position) {
                mListData.remove(position);
                dataChange();
            }

            public void dataChange() {
                this.notifyDataSetChanged();

            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.icon_select, null);
                }
                Log.d("안녕","안녕2");
                ImageView[] img = new ImageView[3];
                img[0] = (ImageView) convertView.findViewById(R.id.thumb);
                img[1] = (ImageView) convertView.findViewById(R.id.mask);
                img[2] = (ImageView) convertView.findViewById(R.id.mask2);
                if (mListData.get(position).check == true) {
                    img[1].setVisibility(View.VISIBLE);
                    img[2].setVisibility(View.VISIBLE);
                } else {
                    img[1].setVisibility(View.INVISIBLE);
                    img[2].setVisibility(View.INVISIBLE);
                }
                if (myCategory.startsWith("문제")) {
                    img[0].setImageResource(thums.thums[position]);
                } else if (myCategory.startsWith("강의")) {

                    img[0].setImageResource(thums2.thums[position]);
                } else {
                    img[0].setImageResource(thums3.thums[position]);
                }
                return convertView;
            }
        }
    }
}
