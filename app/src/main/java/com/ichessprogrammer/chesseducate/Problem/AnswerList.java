package com.ichessprogrammer.chesseducate.Problem;

import com.ichessprogrammer.chesseducate.lecture.BoardMark;

import java.util.ArrayList;

/**
 * Created by 남지니 on 2016-08-16.
 */
public class AnswerList {
    public String Answer;
    public String preMove;
    public String Explain;
    public ArrayList<BoardMark> hintList;
    public int myIndex;
    public int leftIndex;
    public int rightIndex;
    public AnswerList left;
    public AnswerList right;
    public String myTurn;
    public ArrayList<Integer> route;
    public String myPGNdata;
    public AnswerList()
    {
        Answer="null";
        preMove="null";
        Explain="null";
        myTurn="null";
        myPGNdata ="";
        hintList=new ArrayList<>();
        myIndex=0;
        leftIndex=0;
        rightIndex=0;
        route = new ArrayList<>();
    }
}
