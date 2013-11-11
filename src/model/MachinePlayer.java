/**
 * 
 */
package core;

import static core.PlayerName.*;

/**
 * @author Nick Holt
 *
 */
public class MachinePlayer extends Player {
    
    /** A new MachinePlayer that plays with AI level DIFFICULTY. */
    public MachinePlayer(Game game, PlayerName name, int difficulty) {
        _game = game;
        _name = name;
        if (difficulty == 1) {
            _moveFinder = new EasyMoveFinder(this, game);
        } else if (difficulty == 2) {
            _moveFinder = new MediumMoveFinder(this, game);
        } else {
            _moveFinder = new HardMoveFinder(this, game);
        }
    }

    @Override
    public void makeMove(NumberLine numberLine) {
        int move = _moveFinder.getMove();
        System.out.print("Opponent takes the ");
        if (move == 0) {
            System.out.println("left number.");
        } else {
            System.out.println("right number. ");
        }
        if (name() == P1) {
            _game.getNumberLine().grab(P1, move);
        } else {
            _game.getNumberLine().grab(P2, move);
        }
    }
    
    /** @return this player's name. */
    public PlayerName name() {
        return _name;
    }
    
    /** The game of Number Grab this MachinePlayer is playing. */
    private final Game _game;
    
    /** This MachinePlayer's move decision engine. */
    private final MoveFinder _moveFinder;
    
    /** This player's name. */
    private PlayerName _name;

}
