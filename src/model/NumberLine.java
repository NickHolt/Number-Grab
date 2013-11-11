package core;

import java.util.LinkedList;
import java.util.Random;
import static core.PlayerName.*;

/** The number line for a game of Number Grab. The NumberLine is responsible for
 *  keeping track of the game's numbers and the player's scores. 
 * @author Nick Holt */
public class NumberLine {
    
    /** A new number line for a game of Number Grab. 
     * @param length is the length of the number line.
     * @param max is the maximum value of the numbers in this line. 
     * @param random is the RNG used to populate the list. 
     * @param player1 is the first player of Number Grab. This player moves first.
     * @param player2 is the second player of Number Grab.*/
    public NumberLine(int length, int max, Random random
            , Player player1, Player player2) {
        for (int i = length; i > 0; i--) {
            _numbers.add(random.nextInt(max) + 1); //Add a random number in the allowed range to the line. 
        }
        _controllingPlayer = "P1"; _player1 = player1;
        _player2 = player2;
    }
    
    /** A new number line with no numbers for a game of Number Grab.
     * @param player1 is the first player of Number Grab. This player moves first.
     * @param player2 is the second player of Number Grab.
     * If this constructor is used, the list must be manually populated
     * using {@link NumberLine.setNumbers}. */
    public NumberLine(Player player1, Player player2) {
        _player1 = player1;
        _player2 = player2;
    }
    
    /** Pulls a number from the NumberLine and returns it. 
     * @param side indicates which side to pull the number from.
     * 0 pulls from the left, 1 pulls from the right. */
    public int grab(PlayerName name, int side) {
        Reporter.debug(1, "%s grabs with move %d", name, side);
        assert side == 0 || side == 1 : _numbers.size() > 0;
        int num;
        if (side == 0) {
           num = _numbers.removeFirst();
            if (name == P1) {
                setP1Total(getP1Total() + num);
            } else {
                setP2Total(getP2Total() + num);
            }
        } else {
            num = _numbers.removeLast();
            if (name == P1) {
                setP1Total(getP1Total() + num);
            } else {
                setP2Total(getP2Total() + num);
            }
        }
        return num;
    }
    
    /** @return the nth number in this number line. The first
     *  number is n = 0. */
    public int get(int n) {
        return _numbers.get(n);
    }
    
    /** @return the number of numbers in this NumberLine. */
    public int size() {
        return _numbers.size();
    }
    
    /** @return if this NumberLine is empty. */
    public boolean isEmpty() {
        return size() == 0;
    }
    
    /** @return a copy of this NumberLine, such that any changes
     *  made to the copy do not effect this NumberLine. Works in 
     *  O(N) time, where N is the number of numbers remaining in 
     *  the NumberLine. */
    public NumberLine copy() {
        NumberLine result = new NumberLine(_player1, _player2);
        LinkedList<Integer> numbers = new LinkedList<Integer>();
        for (int n : _numbers) {
            numbers.add(n);
        }
        result.setNumbers(numbers);
        result.setP1Total(getP1Total());
        result.setP2Total(getP2Total());
        return result;
    }
    
    @Override
    public String toString() {
        if (_numbers.size() == 0) {
            return "";
        }
        String front = "< " + String.valueOf(_numbers.getFirst()) + " || ";
        String middle = "";
        for (int i = 1; i < _numbers.size() - 1; i++) {
            middle += String.valueOf(_numbers.get(i)) + " | ";
        }
        if (middle.length() > 1) {
            middle = middle.substring(0, middle.length() - 2);
        } else {
            middle = "";
        }
        String end = "|| " + String.valueOf(_numbers.getLast()) + " >";
        String p1 = "[P1: " + String.valueOf(getP1Total()) + "]";
        String p2 = "[P2: " + String.valueOf(getP2Total()) + "]";
        if (_numbers.size() == 2) {
            return p1 + p2 + "  " + front + end.substring(3, end.length());
        } else if (_numbers.size() == 1) {
            return p1 + p2 + "  " + front.substring(0, front.length() - 3) + ">";
        } else {
            return p1 + p2 + "  "  + front + middle + end;
        }
    }
    
    /** Swap turns on this line. */
    public void swap() {
        if (_controllingPlayer.equals("P1")) {
            _controllingPlayer = "P2";
        } else {
            _controllingPlayer = "P1";
        }
    }
    
    /** @return the player who currently has a turn. */
    public String turn() {
        return _controllingPlayer;
    }
    
    /** @return the LinkedList used to hold this NumberLine's numbers. */
    public LinkedList<Integer> getNumbers() {
        return _numbers;
    }
    
    /** Set the LinkedList used to hold this NumberLine's numbers. */
    public void setNumbers(LinkedList<Integer> numbers) {
        _numbers = numbers;
    }
    
    /** @return player 1's total. */
    public int getP1Total() {
        return _p1Total;
    }
    
    /** Set player 1's total. */
    private void setP1Total(int total) {
        _p1Total = total;
    }
    
    /** @return player 2's total. */
    public int getP2Total() {
        return _p2Total;
    }
    
    /** Set player 2's total. */
    private void setP2Total(int total) {
        _p2Total = total;
    }

    /** The sequence of numbers of this NumberLine. */
    private LinkedList<Integer> _numbers = new LinkedList<Integer>(); //Implementation uses a LinkedList for O(1) removal of items.

    /** A string representing the player who currently has a turn. */
    private String _controllingPlayer;
    
    /** The players that play on this number line. */
    private final Player _player1, _player2;
    
    /** The totals of player 1 and player 2. */
    private int _p1Total = 0, _p2Total = 0;
}
