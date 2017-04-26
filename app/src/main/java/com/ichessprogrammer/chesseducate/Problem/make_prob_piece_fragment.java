package com.ichessprogrammer.chesseducate.Problem;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ichessprogrammer.chesseducate.R;
import com.ichessprogrammer.chesseducate.lecture.MakeBoard;

/**
 * Created by 남지니 on 2016-07-24.
 */
public class make_prob_piece_fragment extends Fragment implements View.OnClickListener {
    MakeBoard makeBoard;
    public int donDrawPos=-1;
    public FrameLayout buttonFEN,  buttonDefault, buttonClear;
    public EditText fenText;
    //폰 룩 비숍 나이트 퀸 킹
    public ImageView []Piece = new ImageView[12];
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("make_lec_piece_fragment", "onCreateView");
        //---Inflate the layout for this fragment---\
        return inflater.inflate(
                R.layout.layout_make_prob_piece, container, false);
    }
    public void onActivityCreated(Bundle bundle)
    {
        super.onActivityCreated(bundle);
        Piece[0]=(ImageView)getActivity().findViewById(R.id.pawn_b);
        Piece[1]=(ImageView)getActivity().findViewById(R.id.rook_b);
        Piece[2]=(ImageView)getActivity().findViewById(R.id.bishop_b);
        Piece[3]=(ImageView)getActivity().findViewById(R.id.knight_b);
        Piece[4]=(ImageView)getActivity().findViewById(R.id.queen_b);
        Piece[5]=(ImageView)getActivity().findViewById(R.id.king_b);
        Piece[6]=(ImageView)getActivity().findViewById(R.id.pawn_w);
        Piece[7]=(ImageView)getActivity().findViewById(R.id.rook_w);
        Piece[8]=(ImageView)getActivity().findViewById(R.id.bishop_w);
        Piece[9]=(ImageView)getActivity().findViewById(R.id.knight_w);
        Piece[10]=(ImageView)getActivity().findViewById(R.id.queen_w);
        Piece[11]=(ImageView)getActivity().findViewById(R.id.king_w);
        //buttonFEN=(Button)getActivity().findViewById(R.id.buttonFEN);


        buttonFEN=(FrameLayout) getActivity().findViewById(R.id.buttonFEN);
        //buttonComlicate=(FrameLayout)getActivity().findViewById(R.id.solutionSetting);
        // buttonComlicate.setOnClickListener(this);
        buttonFEN.setOnClickListener(this);
        buttonDefault=(FrameLayout) getActivity().findViewById(R.id.defaultSetting);
        buttonClear=(FrameLayout)getActivity().findViewById(R.id.resetSetting);
        buttonDefault.setOnClickListener(this);
        buttonClear.setOnClickListener(this);
        fenText=(EditText)getActivity().findViewById(R.id.editFEN);
        for(int i =0;i<12;i++)
            Piece[i].setOnClickListener(this);
        //폰 룩 비숍 나이트 퀸 킹
    }
    public void update()
    {
        if(donDrawPos!=-1)
        {
            Piece[donDrawPos].setVisibility(View.INVISIBLE);
        }
        else
        {
            for(int i =0;i<12;i++)
            {
                if(Piece[i].getVisibility()==View.INVISIBLE)
                    Piece[i].setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        //버튼먼저
        if(buttonFEN.getId()==v.getId())
        {
            if(!fenText.getText().toString().equals(""))
            {
                String Fen = fenText.getText().toString();
                fenText.setText("");
                Intent intent = new Intent("prob1");
                intent.putExtra("FEN",true);
                intent.putExtra("FENString",Fen);
                Log.d("디버깅","펀"+Fen);
                getActivity().sendBroadcast(intent);
            }
        }
        else if( buttonDefault.getId()==v.getId())
        {
            donDrawPos=-1;
            update();
            Intent intent = new Intent("prob1");
            intent.putExtra("Pos", donDrawPos);
            intent.putExtra("flag", false);
            intent.putExtra("Default",true);
            getActivity().sendBroadcast(intent);

        }
        else if(buttonClear.getId()==v.getId())
        {
            donDrawPos=-1;
            update();
            Intent intent = new Intent("prob1");
            intent.putExtra("Pos", donDrawPos);
            intent.putExtra("Clear",true);
            getActivity().sendBroadcast(intent);

        } else {
            //이미지뷰
            for (int i = 0; i < 12; i++) {
                if (v.getId() == Piece[i].getId()) {
                    if (donDrawPos == -1) {
                        donDrawPos = i;
                        update();
                        Intent intent = new Intent("prob1");
                        intent.putExtra("Pos", donDrawPos);
                        intent.putExtra("flag", false);
                        getActivity().sendBroadcast(intent);
                        break;
                    } else {
                        donDrawPos = -1;
                        update();
                        break;
                    }
                }
            }
        }
    }
}
