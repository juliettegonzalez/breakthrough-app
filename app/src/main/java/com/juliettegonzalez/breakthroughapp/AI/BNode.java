package com.juliettegonzalez.breakthroughapp.AI;

/**
 * Created by Yvonnig on 14/01/2017.
 */

import android.util.Log;

import java.math.BigInteger;
import java.util.Arrays;


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

        if (move != null){
            //Log.d("DEBUG", "Maj move, depth left : "+(depth-1));
            BMatrix mat = new BMatrix(matrix);
            //Log.d("DEBUG","tab matrix : "+Arrays.deepToString(matrix.getMatrix(true))+", bitboard : "+matrix.toBitboard(matrix.getMatrix(true)));
            mat.applyMove(move);
            mat.changePlayer();
            depth--;
            matrix = new BMatrix(mat);
        }


        if (depth > 0){
            //Log.d("DEBUG", "Higher depths, level : " + (level+1));
            int childLevel = level + 1;
            int offsetY = matrix.isComputerAI() ? -1 : 1;

            if(matrix.getNumberPawns(matrix.isComputerAI())==0){
                // Check if computer
                if (matrix.isComputerAI()){
                    value = -1000.0;
                }else{
                    value = 1000.0;
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
            BNode node = null;
            for(int i = 0; i<64;i++){
                playerBoard = matrix.getMatrix(matrix.isComputerAI());
                //Log.d("DEBUG","playerBoard : "+playerBoard.toString(16));
                //Log.d("DEBUG","testBit : "+(i)+", res : "+playerBoard.testBit(i));
                if(playerBoard.testBit(i)){
                    pPawns++;
                    if(cut){break;}
                    else{
                        //Vérification qu'un pion n'a pas atteint la ligne opposée
                        //Log.d("DEBUG","i : "+i);
                        off = i + offsetY * 8;
                        //Log.d("DEBUG","off : "+off);
                        if(off >= 0 && off < 64){
                            //Log.d("DEBUG","i : "+i);
                            //Log.d("DEBUG","playerBoard : "+playerBoard.toString(16));
                            playerBoard = playerBoard.clearBit(i);

                            //Log.d("DEBUG","playerBoard : "+playerBoard.toString(16));
                            //Log.d("DEBUG","playerBoard.setBit(i + 16) : " + playerBoard.setBit(i+16).toString(16));
                            //Log.d("DEBUG","playerBoard.setBit(i + 17) : " + playerBoard.setBit(i+17).toString(16));

                            //Log.d("DEBUG","testBit : "+(1+i+offsetY*7));
                            //Log.d("DEBUG","res : "+playerBoard.testBit(1+i+offsetY*7));
                            //Log.d("DEBUG","currentPlayer : "+matrix.isComputerAI());
                            //Log.d("DEBUG","Tests pions persos (7): " + playerBoard.testBit(i+offsetY*7));
                            //Log.d("DEBUG","Tests pions persos (8): " + playerBoard.testBit(i+offsetY*8));
                            //Log.d("DEBUG","Tests pions persos (9): " + playerBoard.testBit(i+offsetY*9));
                            //Test dépassement à droite et non-présence d'un pion perso
                            if(((((i+offsetY*7)%8)<0 ?(i+offsetY*7)%8 + 8 : (i+offsetY*7)%8)!=((8-offsetY)%9)) && (!playerBoard.testBit(i+offsetY*7))){
                                //Log.d("DEBUG","Move7 : "+(i + offsetY * 7));
                                nextMove = new BMatrix(playerBoard.setBit(i + offsetY * 7), matrix.getMatrix(!matrix.isComputerAI()),matrix.isComputerAI());
                                node = new BNode(depth, this, nextMove, new BMatrix(matrix), childLevel);
                                node.process();
                            }
                            //Vérification qu'il n'y a pas d'adversaire en face, ni de pion perso
                            if((!matrix.getMatrix(!matrix.isComputerAI()).testBit(i+offsetY*8)) && (!playerBoard.testBit(i+offsetY*8))){
                                //Log.d("DEBUG","Move8 : "+(i + offsetY * 8));
                                nextMove = new BMatrix(playerBoard.setBit(i + offsetY * 8), matrix.getMatrix(!matrix.isComputerAI()),matrix.isComputerAI());
                                node = new BNode(depth, this, nextMove, new BMatrix(matrix), childLevel);
                                node.process();
                            }

                            //Log.d("DEBUG","modulo "+((i+offsetY*9)%8));
                            //Test dépassement à gauche et non-présence d'un pion perso
                            if(((((i+offsetY*9)%8)<0 ?(i+offsetY*9)%8 + 8 : (i+offsetY*9)%8)!=((8+offsetY)%9)) && (!playerBoard.testBit(i+offsetY*9))){
                                //Log.d("DEBUG","Move9 : "+(i + offsetY * 9));
                                nextMove = new BMatrix(playerBoard.setBit(i + offsetY * 9), matrix.getMatrix(!matrix.isComputerAI()),matrix.isComputerAI());
                                node = new BNode(depth, this, nextMove, new BMatrix(matrix), childLevel);
                                node.process();
                            }
                        }
                    }
                    if(pPawns == nPawns){cut = true;}
                }
            }

            if (node != null && this.parent != null){
                propagate(this);
            }

        }else{
            value = matrix.analyze();
            if(!matrix.isComputerAI()){
                value = -value;
            }
            if(value == -1000){
                value = -1000.0 + level;
                //Log.d("DEBUG", "Player winning");
            }
            if(value == 1000){
                value = 1000.0 - level;
                /*Log.d("DEBUG", "Computer winning");
                Log.d("DEBUG", "value : " + (value));
                Log.d("DEBUG", "parent value : " + (parent.value));
                Log.d("DEBUG", "level : " + (level));*/
            }
           /* String actualPlayer;
            if(matrix.isComputerAI()){
                actualPlayer = "computer";
            }else{
                actualPlayer = "human";
            }
            Log.d("DEBUG", "Analyze "+actualPlayer+" level "+level+", Move : "+ Arrays.deepToString(move) +", score : "+ value);
            */
            if (parent != null){
                propagate(this);
            }
        }
    }

    public void propagate(BNode node){
        //negamax
        //if (node.parent.value == null || ((node.level%2==0) && node.value < node.parent.value) || ((node.level%2==1) && node.value > node.parent.value) || ((node.value.equals(node.parent.value)) && (Math.random() < 0.5))){
        if (node.parent.value == null || ((node.level%2==0) && node.value < node.parent.value) || ((node.level%2==1) && node.value > node.parent.value)){
            /*String actualPlayer;
            if(node.matrix.isComputerAI()){
                actualPlayer = "computer";
            }else{
                actualPlayer = "human";
            }
            if(node.parent.value == null){
                actualPlayer+=" new value";
            }else{
                actualPlayer+=", old value : "+node.parent.value;
            }*/
            node.parent.value = node.value;

            //coupe
            if (node.level > 1 && node.parent.parent.value != null &&
                    ((node.level%2==0 && node.parent.value <= node.parent.parent.value) ||
                            (node.level%2==1 && node.parent.value >= node.parent.parent.value))){
                node.parent.cut = true;
                //Log.d("DEBUG","same ? "+ ((node.level%2==0)&&(node.matrix.isComputerAI())));
                //Log.d("DEBUG", "Propagate Cut, player : "+ actualPlayer +", value : "+ node.value +", grand-parent value : "+ node.parent.parent.value);
            }

            if (node.level == 1){
                node.parent.move = node.move;
            }
        }
    }

    public int[][] convert(BMatrix move, BMatrix previous) {
        //Log.d("DEBUG","previous black : "+previous.getMatrix(false).toString(16));
        //Log.d("DEBUG","previous white : "+previous.getMatrix(true).toString(16));
        //Log.d("DEBUG","wanted black : "+move.getMatrix(false).toString(16));
        //Log.d("DEBUG","wanted white : "+move.getMatrix(true).toString(16));
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
                //Log.d("DEBUG","indice : "+indice+", n : "+n+", p : "+p);
                //Log.d("DEBUG","n : "+nMatrix.testBit(47));
                if(n && !p){
                    res[1][0]=i;
                    res[1][1]=j;
                    //Log.d("DEBUG","!p & n : "+indice);
                }else if(p && !n){
                    res[0][0]=i;
                    res[0][1]=j;
                    //Log.d("DEBUG","p & !n : "+indice);
                }
            }
        }
        if(res[0][0]==-1 || res[1][0]==-1){
            Log.d("DEBUG","Conversion BMatrix to coordinates failed");
            //Log.d("DEBUG","pMatrix : "+pMatrix.toString(2));
            //Log.d("DEBUG","nMatrix : "+nMatrix.toString(2));
            //Log.d("DEBUG","res : "+res[0][0]+", "+res[0][1]+", "+res[1][0]+", "+res[1][1]);

        }
        //BigInteger test = new BigInteger("fffe020000000000",16);
        //for(int i = 0; i < 64;i++){
        //    Log.d("DEBUG", "nMatrix.testBit(indice) : "+test.testBit(i)+", i = "+i);
        //}
        //Log.d("DEBUG","res : "+res[0][0]+", "+res[0][1]+", "+res[1][0]+", "+res[1][1]);
        return res;
    }
}
