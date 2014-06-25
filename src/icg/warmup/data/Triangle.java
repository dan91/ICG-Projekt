package icg.warmup.data;

import java.util.Vector;

public class Triangle {
	// the lines of this triangle
	Line[] lines;
	public Triangle(Line l1, Line l2, Line l3){
		this.lines = new Line[]{l1, l2, l3};
	}
	
	// get access to the triangle's lines
	public Line[] getLines(){
		return lines;
	}
	
	// render the triangle
	public Vector<Fragment> render(){
		Vector<Fragment> retVal = new Vector<Fragment>();
		// collect fragments from lines
		for (Line l : lines)
			retVal.addAll(l.render());
		// return all fragments
		return retVal;
	}
}
