package com.juliettegonzalez.breakthroughapp.AI;

/**
 * Created by Yvonnig on 14/01/2017.
 */

        import java.math.BigInteger;

public class BMatrix{


    private BigInteger whiteBoard;
    private BigInteger blackBoard;
    private boolean currentPlayer;

    public BMatrix(){
        whiteBoard = new BigInteger("FFFF000000000000",16);
        blackBoard = new BigInteger("FFFF",16);
        currentPlayer = false;
    }

    //constructeur de recopie
    public BMatrix(BMatrix mat){
        BigInteger whiteB = mat.getMatrix(true);
        BigInteger blackB = mat.getMatrix(false);
        whiteBoard = whiteB;
        blackBoard = blackB;
        if(mat.isComputerAI()){
            currentPlayer = true;
        }else{
            currentPlayer = false;
        }
    }


    public BMatrix(BigInteger board1, BigInteger board2, boolean order){
        if(order){
            whiteBoard = board1;
            blackBoard = board2;
        }else{
            whiteBoard = board2;
            blackBoard = board1;
        }
        currentPlayer = order;
    }


    public BigInteger getMatrix(boolean player){
        if(player){
            return whiteBoard;
        }else{
            return blackBoard;
        }
    }

    public void setMatrix(boolean player,BigInteger b){
        if(player){
            whiteBoard = b;
        }else{
            blackBoard = b;
        }
    }

    public int getNumberPawns(boolean player){
        return getMatrix(player).bitCount();
    }

    public boolean isComputerAI(){
        return currentPlayer;
    }


    public void changePlayer(){
        currentPlayer = !currentPlayer;
    }


    public void applyMove(BMatrix nextMove){
        //Log.d("DEBUG","whiteBoard : " + whiteBoard.toString(16));
        //Log.d("DEBUG","blackBoard : " + blackBoard.toString(16));
        //Log.d("DEBUG","wanted whiteBoard : " + nextMove.whiteBoard.toString(16));
        //Log.d("DEBUG","wanted blackBoard : " + nextMove.blackBoard.toString(16));
        BigInteger oBoard = getMatrix(!currentPlayer);
        BigInteger board = nextMove.getMatrix(nextMove.isComputerAI());
        setMatrix(currentPlayer, board);
        if((board.and(oBoard))!=BigInteger.ZERO){
            oBoard = (board.and(oBoard)).xor(oBoard);
        }
        setMatrix(!currentPlayer,oBoard);
        //Log.d("DEBUG","whiteBoard : " + whiteBoard.toString(16));
        //Log.d("DEBUG","blackBoard : " + blackBoard.toString(16));
    }

    public double analyze(){
        //heuristique, appréciation de la position
        //Heuristique actuelle très naive
        Double score;
        if(winningPosition()){
            if(winner()==currentPlayer) {
                //Log.d("DEBUG", "Computer winning");
                score = 1000.0;
            }else {
                //Log.d("DEBUG", "Player winning");
                score = -1000.0;
            }
        }else {
            score = 0.0;
            score += getNumberPawns(currentPlayer);
            score -= getNumberPawns(!currentPlayer);
        }
        return score;
    }

    public boolean winningPosition() {
        if((whiteBoard.getLowestSetBit()<9) || (blackBoard.compareTo(new BigInteger("FFFFFFFFFFFFFF",16))==1)){
            return true;
        }
        if(getNumberPawns(true) == 0 || getNumberPawns(false) == 0){
            return(true);
        }
        return false;
    }

    //on suppose que l'on sait déjà qu'il y a un vainqueur
    public boolean winner() {
        if(whiteBoard.getLowestSetBit()<9){
            return true;
        }else if((blackBoard.compareTo(new BigInteger("FFFFFFFFFFFFFF",16))==1)){
            return false;
        }
        if(getNumberPawns(true) == 0){
            return false;
        }else{
            return true;
        }
    }

    public void convertMove(int[][] movement) {
        //Log.d("DEBUG","movement : "+ movement[0][0]+", "+movement[0][1]+", "+movement[1][0]+", "+movement[1][1]);
        int indicep, indicen;
        indicep = (7-movement[0][0])*8+(7-movement[0][1]);
        indicen = (7-movement[1][0])*8+(7-movement[1][1]);
        //Log.d("DEBUG","pboard : "+getMatrix(currentPlayer).toString(16));
        //Log.d("DEBUG","poboard : "+getMatrix(!currentPlayer).toString(16));
        BigInteger oBoard = getMatrix(!currentPlayer);
        BigInteger board = getMatrix(currentPlayer).clearBit(indicep);
        board = board.setBit(indicen);
        setMatrix(currentPlayer, board);
        if((board.and(oBoard)) != BigInteger.ZERO){
            oBoard = (board.and(oBoard)).xor(oBoard);
        }
        setMatrix(!currentPlayer,oBoard);
        //Log.d("DEBUG","currentplayer"+currentPlayer);
        //Log.d("DEBUG","nboard : "+getMatrix(currentPlayer).toString(16));
        //Log.d("DEBUG","noboard : "+getMatrix(!currentPlayer).toString(16));
    }
}
