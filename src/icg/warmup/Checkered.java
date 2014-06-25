/*******************************************************************************
 * Copyright (c) 2013 Henrik Tramberend, Marc Latoschik.
 * All rights reserved.
 *******************************************************************************/
package icg.warmup;

import static ogl.vecmathimp.FactoryDefault.vecmath;
import ogl.vecmath.Color;

public class Checkered implements Painter {

	public static void main(String[] args) {
		new ImageGenerator(new Checkered(), 100, 100, "checkered.png");
	}

	@Override
	public Color pixelColorAt(int x, int y, int width, int height) {
		if ((x + y) % 2 == 0 && x <= 8 && y <= 8)
			return vecmath.color(0, 0, 0);
		else
			return vecmath.color(1, 1, 1);
	}

}
