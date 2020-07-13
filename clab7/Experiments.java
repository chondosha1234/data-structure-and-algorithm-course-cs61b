import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.util.ArrayList;

/**
 * Created by hug.
 */
public class Experiments {

    /** insert 5000 random entries and plot average depth vs number of items in chart */
    public static void experiment1() {

        BST<Integer> BiTree = new BST<>();          // BST object
        int r;    //random number
        ArrayList<Double> depth = new ArrayList<>();  // list to record the average depth as tree grows
        ArrayList<Integer> size = new ArrayList<>();  // list to record size as tree grows

        for (int i = 1; i <= 5000; i++){
            r = RandomGenerator.getRandomInt(10000);
            BiTree.add(r);
            size.add(i);
            depth.add(BiTree.averageDepth());
        }

        XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("Number of Items").yAxisTitle("Average Depth").build();
        chart.addSeries("Average Depth", size, depth);
        new SwingWrapper(chart).displayChart();
    }

    public static void experiment2() {
        BST<Integer> tree = new BST<>();
        int r;

        for (int i = 0; i <= 1000; i++){
            r = RandomGenerator.getRandomInt(1000000);
            tree.add(r);
        }
        double startingDepth = tree.averageDepth();
        ArrayList<Integer> numberOfOperations = new ArrayList<>();
        ArrayList<Double> depth = new ArrayList<>();
        //numberOfOperations.add(0);
        //depth.add(startingDepth);

        for (int M = 1; M <= 50000; M ++){
            ExperimentHelper.deleteRandom(tree);
            ExperimentHelper.insertRandom(tree);
            numberOfOperations.add(M);
            depth.add(tree.averageDepth());
        }
        XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("Number of Operations").yAxisTitle("Average Depth").build();
        chart.addSeries("Knott Experiment", numberOfOperations, depth);
        new SwingWrapper(chart).displayChart();
    }

    public static void experiment3() {
        BST<Integer> tree = new BST<>();
        int r;

        for (int i = 0; i <= 1000; i++){
            r = RandomGenerator.getRandomInt(1000000);
            tree.add(r);
        }
        double startingDepth = tree.averageDepth();
        ArrayList<Integer> numberOfOperations = new ArrayList<>();
        ArrayList<Double> depth = new ArrayList<>();
        numberOfOperations.add(0);
        depth.add(startingDepth);
        for (int M = 1; M <= 50000; M ++){
            ExperimentHelper.deleteRandomTwo(tree);
            ExperimentHelper.insertRandom(tree);
            numberOfOperations.add(M);
            depth.add(tree.averageDepth());
        }
        XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("Number of Operations").yAxisTitle("Average Depth").build();
        chart.addSeries("Knott Experiment", numberOfOperations, depth);
        new SwingWrapper(chart).displayChart();
    }

    public static void main(String[] args) {
        //experiment1();
        experiment2();
        //experiment3();
    }
}
