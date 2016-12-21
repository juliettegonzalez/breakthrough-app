package com.juliettegonzalez.breakthroughapp.AI;

import java.util.ArrayList;

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

        if (board.getMatrix()[posI-1][posJ].isFree())
            possibleMoves.add(board.getMatrix()[posI-1][posJ]);

        if (posJ > 0 &&
                board.getMatrix()[posI-1][posJ-1].isFree())
            possibleMoves.add(board.getMatrix()[posI-1][posJ-1]);

        if (posJ < Board.MAX_LENGHT_BOARD-1 &&
                board.getMatrix()[posI-1][posJ+1].isFree())
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

        int posI =  mSelectedPawn.getI();
        int posJ = mSelectedPawn.getJ();

        if (board.getMatrix()[posI-1][posJ].isFree())
            possibleMoves.add(board.getMatrix()[posI-1][posJ]);

        if (posJ > 0 &&
                board.getMatrix()[posI-1][posJ-1].isFree())
            possibleMoves.add(board.getMatrix()[posI-1][posJ-1]);

        if (posJ < Board.MAX_LENGHT_BOARD-1 &&
                board.getMatrix()[posI-1][posJ+1].isFree())
            possibleMoves.add(board.getMatrix()[posI-1][posJ+1]);
    }

    //TODO : who's turn

    //TODO : make AI plays

}
