package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Arrays;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private TETile[][] world;

    /** constructor for hex world, intializes 2D world array and
     * fills it with NOTHING tiles
     * @param width
     * @param height
     */
    public HexWorld(int width, int height){
        world = new TETile[width][height];
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    /** method to add a hexagon to the world
     * takes world, side length, x and y position to start drawing and tile type
     * */
    public void addHexagon(TETile[][] world, int side, int xPos, int yPos, TETile type){
        TETile[][] hex = createHexagon(side, type);
        drawHexagon(world, hex, xPos, yPos);
    }

    /** method to create a hexagon.
     * takes input of a side length and tile type
     * creates a 2 dimensional array of tiles in shape of hexagon
     * @param side
     * @return
     */
    private TETile[][] createHexagon(int side, TETile type){
        int height = side * 2;     //number of rows will be twice side length
        int rowSize = side;     //first (and last) row will be side length
        TETile[][] hex = new TETile[height][];

        for(int i = 0; i < side; i++){      //create first half of hex
            hex[i] = new TETile[rowSize];
            rowSize += 2;
        }
        rowSize -= 2;        //so the next row is same size

        for (int j = side; j < height; j++){   //create second half of hex
            hex[j] = new TETile[rowSize];
            rowSize -= 2;
        }

        for (TETile[] row : hex){     //fill in hexagon with tiles of TYPE
            Arrays.fill(row, type);
        }
        return hex;
    }

    /** draws the hexagon on the map
     * takes the 2D array of tiles (the hexagon) and the world as input
     * and an x and y position to start drawing (will start in the bottom
     * left corner of hexagon and build up)
     */
    private void drawHexagon(TETile[][] world, TETile[][] tiles, int xPos, int yPos){
        int x = xPos;
        int y = yPos;
        int startX = xPos;    //x position of beginning of row, it moves each row
        int half = tiles.length / 2;  //splits hexagon into 2 equal halfs

        for (int i = 0; i < half; i++){             //draw first half of hex
            for (int j = 0; j < tiles[i].length; j++){
                world[x][y] = tiles[i][j];
                x += 1;
            }
            startX -= 1;   //move the x position to the right
            x = startX;
            y += 1;
        }
        startX += 1;      //to correct position after last loop
        x = startX;

        for (int i = half; i < tiles.length; i++){      //draw second half of hex
            for (int j = 0; j < tiles[i].length; j++){
                world[x][y] = tiles[i][j];
                x += 1;
            }
            startX += 1;   // move x position back to left
            x = startX;
            y += 1;
        }
    }

    /** creates a map of hexes of different types
     * for the purpose of project, it will be 19 hexes interlocked
     * takes hex size and starting x-y position of bottom left
     */
    public void createHexMap(int hexSize, int xStart, int yStart){
        int colSize = 3;
        int x = xStart;
        int y = yStart;

        for (int i = 0; i < 3; i++){
            addHexColumn(colSize, hexSize, x, y);
            colSize += 1;
            x += hexSize * 2 - 1; //shift next column to the right
            y -= hexSize;   //shift y position down
        }
        colSize -= 2; //adjust after halfway point
        y += hexSize * 2;

        for (int j = 0; j < 2; j++){
            addHexColumn(colSize, hexSize, x, y);
            colSize -= 1;
            x += hexSize * 2 - 1;  //shift column to right
            y += hexSize;   //shift y position up for next column
        }
    }

    /** method to add a column of hexagons
     * can be used to create multiple columns in order to construct map
     * takes column length, hexagon dimensions, and starting
     * x-y position of bottom hex
     */
    private void addHexColumn(int columnSize, int hexSize, int xPos, int yPos){
        int hexHeight = hexSize * 2;

        for (int i = 0; i < columnSize; i++){
            addHexagon(world, hexSize, xPos, yPos, randomTile());
            yPos += hexHeight;
        }
    }


    /** select a random tile type (from 6 nature tiles) */
    private static TETile randomTile() {
        Random ran = new Random();
        int tileNum = ran.nextInt(6);
        switch (tileNum) {
            case 0: return Tileset.MOUNTAIN;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.WATER;
            case 3: return Tileset.TREE;
            case 4: return Tileset.SAND;
            case 5: return Tileset.GRASS;
            default: return Tileset.NOTHING;
        }
    }

    public static void main(String[] args){
        int WIDTH = 70;
        int HEIGHT = 70;
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        HexWorld hexWorld = new HexWorld(WIDTH, HEIGHT);
        TETile[][] world = hexWorld.world;

        hexWorld.createHexMap(3, 15, 20);

        ter.renderFrame(world);
    }
}
