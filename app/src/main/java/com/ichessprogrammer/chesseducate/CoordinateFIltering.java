package com.ichessprogrammer.chesseducate;

/**
 * Created by XNOTE on 2016-08-05.
 */
public class CoordinateFIltering {

    String coordinate;
    int x;
    int y;

    public CoordinateFIltering() {

    }

    public CoordinateFIltering(String coordinate) {
        this.coordinate = coordinate;
        x = 0;
        y = 0;
    }

    public CoordinateFIltering(int x, int y) {
        this.x = x;
        this.y = y;
        coordinate = "";
    }

    public int getX() {
        char c = coordinate.charAt(0);

        switch (c) {
            case 'a':
                return 1;
            case 'b':
                return 2;
            case 'c':
                return 3;
            case 'd':
                return 4;
            case 'e':
                return 5;
            case 'f':
                return 6;
            case 'g':
                return 7;
            case 'h':
                return 8;
        }
        return -1;
    }

    public int getY() {
        String c = "" + coordinate.charAt(1);
        try {
            return Integer.parseInt(c);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getY(String s) {
        return Integer.parseInt(s);
    }

    public int getX(String s) {
        char c = s.charAt(0);
        switch (c) {
            case 'a':
                return 1;
            case 'b':
                return 2;
            case 'c':
                return 3;
            case 'd':
                return 4;
            case 'e':
                return 5;
            case 'f':
                return 6;
            case 'g':
                return 7;
            case 'h':
                return 8;
        }
        return -1;
    }

    public String getX(int x) {
        String X = "";

        switch (x) {
            case 1:
                X = "a";
                break;
            case 2:
                X = "b";
                break;
            case 3:
                X = "c";
                break;
            case 4:
                X = "d";
                break;
            case 5:
                X = "e";
                break;
            case 6:
                X = "f";
                break;
            case 7:
                X = "g";
                break;
            case 8:
                X = "h";
        }
        return X;
    }

    public String getY(int y) {
        return "" + y;
    }

    public String getCoordinate(int x, int y) {
        String X = "";

        switch (x) {
            case 1:
                X = "a";
                break;
            case 2:
                X = "b";
                break;
            case 3:
                X = "c";
                break;
            case 4:
                X = "d";
                break;
            case 5:
                X = "e";
                break;
            case 6:
                X = "f";
                break;
            case 7:
                X = "g";
                break;
            case 8:
                X = "h";
        }
        return "" + X + y;
    }


    public String getCoordinate() {

        String X = "";

        switch (x) {
            case 1:
                X = "a";
                break;
            case 2:
                X = "b";
                break;
            case 3:
                X = "c";
                break;
            case 4:
                X = "d";
                break;
            case 5:
                X = "e";
                break;
            case 6:
                X = "f";
                break;
            case 7:
                X = "g";
                break;
            case 8:
                X = "h";
        }
        return "" + X + y;
    }

    public String getCoordinate(int x) {
        String X = "";

        switch (x) {
            case 1:
                X = "a";
                break;
            case 2:
                X = "b";
                break;
            case 3:
                X = "c";
                break;
            case 4:
                X = "d";
                break;
            case 5:
                X = "e";
                break;
            case 6:
                X = "f";
                break;
            case 7:
                X = "g";
                break;
            case 8:
                X = "h";
        }
        return "" + X;
    }
}