package com.ichessprogrammer.chesseducate.lecture;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 남지니 on 2016-07-24.
 */
public class view_lec_board_fragment extends Fragment {

    public LectureViewChessDraw myLayout;
    public int me=-1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("lec_board_fragment", "onCreateView");
        myLayout = new LectureViewChessDraw(getActivity().getApplication());
        return myLayout;
    }

}
