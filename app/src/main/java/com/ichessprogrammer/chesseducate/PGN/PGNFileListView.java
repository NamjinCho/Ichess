package com.ichessprogrammer.chesseducate.PGN;

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
import android.widget.Toast;

import com.ichessprogrammer.chesseducate.BoardDB;
import com.ichessprogrammer.chesseducate.CategoryList;
import com.ichessprogrammer.chesseducate.Edit_PGN;
import com.ichessprogrammer.chesseducate.Gallery;
import com.ichessprogrammer.chesseducate.ImageSampling;
import com.ichessprogrammer.chesseducate.KakaoTalkMainActivity;
import com.ichessprogrammer.chesseducate.MyInformation;
import com.ichessprogrammer.chesseducate.R;
import com.ichessprogrammer.chesseducate.Server.PHPConnector;
import com.ichessprogrammer.chesseducate.SettingActivity;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by 남지니 on 2016-08-25.
 */
public class PGNFileListView extends Activity {

    GridView mListView;
    ArrayList<File> filelist;
    ArrayList<pgnListData> mData;
    ListViewAdapter mListAdapter;
    int selectPosition = -1;
    @Override
    public void onCreate(Bundle savebundle)
    {
        super.onCreate(savebundle);
        setContentView(R.layout.layout_pgnfilelistview);
        mListView = (GridView)findViewById(R.id.listViewPGN);
        TextView textView = (TextView)findViewById(R.id.Title);
        textView.setText(BoardDB.Title.get(BoardDB.curPos));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main_menu);
        Log.d("왜","안돼");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle().toString().startsWith("메인")){
                    finish();
                }
                else if (item.getTitle().toString().startsWith("갤러리"))
                {
                    startActivity(new Intent(PGNFileListView.this,Gallery.class));
                }else if(item.getTitle().toString().startsWith("설정"))
                {
                    startActivity(new Intent(PGNFileListView.this,SettingActivity.class));
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
                    Intent intent = new Intent(PGNFileListView.this, KakaoTalkMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
        if(init()){
            mListAdapter=new ListViewAdapter(getApplicationContext(),getLayoutInflater());
            mListAdapter.mListData = mData;
            //mListView.setAdapter();
            mListView.setAdapter(mListAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(position==selectPosition)
                    {
                        selectPosition=-1;
                        return;
                    }
                    if(MyInformation.level>=50&&position==mData.size()-1)
                    {
                        UploadPGN.caller=3;
                        startActivity(new Intent(getApplicationContext(),UploadPGN.class));
                        finish();
                        return;
                    }
                    if(Integer.parseInt(mData.get(position).Levels)<= MyInformation.level) {
                            PGNLoading2.caller="Not";
                        new PHPConnector().ConnectServer("id=" + mData.get(position).PGNID+"&table=PGN_"+ BoardDB.Title.get(BoardDB.curPos),
                                "getPGN.php", "resultPGN");
                        startActivity(new Intent(getApplicationContext(), PGNLoading2.class));
                        PGNFileListView.this.finish();
                    }
                }
            });
            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    if(position==mListView.getCount()-1 && MyInformation.level >=50)
                    {

                    }
                    else
                    {

                        if(mData.get(position).producer.equals(MyInformation.ID) ||MyInformation.level >=999) {
                            selectPosition=position;
                            mListAdapter.dataChange();
                        }
                    }
                    return false;
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(),"현재 업로드 되어있는 기보 가 없습니다.",Toast.LENGTH_SHORT).show();
        }

    }
    public void EditButton(View v)
    {

        Edit_PGN.dataID=Integer.parseInt(mData.get(selectPosition).PGNID);
        Edit_PGN.caller=3;
        Edit_PGN.level=Integer.parseInt(mData.get(selectPosition).Levels);
        PGNLoading2.caller="Yes2";
        PGNsave.caller=true;

        new PHPConnector().ConnectServer("id=" + mData.get(selectPosition).PGNID+"&table=PGN_"+ BoardDB.Title.get(BoardDB.curPos),
                "getPGN.php", "resultPGN");
        startActivity(new Intent(getApplicationContext(), PGNLoading2.class));
        selectPosition=-1;
        mListAdapter.dataChange();
        finish();
    }
    public void DeleteButton(View v)
    {
        if(selectPosition==-1)
            return;
        String param = "id=" + mData.get(selectPosition).PGNID+ "&table=PGN_" + BoardDB.Title.get(BoardDB.curPos)
                + "&tabless=" + BoardDB.Title.get(BoardDB.curPos);
        new PHPConnector().ConnectServer(param, "deletePGN.php", "sads");
        mData.remove(selectPosition);
        // mListAdapter.dataChange();
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
        img.setImageBitmap(ImageSampling.decodeSampledBitmapFromResource(getResources(),R.drawable.bg_orange,
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
        Intent intent = new Intent(PGNFileListView.this, CategoryList.class);
        intent.putExtra("Category",BoardDB.Category.get(BoardDB.curPos));
        startActivity(intent);
        finish();
        System.gc();
    }

    public boolean init()
    {
        String result = getIntent().getStringExtra("result");
        mData = new ArrayList<>();
        if(!result.equals("False")) {
            String[] sub = result.split("!@#");
            Log.d("디버깅",result);
            //Log.d("디버깅",PGNID.length +"/"+PGNFileNames.length+"/"+sub.length+sub[0]);
            int index=0;
            for (int i = 0; i < sub.length; i= i+4) {
                pgnListData temp = new pgnListData();
                temp.PGNID=sub[i];
                temp.PGNFileNames=sub[i+1];
                temp.producer=sub[i+2];
                temp.Levels=sub[i+3];
                mData.add(temp);
                //index++;
            }

            if(MyInformation.level>=50) {
                pgnListData temp = new pgnListData();
                temp.PGNID= "";
                temp.PGNFileNames= "기보 올리기";
                temp.producer= "";
                temp.Levels= "";
                mData.add(temp);
            }
            return true;
        }
        else{

            if(MyInformation.level>=50) {
                pgnListData temp = new pgnListData();
                temp.PGNID= "";
                temp.PGNFileNames= "기보 올리기";
                temp.producer= "";
                temp.Levels= "";
                mData.add(temp);
            }
            return true;
        }

    }
    public class pgnListData{

        String PGNFileNames;
        String PGNID;
        String producer;
        String Levels;
    }
    public class ListViewAdapter extends BaseAdapter {
        LayoutInflater inflater;
        private Context mContext = null;
        private ArrayList<pgnListData> mListData;

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

        public ArrayList<pgnListData> getListData() {
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
                convertView = inflater.inflate(R.layout.pgn_list_item, null);
                if(selectPosition!=-1 && selectPosition==position)
                {

                    convertView = inflater.inflate(R.layout.pgn_list_item_edit, null);
                }
            }
            if(MyInformation.level>=50 && position==mListData.size()-1)
            {
                convertView = inflater.inflate(R.layout.pgn_list_item_plus, null);
                ImageView img = (ImageView)convertView.findViewById(R.id.list_item_img);

                return convertView;
            }
            TextView text_item_title = (TextView) convertView.findViewById(R.id.list_item_title);

            //현재 position( getView()메소드의 첫번재 파라미터 )번째의 Data를 위 해당 View들에 연결..
            text_item_title.setText(mListData.get(position).PGNFileNames);
            return convertView;
        }
    }
}
