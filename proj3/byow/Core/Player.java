package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Player {
    private Point position;
    private Point lastPosition;
    private int xPos, yPos;
    private TETile[][] world;
    private World w;
    private int life;


    public Player(World w){
        world = w.getWorld();    //array of tiles from world
        this.w = w;        //world object where this player is
        life = 5;
    }

    /** needed to split the initialization into 2 parts */
    public void initialize(Point start){
        position = start;
        lastPosition = position;
        xPos = position.getX();
        yPos = position.getY();
    }

    /** method to take damage, called when a monster attacks the player
     *
     */
    public void takeDamage(){
        life -= 1;
    }

    /** returns the amount of life, can be used to update HUD
     *
     * @return  integer, amount of life left
     */
    public int getLife(){
        return life;
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
