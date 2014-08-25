package ogl.cube;

import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glShaderSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ogl.app.MatrixUniform;
import ogl.app.Util;
import ogl.vecmath.Matrix;

import java.util.Vector;

import org.lwjgl.opengl.GL20;

public class Shader {
	private String vsSource;
	private String fsSource;
	
	private MatrixUniform modelMatrixUniform;
	private MatrixUniform viewMatrixUniform;
	private MatrixUniform projectionMatrixUniform;

	// The attribute indices for the vertex data.
	public static int vertexAttribIdx = 0;
	public static int colorAttribIdx = 1;
	public Shader(String file) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(getClass()
				.getResourceAsStream(file)));
		String line;
		String output = "";
		Vector<String> v = new Vector<String>();
		while ((line = br.readLine()) != null) {
			output += line + "\n";
			System.out.println(output);
			v.add(line);
		}
		v.toArray();
	}

	public static void main(String args[]) throws IOException {
		Shader fs = new Shader("fs.txt");
		Shader vs = new Shader("vs.txt");
	}

	public void init() {
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
	}
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
