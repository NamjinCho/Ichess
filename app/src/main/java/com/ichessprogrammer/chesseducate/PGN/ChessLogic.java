package com.ichessprogrammer.chesseducate.PGN;

/**
 * Created by XNOTE on 2016-08-12.
 */
public class ChessLogic {


    static public boolean hasNotationNumber(char c) {
        switch (c) { //숫자가 아니면 false, 숫자면 true
            case '1':
                return true;
            case '2':
                return true;
            case '3':
                return true;
            case '4':
                return true;
            case '5':
                return true;
            case '6':
                return true;
            case '7':
                return true;
            case '8':
                return true;
            case '9':
                return true;
        }
        return  false;
    }
    static public boolean hasChessCode(char c) {
        switch (c) { //체스코드 기본적인 것들
            case 'O' :
                return true;
            case 'x':
                return true;
            case 'P':
                return true;
            case 'K':
                return true;
            case 'Q':
                return true;
            case 'N':
                return true;
            case 'B':
                return true;
            case 'R':
                return true;
            case 'a':
                return true;
            case 'b':
                return true;
            case 'c':
                return true;
            case 'd':
                return true;
            case 'e':
                return true;
            case 'f':
                return true;
            case 'g':
                return true;
            case 'h':
                return true;
        }
        return false;
    }

}
