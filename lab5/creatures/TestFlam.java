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

public class TestFlam {

    @Test
    public void testBasics(){
        Flam f = new Flam(5.0);
        Plip p = new Plip(1.0);
        assertEquals(new Color(196, 156, 55), f.color());
        assertEquals(5.0, f.energy(), 0.01);
        f.move();
        assertEquals(4.97, f.energy(), 0.01);
        f.stay();
        assertEquals(4.96, f.energy(), 0.01);
        f.attack(p);
        assertEquals(4.91, f.energy(), 0.01);
    }

    @Test
    public void testReplicate(){
        Flam f = new Flam(4.0);
        f.move();
        f.move();
        assertEquals(3.94, f.energy(), 0.01);
        Flam copy = f.replicate();
        assertEquals(0.0, f.energy(), 0.01);
        assertEquals(3.0, copy.energy(), 0.01);
        assertNotEquals(copy, f);
    }

    @Test
    public void testChoose(){
        //test rule 1  attack
        Flam f = new Flam(5.0);
        Clorus c = new Clorus(2.0);
        HashMap<Direction, Occupant> enemy = new HashMap<>();
        enemy.put(Direction.TOP, new Impassible());
        enemy.put(Direction.BOTTOM, new Impassible());
        enemy.put(Direction.LEFT, new Impassible());
        enemy.put(Direction.RIGHT, c);

        Action actual = f.chooseAction(enemy);
        Action expected = new Action(Action.ActionType.ATTACK, Direction.RIGHT);

        assertEquals(expected, actual);

        //test rule 2
        HashMap<Direction, Occupant> moving = new HashMap<>();
        moving.put(Direction.TOP, new Impassible());
        moving.put(Direction.BOTTOM, new Impassible());
        moving.put(Direction.LEFT, new Impassible());
        moving.put(Direction.RIGHT, new Empty());

        Action actual2 = f.chooseAction(moving);
        Action expected2 = new Action(Action.ActionType.MOVE, Direction.RIGHT);

        assertEquals(actual2, expected2);

        //replicate rule
        Flam dead = new Flam(0.03);
        Action actual3 = dead.chooseAction(moving);
        Action expected3 = new Action(Action.ActionType.STAY);

        assertEquals(actual3, expected3);

        Flam dead2 = new Flam(0.01);
        Action actual4 = dead2.chooseAction(moving);
        Action expected4 = new Action(Action.ActionType.REPLICATE, Direction.RIGHT);

        assertEquals(actual4, expected4);

        HashMap<Direction, Occupant> staying = new HashMap<>();
        staying.put(Direction.TOP, new Impassible());
        staying.put(Direction.BOTTOM, new Impassible());
        staying.put(Direction.LEFT, new Impassible());
        staying.put(Direction.RIGHT, new Impassible());

        Flam stayDead = new Flam(0.01);
        Action actual5 = stayDead.chooseAction(staying);
        Action expected5 = new Action(Action.ActionType.STAY);

        assertEquals(actual5, expected5);
    }
}
