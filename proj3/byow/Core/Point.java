package byow.Core;

public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }


    /** 2 points are equal if they have the same coordinates (not if they are the same object reference)
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o){
        Point other = (Point) o;
        return (getX() == other.getX() && getY() == other.getY());
    }
}
