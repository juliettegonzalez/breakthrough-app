package com.juliettegonzalez.breakthroughapp.AI;

import android.util.Log;

import java.math.BigInteger;

/**
 * Created by Yvonnig on 17/01/2017.
 */


public class TBNode {
    public int level;
    public TBMatrix matrix;
    public int depth;
    public double alpha;
    public double beta;
    public int color;

    public TBNode(int depth, TBMatrix matrix, int level, double alpha, double beta, int color){
        this.matrix = matrix;
        this.depth = depth;
        this.level = level;
        this.alpha = alpha;
        this.beta = beta;
        this.color = color;
    }

    public double process(){
        double first_alpha = alpha;
        if(MainGame.mMap.containsKey(matrix) && MainGame.mMap.get(matrix).depth >= depth){
            GameValue gv = MainGame.mMap.get(matrix);
            if(gv.type == "Exact"){
                return gv.value;
            }else if(gv.type == "Lower"){
                alpha = Math.max(alpha,gv.value);
            }else if(gv.type == "Upper"){
                beta = Math.min(beta,gv.value);
            }else{
                Log.d("DEBUG","Souci de type dans GameValue");
            }
            if(alpha >= beta ){
                return gv.value;
            }
        }

        if(depth == 0 || matrix.winningPosition()){
            return(color * matrix.analyze(level));
        }


        Boolean player = (color == 1);
        int childLevel = level + 1;
        int offsetY = player ? -1 : 1;
        int off;
        long playerBoard;
        TBMatrix nextMove,mat;
        TBNode node;
        double best = -1000.0;
        double value;
        for(int i=63;0<=i;i--){
            playerBoard = matrix.getMatrix(player);
            if(((playerBoard >>> (long)i) & 1L) == 1L){
                    //Vérification qu'un pion n'a pas atteint la ligne opposée
                    off = i + offsetY * 8;
                    if(off >= 0 && off <= 63){
                        playerBoard &= ~(1L << i);
                        //Test dépassement à droite et non-présence d'un pion perso
                        if(((((i+offsetY*7)%8)<0 ?(i+offsetY*7)%8 + 8 : (i+offsetY*7)%8)!=((8-offsetY)%9)) && ((((playerBoard >>> (long)(i+offsetY*7)) & 1L) == 0L))){
                            nextMove = new TBMatrix((playerBoard | (1L << (long)(i + offsetY * 7))), matrix.getMatrix(!player),player);
                            mat = new TBMatrix(matrix);
                            mat.applyMove(nextMove, player);
                            node = new TBNode(depth-1, mat, childLevel, -beta,-alpha,-color);
                            value = - node.process();
                            best = Math.max(best,value);
                            if(level == 0 && (MainGame.best == null || best > MainGame.bestValue)){
                                MainGame.best = new TBMatrix(mat);
                                MainGame.bestValue = best;
                            }
                            alpha = Math.max(alpha,value);
                            if(alpha >= beta){
                                break;
                            }
                            nextMove = null;
                            node = null;
                            mat = null;
                            System.gc();
                        }
                        //Vérification qu'il n'y a pas d'adversaire en face, ni de pion perso
                        if((((matrix.getMatrix(!player) >>> (long)(i+offsetY*8)) & 1L) == 0L) &&  ((((playerBoard >>> (long)(i+offsetY*8)) & 1L) == 0L))){
                            nextMove = new TBMatrix((playerBoard | (1L << (long)(i + offsetY * 8))), matrix.getMatrix(!player),player);
                            mat = new TBMatrix(matrix);
                            mat.applyMove(nextMove, player);
                            node = new TBNode(depth-1, mat, childLevel, -beta,-alpha,-color);
                            value = - node.process();
                            best = Math.max(best,value);
                            if(level == 0 && (MainGame.best == null || best > MainGame.bestValue)){
                                MainGame.best = new TBMatrix(mat);
                                MainGame.bestValue = best;
                            }
                            alpha = Math.max(alpha,value);
                            if(alpha >= beta){
                                break;
                            }
                            nextMove = null;
                            node = null;
                            mat = null;
                            System.gc();
                        }

                        //Test dépassement à gauche et non-présence d'un pion perso
                        if(((((i+offsetY*9)%8)<0 ?(i+offsetY*9)%8 + 8 : (i+offsetY*9)%8)!=((8+offsetY)%9)) && ((((playerBoard >>> (long)(i+offsetY*9)) & 1L) == 0L))){
                            nextMove = new TBMatrix((playerBoard | (1L << (long)(i + offsetY * 9))), matrix.getMatrix(!player),player);
                            mat = new TBMatrix(matrix);
                            mat.applyMove(nextMove, player);
                            node = new TBNode(depth-1, mat, childLevel, -beta,-alpha,-color);
                            value = - node.process();
                            best = Math.max(best,value);
                            if(level == 0 && (MainGame.best == null || best > MainGame.bestValue)){
                                MainGame.best = new TBMatrix(mat);
                                MainGame.bestValue = best;
                            }
                            alpha = Math.max(alpha,value);
                            if(alpha >= beta){
                                break;
                            }
                            nextMove = null;
                            node = null;
                            mat = null;
                            System.gc();
                        }
                    }
            }
        }
        String type;
        if(best <= first_alpha){
            type = "Upper";
        }else if(beta <= best ){
            type = "Lower";
        }else{
            type = "Exact";
        }
        GameValue new_gv = new GameValue(type,best,depth);
        //Log.d("DEBUG", "Nouvelle GameValue stockée, AI "+ matrix.getMatrix(true).toString(16)+", Player "+matrix.getMatrix(false).toString(16)+", type "+type+", value "+best+", depth "+depth+", player "+color);
        MainGame.mMap.put(matrix,new_gv);
        new_gv = null;
        matrix = null;
        System.gc();
        return best;
    }

    public static int[][] convert(TBMatrix move, TBMatrix previous) {
        int[][] res = {{-1,-1},{-1,-1}};
        long pMatrix = previous.getMatrix(true);
        long nMatrix = move.getMatrix(true);
        int indice;
        boolean n,p;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                indice = (7-i)*8+(7-j);
                n = (((nMatrix >>> (long)(indice)) & 1L) == 1L);
                p = (((pMatrix >>> (long)(indice)) & 1L) == 1L);
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
