package student_player;

import Saboteur.SaboteurBoard;
import Saboteur.SaboteurMove;
import Saboteur.cardClasses.SaboteurCard;
import Saboteur.cardClasses.SaboteurDestroy;
import Saboteur.cardClasses.SaboteurDrop;
import Saboteur.cardClasses.SaboteurTile;
import boardgame.Move;

import Saboteur.SaboteurPlayer;
import Saboteur.SaboteurBoardState;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/** A player file submitted by a student. */
public class StudentPlayer extends SaboteurPlayer {


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


        // GET RID OF MALUS
        if (ridMalus(boardState) != null){
            return ridMalus(boardState);
        }

        // PLAY MAP CARD IF GOAL IS NOT OBVIOUS
        if (playMap(boardState) != null){
            return playMap(boardState);
        }

        // REMOVE DEAD END CARDS IF DESTROY AVAILABLE
        // BY COMPARING THE BEST POSITION IF NOT CONSIDERING BLOCK CARDS AND CONSIDERING BLOCK CARDS
        // 1. Not Considering Block Cards
        ArrayList<Double> bestPosNoBlock = MyTools.getBestPosition(boardState.getHiddenBoard(), true, true, true);
        // 2. Considering Block Cards
        ArrayList<Double> bestPosWithBlock = MyTools.getBestPosition(boardState.getHiddenBoard(), false, true, false);

        // IF NOT SAME POSITION ANY MORE, THEN DESTROY DEAD END BLOCK CARD
        if (!bestPosNoBlock.get(0).equals(bestPosWithBlock.get(0)) || !bestPosNoBlock.get(1).equals(bestPosWithBlock.get(1)) ){

            //TODO: MAKE SURE THAT DROPPING DOES NOT ALLOW OTHER TO WIN
            if (!bestPosWithBlock.get(2).equals(0) && removeDeadEnd(boardState, bestPosWithBlock.get(0).intValue(), bestPosWithBlock.get(1).intValue()) != null)
                return removeDeadEnd(boardState, bestPosWithBlock.get(0).intValue(), bestPosWithBlock.get(1).intValue());
        }

        //TODO; GET AND SORT BEST MOVES

        SaboteurMove bestMove = getBestMove(boardState, bestPosNoBlock);
        if (bestMove != null)
            return bestMove;


        //DONE: PUT CASE FOR CLOSE TO END

        //TODO: PUT CASE FOR DROPPING CARDS/MOVE
        if (boardState.getCurrentPlayerCards().size()>0)
            return playDrop(boardState);


        // Is random the best you can do?
        Move myMove = boardState.getRandomMove();

        // Return your move to be processed by the server.
        return myMove;
    }




    private SaboteurMove ridMalus(SaboteurBoardState boardState){
        if (boardState.getNbMalus(boardState.getTurnPlayer()) > 0){
            for (SaboteurMove m: boardState.getAllLegalMoves()){
                if (m.getCardPlayed().getName().contains("Bonus")){
                    return m;
                }
            }
        }
        return null;
    }


    private SaboteurMove playMap(SaboteurBoardState boardState){
        if (MyTools.checkRevealed(boardState.getHiddenBoard()).size() > 1){
            for (SaboteurMove m: boardState.getAllLegalMoves()){
                if (m.getCardPlayed().getName().contains("Map")){
                    return m;
                }
            }
        }
        return null;
    }


    private SaboteurMove removeDeadEnd(SaboteurBoardState boardState, int i, int j){
        ArrayList<SaboteurCard> cards = boardState.getCurrentPlayerCards();

        for (SaboteurCard c: cards){
            if (c.getName().contains("Destroy")){
                SaboteurMove m = new SaboteurMove(new SaboteurDestroy(), i, j, boardState.getTurnPlayer());

                if (boardState.isLegal(m))
                    return m;
            }
        }

        return null;
    }

    private SaboteurMove getBestMove(SaboteurBoardState boardState, ArrayList<Double> currentBestPos){


        ArrayList<SaboteurMove> bestMoves = new ArrayList<>();

        if (currentBestPos.get(2)>1 && currentBestPos.get(2)<3){
            return playDrop(boardState);
        }

        int i_play = currentBestPos.get(0).intValue();
        int j_play = currentBestPos.get(1).intValue();

        // MOVE TO THE CORRECT POSITION CARD IS PLAYED:
        // LEFT
        if (currentBestPos.get(3).intValue()==0)
            j_play--;

        // RIGHT:
        if (currentBestPos.get(3).intValue()==1)
            j_play++;

        // TOP:
        if (currentBestPos.get(3).intValue()==2)
            i_play--;

        // BOTTOM:
        if (currentBestPos.get(3).intValue()==3)
            i_play++;


        for (SaboteurMove m: boardState.getAllLegalMoves()){
            if (currentBestPos.get(0).equals(i_play) && currentBestPos.get(1).equals(j_play)){

                String card = m.getCardPlayed().getName();

                String[] split = card.split(":");

                if (split.length >1 ){
                    card = split[1];
                } else {
                    card = split[0];
                }

                if (BlockCards.contains(card))
                    continue;

                SaboteurTile[][] newBoard = boardState.getHiddenBoard();

                newBoard[i_play][j_play] = new SaboteurTile(card);

                ArrayList<Double> newPos = MyTools.getBestPosition(newBoard, true, false, true);

                if (newPos.get(2) < currentBestPos.get(2)){
                    bestMoves.add(m);
                }

            }
        }


        if (bestMoves.size() == 0 && currentBestPos.get(2) <= 2)
            return playMalusorDestroy(boardState, currentBestPos);
        else
            return bestMoves.get(0);


    }

    private SaboteurMove playDrop(SaboteurBoardState boardState){
        //TODO: IMPLEMENT DROP HIERARCHY

        ArrayList<Integer> destroy = new ArrayList<>();
        ArrayList<Integer> bonus = new ArrayList<>();
        ArrayList<Integer> block = new ArrayList<>();
        ArrayList<Integer> malus = new ArrayList<>();
        ArrayList<Integer> firstdrop = new ArrayList<>();
        ArrayList<Integer> seconddrop = new ArrayList<>();
        ArrayList<Integer> thirddrop = new ArrayList<>();
        ArrayList<Integer> lastdrop = new ArrayList<>();


        for (int k = 0; k<boardState.getPlayerCardsForDisplay(boardState.getTurnPlayer()).size(); k++){
            String card = boardState.getPlayerCardsForDisplay(boardState.getTurnPlayer()).get(k).getName();
            String[] card_split = card.split(":");
            String name = "";
            if (card_split.length  == 1) name = card_split[0];
            else name = card_split[1];

            if (name == "Destroy") destroy.add(k);

            if (name == "Bonus") bonus.add(k);
            if (BlockCards.contains(name)) block.add(k);
            if (name == "malus") malus.add(k);
            if (name.contains("0") || name.contains("10")) firstdrop.add(k);
            if (name.contains("5") || name.contains("7")) seconddrop.add(k);
            if (name.contains("6") || name.contains("9")) thirddrop.add(k);
            if (name.contains("8")) lastdrop.add(k);

        }

        if (block.size() > 0 ) return new SaboteurMove(new SaboteurDrop(), block.get(0),0, boardState.getTurnPlayer());
            //else if (destroy.size() > 1 ) return new SaboteurMove(new SaboteurDrop(), destroy.get(0),0, boardState.getTurnPlayer());
        else if (bonus.size() > 1 ) return new SaboteurMove(new SaboteurDrop(), bonus.get(0),0, boardState.getTurnPlayer());
            //else if (malus.size() > 1 ) return new SaboteurMove(new SaboteurMalus(), 0,0, boardState.getTurnPlayer());
        else if (firstdrop.size() > 1 ) return new SaboteurMove(new SaboteurDrop(), firstdrop.get(0),0, boardState.getTurnPlayer());
        else if (seconddrop.size() > 1 ) return new SaboteurMove(new SaboteurDrop(), seconddrop.get(0),0, boardState.getTurnPlayer());
        else if (thirddrop.size() > 1 ) return new SaboteurMove(new SaboteurDrop(), thirddrop.get(0),0, boardState.getTurnPlayer());
        else if (lastdrop.size() > 1 ) return new SaboteurMove(new SaboteurDrop(), lastdrop.get(0),0, boardState.getTurnPlayer());
        return new SaboteurMove(new SaboteurDrop(), 0,0, boardState.getTurnPlayer());

    }

    private SaboteurMove playMalusorDestroy(SaboteurBoardState boardState, ArrayList<Double> currentBestPos){
        //TODO: IMPLEMENT PLAY MALUS OR DESTROY

        ArrayList<SaboteurMove> legalMoves = boardState.getAllLegalMoves();

        // play malus
        for (SaboteurMove m: legalMoves){
            if (m.getCardPlayed().getName().equals("Malus")){
                return m;
            }
        }

        //Play Destroy
        SaboteurMove play_destroy = new SaboteurMove(new SaboteurDestroy(), currentBestPos.get(0).intValue(), currentBestPos.get(1).intValue(), boardState.getTurnPlayer());
        if (boardState.isLegal(play_destroy)){
            return play_destroy;
        }

        return null;
    }

}