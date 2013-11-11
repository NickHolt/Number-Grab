package core;


public class Main {

    /** The main engine for Number Grab. ARGS is as described in 
     *  {@link Main.usage}. */
    public static void main(String... args) {
        String options = "--rules --debug= --seed= --length= --max= --frenzy " +
        		"--time= --passing --ai --player2 --difficulty= --gui";
        CommandArgs cArgs = new CommandArgs(options, args);
        if (!cArgs.ok()) {
            usage(); //Print usage instructions if input was not correct.
        }
        
        /* Initialize default variables. */
        int debug = 0, seed = -1, length = 20, max = 10, difficulty = 2;
        double time = Double.POSITIVE_INFINITY;
        boolean frenzied = false, passing = false, timed = false
                , singlePlayer = false, isPlayer2 = false, guiEnabled = false;
        
        /* Process command line arguments. */
        if (cArgs.containsKey("--rules")) {
            rules();
        }
        if (cArgs.containsKey("--debug")) {
            debug = cArgs.getInt("--debug");
            if (debug < 0) {
                usage();
            }
        }
        if (cArgs.containsKey("--seed")) {
            seed = cArgs.getInt("--seed");
            if (seed < 0) {
                usage();
            }
        }
        if (cArgs.containsKey("--length")) {
            length = cArgs.getInt("--length");
            if (length < 0) {
                usage();
            }
        }
        if (cArgs.containsKey("--max")) {
            max = cArgs.getInt("--max");
            if (max < 1) {
                usage();
            }
        }
        if (cArgs.containsKey("--frenzy")) {
            frenzied = true;
        }
        if (cArgs.containsKey("--time")) {
            time = cArgs.getInt("--time");
            timed = true;
            if (time <= 0) {
                usage();
            } else if (timed && frenzied) {
                usage();
            }
        }
        if (cArgs.containsKey("--passing")) {
            passing = true;
        }
        if (cArgs.containsKey("--ai")) {
            singlePlayer = true;
        }
        if (cArgs.containsKey("--player2")) {
            isPlayer2 = true;
            if (!singlePlayer) {
                usage(); //player2 switch cannot be enabled for 2 player game.
            }
        }
        if (cArgs.containsKey("--difficulty")) {
            difficulty = cArgs.getInt("--difficulty");
            if (difficulty < 1 || difficulty > 3) {
                usage();
            }
        }
        if (cArgs.containsKey("--gui")) {
            guiEnabled = true;
        }
        Reporter.setMessageLevel(debug);
        Game game = new Game(seed, length, max, time, difficulty
                , frenzied, timed, passing, singlePlayer, isPlayer2, guiEnabled);
        game.play();

    }
    
    /** A description of the game rules. */
    public static void rules() {
        System.out.print("\n--GAME RULES--\n" +
        		"Welcome to Number Grab! The aim of the game is to \n" +
        		"outsmart and outcompete your opponent. The rules are simple. \n" +
        		"The game board is line of numbers, and you and your \n" +
        		"opponent take turns grabbing a number from either end of the \n" +
        		"number line and adding that number to your totals. Whoever has \n" +
        		"the largest total when the line is empty wins! Be careful, \n" +
        		"just picking the biggest number isn't the best strategy!\n" +
        		"To make a move, simply enter 0 to grab a number from the left\n" +
        		"side of the line, 1 to grab one from the right, or 2 to pass\n" +
        		"the turn if passing is enabled. You can also enter c to view \n" +
        		"a list of commands.\n\n");
    }
    
    /** A description of the command line format. */
    public static void usage() {
        System.out.print("\n--Command line usage instructions--\n" +
        		"Please use the following parameter format (IN THIS ORDER): \n" +
        		"[ --rules] [ --debug=N ] [ --seed=S ] [ --length=L ] [ --max=M ] \n" +
        		"[ --frenzy ] [ --time=T ] [ --passing ] [ --ai ] [ --difficulty=D ] [ --gui ]\n" +
        		"Where square brackets indicate optional parameters. " +
        		"An example is: \n" +
                "\"java core.Main --rules --ai --difficulty=1\" \n" +
                "(Use this example if it's your first time playing!)\n\n" +
        		"The parameter values are:\n" +
        		"-rules prints the game rules. \n" +
        		"-N >= 0 and sets the debug output level. 0 by default.\n" +
        		"-S is the seed of the game's random number generator.\n" +
        		"-L is the length of game's number line. 20 by default.\n" +
        		"-M is the maximum value of the numbers in the line. 10 by default.\n" +
        		"-frenzy enables frenzy mode. In frenzy mode, players will have " +
        		"only three seconds to make a decision or the move won't count. Off by default.\n" +
        		"-T is the TOTAL amount of time a player can use to make moves." +
        		" No time limit by default.\n" +
        		"THE TIME OPTION CANNOT BE USED WITH FRENZY MODE.\n" +
        		"-passing enables turn passing. \n" +
        		"-ai enables an AI opponent. Off by default.\n" +
        		"-player2 sets the human player as player2. Note that player1 moves first. AI MUST BE ENABLED.\n" +
        		"-D sets the difficulty of the AI opponent. 1 is easy, 2 is medium " +
        		"and 3 is hard. 2 by default.\n" +
        		"-gui enables the GUI for this game. Not currently implemented.\n");
        System.exit(1);
    }

}
