package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

public abstract class Area {
    static boolean VERTICAL = true;
    static boolean HORIZONTAL = false;
    TETile[][] tiles;
    boolean reverse;     //determines if a room was placed using the topright corner, variable helps us to know if we need to calculate the bottom right
    int area, width, height;
    int xPosition, yPosition;
    String startSide;
    Point position;     //position of the bottom left tile of area, where drawing starts
    Point topRight;   //position of top right, can be used to calculate POSITION variable
    ArrayList<Point> rightWall;
    ArrayList<Point> leftWall;
    ArrayList<Point> topWall;
    ArrayList<Point> bottomWall;
    ArrayList<Door> doors;  //list of doors, with a string to classify it as "left" "right" "top" "bottom"

    public abstract ArrayList<Door> setDoors(Random rand, String startSide);

    public void setXPos(int x){
        xPosition = x;
    }

    public void setYPos(int y){
        yPosition = y;
    }

    public void setPosition(int x, int y){
        position = new Point(x, y);
    }

    public void setTopRight(int x, int y){
        topRight = new Point(x, y);
    }

    public void setStartSide(String s){
        startSide = s;
    }

    /** use the top right point, width and height to calculate bottom right 'position' variable
     *
     */
    public void calculatePosition(){
        setXPos(topRight.getX() - width + 1);
        setYPos(topRight.getY() - height + 1);
        setPosition(xPosition, yPosition);
    }

    public int getX(){
        return xPosition;
    }

    public int getY(){
        return yPosition;
    }

    public int getArea(){
        return area;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public Point getPosition(){
        return position;
    }

    public Point getTopRight(){
        return topRight;
    }

    public String getStartSide(){
        return startSide;
    }

}
