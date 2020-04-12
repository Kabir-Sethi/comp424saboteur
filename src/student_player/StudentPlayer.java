package student_player;


import Saboteur.SaboteurMove;
import Saboteur.cardClasses.*;
import boardgame.Move;

import Saboteur.SaboteurPlayer;
import Saboteur.SaboteurBoardState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/** A player file submitted by a student. */
public class StudentPlayer extends SaboteurPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260738242");
    }

    private static ArrayList<String> BlockCards = new ArrayList<String>(Arrays.asList("entrance", "1", "2", "2_flip", "3", "3_flip", "4", "4_flip", "11", "11_flip", "12", "12_flip", "13", "14", "14_flip", "15"));
    private static ArrayList<String> LeftCards = new ArrayList<String>(Arrays.asList("entrance", "5_flip", "6", "7_flip", "9", "9_flip", "10", "8"));
    private static ArrayList<String> RightCards = new ArrayList<String>(Arrays.asList("entrance", "5", "6_flip", "7", "8", "9", "9_flip", "10"));
    private static ArrayList<String> TopCards = new ArrayList<String>(Arrays.asList("entrance", "0", "5_flip", "6", "6_flip", "7", "8", "9_flip"));
    private static ArrayList<String> BottomCards = new ArrayList<String>(Arrays.asList("entrance", "0", "5", "6", "6_flip", "7_flip", "8", "9"));

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(SaboteurBoardState boardState) {
        // You probably will make separate functions in MyTools.
        // For example, maybe you'll need to load some pre-processed best opening
        // strategies...

        Move myMove = boardState.getRandomMove();

        SaboteurTile[][] board = boardState.getHiddenBoard();

        ArrayList<SaboteurMove> legalMoves = boardState.getAllLegalMoves();

        long startTime = System.nanoTime();

        ArrayList<Integer> revealed = MyTools.checkRevealed(board);

        if (revealed.size() > 1) {
            // PLAY MAP CARD AND OTHERWISE DONT
            Random r = new Random();
                int index = r.nextInt(revealed.size());
                if (revealed.get(index) == 0){
                    SaboteurMove m = new SaboteurMove(new SaboteurMap(), 12, 3, boardState.getTurnPlayer());
                    if(boardState.isLegal(m)) return m;
                } else if (revealed.get(index) == 1){
                    SaboteurMove m = new SaboteurMove(new SaboteurMap(), 12, 5, boardState.getTurnPlayer());
                    if(boardState.isLegal(m)) return m;
                } else {
                    SaboteurMove m = new SaboteurMove(new SaboteurMap(), 12, 7, boardState.getTurnPlayer());
                    if(boardState.isLegal(m)) return m;
                }
        }

        if (boardState.getNbMalus(boardState.getTurnPlayer()) > 0){
            SaboteurMove move = new SaboteurMove(new SaboteurBonus(), 0, 0, boardState.getTurnPlayer());

            if (boardState.isLegal(move)){
                return move;
            }
        }

        long endTime = System.nanoTime();

        long elapsed = (endTime-startTime)/1000;

        System.out.println("REVEALED ELAPSED: " + elapsed);


        startTime = System.nanoTime();

        System.out.println("BestPos Started");
            // @returns:
            //      best place to play i
            //      best place to play j
            //      direction of best spot to play and current card:
            //                0 -> current card is right of best pos
            //                1 -> current card is left of best pos
            //                2 -> current card is bottom of bestpos
            //                3 -> current card is top of bestpos
            //      average distance from goal for current bestPos
            double[] bestPos = MyTools.calcBestPos(board, true);

        endTime = System.nanoTime();
        elapsed = (endTime-startTime)/1000;

        System.out.println("BESTPOS ELAPSED: " + elapsed);

            //System.out.println("i: " + bestPos[0] + ", j: "+bestPos[1] +", bestAv: "+bestPos[3]);

            // make hierarchy of best moves at position bestPos[0], bestPos[1]

        startTime = System.nanoTime();

            ArrayList<SaboteurMove> bestMoves = MyTools.getBestMoveHierarchy(bestPos[0], bestPos[1], board, legalMoves, bestPos[3]);

            //System.out.println("BestMoves Size: " + bestMoves.size());
        endTime = System.nanoTime();
        elapsed = (endTime-startTime)/1000;

        System.out.println("SYSTEM HIERARCHY ELAPSED: " + elapsed);


        if (bestMoves.size() > 0) return bestMoves.get(0);

            // if close to end, play malus or destroy
            if (bestPos[3] <= 2){
                // play malus
                for (SaboteurMove m: legalMoves){
                    if (m.getCardPlayed().getName().equals("Malus")){
                        return m;
                    }
                }

                int[] last_idx = new int[2];
                // if no malus, destroy last card:
                if (bestPos[2] == 0){
                    last_idx[0] = (int) bestPos[0];
                    last_idx[1] = (int) bestPos[1]+1;
                } else if (bestPos[2] == 1){
                    last_idx[0] = (int) bestPos[0];
                    last_idx[1] = (int) bestPos[1]-1;
                } else if (bestPos[2] == 2){
                    last_idx[0] = (int) bestPos[0]+1;
                    last_idx[1] = (int) bestPos[1];
                } else {
                    last_idx[0] = (int) bestPos[0]-1;
                    last_idx[1] = (int) bestPos[1];
                }

//                SaboteurMove play_destroy = new SaboteurMove(new SaboteurDestroy(), last_idx[0], last_idx[1], boardState.getTurnPlayer());
//
//                if (boardState.isLegal(play_destroy)){
//                    return play_destroy;
//                }
            }



            // if no best move: play random move not at best position
            for (SaboteurMove m: legalMoves){

                String card = m.getCardPlayed().getName();

                String[] card_split = card.split(":");

                String card_idx = "";

                if (card_split.length  == 1){
                    card_idx = card_split[0];
                } else {
                    card_idx = card_split[1];
                }


                if (m.getPosPlayed()[0] != bestPos[0] && m.getPosPlayed()[1] != bestPos[1] && BlockCards.contains(card_idx)){
                    return m;
                }
            }

            // drop destroy cards!
            // or drop block card
        if (boardState.getPlayerCardsForDisplay(boardState.getTurnPlayer()).size() > 1){
            return new SaboteurMove(new SaboteurDrop(), 1,0, boardState.getTurnPlayer());
        } else {
            return myMove;
        }


    }
}
