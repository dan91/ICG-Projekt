package ogl.scenegraph;

import java.util.ArrayList;
import java.util.List;

import static ogl.vecmathimp.FactoryDefault.vecmath;
import ogl.cube.Cube;
import ogl.cube.Plane;
import ogl.cube.Shader;
import ogl.pyramide.Pyramide;
import ogl.triangle.Pyramid;
import ogl.vecmath.*;

public class Node {
	// Stores the transformation which should be done with this node
	private Matrix transformation;

	// Childnodes
	private List<Node> nodes;

	// Name of the mode
	private String name;

	private Node parent;
	private Node previous;
	private int index;

	public Node(String name) {
		setTransformation(vecmath.identityMatrix());
		this.nodes = new ArrayList<Node>();
		this.name = name;
	}
	public Node(String name, Shader defaultshader) {
		setTransformation(vecmath.identityMatrix());
		this.nodes = new ArrayList<Node>();
		this.name = name;
	}

	// Error: parent Node is not correctly set
	public Node addNode(Node node) {
//		this.nodes.add(node);
		// CHANGED from this.getNode(0).getNode(0) TO this
		// if current node has children
		if (this.getNodes().size() != 0) {
			node.parent = this.getNode(0).getNode(0);
			node.index = node.getParent().getNodes().size();
		}
		else{
			node.index = 0;
		}
		if (node.getClass() == Cube.class || node.getClass() == Pyramide.class) {
			if (node.index == 0){
				Node plane = new Node("Plane");
				Node tasks = new Node("Tasks");
				plane.index = 0;
				plane.parent = this;
				tasks.index = 0;
				tasks.parent = plane;
				node.parent = tasks;
				int depth = 0;
				Node check = node.getParent();
				while (check.getName() != "Scene") {
					check = check.getParent();
					depth++;
				}
				depth = depth - 2;
				if (depth > 0) {
					plane.setTransformation(vecmath.translationMatrix(0f, 0f, -1f * depth));
				}
				Plane background = new Plane(new Shader(), "Backgound");
				background.setTransformation(vecmath.translationMatrix(0, 0, -4f));
				background.setParent(plane);
				tasks.nodes.add(node);
				plane.nodes.add(tasks);
				//plane.nodes.add(background);
				this.nodes.add(plane);
			}
			else {
				this.getNode(0).getNode(0).simpleAdd(node);
				node.previous = node.getParent().getNodes().get(node.index - 1);
				node.setTransformation(node.previous.getTransformation().mult(vecmath.translationMatrix(1.5f, 0, 0)));
				int columns = node.index / 5;
				if (columns > 0) {
					node.setTransformation(vecmath.translationMatrix(0, -(float) columns * 1.5f, 0));
				}
			}
		}
		else{
			this.nodes.add(node);
		}
		return node;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Node simpleAdd(Node node){
		nodes.add(node);
		return node;
	}

	
	public String getName() {
		return name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Node getParent() {
		return parent;
	}

	public void removeNode(int index) {
		this.nodes.remove(index);
	}

	public Matrix getTransformation() {
		return this.transformation;
	}

	public List<Node> getNodes() {
		return this.nodes;
	}

	public Node getNode(int index) {
		return nodes.get(index);
	}

	public void setTransformation(Matrix transformation) {
		this.transformation = transformation;
	}

	private static String output;

	public void display(Matrix m) {
		display(m, this);
	}

	public void display(Matrix m, Node n) {
		// do something with the current node instead of System.out
		n.transformation = transformation.mult(m);

		List<Node> children = n.nodes;
		for (int i = 0; i < children.size(); i++) {
			Node currentNode = children.get(i);
			if (currentNode.getClass() != Node.class) {
				currentNode.display(n.transformation);
			} else
				display(transformation, currentNode);
		}
	}

	public static String visitRecursively(Node node) {
		output = "[Node name=" + node.name + "]\n";

		List<Node> list = node.getNodes();

		for (int i = 0; i < list.size(); i++) {
			output = "\t";

			Node childNode = list.get(i);

			output += "Found Node: " + childNode.name + " - with value: " + childNode.name + "\n";
			visitRecursively(childNode);

		}
		return output;
	}

	public String toString() {
		String output = "[Node name=" + this.name + "]\n";

		List<Node> children = this.nodes;
		for (int i = 0; i < children.size(); i++) {
			Node currentNode = children.get(i);
			if (currentNode.nodes.size() == 0) {
				output += "[Node name=" + currentNode.name + "]\n";
			}
			display(transformation, currentNode);
		}
		return output;
	}

	public static void main(String args[]) {
		Node n1 = new Node("N1");
		Node n2 = new Node("N2");
		Node n3 = new Node("N3");
		Node n4 = new Node("N4");
		n1.addNode(n2);
		n1.addNode(n3);
		// System.out.println(n1.toString());
		System.out.println(visitRecursively(n1));
	}
}
