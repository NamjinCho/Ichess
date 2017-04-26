package com.ichessprogrammer.chesseducate;

/**
 * Created by 남지니 on 2016-07-08.
 */public class PIN implements Cloneable {
    public String Type;
    public boolean enPassantEnable ;
    public boolean WB;
    public int x,y;
    public boolean move;

    public PIN(){
        enPassantEnable = false;
        move = false;
    }
    public Object clone() throws CloneNotSupportedException {
        return (PIN)super.clone();
    }

}
