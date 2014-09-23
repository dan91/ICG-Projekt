/*******************************************************************************
 * Copyright (c) 2013 Henrik Tramberend, Marc Latoschik. SWAGer
 * All rights reserved.
 *******************************************************************************/

package ogl.scenegraph;

import static ogl.vecmathimp.FactoryDefault.vecmath;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;

import java.io.File;
import java.io.IOException;
import java.security.acl.LastOwnerException;

import javax.xml.parsers.ParserConfigurationException;

import ogl.app.App;
import ogl.app.Input;
import ogl.app.OpenGLApp;
import ogl.objects.Cube;
import ogl.objects.Plane;
import ogl.objects.Pyramide;
import ogl.parser.ObjLoader;
import ogl.parser.ReaderXML;
import ogl.shader.Shader;
import ogl.vecmath.Matrix;
import ogl.vecmath.Vector;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.xml.sax.SAXException;

// A simple but complete OpenGL 2.0 ES application.
public class Start implements App {

	// app.OpenGLApp
	static public void main(String[] args) {
		new OpenGLApp("ToDo Liste", new Start()).start();
	}

	// default shader object used to display the objects in the scenegraph
	private Shader defaultshader;

	// root node of the scenegraph
	private Node root;

	// camera object
	private Camera cam;
	private FreeFlight free;

	// pointers on active elements
	private Node activeObject;
	private Node activePlane;
	private int countObject;
	private int objectsPerLevel;

	// animationshelper
	private boolean changeInProgress = false;
	private boolean zoomInProgress = false;
	private boolean zoomOutProgress = false;

	// timer
	private float camLastPosi = 0;

	Plane background;

	private ObjLoader loader;

	// Initialize the rotation angle of the cube.

	@Override
	public void init() {
		// Set background color to black.
		glClearColor(.4f, 0.1f, 0.5f, 0.0f);

		// Enable depth testing.
		glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		//
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		// New Shader object
		defaultshader = new Shader();

		// Creates 3D-Objects
		background = new Plane(defaultshader, "Background");
		background2 = new Plane(defaultshader, "Background");
		background3 = new Plane(defaultshader, "Background");

		// //Imports the structure of the scene via XML
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

		// Adds the imported structure to the root node
		root = scene;
		// Node scene = new Node("Scene");
		// scene.addNode(new Cube(defaultshader, "Cube1"));
		// scene.addNode(new Cube(defaultshader, "Cube2"));
		// scene.addNode(new Cube(defaultshader, "Cube3"));
		// scene.getNode(0).getNode(0).getNode(2).addNode(new
		// Cube(defaultshader, "Cube3_1"));
		// scene.addNode(new Cube(defaultshader, "Cube4"));
		// scene.addNode(new Cube(defaultshader, "Cube5"));
		// scene.addNode(new Cube(defaultshader, "Cube6"));
		root.addNode(scene);

		// Set values for the selection of objects
		countObject = 0;
		activeObject = root.getNode(0).getNode(0).getNode(0).getNode(0);
		activePlane = root.getNode(0).getNode(0);
		objectsPerLevel = scene.getNode(0).getNode(0).getNode(0).getNodes()
				.size();

		// Creates Camera
		cam = new Camera("cam");
		free = new FreeFlight("FreeFlight");
		loader = null;
		try {
			loader = new ObjLoader("res/cube.obj");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Should be used for animations
	public void simulate(float elapsed, Input input) {

		
			cam.setEyeY(Mouse.getY());
			cam.setEyeX(Mouse.getX());

		free.tansform(elapsed, input);
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
		} else if (input.isKeyDown(Keyboard.KEY_RIGHT)) {
			if (changeInProgress == true) {
				// stop rotation of active object
				activeObject.setTransformation(vecmath.rotationMatrix(
						vecmath.yAxis(), 0).mult(
						vecmath.translationMatrix(activeObject
								.getTransformation().getPosition())));
				// increment count if count smaller amount of objects per level
				if (countObject < objectsPerLevel - 1)
					countObject++;
				else if (countObject == objectsPerLevel - 1)
					countObject = 0;
				changeInProgress = false;

				// set active object to
				activeObject = activeObject.getParent().getNode(countObject);
			}

		} else if (input.isKeyDown(Keyboard.KEY_LEFT)) {
			if (changeInProgress == true) {
				// stop rotation of active object
				activeObject.setTransformation(vecmath.rotationMatrix(
						vecmath.yAxis(), 0).mult(
						vecmath.translationMatrix(activeObject
								.getTransformation().getPosition())));
				// increment count if count smaller amount of objects per level
				if (countObject > 0)
					countObject--;
				else if (countObject == 0)
					countObject = objectsPerLevel - 1;
				changeInProgress = false;

				// set active object to
				activeObject = activeObject.getParent().getNode(countObject);
			}

		} else if (input.isKeyDown(Keyboard.KEY_RETURN)) {
			if (changeInProgress == true) {
				if (activeObject.getNodes().size() != 0) {
					zoomInProgress = true;
					camLastPosi = cam.getEyeZ();
				}
				changeInProgress = false;
			}

		} else if (input.isKeyDown(Keyboard.KEY_BACK)) {
			if (changeInProgress == true) {
				if (activeObject.getParent().getParent() != root) {
					zoomOutProgress = true;
					camLastPosi = cam.getEyeZ();
				}
				changeInProgress = false;
			}
		} else if (input.isKeyDown(Keyboard.KEY_P)) {
			if (changeInProgress == true) {
				if (activeObject.getClass() == Cube.class)
					((Cube) activeObject).setTexture();
				else
					((Pyramide) activeObject).setTexture();

			}
		}

		else {
			changeInProgress = true;
		}

		// Zoom in
		if (zoomInProgress == true && camLastPosi-6 < cam.getEyeZ()) {
			System.out.println(cam.getTransformation().getPosition().z());
			cam.moveIn(elapsed);
		} else if (zoomInProgress == true) {
			System.out.println(cam.getEyeZ());
			zoomInProgress = false;
			activePlane = activeObject.getNode(0);
			activeObject = activePlane.getNode(0).getNode(0);
			objectsPerLevel = activePlane.getNode(0).getNodes().size();
			countObject = 0;
		}

		// Zoom out
		if (zoomOutProgress == true && camLastPosi+6 > cam.getEyeZ()) {
			cam.moveOut(elapsed);
		} else if (zoomOutProgress == true) {
			System.out.println(cam.getEyeZ());
			zoomOutProgress = false;
			activeObject = activePlane.getParent();
			activePlane = activeObject.getParent().getParent();
			objectsPerLevel = activePlane.getNode(0).getNodes().size();
			countObject = activeObject.getIndex();
		}

		activeObject.setTransformation(activeObject.getTransformation().mult(
				vecmath.rotationMatrix(vecmath.yAxis(), angle)));
		angle = 90 * elapsed;

	}

	// Some class variables used to manipulate the modelmatrix in the display
	// methode
	private float angle = 0;
	Vector axis = vecmath.vector(0, 1, 0);
	Vector move = vecmath.vector(0, 0, 0);
	Vector rotate = vecmath.vector(0, 0, 0);

	private Plane background2;

	private Node background3;

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

		// The modeling transformation. Object space to world space.
		Matrix modelMatrix = vecmath.translationMatrix(vecmath.vector(0, 0, 0));

		// The inverse camera transformation. World space to camera space.
		// cam.setTransformation(vecmath.translationMatrix(2, 0, 0));
		
		
		free.display(modelMatrix);
		cam.display(free.getTransformation());
		Matrix viewMatrix = cam.matrix();

		

		// Shader
		defaultshader.setModelMatrixUniform(modelMatrix);
		defaultshader.setProjectionMatrixUniform(projectionMatrix);
		defaultshader.setViewMatrixUniform(viewMatrix);

		// Renders the Object with some magic
		// root.getNodes().get(0).display(modelMatrix);
		activePlane.display(modelMatrix);
		if (activeObject.getNodes().size() != 0) {
			activeObject.getNode(0).display(modelMatrix);
		}

		// for (int i = 0; i < activeObject.getParent().getNodes().size(); i++)
		// {
		// activeObject.getParent().getNode(i).display(modelMatrix);
		// }
		// if (activeObject.getNodes().size() != 0) {y
		// activeObject.getNode(i).display(modelMatrix);
		// }
		// }
		loader.setTransformation(vecmath.translationMatrix(0,0,0));
		if (input.isKeyDown(Keyboard.KEY_O)) {
			loader.display(modelMatrix, defaultshader);
		}
		background.setTransformation(vecmath.translationMatrix(0, 0, -7f));
		background.display(modelMatrix);
		background2.setTransformation(vecmath.translationMatrix(0, 0, -13f));
		background2.display(modelMatrix);
		background3.setTransformation(vecmath.translationMatrix(0, 0, -1f));
		background3.display(modelMatrix);
	}

}
