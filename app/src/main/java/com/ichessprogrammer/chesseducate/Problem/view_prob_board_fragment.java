package com.ichessprogrammer.chesseducate.Problem;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ichessprogrammer.chesseducate.FEN;
import com.ichessprogrammer.chesseducate.lecture.LecAndProb;

/**
 * Created by 남지니 on 2016-07-24.
 */
public class view_prob_board_fragment extends Fragment {

    public Prob_View_ChessDraw myLayout;
    public int Position=-1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("lec_board_fragment", "onCreateView");
        myLayout = new Prob_View_ChessDraw(getActivity().getApplication());
        //---Inflate the layout for this fragment---
        return myLayout;
    }
    public void onActivityCreated(Bundle bundle)
    {
        super.onActivityCreated(bundle);
        myLayout.mChessBoard.board= FEN.ReadFEN(LecAndProb.dataList.get(Position).mFen);
        myLayout.invalidate();
        myLayout.movingFlag=true;
    }
    public void onDestroy()
    {
        super.onDestroy();
        System.gc();
    }

}
