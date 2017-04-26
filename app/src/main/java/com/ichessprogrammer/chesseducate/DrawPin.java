package com.ichessprogrammer.chesseducate;



/**
 * Created by XNOTE on 2016-08-22.
 */
public class DrawPin {
    /*
    private final int WHITE_PAW = R.drawable.pawn_w25;  private final int BLACK_PAW = R.drawable.pawn_b25;
    private final int WHITE_ROOK = R.drawable.rook_w25;  private final int BLACK_ROOK = R.drawable.rook_b25;
    private final int WHITE_KNIGHT = R.drawable.knight_w25;  private final int BLACK_KNIGHT =  R.drawable.knight_b25;
    private final int WHITE_BISHOP = R.drawable.bishop_w25;  private final int BLACK_BISHOP = R.drawable.bishop_b25;
    private final int WHITE_QUEEN = R.drawable.queen_w25;  private final int BLACK_QUEEN =  R.drawable.queen_b25;
    private final int WHITE_KING = R.drawable.king_w25;  private final int BLACK_KING = R.drawable.king_b25;
    */

    
    public int findPin(String type, boolean WB) {

        int id = -1;

        switch (type.charAt(0)) {
            case 'P' : id = 0;
                break;
            case 'R' : id = 1;
                break;
            case 'B' : id = 2;
                break;
            case 'N' : id = 3;
                break;
            case 'Q' : id = 4;
                break;
            case 'K' : id = 5;
                break;
        }

        if(WB)
            id = id +6;



        return id;
    }
}
