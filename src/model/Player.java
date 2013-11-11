/**
 * 
 */
package core;

/** A player of a game of Number Grab. Extended by HumanPlayer 
 *  and MachinePlayer. 
 * @author Nick Holt */
public abstract class Player {
    
    /** Make a move from this player. A move is represented by the integer
     * 0 or 1, where 0 takes a number from the left end of the line,
     * and 1 takes a number from the right side of the line. 
     * Move are made on the game NUMBERLINE. */
    public abstract void makeMove(NumberLine numberLine);
    
    /** @return this player's total time taken to make moves. */
    public double getTime() {
        return _time;
    }
    
    /** @param time is the player's new total time taken. */
    public void setTime(double time) {
        _time = time;
    }
    
    /** @return this player's name. */
    public PlayerName name() {
        return _name;
    }
    
    /** Sets this player's name. */
    public void setName(PlayerName name) {
        _name = name;
    }
    
    /** The total time this player has taken to make moves. */
    private double _time = 0;
    
    /** This player's name. */
    private PlayerName _name;

}
