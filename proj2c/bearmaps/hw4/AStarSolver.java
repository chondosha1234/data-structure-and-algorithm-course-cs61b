package bearmaps.hw4;

import bearmaps.proj2ab.ArrayHeapMinPQ;
import edu.princeton.cs.introcs.Stopwatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {

    private final SolverOutcome outcome;   // instance variable to return outcome
    private double solutionWeight;  //instance variable to return solution weight
    private List<Vertex> solution;   //list to return solution path
    private double timeSpent;
    private int numOfStatesExplored;

    private ArrayHeapMinPQ<Vertex> fringe;    //our PQ, which represents the fringe as we search
    private HashMap<Vertex, Double> distTo;   //distance to point from start, takes vertex and double
    private HashMap<Vertex, WeightedEdge<Vertex>> edgeTo;  //shows the edge leading back to last point
    private double h;       //heuristic, estimated distance to goal from point, it changes each relax call

    /** constructor for class, most of the work will be done here.  Other public methods
     * are just called to return results
     */
    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout){
        fringe = new ArrayHeapMinPQ<>();
        distTo = new HashMap<>();
        edgeTo = new HashMap<>();
        fringe.add(start, 0);            //must start by adding start vertex with prio 0
        distTo.put(start, 0.0);
        Vertex currentPoint = start;
        Stopwatch sw = new Stopwatch();      //start timer after initializing

        //while PQ is not empty, not at end point, and haven't reached timeout
        while (!fringe.isEmpty() && !currentPoint.equals(end) && (timeSpent < timeout*1000)) {
            currentPoint = fringe.removeSmallest();
            numOfStatesExplored += 1;           //tracks number of dequeue operations

            for (WeightedEdge<Vertex> neighbor : input.neighbors(currentPoint)) {
                h = input.estimatedDistanceToGoal(neighbor.to(), end);   //heuristic from neighbor to goal
                relax(neighbor);            //relax edge between current point and neighbor point
            }
            timeSpent = sw.elapsedTime();   //update time at end of while loop

        }
        outcome = checkOutcome(currentPoint, end, timeout);  //get outcome
        solutionBuilder(start, end);            //will set solution and solutionBuilder instance variables

    }

    /** method to relax an edge between two vertices. This means looking at the current distance to vertex
     * and seeing if the new distance is shorter, if so replacing it.
     * @param edge
     */
    private void relax(WeightedEdge<Vertex> edge){
        Vertex p = edge.from();   //current point
        Vertex q = edge.to();    //one of the neighbors of current point
        double w = edge.weight();      //weight of this edge
        double thisDistance = distTo.get(p) + w;   //make a variable to avoid repeating
        checkMap(q);

        if (thisDistance < distTo.get(q)){
            distTo.replace(q, thisDistance);
            edgeTo.put(q, edge);              //if you update the distance, then change the edgeTo
            if (fringe.contains(q)){
                fringe.changePriority(q, thisDistance + h);
            }else{
                fringe.add(q, thisDistance + h);
            }
        }
    }

    /** quick method to check if vertex is already in distTo map, if not, adds it with infinity distance */
    private void checkMap(Vertex q){
        if(distTo.containsKey(q)){
            return;
        }
        distTo.put(q, Double.MAX_VALUE);
    }

    /** returns our outcome instanced variable, which is set in the constructor using the checkOutcome method */
    @Override
    public SolverOutcome outcome(){
        return outcome;
    }

    /** check outcome after main loop, takes the last vertex before finishing, the goal vertex, and time */
    private SolverOutcome checkOutcome(Vertex lastVertex, Vertex goal, double timeout){
        if (timeSpent > timeout*1000){
            return SolverOutcome.TIMEOUT;     //ran out of time
        }else if (lastVertex.equals(goal)){
            return SolverOutcome.SOLVED;     //reached goal
        }else{
            return SolverOutcome.UNSOLVABLE;   //PQ became empty
        }
    }

    @Override
    public List<Vertex> solution(){
        return solution;
    }

    @Override
    public double solutionWeight(){
        return solutionWeight;
    }

    private void solutionBuilder(Vertex start, Vertex goal){
        solution = new ArrayList<>();
        solutionWeight = 0;                 //initialize solution weight to 0

        if (outcome.equals(SolverOutcome.UNSOLVABLE)){
            return;
        }
        solution.add(goal);
        Vertex previousVertex = goal;         //start at end
        while (!previousVertex.equals(start)) {
            WeightedEdge<Vertex> edge = edgeTo.get(previousVertex);  //get the edge back to last vertex
            solutionWeight += edge.weight();                    //add this edge to solution weight
            previousVertex = edge.from();                   //get next vertex back
            solution.add(previousVertex);                   //add it to the list
        }
        Collections.reverse(solution);

    }

    /** return our number of states explored variable */
    @Override
    public int numStatesExplored(){
        return numOfStatesExplored;
    }

    /** return our timeSpent variable */
    @Override
    public double explorationTime(){
        return timeSpent;
    }
}
