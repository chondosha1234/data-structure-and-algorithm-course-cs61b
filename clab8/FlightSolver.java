import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Solver for the Flight problem (#9) from CS 61B Spring 2018 Midterm 2.
 * Assumes valid input, i.e. all Flight start times are >= end times.
 * If a flight starts at the same time as a flight's end time, they are
 * considered to be in the air at the same time.
 */
public class FlightSolver {
    private PriorityQueue<Flight> mostPassengers;  //keep track of flights in air at same time
    private int current = 0;   //current number of passengers in air
    private int maxPassengers = 0;  //maximum number of passengers in air

    public FlightSolver(ArrayList<Flight> flights) {

        Comparator<Flight> timeCompare = (f1, f2) -> {    //comparator to structure PQ with earliest time at head
            if (f1.startTime > f2.startTime){
                return 1;
            } else if (f1.startTime < f2.startTime){
                return -1;
            }
            return 0;
        };

        mostPassengers = new PriorityQueue<>(timeCompare);
        for (Flight fl : flights){
            mostPassengers.add(fl);
            boolean timeTest = false;   //control while loop
            while (!timeTest) {
                Flight earliestFlight = mostPassengers.peek();  //set the earliest flight to variable
                if (fl.startTime > earliestFlight.endTime) {     //check to see if flights have ended before this one starts
                    current -= earliestFlight.passengers;
                    mostPassengers.remove();                //remove ended flights from queue
                }else {
                    timeTest = true;     //if the earliest flight is still going, get out of loop
                }
            }
            current += fl.passengers;
            if (current > maxPassengers){
                maxPassengers = current;
            }
        }
    }

    public int solve() {
        return maxPassengers;
    }

}
