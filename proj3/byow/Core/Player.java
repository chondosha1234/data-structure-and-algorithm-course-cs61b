package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Player {
    Point position;
    Point lastPosition;
    int xPos, yPos;
    TETile[][] world;
    World w;

    public Player(Point startPosition, World w){
        position = startPosition;
        lastPosition = position;
        xPos = position.getX();
        yPos = position.getY();
        world = w.getWorld();    //array of tiles from world
        this.w = w;        //world object where this player is
    }

    public void updateWorld(){
        world = w.getWorld();
    }

    public Point getPosition(){
        return position;
    }

    public Point getLastPos(){
        return lastPosition;
    }

    public void setPosition(int x, int y){
        lastPosition = position;
        position = new Point(x, y);
    }

    public void toWest(){
        if (checkWall(xPos - 1, yPos)) {
            xPos -= 1;
            setPosition(xPos, yPos);
        }
    }

    public void toEast(){
        if (checkWall(xPos + 1, yPos)) {
            xPos += 1;
            setPosition(xPos, yPos);
        }
    }

    public void toSouth(){
        if (checkWall(xPos, yPos - 1)) {
            yPos -= 1;
            setPosition(xPos, yPos);
        }
    }

    public void toNorth(){
        if (checkWall(xPos, yPos + 1)) {
            yPos += 1;
            setPosition(xPos, yPos);
        }
    }

    private boolean checkWall(int x, int y){
        return world[x][y] == Tileset.FLOOR;
    }
}
