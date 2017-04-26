package com.ichessprogrammer.chesseducate.Diolog;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.ichessprogrammer.chesseducate.Problem.ProblemView;

public class Custom_Dialog3 extends Dialog implements View.OnTouchListener {

    public boolean flag=false;
    Context mContext;
    ProblemView viewer;
    public Custom_Dialog3(Context context, int theme, ProblemView me) {
        super(context, theme);
        mContext = context;
        viewer = me;
        // TODO Auto-generated constructor stub

    }
    public void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        if(flag)
            viewer.newGame();
        else
            viewer.mExit();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Custom_Dialog3.this.cancel();
        return false;
    }
}