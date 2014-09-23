package ogl.parser;

import static ogl.vecmathimp.FactoryDefault.vecmath;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import ogl.objects.Cube;
import ogl.objects.Pyramide;
import ogl.shader.Shader;

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
		ogl.scenegraph.Node root = new ogl.scenegraph.Node("root");

		Node scene = doc.getFirstChild();

		// Get all cubes for Level 1
		NodeList cubesLevel1 = scene.getChildNodes();
		ogl.scenegraph.Node NCubes1 = new ogl.scenegraph.Node("Scene");
		root.addNode(NCubes1);
		for (int temp = 0; temp < cubesLevel1.getLength(); temp++) {

			Node cube = cubesLevel1.item(temp);
			String name = "";
			if(cube.hasAttributes()) {
				name = cube.getAttributes().getNamedItem("name").getTextContent();
			}
			Cube c = new Cube(defaultshader, name);

			if (cube.getNodeType() == Node.ELEMENT_NODE) {
				c.setTransformation(vecmath.translationMatrix(vecmath.vector(
						.75f * temp, 0f, 0f)));
				NCubes1.addNode(c);
			}

			NodeList cubesLevel2 = cube.getChildNodes();

			if (cubesLevel2.getLength() > 0) {
				for (int temp2 = 0; temp2 < cubesLevel2.getLength(); temp2++) {

					Node cube2 = cubesLevel2.item(temp2);
					String name2 = "";
					if(cube2.hasAttributes()) {
						name2 = cube2.getAttributes().getNamedItem("name").getTextContent();
					}
					Pyramide c2 = new Pyramide(defaultshader, name2);

					if (cube2.getNodeType() == Node.ELEMENT_NODE) {
						
						c2.setTransformation(vecmath.translationMatrix(vecmath
								.vector(.75f * temp2, 0f, 0f)));
						c.addNode(c2);
					}
					
					NodeList cubesLevel3 = cube2.getChildNodes();

					if (cubesLevel2.getLength() > 0) {
						for (int temp3 = 0; temp3 < cubesLevel3.getLength(); temp3++) {
							Node cube3 = cubesLevel3.item(temp3);
							String name3 = "";
							if(cube3.hasAttributes()) {
								name3 = cube3.getAttributes().getNamedItem("name").getTextContent();
							}
							if (cube3.getNodeType() == Node.ELEMENT_NODE) {
								
								Cube c3 = new Cube(defaultshader, name3);
								c3.setTransformation(vecmath.translationMatrix(vecmath
										.vector(.75f * temp3, 0f, 0f)));
								c2.addNode(c3);
							}
						}
					}
					
				}
			}

		}
		return root;
	}
	
	

}
