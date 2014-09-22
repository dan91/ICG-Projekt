package ogl.scenegraph;

import static ogl.vecmathimp.FactoryDefault.vecmath;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.vecmath.Vector2f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import ogl.vecmath.Color;
import ogl.vecmath.Vector;

/**
 * A loader for Wavefront OBJ files.
 * @author bburns
 */
public class ObjLoader implements DefaultMesh {		//	extends AbstractModelLoader {

        private ArrayList<Vector> p;
        private Color c = vecmath.color(1, 0, 0);
        private ArrayList<Vector> n; 
        private ArrayList<Vector2f> t;
        private ArrayList<Vertex> vertices;
		private FloatBuffer positionData;
		private FloatBuffer colorData;
		private FloatBuffer normalData;
		private FloatBuffer textureData;
		private static int vertexAttribIdx = 0;
		private static int colorAttribIdx = 1;
		private static int normalAttribIdx = 2;
		private static int textureAttribIdx = 3;
		

        protected static final String[] suffs = new String[] {".jpg", ".png", ".gif"};
        
        public ObjLoader(String file) throws IOException{
//        	Material material= new Material();
        	p = new ArrayList<Vector>();
        	n = new ArrayList<Vector>();
        	t = new ArrayList<Vector2f>();
        	vertices = new ArrayList<Vertex>();
        	load(file);
        }


        /**
         * {@inheritDoc}
         */
        public boolean canLoad(File f) {
                return f.getName().endsWith(".obj");
        }

        /**
         * {@inheritDoc}
         */
        public void load(String file) 
        throws IOException
        {
                load(new FileInputStream(file));
//                int ix = file.indexOf(".obj");
//                if (ix != -1) {
//                        String texture = file.substring(0, ix);
//                        for (int i=0;i<suffs.length;i++) {
//                                File f = new File(texture+suffs[i]);
//                                if (f.exists()) {
//                                        m.getFrame(0).getMesh().setTextureFile(texture+suffs[i]);
//                                        break;
//                                }
//                        }
//                }
        }

        /**
         * {@inheritDoc}
         */
        public void load(InputStream in) 
        throws IOException
        {
        	t.add(new Vector2f(0,0));
    		t.add(new Vector2f(0,1));
    		t.add(new Vector2f(1,1));
    		t.add(new Vector2f(1,0));
                boolean file_normal = false;
//                Mesh m = factory.create();
                int nCount = 0;
                float[] coord = new float[2];

                LineNumberReader input = new LineNumberReader(new InputStreamReader(in));           
                String line = null;
                try {
                        for (line = input.readLine(); 
                        line != null; 
                        line = input.readLine())
                        {
                                if (line.length() > 0) {
                                        if (line.startsWith("v ")) {
                                                float[] vertex = new float[3];
                                                StringTokenizer tok = new StringTokenizer(line);
                                                tok.nextToken();
                                                vertex[0] = Float.parseFloat(tok.nextToken());
                                                vertex[1] = Float.parseFloat(tok.nextToken());
                                                vertex[2] = Float.parseFloat(tok.nextToken());
                                                //m.addVertex(vertex);
                                                p.add(vecmath.vector(vertex[0], vertex[1], vertex[2]));
                                        }
                                        else if (line.startsWith("vt ")) {
                                                StringTokenizer tok = new StringTokenizer(line);
                                                tok.nextToken();
                                                coord[0] = Float.parseFloat(tok.nextToken());
                                                coord[1] = Float.parseFloat(tok.nextToken());
                                                //m.addTextureCoordinate(coord);
                                                t.add(new Vector2f(coord[0], coord[1]));
                                        }
                                        else if (line.startsWith("vn ")) {
                                            nCount++;
                                            float[] norm = new float[3];
                                            StringTokenizer tok = new StringTokenizer(line);
                                            tok.nextToken();
                                            norm[0] = Float.parseFloat(tok.nextToken());
                                            norm[1] = Float.parseFloat(tok.nextToken());
                                            norm[2] = Float.parseFloat(tok.nextToken());
                                            //m.addNormal(norm);
                                            n.add(vecmath.vector(norm[0], norm[1], norm[2]));
                                            file_normal = true;
                                    }
                                        else if (line.startsWith("f ")) {
                                                int[] face = new int[3];
                                                int[] face_n_ix = new int[3];
                                                int[] face_tx_ix = new int[3];
                                                int[] val;

                                                StringTokenizer tok = new StringTokenizer(line);
                                                tok.nextToken();
                                                val = parseIntTriple(tok.nextToken());
                                                face[0] = val[0];
                                                if (val.length > 1 && val[1] > -1)
                                                        face_tx_ix[0] = val[1];
                                                else val[1] = 0;
                                                if (val.length > 2 && val[2] > -1)
                                                        face_n_ix[0] = val[2];
                                                vertices.add(new Vertex(p.get(val[0]), c,n.get(val[2]), t.get(val[1])));

                                                val = parseIntTriple(tok.nextToken());
                                                face[1] = val[0];
                                                if (val.length > 1 && val[1] > -1)
                                                        face_tx_ix[1] = val[1];
                                                else val[1] = 0;
                                                if (val.length > 2 && val[2] > -1)
                                                        face_n_ix[1] = val[2];
                                                vertices.add(new Vertex(p.get(val[0]), c,n.get(val[2]), t.get(val[1])));

                                                val = parseIntTriple(tok.nextToken());
                                                face[2] = val[0];
                                                if (val.length > 1 && val[1] > -1) 
                                                        face_tx_ix[2] = val[1];
                                                else val[1] = 0;
                                                  
                                                if (val.length > 2 && val[2] > -1) 
                                                        face_n_ix[2] = val[2];
                                                vertices.add(new Vertex(p.get(val[0]), c,n.get(val[2]), t.get(val[1])));
                                                        
                                                
                                                if (tok.hasMoreTokens()) {
                                                        val = parseIntTriple(tok.nextToken());
                                                        face[1] = face[2];
                                                        face[2] = val[0];
                                                        if (val.length > 1 && val[1] > -1) {
                                                                face_tx_ix[1] = face_tx_ix[2];
                                                                face_tx_ix[2] = val[1];
                                                              //  m.addTextureIndices(face_tx_ix);
                                                        }
                                                        if (val.length > 2 && val[2] > -1) {
                                                                face_n_ix[1] = face_n_ix[2];
                                                                face_n_ix[2] = val[2];
                                                              //  m.addFaceNormals(face_n_ix);
                                                        }
                                                       // m.addFace(face);
                                                }

                                        }
                                        
                                }
                        }
                        positionData = BufferUtils.createFloatBuffer(vertices.size() * vecmath.vectorSize());
                        colorData = BufferUtils.createFloatBuffer(vertices.size() * vecmath.colorSize());
                        normalData = BufferUtils.createFloatBuffer(vertices.size() * vecmath.vectorSize());
                        textureData = BufferUtils.createFloatBuffer(vertices.size() * 2);

                        for (Vertex v : vertices) {
                          positionData.put(v.position.asArray());
                          colorData.put(v.color.asArray());
                          normalData.put(v.normals.asArray());      
                         float[] tex = new float[2];
                         tex[0] = (float) v.texture2.x;
                         tex[1] = (float) v.texture2.y;
                          textureData.put(tex);
                        }
                        positionData.rewind();
                        colorData.rewind();
                        normalData.rewind();
                        textureData.rewind();
                }
                
                catch (Exception ex) {
                		ex.printStackTrace();
                        System.err.println("Error parsing file:");
                        System.err.println(input.getLineNumber()+" : "+line);
                }
         
        }
        
        protected static int parseInt(String val) {
                if (val.length() == 0) {
                        return -1;
                }
                return Integer.parseInt(val);
        }

        protected static int[] parseIntTriple(String face) {
                int ix = face.indexOf("/");
                if (ix == -1)
                        return new int[] {Integer.parseInt(face)-1};
                else {
                        int ix2 = face.indexOf("/", ix+1);
                        if (ix2 == -1) {
                                return new int[] 
                                               {Integer.parseInt(face.substring(0,ix))-1,
                                                Integer.parseInt(face.substring(ix+1))-1};
                        }
                        else {
                                return new int[] 
                                               {parseInt(face.substring(0,ix))-1,
                                                parseInt(face.substring(ix+1,ix2))-1,
                                                parseInt(face.substring(ix2+1))-1
                                               };
                        }
                }
        }

        public void render (int vertexAttribIdx, int colorAttribIdx, int normalAttribIdx, int textureAttribIdx) {
        	 // Enable the vertex data arrays (with indices 0 and 1). We use a vertex
        	    // position and a vertex color.
        	    glVertexAttribPointer(vertexAttribIdx, 3, false, 0, positionData);
        	    glEnableVertexAttribArray(vertexAttribIdx);
        	    glVertexAttribPointer(colorAttribIdx, 3, false, 0, colorData);
        	    glEnableVertexAttribArray(colorAttribIdx);
        	    glVertexAttribPointer(normalAttribIdx, 3, false, 0, normalData);
        	    glEnableVertexAttribArray(normalAttribIdx);
        	    glVertexAttribPointer(textureAttribIdx, 2, false, 0, textureData);
        	    glEnableVertexAttribArray(textureAttribIdx);

        	    // Draw the triangles that form the cube from the vertex data arrays.
        	    glDrawArrays(GL11.GL_TRIANGLES, 0, vertices.size());
        	}
        	public void render () {
        	    // Enable the vertex data arrays (with indices 0 and 1). We use a vertex
        	       // position and a vertex color.
        	       glVertexAttribPointer(vertexAttribIdx, 3, false, 0, positionData);
        	       glEnableVertexAttribArray(vertexAttribIdx);
        	       glVertexAttribPointer(colorAttribIdx, 3, false, 0, colorData);
        	       glEnableVertexAttribArray(colorAttribIdx);
        	       glVertexAttribPointer(normalAttribIdx, 3, false, 0, normalData);
        	       glEnableVertexAttribArray(normalAttribIdx);
        	       glVertexAttribPointer(textureAttribIdx, 2, false, 0, textureData);
        	       glEnableVertexAttribArray(textureAttribIdx);

        	       // Draw the triangles that form the cube from the vertex data arrays.
        	       glDrawArrays(GL11.GL_TRIANGLES, 0, vertices.size());
        	   }
        	public static void main(String args[]) throws IOException {
              ObjLoader test = new ObjLoader("res/cube.obj");
              

        	}
}
