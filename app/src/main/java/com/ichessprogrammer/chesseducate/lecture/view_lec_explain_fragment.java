package com.ichessprogrammer.chesseducate.lecture;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ichessprogrammer.chesseducate.PGNController;
import com.ichessprogrammer.chesseducate.R;

import java.util.ArrayList;

/**
 * Created by 남지니 on 2016-07-25.
 */
public class view_lec_explain_fragment extends Fragment {

    String mPgn;
    String mExplain;
    String mMarks;
    TextView pgnT, expT;
    Button pre, next;
    int curPos = 0;
    String fen;
    ArrayList<ListData> mDatas = new ArrayList<>();
    PGNController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Fragment 1", "onCreateView");
        controller = new PGNController();
        //---Inflate the layout for this fragment---
        return inflater.inflate(
                R.layout.layout_lec_view_explain, container, false);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        pgnT = (TextView) getActivity().findViewById(R.id.textViewPGN);
        expT = (TextView) getActivity().findViewById(R.id.textViewExplain);
        pre = (Button) getActivity().findViewById(R.id.buttonPrePGN);
        next = (Button) getActivity().findViewById(R.id.buttonNextPGN);
        dividedatas();
        if (mDatas.get(curPos).PGN.startsWith("null"))
            pgnT.setText("");
        else
            pgnT.setText(mDatas.get(curPos).PGN);
        if (mDatas.get(curPos).explain.startsWith("null"))
            expT.setText("");
        else
            expT.setText(mDatas.get(curPos).explain);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curPos + 1 < mDatas.size()) {
                    curPos++;
                    boolean wb = true;
                    String tempPgn="null";
                    if (mDatas.get(curPos).PGN.startsWith("null"))
                        pgnT.setText("");
                    else {
                        pgnT.setText(mDatas.get(curPos).PGN);

                        if (mDatas.get(curPos).PGN.contains("(W)"))
                            wb = true;
                        else
                            wb = false;
                        if (wb)
                            tempPgn = mDatas.get(curPos).PGN.replace("(W)", "");
                        else
                            tempPgn = mDatas.get(curPos).PGN.replace("(B)", "");
                    }
                    if (mDatas.get(curPos).explain.startsWith("null"))
                        expT.setText("");
                    else
                        expT.setText(mDatas.get(curPos).explain);
                    if(tempPgn.contains("(F)"))
                        tempPgn="null";
                    Intent intent = new Intent("ViewLec");
                    intent.putExtra("Update", true);
                    intent.putExtra("WB", wb);
                    intent.putExtra("PGN", tempPgn.trim());
                    intent.putExtra("Pos",curPos);
                    Log.d("디버깅","ㄴ"+curPos);
                    getActivity().sendBroadcast(intent);
                }
                else
                {
                    Intent intent = new Intent("ViewLec");
                    intent.putExtra("Clear",true);
                    getActivity().sendBroadcast(intent);
                }
            }
        });
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curPos - 1 >= 0) {
                    curPos--;

                    if (mDatas.get(curPos).PGN.startsWith("null")) {
                        pgnT.setText("");
                    }
                    else {
                        pgnT.setText(mDatas.get(curPos).PGN);
                    }
                    if (mDatas.get(curPos).explain.startsWith("null"))
                        expT.setText("");
                    else
                        expT.setText(mDatas.get(curPos).explain);
                    Intent intent = new Intent("ViewLec");
                    intent.putExtra("Back", true);
                    intent.putExtra("Pos",curPos);
                    getActivity().sendBroadcast(intent);

                }
            }
        });
    }

    public void dividedatas() {
        int index = 1;
        Log.d("디버깅", mPgn);
        Log.d("디버깅", mExplain);
        Log.d("디버깅",mMarks);
        while (mPgn.indexOf(index + ". ") != -1 || mExplain.indexOf(index + ". ") != -1) {
            ListData data = new ListData();
            int plus=0;
            if(index>9)
                plus++;
            if(index>99)
                plus++;
            if (mPgn.indexOf(index + ". ") != -1) {
                if (mPgn.indexOf((index + 1) + ". ") != -1)
                    data.PGN = mPgn.substring(mPgn.indexOf(index + ". ") + 2+plus, mPgn.indexOf((index + 1) + ". "));
                else
                    data.PGN = mPgn.substring(mPgn.indexOf(index + ". ") + 2+plus);
            } else
                data.PGN = "null";

            if (mExplain.indexOf(index + ". ") != -1) {
                if (mExplain.indexOf((index + 1) + ". ") != -1)
                    data.explain = mExplain.substring(mExplain.indexOf(index + ". ") + 2+plus, mExplain.indexOf((index + 1) + ". "));
                else
                    data.explain = mExplain.substring(mExplain.indexOf(index + ". ") + 2+plus);
            } else
                data.explain = "null";
            data.marks=new ArrayList<>();
            mDatas.add(data);
            index++;
        }
        while(mMarks.indexOf("(")!=-1)
        {
            String sub = mMarks.substring(mMarks.indexOf("("),mMarks.indexOf(")")+1);
            mMarks=mMarks.replace(sub,"");

            sub=sub.replace("(","");
            sub=sub.replace(")","");
            sub=sub.trim();
            Log.d("디버깅",sub);
            String[]temps=sub.split(". ");
            temps[0]=temps[0].trim();
            temps[1]=temps[1].trim();
            ListData tempData = mDatas.get(Integer.parseInt(temps[0])-1);
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
                Log.d("디버깅",(Integer.parseInt(temps[0])-1)+"+"+mDatas.get(Integer.parseInt(temps[0])-1).marks.size());
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
    }
}
