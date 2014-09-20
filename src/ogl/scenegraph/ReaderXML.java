package ogl.scenegraph;

import static ogl.vecmathimp.FactoryDefault.vecmath;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import ogl.cube.Cube;
import ogl.cube.Shader;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReaderXML {

	private Node scene;

	public ogl.scenegraph.Node getScene(File xml, Shader defaultshader)
			throws ParserConfigurationException, SAXException, IOException {

		File fXmlFile = xml;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		doc.getDocumentElement().normalize();
		ogl.scenegraph.Node root = new ogl.scenegraph.Node("Scene");

		Node scene = doc.getFirstChild();

		// Get all cubes for Level 1
		NodeList cubesLevel1 = scene.getChildNodes();
		ogl.scenegraph.Node NCubes1 = new ogl.scenegraph.Node("Cubes1");

		for (int temp = 0; temp < cubesLevel1.getLength(); temp++) {

			Node cube = cubesLevel1.item(temp);
			if (cube.getNodeType() == Node.ELEMENT_NODE) {
				System.out.println("\tCurrent Element :"
						+ cube.getNodeName());
				Cube c = new Cube(defaultshader, "");
				c.setTransformation(vecmath.translationMatrix(vecmath
						.vector(.75f * temp, 0f, 0f)));
				NCubes1.addNode(c);
			}

			
//			NCubes1.setTransformation(vecmath.translationMatrix(vecmath.vector(
//					0f, -1.5f * temp, 0f)));
			root.addNode(NCubes1);

		}
		return root;
	}

}
