package com.ichessprogrammer.chesseducate;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 남지니 on 2016-07-24.
 */
public class ListFragment extends Fragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("ListFragment", "onCreateView");
        //---Inflate the layout for this fragment---
        return inflater.inflate(
                R.layout.layout_listfragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("ListFragment", "onActivityCreated");
        //SetData();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Fragment 1", "onPause");
        //((BitmapDrawable)listView.get.getDrawable()).getBitmap().recycle();
    }
}
