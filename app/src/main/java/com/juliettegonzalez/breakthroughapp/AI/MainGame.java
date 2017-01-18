package com.juliettegonzalez.breakthroughapp.AI;

import android.util.Log;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by juliettegonzalez on 18/12/2016.
 */

public class MainGame {

    public static int nb_play_predicted = 5;
    public static int milliseconds_for_calcul = 5000;
    public static boolean nb_or_time = true;


    private Player mPlayer1;
    private Player mComputer;
    private Player mCurrentPlayer;
    private SquareBoard mSelectedSquare;
    private Board mBoard;
    private BMatrix mMatrix;
    public static HashMap<TBMatrix,GameValue> mMap;

    public interface GameStateListener {
        void onGameEnd(Player winner);
    }

    private GameStateListener listener;

    public MainGame(Player player1, Player computer){
        this.mPlayer1 = player1;
        this.mComputer = computer;
        this.mCurrentPlayer = player1;
        this.mSelectedSquare = null;
        this.mBoard = new Board(player1, computer);
        this.mMatrix = new BMatrix();
        this.listener = null;
        //this.mMap = new HashMap<BMatrix,Double>();
    }


    public Player getmCurrentPlayer() {
        return mCurrentPlayer;
    }

    public void setmCurrentPlayer(Player mCurrentPlayer) {
        this.mCurrentPlayer = mCurrentPlayer;
    }

    public Player getmPlayer1() {
        return mPlayer1;
    }

    public Player getmComputer() {
        return mComputer;
    }

    public SquareBoard getmSelectedSquare() {
        return mSelectedSquare;
    }

    public void setmSelectedSquare(SquareBoard mSelectedSquare) {
        this.mSelectedSquare = mSelectedSquare;
    }

    public Board getmBoard() {
        return mBoard;
    }

    public boolean isHumanTurn(){
        return mCurrentPlayer == mPlayer1;
    }

    public void setGameStateListener(GameStateListener listener) {
        this.listener = listener;
    }


    /**
     * Return the possible moves for the selected pawn
     * @return ArrayList<SquareBoard>
     */
    public ArrayList<SquareBoard> getPossibleMoves(){
        ArrayList<SquareBoard> possibleMoves = new ArrayList<>();

        int posI =  mSelectedSquare.getI();
        int posJ = mSelectedSquare.getJ();

        if (mBoard.getSquareAt(posI-1, posJ).isFree())
            possibleMoves.add(mBoard.getSquareAt(posI-1, posJ));

        if (posJ > 0 &&
                (mBoard.getSquareAt(posI-1, posJ-1).isFree() ||
                        mBoard.getSquareAt(posI-1, posJ-1).getOwner() == mComputer)){
            possibleMoves.add(mBoard.getSquareAt(posI-1, posJ-1));
        }


        if (posJ < Board.MAX_LENGHT_BOARD-1 &&
                (mBoard.getSquareAt(posI-1, posJ+1).isFree()||
                mBoard.getSquareAt(posI-1, posJ+1).getOwner() == mComputer)) {
            possibleMoves.add(mBoard.getSquareAt(posI-1, posJ+1));
        }

        return possibleMoves;
    }

    public boolean movePawn(SquareBoard destination){
        if (mSelectedSquare != null){
            ArrayList<SquareBoard> possibleMoves = getPossibleMoves();
            if(possibleMoves.contains(destination)){
                if (destination.getOwner() == mComputer) {
                    // Eating the enemy
                    //Log.d("DEBUG", "Eating computer");
                    mComputer.getPawns().remove(destination);
                    destination.setFree();
                }

                mSelectedSquare.movePawn(destination);

                // Update AI
                int[][] movement = {{mSelectedSquare.getI(), mSelectedSquare.getJ()},
                        {destination.getI(), destination.getJ()}};
                mMatrix.convertMove(movement);

                mSelectedSquare = null;

                //End of player's turn
                if (!isGameWon()) {

                }

                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    public void endTurn(){
        if (mCurrentPlayer == mPlayer1){
            mCurrentPlayer = mComputer;
        }else{
            mCurrentPlayer = mPlayer1;
        }
        mMatrix.changePlayer();
    }


    /**
     * AI PART
     */

    public void aiPlaying(){
        long startTime = System.currentTimeMillis();
        int actualDepth = 0;
        long newTime,duration;
        BNode nextMove;
        BNode bestMove = new BNode(actualDepth,null,null, mMatrix, 0);
        bestMove.value = -1000.0;
        do{
            actualDepth++;
            //Log.d("DEBUG", "Depth increased "+actualDepth);
            BMatrix copy = new BMatrix(mMatrix);
            nextMove = new BNode(actualDepth,null,null,copy, 0);
            nextMove.process();

            newTime = System.currentTimeMillis();
            duration = newTime - startTime;

            if(nextMove.move!=null && nextMove.value!=null && nextMove.value >= bestMove.value){
                bestMove = nextMove;
            }
            System.gc();
            //Log.d("DEBUG","bestMove (white) : "+bestMove.move.getMatrix(true).toString(16));
            //Log.d("DEBUG","bestMove (black) : "+bestMove.move.getMatrix(false).toString(16));
        }while((nb_or_time && actualDepth < nb_play_predicted)||(!nb_or_time && duration < milliseconds_for_calcul));

        int[][] finalMove = bestMove.convert(bestMove.move, mMatrix);
        mMatrix.applyMove(bestMove.move);

        /*
        for(int i = 0; i < mComputer.getNumberPawns();i++){
            int xPawn = mComputer.getPawns().get(i).getI();
            int yPawn = mComputer.getPawns().get(i).getJ();
            Log.d("DEBUG", "AI Pawn (Juliette) "+ i + " : " + xPawn + ", " + yPawn);
        }
        int[][] computerPawns = mMatrix.getPawns(true);
        for(int i = 0; i < mMatrix.getNumberPawns(true);i++){
            Log.d("DEBUG", "AI Pawn (Yvo) "+ i + " : " + computerPawns[i][0] + ", " + computerPawns[i][1]);
        }

        for(int i = 0; i < mPlayer1.getNumberPawns();i++){
            int xPawn = mPlayer1.getPawns().get(i).getI();
            int yPawn = mPlayer1.getPawns().get(i).getJ();
            Log.d("DEBUG", "Player Pawn (Juliette) "+ i + " : " + xPawn + ", " + yPawn);
        }
        int[][] playerPawns = mMatrix.getPawns(false);
        for(int i = 0; i < mMatrix.getNumberPawns(false);i++){
            Log.d("DEBUG", "Player Pawn (Yvo) "+ i + " : " + playerPawns[i][0] + ", " + playerPawns[i][1]);
        }
        */


        int iInit = finalMove[0][0];
        int jInit = finalMove[0][1];
        int iDest = finalMove[1][0];
        int jDest = finalMove[1][1];
        //Log.d("DEBUG", "Départ = ("+ iInit + "," + jInit + ")");
        //Log.d("DEBUG", "Destination = ("+ iDest + "," + jDest + ")");

        SquareBoard destination = mBoard.getSquareAt(iDest, jDest);
        if (destination.getOwner() == mPlayer1) {
            // Eating the enemy
            //Log.d("DEBUG", "Eating player");
            mPlayer1.getPawns().remove(destination);
            destination.setFree();
        }

        mBoard.getSquareAt(iInit, jInit).movePawn(destination);
    }



    public boolean isGameWon(){
        // Check player's victory
        for (int j=0; j<Board.MAX_LENGHT_BOARD; j++){
            if (mBoard.getSquareAt(0, j).getOwner() == mPlayer1) {
                listener.onGameEnd(mPlayer1);
                return true;
            }
        }

        if (mComputer.getPawns().isEmpty()){
            listener.onGameEnd(mPlayer1);
            return true;
        }

        // Check computer's victory
        for (int j=0; j<Board.MAX_LENGHT_BOARD; j++){
            if (mBoard.getSquareAt(Board.MAX_LENGHT_BOARD-1, j).getOwner() == mComputer) {
                listener.onGameEnd(mComputer);
                return true;
            }
        }

        if (mPlayer1.getPawns().isEmpty()){
            listener.onGameEnd(mComputer);
            return true;
        }

        return false;

    }


    /**
     * OLD AI
     */


    public void aiPlays(){
        Log.v("DEBUG", "AI's turn");
        if (anySolutionForAI(mComputer)) {
            int posI, posJ;
            do {
                posI = ThreadLocalRandom.current().nextInt(0, Board.MAX_LENGHT_BOARD);
                posJ = ThreadLocalRandom.current().nextInt(0, Board.MAX_LENGHT_BOARD);
            }
            while (mBoard.getSquareAt(posI,posJ).getOwner() != mComputer || !isPossibleToMoveAI(mBoard.getSquareAt(posI,posJ)));

            if (mBoard.getSquareAt(posI+1, posJ).isFree()) {
                mBoard.getSquareAt(posI,posJ).movePawn(mBoard.getSquareAt(posI+1,posJ));

            } else if (posJ > 0 &&
                    mBoard.getSquareAt(posI+1, posJ-1).isFree()) {
                mBoard.getSquareAt(posI,posJ).movePawn(mBoard.getSquareAt(posI+1, posJ-1));

            } else if (posJ < Board.MAX_LENGHT_BOARD - 1 &&
                    mBoard.getSquareAt(posI+1, posJ+1).isFree()) {
                mBoard.getSquareAt(posI, posJ).movePawn(mBoard.getSquareAt(posI+1, posJ+1));
            }
        }

        if (!isGameWon()) mCurrentPlayer = mPlayer1;

    }




    public boolean anySolutionForAI(Player player){
        for (SquareBoard square : player.getPawns()) {
            if (isPossibleToMoveAI(square)) return true;
        }
        return false;
    }

    public boolean isPossibleToMoveAI(SquareBoard square){

        int posI = square.getI();
        int posJ = square.getJ();

        if (mBoard.getSquareAt(posI+1, posJ).isFree())
            return true;

        if (posJ > 0 &&
                mBoard.getSquareAt(posI+1, posJ-1).isFree())
            return true;

        if (posJ < Board.MAX_LENGHT_BOARD-1 &&
                mBoard.getSquareAt(posI+1, posJ+1).isFree())
            return true;

        return false;
    }




}
