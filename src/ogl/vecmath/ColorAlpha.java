package ogl.vecmath;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;


/**
 * A simple three component color vector. Color vectors are non-mutable and can
 * be passed around by value.
 * 
 */
/**
 * @author henrik
 *
 */
public class ColorAlpha  {

  private final float r;
  private final float g;
  private final float b;
  private final float a;

  public ColorAlpha(float r, float g, float b, float a) {
    this.r = r;
    this.g = g;
    this.b = b;
    this.a = a;
  }


  /**
   * Convert to float array.
   * 
   * @return
   */
  public float[] asArray() {
    float[] v = { r, g, b, a };
    return v;
  }

}
