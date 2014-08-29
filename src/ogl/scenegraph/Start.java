/*******************************************************************************
 * Copyright (c) 2013 Henrik Tramberend, Marc Latoschik. SWAGer
 * All rights reserved.
 *******************************************************************************/

package ogl.scenegraph;

import static ogl.vecmathimp.FactoryDefault.vecmath;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.FloatBuffer;

import javax.swing.text.Position;

import ogl.app.App;
import ogl.app.Input;
import ogl.app.MatrixUniform;
import ogl.app.OpenGLApp;
import ogl.app.Util;
import ogl.cube.Cube;
import ogl.cube.Shader;
import ogl.triangle.Pyramid;
import ogl.vecmath.Color;
import ogl.vecmath.Matrix;
import ogl.vecmath.Vector;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

// A simple but complete OpenGL 2.0 ES application.
public class Start implements App {

	// app.OpenGLApp
	static public void main(String[] args) {
		new OpenGLApp("ToDo Liste", new Start()).start();
	}

	public Cube cube1;
	public Pyramid pyramid1;
	public Node haus;

	private Shader defaultshader;

	// Initialize the rotation angle of the cube.


	@Override
	public void init() {
		// Set background color to black.
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// Enable depth testing.
		glEnable(GL11.GL_DEPTH_TEST);

		// New Shader object
		defaultshader = new Shader();

		// Creates 3D-Objects
		pyramid1 = new Pyramid(defaultshader, "Pyramide");

		cube1 = new Cube(defaultshader, "Wuerfel");
		
		haus = new Node("Haus");
		
		haus.addNode(cube1);
		haus.addNode(pyramid1);
		System.out.println(haus.toString());
		

	}



	// TODO: in Object3D auslagern
	public void simulate(float elapsed, Input input) {
		if (input.isKeyToggled(Keyboard.KEY_X)) {
			angle += 90 * elapsed;
			axis = vecmath.vector(1, 0, 0);
		}
		if (input.isKeyToggled(Keyboard.KEY_Y)) {
			axis = vecmath.vector(0, 1, 0);
			angle += 90 * elapsed;
		}
		if (input.isKeyToggled(Keyboard.KEY_Z)) {
			axis = vecmath.vector(0, 0, 1);
			angle += 90 * elapsed;
		}
		if (input.isKeyDown(Keyboard.KEY_UP)) {
			move = move.add(vecmath.vector(0, 0.03f, 0));
		}
		if (input.isKeyDown(Keyboard.KEY_DOWN)) {
			move = move.add(vecmath.vector(0, -0.03f, 0));
		}
		if (input.isKeyDown(Keyboard.KEY_LEFT)) {
			move = move.add(vecmath.vector(-0.03f, 0f, 0f));
		}
		if (input.isKeyDown(Keyboard.KEY_RIGHT)) {
			move = move.add(vecmath.vector(0.03f, 0f, 0f));
		}

	}
	
	int cubeangle = 90;
	Vector axis = vecmath.vector(0, 1, 0);
	private float angle = 0;
	Vector move = vecmath.vector(0, 0, 0);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see cg2.cube.App#display(int, int, javax.media.opengl.GL2ES2)
	 */
	@Override
	public void display(int width, int height, Input input) {
		// Adjust the the viewport to the actual window size. This makes the
		// rendered image fill the entire window.
		glViewport(0, 0, width, height);

		// Clear all buffers.
		glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		// Assemble the transformation matrix that will be applied to all
		// vertices in the vertex shader.
		float aspect = (float) width / (float) height;

		// The perspective projection. Camera space to NDC.
		Matrix projectionMatrix = vecmath.perspectiveMatrix(60f, aspect, 0.1f, 100f);

		// The inverse camera transformation. World space to camera space.
		Matrix viewMatrix = vecmath.lookatMatrix(vecmath.vector(0f, 0f, 3f), vecmath.vector(0f, 0f, 0f),
				vecmath.vector(0f, 1f, 0f));

		// The modeling transformation. Object space to world space.
		Matrix modelMatrix = vecmath.translationMatrix(move);
		if (input.isKeyDown(Keyboard.KEY_T)) {
			pyramid1.setTransformation(vecmath.translationMatrix(vecmath.vector(0.1f, 0f, 0f)));
		}
		

//		cube1.setTransformation(vecmath.translationMatrix(vecmath.vector(0f, -1.1f, 0f)).mult(vecmath.rotationMatrix(vecmath.vector(0, 1f, 0f), cubeangle)));
//		cubeangle ++;
		haus.setTransformation(vecmath.translationMatrix(vecmath.vector(0f, -1.1f, 0f)));

		defaultshader.setModelMatrixUniform(modelMatrix);
		defaultshader.setProjectionMatrixUniform(projectionMatrix);
		defaultshader.setViewMatrixUniform(viewMatrix);

		haus.display(modelMatrix);
		

	}

}
