package com.ichessprogrammer.chesseducate.PGN;

import android.util.SparseArray;

/**
 * Created by XNOTE on 2016-08-25.
 */
public class PgnQuizList {

    /*
    public ArrayList<String> event = new ArrayList<String>();
    public ArrayList<String> whites = new ArrayList<String>();
    public ArrayList<String> blacks = new ArrayList<String>();
    public ArrayList<String> dates = new ArrayList<String>();
    public ArrayList<String> result = new ArrayList<String>();
    public ArrayList<StringBuffer> pgnFile = new ArrayList<>();
    public HashMap<Integer, String> dataFEN = new HashMap<>();
*/


    public SparseArray<String> events = new SparseArray<>();
    public SparseArray<String> sites = new SparseArray<>();
    public SparseArray<String> dates = new SparseArray<>();
    public SparseArray<String> rounds = new SparseArray<>();
    public SparseArray<String> whites = new SparseArray<>();
    public SparseArray<String> blacks = new SparseArray<>();
    public SparseArray<String> results = new SparseArray<>();

    public SparseArray<String> fens = new SparseArray<>();

    // 새로 추가

    public SparseArray<StringBuffer> pgnFile = new SparseArray<>();


}


