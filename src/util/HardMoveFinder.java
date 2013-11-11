/**
 * 
 */
package core;

import static core.PlayerName.*;

/** FILL IN
 * @author Nick Holt
 *
 */
public class HardMoveFinder extends MoveFinder {

    /** A new HardMoveFinder which finds moves for a PLAYER of
     *  a GAME of Number Grab. */
    public HardMoveFinder (Player player, Game game) {
        super(player, game);
    }
    
    @Override
    public int getMove() {
        int depth;
        if (getGame().getNumberLine().size() <= 18) {
            depth = getGame().getNumberLine().size();
        } else {
            depth = 9;
        }
        return findBestMove(getPlayer(), getGame().getNumberLine(), depth);
    }
    
    /** Finds the best move for PLAYER on a NUMBERLINE. The best move is found
     *  by choosing the move that results in a difference of at least 4 between 
     *  the number pulled from this move and the largest remaining integer available
     *  to pull in the line. 
     *  If this move does not exist,  choose the move that results in the largest 
     *  (most positive) difference between the player and the opponent's totals 
     *  after DEPTH total moves. Assumes that every player makes the best possible 
     *  move at every step.*/
    private int findBestMove(Player player, NumberLine numberLine, int depth) {
        if (numberLine.size() == 1) {
            return 0;
        }
        int basic = findBestMoveBasic(player, numberLine);
        if (basic != 2) {
            return basic;
        }
        if (depth == 1) { //If player can only move once and cannot look ahead, just pick the biggest.
            if (numberLine.get(0) > numberLine.get(numberLine.size() - 1)) {
                return 0;
            } else {
                return 1;
            }
        }
        Reporter.debug(3, "HardMoveFinder#findBestMove called with depth %d", depth);
        int val0, val1;
        NumberLine line0 = numberLine.copy(), line1 = numberLine.copy(); //Create copies of the number line so that the original isn't altered. 
        line0.grab(player.name(), 0); line1.grab(player.name(), 1); //Make the two possible moves on the copies.
        val0 = -findBestMoveHelper(getGame().opponent(player), line0, depth - 1); //The value of the line is given by negative of the best my opponent can do within depth after this move is made.
        val1 = -findBestMoveHelper(getGame().opponent(player), line1, depth - 1); //See findBestMoveHelper for clarification on "value".
        Reporter.debug(2, "val0: %d  val1: %d", val0, val1);
        if (val0 > val1) {
            return 0; //Pulling the left resulted in a better outcome, this is the best move. 
        } else {
            return 1; // Vice versa
        }
    }
    
    /** The first step of findBestMove. Chooses the move that results in a difference 
     * of at least 4 between the number grabbed from this move and the largest remaining 
     * integer available to grab in the line afterwards. Returns 2 if no such 
     * move is found. Considers a move for PLAYER on NUMBERLINE. */
    private int findBestMoveBasic(Player player, NumberLine numberLine) {
        NumberLine line0 = numberLine.copy(), line1 = numberLine.copy();
        int grab0, grab1, max0, max1, dif0, dif1, bestMove = 0;
        grab0 = line0.grab(player.name(), 0); grab1 = line1.grab(player.name(), 1);
        max0 = Math.max(line0.get(0), line0.get(line0.size() - 1));
        max1 = Math.max(line1.get(0), line1.get(line1.size() - 1));
        dif0 = grab0 - max0; dif1 = grab1 - max1;
        int bestDif = dif0;
        if (dif1 > dif0) {
            bestMove = 1;
            bestDif = dif1;
        }
        if (bestDif >= 4) {
            return bestMove;
        } else {
            return 2;
        }
    }
    
    /** Returns the largest difference in score obtainable by PLAYER
     *  on the NUMBERLINE. Looks DEPTH steps ahead and assumes each player
     *  makes the best possible move at each step. */ 
     private int findBestMoveHelper(Player player, NumberLine numberLine, int depth) {
         if (depth == 1) { //If the player can only move once, pick the biggest. 
             if (numberLine.get(0) > numberLine.get(numberLine.size() - 1)) {
                 numberLine.grab(player.name(), 0);
             } else {
                 numberLine.grab(player.name(), 1);
             }
             if (player.name() == P1) { //Then return the difference in scores. 
                 return numberLine.getP1Total() - numberLine.getP2Total();
             } else {
                 return numberLine.getP2Total() - numberLine.getP1Total();
             }
         }
         NumberLine copy = numberLine.copy();
         while (depth > 0) { //Run a 'simulation' game where each player take turns making the best possible move
             copy.grab(player.name(), findBestMove(player, numberLine, depth));
             player = getGame().opponent(player);
             depth -= 1;
             Reporter.debug(4, "%s", copy);
         }
         if (player.name() == P1) { //Then return the difference in scores. 
             return copy.getP1Total() - numberLine.getP2Total();
         } else {
             return copy.getP2Total() - numberLine.getP1Total();
         }
     }
}
