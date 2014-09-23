package ogl.scenegraph;

import org.lwjgl.input.Mouse;
import static ogl.vecmathimp.FactoryDefault.vecmath;

import ogl.app.Input;
import ogl.vecmath.*;

public class FreeFlight extends Node {
	public FreeFlight(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	Matrix tansform(float elapsed, Input input) {
		Matrix transformation = vecmath.identityMatrix();

		transformation = transformation.mult(vecmath.rotationMatrix(vecmath.vector(1, 0, 0), -Mouse.getDY()*elapsed*60));
		return transformation;
	}
}
