package ogl.triangle;

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
import static org.lwjgl.opengl.GL11.*;


import org.apache.commons.*;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import ogl.app.App;
import ogl.app.Input;
import ogl.app.MatrixUniform;
import ogl.app.OpenGLApp;
import ogl.app.Util;
import ogl.cube.Cube;
import ogl.cube.RotatingCube;
import ogl.cube.Shader;
import ogl.scenegraph.Vertex;
import ogl.vecmath.Matrix;

public class PyramideApp {

	private Shader defaultshader;
	private Matrix m;
	
	private FloatBuffer positionData;
	private FloatBuffer colorData;
	
	
	private Vertex[] vertices;
	
	public int program;
	
	private float angle = 0;
	
//	@Override
//	public void init() {
//		try {
//			this.defaultshader = new Shader();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		//RotatingCube cube = new RotatingCube();
//		Triangle tri = new Triangle();
//		Cube cube = new Cube();
//		
//		this.vertices = ArrayUtils.addAll(tri.vertices, cube.vertices);
//		for(Vertex v : vertices) {
//			
//		}
//		// Set background color to black.
//	    glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
//
//	    // Enable depth testing.
//	    glEnable(GL11.GL_DEPTH_TEST);
//
//	    // Create and compile the vertex shader.
//	    int vs = glCreateShader(GL20.GL_VERTEX_SHADER);
//	    glShaderSource(vs, defaultshader.vsSource);
//	    glCompileShader(vs);
//	    Util.checkCompilation(vs);
//
//	    // Create and compile the fragment shader.
//	    int fs = glCreateShader(GL20.GL_FRAGMENT_SHADER);
//	    glShaderSource(fs, defaultshader.fsSource);
//	    glCompileShader(fs);
//	    Util.checkCompilation(fs);
//
//	    // Create the shader program and link vertex and fragment shader
//	    // together.
//	    program = glCreateProgram();
//	    glAttachShader(program, vs);
//	    glAttachShader(program, fs);
//
//	    // Bind the vertex attribute data locations for this shader program. The
//	    // shader expects to get vertex and color data from the mesh. This needs to
//	    // be done *before* linking the program.
//	    glBindAttribLocation(program, defaultshader.vertexAttribIdx, "vertex");
//	    glBindAttribLocation(program, defaultshader.colorAttribIdx, "color");
//
//	    // Link the shader program.
//	    glLinkProgram(program);
//	    Util.checkLinkage(program);
//
//	    // Bind the matrix uniforms to locations on this shader program. This needs
//	    // to be done *after* linking the program.
//	    defaultshader.modelMatrixUniform = new MatrixUniform(program, "modelMatrix");
//	    defaultshader.viewMatrixUniform = new MatrixUniform(program, "viewMatrix");
//	    defaultshader.projectionMatrixUniform = new MatrixUniform(program, "projectionMatrix");
//		
//		
//	    // Prepare the vertex data arrays.
//		// Compile vertex data into a Java Buffer data structures that can be
//		// passed to the OpenGL API efficently.
//		positionData = BufferUtils.createFloatBuffer(vertices.length
//				* vecmath.vectorSize());
//		colorData = BufferUtils.createFloatBuffer(vertices.length
//				* vecmath.colorSize());
//
//		for (Vertex ver : vertices) {
//			positionData.put(ver.position.asArray());
//			colorData.put(ver.color.asArray());
//		}
//		int ind = 0;
//	    while(ind < 17) {
//	    	//Vector new =  new Vector(vertices[ind].position.x()*2;
//	    	ind++;
//		}
//		positionData.rewind();
//		colorData.rewind();
//		
//	}
//
//	@Override
//	public void simulate(float elapsed, Input input) {
//		 // Pressing key 'r' toggles the cube animation.
//	    if (input.isKeyToggled(Keyboard.KEY_R))
//	      // Increase the angle with a speed of 90 degrees per second.
//	      angle += 90 * elapsed;		
//	}
//
//	@Override
//	public void display(int width, int height) {
////		// Adjust the the viewport to the actual window size. This makes the
//	    // rendered image fill the entire window.
//	    glViewport(0, 0, width, height);
//
//	    // Clear all buffers.
//	    glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
//
//	    // Assemble the transformation matrix that will be applied to all
//	    // vertices in the vertex shader.
//	    float aspect = (float) width / (float) height;
//
//	    // The perspective projection. Camera space to NDC.
//	    Matrix projectionMatrix = vecmath
//	      .perspectiveMatrix(60f, aspect, 0.1f, 100f);
//
//	    // The inverse camera transformation. World space to camera space.
//	    Matrix viewMatrix = vecmath.lookatMatrix(vecmath.vector(0f, 0f, 3f),
//	      vecmath.vector(0f, 0f, 0f), vecmath.vector(0f, 1f, 0f));
//
//	    // The modeling transformation. Object space to world space.
//	    Matrix modelMatrix = vecmath.rotationMatrix(vecmath.vector(1, 1, 1), this.angle);
//
//		// Activate the shader program and set the transformation matricies to the
//	    // uniform variables.
//	    glUseProgram(program);
//
//	    defaultshader.modelMatrixUniform.set(modelMatrix);
//	    defaultshader.viewMatrixUniform.set(viewMatrix);
//	    defaultshader.projectionMatrixUniform.set(projectionMatrix);
//	    
//	    
//	    // Enable the vertex data arrays (with indices 0 and 1). We use a vertex
//	    // position and a vertex color.
//	    glVertexAttribPointer(defaultshader.vertexAttribIdx, 3, false, 0, positionData);
//	    glEnableVertexAttribArray(defaultshader.vertexAttribIdx);
//	    glVertexAttribPointer(defaultshader.colorAttribIdx, 3, false, 0, colorData);
//	    glEnableVertexAttribArray(defaultshader.colorAttribIdx);
//	    glTranslatef(1.5f,1f,0.1f);
//	    // Draw the triangles that form the cube from the vertex data arrays.
//	    
//	    glDrawArrays(GL11.GL_TRIANGLES, 0, 18);
//	    glDrawArrays(GL11.GL_TRIANGLES, 18, 36);
//	    
//	 
//	    
//	    
//	}
	
	 static public void main(String[] args) {
		    new OpenGLApp("Eigener Shader", new PyramideApp())
		      .start();
	}

}
