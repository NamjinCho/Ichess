package com.ichessprogrammer.chesseducate;

/**
 * Created by 남지니 on 2016-09-02.
 */
public class CheckMM {
    public static int check(int temp)
    {
        if(temp < 0)
            temp=0;
        if(temp >7)
            temp=7;
        return temp;
    }
}
