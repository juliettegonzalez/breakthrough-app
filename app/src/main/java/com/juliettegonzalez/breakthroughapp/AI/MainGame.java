package com.juliettegonzalez.breakthroughapp.AI;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by juliettegonzalez on 18/12/2016.
 */

public class MainGame {

    public static int nb_play_predicted = 7;
    public static int milliseconds_for_calcul = 5000;
    public static boolean nb_or_time = true;

    private Player mPlayer1;
    private Player mComputer;
    private Player mCurrentPlayer;
    private SquareBoard mSelectedSquare;
    private Board mBoard;

    private TBMatrix mMatrix;
    //private Matrix mMatrix;
    public static HashMap<TBMatrix,GameValue> mMap;
    public static TBMatrix best = null;
    public static double bestValue = -1000.0;

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
        this.mMatrix = new TBMatrix();
        //this.mMatrix = new Matrix();
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
                //mMatrix.applyMove(movement);

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
        //mMatrix.changePlayer();
    }


    /**
     * AI PART
     */

    public void aiPlaying(){
        long startTime = System.currentTimeMillis();
        int actualDepth = 1;
        long newTime,duration;
        TBNode nextMove;
        int[][] finalMove;
        //BNode bestMove = new BNode(actualDepth,null,null, mMatrix, 0);

        //Node nextMove;
        //Node bestMove = new Node(actualDepth,null,null, mMatrix, 0);

        //bestMove.value = -1000.0;
        do{
            actualDepth++;

            TBMatrix copy = new TBMatrix(mMatrix);
            nextMove = new TBNode(actualDepth,copy,0,-1000,1000,1);

            //Matrix copy = new Matrix(mMatrix);
            //nextMove = new Node(actualDepth,null,null,copy, 0);
            mMap = new HashMap<TBMatrix,GameValue>();
            double value = nextMove.process();


            newTime = System.currentTimeMillis();
            duration = newTime - startTime;

            /*if(nextMove.move!=null && nextMove.value!=null && nextMove.value >= bestMove.value){
                bestMove = nextMove;
            }*/

            finalMove = TBNode.convert(best, mMatrix);
            System.gc();

        }while((nb_or_time && actualDepth < nb_play_predicted)||(!nb_or_time && duration < milliseconds_for_calcul));


        //int[][] finalMove = bestMove.move;
        //mMatrix.applyMove(bestMove.move);
        mMatrix = new TBMatrix(best);
        best = null;
        bestValue = -1000;

        int iInit = finalMove[0][0];
        int jInit = finalMove[0][1];
        int iDest = finalMove[1][0];
        int jDest = finalMove[1][1];

        SquareBoard destination = mBoard.getSquareAt(iDest, jDest);
        if (destination.getOwner() == mPlayer1) {
            // Eating the enemy
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

}
