package student_player;

import Saboteur.SaboteurBoardState;
import Saboteur.cardClasses.SaboteurTile;

import java.lang.reflect.Array;
import java.util.*;

public class MyTools {
    public static double calcDistanceToGoal(int[][] currentBest,SaboteurBoardState boardState) {


        return Math.random();
    }

    public static ArrayList<Integer> checkRevealed(SaboteurBoardState boardState){
        SaboteurTile[][] board = boardState.getHiddenBoard();

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

    public static Boolean checkPathExists(SaboteurBoardState boardState, int i, int j){



        return true;

    }

    public static double[] calcBestPos(SaboteurBoardState boardState){

        //calc dist from lowest 1 from each column

        SaboteurTile[][] board = boardState.getHiddenBoard();


        int bestRow = Integer.MAX_VALUE;
        int bestCol = Integer.MAX_VALUE;
        double bestAv = Integer.MAX_VALUE;

        int[] goalRow = {3,5,7};
        int[] goalCol = {12,12,12};

        ArrayList<Integer> revealedPositions = checkRevealed(boardState);


        ArrayList<String> BlockCards = new ArrayList<String>(
                Arrays.asList("1", "2", "2_flip", "3", "3_flip", "4", "4_flip", "11", "11_flip", "12", "12_flip", "13", "14", "14_flip", "15")
        );
        ArrayList<String> LeftCards = new ArrayList<String>(
                Arrays.asList("5_flip", "6", "7_flip", "9", "9_flip", "10", "8")
        );
        ArrayList<String> RightCards = new ArrayList<String>(
                Arrays.asList("5", "6_flip", "7", "8", "9", "9_flip", "10")
        );
        ArrayList<String> TopCards = new ArrayList<String>(
                Arrays.asList("0", "5_flip", "6", "6_flip", "7", "8", "9_flip")
        );
        ArrayList<String> BottomCards = new ArrayList<String>(
                Arrays.asList("0", "5", "6", "6_flip", "7_flip", "8", "9")
        );


        for (int i = 0; i<12; i++){
            for (int j= 0; j<board.length; j++){
                try {
                    String card = board[i][j].getIdx();
                    if (BlockCards.contains(card)){
                        continue;
                    }


                    // CHECK IF PATH EXISTS TO CARD, IF NOT CANNOT BE BEST POSITION
                    if (!checkPathExists(boardState, i, j)){
                        continue;
                    }




                    //sum from [left, right, top, bottom]
                    double[] sums = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};

                    for (int check: revealedPositions){



                        if (LeftCards.contains(card) && j!=0){
                            if (sums[0] == Integer.MAX_VALUE){
                                sums[0] = 0;
                            }
                            sums[0] += Math.abs(i-goalCol[check])+Math.abs(j-1-goalRow[check]);
                        }
                        if (RightCards.contains(card) && j!=13){
                            if (sums[1] == Integer.MAX_VALUE){
                                sums[1] = 0;
                            }
                            sums[1] += Math.abs(i-goalCol[check])+Math.abs(j+1-goalRow[check]);
                        }
                        if (TopCards.contains(card) && i!=0){
                            if (sums[2] == Integer.MAX_VALUE){
                                sums[2] = 0;
                            }
                            sums[2] += Math.abs(i-1-goalCol[check])+Math.abs(j-goalRow[check]);
                        }
                        if (BottomCards.contains(card) && i!=13){
                            if (sums[3] == Integer.MAX_VALUE){
                                sums[3] = 0;
                            }
                            sums[3] += Math.abs(i+1-goalCol[check])+Math.abs(j-goalRow[check]);
                        }
                    }

                    double lowest = Integer.MAX_VALUE;
                    int lowest_idx = -1;

                    for (int k = 0; k<sums.length; k++){
                        sums[k] = sums[k]/revealedPositions.size();
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
                        } else if (lowest_idx == 1){
                            bestCol = i;
                            bestRow = j+1;
                        }
                        else if (lowest_idx == 2){
                            bestCol = i-1;
                            bestRow = j;
                        } else {
                            bestCol = i+1;
                            bestRow = j;
                        }
                    }


                } catch (Exception e){
                    continue;
                }
            }
            System.out.println(" ");
        }



        double[] ret = {bestCol, bestRow, bestAv};

        return ret;

    }
}