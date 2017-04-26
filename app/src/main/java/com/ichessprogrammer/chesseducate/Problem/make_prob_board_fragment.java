package com.ichessprogrammer.chesseducate.Problem;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 남지니 on 2016-07-24.
 */
public class make_prob_board_fragment extends Fragment {

    public ChessDraw_prob myLayout;
    public int me=-1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("lec_board_fragment", "onCreateView");
        myLayout = new ChessDraw_prob(getActivity().getApplication());
        //---Inflate the layout for this fragment---
        return myLayout;
    }
    public void onDestroy()
    {
        super.onDestroy();
        System.gc();
    }

}
