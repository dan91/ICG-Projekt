package ogl.triangle;

import static ogl.vecmathimp.FactoryDefault.vecmath;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.FloatBuffer;

import ogl.app.MatrixUniform;
import ogl.cube.Shader;
import ogl.scenegraph.Node;
import ogl.scenegraph.Vertex;
import ogl.vecmath.Color;
import ogl.vecmath.Matrix;
import ogl.vecmath.Vector;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;


public class Pyramid extends Node {

	
	private Shader defaultshader;


	public Pyramid (Shader defaultshader, String name) {
		super(name);
		this.defaultshader = defaultshader;
		// Prepare the vertex data arrays.
		// Compile vertex data into a Java Buffer data structures that can be
		// passed to the OpenGL API efficently.
		positionData = BufferUtils.createFloatBuffer(vertices.length
				* vecmath.vectorSize());
		colorData = BufferUtils.createFloatBuffer(vertices.length
				* vecmath.colorSize());

		for (Vertex v : vertices) {
			positionData.put(v.position.asArray());
			colorData.put(v.color.asArray());
		}
		positionData.rewind();
		colorData.rewind();
		
	}





	public void display(Matrix m) { 

		defaultshader.setModelMatrixUniform(m.mult(getTransformation()));


		// Enable the vertex data arrays (with indices 0 and 1). We use a vertex
		// position and a vertex color.
		glVertexAttribPointer(vertexAttribIdx, 3, false, 0, positionData);
		glEnableVertexAttribArray(vertexAttribIdx);
		glVertexAttribPointer(colorAttribIdx, 3, false, 0, colorData);
		glEnableVertexAttribArray(colorAttribIdx);

		// Draw the triangles that form the cube from the vertex data arrays.
		glDrawArrays(GL11.GL_TRIANGLES, 0, vertices.length);
	}



	// The attribute indices for the vertex data.
	public static int vertexAttribIdx = 0;
	public static int colorAttribIdx = 1;

	// Width, depth and height of the cube divided by 2.
	float w2 = 0.5f;
	float h2 = 0.5f;
	float d2 = 0.5f;



	// Auxillary class to represent a single vertex.
	private class Vertex {
		Vector position;
		Color color;

		Vertex(Vector p, Color c) {
			position = p;
			color = c;
		}
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
			col(1, 0, 0), 
			col(0, 0, 1), 
			col(0, 2, 0), 
			col(0, 4, 0),
			col(0, 0, 1), 
			col(1, 0, 1), 
			col(0, 0, 1), 
			col(0, 4, 1) 
	};

	// Vertices combine position and color information. Every tree vertices define
	// one side of the triangle.
	private Vertex[] vertices = {
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