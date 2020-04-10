package student_player;

import Saboteur.SaboteurBoardState;
import Saboteur.cardClasses.SaboteurTile;

import java.lang.reflect.Array;
import java.util.*;

public class MyTools {

    private static ArrayList<String> BlockCards = new ArrayList<String>(
            Arrays.asList("entrance", "1", "2", "2_flip", "3", "3_flip", "4", "4_flip", "11", "11_flip", "12", "12_flip", "13", "14", "14_flip", "15")
    );
    private static ArrayList<String> LeftCards = new ArrayList<String>(
            Arrays.asList("entrance","5_flip", "6", "7_flip", "9", "9_flip", "10", "8")
    );
    private static ArrayList<String> RightCards = new ArrayList<String>(
            Arrays.asList("entrance","5", "6_flip", "7", "8", "9", "9_flip", "10")
    );
    private static ArrayList<String> TopCards = new ArrayList<String>(
            Arrays.asList("entrance","0", "5_flip", "6", "6_flip", "7", "8", "9_flip")
    );
    private static ArrayList<String> BottomCards = new ArrayList<String>(
            Arrays.asList("entrance","0", "5", "6", "6_flip", "7_flip", "8", "9")
    );


    public static ArrayList<Integer> checkRevealed(SaboteurTile[][] board){

        ArrayList<Integer> toConsider = new ArrayList<>();

        if (board[12][3].getIdx() != "8"){
            if (board[12][3].getIdx() == "nugget"){
                toConsider.removeAll(toConsider);
                toConsider.add(0);
            }
        } else {
            toConsider.add(0);
        }

        if (board[12][5].getIdx() != "8"){
            if (board[12][5].getIdx() == "nugget"){
                toConsider.removeAll(toConsider);
                toConsider.add(1);
            }
        } else {
            toConsider.add(1);
        }

        if (board[12][7].getIdx() != "8"){
            if (board[12][7].getIdx() == "nugget"){
                toConsider.removeAll(toConsider);
                toConsider.add(2);
            }
        } else {
            toConsider.add(2);
        }

        return toConsider;
    }

    public static Boolean checkPathExists(SaboteurTile[][] board, int i, int j, int old_i, int old_j){

        int i_goal = 5;
        int j_goal = 5;

        if (i_goal == i && j_goal == j){
            return true;
        }


        //System.out.println("checking path: i: " + i + ", j: " + j);

        try{
            String card = board[i][j].getIdx();
        } catch (Exception e){
            return false;
        }

        Boolean[] ret = new Boolean[4];

        for (int k = 0; k<ret.length; k++){
            ret[k] = false;
        }

        //checking left
        if (old_j != j-1){
            try{
                String card = board[i][j].getIdx();
                if (LeftCards.contains(card)){
                    ret[0] = checkPathExists(board, i, j-1, i, j);
                }
            } catch (Exception e){}
        }

        //checking right
        if (old_j != j+1){
            try{
                String card = board[i][j].getIdx();
                if (RightCards.contains(card)){
                    ret[1] = checkPathExists(board, i, j+1, i, j);
                }
            } catch (Exception e){}
        }

        //checking top
        if (old_i != i-1){
            //System.out.println("HERE AT TOP");
            try{
                String card = board[i][j].getIdx();
                //System.out.println(card);

                //System.out.print("TRY WOKRED");
                if (TopCards.contains(card)){
                    ret[2] = checkPathExists(board, i-1, j, i, j);
                    //System.out.println("recursed top");
                }
            } catch (Exception e){}
        }

        //checking bottom
        if (old_i != i+1){
            try{
                String card = board[i][j].getIdx();
                if (BottomCards.contains(card)){
                    ret[3] = checkPathExists(board, i+1, j, i, j);
                }
            } catch (Exception e){}
        }

        for (Boolean b: ret){
            if (b==true){
                return true;
            }
        }
        return false;
    }

    public static double[] calcBestPos(SaboteurTile[][] board){

        //calc dist from lowest 1 from each column


        int bestRow = 100;
        int bestCol = 100;
        double bestAv = 400;
        int direction = 5;

        int[] goalRow = {3,5,7};
        int[] goalCol = {12,12,12};

        ArrayList<Integer> revealedPositions = checkRevealed(board);





        for (int i = 0; i<12; i++){
            for (int j= 0; j<board.length; j++){
                try {


                    String card = board[i][j].getIdx();


                    if (BlockCards.contains(card)){
                        continue;
                    }
                    //System.out.println("i: " + i + ", j:" + j);
                    //System.out.println(card);

                    // CHECK IF PATH EXISTS TO CARD, IF NOT CANNOT BE BEST POSITION
                      if (checkPathExists(board, i, j, -1, -1) == false){
                       continue;
                    }

                          //System.out.println("PATH EXISTS TO: " + i + ", " + j);


                    //sum from [left, right, top, bottom]
                    double[] sums = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};

                    for (int check: revealedPositions){



                        if (LeftCards.contains(card) && j!=0){
                            if (sums[0] > 100){
                                sums[0] = 0;
                            }
                            sums[0] += Math.abs(i-goalCol[check])+Math.abs(j-1-goalRow[check]);
                        }
                        if (RightCards.contains(card) && j!=13){
                            if (sums[1] > 100){
                                sums[1] = 0;
                            }
                            sums[1] += Math.abs(i-goalCol[check])+Math.abs(j+1-goalRow[check]);
                        }
                        if (TopCards.contains(card) && i!=0){
                            if (sums[2] > 100){
                                sums[2] = 0;
                            }
                            sums[2] += Math.abs(i-1-goalCol[check])+Math.abs(j-goalRow[check]);
                        }
                        if (BottomCards.contains(card) && i!=13){
                            if (sums[3] > 100){
                                sums[3] = 0;
                            }
                            sums[3] += Math.abs(i+1-goalCol[check])+Math.abs(j-goalRow[check]);
                        }
                    }

                    double lowest = Integer.MAX_VALUE;
                    int lowest_idx = -1;

                    for (int k = 0; k<sums.length; k++){
                        sums[k] = sums[k]/revealedPositions.size();

                        //System.out.println(sums[k]);
                        if (sums[k]<lowest){
                            lowest = sums[k];
                            lowest_idx = k;
                        }
                    }



                    if (sums[lowest_idx]<bestAv){
                        bestAv = sums[lowest_idx];
                        if (lowest_idx == 0) {
                            bestCol = i;
                            bestRow = j-1;
                            direction = 0;
                        } else if (lowest_idx == 1){
                            bestCol = i;
                            bestRow = j+1;
                            direction = 1;
                        }
                        else if (lowest_idx == 2){
                            bestCol = i-1;
                            bestRow = j;
                            direction = 2;
                        } else {
                            bestCol = i+1;
                            bestRow = j;
                            direction = 3;
                        }
                    }


                } catch (Exception e){
                    continue;
                }
            }
        }



        double[] ret = {bestCol, bestRow, direction, bestAv};

        return ret;

    }
}