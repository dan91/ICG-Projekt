package ogl.triangle;

import static ogl.vecmathimp.FactoryDefault.vecmath;
import icg.warmup.Node;

import java.nio.FloatBuffer;

import ogl.scenegraph.Vertex;
import ogl.vecmath.Color;
import ogl.vecmath.Vector;

//import scenegraph.basics.Node;
//import scenegraph.basics.Shader;

public class Triangle extends Node {

	public Triangle() {
		super("");
	}




	// The attribute indices for the vertex data.
	public static int vertexAttribIdx = 0;
	public static int colorAttribIdx = 1;

	// Width, depth and height of the cube divided by 2.
	float w2 = 0.5f;
	float h2 = 0.5f;
	float d2 = 0.5f;



	
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

	//
	//      
	//         4       
	//         |
	//         |
	//     3 --|---- 2 
	//    /    *    / 
	//   0 ------- 1
	//

	// The positions of the triangle vertices.
	private Vector[] t = { 
			vec(-w2, -h2, -d2), 
			vec(w2, -h2, -d2), 
			vec(w2, -h2, d2), 
			vec(-w2, -h2, d2),
			vec(0, h2, 0)
	};

	
	// The colors of the triangle vertices.
	private Color[] c = { 
			col(0, 3, 0), 
			col(0, 2, 1), 
			col(2, 0, 0), 
			col(0, 7, 0),
			col(1, 0, 12)
			
	};

	// Vertices combine position and color information. Every tree vertices define
	// one side of the triangle.
	public Vertex[] vertices = {
			// front
			v(t[0], c[0]), v(t[1], c[1]), v(t[4], c[4]), 
			// right
			v(t[1], c[1]), v(t[2], c[2]),  v(t[4], c[4]),
			// back
			v(t[2], c[2]), v(t[3], c[3]), v(t[4], c[4]),
			// left
			v(t[3], c[3]), v(t[0], c[0]), v(t[4], c[4]), 
			// bottom
			v(t[0], c[0]), v(t[2], c[2]), v(t[1], c[1]),
			// bottom
			v(t[0], c[0]), v(t[3], c[3]), v(t[2], c[2]),

	};
	
	private FloatBuffer positionData;
	private FloatBuffer colorData;

	// Initialize the rotation angle of the triangle.
	//TODO nicht implementiert
	private float angle = 0;


	
}