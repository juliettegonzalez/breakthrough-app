package com.juliettegonzalez.breakthroughapp.AI;

/**
 * Created by juliettegonzalez on 18/12/2016.
 */

public class SquareBoard {
    private int i,j;
    private Player owner;

    public SquareBoard(int i, int j, Player p){
        this.i = i;
        this.j = j;
        this.owner = p;
    }


    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public Player getOwner(){
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public boolean isFree(){
        return this.owner == null;
    }

    public void setFree(){
        this.owner.getPawns().remove(this);
        this.owner = null;
    }

    public void movePawn(SquareBoard destination){
        destination.setOwner(this.owner);
        this.owner.getPawns().add(destination);
        this.setFree();

    }

}
