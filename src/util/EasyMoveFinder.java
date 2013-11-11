/**
 * 
 */
package core;

/** A move finding engine with easy difficulty. Selects a move at random, unless
 *  there are only two numbers left, in which case the move that grabs the
 *  larger is selected. 
 * @author Nick Holt */
public class EasyMoveFinder extends MoveFinder {
    
    /** A new EasyMoveFinder which finds moves for a PLAYER of
     *  a GAME of Number Grab. */
    public EasyMoveFinder (Player player, Game game) {
        super(player, game);
    }

    @Override
    public int getMove() {
        Game game = super.getGame();
        NumberLine numberLine = game.getNumberLine();
        if (numberLine.size() == 2) {
            if (numberLine.get(0) > numberLine.get(1)) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return game.getRandom().nextInt(2);
        }
    }

}
