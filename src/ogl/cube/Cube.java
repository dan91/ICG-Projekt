package ogl.cube;

import static ogl.vecmathimp.FactoryDefault.vecmath;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.FloatBuffer;

import ogl.app.MatrixUniform;
import ogl.app.OpenGLApp;
import ogl.app.Util;
import ogl.cube.RotatingCube.Vertex;
import ogl.vecmath.Color;
import ogl.vecmath.Matrix;
import ogl.vecmath.Vector;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import icg.warmup.Node;

public class Cube extends Node {

	public Cube(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	public void init() {
	    // Set background color to black.
	    glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

	    // Enable depth testing.
	    glEnable(GL11.GL_DEPTH_TEST);

	    // Bind the vertex attribute data locations for this shader program. The
	    // shader expects to get vertex and color data from the mesh. This needs to
	    // be done *before* linking the program.
	    glBindAttribLocation(program, vertexAttribIdx, "vertex");
	    glBindAttribLocation(program, colorAttribIdx, "color");

	    // Link the shader program.
	    glLinkProgram(program);
	    Util.checkLinkage(program);

	    // Bind the matrix uniforms to locations on this shader program. This needs
	    // to be done *after* linking the program.
	    modelMatrixUniform = new MatrixUniform(program, "modelMatrix");
	    viewMatrixUniform = new MatrixUniform(program, "viewMatrix");
	    projectionMatrixUniform = new MatrixUniform(program, "projectionMatrix");

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
	
	public void display(int width, int height) {
	    // Adjust the the viewport to the actual window size. This makes the
	    // rendered image fill the entire window.
	    glViewport(0, 0, width, height);

	    // Clear all buffers.
	    glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

	    // Assemble the transformation matrix that will be applied to all
	    // vertices in the vertex shader.
	    float aspect = (float) width / (float) height;

	    // The perspective projection. Camera space to NDC.
	    Matrix projectionMatrix = vecmath
	      .perspectiveMatrix(60f, aspect, 0.1f, 100f);

	    // The inverse camera transformation. World space to camera space.
	    Matrix viewMatrix = vecmath.lookatMatrix(vecmath.vector(0f, 0f, 3f),
	      vecmath.vector(0f, 0f, 0f), vecmath.vector(0f, 1f, 0f));

	    // The modeling transformation. Object space to world space.
	    Matrix modelMatrix = vecmath.rotationMatrix(vecmath.vector(1, 1, 1), angle);

	    // Activate the shader program and set the transformation matricies to the
	    // uniform variables.
	    glUseProgram(program);

	    modelMatrixUniform.set(modelMatrix);
	    viewMatrixUniform.set(viewMatrix);
	    projectionMatrixUniform.set(projectionMatrix);

	    // Enable the vertex data arrays (with indices 0 and 1). We use a vertex
	    // position and a vertex color.
	    glVertexAttribPointer(vertexAttribIdx, 3, false, 0, positionData);
	    glEnableVertexAttribArray(vertexAttribIdx);
	    glVertexAttribPointer(colorAttribIdx, 3, false, 0, colorData);
	    glEnableVertexAttribArray(colorAttribIdx);

	    // Draw the triangles that form the cube from the vertex data arrays.
	    glDrawArrays(GL11.GL_QUADS, 0, vertices.length);
	  }
	

	  // The shader program.
	  private int program;

	  // The location of the "mvpMatrix" uniform variable.
	  private MatrixUniform modelMatrixUniform;
	  private MatrixUniform viewMatrixUniform;
	  private MatrixUniform projectionMatrixUniform;

	  // The attribute indices for the vertex data.
	  public static int vertexAttribIdx = 0;
	  public static int colorAttribIdx = 1;

	  // Width, depth and height of the cube divided by 2.
	  float w2 = 0.5f;
	  float h2 = 0.5f;
	  float d2 = 0.5f;

	  // The vertex program source code.
	  private String[] vsSource = {
	      "uniform mat4 modelMatrix;",
	      "uniform mat4 viewMatrix;",
	      "uniform mat4 projectionMatrix;",

	      "attribute vec3 vertex;",
	      "attribute vec3 color;",
	      "varying vec3 fcolor;",

	      "void main() {",
	      "  fcolor = color;",
	      "  gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(vertex, 1);",
	      "}" };

	  // The fragment program source code.
	  private String[] fsSource = { 
	      "varying vec3 fcolor;",
	      "void main() {", 
	      "  gl_FragColor = vec4(fcolor, 1.0);", 
	      "}" };

	  // Auxillary class to represent a single vertex.
	  public class Vertex {
	    Vector position;
	    Color color;

	    Vertex(Vector p, Color c) {
	      position = p;
	      color = c;
	    }
	  }
	  
	  static public void main(String[] args) {
		    new OpenGLApp("Rotating Cube - OpenGL ES 2.0 (lwjgl)", new RotatingCube())
		      .start();
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
	      vec(w2, h2, d2) 
	  };

	  // The colors of the cube vertices.
	  private Color[] d = { 
	      col(0, 0, 0), 
	      col(1, 0, 0), 
	      col(1, 1, 0), 
	      col(0, 1, 0),
	      col(1, 0, 1), 
	      col(0, 0, 1), 
	      col(0, 1, 1), 
	      col(1, 1, 1) 
	  };

	  // The colors of the cube vertices.
	  private Color[] c = { 
	      col(1, 0, 0), 
	      col(1, 0, 0), 
	      col(1, 0, 0), 
	      col(1, 0, 0),
	      col(0, 1, 0), 
	      col(0, 1, 0), 
	      col(0, 1, 0), 
	      col(0, 1, 0) 
	  };

	  // Vertices combine position and color information. Every four vertices define
	  // one side of the cube.
	  private Vertex[] vertices = {
	      // front
	      v(p[0], c[0]), v(p[1], c[1]), v(p[2], c[2]), v(p[3], c[3]),
	      // back
	      v(p[4], c[4]), v(p[5], c[5]), v(p[6], c[6]), v(p[7], c[7]),
	      // right
	      v(p[1], c[1]), v(p[4], c[4]), v(p[7], c[7]), v(p[2], c[2]),
	      // top
	      v(p[3], c[3]), v(p[2], c[2]), v(p[7], c[7]), v(p[6], c[6]),
	      // left
	      v(p[5], c[5]), v(p[0], c[0]), v(p[3], c[3]), v(p[6], c[6]),
	      // bottom
	      v(p[5], c[5]), v(p[4], c[4]), v(p[1], c[1]), v(p[0], c[0]) 
	  };

	  private FloatBuffer positionData;
	  private FloatBuffer colorData;

	  // Initialize the rotation angle of the cube.
	  private float angle = 0;
	
	
}
