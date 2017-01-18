package com.juliettegonzalez.breakthroughapp.AI;

/**
 * Created by Yvonnig on 18/01/2017.
 */

public class GameValue {
    public String type;
    public double value;
    public int depth;

    public GameValue(String type, double value, int depth) {
        this.type = type;
        this.value = value;
        this.depth = depth;
    }
}
