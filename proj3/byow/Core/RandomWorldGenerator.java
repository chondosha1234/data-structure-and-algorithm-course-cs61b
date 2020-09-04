package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

import static byow.Core.RandomUtils.bernoulli;
import static byow.Core.RandomUtils.uniform;

public class RandomWorldGenerator {
    TETile[][] world;     //2D area of the world map
    Random rand;
    HashMap<String, ArrayList<ArrayList<Point>>> walls;
    ArrayList<Point> doors;
    ArrayList<HashMap<Boolean, ArrayList<Point>>> endPoints; //hashmap relates boolean for orientation to list of end points
    double worldSize;
    double percentFilled;
    int worldWidth;
    int worldHeight;

    /** Constructor for RandomWorldGenerator
     * Creates a blank map with given height and width.
     * other methods will generate a world on this blank template
     * @param width
     * @param height
     */
    public RandomWorldGenerator(int width, int height){
        worldWidth = width;
        worldHeight = height;
        world = new TETile[width][height];
        walls = new HashMap<>();
        doors = new ArrayList<>();
        endPoints = new ArrayList<>();
        worldSize = width * height;
        percentFilled = 0.0;

        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        walls.put("rightWalls", new ArrayList<>());
        walls.put("leftWalls", new ArrayList<>());
        walls.put("topWalls", new ArrayList<>());
        walls.put("bottomWalls", new ArrayList<>());

        //this list holds 2 maps, one for vertical halls and one for horizontal halls
        endPoints.add(new HashMap<>());
        endPoints.add(new HashMap<>());
        endPoints.get(0).put(true, new ArrayList<>());
        endPoints.get(1).put(false, new ArrayList<>());

    }

    /** creates a random map in the WORLD instance variable
     *
     */
    public TETile[][] generateWorld(int SEED){
        rand = new Random(SEED);
        Point start = selectRandomPoint();
        TETile[][] firstRoom = createRandomRoom();
        drawArea(firstRoom, start.getX(), start.getY(), false);
        double roomArea = calculateArea(firstRoom);
        boolean reverse;
        int hallArea;
        percentFilled = roomArea / worldSize;

        while (percentFilled < 0.75){
            TETile[][] nextHall = createRandomHall();
            String wallSide = selectSide();
            Point startPoint = selectRandomWallPoint(wallSide);
            if(wallSide == "rightWalls" || wallSide == "topWalls"){
                reverse = true;
            }else{
                reverse = false;
            }
            drawArea(nextHall, startPoint.getX(), startPoint.getY(), reverse); //fix boolean
            hallArea = calculateArea(nextHall);
            percentFilled += hallArea / worldSize;

            TETile[][] nextRoom = createRandomRoom();
            startPoint = selectRandomEnd();
            drawArea(nextRoom, startPoint.getX(), startPoint.getY(), true);
            roomArea = calculateArea(nextRoom);
            percentFilled += roomArea / worldSize;
        }
        setDoors();
        return world;
    }

    /** select a random point in world
     * I subtracted 5 from the max height/width because you wouldnt want to
     * start drawing an area at the edge of the map
     * @return
     */
    private Point selectRandomPoint(){
        int xCoordinate = uniform(rand, 0, worldWidth-5);
        int yCoordinate = uniform(rand, 0, worldHeight-5);
        return new Point(xCoordinate, yCoordinate);
    }

    /** randomly select a point from a wall of a room or hall to start the next hallways
     *
     * @return
     */
    private Point selectRandomWallPoint(String wall){
        ArrayList<ArrayList<Point>> wallList = walls.get(wall);    // get list of all 'right' walls

        int listNum = uniform(rand, 0, wallList.size());  //select a random wall
        ArrayList<Point> thisWall = wallList.remove(listNum);   // get the list of points for that wall

        int pointNum = uniform(rand, 0, thisWall.size()); //get a random point on that wall
        return thisWall.get(pointNum);
    }

    /** randomly select a side (left, right, up, down) and return a string which can be used to access a map
     *
     * @return
     */
    private String selectSide(){
        int wallNum = uniform(rand, 0, 4);  //randomly select one of the foor wall directions
        switch(wallNum){
            case 0: return "rightWalls";
            case 1: return "leftWalls";
            case 2: return "topWalls";
            case 3: return "bottomWalls";
            default: return null;
        }
    }

    /** select a point from the end of a hallway where we can place a new room
     * orientation means hall is vertical if true, and horizontal if false
     * @return
     */
    private Point selectRandomEnd(){
        ArrayList<Point> points;
        boolean orientation;
        int direction = uniform(rand, 0, 2);
        HashMap<Boolean, ArrayList<Point>> endMap = endPoints.get(direction);
        if (direction == 0 && endMap.get(true).size() != 0){
            points = endMap.get(true);
            orientation = true;
        }else if (direction == 1 && endMap.get(false).size() != 0){
            points = endMap.get(false);
            orientation = false;
        }else{
            return selectRandomEnd();
        }
        int num = uniform(rand, 0, points.size());
        return findValidStart(points.remove(num), orientation);
    }

    /** takes a hall's end point and finds a valid start point for a room to the left or right (or up or down)
     * because you don't want to start building a room from the middle of the hall
     * @param end
     * @param orientation
     * @return
     */
    private Point findValidStart(Point end, boolean orientation){
        Point p;
        int range = uniform(rand, -4, 4);
        if (orientation){
            p = new Point(end.getX() + range, end.getY());
        }else{
            p = new Point(end.getX(), end.getY() + range);
        }
        return p;
    }

    /** calculate the mathematical area of an area
     *
     * @param area
     * @return
     */
    private int calculateArea(TETile[][] area){
        return area.length * area[0].length;
    }

    /** method to create a general room,
     * can be used in creating random rooms and hallways
     * @param width
     * @param height
     * @return
     */
    private TETile[][] createRoom(int width, int height){
        TETile[][] room = new TETile[width][height];
        //first set everything to floor
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                room[i][j] = Tileset.FLOOR;
            }
        }
        //make wall for bottom and top
        for (int i = 0; i < width; i++){
            room[i][0] = Tileset.WALL;
            room[i][height - 1] = Tileset.WALL;
        }
        //make wall for left and right sides
        for (int j = 0; j < height; j++){
            room[0][j] = Tileset.WALL;
            room[width - 1][j] = Tileset.WALL;
        }
        return room;
    }

    /** creates a 2D array which holds tiles to make a room
     * with random width / height, and with a random spot for a door
     * @return
     */
    private TETile[][] createRandomRoom(){
        int width = uniform(rand, 5, 15);
        int height = uniform(rand, 5, 15);
        return createRoom(width, height);
    }

    /** creates a hallway, orientation variable dictates if it is vertical or horizontal
     * true for vertical, false for horizontal
     * @param width
     * @param height
     * @param orientation
     * @return
     */
    private TETile[][] createHall(int width, int height, boolean orientation){
        if (orientation){
            width = 3;
        }else{
            height = 3;
        }
        return createRoom(width, height);
    }

    /** create a hall with random orientation and random length
     * @return
     */
    private TETile[][] createRandomHall(){
        boolean orientation = bernoulli(rand);
        int height = uniform(rand, 5, 15);
        int width = uniform(rand, 5, 15);
        return createHall(width, height, orientation);
    }

    /** takes a 2D array of tiles which represents a room or hallway
     * and draws them on the world
     * @param area
     * @param xPos
     * @param yPos
     */
    private void drawArea(TETile[][] area, int xPos, int yPos, boolean reverse){
        int x = 0;
        int y = 0;
        int endX = xPos + area.length;
        int endY = yPos + area[0].length;

        if (reverse){
            drawReverse(area, endX, endY);
            findWalls(area, xPos, yPos, reverse);
        }else {
            findWalls(area, xPos, yPos, reverse);
            for (int i = xPos; i < endX; i++) {   //must increment
                for (int j = yPos; j < endY; j++) {
                    if (checkBoundary(i, j)) {
                        world[i][j] = area[x][y];
                    }
                    y += 1;
                }
                y = 0;
                x += 1;
            }
        }
    }

    /** the reverse of draw area
     * if an area needs to be drawn from the left or bottom side of a room
     * then it is better the start at the wall and draw out from it, so there
     * needs to be a draw method that draws it from right to left, or top to bottom
     * @param area
     * @param xPos
     * @param yPos
     */
    private void drawReverse(TETile[][] area, int xPos, int yPos){
        int x = 0;
        int y = 0;
        int endX = xPos - area.length;
        int endY = yPos - area[0].length;

        for (int i = xPos; i > endX; i--){   //must decrement
            for (int j = yPos; j > endY; j--){
                if (checkBoundary(i, j)) {
                    world[i][j] = area[x][y];
                }
                y += 1;
            }
            y = 0;
            x += 1;
        }
    }

    /** check if the coordinates you are about to update are already
     * a different tile
     * @param xPos
     * @param yPos
     * @return
     */
    private boolean checkBoundary(int xPos, int yPos){
        return  xPos < worldWidth
                && yPos < worldHeight
                && xPos >= 0
                && yPos >= 0
                && world[xPos][yPos].equals(Tileset.NOTHING);
    }

    /** find the walls of an area (room or hall)
     * puts valid points of a wall into a list and puts that list into a list of all similar walls
     * that list of lists is in a map to designate them as 'right' or 'left' walls
     * @param area
     * @param xPos
     * @param yPos
     */
    private void findWalls(TETile[][] area, int xPos, int yPos, boolean reverse){
        int width = area.length;
        int height = area[0].length;
        if (width == 3){
            findHallPoints(area, xPos, yPos, true);
            findDoor(area, xPos, yPos, true, reverse);
        }else if (height == 3) {
            findHallPoints(area, xPos, yPos, false);
            findDoor(area, xPos, yPos, true, reverse);
        }else {
           findRoomWalls(area, xPos, yPos);
        }
    }

    /** find wall points for a room
     * @param area
     * @param xPos
     * @param yPos
     */
    private void findRoomWalls(TETile[][] area, int xPos, int yPos){
        int endX = xPos + area.length;
        int endY = yPos + area[0].length;
        ArrayList<Point> rightWall = new ArrayList<>();
        ArrayList<Point> leftWall = new ArrayList<>();
        ArrayList<Point> topWall = new ArrayList<>();
        ArrayList<Point> bottomWall = new ArrayList<>();

        //bottom wall points
        for (int i = xPos + 2; i < endX; i ++){  //exclude bottom left corner
            Point p = new Point(i, yPos);
            bottomWall.add(p);
        }
        //top wall points
        for (int i = xPos; i < endX - 2; i++){  //exclude upper right corner
            Point p = new Point(i, endY-1);
            topWall.add(p);
        }
        //left wall points
        for (int j = yPos + 2; j < endY; j++){  //exclude bottom left corner
            Point p = new Point(xPos, j);
            leftWall.add(p);
        }
        //right wall points
        for (int j = yPos; j < endY - 2; j++){
            Point p = new Point(endX-1, j);
            rightWall.add(p);
        }
        //add wall lists into the map
        walls.get("rightWalls").add(rightWall);
        walls.get("leftWalls").add(leftWall);
        walls.get("topWalls").add(topWall);
        walls.get("bottomWalls").add(bottomWall);
    }

    /** find wall points for hallways, should only be the 2 long sides
     * the shorter sides will be doors and start points for rooms
     * @param area
     * @param xPos
     * @param yPos
     * @param orientation
     */
    private void findHallPoints(TETile[][] area, int xPos, int yPos, boolean orientation){
        int endX = xPos + area.length;
        int endY = yPos + area[0].length;

        if (orientation){       //vertical hall, only get the side walls
            ArrayList<Point> leftWall = new ArrayList<>();
            ArrayList<Point> rightWall = new ArrayList<>();

            //left wall points
            for (int j = yPos + 2; j < endY; j++){  //exclude bottom left corner
                Point p = new Point(xPos, j);
                leftWall.add(p);
            }
            //right wall points
            for (int j = yPos; j < endY - 2; j++){
                Point p = new Point(endX-1, j);
                rightWall.add(p);
            }
            walls.get("rightWalls").add(rightWall);
            walls.get("leftWalls").add(leftWall);

        }else{    //horizontal wall, only get top and bottom walls
            ArrayList<Point> topWall = new ArrayList<>();
            ArrayList<Point> bottomWall = new ArrayList<>();

            //bottom wall points
            for (int i = xPos + 2; i < endX; i ++){  //exclude bottom left corner
                Point p = new Point(i, yPos);
                bottomWall.add(p);
            }
            //top wall points
            for (int i = xPos; i < endX - 2; i++){  //exclude upper right corner
                Point p = new Point(i, endY-1);
                topWall.add(p);
            }
            walls.get("topWalls").add(topWall);
            walls.get("bottomWalls").add(bottomWall);
        }
    }

    /** find the point of the hall where it connects to a room and marks it as a door
     *
     * @param area
     * @param xPos
     * @param yPos
     * @param orientation
     * @param reverse
     */
    private void findDoor(TETile[][] area, int xPos, int yPos, boolean orientation, boolean reverse){
        Point door;
        Point endPoint;  //end point will be where a new room will be
        int width;
        int height;
        int mapNum;  //based on orientation will be first or second hashmap in endpoints list of maps

        if (orientation) {   //vertical hall
            mapNum = 0;
            if (reverse) {   //reverse drawn
                height = yPos - area[0].length - 1;  //reverse point starts at higher value
                door = new Point(xPos - 1, yPos);
                endPoint = new Point(xPos - 1, height);
            }else{
                height = yPos + area[0].length - 1;  //normal point starts at lower value
                door = new Point(xPos + 1, yPos);
                endPoint = new Point(xPos + 1, height);
            }
        }else{    //horizontal hall
            mapNum = 1;
            if (reverse) {  //reverse drawn
                width = xPos - area.length - 1;
                door = new Point(xPos, yPos - 1);
                endPoint = new Point(width, yPos - 1);
            }else{
                width = xPos + area.length - 1;
                door = new Point(xPos, yPos + 1);
                endPoint = new Point(width, yPos + 1);
            }
        }
        doors.add(door);
        endPoints.get(mapNum).get(orientation).add(endPoint);
    }

    /** after the world is built, go back through list of points that mark doors
     * and set them as floor (or door if wanted)
     */
    private void setDoors(){
        for (Point p : doors){
            if(p.getX() > 0 && p.getY() > 0) {   //temporary fix
                world[p.getX()][p.getY()] = Tileset.FLOOR;
            }
        }
    }

    public static void main(String[] args){
        RandomWorldGenerator newWorld = new RandomWorldGenerator(50, 50);
        TERenderer ter = new TERenderer();
        ter.initialize(50, 50);
        TETile[][] world = newWorld.generateWorld(5434);

        /* TETile[][] room = newWorld.createRandomRoom();
        TETile[][] hall = newWorld.createRandomHall();
        newWorld.drawArea(room, 10, 10, true);
        newWorld.drawArea(hall, 20, 20, false);
        ArrayList<Point> list = newWorld.walls.get("bottomWalls").get(0);
        for (Point p : list){
            System.out.print(p.getX() + " ");
            System.out.print(p.getY());
            System.out.println();
        }*/
        ter.renderFrame(world);
    }
}
