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
import ogl.triangle.Triangle;
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
	public Triangle triangle1;

	public int x;
	public int y;

	private Shader defaultshader;

	// init(), simulate(), display() kommen aus der alten RotatingCube Klasse
	// setter
	public void start(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void init() {
		// Set background color to black.
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// Enable depth testing.
		glEnable(GL11.GL_DEPTH_TEST);

		// ruft den shader auf
			defaultshader = new Shader();
		

		// cube1 = new Cube();
		// cube1.init(defaultshader); //initialisiert mit o.g. shader

		triangle1 = new Triangle(); // ist eigentlich pyramide
		triangle1.init(defaultshader); // dito
		
		cube1 = new Cube(); // ist eigentlich pyramide
		cube1.init(defaultshader); // dito
		//triangle1.setTransformation(vecmath.translationMatrix((float)0.9, 0, 0)); 

		// ==translationVerschiebt (-links +rechts, -runter +hoch, -vor +zurück)
		//triangle1.setTransformation(vecmath
		//		.translationMatrix(0, (float) 0.5, 1));

		// macht komischerweise gar nichts, vll schon und die camera geht mit
		// house.setTransformation(vecmath.translationMatrix(-1, 1, 2));
	}

	// dreht irgendwas, aber laesst auch den cube verschwinden wenn 0.0.0
	Vector axis = vecmath.vector(0, 1, 0);

	// ich wollte das in while schleifen machen weil wir ja noch einen übergang
	// brauchen
	// aber jetzt isses nur so, wenn ich X als erstes drück und dann Y oder Z
	// wird die Rotation
	// nur schneller. vllt kann einer von euch was damit anfangen

	public void rotate(float elapsed, Input input) {
		// TODO mit rausziehen (angle von Cube uebegeben)
		while (input.isKeyToggled(Keyboard.KEY_X)) {
			angle += 90 * elapsed;
			axis = vecmath.vector(1, 0, 0);
			break;
		}
		while (input.isKeyToggled(Keyboard.KEY_Y)) {
			axis = vecmath.vector(0, 1, 0);
			angle += 90 * elapsed;
			break;
		}
		while (input.isKeyToggled(Keyboard.KEY_Z)) {
			axis = vecmath.vector(0, 0, 1);
			angle += 90 * elapsed;
			break;
		}
	}

	public void move(KeyEvent e) {
		int y_speed = 0;
		int x_speed = 0;
		int y_position = 0;
		int x_position = 0;

		// ich weiß nicht wie ich die an die Position von unseren Objekten komme
		// um sie zu verändern.
		// positionData.position() += y_speed;

		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			y_speed = 2;
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			y_speed = -2;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			x_speed = -2;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			x_speed = 2;
		}
	}

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
		Matrix viewMatrix = vecmath.lookatMatrix(vecmath.vector(0f, 0f, 3f),
				vecmath.vector(0f, 0f, 0f), vecmath.vector(0f, 1f, 0f));

		// TODO damit dreht sich der W�rfel, weil sich angle immer ver�ndert
		// TODO dieser Angle soll allerding in die 3d objekt-klassen
		// setzt neue Transformationsmatrix je nachdem ob triangle steht, wird
		// triangle aufgerufen und bei cube cube
		// The modeling transformation. Object space to world space.
		Matrix modelMatrix = vecmath.rotationMatrix(axis, angle);
		
		triangle1.setTransformation(vecmath.translationMatrix(vecmath.vector(1f, 0f, 0f)));
		cube1.setTransformation(vecmath.translationMatrix(vecmath.vector(-1f, 0f, 0f)));

		defaultshader.activate();

		defaultshader.setModelMatrixUniform(modelMatrix);
		defaultshader.setProjectionMatrixUniform(projectionMatrix);
		defaultshader.setViewMatrixUniform(viewMatrix);

		triangle1.display(modelMatrix);
		cube1.display(modelMatrix);

		
	}

	// The attribute indices for the vertex data.
	public static int vertexAttribIdx = 0;
	public static int colorAttribIdx = 1;

	// Width, depth and height of the cube divided by 2.
	float w2 = 0.5f;
	float h2 = 0.5f;
	float d2 = 0.5f;

	// TODO in die cube bringen, (ist schon aber nicht implementiert scheinbar)
	// Initialize the rotation angle of the cube.
	private float angle = 0;

}
