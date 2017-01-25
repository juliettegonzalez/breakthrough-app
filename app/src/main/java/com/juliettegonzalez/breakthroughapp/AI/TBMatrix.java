package com.juliettegonzalez.breakthroughapp.AI;

/**
 * Created by Yvonnig on 17/01/2017.
 */
import android.util.Log;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class TBMatrix{

    private long whiteBoard;
    //private BigInteger whiteBoard;
    private long blackBoard;
    //private BigInteger blackBoard;

    public TBMatrix(){
        whiteBoard = 0xFFFF000000000000L;
        //whiteBoard = new BigInteger("FFFF000000000000",16);
        blackBoard = 0xFFFFL;
        //blackBoard = new BigInteger("FFFF",16);
    }

    //constructeur de recopie
    public TBMatrix(TBMatrix mat){
        long whiteB = mat.getMatrix(true);
        long blackB = mat.getMatrix(false);
        whiteBoard = whiteB;
        blackBoard = blackB;
    }


    public TBMatrix(long board1, long board2, boolean order){
        if(order){
            whiteBoard = board1;
            blackBoard = board2;
        }else{
            whiteBoard = board2;
            blackBoard = board1;
        }
    }


    public long getMatrix(boolean player){
        if(player){
            return whiteBoard;
        }else{
            return blackBoard;
        }
    }

    public void setMatrix(boolean player, long b){
        if(player){
            whiteBoard = b;
        }else{
            blackBoard = b;
        }
    }

    public int getNumberPawns(boolean player){
        return Long.bitCount(getMatrix(player));
        //return getMatrix(player).bitCount();
    }


    public void applyMove(TBMatrix nextMove, boolean player){
        //Log.d("DEBUG","whiteBoard : " + whiteBoard.toString(16));
        //Log.d("DEBUG","blackBoard : " + blackBoard.toString(16));
        //Log.d("DEBUG","wanted whiteBoard : " + nextMove.whiteBoard.toString(16));
        //Log.d("DEBUG","wanted blackBoard : " + nextMove.blackBoard.toString(16));
        long oBoard = getMatrix(!player);
        long board = nextMove.getMatrix(player);
        setMatrix(player, board);
        if((board & (oBoard))!=0){
            oBoard = (board & (oBoard)) ^ (oBoard);
        }
        setMatrix(!player,oBoard);
        //Log.d("DEBUG","whiteBoard : " + whiteBoard.toString(16));
        //Log.d("DEBUG","blackBoard : " + blackBoard.toString(16));
    }

    public double analyze(int level){
        //heuristique, appréciation de la position
        //Heuristique actuelle très naive
        Double score;
        if(winningPosition()){
            if(winner()==true) {
                //Log.d("DEBUG", "Computer winning");
                score = 1000.0 - level;
            }else {
                //Log.d("DEBUG", "Player winning");
                score = -1000.0 + level;
            }
        }else {
            score = 0.0;
            score += getNumberPawns(true);
            score -= getNumberPawns(false);
            //score += Math.random()-0.5
            if(0 < score){
                //Log.d("DEBUG","Computer stronger");
            }else if(score < 0){
                //Log.d("DEBUG","Player stronger");
            }
        }
        return score;
    }

    public boolean winningPosition() {
        if((whiteBoard & 0xFFL) > 0L || (blackBoard & 0xFF00000000000000L) > 0L){
        //if((whiteBoard.getLowestSetBit()<9) || (blackBoard.compareTo(new BigInteger("FFFFFFFFFFFFFF",16))==1)){
            return true;
        }
        if(getNumberPawns(true) == 0 || getNumberPawns(false) == 0){
            return(true);
        }
        return false;
    }

    //on suppose que l'on sait déjà qu'il y a un vainqueur
    public boolean winner() {

        if((whiteBoard & 0xFFL) > 0L){
            return true;
        }else if((blackBoard & 0xFF00000000000000L) > 0L){
            return false;
        }
        if(getNumberPawns(true) == 0){
            return false;
        }else{
            return true;
        }
    }

    /**
     * Compute the number of pawns that own the player on a given column
     * @param c
     * @return
     */
    public double getNumberPawnsOnColumn(boolean player, int c){
        double nbPawn = 0.0;
        for(int j = 0; j < 63; j ++){
            if(((getMatrix(player) >>> (long)j) & 1L) == 1L){
                if(j%8 == 7-c){
                    nbPawn++;
                }
            }
        }
        return nbPawn;
    }







    public void convertMove(int[][] movement) {
        //Log.d("DEBUG","movement : "+ movement[0][0]+", "+movement[0][1]+", "+movement[1][0]+", "+movement[1][1]);
        int indicep, indicen;
        indicep = (7-movement[0][0])*8+(7-movement[0][1]);
        indicen = (7-movement[1][0])*8+(7-movement[1][1]);
        //Log.d("DEBUG","pboard : "+getMatrix(currentPlayer).toString(16));
        //Log.d("DEBUG","poboard : "+getMatrix(!currentPlayer).toString(16));
        long oBoard = whiteBoard;

        //playerBoard &= ~(1L << i);
        long board = blackBoard & ~(1L << indicep);
        //long board = blackBoard.clearBit(indicep);
        board |= (1L << (long)(indicen));
        //board = board.setBit(indicen);
        blackBoard = board;
        if((board & (oBoard)) != 0){
            oBoard = (board & (oBoard)) ^ (oBoard);
        }
        whiteBoard = oBoard;
        //Log.d("DEBUG","currentplayer"+currentPlayer);
        //Log.d("DEBUG","nboard : "+getMatrix(currentPlayer).toString(16));
        //Log.d("DEBUG","noboard : "+getMatrix(!currentPlayer).toString(16));
    }
}
