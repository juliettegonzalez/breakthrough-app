package com.juliettegonzalez.breakthroughapp.AI;

/**
 * Created by Yvonnig on 14/01/2017.
 */

import android.util.Log;

import java.math.BigInteger;


public class BNode {
    public BNode parent;
    public Double value;
    public int level;
    public BMatrix move;
    public BMatrix matrix;
    public int depth;
    public boolean cut = false;

    public BNode(int depth, BNode parent, BMatrix move, BMatrix matrix, int level){
        this.parent = parent;
        this.move = move;
        this.matrix = matrix;
        this.depth = depth;
        this.level = level;
    }

    public void process(){
        System.gc();
        if (move != null){
            BMatrix mat = new BMatrix(matrix);
            mat.applyMove(move);
            mat.changePlayer();
            depth--;
            matrix = new BMatrix(mat);
            mat=null;
        }


        if (depth > 0){
            int childLevel = level + 1;
            int offsetY = matrix.isComputerAI() ? -1 : 1;

            if(matrix.getNumberPawns(matrix.isComputerAI())==0){
                // Check if computer
                if (matrix.isComputerAI()){
                    value = -1000.0 + level;
                }else{
                    value = 1000.0 - level;
                }
                if (this.parent != null){
                    propagate(this);
                }
                return;
            }
            int pPawns = 0;
            int nPawns = matrix.getNumberPawns(matrix.isComputerAI());
            int off;
            BigInteger playerBoard;
            BMatrix nextMove;
            Boolean found = false;
            BNode node;
            for(int i = 63; 0<=i;i--){
                playerBoard = matrix.getMatrix(matrix.isComputerAI());
                if(playerBoard.testBit(i)){
                    pPawns++;
                    if(cut){break;}
                    else{
                        //Vérification qu'un pion n'a pas atteint la ligne opposée
                        off = i + offsetY * 8;
                        if(off >= 0 && off <= 63){
                            playerBoard = playerBoard.clearBit(i);
                            //Test dépassement à droite et non-présence d'un pion perso
                            if(((((i+offsetY*7)%8)<0 ?(i+offsetY*7)%8 + 8 : (i+offsetY*7)%8)!=((8-offsetY)%9)) && (!playerBoard.testBit(i+offsetY*7))){
                                nextMove = new BMatrix(playerBoard.setBit(i + offsetY * 7), matrix.getMatrix(!matrix.isComputerAI()),matrix.isComputerAI());
                                node = new BNode(depth, this, nextMove, new BMatrix(matrix), childLevel);
                                node.process();
                                nextMove = null;
                                node = null;
                                System.gc();
                                found = true;
                            }
                            //Vérification qu'il n'y a pas d'adversaire en face, ni de pion perso
                            if((!matrix.getMatrix(!matrix.isComputerAI()).testBit(i+offsetY*8)) && (!playerBoard.testBit(i+offsetY*8))){
                                nextMove = new BMatrix(playerBoard.setBit(i + offsetY * 8), matrix.getMatrix(!matrix.isComputerAI()),matrix.isComputerAI());
                                node = new BNode(depth, this, nextMove, new BMatrix(matrix), childLevel);
                                node.process();
                                nextMove = null;
                                node = null;
                                System.gc();
                                found = true;
                            }
                            //Test dépassement à gauche et non-présence d'un pion perso
                            if(((((i+offsetY*9)%8)<0 ?(i+offsetY*9)%8 + 8 : (i+offsetY*9)%8)!=((8+offsetY)%9)) && (!playerBoard.testBit(i+offsetY*9))){
                                nextMove = new BMatrix(playerBoard.setBit(i + offsetY * 9), matrix.getMatrix(!matrix.isComputerAI()),matrix.isComputerAI());
                                node = new BNode(depth, this, nextMove, new BMatrix(matrix), childLevel);
                                node.process();
                                nextMove = null;
                                node = null;
                                System.gc();
                                found = true;
                            }
                        }
                    }
                    if(pPawns == nPawns){cut = true;}
                }
            }

            if (found && this.parent != null){
                propagate(this);
            }

        }else{
            value = matrix.analyze();
            if(!matrix.isComputerAI()){
                value = -value;
            }
            if(value <= -900){
                value += level;
            }
            if(value >= 900){
                value -= level;
            }
            if (parent != null){
                propagate(this);
            }
        }
    }

    public void propagate(BNode node){
        //negamax
        if (node.parent.value == null || ((node.level%2==0) && node.value < node.parent.value) || ((node.level%2==1) && node.value > node.parent.value)){
            node.parent.value = node.value;

            //coupe
            if (node.level > 1 && node.parent.parent.value != null &&
                    ((node.level%2==0 && node.parent.value <= node.parent.parent.value) ||
                            (node.level%2==1 && node.parent.value >= node.parent.parent.value))){
                node.parent.cut = true;
            }

            if (node.level == 1){
                node.parent.move = node.move;
            }
        }
    }

    public static int[][] convert(BMatrix move, BMatrix previous) {
        int[][] res = {{-1,-1},{-1,-1}};
        BigInteger pMatrix = previous.getMatrix(true);
        BigInteger nMatrix = move.getMatrix(true);
        int indice;
        boolean n,p;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                indice = (7-i)*8+(7-j);
                n = nMatrix.testBit(indice);
                p = pMatrix.testBit(indice);
                if(n && !p){
                    res[1][0]=i;
                    res[1][1]=j;
                }else if(p && !n){
                    res[0][0]=i;
                    res[0][1]=j;
                }
            }
        }
        if(res[0][0]==-1 || res[1][0]==-1){
            Log.d("DEBUG","Conversion BMatrix to coordinates failed");

        }
        return res;
    }
}
