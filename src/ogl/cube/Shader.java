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

import ogl.app.Util;

import org.lwjgl.opengl.GL20;

public class Shader {

	public Shader() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(getClass()
				.getResourceAsStream("Shader.txt")));
		String line;
		String output = "";
		while ((line = br.readLine()) != null) {
			output += line + "\n";
		}
		System.out.println(output);
		br.close();
	}

	public static void main(String args[]) throws IOException {
		Shader s = new Shader();
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
}
