/*******************************************************************************
 * Copyright (c) 2013 Henrik Tramberend, Marc Latoschik.
 * All rights reserved.
 *******************************************************************************/
package ogl.pyramide;

import static ogl.vecmathimp.FactoryDefault.vecmath;

import java.io.IOException;
import java.nio.FloatBuffer;

import ogl.app.App;
import ogl.app.Input;
import ogl.app.MatrixUniform;
import ogl.app.OpenGLApp;
import ogl.cube.Shader;
import ogl.scenegraph.Vertex;
import ogl.vecmath.Color;
import ogl.vecmath.Vector;

import org.lwjgl.input.Keyboard;

//Select the factory we want to use.

// A simple but complete OpenGL 2.0 ES application.

public class Pyramide implements App {
	public Shader vs;
	public Shader fs;

	public Pyramide () throws IOException {
		  this.vs = new Shader();
		  this.fs = new Shader();
	}
  static public void main(String[] args) throws IOException {
    new OpenGLApp("Rotating Cube - OpenGL ES 2.0 (lwjgl)", new Pyramide())
      .start();
  }

 

  public MatrixUniform getModelMatrixUniform() {
	return modelMatrixUniform;
}

public void setModelMatrixUniform(MatrixUniform modelMatrixUniform) {
	this.modelMatrixUniform = modelMatrixUniform;
}

public MatrixUniform getViewMatrixUniform() {
	return viewMatrixUniform;
}

public void setViewMatrixUniform(MatrixUniform viewMatrixUniform) {
	this.viewMatrixUniform = viewMatrixUniform;
}

public MatrixUniform getProjectionMatrixUniform() {
	return projectionMatrixUniform;
}

public void setProjectionMatrixUniform(MatrixUniform projectionMatrixUniform) {
	this.projectionMatrixUniform = projectionMatrixUniform;
}

/*
   * (non-Javadoc)
   * 
   * @see cg2.cube.App#simulate(float, cg2.cube.Input)
   */
  @Override
  public void simulate(float elapsed, Input input) {
    // Pressing key 'r' toggles the cube animation.
    if (input.isKeyToggled(Keyboard.KEY_R))
      // Increase the angle with a speed of 90 degrees per second.
      angle += 90 * elapsed;
  }

  /*
   * (non-Javadoc)
   * 
   * @see cg2.cube.App#display(int, int, javax.media.opengl.GL2ES2)
   */
  @Override
  public void display(int width, int height) {
    
  }

  public static int getVertexAttribIdx() {
	return vertexAttribIdx;
}

public static void setVertexAttribIdx(int vertexAttribIdx) {
	Pyramide.vertexAttribIdx = vertexAttribIdx;
}

public static int getColorAttribIdx() {
	return colorAttribIdx;
}

public static void setColorAttribIdx(int colorAttribIdx) {
	Pyramide.colorAttribIdx = colorAttribIdx;
}

// The shader program.
  private int program;

  // The location of the "mvpMatrix" uniform variable.
  private MatrixUniform modelMatrixUniform;
  private MatrixUniform viewMatrixUniform;
  private MatrixUniform projectionMatrixUniform;

  // The attribute indices for the vertex data.
  public static int vertexAttribIdx = 0;
  public static int colorAttribIdx = 1;

  // Width, depth and height of the cube divided by 2.
  float w2 = 0.5f;
  float h2 = 0.5f;
  float d2 = 0.5f;


 

  // Make construction of vertices easy on the eyes.
  private Vertex v(Vector p, Color c) {
    return new Vertex(p, c);
  }

  // Make construction of vectors easy on the eyes.
private Vector vec(float x, float y, float z) {
    return vecmath.vector(x, y, z);
  }

  // Make construction of colors easy on the eyes.
  private Color col(float r, float g, float b) {
    return vecmath.color(r, g, b);
  }

  //
  //     
  //   
  //        2   | 
  //  |  |      |  | 
  //  |  3   	   4
  //  | /       | / 
  //  0 ------- 1
  //
  
  // The positions of the cube vertices.
  private Vector[] p = { 
      vec(-w2, -h2, d2), // 0
      vec(w2, -h2, d2), // 1
      vec(0, h2, 0), //2
      vec(-w2, -h2, -d2), //3
      vec(w2, -h2, -d2), //4
  };

  // The colors of the cube vertices.
  private Color[] c = { 
      col(1, 1, 1), 
      col(1, 1, 1), 
      col(1, 1, 1), 
      col(1, 1, 1), 
      col(1, 1, 1)
  };


  // Vertices combine position and color information. Every four vertices define
  // one side of the cube.
  private Vertex[] vertices = {
      // front
      v(p[0], c[0]), v(p[2], c[1]), v(p[1], c[2]),
      // right
      v(p[1], c[1]), v(p[4], c[4]), v(p[2], c[2]),
      // back
      v(p[4], c[4]), v(p[3], c[2]), v(p[2], c[3]),
      // left
      v(p[3], c[3]), v(p[0], c[0]), v(p[2], c[2]),
      // bottom right
      v(p[3], c[3]), v(p[4], c[0]), v(p[1], c[2]),
      // bottom left
      v(p[1], c[3]), v(p[0], c[4]), v(p[3], c[1]),
  };

  private FloatBuffer positionData;
  private FloatBuffer colorData;

  // Initialize the rotation angle of the cube.
  private float angle = 0;


}
