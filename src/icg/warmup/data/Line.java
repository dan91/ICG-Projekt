package icg.warmup.data;

import java.util.Vector;
import javax.vecmath.Point2d;

public class Line {
	private Point2d p1;
	private Point2d p2;

	// constructor
	public Line(Point2d p1, Point2d p2) {
		this.setPoint1(p1);
		this.setPoint2(p2);
	}

	// returns a vector containing all fragments belonging to this triangle
	public Vector<Fragment> render() {
		return render(new IntWrapper((int) getPoint1().x), new IntWrapper(
				(int) getPoint1().y), new IntWrapper((int) getPoint2().x),
				new IntWrapper((int) getPoint2().y));
	}

	// create a vector containing all fragments that belong to a line between
	// (x0,y0) and (x1,y1)
	private Vector<Fragment> render(IntWrapper x0, IntWrapper y0, IntWrapper x1, IntWrapper y1){
		// TODO: implement me
		Vector<Fragment> retVal = new Vector<Fragment>();
		// create a point with the size of 5 pixels at the end of the line
		// NOTE: this is meant to be replaced by the bresenham line algorithm
//		for (int i = -2; i < 2; i++){
//			for (int j = -2; j < 2; j++){
//				retVal.add(new Fragment(x0.value + i, y0.value + j));
//				retVal.add(new Fragment(x1.value + i, y1.value + j));
//			}
//		}
		
		return drawBresenhamLine(x0.value, y0.value, x1.value, y1.value);
	}

	private Vector<Fragment> drawBresenhamLine(int x1, int y1, int x2, int y2) {
		Vector<Fragment> retVal = new Vector<Fragment>();

		int xIncrement = 1, yIncrement = 1, dy = 2 * (y2 - y1), dx = 2 * (x1 - x2), tmp;

		if (x1 > x2) { // Spiegeln an Y-Achse
			xIncrement = -1;
			dx = -dx;
		}

		if (y1 > y2) { // Spiegeln an X-Achse
			yIncrement = -1;
			dy = -dy;
		}

		int e = 2 * dy + dx;
		int x = x1; // Startpunkte setzen
		int y = y1;

		if (dy < -dx) // Steigung < 1
		{
			while (x != (x2 + 1)) {
				e += dy;
				if (e > 0) {
					e += dx;
					y += yIncrement;
				}
				retVal.add(new Fragment(x, y));
				retVal.add(new Fragment(x, y));
				x += xIncrement;
			}
		} else // ( dy >= -dx ) Steigung >=1
		{
			// an der Winkelhalbierenden spiegeln
			tmp = -dx;
			dx = -dy;
			dy = tmp;

			e = 2 * dy + dx;

			while (y != (y2 + 1)) {
				e += dy;
				if (e > 0) {
					e += dx;
					x += xIncrement;
				}
				retVal.add(new Fragment(x, y));
				retVal.add(new Fragment(x, y));
				y += yIncrement;
			}
		}
		return retVal;
	}

	public Point2d getPoint1() {
		return p1;
	}

	public void setPoint1(Point2d p1) {
		this.p1 = p1;
	}

	public Point2d getPoint2() {
		return p2;
	}

	public void setPoint2(Point2d p2) {
		this.p2 = p2;
	}
}
