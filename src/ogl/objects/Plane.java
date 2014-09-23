package ogl.objects;

import static ogl.vecmathimp.FactoryDefault.vecmath;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.io.File;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import ogl.app.Texture;
import ogl.scenegraph.Node;
import ogl.scenegraph.Vertex;
import ogl.shader.ColorAlpha;
import ogl.shader.Shader;
import ogl.vecmath.Color;
import ogl.vecmath.Matrix;
import ogl.vecmath.TexCoord;
import ogl.vecmath.Vector;
import ogl.shader.ColorAlpha;

public class Plane extends Node {

	Shader defaultshader;
	
	
	private FloatBuffer positionData;
	private FloatBuffer colorData;
	private FloatBuffer textureData;
	
	Texture tex;
	
	public Plane(Shader defaultshader, String name) {
		super(name);
		this.defaultshader = defaultshader;

		// Prepare the vertex data arrays.
		// Compile vertex data into a Java Buffer data structures that can be
		// passed to the OpenGL API efficently.
		positionData = BufferUtils.createFloatBuffer(vertices.length
				* vecmath.vectorSize());
		colorData = BufferUtils.createFloatBuffer(vertices.length
				* (vecmath.colorSize()+1));
		
		textureData = BufferUtils.createFloatBuffer(vertices.length * 2);

		for (Vertex v : vertices) {
			positionData.put(v.position.asArray());
			colorData.put(v.colorA.asArray());
			textureData.put(v.texture.asArray());
		}
		positionData.rewind();
		colorData.rewind();
		textureData.rewind();
		
		tex = new Texture(new File("res/white.PNG"));
		
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
		glVertexAttribPointer(colorAttribIdx, 4, false, 0, colorData);
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
	float w2 = 20f;
	float h2 = 20f;
	float d2 = 0f;

	// Make construction of vertices easy on the eyes.
	private Vertex v(Vector p, ColorAlpha c, TexCoord t) {
		return new Vertex(p, c, t);
	}

	// Make construction of vectors easy on the eyes.
	private Vector vec(float x, float y, float z) {
		
		return vecmath.vector(x, y, z);
	}

	// Make construction of colors easy on the eyes.
	private ColorAlpha col(float r, float g, float b) {
		return new ColorAlpha(1,1,1,0.4f);
	}
	
	private TexCoord tex(float x, float y){
		return new TexCoord(x, y);
	}

	  // The colors of the cube vertices.
	  private ColorAlpha[] c = { 
	      col(0, 0, 2.55f), 
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
		  };
	  

		  // Vertices combine position and color information. Every four vertices define
		  // one side of the cube.
		  private Vertex[] vertices = {
		      // front
		      v(p[0], c[0], t[0]), v(p[2], c[0], t[1]), v(p[3], c[0], t[2]),
		      // front 2
		      v(p[0], c[0], t[0]), v(p[1], c[0], t[1]), v(p[2], c[0], t[2]),
		     
		      
		  };
	  
}
