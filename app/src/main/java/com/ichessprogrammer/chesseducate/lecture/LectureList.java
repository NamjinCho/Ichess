package com.ichessprogrammer.chesseducate.lecture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ichessprogrammer.chesseducate.BoardDB;
import com.ichessprogrammer.chesseducate.CategoryList;
import com.ichessprogrammer.chesseducate.Gallery;
import com.ichessprogrammer.chesseducate.ImageSampling;
import com.ichessprogrammer.chesseducate.KakaoTalkMainActivity;
import com.ichessprogrammer.chesseducate.MyInformation;
import com.ichessprogrammer.chesseducate.R;
import com.ichessprogrammer.chesseducate.Server.PHPConnector;
import com.ichessprogrammer.chesseducate.SettingActivity;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.util.ArrayList;

/**
 * Created by 남지니 on 2016-07-28.
 */
public class LectureList extends Activity {
    public ListViewAdapter mAdapter = null;
    public GridView mListView = null;
    public ArrayList<LectureListData> tempData;
    public int selectPosition = -1;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_activity_lecturelist);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        TextView textView = (TextView)findViewById(R.id.Title);
        textView.setText(BoardDB.Title.get(BoardDB.curPos));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle().toString().startsWith("메인")){
                    finish();
                }
                else if (item.getTitle().toString().startsWith("갤러리"))
                {
                    startActivity(new Intent(LectureList.this,Gallery.class));
                }else if(item.getTitle().toString().startsWith("설정"))
                {
                    startActivity(new Intent(LectureList.this,SettingActivity.class));
                }else if(item.getTitle().toString().startsWith("동영상"))
                {

                }else if(item.getTitle().toString().startsWith("약도"))
                {

                }else if(item.getTitle().toString().startsWith("로그아웃")){
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
                    Intent intent = new Intent(LectureList.this, KakaoTalkMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
        tempData = new ArrayList<>();
        String result = getIntent().getStringExtra("passData");
        //아이디 , 레벨 , 펀 , 피지엔 , 설명 , 제목
        if (!result.startsWith("False")) {
            String[] subResult = result.split("@#!");
            for (int i = 0; i < subResult.length; i = i + 8) {
                LectureListData newData = new LectureListData();
                newData.lecID = subResult[i];
                newData.level = subResult[i + 1];
                newData.fen = subResult[i + 2];
                newData.pgn = subResult[i + 3];
                newData.exlpain = subResult[i + 4];
                newData.title = subResult[i + 5];
                newData.producer = subResult[i + 6];
                if (i + 7 < subResult.length)
                    newData.marks = subResult[i + 7];
                Log.d("디버깅", "/" + subResult.length);
                tempData.add(newData);
            }
        }
        if (MyInformation.level >= 50) {
            LectureListData newData = new LectureListData();
            newData.title = "강의 추가";
            tempData.add(newData);
        }
        mListView = (GridView) findViewById(R.id.lectureListView);
       // mListView.setColumnWidth(width / 6);
        mAdapter = new ListViewAdapter(getApplicationContext(), getLayoutInflater(), tempData);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LectureListData curData = (LectureListData) mAdapter.getItem(position);
                if (((LectureListData) mAdapter.getItem(position)).title.equals("강의 추가")) {
                    Make_Lec_Act2.caller = 1;
                    LecureSave.lecID=-1;
                    Intent intent = new Intent(getApplicationContext(), MakeBoard.class);
                    startActivity(intent);
                    finish();
                    return;
                }
                if (Integer.parseInt(curData.level) > MyInformation.level)
                    return;

                if (MyInformation.ClearLecID.containsKey(BoardDB.Title.get(BoardDB.curPos))) {

                    Intent intent = new Intent(getApplicationContext(), LectureLoading2.class);
                    LecAndProb.LectureData = curData;
                    startActivity(intent);
                    LectureList.this.finish();
                } else {
                    String passing = "";
                    passing = passing + curData.fen + "@#!" + curData.pgn + "@#!" + curData.exlpain + "@#!" + curData.lecID + "@#!" + curData.level + "@#!" +
                            curData.marks;
                    LecAndProb.LectureData = curData;
                    Intent intent = new Intent(getApplicationContext(), LectrueView.class);
                    intent.putExtra("passData", passing);
                    startActivity(intent);
                    LectureList.this.finish();
                }
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(MyInformation.level>=999 || MyInformation.ID.equals(tempData.get(position).producer)) {
                    selectPosition = position;
                    mAdapter.dataChange();
                }
                //ShowDialog(position);
                return true;
            }
        });
    }

    public void EditButton(View v)
    {

        LectureListData curData = (LectureListData) mAdapter.getItem(selectPosition);
        String passing = "";
        passing = passing + curData.fen + "@#!" + curData.pgn + "@#!" + curData.exlpain + "@#!" + curData.lecID + "@#!" + curData.level + "@#!" +
                curData.marks;
        Make_Lec_Act2.caller = 2;
        LecureSave.prod=curData.producer;
        Intent intent = new Intent(LectureList.this, Make_Lec_Act2.class);
        intent.putExtra("passing", passing);
        startActivity(intent);
        finish();
        selectPosition=-1;
        mAdapter.dataChange();
    }
    public void DeleteButton(View v)
    {

        LectureListData curData = (LectureListData) mAdapter.getItem(selectPosition);
        String tableParam = "&table=problem_" + BoardDB.Title.get(BoardDB.curPos) + "&tabless=clearProblem_" + BoardDB.Title.get(BoardDB.curPos)
                + "&tablesss=lecture_" + BoardDB.Title.get(BoardDB.curPos) + "&tablessss=clearLecture_" + BoardDB.Title.get(BoardDB.curPos);
        Log.d("디버깅", tableParam);
        MyInformation.ClearLecID.remove(BoardDB.Title.get(BoardDB.curPos));
        //mAdapter.remove(selectPosition);
        new PHPConnector().ConnectServer("id=" + curData.lecID + tableParam, "deleteLec.php", "delLec");
        tempData.remove(selectPosition);
        selectPosition=-1;
        mAdapter.dataChange();
    }
    public void onResume()
    {
        super.onResume();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        ImageView img = (ImageView)findViewById(R.id.background);
        img.setImageBitmap(ImageSampling.decodeSampledBitmapFromResource(getResources(),R.drawable.bg_green,
                width,height));
    }

    public void onPause()
    {
        super.onPause();
        ImageView img = (ImageView)findViewById(R.id.background);
        img.setImageBitmap(null);
        System.gc();
    }
    public void BackButton(View v)
    {
        onBackPressed();
        //myCategory = getIntent().getStringExtra("Category");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LectureList.this, CategoryList.class);
        intent.putExtra("Category",BoardDB.Category.get(BoardDB.curPos));
        startActivity(intent);
        finish();
        System.gc();
    }


    public class ListViewAdapter extends BaseAdapter {
        LayoutInflater inflater;
        private Context mContext = null;
        private ArrayList<LectureListData> mListData = new ArrayList<>();

        public ListViewAdapter(Context mContext, LayoutInflater inflater, ArrayList<LectureListData> listdatas) {
            super();
            this.mContext = mContext;
            this.inflater = inflater;
            mListData = listdatas;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        public ArrayList<LectureListData> getListData() {
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
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if (true) {
                convertView = inflater.inflate(R.layout.lecture_list_item, null);

                if(selectPosition!=-1 && position==selectPosition)
                {
                    convertView = inflater.inflate(R.layout.lecture_list_item_edit, null);
                }
                ImageView img = (ImageView)convertView.findViewById(R.id.list_item_img);
                TextView text_item_title = (TextView) convertView.findViewById(R.id.list_item_title);

                //현재 position( getView()메소드의 첫번재 파라미터 )번째의 Data를 위 해당 View들에 연결..
                text_item_title.setText(mListData.get(position).title);
            }
            if(MyInformation.level>=50 && position==mListData.size()-1)
            {
                convertView = inflater.inflate(R.layout.lecture_list_item_plus, null);
                ImageView img = (ImageView)convertView.findViewById(R.id.list_item_img);

            }
            //convertView
            return convertView;
        }
    }
}
