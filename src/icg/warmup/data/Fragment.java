package icg.warmup.data;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Fragment{
	// pixel position
	final int x, y;
	// color data
	java.awt.Color c = Color.BLACK;

	// default constructor
	public Fragment(int x, int y){
		this.x = x;
		this.y = y;
	}

	// update color
	public void setColor(Color c){
		this.c = c;
	}

	// update associated pixel in image (if not out of bounds)
	public void render(BufferedImage to, int yOffset){
		if (x >= 0 && x < to.getWidth() && yOffset-y >= 0 && yOffset-y < to.getHeight())
			to.setRGB(x, yOffset - y, c.getRGB());
	}

	// nice string representation of this fragment
	public String toString(){
		return "Fragment at (" + x + ", " + y + ") colored " + c;
	}
}