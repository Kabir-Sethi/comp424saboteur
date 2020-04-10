package student_player;


import Saboteur.SaboteurMove;
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

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(SaboteurBoardState boardState) {
        // You probably will make separate functions in MyTools.
        // For example, maybe you'll need to load some pre-processed best opening
        // strategies...

        // Is random the best you can do?
        Move myMove = boardState.getRandomMove();
        ArrayList<SaboteurMove> legalMoves = boardState.getAllLegalMoves();
        String direction = "";
        if (direction.equals("top")){
            ArrayList<SaboteurMove> topMove = new ArrayList<>();
            for (SaboteurMove m: legalMoves) {
                if (TopCards.contains(m.getCardPlayed().getName())){
                    topMove.add(m);
                }
            }

        } else if (direction.equals("bottom")){
            ArrayList<SaboteurMove> bottomMove = new ArrayList<>();
            for (SaboteurMove m: legalMoves) {
                if (BottomCards.contains(m.getCardPlayed().getName())){
                    bottomMove.add(m);
                }
            }
        } else if (direction.equals("left")){
            ArrayList<SaboteurMove> leftMove = new ArrayList<>();
            for (SaboteurMove m: legalMoves) {
                if (LeftCards.contains(m.getCardPlayed().getName())){
                    leftMove.add(m);
                }
            }
        } else if (direction.equals("right")){
            ArrayList<SaboteurMove> rightMove = new ArrayList<>();
            for (SaboteurMove m: legalMoves) {
                if (RightCards.contains(m.getCardPlayed().getName())){
                    rightMove.add(m);
                }
            }
        } else{
            ArrayList<SaboteurMove> blockMove = new ArrayList<>();
            for (SaboteurMove m: legalMoves) {
                if (BlockCards.contains(m.getCardPlayed().getName())){
                    blockMove.add(m);
                }
            }
        }

        for (int i = 0; i < legalMoves.size(); i++){
            if (legalMoves.get(i).getCardPlayed().getName().equals("Map")) {
                myMove = legalMoves.get(i);
                return myMove;
            }
//            } else if (legalMoves.get(i).getCardPlayed().getName().equals("Malus")){
//                myMove = legalMoves.get(i);
//                return myMove;
//            } else if (legalMoves.get(i).getCardPlayed().getName().equals("Destroy")){
//                myMove = legalMoves.get(i);
//                return myMove;
//            }
        }

        // Return your move to be processed by the server.
        return myMove;
    }
}