package com.ichessprogrammer.chesseducate.PGN;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ichessprogrammer.chesseducate.R;

/**
 * Created by XNOTE on 2016-08-25.
 */
public class Quiz_Fragment extends Fragment {
    TextView selection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.quiz_fragment, container, false);
    }

}
