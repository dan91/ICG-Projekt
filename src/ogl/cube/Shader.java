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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import ogl.app.MatrixUniform;
import ogl.app.OpenGLApp;
import ogl.app.Util;
import ogl.scenegraph.Vertex;
import ogl.triangle.PyramideApp;
import ogl.vecmath.Color;
import ogl.vecmath.Matrix;
import ogl.vecmath.Vector;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Shader {
	public String vsSource;
	public String fsSource;
	
	public MatrixUniform modelMatrixUniform;
	public MatrixUniform viewMatrixUniform;
	public MatrixUniform projectionMatrixUniform;

	// The attribute indices for the vertex data.
	public static int vertexAttribIdx = 0;
	public static int colorAttribIdx = 1;
	private FloatBuffer positionData;
	private FloatBuffer colorData;
	
	private Vertex[] vertices;
	
	public int program;

	public Shader() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(getClass()
				.getResourceAsStream("fs.txt")));
		String line;
		String output = "";
		while ((line = br.readLine()) != null) {
			output += line + "\n";
		}
		fsSource = output;
		BufferedReader br1 = new BufferedReader(new InputStreamReader(getClass()
				.getResourceAsStream("vs.txt")));
		String line1;
		String output1 = "";
		List<String> v2 = new ArrayList<String>();
		while ((line1 = br1.readLine()) != null) {
			output1 += line1 + "\n";
		}
		vsSource = output1;
		// Set background color to black.
	    glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

	    // Enable depth testing.
	    glEnable(GL11.GL_DEPTH_TEST);

	    // Create and compile the vertex shader.
	    int vs = glCreateShader(GL20.GL_VERTEX_SHADER);
	    glShaderSource(vs, vsSource);
	    glCompileShader(vs);
	    Util.checkCompilation(vs);

	    // Create and compile the fragment shader.
	    int fs = glCreateShader(GL20.GL_FRAGMENT_SHADER);
	    glShaderSource(fs, fsSource);
	    glCompileShader(fs);
	    Util.checkCompilation(fs);

	    // Create the shader program and link vertex and fragment shader
	    // together.
	    program = glCreateProgram();
	    glAttachShader(program, vs);
	    glAttachShader(program, fs);

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

	}

	
	
public MatrixUniform getModelMatrixUniform() {
	return modelMatrixUniform;
}

public void setModelMatrixUniform(MatrixUniform modelMatrixUniform) {
	this.modelMatrixUniform = modelMatrixUniform;
}

public MatrixUniform getViewMatrixUniform() {
	return viewMatrixUniform;
}

public void setViewMatrixUniform(MatrixUniform viewMatrixUniform) {
	this.viewMatrixUniform = viewMatrixUniform;
}

public MatrixUniform getProjectionMatrixUniform() {
	return projectionMatrixUniform;
}

public void setProjectionMatrixUniform(MatrixUniform projectionMatrixUniform) {
	this.projectionMatrixUniform = projectionMatrixUniform;
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
	
	
}
