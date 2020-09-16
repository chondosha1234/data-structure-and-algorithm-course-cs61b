package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Engine {
    TERenderer ter;
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    World world;
    public Point savedPosition;
    public int savedSeed;
    public Input input;
    public boolean gameOver;

    public Engine(){
        ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        gameOver = false;
    }
    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        InputKeyboard IKD = new InputKeyboard();
        input = IKD;
        char nextChar;
        ter.startMenu(this);      //start menu method needs engine for process input method (which takes input type)
        while (IKD.possibleNextInput() && !gameOver) {
            ter.setUI(world);
            nextChar = IKD.getNextKey();
            processInput(String.valueOf(nextChar), IKD);
            ter.renderFrame(world.getWorld());
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        InputString ISD = new InputString(input);  //input string device
        this.input = ISD;
        StringBuilder str = new StringBuilder(input.length());
        while(ISD.possibleNextInput()){
            char next = ISD.getNextKey();
            str.append(next);
        }
        processInput(str.toString(), ISD);
        ter.renderFrame(world.getWorld());
        return world.getWorld();
    }

    /** takes a TETile grid and saves it to a text file to be called upon later when game is loaded
     *
     * @param currentGrid   a STRING that represents the whole TETile grid
     */
    private void saveGame(TETile[][] currentGrid){
        Point p = world.getPlayer().getPosition();
        int x = p.getX();
        int y = p.getY();
        try {
            FileWriter writer = new FileWriter("SaveFiles.txt");  //create file / overwrite file
            writer.write(x + "\n");
            writer.write(y + "\n");
            writer.write(world.getSeed() + "\n");
            writer.write(TETile.toString(currentGrid));     //write the string version of grid
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /** load a TETile grid from SavedFiles.txt
     * only one save can be in the file at a time
     * @return    TETile grid that represents the world state when save/quit last time
     */
    public TETile[][] loadGame(){
        TETile[][] savedGrid = new TETile[WIDTH][HEIGHT];   //new grid to be returned, Height minus 5 for HUD
        try {
            BufferedReader reader = new BufferedReader(new FileReader("SaveFiles.txt"));
            String line = reader.readLine();    //read first line with player position data
            int x = Integer.parseInt(line);     //x position
            line = reader.readLine();
            int y = Integer.parseInt(line);     //y position
            savedPosition = new Point(x, y);
            line = reader.readLine();           //read second line, the seed from saved world
            savedSeed = Integer.parseInt(line);
            line = reader.readLine();

            int lineNumber = HEIGHT - 1;      //line number 1 will be the end of the array
            while (line != null){           //go through each line
                processLine(line, savedGrid, lineNumber);  //method to convert char into TETile
                lineNumber -= 1;
                line = reader.readLine();   //read next line
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return savedGrid;
    }

    /** take a line of input from a text file (the save file) and look at each character
     * compare that character to Tileset characters and retrieve the needed tile
     * @param line    String of characters
     * @param grid    the return grid
     * @param lineNumber   HEIGHT index (saved file starts reading from the top, grid starts from bottom)
     */
    private void processLine(String line, TETile[][] grid, int lineNumber){
        for (int i = 0; i < line.length(); i++){
            TETile tile = Tileset.charToTile(line.charAt(i));   //take 1 char from string line, translate to tile
            grid[i][lineNumber] = tile;                     //set that tile to its position
        }
    }

    /** takes a string S and processes it
     * can be used with InputString and will analyze the entire stirng,
     * keyboard input will send 1 character at a time
     * @param s
     */
    public void processInput(String s, Input inputType){
        char next;
        boolean isPressed = false;
        for (int i = 0; i < s.length(); i++){
            next = s.charAt(i);
            if (next == 'n'){               //N means create new world
                StringBuilder seed = new StringBuilder();
                i += 1;
                next = s.charAt(i);
                while (next != 's'){      //get each digit before s
                    seed.append(next);    //add digit to string
                    i += 1;
                    next = s.charAt(i);
                }
                int realSeed = Integer.parseInt(String.valueOf(seed));   //convert string to integer
                world = new World(WIDTH, HEIGHT, realSeed);     //height minus 5 to give room for HUD
                world.generateRandomWorld();
            }else if (next == 'l'){          // 'l' key loads the game
                world = new World(WIDTH, HEIGHT, 0);     //make a new world (for new game especially)
                TETile[][] saved = loadGame();       //use load game method
                world.setWorld(saved);
                Player player = world.getPlayer();        //get the player from the new world
                player.setWorld(saved);
                player.initialize(savedPosition);   //set the player position to the loaded game's player position
                world.setSeed(savedSeed);         //set the loaded game's seed to the current game
                ter.renderFrame(world.getWorld());
            }

            switch(next){
                case 'a': world.player.toWest();    break;
                case 'd': world.player.toEast();    break;
                case 'w': world.player.toNorth();   break;
                case 's': world.player.toSouth();   break;
                default: break;
            }
            world.updatePlayerPosition();
            if (world.player.getPosition().equals(world.getEndPoint())){
                gameOver();
            }

            if (inputType instanceof InputString){
                if (next == ':'){
                    isPressed = true;
                }
                if (next == 'q' && isPressed){
                    saveGame(world.getWorld());
                }
            }else {
                if (next == ':') {
                    while(true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            next = StdDraw.nextKeyTyped();
                            break;
                        }
                    }
                    if (next == 'q') {
                        saveGame(world.getWorld());
                        System.exit(0);
                    }
                }
            }
        }
    }

    /** set gameover variable when player reaches locked door
     *
     */
    private void gameOver(){
        gameOver = true;
        ter.gameOver();
    }
}
