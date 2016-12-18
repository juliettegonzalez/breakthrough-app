package com.juliettegonzalez.breakthroughapp.AI;

/**
 * Created by juliettegonzalez on 18/12/2016.
 */

public class MainGame {

    private Player player1;
    private Player computer;
    private Player currentPlayer;
    private Board board;

    public MainGame(Player player1, Player computer){
        this.player1 = player1;
        this.computer = computer;
        this.currentPlayer = player1;
        this.board = new Board(player1, computer);
    }


    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getComputer() {
        return computer;
    }

    public Board getBoard() {
        return board;
    }
}
