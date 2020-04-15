package student_player;

import Saboteur.SaboteurBoard;
import Saboteur.SaboteurBoardState;
import Saboteur.cardClasses.SaboteurTile;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class MyTools {

    private static ArrayList<String> BlockCards = new ArrayList<String>(Arrays.asList("entrance", "1", "2", "2_flip", "3", "3_flip", "4", "4_flip", "11", "11_flip", "12", "12_flip", "13", "14", "14_flip", "15"));
    private static ArrayList<String> LeftCards = new ArrayList<String>(Arrays.asList("entrance", "5_flip", "6", "7_flip", "9", "9_flip", "10", "8"));
    private static ArrayList<String> RightCards = new ArrayList<String>(Arrays.asList("entrance", "5", "6_flip", "7", "8", "9", "9_flip", "10"));
    private static ArrayList<String> TopCards = new ArrayList<String>(Arrays.asList("entrance", "0", "5_flip", "6", "6_flip", "7", "8", "9_flip"));
    private static ArrayList<String> BottomCards = new ArrayList<String>(Arrays.asList("entrance", "0", "5", "6", "6_flip", "7_flip", "8", "9"));



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


    public static ArrayList<Double> getBestPosition(SaboteurTile[][] board, boolean omitBlockCards, boolean checkPath, boolean checkSurroundings){

        double bestAv = Integer.MAX_VALUE;
        int best_i = 5;
        int best_j = 5;
        int best_side = 0;

        for (int i = 0; i<board.length; i++) {
            for (int j = 0; j < board.length; j++) {

                try {
                    String card_idx = board[i][j].getIdx();


                // IF WE WANT TO OMIT BLOCKCARDS AND THIS IS BLOCK CARD, CAN'T BE BEST POSITION
                if (omitBlockCards && BlockCards.contains(card_idx)) continue;

                // IF PATH DOESN'T EXIST TO POSITION MOVE TO NEXT ONE
                if (checkPath) if (!checkPathExists(board, i, j)) continue;


                // IF SURROUNDED BY OTHER CARDS ON ALL SIDES, CAN'T BE BEST CARD
                boolean[] peripheral = surrounded(board, i, j);
                boolean should_skip = true;
                for (boolean b : peripheral) {
                    if (b == true) should_skip = false;
                    else if (!checkSurroundings) {
                        b = true;
                    }

                }
                if (should_skip && checkSurroundings) continue;

                //ASSIGN NEW BEST AVERAGES
                double[] avg = calcBestAvg(board, i, j, peripheral);
                if (avg[0] < bestAv) {
                    bestAv = avg[0];
                    best_i = i;
                    best_j = j;
                    best_side = (int) avg[1];
                }

                } catch (Exception e){
                    System.out.println(e);
                }
            }
        }

        ArrayList<Double> ret = new ArrayList<>();
        ret.add((double)best_i); ret.add((double)best_j); ret.add(bestAv); ret.add((double)best_side);

        return ret;
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
            //System.out.println(queue.size());
            Integer[] s = queue.poll();
            visited[s[0]][s[1]] = true;
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


    public static boolean[] surrounded(SaboteurTile[][] board, int i, int j){

        String idx = board[i][j].getIdx();

        boolean[] sides = new boolean[4];
        // SIDE 0: LEFT
        // SIDE 1: RIGHT
        // SIDE 2: TOP
        // SIDE 3: BOTTOM
        for (boolean b: sides){
            b = true;
        }


        if (LeftCards.contains(idx)){
            try{
                if (j > 0){
                    board[i][j-1].getIdx();
                }

                sides[0] = false;

            } catch (Exception e){ }

        } else {
            sides[0] = false;
        }


        if (RightCards.contains(idx)){
            try{
                if (j < 13){
                    board[i][j+1].getIdx();
                }

                sides[1] = false;

            } catch (Exception e){ }

        } else {
            sides[1] = false;
        }

        if (TopCards.contains(idx)){
            try{
                if (i > 0){
                    board[i-1][j].getIdx();
                }

                sides[2] = false;

            } catch (Exception e){ }

        } else {
            sides[2] = false;
        }

        if (BottomCards.contains(idx)){
            try{
                if (j < 13){
                    board[i+1][j].getIdx();
                }

                sides[3] = false;

            } catch (Exception e){ }

        } else {
            sides[3] = false;
        }

        return sides;

    }

    public static double[] calcBestAvg(SaboteurTile[][] board, int i, int j, boolean[] peripheral){

        ArrayList<Integer> revealed = checkRevealed(board);

        int[] goal_i = {12, 12, 12};
        int[] goal_j = {3, 5, 7};

        double avg = Double.MAX_VALUE;
        double side = 0;

        for (int k = 0; k < peripheral.length; k++){
            if (!peripheral[k]) continue;
            else {
                int next_i = i;
                int next_j = j;
                // LEFT:
                if (k==0)
                    next_j--;

                // RIGHT:
                if (k==1)
                    next_j++;

                // TOP:
                if (k==2)
                    next_i--;

                // BOTTOM:
                if (k==3)
                    next_i++;

                int sum = 0;

                for (int l = 0; l<revealed.size(); l++){
                    sum += Math.abs(goal_i[revealed.get(l)] - next_i);
                    sum += Math.abs(goal_j[revealed.get(l)] - next_j);
                }

                double curr_avg = sum/revealed.size();

                if (curr_avg<avg)
                    avg = curr_avg;
                    side = k;

            }
        }

        double[] ret = {avg, side};
        return ret;
    }


}