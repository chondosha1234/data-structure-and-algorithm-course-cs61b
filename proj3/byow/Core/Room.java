package byow.Core;

import byow.TileEngine.TETile;

import java.util.ArrayList;
import java.util.Random;

import static byow.Core.RandomUtils.uniform;
import static byow.Core.Constants.*;

public class Room extends Area{

    /** constructor for Room object
     * fills it with FLOOR tiles and then adds walls along the edge
     * @param width    width of the room area
     * @param height   height of the room area
     */
    public Room(int width, int height, int x, int y, boolean reverse){
        tiles = new TETile[width][height];
        this.width = width;
        this.height = height;
        this.reverse = reverse;
        area = width * height;
        doors = new ArrayList<>();
        rightWall = new ArrayList<>();
        leftWall = new ArrayList<>();
        topWall = new ArrayList<>();
        bottomWall = new ArrayList<>();
        startSide = "";
        if (reverse){
            setTopRight(x, y);
            calculatePosition();
        }else {
            setXPos(x);
            setYPos(y);
            setPosition(x, y);
        }
        //addTiles();
        setWalls();
    }

    /** used during construction of room object
     * sets points on each wall which could be valid Door points
     */
    private void setWalls(){
        int endX = xPosition + width;
        int endY = yPosition + height;

        //bottom wall points
        for (int i = xPosition + 2; i < endX - 1; i ++){  //exclude bottom left corner
            Point p = new Point(i, yPosition);
            bottomWall.add(p);
        }
        //top wall points
        for (int i = xPosition + 1; i < endX - 2; i++){  //exclude upper right corner
            Point p = new Point(i, endY-1);
            topWall.add(p);
        }
        //left wall points
        for (int j = yPosition + 2; j < endY - 1; j++){  //exclude bottom left corner
            Point p = new Point(xPosition, j);
            leftWall.add(p);
        }
        //right wall points
        for (int j = yPosition + 1; j < endY - 2; j++){
            Point p = new Point(endX-1, j);
            rightWall.add(p);
        }
    }

    /** randomly selects a point on each wall and marks it as a potential door, where a hallways can
     * later be connected to it
     * @param rand  random seed from the world class
     */
    public ArrayList<Door> setDoors(Random rand, String startSide){
        int randomIndex;
        Point p;
        if (!startSide.equals(RIGHT)) {
            randomIndex = uniform(rand, 0, rightWall.size());
            p = rightWall.get(randomIndex);
            Door rightDoor = new Door(p.getX(), p.getY(), RIGHT);
            doors.add(rightDoor);
        }

        if (!startSide.equals(LEFT)) {
            randomIndex = uniform(rand, 0, leftWall.size());
            p = leftWall.get(randomIndex);
            Door leftDoor = new Door(p.getX(), p.getY(), LEFT);
            doors.add(leftDoor);
        }

        if (!startSide.equals(BOTTOM)) {
            randomIndex = uniform(rand, 0, bottomWall.size());
            p = bottomWall.get(randomIndex);
            Door bottomDoor = new Door(p.getX(), p.getY(), BOTTOM);
            doors.add(bottomDoor);
        }

        if (!startSide.equals(TOP)) {
            randomIndex = uniform(rand, 0, topWall.size());
            p = topWall.get(randomIndex);
            Door topDoor = new Door(p.getX(), p.getY(), TOP);
            doors.add(topDoor);
        }

        return doors;
    }


}
