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
	private Node scene;
	private Node activeLevel;
	private Node activeObject;
	private int countObject;
	private int objectsPerLevel;
	private int levels;

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
		levels = 2; 
		objectsPerLevel = 5;
		scene = new Node("Scene");
		for(float i = 0; i < levels; i++) {
			Node level = new Node("Level "+i);
			for(float j = 0; j < objectsPerLevel; j++) {
				Cube cube = new Cube(defaultshader, "Cube "+j);
				cube.setTransformation(vecmath.translationMatrix(
						vecmath.vector(1.5f*j, 0f, 0f)));
				level.addNode(cube);
			}
			level.setTransformation(vecmath.translationMatrix(
					vecmath.vector(0f, 1.5f*i, 0f)));
			scene.addNode(level);
		}
		countObject = 0;
		activeLevel = scene.getNodes().get(0);
		activeObject = activeLevel.getNodes().get(countObject);
	}

	// Should be used for animations
	public void simulate(float elapsed, Input input) {
		if (input.isKeyDown(Keyboard.KEY_DOWN)) {
			move = move.add(vecmath.vector(0, 0, +0.03f));
		}
		if (input.isKeyDown(Keyboard.KEY_UP)) {
			move = move.add(vecmath.vector(move.x(), move.y(), -0.03f));
		}
		if (input.isKeyToggled(Keyboard.KEY_RIGHT)) {	
			//stop rotation of active object
			activeObject.setTransformation(vecmath.rotationMatrix(vecmath.xAxis(), 0).mult(vecmath.translationMatrix(activeObject.getTransformation().getPosition())));
			// increment count if count smaller amount of objects per level
			if(countObject < objectsPerLevel-1)
				countObject++;
			else if(countObject == objectsPerLevel-1)
				countObject = 0;
			// set active object to 
			activeObject = activeLevel.getNodes().get(countObject);
			
			
		}
		
		activeObject.setTransformation(vecmath.rotationMatrix(vecmath.xAxis(), angle).mult(vecmath.translationMatrix(activeObject.getTransformation().getPosition())));
		
		angle += 90 * elapsed;

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
		Matrix viewMatrix = vecmath.lookatMatrix(vecmath.vector(3f, 0f, 7f),
				vecmath.vector(3f, 0f, 0f), vecmath.vector(0f, 1f, 0f));

		// The modeling transformation. Object space to world space.
		Matrix modelMatrix = vecmath.translationMatrix(vecmath.vector(0, 0, 0));

		// Sets Transformations
		//activeObject.setTransformation(vecmath.translationMatrix(vecmath.vector(-2f, -1.5f, 4f)));

		scene.setTransformation(vecmath.translationMatrix(move));

		// Shader
		defaultshader.setModelMatrixUniform(modelMatrix);
		defaultshader.setProjectionMatrixUniform(projectionMatrix);
		defaultshader.setViewMatrixUniform(viewMatrix);

		// Renders the Object with some magic
		scene.display(modelMatrix);
	}

}
