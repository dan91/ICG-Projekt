package icg.warmup;

import static ogl.vecmathimp.FactoryDefault.vecmath;
import ogl.vecmath.Color;

public class Circle implements Painter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ImageGenerator(new Circle(), 100, 100, "checkered.png");
	}

	@Override
	public Color pixelColorAt(int x, int y, int width, int height) {
		double shortEdge = Math.min(width, height);
		double circleDiameter = shortEdge * 0.9;
		double step = circleDiameter / shortEdge;

		for (int i = x; i < width; i += step) {
			if (x > 5)
				return vecmath.color(1, 0, 0);
		}
		return null;
	}

}
