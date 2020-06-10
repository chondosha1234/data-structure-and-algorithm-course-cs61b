public class TestBody{
    public static void main (String[] args){
        Body earth = new Body(0, 0, 0, 0, 100000, null);
        Body mars = new Body(200, 400, 0, 0, 75000, null);
        double force;
        force = earth.calcForceExertedBy(mars);
        System.out.println(force);
        force = mars.calcForceExertedBy(earth);
        System.out.println(force);
    }

}
