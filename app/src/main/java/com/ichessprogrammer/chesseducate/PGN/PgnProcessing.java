package com.ichessprogrammer.chesseducate.PGN;

import android.widget.LinearLayout;

import java.util.LinkedList;

/**
 * Created by XNOTE on 2016-08-17.
 */

/**
 * Created by XNOTE on 2016-08-17.
 */
public class PgnProcessing {

    int notationNum = 0;
    LinearLayout show;
    LinkedList<PgnData> mainPgnData = new LinkedList<>();
    String txt;


    public PgnProcessing(String txt) {
        this.txt = txt;
        dataProcessing(txt);
    }

    public LinkedList<PgnData> getPgnData() {
        return mainPgnData;
    }

    public void dataProcessing(String txt) {

        boolean WB = true;

        String subString = "";

        while ((subString = txt.substring(0, ((txt.indexOf(" ") > 0)) ? txt.indexOf(" ") : txt.length())).length() > 0) {
            int length = 0;
            //CommentBox commentBox = new CommentBox();


            if (txt.startsWith(" ")) {
                length = 1;
            } else if (subString.startsWith("{")) { //comment
                String comment = txt.substring(txt.indexOf("{") + 1, txt.indexOf("}"));
                pgnDataSave(comment, notationNum, true, true, 0, false, false, WB, false);
                length = comment.length() + 3;
            } else if (subString.startsWith("(")) { // 주석
                int from = 0;
                int to = 0;
                String comment = txt.substring(1, txt.indexOf(")"));

                int search = 0;

                search = comment.indexOf("(");

                while (search != -1) { // 이중 삼중일 수도 있잖아?
                    if (comment.contains("(")) {
                        from = txt.indexOf(")", from);
                        to = txt.indexOf(")", from + 1);
                        comment = comment + txt.substring(from, to);
                        from++;
                    }
                    search = comment.indexOf("(", search + 1);
                }
                commentLoop(comment, 1);
                length = comment.length() + 3;
            } else if (ChessLogic.hasNotationNumber(subString.charAt(0))) { //숫자
                if (subString.contains(".")) {
                    notationNum = Integer.parseInt(subString.substring(0, subString.indexOf(".")));


                    if (subString.contains(".."))
                        WB = false;
                    else
                        WB = true;
                    length = subString.length() + 1;

                    pgnDataSave(subString, notationNum, false, true, 0, false, true, WB, false);
                } else if (subString.contains("-")) { //1-0인 경우도 있음.
                    length = txt.length();
                }
                //String notation = txt.substring(index, txt);
            } else if (ChessLogic.hasChessCode(subString.charAt(0))) { //숫자가 아닐 수도 있음. 흑으로 시작하면 말이야.
                pgnDataSave(subString, notationNum, false, true, 0, false, false, WB, false);
                WB = !WB;
                length = subString.length() + 1;

            } else if (subString.contains("/_")) {
                String mask = txt.substring(txt.indexOf("/_") +2 ,txt.indexOf("_/"));
                pgnDataSave(mask, notationNum, false, true, 0, false, false, WB, true);
                length = mask.length() + 5;
            }


            else {
                length = subString.length() + 1;
            }
            if (txt.length() <= length)
                break;
            txt = txt.substring(length);
        }
    }


    public void pgnDataSave(String txt, int num, boolean comment, boolean isMain, int loop, boolean loopEnd, boolean isCount, boolean WB, boolean isMark) {
        PgnData pgnData = new PgnData();


        pgnData.setData(txt);
        pgnData.setNumber(num);
        pgnData.setComment(comment);
        pgnData.setMainSequence(isMain); //널 바꿀 수 있을 것 같아.
        pgnData.setCount(isCount);
        pgnData.setWB(WB);
        pgnData.setMark(isMark);


        pgnData.setLoop(loop);
        pgnData.setLoopEnd(loopEnd);


        mainPgnData.add(pgnData);
    }

    public void commentLoop(String txt, int n) { //가정 (로 시작.

        boolean WB = false;

        String subString = "";
        int length = 0;

        while ((subString = txt.substring(0, ((txt.indexOf(" ") > 0)) ? txt.indexOf(" ") : txt.length())).length() > 0) {
            if (txt.startsWith(" ")) {
                length = 1;
            } else if (txt.startsWith("{")) { //comment
                String comment = txt.substring(txt.indexOf("{") + 1, txt.indexOf("}"));
                pgnDataSave(comment, notationNum, true, false, n, false, false, WB, false);
                length = comment.length() + 3;
            } else if (txt.startsWith("(")) { // 또 만나요~
                String comment = txt.substring(txt.indexOf("(") + 1, txt.indexOf(")"));

                int from = 0;
                int to = 0;

                int search;

                search = comment.indexOf("(");

                while (search != -1) { // 이중 삼중일 수도 있잖아?
                    if (comment.contains("(")) {
                        from = txt.indexOf(")", from);
                        to = txt.indexOf(")", from + 1);
                        comment = comment + txt.substring(from, to);
                        from++;
                    }
                    search = comment.indexOf("(", search + 1);
                }
                commentLoop(comment, n + 1);

                length = comment.length() + 3;

            } else if (ChessLogic.hasNotationNumber(subString.charAt(0))) { //number;
                notationNum = Integer.parseInt(subString.substring(0, txt.indexOf(".")));


                if (subString.contains(".."))
                    WB = false;
                else
                    WB = true;


                pgnDataSave(subString, notationNum, false, false, n, false, true, WB, false);

                length = subString.length() + 1;
                //String notation = txt.substring(index, txt);
            } else if (ChessLogic.hasChessCode(subString.charAt(0))) { //숫자가 아닐 수도 있음. 흑으로 시작하면 말이야.
                pgnDataSave(subString, notationNum, false, false, n, false, false, WB, false);
                WB = !WB;
                length = subString.length() + 1;
            } else if (subString.contains("/_")) {
                String mask = txt.substring(txt.indexOf("/_") +2 ,txt.indexOf("_/"));
                pgnDataSave(mask, notationNum, false, false, n, false, false, WB, true);
                length = mask.length() + 5;
            }
            else {
                length = subString.length() + 1;
            }
            if (txt.length() <= length)
                break;
            txt = txt.substring(length);
        }

        PgnData pgnData = mainPgnData.getLast();
        pgnData.setLoopEnd(true);

        mainPgnData.removeLast();
        mainPgnData.add(pgnData);
    }

}
