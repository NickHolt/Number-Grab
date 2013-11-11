package core;

/** Indictes player 1 or player 2. */
public enum PlayerName {
    
    /** The names of the two player. */
    P1, P2;
    
    /** Return the opposing player. */
    PlayerName opponent() {
        return this == P1 ? P2 : P1;
    }

}
