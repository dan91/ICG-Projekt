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

import java.awt.Font;
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
public class StartDan implements App {

	// app.OpenGLApp
	static public void main(String[] args) {
		new OpenGLApp("ToDo Liste", new StartDan()).start();
	}

	public Cube cube1;
	public Cube cube2;
	public Cube cube3;

	private Shader defaultshader;
	private float angleRotateX;
	private Node level1;
	private Node level2;
	private Cube cube4;
	private Cube cube5;
	private Cube cube6;

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
		cube1 = new Cube(defaultshader, "Wuerfel");
		cube2 = new Cube(defaultshader, "Wuerfel 2");
		cube3 = new Cube(defaultshader, "Wuerfel 3");
		cube4 = new Cube(defaultshader, "Wuerfel 4");
		cube5 = new Cube(defaultshader, "Wuerfel 5");
		cube6 = new Cube(defaultshader, "Wuerfel 6");

		level1 = new Node("Level1");
		level2 = new Node("Level2");
		
		level1.addNode(cube1);
		level1.addNode(cube2);
		level1.addNode(cube3);
		
		level2.addNode(cube4);
		level2.addNode(cube5);
		level2.addNode(cube6);
		
	}

	// Should be used for animations
	public void simulate(float elapsed, Input input) {

		if (input.isKeyDown(Keyboard.KEY_DOWN)) {
			move = move.add(vecmath.vector(0, 0, +0.03f));
		}
		if (input.isKeyDown(Keyboard.KEY_UP)) {
			move = move.add(vecmath.vector(move.x(), move.y(), -0.03f));
		}

		
		//angle1 += 90 * elapsed;

	}

	// Some class variables used to manipulate the modelmatrix in the display
	// methode
	private float angle = 0;
	Vector axis = vecmath.vector(0, 1, 0);
	Vector move = vecmath.vector(0, 0, 0);
	Vector rotate = vecmath.vector(0, 0, 0);
	private float angle1;

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
		Matrix projectionMatrix = vecmath.perspectiveMatrix(60f, aspect, 0.1f,
				100f);

		// The inverse camera transformation. World space to camera space.
		Matrix viewMatrix = vecmath.lookatMatrix(vecmath.vector(-1f, 0f, 5f),
				vecmath.vector(0f, 0f, 0f), vecmath.vector(0f, 1f, 0f));

		// The modeling transformation. Object space to world space.
		Matrix modelMatrix = vecmath.translationMatrix(vecmath.vector(0, 0, 0));

		// Sets Transformations
		
		// translation of cubes so they don't overlap
		cube1.setTransformation(vecmath.translationMatrix(
				vecmath.vector(-1.5f, 0f, 0f)));
		cube2.setTransformation(vecmath.translationMatrix(
				vecmath.vector(0f, 0f, 0f)));
		cube3.setTransformation(vecmath.translationMatrix(
				vecmath.vector(1.5f, 0f, 0f)));
		cube4.setTransformation(vecmath.translationMatrix(
				vecmath.vector(-1.5f, 1f, 0f)));
		cube5.setTransformation(vecmath.translationMatrix(
				vecmath.vector(0f, 1f, 0f)));
		cube6.setTransformation(vecmath.translationMatrix(
				vecmath.vector(1.5f, 1f, 0f)));
		
		level1.setTransformation(vecmath.translationMatrix(move));

		// Shader
		defaultshader.setModelMatrixUniform(modelMatrix);
		defaultshader.setProjectionMatrixUniform(projectionMatrix);
		defaultshader.setViewMatrixUniform(viewMatrix);

		// Renders the Object with some magic
		level1.display(modelMatrix);
		level2.display(modelMatrix);
	}

}
