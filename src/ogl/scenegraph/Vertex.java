package ogl.scenegraph;

import javax.vecmath.Vector2f;

import ogl.vecmath.Color;
import ogl.vecmath.TexCoord;
import ogl.vecmath.Vector;


public class Vertex {
	 // Auxillary class to represent a single vertex.
	    public Vector position;
	    public Color color;
	    public Vector normals;
	    public TexCoord texture;
	    public Vector2f texture2;
	    public Vertex(Vector p, Color c, TexCoord t) {
	      position = p;
	      color = c;
	      texture = t;
	    }

		public Vertex(Vector vector, Color c, Vector normals, Vector2f texture) {
			this.position = vector;
			this.color = c;
			this.normals = normals;
			this.texture2 = texture;
		}
	  

}
