package com.ichessprogrammer.chesseducate.Problem;

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
import com.ichessprogrammer.chesseducate.Edit_PGN;
import com.ichessprogrammer.chesseducate.Gallery;
import com.ichessprogrammer.chesseducate.ImageSampling;
import com.ichessprogrammer.chesseducate.KakaoTalkMainActivity;
import com.ichessprogrammer.chesseducate.MyInformation;
import com.ichessprogrammer.chesseducate.PGN.PGNLoading2;
import com.ichessprogrammer.chesseducate.PGN.PGNsave;
import com.ichessprogrammer.chesseducate.R;
import com.ichessprogrammer.chesseducate.Server.PHPConnector;
import com.ichessprogrammer.chesseducate.SettingActivity;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.util.ArrayList;

/**
 * Created by 남지니 on 2016-09-12.
 */
public class QuizList extends Activity {

    ArrayList<QuizListData> mData;
    ListViewAdapter mAdapter;
    GridView mGridView;
    int selectPosition = -1;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.layout_activity_quizlist);
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
                    startActivity(new Intent(QuizList.this,Gallery.class));
                }else if(item.getTitle().toString().startsWith("설정"))
                {
                    startActivity(new Intent(QuizList.this,SettingActivity.class));
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
                    Intent intent = new Intent(QuizList.this, KakaoTalkMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
        init();
        mGridView = (GridView)findViewById(R.id.gridView_QuizList);
        mAdapter = new ListViewAdapter(getApplicationContext(),getLayoutInflater());
        mAdapter.mListData = mData;
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(selectPosition!=-1)
                    return;
                if(MyInformation.level>=50 && position==mData.size()-1) {
                    ProblemSave.probID = -1;
                    make_prob_select.caller = 2;
                    ProblemSave.CallActivity = "prob1";
                    ProblemSave.FEN = null;
                    startActivity(new Intent(QuizList.this,make_prob_select.class));
                    finish();
                }else
                {
                    String Pos;
                    //Pos = "" + (position);
                    Intent intent = new Intent(getApplicationContext(), PGNLoading2.class);
                    //intent.putExtra("Pos", Pos);
                    PGNsave.Pos=position;
                    PGNLoading2.caller="Yes";
                    PGNsave.caller=false;
                    PHPConnector.MessageQue.clear();
                    new PHPConnector().ConnectServer("id=" + mData.get(position).quiziD+"&table=PGNproblem_"+
                            BoardDB.Title.get(BoardDB.curPos), "getPGNProblem.php", "resultPGN");
                    startActivity(intent);
                    finish();
                }
            }
        });
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if(MyInformation.level>=50)
                {
                    if(position==mData.size()-1)
                    {
                        return false;
                    }
                }
                if(MyInformation.level>999 ||mData.get(position).producer.equals(MyInformation.ID)){
                    if(selectPosition==position) {
                        selectPosition = -1;
                        return false;
                    }
                    selectPosition = position;
                    mAdapter.dataChange();
                }
                return false;
            }
        });
    }
    public void EditButton(View v)
    {

        Edit_PGN.dataID=Integer.parseInt(mData.get(selectPosition).quiziD);
        Edit_PGN.caller=1;
        Edit_PGN.level=mData.get(selectPosition).level;
        PGNLoading2.caller="Yes2";
        PGNsave.caller=true;

        new PHPConnector().ConnectServer("id=" + mData.get(selectPosition).quiziD+"&table=PGNproblem_"+
                BoardDB.Title.get(BoardDB.curPos), "getPGNProblem.php", "resultPGN");
        startActivity(new Intent(getApplicationContext(), PGNLoading2.class));
        selectPosition=-1;
        mAdapter.dataChange();
        finish();
    }
    public void DeleteButton(View v)
    {
        String param = "id=" + mData.get(selectPosition).quiziD+ "&table=PGNproblem_" + BoardDB.Title.get(BoardDB.curPos)
                + "&tabless=clearPGNproblem_" + BoardDB.Title.get(BoardDB.curPos);
        new PHPConnector().ConnectServer(param, "deleteProb2.php", "sads");
        mData.remove(selectPosition);
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
        img.setImageBitmap(ImageSampling.decodeSampledBitmapFromResource(getResources(),R.drawable.bg_blue,
                width,height));
    }
    public void onPause()
    {
        super.onPause();
        ImageView img = (ImageView)findViewById(R.id.background);
        img.setImageBitmap(null);
        System.gc();
    }
    public void onDestory()
    {
        super.onDestroy();
        System.gc();
    }
    public void init()
    {
        mData=new ArrayList<>();
        if(!DataTransfer.result.equals("False")) {
            String[] sub = DataTransfer.result.split("!@#");
            for(int i=0;i<sub.length;i=i+4)
            {
                QuizListData temp = new QuizListData();
                temp.quiziD = sub[i];
                temp.title = sub[i+1];
                temp.producer = sub[i+2];
                temp.level =Integer.parseInt(sub[i+3]);
                mData.add(temp);
            }
        }
        if(MyInformation.level>50)
        {
            QuizListData temp = new QuizListData();
            temp.title = "null";
            mData.add(temp);
        }
    }
    public void BackButton(View v)
    {
        Intent intent = new Intent(QuizList.this, CategoryList.class);
        intent.putExtra("Category","문제풀기");
        startActivity(intent);
        finish();
        //myCategory = getIntent().getStringExtra("Category");
    }
    public class QuizListData{
        public String title;
        public int iconNum=0;
        public String producer;
        public int level;
        public String quiziD;

    }
    public class ListViewAdapter extends BaseAdapter {
        LayoutInflater inflater;
        private Context mContext = null;
        private ArrayList<QuizListData> mListData;

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

        public ArrayList<QuizListData> getListData() {
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
            if (true) {
                convertView = inflater.inflate(R.layout.sub_menu_list_item, null);
            }
            if(selectPosition!=-1 && position==selectPosition)
            {

                convertView = inflater.inflate(R.layout.sub_menu_list_item_edit, null);
            }

            if(!mListData.get(position).title.equals("null")) {
                TextView text_item_title = (TextView) convertView.findViewById(R.id.subMenuTitle);
                text_item_title.setText(mListData.get(position).title);
            }else{
                TextView text_item_title = (TextView) convertView.findViewById(R.id.subMenuTitle);
                text_item_title.setVisibility(View.INVISIBLE);
                ImageView imageView = (ImageView)convertView.findViewById(R.id.plusIcon);
                TextView text = (TextView)convertView.findViewById(R.id.plusTitle);
                text.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
            }
            return convertView;
        }
    }
}
