package creatures;

import static creatures.Plip.randomEntry;

import huglife.Creature;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

public class Clorus extends Creature {

    /** color variables */
    private int r;
    private int g;
    private int b;

    /** constructor for clorus, sets colors and energy */
    public Clorus(double e){
        super("clorus");
        r = 34;
        g = 0;
        b = 231;
        energy = e;
    }

    /** returns Color object, clorus should always have same color */
    public Color color(){
        return color(r, g, b);
    }

    /** attack another creature and gain its energy */
    public void attack(Creature c){
        energy += c.energy();
    }

    /** clorus loses energy by moving */
    public void move(){
        energy -= 0.03;
        energy = Math.max(energy, 0);
    }

    /** clorus loses energy by staying */
    public void stay(){
        energy -= 0.01;
        energy = Math.max(energy, 0);
    }

    /** replicates like Plip, splits energy in half */
    public Clorus replicate(){
        energy = energy / 2;
        Clorus copy = new Clorus(energy);
        return copy;
    }

    /** Chooses and action and returns a new Action method.
     * rule 1: If there are no empty squares, clorus will stay. (even if Plip nearby)
     * rule 2: Otherwise, if there are any Plips, clorus will attack one of them randomly
     * rule 3: Otherwise, if energy is >= 1.0 clorus will replicate into random empty space
     * rule 4: Otherwise, clorus Moves to random empty space
     */
    public Action chooseAction(Map<Direction, Occupant> neighbors){

        // Rule 1
        Deque<Direction> emptyNeighbors = new ArrayDeque<>();
        Deque<Direction> targets = new ArrayDeque<>();
        boolean anyPlip = false;
        boolean possibleMove = false;

        for (Direction direction : neighbors.keySet()){
            if (neighbors.get(direction).name().equals("empty")){
                possibleMove = true;
                emptyNeighbors.add(direction);
            }
            if (neighbors.get(direction).name().equals("plip")){
                anyPlip = true;
                targets.add(direction);
            }
        }
        if (!possibleMove){
            return new Action(Action.ActionType.STAY);
        }

        //rule 2
        if (anyPlip){
            return new Action(Action.ActionType.ATTACK, randomEntry(targets));
        }

        //rule 3
        if (energy >= 1.0) {
            return new Action(Action.ActionType.REPLICATE, randomEntry(emptyNeighbors));
        }
        //rule 4
        return new Action(Action.ActionType.MOVE, randomEntry(emptyNeighbors));
    }
}
