package com.ichessprogrammer.chesseducate.PGN;

/**
 * Created by XNOTE on 2016-08-10.
 */
public interface PgnTagInterface {
    /**
     *  7가지 필수 요소
     * Event, Site, Date, Round, White, Black, Result
    */
    public static final String PGN_EVENT = "Event \"";
    public static final String PGN_SITE = "Site \"";
    public static final String PGN_DATE = "Date \"";
    public static final String PGN_ROUND = "Round \"";
    public static final String PGN_WHITE = "White \"";
    public static final String PGN_BLACK = "Black \"";
    public static final String PGN_RESULT = "Result \"";
    public static final String PGN_EVENTDATE = "EventDate \"";
    /**
     *  그 외 다양한 것들..
     * Annotator, PlyCount, TimeCount, TIme, Termination, Mode, FEN
     */

    public static final String PGN_ANNOTATOR = "Annotator";
    public static final String PGN_PLYCOUNT = "PlyCount";
    public static final String PGN_TIMECOUNT = "TimeCount";
    public static final String PGN_TIME = "Time";
    public static final String PGN_TERMINATION = "Termination";
    public static final String PGN_MODE = "Mode";
    public static final String PGN_FEN = "FEN \"";
}
