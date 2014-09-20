package ogl.scenegraph;

import static ogl.vecmathimp.FactoryDefault.vecmath;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Object3D extends Node {
	private FloatBuffer positionData;
	private FloatBuffer colorData;

	public Object3D(String name) {
		super(name);

	}

}
