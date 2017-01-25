package com.juliettegonzalez.breakthroughapp.AI;

public class Node {
    public Node parent;
    public Double value;
    public int level;
    public int[][] move;
    public Matrix matrix;
    public int depth;
    public boolean cut = false;

    public Node(int depth, Node parent, int[][] move, Matrix matrix, int level){
        this.parent = parent;
        this.move = move;
        this.matrix = matrix;
        this.depth = depth;
        this.level = level;
    }

    public void process(){

        if (move != null){
            Matrix mat = new Matrix(matrix);
            mat.applyMove(move);
            mat.changePlayer();
            depth--;
            matrix = new Matrix(mat);
        }


        if (depth > 0){
            int childLevel = level + 1;
            int offsetY = matrix.isComputerAI() ? 1 : -1;
            int[][] pawns = matrix.getPawns(matrix.isComputerAI());


            if (pawns.length == 0){
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

            Node node = null;
            Boolean found = false;
            for (int i=0;i<pawns.length;i++){

                int[][] newMove1 = {pawns[i], {pawns[i][0]+offsetY, pawns[i][1]-1}}; //Front left move
                if (!cut && newMove1[1][1]>=0 && matrix.validMove(newMove1, false)){
                    node = new Node(depth, this, newMove1, new Matrix(matrix), childLevel);
                    node.process();
                    node = null;
                    System.gc();
                    found = true;
                }

                int[][] newMove2 = {pawns[i], {pawns[i][0]+offsetY, pawns[i][1]}}; //Front move
                if (!cut && matrix.validMove(newMove2, true)){
                    node = new Node(depth, this, newMove2, new Matrix(matrix), childLevel);
                    node.process();
                    node = null;
                    System.gc();
                    found = true;
                }

                int[][] newMove3 = {pawns[i], {pawns[i][0]+offsetY, pawns[i][1]+1}}; //Front right move
                if (!cut && newMove3[1][1]<=7 && matrix.validMove(newMove3, false)){
                    node = new Node(depth, this, newMove3, new Matrix(matrix), childLevel);
                    node.process();
                    node = null;
                    System.gc();
                    found = true;
                }
                if (cut){break;}
                else if(i == pawns.length-1){
                    cut = true;
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
            if(value < -900){
                value += level;
            }
            if(value > 900){
                value -= level;
            }
            if (parent != null){
                propagate(this);
            }
        }
    }

    public void propagate(Node node){
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
}
