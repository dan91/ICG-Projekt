package ogl.scenegraph;

import ogl.vecmath.Color;
import ogl.vecmath.TexCoord;
import ogl.vecmath.Vector;


public class Vertex {
	 // Auxillary class to represent a single vertex.
	    public Vector position;
	    public Color color;
	    public TexCoord texture;

	    public Vertex(Vector p, Color c, TexCoord t) {
	      position = p;
	      color = c;
	      texture = t;
	    }
	  

}
