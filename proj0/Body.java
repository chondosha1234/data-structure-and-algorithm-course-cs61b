
/** Body class creates Body obejcts.  These could be planets or stars */
public class Body{
    public double xxPos;  // x position
    public double yyPos; // y position
    public double xxVel; // x velocity
    public double yyVel; // y velocity
    public double mass;  // mass
    public String imgFileName;  //file name like .gif
    static final double G = 6.67e-11;

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

    /** Calculate the distance between 'this' body and another Body
     * uses the equation r^2 = dx^2 + dy^2 */
    public double calcDistance(Body b){
      double r;  //distance between bodies
      double dx; // distance in x axis
      double dy; // distance in y axis

      dx = b.xxPos - this.xxPos;
      dy = b.yyPos - this.yyPos;
      r = Math.sqrt((dx*dx) + (dy*dy));
      return r;
    }

    /** calculates force exerted on body object by body b
     * uses equations  F = (G * m1 * m2)/ r2   */
    public double calcForceExertedBy(Body b){
      double force;  //force exerted on this body by body b
      double r = this.calcDistance(b);   //distance between bodies
      double m1 = this.mass;
      double m2 = b.mass;

      force = (G * m1 * m2) / (r*r);
      return force;
    }

    /** calculates the force exerted in the x axis only */
    public double calcForceExertedByX(Body b){
      double fx;   // force in x axis
      double dx = b.xxPos - this.xxPos;  // distance in x axis
      double r = this.calcDistance(b);  //distance between bodies

      fx = (this.calcForceExertedBy(b) * dx) / r;
      return fx;
    }

    /** calculates the force exerted in the y axis only */
    public double calcForceExertedByY(Body b){
      double fy;   //force in x axis
      double dy = b.yyPos - this.yyPos;
      double r = this.calcDistance(b);

      fy = (this.calcForceExertedBy(b) * dy) / r;
      return fy;
    }

    /** calculate net force in x axis on a body from multiple other bodies */
    public double calcNetForceExertedByX(Body[] bodies){
      double fxnet = 0;  // net x axis force
      for (int i = 0; i < bodies.length; i++){
        if (this.equals(bodies[i]))
          continue;
        fxnet += this.calcForceExertedByX(bodies[i]);  //sum all forces through array
      }
      return fxnet;
    }

    /** calculate net force in y axis on a body from multiple other bodies */
    public double calcNetForceExertedByY(Body[] bodies){
      double fynet = 0;  // net y axis force
      for (int i = 0; i < bodies.length; i++){
        if (this.equals(bodies[i]))
          continue;
        fynet += this.calcForceExertedByY(bodies[i]);  // sum all forces through array
      }
      return fynet;
    }

    /** updates the body's position in space based on forces applied to it */
    public void update(double dt, double fX, double fY){
      double ax, ay;    // acceleration in x and y axis

      ax = fX / this.mass;
      ay = fY / this.mass;
      this.xxVel += (dt * ax);
      this.yyVel += (dt * ay);
      this.xxPos += (dt * this.xxVel);
      this.yyPos += (dt * this.yyVel);

    }

    public void draw(){
      String file = "images/" + this.imgFileName;
      StdDraw.picture(this.xxPos, this.yyPos, file);
    }

}
