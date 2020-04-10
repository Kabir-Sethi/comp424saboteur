package student_player;


import Saboteur.SaboteurMove;
import boardgame.Move;

import Saboteur.SaboteurPlayer;
import Saboteur.SaboteurBoardState;

import java.util.ArrayList;

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

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(SaboteurBoardState boardState) {
        // You probably will make separate functions in MyTools.
        // For example, maybe you'll need to load some pre-processed best opening
        // strategies...
        //MyTools.getSomething();

        ArrayList<SaboteurMove> moves = boardState.getAllLegalMoves();

        // RADNOM MOVE PLACEHOLDER
        Move myMove = boardState.getRandomMove();


        //boardState.printBoard();

        double[] ret = MyTools.calcBestPos(boardState);


        System.out.println(ret[2]);

        // Return your move to be processed by the server.
        return myMove;
    }
}