package com.ichessprogrammer.chesseducate.Problem;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ichessprogrammer.chesseducate.R;

/**
 * Created by 남지니 on 2016-07-24.
 */
public class make_prob_etc_fragment extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("make_lec_piece_fragment", "onCreateView");
        //---Inflate the layout for this fragment---\
        return inflater.inflate(
                R.layout.layout_make_prob_2_etc, container, false);
    }
    public void onActivityCreated(Bundle bundle)
    {
        super.onActivityCreated(bundle);
    }
    public void update()
    {

    }

    @Override
    public void onClick(View v) {

    }
}
