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
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.prefs.BackingStoreException;

import javax.swing.plaf.SliderUI;
import javax.swing.text.Position;
import javax.xml.parsers.ParserConfigurationException;

import ogl.app.App;
import ogl.app.Input;
import ogl.app.MatrixUniform;
import ogl.app.OpenGLApp;
import ogl.app.Util;
import ogl.cube.Cube;
import ogl.cube.Plane;
import ogl.cube.Shader;
import ogl.triangle.Pyramid;
import ogl.vecmath.Color;
import ogl.vecmath.Matrix;
import ogl.vecmath.Vector;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.xml.sax.SAXException;

// A simple but complete OpenGL 2.0 ES application.
public class StartDan implements App {

	// app.OpenGLApp
	static public void main(String[] args) {
		new OpenGLApp("ToDo Liste", new StartDan()).start();
	}

	// default shader object used to display the objects in the scenegraph
	private Shader defaultshader;

	// root node of the scenegraph
	private Node root;

	// camera object
	private Camera cam;

	// pointers on active elements
	private Node activePlane;
	private Node activeLevel;
	private Node activeObject;
	private int countObject;
	private int objectsPerLevel;
	private int levels;
	private int countLevel;

	// animationshelper
	private boolean changeInProgress = false;
	private boolean zoomInProgress = false;
	private boolean zoomOutProgress = false;

	// timer
	private float timeElapsed = 0;
	private float actionTime = 0;

	Plane background;

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
		background = new Plane(defaultshader, "Background");

		
		ReaderXML reader = new ReaderXML();
		Node scene = null;
		try {
			scene = reader.getScene(new File("res/scene.xml"), defaultshader);
			System.out.println(scene.toString());

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		root = new Node("Root");

		Node plane1 = root.addNode(scene);
		//plane1.addNode(scene.getNodes());
//		// Draws level
//		for (int i = 0; i < 4; i++) {
//			// Draws objects
//			Node level = new Node("Level " + i);
//			for (int j = 0; j < 5; j++) {
//				Cube cube = new Cube(defaultshader, "Cube " + j);
//				cube.setTransformation(vecmath.translationMatrix(vecmath
//						.vector(1.5f * j, 0f, 0f)));
//				level.addNode(cube);
//			}
//			level.setTransformation(vecmath.translationMatrix(vecmath.vector(
//					0f, -1.5f * i, 0f)));
//			root.getNodes().get(0).addNode(level);
//		}
		countObject = 0;
		//activeLevel = root.getNodes().get(0).getNodes().get(0);
		activeLevel = plane1.getNode(0);
		activeObject = activeLevel.getNodes().get(countObject);

		Node sceneX = new Node("Scene2");
		Node levelX = new Node("LevelX");
		Cube cubeX = new Cube(defaultshader, "CubeX");
		cubeX.setTransformation(vecmath.translationMatrix(vecmath.vector(0, 0,
				-11)));
		levelX.addNode(cubeX);
		sceneX.addNode(levelX);
		root.getNode(0).getNode(0).getNode(0).addNode(cubeX);

		// Creates Camera
		cam = new Camera();

		activePlane = root.getNode(0);
		levels = activePlane.getNodes().size();
	}

	// Should be used for animations
	public void simulate(float elapsed, Input input) {
		objectsPerLevel = activeLevel.getNodes().size();

		timeElapsed += elapsed;
		// System.out.println(timeElapsed);

		// Animation of the camera
		if (input.isKeyDown(Keyboard.KEY_W)) {
			cam.moveUp(elapsed);
		}
		if (input.isKeyDown(Keyboard.KEY_S)) {
			cam.moveDown(elapsed);
		}
		if (input.isKeyDown(Keyboard.KEY_A)) {
			cam.moveLeft(elapsed);
		}
		if (input.isKeyDown(Keyboard.KEY_D)) {
			cam.moveRight(elapsed);
		}
		if (input.isKeyDown(Keyboard.KEY_Q)) {
			cam.moveIn(elapsed);
		}
		if (input.isKeyDown(Keyboard.KEY_Y)) {
			cam.moveOut(elapsed);
		}

		// Animation of the Objects
		if (input.isKeyDown(Keyboard.KEY_UP)) {
			if (changeInProgress == true) {
				// stop rotation of active object
				activeObject.setTransformation(vecmath.rotationMatrix(
						vecmath.xAxis(), 0).mult(
						vecmath.translationMatrix(activeObject
								.getTransformation().getPosition())));
				// increment count if count smaller amount of objects per level
				if (countLevel > 0)
					countLevel--;
				else if (countLevel == 0)
					countLevel = levels - 1;
				changeInProgress = false;

				// set active object to
				activeLevel = root.getNodes().get(0).getNodes().get(countLevel);
				activeObject = root.getNodes().get(0).getNodes()
						.get(countLevel).getNodes().get(countObject);
			}
		} else if (input.isKeyDown(Keyboard.KEY_DOWN)) {
			if (changeInProgress == true) {
				// stop rotation of active object
				activeObject.setTransformation(vecmath.rotationMatrix(
						vecmath.xAxis(), 0).mult(
						vecmath.translationMatrix(activeObject
								.getTransformation().getPosition())));
				// increment count if count smaller amount of objects per level
				if (countLevel < levels - 1)
					countLevel++;
				else if (countLevel == levels - 1)
					countLevel = 0;
				changeInProgress = false;

				// set active object to
				activeLevel = root.getNodes().get(0).getNodes().get(countLevel);
				activeObject = root.getNodes().get(0).getNodes()
						.get(countLevel).getNodes().get(countObject);
			}
		} else if (input.isKeyDown(Keyboard.KEY_RIGHT)) {
			if (changeInProgress == true) {
				// stop rotation of active object
				activeObject.setTransformation(vecmath.rotationMatrix(
						vecmath.xAxis(), 0).mult(
						vecmath.translationMatrix(activeObject
								.getTransformation().getPosition())));
				// increment count if count smaller amount of objects per level
				if (countObject < objectsPerLevel - 1)
					countObject++;
				else if (countObject == objectsPerLevel - 1)
					countObject = 0;
				changeInProgress = false;

				// set active object to
				activeObject = activeLevel.getNodes().get(countObject);
			}

		} else if (input.isKeyDown(Keyboard.KEY_LEFT)) {
			if (changeInProgress == true) {
				// stop rotation of active object
				activeObject.setTransformation(vecmath.rotationMatrix(
						vecmath.xAxis(), 0).mult(
						vecmath.translationMatrix(activeObject
								.getTransformation().getPosition())));
				// increment count if count smaller amount of objects per level
				if (countObject > 0)
					countObject--;
				else if (countObject == 0)
					countObject = objectsPerLevel - 1;
				changeInProgress = false;

				// set active object to
				activeObject = activeLevel.getNodes().get(countObject);
			}

		} else if (input.isKeyDown(Keyboard.KEY_RETURN)) {
			if (changeInProgress == true) {
				if (activeObject.getNodes().size() != 0) {
					zoomInProgress = true;
					actionTime = timeElapsed;
				}
			}

		} else if (input.isKeyDown(Keyboard.KEY_BACK)) {
			if (changeInProgress == true) {
				if (activePlane.getParent() != root) {
					zoomOutProgress = true;
					actionTime = timeElapsed;
				}
			}
		} else if (input.isKeyDown(Keyboard.KEY_P)) {
			if (changeInProgress == true) {
				((Cube) activeObject).setTexture();
			}
		}

		else {
			changeInProgress = true;
		}

		if (zoomInProgress == true && actionTime + 2.0 > timeElapsed) {
			cam.moveIn(elapsed);
		} else if (zoomInProgress == true) {
			zoomInProgress = false;
			activePlane = activeObject.getNode(0);
			//if (activeObject.getNodes().size() == 0) {
				//activeObject = activePlane.getNode(0).getNode(0);
			//} else
			activeObject = activeObject.getNodes().get(0);
		}
		if (zoomOutProgress == true && actionTime + 2.0 > timeElapsed) {
			cam.moveOut(elapsed);
		} else if (zoomOutProgress == true) {
			zoomOutProgress = false;
			activeObject = activePlane.getParent();
			activePlane = activePlane.getParent().getParent().getParent();
		}

		activeObject.setTransformation(vecmath.rotationMatrix(vecmath.xAxis(),
				angle).mult(
				vecmath.translationMatrix(activeObject.getTransformation()
						.getPosition())));
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
		Matrix viewMatrix = cam.matrix();

		// The modeling transformation. Object space to world space.
		Matrix modelMatrix = vecmath.translationMatrix(vecmath.vector(0, 0, 0));

		// Sets Transformations
		// activeObject.setTransformation(vecmath.translationMatrix(vecmath.vector(-2f,
		// -1.5f, 4f)));

		root.getNodes().get(0).getNodes().get(0)
				.setTransformation(vecmath.translationMatrix(move));

		// Shader
		defaultshader.setModelMatrixUniform(modelMatrix);
		defaultshader.setProjectionMatrixUniform(projectionMatrix);
		defaultshader.setViewMatrixUniform(viewMatrix);

		// Renders the Object with some magic
		activePlane.display(modelMatrix);
		if (activeObject.getNodes().size() != 0) {
			activeObject.getNode(0).display(modelMatrix);
		}
		background.display(vecmath.matrix(vecmath.vector(1, 0, 0),
				vecmath.vector(0, 1, 0), vecmath.vector(0, 0, 1)));
	}

}
