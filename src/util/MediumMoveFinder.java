/**
 * 
 */
package core;

/** A move finding engine with medium difficulty. If there at least 4 
 *  numbers remaining in the number line, the engine considers the two pairs
 *  of numbers formed by the first and second number, and the last
 *  and second to last number. It then picks the move that grabs
 *  the number from the pair with the largest (most positive) difference. 
 *  The difference of the first pair is found by (first - second), and the
 *  the difference for the second pair is found by (last - second to last). 
 *  If only three or fewer numbers are in the line, the larger of the two
 *  options is taken.
 * @author Nick Holt */
public class MediumMoveFinder extends MoveFinder {

    /** A new MediumMoveFinder which finds moves for a PLAYER of
     *  a GAME of Number Grab. */
    public MediumMoveFinder (Player player, Game game) {
        super(player, game);
    }
    
    @Override
    public int getMove() {
        NumberLine numberLine = super.getGame().getNumberLine();
        if (numberLine.size() > 3) {
            int d1 = numberLine.get(0) - numberLine.get(1);
            int d2 = numberLine.get(numberLine.size() - 1)
                    - numberLine.get(numberLine.size() - 2);
            if (d1 > d2) {
                return 0;
            } else {
                return 1;
            }
        }
        if (numberLine.get(0) > numberLine.get(numberLine.size() - 1)) {
            return 0;
        } else {
            return 1;
        }
    }

}
