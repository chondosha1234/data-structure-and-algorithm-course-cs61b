package byow.Core;

import byow.TileEngine.TETile;

import java.util.ArrayList;
import java.util.Random;

import static byow.Core.RandomUtils.uniform;
import static byow.Core.Constants.*;

public class Hall extends Area{
    boolean orientation;   //vertical or horizontal hall

    /** constructor for a Hall object.
     * takes the length and orientation (horizontal or vertical)
     * we know that one of the dimensions will always be 3 for a hallways based off of the orientation
     *
     * @param length        length of the long side of hallway (other side is always 3)
     * @param orientation   boolean to determine vertical or horizontal
     */
    public Hall(int length, boolean orientation, int x, int y, boolean reverse){
        doors = new ArrayList<>();
        this.orientation = orientation;
        this.reverse = reverse;
        startSide = "";
        if(orientation == VERTICAL){
            height = length;
            width = 3;
        }else{
            width = length;
            height = 3;
        }
        area = width * height;
        tiles = new TETile[width][height];
        if (reverse){
            setTopRight(x, y);
            calculatePosition();
        }else {
            setXPos(x);
            setYPos(y);
            setPosition(x, y);
        }
        setWalls();
    }

    /** after creating a hall, find the 2 doors on either end, and also set 1 door on each side wall
     *
     */
    public ArrayList<Door> setDoors(Random rand, String startSide){
        if (orientation == VERTICAL){
            int xCoordinate = xPosition + 1;
            int bottomYCoordinate = yPosition;
            int topYCoordinate = yPosition + height - 1;
            //get the door point at the end of hall opposite the start end
            if (!startSide.equals(BOTTOM)) {
                Door bottomDoor = new Door(xCoordinate, bottomYCoordinate, BOTTOM);
                doors.add(bottomDoor);
            }else {
                Door topDoor = new Door(xCoordinate, topYCoordinate, TOP);
                doors.add(topDoor);
            }
            //get a random door point on the side walls
            int randomIndex = uniform(rand, 0, leftWall.size());
            Point p = leftWall.get(randomIndex);
            Door leftDoor = new Door(p.getX(), p.getY(), LEFT);
            doors.add(leftDoor);

            randomIndex = uniform(rand, 0, rightWall.size());
            p = rightWall.get(randomIndex);
            Door rightDoor = new Door(p.getX(), p.getY(), RIGHT);
            doors.add(rightDoor);

        }else if (orientation == HORIZONTAL){
            int yCoordinate = yPosition + 1;
            int leftXCoordinate = xPosition;
            int rightXCoordinate = xPosition + width - 1;
            if (!startSide.equals(RIGHT)) {
                Door rightDoor = new Door(rightXCoordinate, yCoordinate, RIGHT);
                doors.add(rightDoor);
            }else {
                Door leftDoor = new Door(leftXCoordinate, yCoordinate, LEFT);
                doors.add(leftDoor);
            }

            int randomIndex = uniform(rand, 0, topWall.size());
            Point p = topWall.get(randomIndex);
            Door topDoor = new Door(p.getX(), p.getY(), TOP);
            doors.add(topDoor);

            randomIndex = uniform(rand, 0, bottomWall.size());
            p = bottomWall.get(randomIndex);
            Door bottomDoor = new Door(p.getX(), p.getY(), BOTTOM);
            doors.add(bottomDoor);
        }
        return doors;
    }

    /** set the points on the long walls of a hallway, these can also be used as a start point of
     * another hall
     */
    private void setWalls(){
        int endX = xPosition + width;
        int endY = yPosition + height;

        if (orientation == VERTICAL){
            leftWall = new ArrayList<>();
            rightWall = new ArrayList<>();
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
        }else if (orientation == HORIZONTAL){
            bottomWall = new ArrayList<>();
            topWall = new ArrayList<>();
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
        }
    }
}
