package ogl.objects;

import static ogl.vecmathimp.FactoryDefault.vecmath;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.io.File;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import ogl.app.Texture;
import ogl.scenegraph.Node;
import ogl.scenegraph.Vertex;
import ogl.shader.Shader;
import ogl.vecmath.Color;
import ogl.vecmath.Matrix;
import ogl.vecmath.TexCoord;
import ogl.vecmath.Vector;

public class Cube extends Node {

	Shader defaultshader;
	Texture tex;

	private FloatBuffer positionData;
	private FloatBuffer colorData;
	private FloatBuffer textureData;
	
	public Cube(Shader defaultshader, String name) {
		super(name);
		this.defaultshader = defaultshader;

		// Prepare the vertex data arrays.
		// Compile vertex data into a Java Buffer data structures that can be
		// passed to the OpenGL API efficently.
		positionData = BufferUtils.createFloatBuffer(vertices.length
				* vecmath.vectorSize());
		colorData = BufferUtils.createFloatBuffer(vertices.length
				* vecmath.colorSize());
		
		textureData = BufferUtils.createFloatBuffer(vertices.length * 2);

		for (Vertex v : vertices) {
			positionData.put(v.position.asArray());
			colorData.put(v.color.asArray());
			textureData.put(v.texture.asArray());
		}
		positionData.rewind();
		colorData.rewind();
		textureData.rewind();
		
		tex = new Texture(new File("res/Test1.PNG"));
		
	}
	
	public Shader getDefaultshader() {
		return defaultshader;
	}

	
	@Override
	public void display(Matrix m) { 

		defaultshader.setModelMatrixUniform(m.mult(getTransformation()));

		// Enable the vertex data arrays (with indices 0 and 1). We use a vertex
		// position and a vertex color.
		glVertexAttribPointer(vertexAttribIdx, 3, false, 0, positionData);
		glEnableVertexAttribArray(vertexAttribIdx);
		glVertexAttribPointer(colorAttribIdx, 3, false, 0, colorData);
		glEnableVertexAttribArray(colorAttribIdx);
		glVertexAttribPointer(textureAttribIdx, 2, false, 0, textureData);
		glEnableVertexAttribArray(textureAttribIdx);

		// Draw the triangles that form the cube from the vertex data arrays.
		tex.bind();
		glDrawArrays(GL11.GL_TRIANGLES, 0, vertices.length);
	}
	
	public void setTexture(){
		tex = new Texture(new File("res/Test2.PNG"));
	}
	
	// The attribute indices for the vertex data.
	public static int vertexAttribIdx = 0;
	public static int colorAttribIdx = 1;
	public static int textureAttribIdx = 2;

	// Width, depth and height of the cube divided by 2.
	float w2 = 0.5f;
	float h2 = 0.5f;
	float d2 = 0.5f;

	// Make construction of vertices easy on the eyes.
	private Vertex v(Vector p, Color c, TexCoord t) {
		return new Vertex(p, c, t);
	}

	// Make construction of vectors easy on the eyes.
	private Vector vec(float x, float y, float z) {
		return vecmath.vector(x, y, z);
	}

	// Make construction of colors easy on the eyes.
	private Color col(float r, float g, float b) {
		return vecmath.color(r, g, b);
	}
	
	private TexCoord tex(float x, float y){
		return new TexCoord(x, y);
	}

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
	  
	  private TexCoord[] t = {
			  tex(0, 0),
			  tex(1, 0),
			  tex(1, 1)
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
		  private Vertex[] vertices = {
		      // front
		      v(p[0], c[0], t[0]), v(p[2], c[1], t[1]), v(p[3], c[3], t[2]),
		      // front 2
		      v(p[0], c[0], t[0]), v(p[1], c[1], t[1]), v(p[2], c[3], t[2]),
		     
		      // right
		      v(p[1], c[1], t[0]), v(p[7], c[4], t[1]), v(p[2], c[7], t[2]),
		      // right 2
		      v(p[1], c[1], t[0]), v(p[4], c[4], t[1]), v(p[7], c[7], t[2]),
		      
		      // back
		      v(p[4], c[1], t[0]), v(p[6], c[4], t[1]), v(p[7], c[7], t[2]),
		      // back 2
		      v(p[4], c[1], t[0]), v(p[5], c[4], t[1]), v(p[6], c[7], t[2]),
		      
		      // left
		      v(p[5], c[1], t[0]), v(p[3], c[4], t[1]), v(p[6], c[7], t[2]),
		      // left 2
		      v(p[5], c[1], t[0]), v(p[0], c[4], t[1]), v(p[3], c[7], t[2]),
		      
		
		      // top
		      v(p[3], c[3], t[0]), v(p[7], c[7], t[1]), v(p[6], c[6], t[2]),
		      // top2
		      v(p[3], c[3], t[0]), v(p[2], c[7], t[1]), v(p[7], c[6], t[2]),
		      
		      // bottom
		      v(p[5], c[5], t[0]), v(p[1], c[1], t[1]), v(p[0], c[0], t[2]),
		      // bottom 2
		      v(p[5], c[5], t[0]), v(p[4], c[1], t[1]), v(p[1], c[0], t[2]),
		      
		  };
	  
}
