package com.ichessprogrammer.chesseducate.lecture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ichessprogrammer.chesseducate.R;
import com.ichessprogrammer.chesseducate.Server.PHPConnector;

import java.util.ArrayList;

/**
 * Created by 남지니 on 2016-07-28.
 */
public class make_lec_pgn_list extends Activity {
    public ListViewAdapter mAdapter = null;
    public ListView mListView = null;
    public ArrayList<pgnData> tempData;
    @Override
    protected void onCreate(Bundle bundle)
    {
        super. onCreate(bundle);
        setContentView(R.layout.layout_pgn_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        tempData = new ArrayList<>();
        mListView = (ListView)findViewById(R.id.pgnlist);
        init();
        mAdapter=new ListViewAdapter(this,getLayoutInflater(),tempData);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("디버깅","제목:"+tempData.get(position).title+" 아이디:"+tempData.get(position).ID);
                String title = tempData.get(position).title;
                String pgnid=tempData.get(position).ID;
                Intent intent = new Intent(getApplicationContext(),pgnLoading2.class);
                intent.putExtra("id",pgnid);
                intent.putExtra("title",title);
                startActivity(intent);
                finish();
            }
        });
    }
    private void init()
    {
        String result;
        result=getIntent().getStringExtra("result");
        if(result!=null) {
            Log.d("디버깅", result + "ㄴ");
            PHPConnector.MessageQue.clear();
            String[] sub = result.split(",");
            for (int i = 0; i < sub.length - 1; i = i + 2) {
                pgnData data = new pgnData();
                data.ID = sub[i];
                data.title = sub[i + 1];
                tempData.add(data);
                data = null;
            }
        }
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }
    public class pgnData{
        public String title;
        public String ID;
    }
    public class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<pgnData> mListData = new ArrayList<>();
        LayoutInflater inflater;

        public ListViewAdapter(Context mContext, LayoutInflater inflater,ArrayList<pgnData> listdatas) {
            super();
            this.mContext = mContext;
            this.inflater = inflater;
            mListData = listdatas;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }
        public void setmListData(ArrayList<pgnData> t)
        {
            mListData=t;
        }
        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }
        public ArrayList<pgnData> getListData(){return mListData;}
        @Override
        public long getItemId(int position) {
            return position;
        }
        public void remove(int position){
            mListData.remove(position);
            dataChange();
        }
        public void dataChange(){
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.pgnlistitem, null);
            }
            TextView text_item_title = (TextView) convertView.findViewById(R.id.pgntitle);

            //현재 position( getView()메소드의 첫번재 파라미터 )번째의 Data를 위 해당 View들에 연결..
            text_item_title.setText(mListData.get(position).title);
            return convertView;
        }
    }
}
