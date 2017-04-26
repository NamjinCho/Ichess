package com.ichessprogrammer.chesseducate.lecture;

import java.util.ArrayList;

/**
 * Created by 남지니 on 2016-08-18.
 */
public class LecAndProb {
    public static LectureListData LectureData;
    public static ArrayList<ProbData>dataList;
    public static class ProbData{
        public String probTitle;
        public String probID;
        public String mTitle;
        public String mFen;
        public String hint;
        public String answer;
        public String producer;
        public String explain;
        public String minLevel;
    }
}
