package ogl.cube;

import static ogl.vecmathimp.FactoryDefault.vecmath;
import icg.warmup.Node;

import java.nio.FloatBuffer;

import ogl.scenegraph.Vertex;
import ogl.vecmath.Color;
import ogl.vecmath.Vector;

public class Cube extends Node {

	public Cube() {
		super("test");
	}

	  // Make construction of vertices easy on the eyes.
	  private Vertex v(Vector p, Color c) {
	    return new Vertex(p, c);
	  }

	  // Make construction of vectors easy on the eyes.
	  private Vector vec(float x, float y, float z) {
	    return vecmath.vector(x, y, z);
	  }

	  // Make construction of colors easy on the eyes.
	  private Color col(float r, float g, float b) {
	    return vecmath.color(r, g, b);
	  }

	  // Width, depth and height of the cube divided by 2.
	  float w2 = 0.5f;
	  float h2 = 0.5f;
	  float d2 = 0.5f;

	  // The colors of the cube vertices.
	  private Color[] c = { 
	      col(0, 0, 0), 
	      col(1, 0, 0), 
	      col(1, 1, 0), 
	      col(0, 1, 0),
	      col(1, 0, 1), 
	      col(0, 0, 1), 
	      col(0, 1, 1), 
	      col(1, 1, 1),
	      col(1, 23, 1) 
	  };
	  // The vertex program source code.
	
	
	  //
	  //     6 ------- 7 
	  //   / |       / | 
	  //  3 ------- 2  | 
	  //  |  |      |  | 
	  //  |  5 -----|- 4 
	  //  | /       | / 
	  //  0 ------- 1
	  //
	  
	  // The positions of the cube vertices.
	  private Vector[] p = { 
		      vec(-w2, -h2, -d2), 
		      vec(w2, -h2, -d2),
		      vec(w2, h2, -d2), 
		      vec(-w2, h2, -d2), 
		      vec(w2, -h2, d2), 
		      vec(-w2, -h2, d2),
		      vec(-w2, h2, d2), 
		      vec(w2, h2, d2),
		      vec(0, 2*h2, 0)
		  };

		  // Vertices combine position and color information. Every four vertices define
		  // one side of the cube.
		  public Vertex[] vertices = {
		      // front
		      v(p[0], c[0]), v(p[2], c[1]), v(p[3], c[3]),
		      // front 2
		      v(p[0], c[0]), v(p[1], c[1]), v(p[2], c[3]),
		     
		      // right
		      v(p[1], c[1]), v(p[7], c[4]), v(p[2], c[7]),
		      // right 2
		      v(p[1], c[1]), v(p[4], c[4]), v(p[7], c[7]),
		      
		      // back
		      v(p[4], c[1]), v(p[6], c[4]), v(p[7], c[7]),
		      // back 2
		      v(p[4], c[1]), v(p[5], c[4]), v(p[6], c[7]),
		      
		      // left
		      v(p[5], c[1]), v(p[3], c[4]), v(p[6], c[7]),
		      // left 2
		      v(p[5], c[1]), v(p[0], c[4]), v(p[3], c[7]),
		      
		
		      // top
		      v(p[3], c[3]), v(p[7], c[7]), v(p[6], c[6]),
		      // top2
		      v(p[3], c[3]), v(p[2], c[7]), v(p[7], c[6]),
		      
		      // bottom
		      v(p[5], c[5]), v(p[1], c[1]), v(p[0], c[0]),
		      // bottom 2
		      v(p[5], c[5]), v(p[4], c[1]), v(p[1], c[0]),
		      
		  };

		  //
		  //     6 ------- 7 
		  //   / |       / | 
		  //  3 ------- 2  | 
		  //  |  |      |  | 
		  //  |  5 -----|- 4 
		  //  | /       | / 
		  //  0 ------- 1
		  //
		  
	  private FloatBuffer positionData;
	  private FloatBuffer colorData;

	  // Initialize the rotation angle of the cube.
	  private float angle = 30;
	 
	
}
