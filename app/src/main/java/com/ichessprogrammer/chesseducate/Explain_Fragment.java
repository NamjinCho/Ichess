package com.ichessprogrammer.chesseducate;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by 남지니 on 2016-07-09.
 */
public class Explain_Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Fragment 1", "onCreateView");
//---Inflate the layout for this fragment---
        return inflater.inflate(
                R.layout.explain_fragment, container, false);
    }

}
