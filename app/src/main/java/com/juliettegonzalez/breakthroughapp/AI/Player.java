package com.juliettegonzalez.breakthroughapp.AI;

import java.util.concurrent.ThreadLocalRandom;

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

    public static PawnType getRandomPawn(PawnType playerPawn){
        PawnType ranPawn = null;

        do {
            int rand = ThreadLocalRandom.current().nextInt(0, 4);
            ranPawn = PawnType.values()[rand];
        }while (ranPawn == playerPawn);

        return ranPawn;
    }

}
