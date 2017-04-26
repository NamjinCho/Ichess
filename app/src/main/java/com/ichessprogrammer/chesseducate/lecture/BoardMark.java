package com.ichessprogrammer.chesseducate.lecture;

/**
 * Created by 남지니 on 2016-08-11.
 */
public class BoardMark {
    public int pos; // 1 원 2 엑스 3 4 5 큐브 6 화살표
    public int row;
    public int col;
    //화살표용
    public int row2;
    public int col2;
    public BoardMark()
    {
        pos=-1;
        row=-1;
        col=-1;
        row2=-1;
        col2=-1;
    }
}
