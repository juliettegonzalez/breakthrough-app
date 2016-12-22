package com.juliettegonzalez.breakthroughapp.AI;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by juliettegonzalez on 18/12/2016.
 */

public class MainGame {

    private Player mPlayer1;
    private Player mComputer;
    private Player mCurrentPlayer;
    private SquareBoard mSelectedPawn;
    private Board mBoard;

    public interface GameStateListener {
        public void onGameEnd(Player winner);
    }

    private GameStateListener listener;

    public MainGame(Player player1, Player computer){
        this.mPlayer1 = player1;
        this.mComputer = computer;
        this.mCurrentPlayer = player1;
        this.mSelectedPawn = null;
        this.mBoard = new Board(player1, computer);
        this.listener = null;
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

    public SquareBoard getmSelectedPawn() {
        return mSelectedPawn;
    }

    public void setmSelectedPawn(SquareBoard mSelectedPawn) {
        this.mSelectedPawn = mSelectedPawn;
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

        int posI =  mSelectedPawn.getI();
        int posJ = mSelectedPawn.getJ();

        //TODO : pb a l'élimination du dernier pion adverse

        if (mBoard.getMatrix()[posI-1][posJ].isFree())
            possibleMoves.add(mBoard.getMatrix()[posI-1][posJ]);

        if (posJ > 0 &&
                (mBoard.getMatrix()[posI-1][posJ-1].isFree() ||
                        mBoard.getMatrix()[posI-1][posJ-1].getOwner() == mComputer))
            possibleMoves.add(mBoard.getMatrix()[posI-1][posJ-1]);

        if (posJ < Board.MAX_LENGHT_BOARD-1 &&
                (mBoard.getMatrix()[posI-1][posJ+1].isFree()||
                mBoard.getMatrix()[posI-1][posJ+1].getOwner() == mComputer))
            possibleMoves.add(mBoard.getMatrix()[posI-1][posJ+1]);

        return possibleMoves;
    }

    public boolean movePawn(SquareBoard destination){
        if (mSelectedPawn != null){
            ArrayList<SquareBoard> possibleMoves = getPossibleMoves();
            if(possibleMoves.contains(destination)){
                destination.setOwner(mCurrentPlayer);
                mSelectedPawn.setFree();
                mSelectedPawn = null;
                mCurrentPlayer = mComputer;

                //Make the computer play !
                //TODO : remplacer par réelle intelligence
                aiPlays();


                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }


    public void aiPlays(){
        ArrayList<SquareBoard> possibleMoves = new ArrayList<>();

        int posI, posJ;
        do {
            posI =  ThreadLocalRandom.current().nextInt(0, Board.MAX_LENGHT_BOARD);
            posJ = ThreadLocalRandom.current().nextInt(0, Board.MAX_LENGHT_BOARD);
        }while (mBoard.getMatrix()[posI][posJ].getOwner() != mComputer || !isPossibleToMove(mBoard.getMatrix()[posI][posJ]));

        if (mBoard.getMatrix()[posI+1][posJ].isFree()){
            mBoard.getMatrix()[posI+1][posJ].setOwner(mComputer);
            mBoard.getMatrix()[posI][posJ].setFree();
        }else if (posJ > 0 &&
                mBoard.getMatrix()[posI+1][posJ-1].isFree()){
            mBoard.getMatrix()[posI+1][posJ-1].setOwner(mComputer);
            mBoard.getMatrix()[posI][posJ].setFree();
        }else if (posJ < Board.MAX_LENGHT_BOARD-1 &&
                mBoard.getMatrix()[posI+1][posJ+1].isFree()){
            mBoard.getMatrix()[posI+1][posJ+1].setOwner(mComputer);
            mBoard.getMatrix()[posI][posJ].setFree();
        }

        if (!isGameWon()) mCurrentPlayer = mPlayer1;

    }

    public boolean isPossibleToMove(SquareBoard square){

        int posI = square.getI();
        int posJ = square.getJ();

        if (mBoard.getMatrix()[posI+1][posJ].isFree())
            return true;

        if (posJ > 0 &&
                mBoard.getMatrix()[posI+1][posJ-1].isFree())
            return true;

        if (posJ < Board.MAX_LENGHT_BOARD-1 &&
                mBoard.getMatrix()[posI+1][posJ+1].isFree())
            return true;

        return false;
    }


    public boolean isGameWon(){
        // Check player's victory
        for (int j=0; j<Board.MAX_LENGHT_BOARD; j++){
            if (mBoard.getMatrix()[0][j].getOwner() == mPlayer1) {
                listener.onGameEnd(mPlayer1);
                return true;
            }
        }

        // Check computer's victory
        for (int j=0; j<Board.MAX_LENGHT_BOARD; j++){
            if (mBoard.getMatrix()[Board.MAX_LENGHT_BOARD-1][j].getOwner() == mComputer) {
                listener.onGameEnd(mComputer);
                return true;
            }
        }

        return false;

    }

}
