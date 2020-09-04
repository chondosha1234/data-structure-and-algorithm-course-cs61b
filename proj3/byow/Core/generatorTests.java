package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

public class generatorTests {

    public static void main(String[] args){
        Engine eng = new Engine();
        eng.interactWithInputString("n9281s");


        /*
        int width = 70;
        int height = 40;
        TERenderer ter = new TERenderer();
        ter.initialize(width, height);
        World newWorld = new World(width, height, 92884);
        TETile[][] world = newWorld.generateRandomWorld();
        /*
        Door nextDoor;
        boolean reverse = false;
        boolean hallOrientation;
        Point start = newWorld.selectRandomPoint();
        Hall nextHall;
        Room nextRoom = newWorld.createRandomRoom(start.getX(), start.getY(), reverse);
        newWorld.getDoors(nextRoom);
        newWorld.drawArea2(nextRoom);

        ter.renderFrame(newWorld.world);
        nextDoor = newWorld.selectDoor();
        hallOrientation = newWorld.checkOrientation(nextDoor);
        reverse = newWorld.checkReverse(nextDoor);
        start = newWorld.calculateHallStartPoint(nextDoor);
        nextHall = newWorld.createRandomHall(start.getX(), start.getY(), reverse, hallOrientation);
        nextHall.setStartSide(nextDoor.getOppositeSide());
        newWorld.drawArea2(nextHall);
        newWorld.getDoors(nextHall);
        ter.renderFrame(newWorld.world);

        ter.renderFrame(world);
        */
    }
}
