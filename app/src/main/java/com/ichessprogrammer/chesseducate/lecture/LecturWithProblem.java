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
import com.ichessprogrammer.chesseducate.Edit_PGN;
import com.ichessprogrammer.chesseducate.Gallery;
import com.ichessprogrammer.chesseducate.ImageSampling;
import com.ichessprogrammer.chesseducate.KakaoTalkMainActivity;
import com.ichessprogrammer.chesseducate.MyInformation;
import com.ichessprogrammer.chesseducate.PGN.PGNLoading2;
import com.ichessprogrammer.chesseducate.PGN.PGNsave;
import com.ichessprogrammer.chesseducate.Problem.LecTitleData;
import com.ichessprogrammer.chesseducate.Problem.ProblemSave;
import com.ichessprogrammer.chesseducate.Problem.make_prob_select;
import com.ichessprogrammer.chesseducate.R;
import com.ichessprogrammer.chesseducate.Server.PHPConnector;
import com.ichessprogrammer.chesseducate.SettingActivity;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.util.ArrayList;

/**
 * Created by 남지니 on 2016-08-18.
 */
public class LecturWithProblem extends Activity {

    //public ArrayList<LecAndProb.ProbData> datas = new ArrayList<>();
    public ListViewAdapter mListAdapter;
    public GridView mGridView = null;
    boolean flag = false;
    public int selectPosition = -1;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_activity_lecturelist);

        //
        TextView textView = (TextView)findViewById(R.id.Title);
        textView.setText(LecAndProb.LectureData.title);
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
                    startActivity(new Intent(LecturWithProblem.this,Gallery.class));
                }else if(item.getTitle().toString().startsWith("설정"))
                {
                    startActivity(new Intent(LecturWithProblem.this,SettingActivity.class));
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
                    Intent intent = new Intent(LecturWithProblem.this, KakaoTalkMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();                }
                return false;
            }
        });
        //
        init();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        mListAdapter = new ListViewAdapter(this, getLayoutInflater(), LecAndProb.dataList);

        mGridView = (GridView) findViewById(R.id.lectureListView);
        mGridView.setAdapter(mListAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    String passing = "";
                    passing = passing + LecAndProb.LectureData.fen + "@#!" + LecAndProb.LectureData.pgn + "@#!"
                            + LecAndProb.LectureData.exlpain + "@#!" + LecAndProb.LectureData.lecID + "@#!" + LecAndProb.LectureData.level + "@#!" +
                            LecAndProb.LectureData.marks;
                    Intent intent = new Intent(getApplicationContext(), LectrueView.class);
                    intent.putExtra("passData", passing);

                    startActivity(intent);
                    LecturWithProblem.this.finish();
                } else if (MyInformation.level >= 50 && position == LecAndProb.dataList.size() - 1) {
                    ProblemSave.probID = -1;
                    make_prob_select.caller=1;
                    ProblemSave.CallActivity="prob1";
                    ProblemSave.FEN=null;
                    //ProblemSave.datas=null;
                    Intent intent = new Intent(getApplicationContext(), make_prob_select.class);
                    startActivity(intent);
                    LecturWithProblem.this.finish();
                } else {
                    if (Integer.parseInt(LecAndProb.dataList.get(position).minLevel) <= MyInformation.level) {
                        String Pos;
                        //Pos = "" + (position);
                        Intent intent = new Intent(getApplicationContext(), PGNLoading2.class);
                        //intent.putExtra("Pos", Pos);
                        PGNsave.Pos=position;
                        PGNLoading2.caller="Yes";
                        PGNsave.caller=true;
                        PHPConnector.MessageQue.clear();
                        new PHPConnector().ConnectServer("id=" +LecAndProb.dataList.get(position).probID+"&table=problem_"+
                                BoardDB.Title.get(BoardDB.curPos), "getProbData.php", "resultPGN");
                        startActivity(intent);
                        LecturWithProblem.this.finish();
                    }
                }
            }
        });
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                        if (LecAndProb.dataList.get(position).producer.equals(MyInformation.ID) || MyInformation.level >= 999) {
                            selectPosition=position;
                            mListAdapter.dataChange();
                        }
                }
                return true;
            }
        });
    }
    public void EditButton(View v)
    {
        Edit_PGN.dataID=Integer.parseInt(LecAndProb.dataList.get(selectPosition).probID);
        Edit_PGN.caller=2;
        Edit_PGN.level=Integer.parseInt(LecAndProb.dataList.get(selectPosition).minLevel);
        PGNLoading2.caller="Yes2";
        PGNsave.caller=true;

        new PHPConnector().ConnectServer("id=" +LecAndProb.dataList.get(selectPosition).probID+"&table=problem_"+
                BoardDB.Title.get(BoardDB.curPos), "getProbData.php", "resultPGN");
        startActivity(new Intent(getApplicationContext(), PGNLoading2.class));
        selectPosition=-1;
        mListAdapter.dataChange();
        finish();
    }
    public void DeleteButton(View v)
    {
        String param = "id=" + LecAndProb.dataList.get(selectPosition).probID + "&table=problem_" + BoardDB.Title.get(BoardDB.curPos)
                + "&tabless=clearProblem_" + BoardDB.Title.get(BoardDB.curPos);

        new PHPConnector().ConnectServer(param, "deleteProb.php", "sads");
        LecAndProb.dataList.remove(selectPosition);
        selectPosition=-1;
        mListAdapter.dataChange();
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
        Intent intent = new Intent(LecturWithProblem.this, LecturLoading.class);
        //intent.putExtra("Category",BoardDB.Category.get(BoardDB.curPos));
        startActivity(intent);
        finish();
        System.gc();
    }


    protected void init() {
        String result = getIntent().getStringExtra("prob");
        LecAndProb.dataList = new ArrayList<>();
        LecAndProb.dataList.add(new LecAndProb.ProbData());
        if (!result.equals("False")) {
            Log.d("디버깅", "여기가안오나?2" + result);
            //probid , fen , hint , answer
            String[] sub = result.split("@#!");
            for (int i = 0; i < sub.length; i = i + 3) {
                LecAndProb.ProbData temp = new LecAndProb.ProbData();
                temp.probID = sub[i].trim();
                temp.minLevel = sub[i + 2];
                temp.producer = sub[i + 1];
                LecAndProb.dataList.add(temp);
            }
        }

        if (MyInformation.level >= 50) {
            LecAndProb.ProbData temp = new LecAndProb.ProbData();
            temp.mTitle = "문제 추가";
            LecAndProb.dataList.add(temp);
            ProblemSave.datas = new LecTitleData();
            ProblemSave.datas.Title = LecAndProb.LectureData.title;
            ProblemSave.datas.ID = LecAndProb.LectureData.lecID;
            ProblemSave.datas.Level = LecAndProb.LectureData.level;
        }
    }


    public class ListViewAdapter extends BaseAdapter {
        LayoutInflater inflater;
        private Context mContext = null;
        private ArrayList<LecAndProb.ProbData> mListData = new ArrayList<>();

        public ListViewAdapter(Context mContext, LayoutInflater inflater, ArrayList<LecAndProb.ProbData> listdatas) {
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

        public ArrayList<LecAndProb.ProbData> getListData() {
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

            if (true) {
                convertView = inflater.inflate(R.layout.lecture_list_item, null);
                if(selectPosition!=-1 && position==selectPosition)
                {
                    convertView = inflater.inflate(R.layout.lecture_list_item_edit, null);
                }
            }
            if(MyInformation.level>=50 && position==mListData.size()-1)
            {
                convertView = inflater.inflate(R.layout.lecture_list_item_plus2, null);
                ImageView img = (ImageView)convertView.findViewById(R.id.list_item_img);
                int width;
                int height;
                return convertView;
            }


            TextView text_item_title = (TextView) convertView.findViewById(R.id.list_item_title);

            //현재 position( getView()메소드의 첫번재 파라미터 )번째의 Data를 위 해당 View들에 연결..
            if(position==0)
            {
                text_item_title.setText(LecAndProb.LectureData.title);
            }else
            {
                text_item_title.setText("예제 "+position);
            }
            //현재 position( getView()메소드의 첫번재 파라미터 )번째의 Data를 위 해당 View들에 연결..

            return convertView;
        }
    }
}
