package com.ichessprogrammer.chesseducate;

import java.util.ArrayList;

/**
 * Created by 남지니 on 2016-07-13.
 */
public class Mask {

    public  ArrayList<Integer> list_X;
    public  ArrayList<Integer> list_Y;

    public int mX;
    public int mY;
    public Mask() {
        list_X = new ArrayList<Integer>();
        list_Y = new ArrayList<Integer>();
        mX = -1;
        mY = -1;
    }
    public void AddMask(int x, int y) {
        list_X.add(x);
        list_Y.add(y);
    }
    public void AddPickedIndex(int x, int y) {
        mX = x;
        mY = y;
    }

    public ArrayList<Integer> getList_X() {
        return list_X;
    }
    public ArrayList<Integer> getList_Y() {
        return list_Y;
    }

}
