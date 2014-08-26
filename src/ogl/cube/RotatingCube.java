/*******************************************************************************
 * Copyright (c) 2013 Henrik Tramberend, Marc Latoschik.
 * All rights reserved.
 *******************************************************************************/
package ogl.cube;

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

import java.nio.FloatBuffer;

import ogl.app.App;
import ogl.app.Input;
import ogl.app.MatrixUniform;
import ogl.app.OpenGLApp;
import ogl.app.Util;
import ogl.scenegraph.Vertex;
import ogl.vecmath.Color;
import ogl.vecmath.Matrix;
import ogl.vecmath.Vector;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

//Select the factory we want to use.

// A simple but complete OpenGL 2.0 ES application.
public class RotatingCube implements App {

  static public void main(String[] args) {
    new OpenGLApp("Rotating Cube - OpenGL ES 2.0 (lwjgl)", new RotatingCube())
      .start();
  }

  @Override
  public void init() {
    // Set background color to black.
    glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

    // Enable depth testing.
    glEnable(GL11.GL_DEPTH_TEST);

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

    // Bind the vertex attribute data locations for this shader program. The
    // shader expects to get vertex and color data from the mesh. This needs to
    // be done *before* linking the program.
    glBindAttribLocation(program, vertexAttribIdx, "vertex");
    glBindAttribLocation(program, colorAttribIdx, "color");

    // Link the shader program.
    glLinkProgram(program);
    Util.checkLinkage(program);

    // Bind the matrix uniforms to locations on this shader program. This needs
    // to be done *after* linking the program.
    modelMatrixUniform = new MatrixUniform(program, "modelMatrix");
    viewMatrixUniform = new MatrixUniform(program, "viewMatrix");
    projectionMatrixUniform = new MatrixUniform(program, "projectionMatrix");

    // Prepare the vertex data arrays.
    // Compile vertex data into a Java Buffer data structures that can be
    // passed to the OpenGL API efficently.
    positionData = BufferUtils.createFloatBuffer(vertices.length
        * vecmath.vectorSize());
    colorData = BufferUtils.createFloatBuffer(vertices.length
        * vecmath.colorSize());

    for (Vertex v : vertices) {
      positionData.put(v.position.asArray());
      colorData.put(v.color.asArray());
    }
    positionData.rewind();
    colorData.rewind();
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
    // Adjust the the viewport to the actual window size. This makes the
    // rendered image fill the entire window.
    glViewport(0, 0, width, height);

    // Clear all buffers.
    glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

    // Assemble the transformation matrix that will be applied to all
    // vertices in the vertex shader.
    float aspect = (float) width / (float) height;

    // The perspective projection. Camera space to NDC.
    Matrix projectionMatrix = vecmath
      .perspectiveMatrix(60f, aspect, 0.1f, 100f);

    // The inverse camera transformation. World space to camera space.
    Matrix viewMatrix = vecmath.lookatMatrix(vecmath.vector(0f, 0f, 3f),
      vecmath.vector(0f, 0f, 0f), vecmath.vector(0f, 1f, 0f));

    // The modeling transformation. Object space to world space.
    Matrix modelMatrix = vecmath.rotationMatrix(vecmath.vector(1, 1, 1), angle);

    // Activate the shader program and set the transformation matricies to the
    // uniform variables.
    glUseProgram(program);

    modelMatrixUniform.set(modelMatrix);
    viewMatrixUniform.set(viewMatrix);
    projectionMatrixUniform.set(projectionMatrix);

    // Enable the vertex data arrays (with indices 0 and 1). We use a vertex
    // position and a vertex color.
    glVertexAttribPointer(vertexAttribIdx, 3, false, 0, positionData);
    glEnableVertexAttribArray(vertexAttribIdx);
    glVertexAttribPointer(colorAttribIdx, 3, false, 0, colorData);
    glEnableVertexAttribArray(colorAttribIdx);

    // Draw the triangles that form the cube from the vertex data arrays.
    glDrawArrays(GL11.GL_QUADS, 0, vertices.length);
  }

  public static int getVertexAttribIdx() {
	return vertexAttribIdx;
}

public static void setVertexAttribIdx(int vertexAttribIdx) {
	RotatingCube.vertexAttribIdx = vertexAttribIdx;
}

public static int getColorAttribIdx() {
	return colorAttribIdx;
}

public static void setColorAttribIdx(int colorAttribIdx) {
	RotatingCube.colorAttribIdx = colorAttribIdx;
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

  // The vertex program source code.
  private String[] vsSource = {
      "uniform mat4 modelMatrix;",
      "uniform mat4 viewMatrix;",
      "uniform mat4 projectionMatrix;",

      "attribute vec3 vertex;",
      "attribute vec3 color;",
      "varying vec3 fcolor;",

      "void main() {",
      "  fcolor = color;",
      "  gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(vertex, 1);",
      "}" };

  // The fragment program source code.
  private String[] fsSource = { 
      "varying vec3 fcolor;",
      "void main() {", 
      "  gl_FragColor = vec4(fcolor, 1.0);", 
      "}" };

  // Auxillary class to represent a single vertex.
//  public class Vertex {
//    Vector position;
//    Color color;
//
//    Vertex(Vector p, Color c) {
//      position = p;
//      color = c;
//    }
//  }

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
  // The colors of the cube vertices.
  private Color[] c = { 
      col(0, 0, 0), 
      col(1, 0, 0), 
      col(1, 1, 0), 
      col(0, 1, 0),
      col(1, 0, 1), 
      col(0, 0, 1), 
      col(0, 1, 1), 
      col(1, 1, 1),
      col(1, 23, 1) 
  };
  //
  //     6 ------- 7 
  //   / |       / | 
  //  3 ------- 2  | 
  //  |  |      |  | 
  //  |  5 -----|- 4 
  //  | /       | / 
  //  0 ------- 1
  //
  
  // The positions of the cube vertices.
  private Vector[] p = { 
      vec(-w2, -h2, -d2), 
      vec(w2, -h2, -d2),
      vec(w2, h2, -d2), 
      vec(-w2, h2, -d2), 
      vec(w2, -h2, d2), 
      vec(-w2, -h2, d2),
      vec(-w2, h2, d2), 
      vec(w2, h2, d2),
      vec(0, 2*h2, 0)
  };





  // Vertices combine position and color information. Every four vertices define
  // one side of the cube.
  public Vertex[] vertices = {
      // front
      v(p[0], c[0]), v(p[1], c[1]), v(p[2], c[2]), v(p[3], c[3]),
      // back
      v(p[4], c[4]), v(p[5], c[5]), v(p[6], c[6]), v(p[7], c[7]),
      // right
      v(p[1], c[1]), v(p[4], c[4]), v(p[7], c[7]), v(p[2], c[2]),
      // top
      v(p[3], c[3]), v(p[2], c[2]), v(p[7], c[7]), v(p[6], c[6]),
      // left
      v(p[5], c[5]), v(p[0], c[0]), v(p[3], c[3]), v(p[6], c[6]),
      // bottom
      v(p[5], c[5]), v(p[4], c[4]), v(p[1], c[1]), v(p[0], c[0]),
      // roof front
      v(p[6], c[3]), v(p[3], c[1]), v(p[8], c[1]), v(p[6], c[1]),
      // roof right
      v(p[2], c[3]), v(p[7], c[1]), v(p[8], c[1]), v(p[2], c[1]),
  };

  private FloatBuffer positionData;
  private FloatBuffer colorData;

  // Initialize the rotation angle of the cube.
  private float angle = 0;
}
