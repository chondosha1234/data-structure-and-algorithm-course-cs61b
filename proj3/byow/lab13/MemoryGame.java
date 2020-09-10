package byow.lab13;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

import static byow.Core.RandomUtils.uniform;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private char[] mode;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] RUSSIAN = "йцукенгшщзхъфывапролджэячсмитьбю".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        rand = new Random(seed);
    }

    /** set game mode to English or Russian
     *
     * @param c  the user input to choose mode
     */
    private void chooseMode(char c){
        char choice = Character.toLowerCase(c);
        if (choice == 'r'){
            mode = RUSSIAN;
        }else{
            mode = CHARACTERS;
        }

    }

    /** generate random string of length N
     *
     * @param n  length of return string
     * @return    string
     */
    public String generateRandomString(int n) {
        StringBuilder s = new StringBuilder(n);
        int index;
        for (int i = 0; i < n; i++){
            index = uniform(rand, 0, mode.length);
            s.append(mode[index]);
        }
        return s.toString();
    }

    /** take random string and display it in middle of frame
     * shows 1 letter at a time and pauses for 3 seconds before moving
     * to next letter
     * @param s    randomly generated string
     */
    public void drawFrame(String s) {
        StdDraw.clear(Color.BLACK);
        setUI();
        setEncouragement();
        playerTurn = false;
        flashSequence(s);
    }

    /** take random string and display it in middle of frame
     * shows 1 letter at a time and pauses for 3 seconds before moving
     * to next letter
     */
    public void flashSequence(String letters) {
        StdDraw.text(width/2, height/2, "Round: " + round);
        StdDraw.show();
        StdDraw.pause(2000);
        for (int i = 0; i < letters.length(); i++) {
            String nextChar = String.valueOf(letters.charAt(i));
            StdDraw.clear(Color.BLACK);
            setUI();
            setEncouragement();;
            StdDraw.text(width/2, height/2, nextChar);
            StdDraw.show();
            StdDraw.pause(1000);
            StdDraw.clear(Color.BLACK);
            setUI();
            setEncouragement();
            StdDraw.pause(500);
        }
    }

    /** reset the constant UI after each time another method clears
     * the board
     */
    private void setUI(){
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.line(0, height - 3, width, height - 3);
        StdDraw.text(5, height - 1, "Round: " + round);
        if (playerTurn){
            StdDraw.text(17, height - 1, "Type!");
        }else{
            StdDraw.text(17, height - 1, "Watch!");
        }
        StdDraw.show();
    }

    /** set the encouragement message.  if it is set every time
     * setUI is called, it will be too many times
     */
    private void setEncouragement(){
        int num = uniform(rand, 0, ENCOURAGEMENT.length);
        String encourage = ENCOURAGEMENT[num];
        StdDraw.text(30, height - 1, encourage);
        StdDraw.show();
    }

    /** read letters of player input
     * display typed letters in middle of screen as player types
     * @param n   number of letters needed to be typed
     * @return    the completed string after N characters
     */
    public String solicitNCharsInput(int n) {
        StringBuilder s = new StringBuilder(n);
        char nextChar;
        playerTurn = true;
        while (n > 0){
            if (StdDraw.hasNextKeyTyped()){
                nextChar = StdDraw.nextKeyTyped();
                s.append(nextChar);
                n--;
            }
            StdDraw.clear(Color.BLACK);  // causing flash?
            setUI();
            StdDraw.text(width/2, height/2, s.toString());
            StdDraw.show();
        }
        setEncouragement();
        return s.toString();
    }

    private void modeMenu(){
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(width/2, height/2, "(E) English mode");
        StdDraw.text(width/2, height/2 - 2, "(R) Russian mode");
        StdDraw.show();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char choice = StdDraw.nextKeyTyped();
                chooseMode(choice);
                return;
            }
        }
    }

    /** method to start the game, sets initial variables and has a loop until gameOver is reached
     * generates a string, draws a frame, solicits user input, then checks if gameOver
     */
    public void startGame() {
        gameOver = false;
        round = 1;
        String randString, answer;
        modeMenu();

        while (!gameOver){
            randString = generateRandomString(round);
            drawFrame(randString);
            answer = solicitNCharsInput(round);
            gameOver = !answer.equals(randString);
            round++;
        }
        StdDraw.clear(Color.BLACK);
        StdDraw.text(width/2, height/2, "GAME OVER!");
        StdDraw.show();
        StdDraw.pause(3000);
    }

}
