/**
 * 
 */
package core;

import static core.PlayerName.*;

/**
 * @author Nick Holt
 *
 */
public class HumanPlayer extends Player {
    
    /** A human player that plays the Number Grab GAME. */
    public HumanPlayer(Game game, PlayerName name) {
        _game = game;
        setName(name);
    }

    @Override
    public void makeMove(NumberLine numberLine) {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        int move = _game.getMove(); //Get a move from the command line input.
        if (_game.getFrenzied() && stopwatch.getElapsed() > 3000) {
            System.out.println("Too slow! Turn doesn't count!");
        } else {
            if (move != 2) {
                if (name() == P1) {
                    _game.getNumberLine().grab(P1, move);
                } else {
                    _game.getNumberLine().grab(P2, move);
                }
            } else {
                System.out.println("Turn passed.");
            }
        }
        stopwatch.stop();
    }
    
    /** The game that this player is playing. */
    private Game _game;
}
