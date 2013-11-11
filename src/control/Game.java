/**
 * 
 */
package core;

import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static core.PlayerName.*;

/** The primary game engine for Number Grab. Runs the game and handles
 *  command line input from all players. 
 * @author Nick Holt */
public class Game {

    /** A new game of Number Grab. 
     * @param seed the random number generator seed.
     * @param length the number line length.
     * @param max the maximum number value.
     * @param time the total time alloted to a player's moves.
     * @param difficulty the ai difficulty.
     * @param frenzied frenzy mode switch. 
     * @param timed timed mode switch. 
     * @param passing is true iff this game allows turn passing. 
     * @param singlePlayer ai enabled switch.
     * @param player2 indicates that the human player is player2. 
     * @param guiEnabled gui enabled switch. 
     */
    public Game(int seed, int length, int max, double time,
            int difficulty, boolean frenzied, boolean timed, boolean passing,
            boolean singlePlayer, boolean isPlayer2, boolean guiEnabled) {
        _seed = seed; _length = length; _max = max; _time = time;
        _difficulty = difficulty;
        _frenzied = frenzied; ;_passing = passing; _timed = timed;
        _singlePlayer = singlePlayer; _isPlayer2 = isPlayer2;
        _guiEnabled = guiEnabled;
        if (seed < 0) {
            _random = new Random();
        } else {
            _random = new Random(_seed);
        }
        _random = new Random(_seed);
        if (_singlePlayer) {
            if (_isPlayer2) {
                _player2 = new HumanPlayer(this, P2);
                _player1 = new MachinePlayer(this, P1, _difficulty);
            } else {
                _player1 = new HumanPlayer(this, P1);
                _player2 = new MachinePlayer(this, P2, _difficulty);
            }
        } else {
            _player1 = new HumanPlayer(this, P1); _player2 = new HumanPlayer(this, P2);
        }
        _numberLine = new NumberLine(_length, _max, _random
                , _player1, _player2);
    }
    
    /** Gets a move from the command line and returns it. */
    public int getMove() {
        System.out.printf("%s's move > ", _numberLine.turn());
        System.out.flush();
        Pattern pat = Pattern.compile("\\s*([012])\\s*");
        String input = inp.nextLine();
        Matcher mat = pat.matcher(input);
        if (mat.matches()) {
            if (input.equals("2") && !getPassing()) {
                System.out.println("Passing is not allowed.");
                return getMove();
            }
            return Integer.parseInt(mat.group(1));
        } else {
            return getCommand(input);
        }
    }
    
    /** Gets a command from the command line and processes it, then
     *  gets a move and returns it.  
     *  This is a utility method for Game.getMove.*/
    private int getCommand(String oldInput) {
        Scanner scanner = new Scanner(oldInput); //This is done to allow whitespace before the command
        String input = scanner.next().toLowerCase(); //Ignore case
        if (input.equals("")) { //Begins handling of different recognized commands.
            return getMove();
        } else if (input.charAt(0) == 'q') {
            System.out.printf("%s has quit.\n", _numberLine.turn());
            System.exit(1);
        } else if (input.charAt(0) == 'n') {
            System.out.printf("%s has started a new game.\n", _numberLine.turn());
            Game newGame = new Game(getRandom().nextInt(), _length, _max, _time,
            _difficulty, _frenzied, _timed, _passing,
            _singlePlayer, _isPlayer2, _guiEnabled);
            newGame.play();
        } else if (input.charAt(0) == 't') {
            System.out.printf("Player 1 has %.2f seconds left " +
            		"and Player 2 has %.2f seconds left.\n"
                    , _time - _player1.getTime(), _time - _player2.getTime());
        } else if (input.charAt(0) == 'p') {
            System.out.printf("\n--Game Parameters:\n" +
            		"RNG Seed: %d.\n" +
            		"Initial line length: %d\n" +
            		"Maximum number value: %d\n" +
            		"Total time allowed: %.2f\n" +
            		"AI difficulty: %d\n\n"
            		, _seed, _length, _max, _time, _difficulty);
        } else if (input.charAt(0) == 'r') {
            Main.rules();
        } else if (input.charAt(0) == 's') {
            System.out.println(getNumberLine());
        } else if (input.charAt(0) == 'c') {
            System.out.print("\nCommands:\n" +
            		"0 : grab a number from the left.\n" +
            		"1 : grab a number from the right.\n" +
            		"2 : pass the turn (if enabled).\n" +
            		"q : quit the game and terminate the program.\n" +
            		"n : quit and start a new game.\n" +
            		"t : show the remaining time for both players. \n" +
            		"p : show game parameters.\n" +
            		"r : print the game rules.\n" +
            		"s : print the number line.\n\n");
        } else {
            System.out.println("Enter 0, 1 or 2 to make a move," +
                    " or 'c' for a list of commands.");
        }
        return getMove();
    }
    
    /** Play this game. */
    public void play() {
        inp = new Scanner(System.in);
        Stopwatch stopwatch = new Stopwatch();
        while (!gameOver()) {
            System.out.println(_numberLine);
            if (_timed) stopwatch.start(); //Start the stopwatch if mode is enabled
            _player1.makeMove(_numberLine); //Get a move from player1 and perform it
            if (_timed) { //If timed mode is enabled, add the elapsed time to the players running total and reset the stopwatch. 
                _player1.setTime(_player1.getTime() +
                        (double) stopwatch.getElapsed() / (double) 1000); //Stopwatch gives time in milliseconds, so divide by 1000 to give seconds
                stopwatch.stop();
                stopwatch.reset();
            }
            System.out.println(_numberLine);
            if (gameOver()) { //Check again to see if the game is over. 
                break;
            }
            _numberLine.swap(); //Swap NumberLine control to player2. 
            if (_timed) stopwatch.start(); //Perform similar timed mode procedure to above.
            _player2.makeMove(_numberLine);
            if (_timed) {
                _player2.setTime(_player2.getTime() +
                        (double) stopwatch.getElapsed() / (double) 1000);
                stopwatch.stop();
                stopwatch.reset();
            }
            _numberLine.swap(); //Swap NumberLine control back to player1. 
        }
        boolean timeCheck = false; //A switch to keep from printing redundant / conflicting / incorrect victory statements. 
        if (_timed) {              //Keeps the program from declaring a player the winner if they have a higher score even if they ran out of time.
            if (_player1.getTime() >_time && _player2.getTime() > _time) {
                System.out.println("Both players have run out of time! You all lose!");
                timeCheck = true;
            } else if (_player1.getTime() > _time) {
                System.out.println("Player 1 has run out of time! Player 2 wins!");
                timeCheck = true;
            } else if (_player2.getTime() > _time) {
                System.out.println("Player 2 has run out of time! Player 1 wins!");
                timeCheck = true;
            }
        }
        if (!timeCheck) {
            if (getNumberLine().getP1Total() > getNumberLine().getP2Total()) {
                System.out.println("\nPlayer 1 wins!");
            } else if (getNumberLine().getP1Total() < getNumberLine().getP2Total()) {
                System.out.println("\nPlayer 2 wins!");
            } else {
                System.out.println("\nIt's a tie! Everyone loses!");
            }
        }
        System.out.printf("Final scores: P1 - %d\n" +
        		"              P2 - %d\n\n", getNumberLine().getP1Total()
        		, getNumberLine().getP2Total());
        System.exit(1);
    }
    
    /** @return if this game is over. */
    private boolean gameOver() {
        if (_numberLine.isEmpty()) {
            return true; //Game is over if line has no more numbers.
        } else if (_timed) { //Only check this if timed mode is enabled.
            return _player1.getTime() > _time && _player2.getTime() > _time; //Ensure both players have time left
        } else {
            return false; //Serves as the counterpart to the first 'if' clause, since it will never be reached if timed mode is enabled. 
        }
        
    }
    
    /** @return the opponent of PLAYER. */
    public Player opponent(Player player) {
        if (player.name() == P1) {
            return _player2;
        } else {
            return _player1;
        }
    }
    
    /**@return true iff this game has frenzy mode enabled. */
    public boolean getFrenzied() {
        return _frenzied;
    }
    
    /** @return this game's random number generator. */
    public Random getRandom() {
        return _random;
    }
    
    /** @return true iff this game allows turn passing. */
    public boolean getPassing() {
        return _passing;
    }
    
    /** @return true iff this game is timed. */
    public boolean getTimed() {
        return _timed;
    }
    
    /** @return player 1. */
    public Player getPlayer1() {
        return _player1;
    }
    
    /** @return player 2. */
    public Player getPlayer2() {
        return _player2;
    }
    
    /** @return the NumberLine this game uses. */
    public NumberLine getNumberLine() {
        return _numberLine;
    }
    
    
    /* Game variables. */
    private int _seed, _length, _max, _difficulty;
    private double _time;
    private boolean _frenzied, _passing, _timed, _singlePlayer, _isPlayer2
        , _guiEnabled;
    private Random _random;
    
    /** This game's input scanner. */
    private Scanner inp;
    
    /** The players of Number Grab. */
    private Player _player1, _player2;
    
    /** The NumberLine with which the game is played. */
    private NumberLine _numberLine;


}
