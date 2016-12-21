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
    private Board board;

    public MainGame(Player player1, Player computer){
        this.mPlayer1 = player1;
        this.mComputer = computer;
        this.mCurrentPlayer = player1;
        this.mSelectedPawn = null;
        this.board = new Board(player1, computer);
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

    public Board getBoard() {
        return board;
    }

    public boolean isHumanTurn(){
        return mCurrentPlayer == mPlayer1;
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

        if (board.getMatrix()[posI-1][posJ].isFree())
            possibleMoves.add(board.getMatrix()[posI-1][posJ]);

        if (posJ > 0 &&
                (board.getMatrix()[posI-1][posJ-1].isFree() ||
                        board.getMatrix()[posI-1][posJ-1].getOwner() == mComputer))
            possibleMoves.add(board.getMatrix()[posI-1][posJ-1]);

        if (posJ < Board.MAX_LENGHT_BOARD-1 &&
                (board.getMatrix()[posI-1][posJ+1].isFree()||
                board.getMatrix()[posI-1][posJ+1].getOwner() == mComputer))
            possibleMoves.add(board.getMatrix()[posI-1][posJ+1]);

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
        }while (board.getMatrix()[posI][posJ].getOwner() != mComputer || !isPossibleToMove(board.getMatrix()[posI][posJ]));

        if (board.getMatrix()[posI+1][posJ].isFree()){
            board.getMatrix()[posI+1][posJ].setOwner(mComputer);
            board.getMatrix()[posI][posJ].setFree();
        }else if (posJ > 0 &&
                board.getMatrix()[posI+1][posJ-1].isFree()){
            board.getMatrix()[posI+1][posJ-1].setOwner(mComputer);
            board.getMatrix()[posI][posJ].setFree();
        }else if (posJ < Board.MAX_LENGHT_BOARD-1 &&
                board.getMatrix()[posI+1][posJ+1].isFree()){
            board.getMatrix()[posI+1][posJ+1].setOwner(mComputer);
            board.getMatrix()[posI][posJ].setFree();
        }

        mCurrentPlayer = mPlayer1;

    }

    public boolean isPossibleToMove(SquareBoard square){

        int posI = square.getI();
        int posJ = square.getJ();

        if (board.getMatrix()[posI+1][posJ].isFree())
            return true;

        if (posJ > 0 &&
                board.getMatrix()[posI+1][posJ-1].isFree())
            return true;

        if (posJ < Board.MAX_LENGHT_BOARD-1 &&
                board.getMatrix()[posI+1][posJ+1].isFree())
            return true;

        return false;
    }

    //TODO : who's turn

    //TODO : make AI plays

}
