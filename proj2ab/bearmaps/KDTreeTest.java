package bearmaps;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;


public class KDTreeTest {

    @Test
    public void testNearest(){
        ArrayList<Point> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++){
            list.add(new Point(StdRandom.uniform()*100, StdRandom.uniform()*100));
        }
        NaivePointSet nps = new NaivePointSet(list);
        KDTree kdt = new KDTree(list);
        for (int j = 0; j < 1000; j++) {
            Point randomPoint = new Point(StdRandom.uniform()*100, StdRandom.uniform()*100);
            Point npsNearest = nps.nearest(randomPoint.getX(), randomPoint.getY());
            Point kdtNearest = kdt.nearest(randomPoint.getX(), randomPoint.getY());
            assertEquals(npsNearest, kdtNearest);
        }
    }

    public static void main(String[] args){
       /* Point p1 = new Point(1.1, 2.2); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);
        Point p4 = new Point(5.0, 1.4);
        Point p5 = new Point(-1.2, 4.5);

        KDTree tree = new KDTree(List.of(p1, p2, p3, p4, p5));
        System.out.println("hello"); */

        Point p1 = new Point(1.1, 2.2); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);

        NaivePointSet nn = new NaivePointSet(List.of(p1, p2, p3));
        Point ret = nn.nearest(3.0, 4.0); // returns p2
        System.out.println(ret.getX()); // evaluates to 3.3
        System.out.println(ret.getY()); // evaluates to 4.4
    }
}
