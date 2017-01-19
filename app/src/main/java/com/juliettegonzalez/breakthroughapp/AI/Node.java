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
            //Log.d("DEBUG", "Maj move, depth left : "+(depth-1));
            Matrix mat = new Matrix(matrix);
            //Log.d("DEBUG","tab matrix : "+Arrays.deepToString(matrix.getMatrix(true))+", bitboard : "+matrix.toBitboard(matrix.getMatrix(true)));
            mat.applyMove(move);
            mat.changePlayer();
            depth--;
            matrix = new Matrix(mat);
        }


        if (depth > 0){
            //Log.d("DEBUG", "Higher depths, level : " + (level+1));
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
                    //Log.d("DEBUG", "Node 1, depth : " + depth + ", level : "+ childLevel +", move : "+ Arrays.deepToString(newMove1));
                    node = new Node(depth, this, newMove1, new Matrix(matrix), childLevel);
                    //Log.d("DEBUG", "N1 : Depth : "+depth+", level : "+childLevel);
                    node.process();
                    node = null;
                    System.gc();
                    found = true;
                }

                int[][] newMove2 = {pawns[i], {pawns[i][0]+offsetY, pawns[i][1]}}; //Front move
                if (!cut && matrix.validMove(newMove2, true)){
                    //Log.d("DEBUG", "Node 2, depth : " + depth + ", level : "+ childLevel +", move : "+ Arrays.deepToString(newMove2));
                    node = new Node(depth, this, newMove2, new Matrix(matrix), childLevel);
                    //Log.d("DEBUG", "N2 : Depth : "+depth+", level : "+childLevel);
                    node.process();
                    node = null;
                    System.gc();
                    found = true;
                }

                int[][] newMove3 = {pawns[i], {pawns[i][0]+offsetY, pawns[i][1]+1}}; //Front right move
                if (!cut && newMove3[1][1]<=7 && matrix.validMove(newMove3, false)){
                    //Log.d("DEBUG", "Node 3, depth : " + depth + ", level : "+ childLevel +", move : "+ Arrays.deepToString(newMove3));
                    node = new Node(depth, this, newMove3, new Matrix(matrix), childLevel);
                    //Log.d("DEBUG", "N3 : Depth : "+depth+", level : "+childLevel);
                    node.process();
                    node = null;
                    System.gc();
                    found = true;
                }
                if (cut){
                    /*String actualPlayer;
                    if(matrix.isComputerAI()){
                        actualPlayer = "computer";
                    }else{
                        actualPlayer = "human";
                    }
                    Log.d("DEBUG", "Real Cut, player : "+ actualPlayer +", value : "+ value +", parent value : "+ parent.value);
                    */break;
                }
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
            */if (parent != null){
                propagate(this);
            }
        }
    }

    public void propagate(Node node){
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
}
