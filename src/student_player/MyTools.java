package student_player;

import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;
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

        if (!board[12][3].getIdx().contains("8")){

            if (board[12][3].getIdx().contains( "nugget")){
                toConsider.removeAll(toConsider);
                toConsider.add(0);
                return toConsider;
            }
        } else {
            toConsider.add(0);
        }

        if (!board[12][5].getIdx().contains("8")){

            if (board[12][5].getIdx().contains("nugget")){
                toConsider.removeAll(toConsider);
                toConsider.add(1);
                return toConsider;
            }
        } else {
            toConsider.add(1);
        }


        if (!board[12][7].getIdx().contains("8")){

            if (board[12][7].getIdx().contains( "nugget")){
                toConsider.removeAll(toConsider);
                toConsider.add(2);
                return toConsider;
            }
        } else {
            toConsider.add(2);
        }

        if (toConsider.size() == 0){
            if (board[12][3].getIdx().contains("8")) toConsider.add(0);
            if (board[12][5].getIdx().contains("8")) toConsider.add(1);
            if (board[12][7].getIdx().contains("8")) toConsider.add(2);
        }

        return toConsider;
    }


    public static Boolean checkPathExists(SaboteurTile[][] board, int i, int j){

        boolean[][] visited = new boolean[board.length][board[0].length];

        LinkedList<Integer[]> queue = new LinkedList<>();

        visited[i][j] = true;

        for (int k=0; k<visited.length; k++){
            for (int l =0; l<visited[0].length; l++){
                visited[k][l]= false;
            }
        }

        Integer [] p = {i,j};

        queue.add(p);

        while (queue.size() > 0){

            Integer[] s = queue.poll();

            if (s[0] == 5 && s[1] == 5){
                return true;
            }

            String card_idx = board[s[0]][s[1]].getIdx();

            if (!BlockCards.contains(card_idx)){
                //Checking Right
                if (RightCards.contains(card_idx) && !visited[s[0]][s[1]+1]){
                    try{
                        String card = board[s[0]][s[1]+1].getIdx();
                        Integer[] to_add = {s[0], s[1]+1};
                        if (s[0] == 5 && s[1]+1 == 5) return true;
                        queue.add(to_add);

                    } catch (Exception e){

                    }
                }
                //Checking Left
                if (LeftCards.contains(card_idx) && !visited[s[0]][s[1]-1]){
                    try{
                        String card = board[s[0]][s[1]-1].getIdx();
                        Integer[] to_add = {s[0], s[1]-1};
                        if (s[0] == 5 && s[1]-1 == 5) return true;
                        queue.add(to_add);
                    } catch (Exception e){

                    }
                }
                //Checking Bottom
                if (BottomCards.contains(card_idx) && !visited[s[0]+1][s[1]]){
                    try{
                        String card = board[s[0]+1][s[1]].getIdx();
                        Integer[] to_add = {s[0]+1, s[1]};
                        if (s[0]+1 == 5 && s[1] == 5) return true;
                        queue.add(to_add);
                    } catch (Exception e){

                    }
                }
                //Checking Top
                if (TopCards.contains(card_idx) && !visited[s[0]-1][s[1]]){
                    try{
                        String card = board[s[0]-1][s[1]].getIdx();
                        Integer[] to_add = {s[0]-1, s[1]};
                        if (s[0]-1 == 5 && s[1] == 5) return true;
                        queue.add(to_add);
                    } catch (Exception e){

                    }
                }

            }

        }



        return false;

    }

    public static double[] calcBestPos(SaboteurTile[][] board, boolean checkPath){

        //calc dist from lowest 1 from each column


        int bestRow = 5;
        int bestCol = 6;
        double bestAv = (7+9+9)/3;
        int direction = 5;

        int[] goalRow = {3,5,7};
        int[] goalCol = {12,12,12};

        ArrayList<Integer> revealedPositions = checkRevealed(board);





        for (int i = 0; i<board.length; i++){
            for (int j= 0; j<board.length; j++){
                try {


                    String card = board[i][j].getIdx();

                    if (j== goalRow[0] && i==goalCol[0]){
                        continue;
                    }
                    if (j== goalRow[1] && i==goalCol[1]){
                        continue;
                    }
                    if (j== goalRow[2] && i==goalCol[2]){
                        continue;
                    }



                    if (BlockCards.contains(card)){
                        continue;
                    }
                    //System.out.println("i: " + i + ", j:" + j);
                    //System.out.println(card);

                    //CHECK IF PATH EXISTS TO CARD, IF NOT CANNOT BE BEST POSITION

//                    long startTime = System.nanoTime();
//                    System.out.println("Started checking path existence");
//                    if (checkPath){
//                        if (!checkPathExists(board, i, j)){
//                            System.out.println("NO PATH TO: " + i + ", " + j);
//                            continue;
//                        }
//                    }
//                    long endTime = System.nanoTime();
//
//                    long elapsed = (endTime-startTime)/1000;
//
//                    System.out.println("PATH EXISTENCE ELAPSED:" + elapsed);


                    //System.out.println("PATH EXISTS TO: " + i + ", " + j);


                    //sum from [left, right, top, bottom]
                    double[] sums = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};

                    for (int check: revealedPositions){

                        //System.out.println("Revealed Postions size: " + revealedPositions.size());

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
                        if (sums[k]<=lowest){
                            lowest = sums[k];
                            lowest_idx = k;
                        }
                    }



                    if (sums[lowest_idx]<bestAv){

                        if (lowest_idx == 0) {
                            try {
                                String c = board[i][j-1].getIdx();
                            } catch (Exception e) {
                                bestAv = sums[lowest_idx];
                                bestCol = i;
                                bestRow = j - 1;
                                direction = 0;
                            }
                        } else if (lowest_idx == 1){
                            try {
                                String c = board[i][j+1].getIdx();
                            } catch (Exception e) {
                                bestAv = sums[lowest_idx];
                                bestCol = i;
                                bestRow = j + 1;
                                direction = 1;
                            }
                        }
                        else if (lowest_idx == 2){
                                try {
                                    String c = board[i-1][j].getIdx();
                                } catch (Exception e) {

                                    bestAv = sums[lowest_idx];
                                    bestCol = i - 1;
                                    bestRow = j;
                                    direction = 2;
                                }
                        } else {
                                try {
                                    String c = board[i+1][j].getIdx();
                                } catch (Exception e) {
                                    bestAv = sums[lowest_idx];
                                    bestCol = i + 1;
                                    bestRow = j;
                                    direction = 3;
                                }
                        }
                    }


                } catch (Exception e){

                }
            }
        }



        double[] ret = {bestCol, bestRow, direction, bestAv};

        return ret;

    }


    public static ArrayList<SaboteurMove> sortBestMoves(ArrayList<SaboteurMove> bestMoves){

        ArrayList<SaboteurMove> firstorder = new ArrayList<>();
        ArrayList<SaboteurMove> secondorder = new ArrayList<>();
        ArrayList<SaboteurMove> thirdorder = new ArrayList<>();
        ArrayList<SaboteurMove> fourthorder = new ArrayList<>();

        for (SaboteurMove m: bestMoves){
            String card = m.getCardPlayed().getName();

            String[] card_split = card.split(":");

            String card_idx = "";

            if (card_split.length  == 1){
                card_idx = card_split[0];
            } else {
                card_idx = card_split[1];
            }

            if (card_idx == "10" || card_idx == "0"){
                firstorder.add(m);
            }
            else if (card_idx.contains("5") || card_idx.contains("7")){
                secondorder.add(m);
            }
            else if (card_idx.contains("6") || card_idx.contains("9")){
                thirdorder.add(m);
            }
            else if (card_idx == "8"){
                fourthorder.add(m);
            }
            else if (!BlockCards.contains(card_idx)){
                fourthorder.add(m);
            }

        }

        firstorder.addAll(secondorder);
        firstorder.addAll(thirdorder);
        firstorder.addAll(fourthorder);

        return firstorder;
    }

    public static ArrayList<SaboteurMove> getBestMoveHierarchy(double col, double row, SaboteurTile[][] board, ArrayList<SaboteurMove> legalMoves, double bestAv) {

        int Col = (int)col;
        int Row = (int)row;


        ArrayList<SaboteurMove> bestMoves = new ArrayList<>();


        for (SaboteurMove m: legalMoves){
            String card = m.getCardPlayed().getName();

            String[] card_split = card.split(":");

            String card_idx = "";

            if (card_split.length  == 1){
                card_idx = card_split[0];
            } else {
                card_idx = card_split[1];
            }


            if (m.getPosPlayed()[0] == Col && m.getPosPlayed()[1] == Row && !BlockCards.contains(card_idx)){
                board[Col][Row] = new SaboteurTile(card_idx);

                double[] bestPos = calcBestPos(board, false);

                if (bestPos[3] <= bestAv){
                    bestMoves.add(m);
                }

            }

        }


        bestMoves = sortBestMoves(bestMoves);

        return bestMoves;

    }
}