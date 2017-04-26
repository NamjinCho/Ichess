package com.ichessprogrammer.chesseducate.PGN;

/**
 * Created by XNOTE on 2016-08-12.
 */
public class PgnData {

    private int number;
    private int index;
    private String data;//New version
    private boolean WB;
    private boolean isMainSequence;
    private boolean isComment;
    private boolean isCount;
    private boolean isMark;


    private int loop;
    private boolean isLoopEnd;

    public PgnData() {
        isMainSequence = true;
        number = 0;
        isComment = false;
        loop  = 0 ;
        isLoopEnd = false;
        isCount = false;
        isMark = false;
    }




    public void setIndex(int n) {
        index = n;
    }
    public void setCount(boolean b) {
        isCount = b;
    }
    public void setWB(boolean b) {
        WB = b;
    }
    public void setNumber(int n) {
        number = n;
    }
    public void setData(String d) {
        data = d;
    }
    public void setMainSequence(boolean b) {
        isMainSequence = b;
    }
    public void setComment(boolean b) {
        isComment = b;
    }

    //New version
    public void setMark(boolean b) {
        isMark = b;
    }


    public void setLoop(int i) {
        loop = i;
    }
    public void setLoopEnd(boolean b) { isLoopEnd  = b;}




    public int getIndex() {
        return  index;
    }
    public int getNumber() {
        return number;
    }
    public String getData() {
        return data;
    }
    public boolean MainSequence() {
        return isMainSequence;
    }
    public boolean Comment() {
        return isComment;
    }
    public boolean turn() {
        return WB;
    }
    public int loop() {
        return  loop;
    }
    public boolean LoopEnd() {
        return  isLoopEnd;
    }
    public boolean WB() {
        return WB;
    }
    public boolean isMark() {
        return  isMark;
    }


    public boolean isCount(){
        return isCount;
    };
}
