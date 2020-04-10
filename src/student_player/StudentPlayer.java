package student_player;


import Saboteur.SaboteurMove;
import Saboteur.cardClasses.SaboteurTile;
import boardgame.Move;

import Saboteur.SaboteurPlayer;
import Saboteur.SaboteurBoardState;

import java.util.ArrayList;
import java.util.Arrays;

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


        if (MyTools.checkRevealed(boardState.getHiddenBoard()).size() > 1) {
            // PLAY MAP CARD AND OTHERWISE DONT
            for (int i = 0; i < legalMoves.size(); i++) {
                if (legalMoves.get(i).getCardPlayed().getName().equals("Map")) {
                    myMove = legalMoves.get(i);
                    return myMove;
                }
            }
        }

            // @returns:
            //      best place to play i
            //      best place to play j
            //      direction of best spot to play and current card:
            //                0 -> current card is right of best pos
            //                1 -> current card is left of best pos
            //                2 -> current card is bottom of bestpos
            //                3 -> current card is top of bestpos
            //      average distance from goal for current bestPos
            double[] bestPos = MyTools.calcBestPos(board);

            ArrayList<SaboteurMove> bestMoves = new ArrayList<>();
            ArrayList<SaboteurMove> allOtherBlocks = new ArrayList<>();
            ArrayList<SaboteurMove> allOtherMoves = new ArrayList<>();
            ArrayList<SaboteurMove> MalusMoves = new ArrayList<>();
            ArrayList<SaboteurMove> DestroyMoves = new ArrayList<>();

            for (SaboteurMove m : legalMoves) {

                //System.out.println(m.getCardPlayed().getName());

                String[] name = m.getCardPlayed().getName().split(":");
                String cardName = "";
                if (name.length == 1){
                    cardName = name[0];
                } else {
                    cardName = name[1];
                }

                if (m.getPosPlayed()[1] == bestPos[1] && m.getPosPlayed()[0] == bestPos[0] && !BlockCards.contains(cardName)) {
                    SaboteurTile[][] newBS = boardState.getHiddenBoard();
                    SaboteurTile newTile = new SaboteurTile(cardName);
                    newBS[m.getPosPlayed()[0]][m.getPosPlayed()[1]] = newTile;
                    double[] newPos = MyTools.calcBestPos(newBS);
                    if (newPos[3] < bestPos[3]) {
                        bestMoves.add(m);
                    }
                } else if (BlockCards.contains(cardName) && m.getPosPlayed()[0] != bestPos[1] && m.getPosPlayed()[1] != bestPos[0]) {
                    allOtherBlocks.add(m);
                } else if (m.getPosPlayed()[0] != bestPos[1] && m.getPosPlayed()[1] != bestPos[0]) {
                    allOtherBlocks.add(m);
                }
                if (cardName == "Malus"){
                    MalusMoves.add(m);
                }
                if (cardName == "Destroy"){
                    DestroyMoves.add(m);
                }

            }


            //case: cannot play best move, must check if close to goal:
            if (bestPos[3] < 2) {
                //CASE: close to goal and cannot play a finishing move
                if (MalusMoves.size() != 0){
                    return MalusMoves.get(0);
                } else if (DestroyMoves.size()!=0){
                    return DestroyMoves.get(0);
                }

            }

            ArrayList<String> secondBest = new ArrayList<>(Arrays.asList("5", "5_flip", "7", "7_flip"));

            ArrayList<String> thirdBest = new ArrayList<>(Arrays.asList("6", "6_flip", "9", "9_flip"));

            for (SaboteurMove m : bestMoves) {
                if (m.getCardPlayed().getName().split(":")[1].equals("0") || m.getCardPlayed().getName().split(":")[1].equals("10")) {
                    return m;
                }
            }

            for (SaboteurMove m : bestMoves) {
                if (secondBest.contains(m.getCardPlayed().getName().split(":")[1])) {
                    return m;
                }
            }

            for (SaboteurMove m : bestMoves) {
                if (thirdBest.contains(m.getCardPlayed().getName().split(":")[1])) {
                    return m;
                }
            }

            for (SaboteurMove m : bestMoves) {
                if (m.getCardPlayed().getName().split(":")[1].equals("8")) {
                    return m;
                }
            }

            if (allOtherBlocks.size() != 0){
                return allOtherBlocks.get(0);
            } else {
                return allOtherMoves.get(0);
            }
    }
}
