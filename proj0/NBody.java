public class NBody{

  public static double readRadius (String s){
    In in = new In(s);

    int n = in.readInt();
    double radius = in.readDouble();
    return radius;
  }

  public static Body[] readBodies(String s){
    In in = new In(s);

    int n = in.readInt();
    Body[] bodies = new Body[n];
    double radius = in.readDouble();

    for (int i = 0; i < bodies.length; i++){
      double xxPos = in.readDouble();
      double yyPos = in.readDouble();
      double xxVel = in.readDouble();
      double yyVel = in.readDouble();
      double mass = in.readDouble();
      String img = in.readString();
      Body b = new Body(xxPos, yyPos, xxVel, yyVel, mass, img);
      bodies[i] = b;
    }

    return bodies;
  }

  /** Create an animation with background and moving planets
  *  utilizes the StdDraw library  */
  public static void main (String[] args){
    double T = Double.parseDouble(args[0]);    // max time variable
    double dt = Double.parseDouble(args[1]);   // rate of time change
    double t;                                  // changing time variable
    String filename = args[2];                // string of data file with info for universe
    double radius = readRadius(filename);    // radius of universe
    Body[] bodies = readBodies(filename);   // array of bodies in universe

    StdDraw.enableDoubleBuffering();      // prevent flickering
    StdDraw.setScale(-radius, radius);
    StdDraw.picture(0, 0, "images/starfield.jpg");   //create background
    StdDraw.show();                                 // used to display after creating

    for (int i = 0; i < bodies.length; i++){     // draw each body in array
      bodies[i].draw();
    }
    StdDraw.show();

    for (t = 0.0; t <= T; t += dt){
      double[] xForces = new double[bodies.length];    // force in x axis for each body
      double[] yForces = new double[bodies.length];   // force in y axis for each body

      for (int i = 0; i < bodies.length; i++){       //calculate net forces for each body and store in arrays
        xForces[i] = bodies[i].calcNetForceExertedByX(bodies);
        yForces[i] = bodies[i].calcNetForceExertedByY(bodies);
      }

      for (int j = 0; j < bodies.length; j++){
        bodies[j].update(dt, xForces[j], yForces[j]);     //update each bodies position
      }

        StdDraw.picture(0, 0, "images/starfield.jpg");
        for (int i = 0; i < bodies.length; i++){     // draw each body in array
          bodies[i].draw();
        }
        StdDraw.show();
        StdDraw.pause(10);
    }

  }
}
