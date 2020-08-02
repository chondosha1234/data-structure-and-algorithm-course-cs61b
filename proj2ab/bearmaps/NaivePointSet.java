package bearmaps;

import java.util.List;

public class NaivePointSet implements PointSet {

    List<Point> points;

    public NaivePointSet(List<Point> points){
        this.points = points;
    }

    @Override
    public Point nearest(double x, double y) {
        Point target = new Point(x, y);
        Point nearestPoint = null;
        double nearestDistance = Double.POSITIVE_INFINITY;
        double currDistance;
        for (Point p : points){
            currDistance = Point.distance(target, p);
            if (currDistance < nearestDistance){
                nearestDistance = currDistance;
                nearestPoint = p;
            }
        }
        return nearestPoint;
    }
}
