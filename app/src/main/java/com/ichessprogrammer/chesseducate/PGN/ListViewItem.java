package com.ichessprogrammer.chesseducate.PGN;

/**
 * Created by XNOTE on 2016-08-10.
 */
public class ListViewItem {
    private String whitePlayer;
    private String blackPlayer;
    private String date;
    private String result;


    public void setWhitePlayer(String wp) {
        whitePlayer = wp;
    }
    public void setBlackPlayer(String bp) {
        blackPlayer = bp;
    }
    public void setDate(String d) {
        date = d;
    }
    public void setResult(String r) {
        result = r;
    }
    public String getWhitePlayer (){
        return whitePlayer;
    }
    public String getBlackPlayer() {
        return blackPlayer;
    }
    public String getDate() {
        return date;
    }
    public String getResult() {
        return result;
    }
}
