package com.ichessprogrammer.chesseducate.Diolog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ichessprogrammer.chesseducate.R;

/**
 * Created by XNOTE on 2016-08-06.
 */
public class PromotionFragment extends DialogFragment {

    Dialog myLayout;
    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    //int[] buttonId = {R.id.rook, R.id.knight, R.id.bishop, R.id.queen };

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreateDialog(savedInstanceState);

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater mLayoutInflater = getActivity().getLayoutInflater();


        mBuilder.setView(mLayoutInflater
                .inflate(R.layout.promotion_fragment, null));


        myLayout=mBuilder.create();
        return myLayout;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;

        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams)params);

        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


}