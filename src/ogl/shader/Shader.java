package ogl.shader;

import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;

import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import ogl.app.MatrixUniform;
import ogl.app.Util;
import ogl.vecmath.Matrix;


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
	
	// The fragment program source code.
	private String[] fsSource;
	
	// The vertex program source code.
	private String[] vsSource;


	public Shader() {
		//Import shader from txt.
        try {
        	FileReader fileReader = new FileReader("src/ogl/shader/vs.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            List<String> lines = new ArrayList<String>();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            bufferedReader.close();
            vsSource = lines.toArray(new String[lines.size()]);

            FileReader fileReader2 = new FileReader("src/ogl/shader/fs.txt");
            BufferedReader bufferedReader2 = new BufferedReader(fileReader2);
            List<String> lines2 = new ArrayList<String>();
            String line2 = null;
            while ((line2 = bufferedReader2.readLine()) != null) {
                lines2.add(line2);
            }
            bufferedReader2.close();
            fsSource = lines2.toArray(new String[lines2.size()]);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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