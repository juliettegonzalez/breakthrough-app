package com.juliettegonzalez.breakthroughapp.AI;

/**
 * Created by juliettegonzalez on 18/12/2016.
 */

public class Player {

    public enum PawnType {
        DRAGON, GRANDPA, KING, WIZARD
    }

    public enum Color {
        BLACK, WHITE
    }


    private boolean computer;
    private Color color;
    private PawnType pawnType;

    public Player (boolean _computer, Color _color, PawnType _pawnType){
        this.computer = _computer;
        this.color = _color;
        this.pawnType = _pawnType;
    }


    public boolean isComputer() {
        return computer;
    }

    public Color getColor() {
        return color;
    }

    public PawnType getPawnType() {
        return pawnType;
    }
}
