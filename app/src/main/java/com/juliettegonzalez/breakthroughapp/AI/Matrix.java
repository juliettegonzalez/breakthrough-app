package com.juliettegonzalez.breakthroughapp.AI;

/**
 * Created by juliettegonzalez on 03/01/2017.
 */

public class Matrix{

    private int[][] whiteBoard;
    private int[][] blackBoard;
    private boolean currentPlayer;

    private final double COLUMN_EMPTY_VALUE = 10.0;

    public Matrix(){
        int[][] whiteB = {{1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0}};
        int[][] blackB = {{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1}};
        whiteBoard = whiteB;
        blackBoard = blackB;
        currentPlayer = false;
    }

    //constructeur de recopie
    public Matrix(Matrix mat){
        int[][] whiteB = new int[8][8];
        int[][] blackB = new int[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                whiteB[i][j] = mat.getMatrix(true)[i][j];
                blackB[i][j] = mat.getMatrix(false)[i][j];
            }
        }
        whiteBoard = whiteB;
        blackBoard = blackB;
        if(mat.isComputerAI()){
            currentPlayer = true;
        }else{
            currentPlayer = false;
        }
    }


    public int[][] getMatrix(boolean player){
        if(player){
            return whiteBoard;
        }else{
            return blackBoard;
        }
    }

    public int getNumberPawns(boolean player){
        int[][] board = getMatrix(player);
        int result = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(board[i][j]==1){
                    result++;
                }
            }
        }
        return result;
    }

    public int[][] getPawns(boolean player){
        int pawns = getNumberPawns(player);
        int[][] board = getMatrix(player);
        int[][] result = new int[pawns][2];
        int index = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(board[i][j]==1){
                    result[index][0]=i;
                    result[index][1]=j;
                    index++;
                }
            }
        }
        return result;
    }


    public boolean isComputerAI(){
        return currentPlayer;
    }


    public void changePlayer(){
        currentPlayer = !currentPlayer;
    }


    public boolean validMove(int[][] move, boolean straight){
        //débordement en Y
        if((move[1][0] < 0)||(7 < move[1][0])){return false;}
        //pion personnel déjà présent
        int[][] board = getMatrix(currentPlayer);
        if(board[move[1][0]][move[1][1]] == 1){return false;}
        //si tout droit, pas de pion adverse
        if(straight){
            board = getMatrix(!currentPlayer);
            if(board[move[1][0]][move[1][1]] == 1){return false;}
        }
        return true;
    }

    public void applyMove(int[][] move){
        if(move==null){
            return;
        }
        int[][] board = getMatrix(currentPlayer);
        board[move[0][0]][move[0][1]] = 0;
        board[move[1][0]][move[1][1]] = 1;
        board = getMatrix(!currentPlayer);
        if(board[move[1][0]][move[1][1]] == 1){
            board[move[1][0]][move[1][1]] = 0;
        }
    }

    public double analyze(){
        //TODO : heuristique, appréciation de la position
        //Heuristique actuelle très naive
        double score = 0.0;

        /*********************/
        /* Trying to improve */
        /*********************/

        /*for (int j=0; j<8 ; j++){
            // Check empty column (only once)
            if (getNumberPawnsOnColumn(currentPlayer, j) == 0) score -= COLUMN_EMPTY_VALUE;
        }
        score = score + getBoardValue(currentPlayer);
        score = score - getBoardValue(!currentPlayer);*/


        // Winning position
        if(winningPosition()) {
            if (winner() == currentPlayer) {
                score += 1000.0;
            } else {
                score -= 1000.0;
            }
        }

        score += getNumberPawns(currentPlayer);
        score -= getNumberPawns(!currentPlayer);

        return score;
    }

    public boolean winningPosition() {
        for(int j = 0; j < 8; j++){
            if((whiteBoard[7][j]==1)||(blackBoard[0][j]==1)) {
                return true;
            }
        }
        if(getNumberPawns(true) == 0 || getNumberPawns(false) == 0){
            return(true);
        }
        return false;
    }

    //on suppose que l'on sait déjà qu'il y a un vainqueur
    public boolean winner() {
        for(int j = 0; j < 8; j++){
            if(whiteBoard[7][j]==1) {
                return true;
            }else if(blackBoard[0][j]==1){
                return false;
            }
        }
        if(getNumberPawns(true) == 0){
            return false;
        }else{
            return true;
        }
    }

    /*******************************************************/
    /*              FEATURES IMPLEMENTATION                */
    /*******************************************************/

    /**
     * Compute the number of pawns that own the player on a given column
     * @param currentPlayer
     * @param c
     * @return
     */
    public double getNumberPawnsOnColumn(boolean currentPlayer, int c){
        double nbPawn = 0.0;
        for (int i=0; i<8; i++){
            if (getMatrix(currentPlayer)[i][c] == 1) nbPawn = nbPawn+1;
        }
        return nbPawn;
    }


    public double getBoardValue(boolean player){
        int[][] board = getMatrix(player);
        double result = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(board[i][j]==1){
                    if ((player && (i == 0 || i == 6)) ||
                            (!player && (i == 7 || i == 1))) result = result + 0.5;
                    result = result + 1;
                }
            }
        }
        return result;
    }



    // Ce truc est trop long

    /**
     * Return a value depending on its position on the board and mouvement possibilities
     * @param i
     * @param j
     * @param player
     * @return double value
     */
    public double getPawnValue(int i, int j, boolean player){
        double value = 0.0;
        if (player){
            // WHITE
            if (i == 0 || i == 6) {
                value += 1.5;
            }else {
                value += 1;
            }

            // Add point depending on the liberty degree
            if (i<8 && j>0 && getMatrix(player)[i+1][j-1] == 0) value += 0.5;
            if (i<8 && getMatrix(player)[i+1][j] == 0 && getMatrix(!player)[i+1][j] == 0) value += 0.5;
            if (i<8 && j<7 && getMatrix(player)[i+1][j+1] == 0) value += 0.5;
        }else{
            // BLACK
            if (i == 7 || i == 1) {
                value += 1.5;
            }else {
                value += 1;
            }

            // Add point depending on the liberty degree
            if (i>0 && j>7 && getMatrix(!player)[i-1][j+1] == 0) value += 0.5;
            if (i>0 && getMatrix(!player)[i-1][j] == 0 && getMatrix(player)[i-1][j] == 0) value += 0.5;
            if (i>0 && j>0 && getMatrix(!player)[i-1][j-1] == 0) value += 0.5;
        }

        return value;
    }

}
