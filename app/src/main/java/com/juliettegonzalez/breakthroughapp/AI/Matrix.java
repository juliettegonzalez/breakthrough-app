package com.juliettegonzalez.breakthroughapp.AI;

/**
 * Created by juliettegonzalez on 03/01/2017.
 */

public class Matrix {

    private int[][] whiteBoard;
    private int[][] blackBoard;
    private boolean currentPlayer;

    public Matrix(){
        int[][] whiteB = {{1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0}};
        int[][] blackB = {{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1}};
        whiteBoard = whiteB;
        blackBoard = blackB;
        currentPlayer = true;
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
        if((move[1][0] < 0)||(8 < move[1][0])){return false;}
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
        int[][] board = getMatrix(currentPlayer);
        board[move[0][0]][move[1][0]] = 0;
        board[move[1][0]][move[1][1]] = 1;
        board = getMatrix(!currentPlayer);
        if(board[move[1][0]][move[1][1]] == 1){
            board[move[1][0]][move[1][1]] = 0;
        }
    }

    /*public Board copy(){
        return(new Board(whiteBoard,blackBoard,currentPlayer));
    }*/

    public int analyze(){
        //TODO : heuristique, appréciation de la position
        //Heuristique actuelle très naive
        if(winningPosition()){
            if(winner()) {
                return(Integer.MAX_VALUE);
            }else {
                return(Integer.MIN_VALUE);
            }
        }else {
            return 0;
        }
    }

    public boolean winningPosition() {
        for(int j = 0; j < 8; j++){
            if((whiteBoard[7][j]==1)||(blackBoard[0][j]==1)) {
                return true;
            }
        }
        return false;
    }

    //on suppose que l'on sait déjà qu'il y a un vainqueur
    public boolean winner() {
        for(int j = 0; j < 8; j++){
            if(whiteBoard[7][j]==1) {
                return true;
            }
        }
        return false;
    }

}
