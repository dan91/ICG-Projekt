package ogl.scenegraph;

import java.util.ArrayList;
import java.util.List;

import static ogl.vecmathimp.FactoryDefault.vecmath;
import ogl.cube.Cube;
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

	// Error: parent Node is not correctly set
	public Node addNode(Node node) {
		this.nodes.add(node);
		node.parent = this;
		node.index = node.getParent().getNodes().indexOf(node);
		if(node.index != 0) {
			node.previous = node.getParent().getNodes().get(node.index-1);
			node.setTransformation(node.previous.getTransformation().mult(vecmath.translationMatrix(1.5f, 0, 0)));
		}
		int columns = node.index / 5;
		if (columns > 0) {
			node.setTransformation(vecmath.translationMatrix(0, -(float) columns * 1.5f, 0));
		}
		int depth = 0;
		Node check = this;
		while (check.getClass() != Node.class) {
			check = check.getParent();
			depth++;
		}
		if (depth != 0) {
			node.setTransformation(node.transformation.mult(vecmath.translationMatrix(0, 0, -2*depth)));
		}
		return node;
		
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
	
	public Node getNode(int index){
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
		transformation = m.mult(transformation);

		List<Node> children = n.nodes;
		for (int i = 0; i < children.size(); i++) {
			Node currentNode = children.get(i);
			if (currentNode instanceof Cube) {
				currentNode.display(n.transformation);
			}
			else display(transformation, currentNode);
		}
	}

	public static String visitRecursively(Node node) {
		output = "[Node name=" + node.name + "]\n";

		List<Node> list = node.getNodes();

		for (int i = 0; i < list.size(); i++) {
			output = "\t";

			Node childNode = list.get(i);

			output += "Found Node: " + childNode.name
			+ " - with value: " + childNode.name + "\n";
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
