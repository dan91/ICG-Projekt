package ogl.vecmath;

public class TexCoord {
	
	private float[] coords = new float[] {0.0F, 0.0F};

	public TexCoord(float x, float y){
		coords[0] = x;
		coords[1] = y;
	}
	
	public int texSize (){
		return coords.length;
	}
	
	public float[] asArray(){
		return coords;
	}
}
