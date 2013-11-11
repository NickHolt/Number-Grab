package core;

/** An abstract move finding engine. This engine is capable of analyzing
 *  a number line in a Number Grab game and making a decision based on the
 *  line's state. This class is extended by different difficulties. 
 * @author Nick Holt */
public abstract class MoveFinder {
    
    /** A new move making engine for a game of Number Grab. 
     *  The MoveMaker generates moves for PLAYER that are 
     *  made on the Number Grab GAME. */
    public MoveFinder(Player player, Game game) {
        _player = player;
        _game = game;
    }
    
    /** Get a move from this MoveMaker based on the game's current state. */
    public abstract int getMove();
    
    /** Get this MoveFinder's player. */
    public Player getPlayer() {
        return _player;
    }
    
    /** Get this MoveFinder's game. */
    public Game getGame() {
        return _game;
    }
    
    /** The player that uses this move maker. */
    private final Player _player;
    
    /** The game that this MoveMaker's player plays on. */
    private final Game _game;

}
