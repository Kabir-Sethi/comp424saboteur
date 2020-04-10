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

        Move myMove = boardState.getRandomMove();
        ArrayList<SaboteurMove> legalMoves = boardState.getAllLegalMoves();

        //for (SaboteurMove.)


        if (MyTools.checkRevealed(boardState).size() > 1){
            // PLAY MAP CARD AND OTHERWISE DON'T
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
        double[] bestPos = MyTools.calcBestPos(boardState);

        //case: cannot play best move, must check if close to goal:
        if (bestPos[3] < 2){
            //CASE: close to goal and cannot play a finishing move

        }

        // Return your move to be processed by the server.
        return myMove;
    }
}