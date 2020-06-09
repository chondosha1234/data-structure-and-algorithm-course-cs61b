
/** Body class creates Body obejcts.  These could be planets or stars */
public class Body{
    public double xxPos;  // x position
    public double yyPos; // y position
    public double xxVel; // x velocity
    public double yyVel; // y velocity
    public double mass;  // mass
    public String imgFileName;  //file name like .gif

    /** First constructor to create Body object and set all instance variables */
    public Body(double xP, double yP, double xV, double yV, double m, String img){
      xxPos = xP;
      yyPos = yP;
      xxVel = xV;
      yyVel = yV;
      mass = m;
      imgFileName = img;
    }

    /** Second constructor to copy a Body object */
    public Body(Body b){
      xxPos = b.xxPos;
      yyPos = b.yyPos;
      xxVel = b.xxVel;
      yyVel = b.yyVel;
      mass = b.mass;
      imgFileName = b.imgFileName;
    }

}
