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

/** Flam will be a third creature which will attack Plip and Clorus, but will eventually run
 * out of energy
 */
public class Flam extends Creature {
    /** color variables */
    private int r;
    private int g;
    private int b;
    private final double originalEnergy;

    public Flam(double e) {
        super("flam");
        r = 196;
        g = 156;
        b = 55;
        energy = e;
        originalEnergy = e;
    }

    public Color color() {
        return color(r, g, b);
    }

    public void attack(Creature c){
        energy -= 0.05;
        energy = Math.max(energy, 0);
    }

    public void move(){
        energy -= 0.03;
        energy = Math.max(energy, 0);
    }

    public void stay(){
        energy -= 0.01;
        energy = Math.max(energy, 0);
    }

    public Flam replicate(){
        energy = 0;
        Flam copy = new Flam(originalEnergy * 0.25);
        return copy;
    }

    /** returns an Action object based on a set of rules
     *  rule 1: if there is an enemy nearby, attack one randomly
     *  rule 2: if there are no enemies and you can move, move randomly
     *  rule 3: if an action will kill the Flam, replicate if possible
     *  rule 4: else stay
     */
    public Action chooseAction(Map<Direction, Occupant> neighbors){
        //rule 1
        Deque<Direction> emptyNeighbors = new ArrayDeque<>();
        Deque<Direction> targets = new ArrayDeque<>();
        boolean anyTarget = false;
        boolean possibleMove = false;

        for (Direction direction : neighbors.keySet()){
            if (neighbors.get(direction).name().equals("empty")){
                possibleMove = true;
                emptyNeighbors.add(direction);
            }
            if (neighbors.get(direction).name().equals("plip")||neighbors.get(direction).name().equals("clorus")){
                anyTarget = true;
                targets.add(direction);
            }
        }

        //rule 1
        if (anyTarget && energy > 0.05) {
            return new Action(Action.ActionType.ATTACK, randomEntry(targets));
        }
        //rule 2
        if (possibleMove && energy > 0.03){
            return new Action(Action.ActionType.MOVE, randomEntry(emptyNeighbors));
        }
        //rule 4
        if (energy > 0.01 || !possibleMove) {
            return new Action(Action.ActionType.STAY);
        }
        //rule 3
        return new Action(Action.ActionType.REPLICATE, randomEntry(emptyNeighbors));

    }
}
