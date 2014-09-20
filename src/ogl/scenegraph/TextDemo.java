package ogl.scenegraph;


import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class TextDemo{
	public static void main(String[]args) {
		new TextDemo();
	
	}
	
public TextDemo(){
	try {
	//Display.setDisplayMode(new DisplayMode (512,512, 0, 0));
	Display.create();
	init();
	while (!Display.isCloseRequested()) {
		Display.update();
		render();
Display.update();
	}
	close();
	
} catch (LWJGLException e) {
	e.printStackTrace();
}
} 
public void init(){
	GL11.glMatrixMode(GL11.GL_PROJECTION);
	GL11.glLoadIdentity();
	GL11.glOrtho(0, 512, 0, 512, 1, -1);
	GL11.glMatrixMode(GL11.GL_MODELVIEW);
	
}
public void render(){
	GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	GL11.glLoadIdentity();

	GL11.glColor3f(1, 1, 1);
	SimpleText.drawString("Hello World", 300, 300);
	
	
}
public void close(){
	Display.destroy();
	System.exit(0);
	
	
	
}
}
