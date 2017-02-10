package com.juliettegonzalez.breakthroughapp.AI;

/**
 * Created by Yvonnig on 17/01/2017.
 */
<<<<<<< HEAD
=======

>>>>>>> 09c71e9def109e5b73d6d600d7b4a4cf575b916c
public class TBMatrix{

    private long whiteBoard;
    private long blackBoard;
    public static double COLUMN_EMPTY_VALUE = 0.4;

    public TBMatrix(){
        whiteBoard = 0xFFFF000000000000L;
        blackBoard = 0xFFFFL;
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
    }


    public void applyMove(TBMatrix nextMove, boolean player){
        long oBoard = getMatrix(!player);
        long board = nextMove.getMatrix(player);
        setMatrix(player, board);
        if((board & (oBoard))!=0){
            oBoard = (board & (oBoard)) ^ (oBoard);
        }
        setMatrix(!player,oBoard);
    }

    public double analyze(int level){
        Double score = 0.0;
        if(winningPosition()){
            if(winner()) {
                score += 1000.0 - level;
            }else {
                score += -1000.0 + level;
            }
        }else {
            score += getNumberPawns(true);
            score -= getNumberPawns(false);
            //score += 0.1*(Math.random()-0.5)

            score += getNumberPawnsOnRow(true,0)*0.5;
            score += getNumberPawnsOnRow(true,6)*0.5;
            score += getNumberPawnsOnRow(true,5)*0.25;

            for (int j=0; j<8 ; j++){
                // Check empty column (only once)
                if (getNumberPawnsOnColumn(true, j) == 0) score -= COLUMN_EMPTY_VALUE;
                if (getNumberPawnsOnColumn(false, j) == 0) score += COLUMN_EMPTY_VALUE;
            }
        }
        return score;
    }

    public boolean winningPosition() {
        if((whiteBoard & 0xFFL) > 0L || (blackBoard & 0xFF00000000000000L) > 0L){
            return true;
        }
        return(getNumberPawns(true) == 0 || getNumberPawns(false) == 0);

    }

    //on suppose que l'on sait déjà qu'il y a un vainqueur
    public boolean winner() {
        if((whiteBoard & 0xFFL) > 0L){
            return true;
        }else if((blackBoard & 0xFF00000000000000L) > 0L){
            return false;
        }
        return(getNumberPawns(true) == 0);
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

    /**
     * Compute the number of pawns that own the player on a given row
     * @param c
     * @return
     */
    public double getNumberPawnsOnRow(boolean player, int c){
        long matrix = getMatrix(player);
        matrix &= (0xFFL << 8*(7-c));
        return Long.bitCount(matrix);
    }

    public void convertMove(int[][] movement) {
        int indicep, indicen;
        indicep = (7-movement[0][0])*8+(7-movement[0][1]);
        indicen = (7-movement[1][0])*8+(7-movement[1][1]);
        long oBoard = whiteBoard;
        long board = blackBoard & ~(1L << indicep);
        board |= (1L << (long)(indicen));
        blackBoard = board;
        if((board & (oBoard)) != 0){
            oBoard = (board & (oBoard)) ^ (oBoard);
        }
        whiteBoard = oBoard;
    }
}
