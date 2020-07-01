package creatures;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.awt.Color;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.Impassible;
import huglife.Empty;

public class TestClorus {

    @Test
    public void testBasics(){
        Clorus c = new Clorus(2);
        Plip p = new Plip(2);
        assertEquals(2.0, c.energy(), 0.01);
        assertEquals(new Color(34, 0, 231), c.color());
        c.move();
        assertEquals(1.97, c.energy(), 0.01);
        c.move();
        assertEquals(1.94, c.energy(), 0.01);
        c.stay();
        assertEquals(1.93, c.energy(), 0.01);
        c.attack(p);
        assertEquals(3.93, c.energy(), 0.01);
    }

    @Test
    public void testReplicate(){
        Clorus c = new Clorus(2);
        Clorus copy = c.replicate();
        assertEquals(1.0, c.energy(), 0.01);
        assertEquals(1.0, copy.energy(), 0.01);
        assertNotEquals(c, copy);
    }

    @Test
    public void testChoose(){

        // No empty adjacent spaces; stay.
        Clorus c = new Clorus(2);
        Plip p1 = new Plip(2);
        HashMap<Direction, Occupant> surrounded = new HashMap<>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, p1);

        Action actual = c.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);

        // Plips to attack
        Plip p2 = new Plip(2);
        HashMap<Direction, Occupant> enemies = new HashMap<>();
        enemies.put(Direction.TOP, p2);
        enemies.put(Direction.RIGHT, new Empty());
        enemies.put(Direction.BOTTOM, new Empty());
        enemies.put(Direction.LEFT, new Empty());

        Action actual2 = c.chooseAction(enemies);
        Action expected2 = new Action(Action.ActionType.ATTACK, Direction.TOP);

        assertEquals(actual2, expected2);

        // Test replicate
        Clorus c2 = new Clorus(1.0);
        HashMap<Direction, Occupant> replicate = new HashMap<>();
        replicate.put(Direction.TOP, new Impassible());
        replicate.put(Direction.RIGHT, new Impassible());
        replicate.put(Direction.BOTTOM, new Impassible());
        replicate.put(Direction.LEFT, new Empty());

        Action actual3 = c2.chooseAction(replicate);
        Action expected3 = new Action(Action.ActionType.REPLICATE, Direction.LEFT);

        assertEquals(actual3, expected3);

        //Test Move
        Clorus c3 = new Clorus(0.9);
        HashMap<Direction, Occupant> moving = new HashMap<>();
        moving.put(Direction.TOP, new Impassible());
        moving.put(Direction.RIGHT, new Impassible());
        moving.put(Direction.BOTTOM, new Impassible());
        moving.put(Direction.LEFT, new Empty());

        Action actual4 = c3.chooseAction(moving);
        Action expected4 = new Action(Action.ActionType.MOVE, Direction.LEFT);

        assertEquals(actual4, expected4);

    }
}
