package com.juliettegonzalez.breakthroughapp.AI;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by juliettegonzalez on 18/12/2016.
 */

public class MainGame {
    // 0 for Matrix, 1 for BMatrix, 2 for TBMatrix
    private static int algoType = 2;
    private static int nb_play_predicted = 4;
    private static int milliseconds_for_calcul = 5000;
    private static boolean nb_or_time = true;


    private Player mPlayer1;
    private Player mPlayer2;
    private Player mCurrentPlayer;
    private SquareBoard mSelectedSquare;
    private Board mBoard;

    private TBMatrix tbmMatrix;
    private Matrix mMatrix;
    private BMatrix bmMatrix;
    public static HashMap<TBMatrix,GameValue> mMap;
    public static TBMatrix best = null;
    public static double bestValue = -1000.0;

    public interface GameStateListener {
        void onGameEnd(Player winner);
    }

    private GameStateListener listener;

    public void setGameStateListener(GameStateListener listener) {
        this.listener = listener;
    }

    public MainGame(Player player1, Player player2){
        this.mPlayer1 = player1;
        this.mPlayer2 = player2;
        this.mCurrentPlayer = player1;
        this.mSelectedSquare = null;
        this.mBoard = new Board(player1, player2);
        if(algoType == 2){
            this.tbmMatrix = new TBMatrix();
        }else if(algoType == 1){
            this.bmMatrix = new BMatrix();
        }else{
            this.mMatrix = new Matrix();
        }
        this.listener = null;
    }


    public Player getmCurrentPlayer() {
        return mCurrentPlayer;
    }

    public Player getOpponent() {
        if (mCurrentPlayer == mPlayer1){
            return mPlayer2;
        }else{
            return mPlayer1;
        }
    }

    public void setmCurrentPlayer(Player currentPlayer) {
        this.mCurrentPlayer = currentPlayer;
    }

    public Player getmPlayer1() {
        return mPlayer1;
    }

    public Player getmPlayer2() {
        return mPlayer2;
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

    public boolean isFirstPlayerTurn(){
        return mCurrentPlayer == mPlayer1;
    }


    /**
     * Return the possible moves for the selected pawn
     * @return ArrayList<SquareBoard>
     */
    public ArrayList<SquareBoard> getPossibleMoves(Player player){
        ArrayList<SquareBoard> possibleMoves = new ArrayList<>();
        int offsetI = (player == mPlayer1) ? -1 : 1;

        int posI =  mSelectedSquare.getI();
        int posJ = mSelectedSquare.getJ();

        if (mBoard.getSquareAt(posI+offsetI, posJ).isFree())
            possibleMoves.add(mBoard.getSquareAt(posI+offsetI, posJ));

        if (posJ > 0 &&
                (mBoard.getSquareAt(posI+offsetI, posJ-1).isFree() ||
                        mBoard.getSquareAt(posI+offsetI, posJ-1).getOwner() == getOpponent())){
            possibleMoves.add(mBoard.getSquareAt(posI+offsetI, posJ-1));
        }


        if (posJ < Board.MAX_LENGHT_BOARD-1 &&
                (mBoard.getSquareAt(posI+offsetI, posJ+1).isFree()||
                mBoard.getSquareAt(posI+offsetI, posJ+1).getOwner() == getOpponent())) {
            possibleMoves.add(mBoard.getSquareAt(posI+offsetI, posJ+1));
        }

        return possibleMoves;
    }

    public boolean movePawn(SquareBoard destination){
        if (mSelectedSquare != null){
            ArrayList<SquareBoard> possibleMoves = getPossibleMoves(mCurrentPlayer);
            if(possibleMoves.contains(destination)){
                if (destination.getOwner() == mPlayer2) {
                    mPlayer2.getPawns().remove(destination);
                    destination.setFree();
                }
                mSelectedSquare.movePawn(destination);

                // Update AI
                int[][] movement = {{mSelectedSquare.getI(), mSelectedSquare.getJ()},
                        {destination.getI(), destination.getJ()}};

                if(algoType == 2){
                    tbmMatrix.convertMove(movement);
                }else if(algoType == 1){
                    bmMatrix.convertMove(movement);
                }else{
                    mMatrix.applyMove(movement);
                }

                mSelectedSquare = null;
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
            mCurrentPlayer = mPlayer2;
        }else{
            mCurrentPlayer = mPlayer1;
        }
        if(algoType == 0){
            mMatrix.changePlayer();
        }else if(algoType == 1){
            bmMatrix.changePlayer();
        }
    }


    /**
     * AI PART
     */

    public void aiPlaying(){
        long startTime = System.currentTimeMillis();
        int actualDepth = 1;
        long newTime,duration;
        TBNode tbnextMove;
        BNode bnextMove, bbestMove = null;
        Node nextMove, bestMove = null;
        TBMatrix tbcopy;
        BMatrix bcopy;
        Matrix copy;
        int[][] finalMove;
        if(algoType == 0){
            bestMove = new Node(actualDepth,null,null, mMatrix, 0);
            bestMove.value = -1000.0;
        }else if(algoType == 1){
            bbestMove = new BNode(actualDepth,null,null, bmMatrix, 0);
            bbestMove.value = -1000.0;
        }
        do{
            actualDepth++;
            if(algoType == 2){
                tbcopy = new TBMatrix(tbmMatrix);
                tbnextMove = new TBNode(actualDepth,tbcopy,0,-1000,1000,1);
                mMap = new HashMap<TBMatrix,GameValue>();
                tbnextMove.process();
                finalMove = TBNode.convert(best, tbmMatrix);
            }else if(algoType == 1){
                bcopy = new BMatrix(bmMatrix);
                bnextMove = new BNode(actualDepth,null,null,bcopy,0);
                bnextMove.process();
                if(bnextMove.move!=null && bnextMove.value!=null && bnextMove.value >= bbestMove.value){
                    bbestMove = bnextMove;
                }
                finalMove = BNode.convert(bbestMove.move,bmMatrix);
            }else{
                copy = new Matrix(mMatrix);
                nextMove = new Node(actualDepth,null,null,copy, 0);
                nextMove.process();
                if(nextMove.move!=null && nextMove.value!=null && nextMove.value >= bestMove.value){
                    bestMove = nextMove;
                }
                finalMove = bestMove.move;
            }
            newTime = System.currentTimeMillis();
            duration = newTime - startTime;
            System.gc();
        }while((nb_or_time && actualDepth < nb_play_predicted)||(!nb_or_time && duration < milliseconds_for_calcul));

        if(algoType == 2){
            tbmMatrix = new TBMatrix(best);
        }else if(algoType == 1){
            bmMatrix.applyMove(bbestMove.move);
        }else{
            mMatrix.applyMove(bestMove.move);
        }

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

        if (mPlayer2.getPawns().isEmpty()){
            listener.onGameEnd(mPlayer1);
            return true;
        }

        // Check computer's victory
        for (int j=0; j<Board.MAX_LENGHT_BOARD; j++){
            if (mBoard.getSquareAt(Board.MAX_LENGHT_BOARD-1, j).getOwner() == mPlayer2) {
                listener.onGameEnd(mPlayer2);
                return true;
            }
        }

        if (mPlayer1.getPawns().isEmpty()){
            listener.onGameEnd(mPlayer2);
            return true;
        }

        return false;

    }

}
