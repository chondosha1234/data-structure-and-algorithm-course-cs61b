package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static byow.Core.RandomUtils.uniform;
import static byow.Core.Constants.*;

public class World {
    Player player;
    TETile[][] world;
    int worldWidth, worldHeight;
    double worldSize;
    double percentFilled;
    Random rand;
    int SEED;
    List<Door> doorList;  //list of hash maps which hold doors and their string description
    List<Door> openDoors;

    /** constructor for a new World
     * it takes a width and height for the world, and fills the area with NOTHING tiles
     * also sets instance variables WORLDSIZE, PERCENTFILLED
     * @param width      width dimension of world
     * @param height     height dimension of world
     */
    public World(int width, int height, int SEED){
        world = new TETile[width][height];
        worldWidth = width;
        worldHeight = height;
        worldSize = width * height;
        percentFilled = 0.0;
        this.SEED = SEED;
        rand = new Random(SEED);
        doorList = new ArrayList<>();
        openDoors = new ArrayList<>();
        player = new Player(this);   //put this world itself into constructor

        //fill world map with NOTHING tiles first
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    /** method to hold algorithm to generate a world randomly.
     * it will update the 2D world array, and use the RAND variable in all subsequent random operations
     */
    public void generateRandomWorld(){
        Door nextDoor;
        boolean reverse = false;   //first room never needs to be reverse
        boolean hallOrientation;
        Point start = selectRandomPoint();
        Hall nextHall;
        Room nextRoom = createRandomRoom(start.getX(), start.getY(), reverse);
        getDoors(nextRoom);
        drawArea(nextRoom);
        percentFilled = nextRoom.getArea() / worldSize;
        Point p = placePlayer(start);      //player's start position
        player.initialize(p);

        while (percentFilled < 0.55){
            nextDoor = selectDoor();
            hallOrientation = checkOrientation(nextDoor);
            reverse = checkReverse(nextDoor);
            start = calculateHallStartPoint(nextDoor);
            nextHall = createRandomHall(start.getX(), start.getY(), reverse, hallOrientation);
            nextHall.setStartSide(nextDoor.getOppositeSide());
            if (checkSpace(nextHall)) { //make sure it wont run into anything
                drawArea(nextHall);
                getDoors(nextHall);
                removeDoor(nextDoor);     //maybe change
                openDoors.add(nextDoor);
                percentFilled += nextHall.getArea() / worldSize;
            }

            nextDoor = selectDoor();
            reverse = checkReverse(nextDoor);
            //start = calculateRoomStartPoint(nextDoor);
            start = calculateHallStartPoint(nextDoor);
            nextRoom = createRandomRoom(start.getX(), start.getY(), reverse);
            nextRoom.setStartSide(nextDoor.getOppositeSide());
            if (checkSpace(nextRoom)) {  //make sure it won't run into anything
                drawArea(nextRoom);
                getDoors(nextRoom);
                removeDoor(nextDoor);   // maybe change
                openDoors.add(nextDoor);
                percentFilled += nextRoom.getArea() / worldSize;
            }
        }
        openDoors();
    }

    /** returns the Tile array of the world to other classes
     *
     * @return   2D TETile array
     */
    public TETile[][] getWorld(){
        return world;
    }

    /** set world grid to this grid
     *
     * @param grid TETile grid, probably from load game
     */
    public void setWorld(TETile[][] grid){
        world = grid;
    }

    /** return the random seed of world for load games
     *
     * @return  integer for seed
     */
    public int getSeed(){
        return SEED;
    }

    /** set the world's random seed
     *
     * @param seed  integer, probably from load game
     */
    public void setSeed(int seed){
        SEED = seed;
    }

    /** get the player object associated with this world instance
     *
     * @return  Player object
     */
    public Player getPlayer(){
        return player;
    }

    /** place your player avatar at a point in the first room
     *
     * @param p   first start point
     */
    private Point placePlayer(Point p){
        int x = p.getX() + 2;
        int y = p.getY() + 2;
        world[x][y] = Tileset.AVATAR;
        return new Point(x, y);
    }

    /** move player avatar to new position
     * sets new coordinates to avatar, and sets previous coordinates back to
     * floor.
     */
    public void updatePlayerPosition(){
        Point last = player.getLastPos();
        Point pos = player.getPosition();
        int x = pos.getX();
        int y = pos.getY();
        world[last.getX()][last.getY()] = Tileset.FLOOR;
        world[x][y] = Tileset.AVATAR;
    }

    /** select a random point in world
     * I subtracted 5 from the max height/width because you wouldnt want to
     * start drawing an area at the edge of the map
     * @return  a Point in the world
     */
    private Point selectRandomPoint(){
        int xCoordinate = uniform(rand, 10, worldWidth-15);
        int yCoordinate = uniform(rand, 10, worldHeight-15);
        return new Point(xCoordinate, yCoordinate);
    }

    /** makes a room of random size
     *
     * @param x   x coordinate where to start drawing
     * @param y    y coordinate where to start drawing
     * @param reverse   is the position reversed or not
     * @return      room object
     */
    private Room createRandomRoom(int x, int y, boolean reverse){
        int width = uniform(rand, 5, 12);
        int height = uniform(rand, 5, 12);
        return new Room(width, height, x, y, reverse);
    }

    /** create a hall of random length
     * needs orientation and reverse info first
     * @param x    x coordinate
     * @param y    y coordinate
     * @param reverse    reverse position factor
     * @param orientation   vertical / horizontal orientation
     * @return     Hall object
     */
    private Hall createRandomHall(int x, int y, boolean reverse, boolean orientation){
        int length = uniform(rand, 5, 10);
        return new Hall(length, orientation, x, y, reverse);
    }

    /** returns orientation based on the next door starting point
     * true = vertical,   false = horizontal
     * @param door    door Point object
     * @return      true / false
     */
    private boolean checkOrientation(Door door){
        String side = door.getSide();
        switch(side){
            case RIGHT:
            case LEFT:
                return false;
            case TOP:
            case BOTTOM:
                return true;
            default: return true;
        }
    }

    /** similar to check orientation, only it checks for REVERSE variable
     * this reverse variable determines if we need to use TopRight point
     * to calculate the bottom left point of an area
     * @param door    door point object
     * @return     true / false
     */
    private boolean checkReverse(Door door){
        String side = door.getSide();
        switch(side){
            case RIGHT:
            case TOP:
                return false;
            case LEFT:
            case BOTTOM:
                return true;
            default: return true;
        }
    }

    /** finds the point to create a new hall object
     * for halls, it will always be just one space next to the door point
     * @param door  current door object
     * @return     point to start next hall
     */
    private Point calculateHallStartPoint(Door door){
        String side = door.getSide();
        int x = door.getX();
        int y = door.getY();
        if (side.equals(RIGHT)){
            return new Point(x, y - 1);
        }else if (side.equals(LEFT)){
            return new Point(x, y + 1);
        }else if (side.equals(BOTTOM)){
            return new Point(x + 1, y);
        }else{
            return new Point(x - 1, y);
        }
    }

    /** calculate a point to start a new a room.  it could be started anywhere
     * from 1 space to 7? spaces away from door, determined randomly
     * @param door    door object
     * @return      Point to start the next room
     */
    private Point calculateRoomStartPoint(Door door){
        String side = door.getSide();
        int x = door.getX();
        int y = door.getY();
        int randomNum = uniform(rand, 0, 7);
        switch (side) {
            case RIGHT:
                return new Point(x, y - randomNum);
            case LEFT:
                return new Point(x, y + randomNum);
            case TOP:
                return new Point(x - randomNum, y);
            case BOTTOM:
                return new Point(x + randomNum, y);
            default:
                return null;
        }
    }

    /** draw an area into the world instance variable
     *
     * @param area   area object
     */
    private void drawArea(Area area){
        int width = area.width;
        int height = area.height;
        int x = area.getX();
        int y = area.getY();
        for (int i = x; i < x + width; i++){
            for (int j = y; j < y + height; j++){
                world[i][j] = Tileset.FLOOR;
            }
        }
        //make wall for bottom and top
        for (int i = x; i < x + width; i++){
            world[i][y] = Tileset.WALL;
            world[i][y + height - 1] = Tileset.WALL;
        }
        //make wall for left and right sides
        for (int j = y; j < y + height; j++){
            world[x][j] = Tileset.WALL;
            world[x + width - 1][j] = Tileset.WALL;
        }
    }

    /** get the map of doors from area object (room or hall) and
     * add it to the list of maps DOORLIST
     * @param area    area object
     */
    private void getDoors(Area area){
        ArrayList<Door> roomDoors = area.setDoors(rand, area.getStartSide());
        doorList.addAll(roomDoors);
    }

    /** randomly choose one of the HashMaps of doors
     * one map represents a room or hallways' door points
     * @return  hash map of doors
     */
    private Door selectDoor(){
        int randomNum = uniform(rand, 0, doorList.size());
        return doorList.get(randomNum);
    }

    private void removeDoor(Door d){
        doorList.remove(d);
    }

    /** go through list of doors in the OPENDOORS list and set the
     * WORLD to FLOOR tiles at those coordinates
     */
    private void openDoors(){
        for(Door d : openDoors){
            world[d.getX()][d.getY()] = Tileset.FLOOR;
        }
    }

    /** take area dimensions and see if it can fit in the projected area
     * without running into another area or edge
     * @param area    area object, room or hall
     * @return   true/false
     */
    private boolean checkSpace(Area area){
        int width = area.getWidth();
        int height = area.getHeight();
        int x = area.getX();
        int y = area.getY();
        int endX, endY;
        if (!checkEdge(x, y)){
            return false;
        }

        if (area.reverse){
            endX = x + width - 1;
            endY = y + height - 1;
            Point tr = area.getTopRight();
            int trX = tr.getX();
            int trY = tr.getY();
            if (!checkEdge(trX, trY)){
                return false;
            }
        }else {
            endX = x + width;
            endY = y + height;
            x += 1;
            y += 1;
        }

        for (int i = x; i < endX; i++) {      //maybe change
            for (int j = y; j < endY; j++) {
                if (!checkBoundary(i, j)){
                    return false;
                }
            }
        }
        return true;
    }

    /** check if a point is already occupied by another tile type (besides nothing)
     *
     * @param x    x coordinate of current point
     * @param y    y coordinate of current point
     * @return    true/false
     */
    private boolean checkBoundary(int x, int y){
        if (checkEdge(x, y)) {
            return world[x][y] == Tileset.NOTHING;
        }else{
            return false;
        }
    }

    /** check if the current point is off the edge of the map
     * the 2 is a border buffer
     *
     * @param x  x coordinate of current point
     * @param y  y coordinate of current point
     * @return   true / false
     */
    private boolean checkEdge(int x, int y){
        return x >= 2 &&
                y >= 2 &&
                x < worldWidth - 2 &&
                y < worldHeight - 2;
    }
}
