package ogl.scenegraph;

import ogl.vecmath.Color;
import ogl.vecmath.Vector;


public class Vertex {
	 // Auxillary class to represent a single vertex.
	    public Vector position;
	    public Color color;

	    public Vertex(Vector p, Color c) {
	      position = p;
	      color = c;
	    }
	  

}
