package com.ichessprogrammer.chesseducate.Problem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.ichessprogrammer.chesseducate.R;

import java.util.ArrayList;

/**
 * Created by 남지니 on 2016-08-13.
 */
public class lectureListFroProb extends Activity {

    public ListViewAdapter mAdapter1,mAdapter2,mAdapter3;
    public GridView mListView1,mListView2,mListView3;
    public ArrayList<LecTitleData> tempData1,tempData2,tempData3;
    String result_easy;
    String result_normal;
    String result_hard;
    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.layout_lecturelist_for_prob);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        result_easy=getIntent().getStringExtra("easy");
        result_normal=getIntent().getStringExtra("normal");
        result_hard=getIntent().getStringExtra("hard");
        mListView1 = (GridView)findViewById(R.id.gridView_easy);
        mListView2 = (GridView)findViewById(R.id.gridView_normal);
        mListView3 = (GridView)findViewById(R.id.gridView_hard);
        init();
        if(mAdapter1!=null) {
            mListView1.setAdapter(mAdapter1);
            mListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ProblemSave.datas=mAdapter1.getItem(position);
                    startActivity(new Intent(getApplicationContext(),MakeBoard.class));
                    Log.d("디버깅","이지");
                    lectureListFroProb.this.finish();
                }
            });
        }
        if(mAdapter2!=null) {
            mListView2.setAdapter(mAdapter2);

            mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ProblemSave.datas=mAdapter2.getItem(position);
                    startActivity(new Intent(getApplicationContext(),MakeBoard.class));
                    lectureListFroProb.this.finish();
                }
            });
            Log.d("디버깅","노말");
        }
        if(mAdapter3!=null) {
            mListView3.setAdapter(mAdapter3);
            Log.d("디버깅","하드");

            mListView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ProblemSave.datas=mAdapter3.getItem(position);
                    startActivity(new Intent(getApplicationContext(),MakeBoard.class));
                    lectureListFroProb.this.finish();
                }
            });
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        mListView1.setColumnWidth(width/6);
        mListView2.setColumnWidth(width/6);
        mListView3.setColumnWidth(width/6);
    }
    public void init()
    {
        //easy divide
        if(result_easy!=null && !result_easy.equals("False"))
        {
            Log.d("디버깅",result_easy);
            result_easy=result_easy.substring(0,result_easy.length()-1);
            String []sub = result_easy.split(",");
            tempData1 = new ArrayList<>();
            for(int i = 0 ; i<sub.length;i=i+2)
            {
                LecTitleData temp = new LecTitleData();
                temp.ID=sub[i];
                temp.Title=sub[i+1];
                temp.Level="easy";
                tempData1.add(temp);
            }
            mAdapter1= new ListViewAdapter(this,getLayoutInflater(),tempData1);
        }
        //normal divide
        if(result_normal!=null && !result_normal.equals("False"))
        {
            int i;

            Log.d("디버깅",result_normal);
            String []sub = result_normal.split(",");
            tempData2 = new ArrayList<>();
            for(i = 0 ; i<sub.length;i=i+2)
            {
                LecTitleData temp = new LecTitleData();
                temp.ID=sub[i];
                temp.Title=sub[i+1];
                temp.Level="normal";
                tempData2.add(temp);
            }
            mAdapter2= new ListViewAdapter(this,getLayoutInflater(),tempData2);
        }
        //easy divide
        if(result_hard!=null && !result_hard.equals("False"))
        {
            int i;

            Log.d("디버깅",result_hard);
            String []sub = result_hard.split(",");
            tempData3 = new ArrayList<>();
            for(i = 0 ; i<sub.length;i=i+2)
            {
                LecTitleData temp = new LecTitleData();
                temp.ID=sub[i];
                temp.Title=sub[i+1];
                temp.Level="hard";
                tempData3.add(temp);
            }
            mAdapter3= new ListViewAdapter(this,getLayoutInflater(),tempData3);
        }

    }
    public class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<LecTitleData> mListData = new ArrayList<>();
        LayoutInflater inflater;

        public ListViewAdapter(Context mContext, LayoutInflater inflater,ArrayList<LecTitleData> listdatas) {
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
        public LecTitleData getItem(int position) {
            return mListData.get(position);
        }
        public ArrayList<LecTitleData> getListData(){return mListData;}
        @Override
        public long getItemId(int position) {
            return position;
        }
        public void remove(int position){
            mListData.remove(position);
            dataChange();
        }
        public void dataChange(){
            this.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.lecture_list_item, null);
            }
            TextView text_item_title = (TextView) convertView.findViewById(R.id.list_item_title);

            //현재 position( getView()메소드의 첫번재 파라미터 )번째의 Data를 위 해당 View들에 연결..
            text_item_title.setText(mListData.get(position).Title);
            return convertView;
        }
    }
}
