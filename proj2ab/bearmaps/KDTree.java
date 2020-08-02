package bearmaps;

import java.util.List;

public class KDTree implements PointSet{

    /** Node class to construct a tree
     * each Node object will have a POINT variable to hold the point object,
     * a left and right child and a DIMENSION variable to determine which dimension to use for
     * comparison
     */
    private class Node{
        Point p;            //pair the point object with the node
        Node left, right;  //children (left is down, right is up  for 2D )
        char dimension;    //determines which dimension to compare by (x or y)

        public Node(Point p, char dimension){
            this.p = p;
            this.dimension = dimension;
        }
    }

    private Node root;

    public KDTree(List<Point> points){
        for (Point p : points){
            insert(p);
        }
    }

    /** insert method, which starts at root, and begins with comparing by x dimension
     */
    private void insert(Point p){
        root = insert(root, p, 'x');
    }
    /** can be private, because it will only be used inside this class to construct the KD tree
     * KDTree should be immutable, so a user wouldn't ever call insert
     */
    private Node insert(Node point, Point p, char dimension){
        if (point == null) return new Node(p, dimension);
        if (dimension == 'x'){
            if (p.getX() >= point.p.getX()){
                point.right = insert(point.right, p, 'y');
            }else if(p.getX() < point.p.getX()){
                point.left = insert(point.left, p, 'y');
            }
        }else if (dimension == 'y'){
            if (p.getY() >= point.p.getY()){
                point.right = insert(point.right, p, 'x');
            }else if(p.getY() < point.p.getY()){
                point.left = insert(point.left, p, 'x');
            }
        }
        return point;
    }


    @Override
    public Point nearest(double x, double y){
        Point p = new Point(x, y);
        Node closest = nearestHelper(root, p, root);
        return closest.p;
    }

    private Node nearestHelper(Node node, Point p, Node best){  //need a way to return the point associated with best

        if(node == null){
            return best;
        }

        Point thisPoint = node.p;
        double distance = Point.distance(thisPoint, p); //calculate new distance
        double bestDistance = Point.distance(best.p, p);   //calculate the distance from best

        if (distance < bestDistance){    //compare the distances and assign the better one to variable to pass to next call
            best = node;
            bestDistance = distance;
        }

        Node goodSide, badSide;     //will be left and right of node, based on which is 'good' and 'bad' for searching

        if (node.dimension == 'x'){
            double badDistance = Point.distance(new Point(thisPoint.getX(), p.getY()), p);

            if(p.getX() > thisPoint.getX()){
                goodSide = node.right;
                badSide = node.left;
            }else{
                goodSide = node.left;
                badSide = node.right;
            }
            best = nearestHelper(goodSide, p, best); //always check good side
            if (badDistance < bestDistance){
                best = nearestHelper(badSide, p, best); //check the bad side if its possible to have a better value
            }

        }else if (node.dimension == 'y'){
            double badDistance = Point.distance(new Point(p.getX(), thisPoint.getY()), p);

            if(p.getY() > thisPoint.getY()){
                goodSide = node.right;
                badSide = node.left;
            }else{
                goodSide = node.left;
                badSide = node.right;
            }
            best = nearestHelper(goodSide, p, best);  //always check good side
            if (badDistance < bestDistance){
                best = nearestHelper(badSide, p, best);
            }
        }
        return best;
    }

}
