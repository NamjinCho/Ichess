package com.ichessprogrammer.chesseducate.lecture;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ichessprogrammer.chesseducate.R;
import com.ichessprogrammer.chesseducate.Server.PHPConnector;

import java.util.ArrayList;

/**
 * Created by 남지니 on 2016-07-25.
 */
public class make_lec_explain_fragment extends Fragment {

    public ListView mListView = null;
    public ListViewAdapter mAdapter = null;

    Button buttonExplain;
    ImageView buttonNext;
    ImageView buttonPre;
    public String mPgn;
    boolean flag=true;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Fragment 1", "onCreateView");
        mPgn="null";
        //---Inflate the layout for this fragment---
        return inflater.inflate(
                R.layout.layout_lecexplainfragment, container, false);
    }
    public boolean dividedatas(String mPgn2,String mExplain2,String mMarks2) {
        int index = 2;
        ArrayList<ListData> decoding =new ArrayList<>();
        Log.d("디버깅", mPgn2);
        Log.d("디버깅", mExplain2);
        Log.d("디버깅",mMarks2);
        while (mPgn.indexOf(index + ". ") != -1 || mExplain2.indexOf(index + ". ") != -1) {
            ListData data = new ListData();
            int plus=0;
            if(index>9)
                plus++;
            if(index>99)
                plus++;
            if (mPgn2.indexOf(index + ". ") != -1) {
                if (mPgn2.indexOf((index + 1) + ". ") != -1)
                    data.PGN = mPgn2.substring(mPgn2.indexOf(index + ". ") + 2+plus, mPgn2.indexOf((index + 1) + ". "));
                else
                    data.PGN = mPgn2.substring(mPgn2.indexOf(index + ". ") + 2+plus);
            } else
                data.PGN = "null";

            if (mExplain2.indexOf(index + ". ") != -1) {
                if (mExplain2.indexOf((index + 1) + ". ") != -1)
                    data.explain = mExplain2.substring(mExplain2.indexOf(index + ". ") + 2+plus, mExplain2.indexOf((index + 1) + ". "));
                else
                    data.explain = mExplain2.substring(mExplain2.indexOf(index + ". ") + 2+plus);
            } else
                data.explain = "null";
            data.marks=new ArrayList<>();
            decoding.add(data);
            index++;
        }
        while(mMarks2.indexOf("(")!=-1)
        {
            String sub = mMarks2.substring(mMarks2.indexOf("("),mMarks2.indexOf(")")+1);
            mMarks2=mMarks2.replace(sub,"");

            sub=sub.replace("(","");
            sub=sub.replace(")","");
            sub=sub.trim();
            Log.d("디버깅",sub);
            String[]temps=sub.split(". ");
            temps[0]=temps[0].trim();
            temps[1]=temps[1].trim();
            ListData tempData = decoding.get(Integer.parseInt(temps[0])-1);
            BoardMark boardMark = new BoardMark();
            if(tempData.marks==null)tempData.marks = new ArrayList<>();

            if(temps[1].charAt(0)!='6')
            {
                int pos = Integer.parseInt(temps[1].charAt(0)+"");
                int row = Integer.parseInt(temps[1].charAt(1)+"");
                int col = Integer.parseInt(temps[1].charAt(2)+"");
                boardMark.pos=pos;
                boardMark.row=row;
                boardMark.col=col;
                Log.d("디버깅",tempData.PGN+"디버깅");
                tempData.marks.add(boardMark);
                Log.d("디버깅",(Integer.parseInt(temps[0])-1)+"+"+decoding.get(Integer.parseInt(temps[0])-1).marks.size());
            }
            else if(temps[1].charAt(0)=='6')
            {
                int pos = Integer.parseInt(temps[1].charAt(0)+"");
                int row = Integer.parseInt(temps[1].charAt(1)+"");
                int col = Integer.parseInt(temps[1].charAt(2)+"");
                int row2 = Integer.parseInt(temps[1].charAt(3)+"");
                int col2 = Integer.parseInt(temps[1].charAt(4)+"");
                boardMark.pos=pos;
                boardMark.row=row;
                boardMark.col=col;
                boardMark.row2=row2;
                boardMark.col2=col2;
                tempData.marks.add(boardMark);
            }
        }
        mAdapter = new ListViewAdapter(getActivity().getApplicationContext(),getActivity().getLayoutInflater());
        mAdapter.mListData=decoding;
        mListView.setAdapter(mAdapter);
        return true;
    }
    public void pgninit()
    {
        String pgns = getActivity().getIntent().getStringExtra("pgn");
        String []sub = pgns.split(" ");
        int index=0;
        for(int i=0;i<sub.length;i++)
        {
            sub[i]=sub[i].trim();
            if(sub[i].length()>0) {
                String pgn = "";
                if (index % 2 == 0)
                    pgn = sub[i] + "(W)";
                else if (index % 2 == 1)
                    pgn = sub[i] + "(B)";
                if (i == sub.length - 1)
                    pgn = sub[i] + "(F)";
                index++;
                mAdapter.addItem("null", pgn,null);
            }
        }
        PHPConnector.MessageQue.clear();
    }
    @Override
    public void onPause()
    {
        super.onPause();
    }
    @Override
    public void onResume()
    {
        super.onResume();
    }
    public void onActivityCreated(Bundle bundle)
    {
        super.onActivityCreated(bundle);
        mListView = (ListView)getActivity().findViewById(R.id.listViewExplain);
        mAdapter = new ListViewAdapter(getActivity().getApplicationContext(),getActivity().getLayoutInflater());
        mAdapter.addItem("","null",new ArrayList<BoardMark>());
        mListView.setAdapter(mAdapter);
        //backup = null;
        buttonExplain = (Button)getActivity().findViewById(R.id.buttonExplain);
        buttonExplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //plusList(null);
                Intent intent = new Intent("Lec2");
                intent.putExtra("flag",true);
                intent.putExtra("plus","plus");
                getActivity().sendBroadcast(intent);
            }
        });
        buttonNext = (ImageView) getActivity().findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("Lec2");
                intent.putExtra("next","next");
                getActivity().sendBroadcast(intent);
            }
        });
        buttonPre = (ImageView) getActivity().findViewById(R.id.buttonPre);
        buttonPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent("Lec2");
                intent.putExtra("pre","pre");
                getActivity().sendBroadcast(intent);
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //mAdapter.remove(position);
                Log.d("ad",""+position);
                Intent intent = new Intent("Lec2");
                intent.putExtra("popup","popup");
                intent.putExtra("pos",position);
                getActivity().sendBroadcast(intent);
            }
        });

        if(getActivity().getIntent().getStringExtra("opener")!=null)
            pgninit();
      //  buttonNext.setScaleX((float)0.5);
       // buttonPre.setScaleX((float)0.5);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Fragment 1", "onDestroyView");
        Intent intent = new Intent("Lec2");
        intent.putExtra("bakupList","backup");
        getActivity().sendBroadcast(intent);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        System.gc();
    }
    public void plusList(ArrayList<BoardMark>markList)
    {

        TextView mText = (TextView)getActivity().findViewById(R.id.board_explain);
        mAdapter.addItem(mText.getText().toString(),mPgn,markList);
        mPgn="null";
        mText.setText("");
    }
    public void backUpList(ArrayList<ListData> data)
    {
        Log.d("디버깅" ,"3사이즈"+data.size());
        mAdapter = new ListViewAdapter(getActivity().getApplicationContext(), getActivity().getLayoutInflater(), data);
        mListView.setAdapter(mAdapter);
    }
    public class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<ListData> mListData = new ArrayList<ListData>();
        LayoutInflater inflater;

        public ListViewAdapter(Context mContext, LayoutInflater inflater) {
            super();
            this.mContext = mContext;
            this.inflater = inflater;
        }
        public ListViewAdapter(Context mContext, LayoutInflater inflater,ArrayList<ListData> backup) {
            super();
            this.mContext = mContext;
            this.inflater = inflater;
            mListData=backup;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public ListData getItem(int position) {
            return mListData.get(position);
        }
        public ArrayList<ListData> getListData(){return mListData;}
        @Override
        public long getItemId(int position) {
            return position;
        }
        public void addItem(String explain,String pgn ,ArrayList<BoardMark> marks){
            ListData addInfo = null;
            addInfo = new ListData();
            addInfo.explain=explain+"\n";
            addInfo.PGN=pgn;
            addInfo.marks= (ArrayList<BoardMark>) marks.clone();
            mListData.add(addInfo);
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
                convertView = inflater.inflate(R.layout.explain_listview_item, null);
            }
            TextView text_item_Explain = (TextView) convertView.findViewById(R.id.explains);
            TextView text_item_PGN = (TextView) convertView.findViewById(R.id.pgns);
            //현재 position( getView()메소드의 첫번재 파라미터 )번째의 Data를 위 해당 View들에 연결..
            text_item_Explain.setText(mListData.get(position).explain);
            text_item_PGN.setText(mListData.get(position).PGN);
            return convertView;
        }
    }

}
