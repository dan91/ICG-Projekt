package ogl.cube;

import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import ogl.app.MatrixUniform;
import ogl.app.Util;
import ogl.vecmath.Matrix;

import org.lwjgl.opengl.GL20;


public class Shader {
	
	


	// The shader program.
	private int program;

	// The location of the "mvpMatrix" uniform variable.
	private MatrixUniform modelMatrixUniform;
	private MatrixUniform viewMatrixUniform;
	private MatrixUniform projectionMatrixUniform;

	// The attribute indices for the vertex data.
	public static int vertexAttribIdx = 0;
	public static int colorAttribIdx = 1;


	public Shader() {
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
		
		glUseProgram(program);

	}

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


	public void activate() {
		// Activate the shader program and set the transformation matricies to the
		// uniform variables.
	}


	//Aufgabe 2.2
	public void setModelMatrixUniform(Matrix modelMatrixUniform) {
		this.modelMatrixUniform.set(modelMatrixUniform);
	}
	public void setViewMatrixUniform(Matrix viewMatrixUniform) {
		this.viewMatrixUniform.set(viewMatrixUniform);
	}
	public void setProjectionMatrixUniform(Matrix projectionMatrixUniform) {
		this.projectionMatrixUniform.set(projectionMatrixUniform);
	}

}