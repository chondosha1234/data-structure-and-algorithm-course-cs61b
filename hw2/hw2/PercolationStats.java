package hw2;

import edu.princeton.cs.introcs.StdStats;
import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.Stopwatch;

public class PercolationStats {

    private Percolation system;     //represents the percolation grid for experiment
    private int T;
    private double[] results;
    private double mean;
    private double stddev;
    private double confidenceLow;
    private double confidenceHigh;

    /** constructor for class
     * performs T experiments on a grid of N-by-N and records results into instance variables
     * */
    public PercolationStats(int N, int T, PercolationFactory pf){
        if ((N <= 0) || (T <= 0)){
            throw new IllegalArgumentException();
        }
        this.T = T;
        results = new double[T];    //results of T number of experiments

        for (int i = 0; i < T; i++){     //create a new grid and run a test on it, record in results
            double threshold;
            system = pf.make(N);
            while (!system.percolates()){
                int row = StdRandom.uniform(N);  //get random number between 0 and N
                int col = StdRandom.uniform(N);  //get second random number
                system.open(row, col);
            }
            threshold = (double) system.numberOfOpenSites() / (N*N);  //record number of sites that needed to be opened
            results[i] = threshold;              //add to results to later calculate stats
        }
        mean = mean();
        stddev = stddev();
        confidenceLow = confidenceLow();
        confidenceHigh = confidenceHigh();
    }

    /** sample mean of percolation threshold */
    public double mean(){
        return StdStats.mean(results);
    }

    /** sample standard deviation of percolation threshold */
    public double stddev(){
        return StdStats.stddev(results);
    }

    /** low endpoint of 95% confidence interval */
    public double confidenceLow(){
        return (mean() - ((1.96 * stddev()) / Math.sqrt(T)));
    }

    /** high endpoint of 95% confidence interval */
    public double confidenceHigh(){
        return (mean() + ((1.96 * stddev())) / Math.sqrt(T));
    }

    public static void main(String[] args){
        Stopwatch stop = new Stopwatch();
        PercolationStats ps = new PercolationStats(100, 10000, new PercolationFactory());
        System.out.println(ps.mean);
        System.out.println(ps.stddev);
        System.out.println(ps.confidenceHigh);
        System.out.println(ps.confidenceLow);
        System.out.println(stop.elapsedTime());
    }
}
