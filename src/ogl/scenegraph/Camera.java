package ogl.scenegraph;

import static ogl.vecmathimp.FactoryDefault.vecmath;

import org.lwjgl.input.Mouse;

import ogl.vecmath.*;

public class Camera extends Node{
	
	public Camera(String name) {
		super(name);
	}

	private float speed = 5f;
	
	private float eyeX = 3f;
	private float eyeY = 2f;
	private float eyeZ = 4.9f;
	
	private float centerX = 3f;
	private float centerY = 0f;
	private float centerZ = -1.1f;

	private Vector eye = vecmath.vector(eyeX, eyeY, eyeZ);
	private Vector center = vecmath.vector(centerX, centerY, centerZ);
	private Vector up = vecmath.vector(0f, 1f, 0f);
	
	private Matrix viewMatrix = vecmath.lookatMatrix(eye, center, up);

	public void display(Matrix m){
		eye = vecmath.vector(eyeX, eyeY, eyeZ);
		center = vecmath.vector(centerX, centerY, centerZ);
		viewMatrix = vecmath.lookatMatrix(eye, center, up).mult(m).mult(getTransformation());
	}
	
	public Matrix matrix(){
		return viewMatrix;
	}
	
	public void moveLeft(float time){
		eyeX = eyeX - 1 * time * speed;
		centerX = centerX - 1 * time * speed;
		
	}
	
	public void moveRight(float time){
		eyeX = eyeX + 1 * time * speed;
		centerX = centerX + 1 * time * speed;
	}
	
	public void moveDown(float time){
		eyeY = eyeY - 1 * time * speed;
		centerY = centerY - 1 * time * speed;
	}
	
	public void moveUp(float time){
		eyeY = eyeY + 1 * time * speed;
		centerY = centerY + 1 * time * speed;
	}
	
	public void moveIn(float time){
		eyeZ = eyeZ - 1 * time * speed;
		centerZ = centerZ - 1 * time * speed;
	}
	
	public void moveOut(float time){
		eyeZ = eyeZ + 1 * time * speed;
		centerZ = centerZ + 1 * time * speed;
	}
	
	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getEyeZ() {
		return eyeZ;
	}

	public void setEyeY(float eyeY) {
		int middleY = 768/2;
		this.eyeY = (middleY-(eyeY))/20;
	}

	
	public void setEyeX(float eyeX) {
		int middleX = 1324/2;
		this.eyeX = (middleX-(eyeX))/20;
	}

	public void setEyeZ(float eyeZ) {
		this.eyeZ = eyeZ;
	}
	
	
	
	
	
	
}
