package ogl.scenegraph;

import java.util.ArrayList;
import java.util.List;

import static ogl.vecmathimp.FactoryDefault.vecmath;
import ogl.vecmath.*;

public class Node {
	// Stores the transformation which should be done with this node
	private Matrix transformation;

	// Childnodes
	private List<Node> nodes;

	// Name of the mode
	private String name;

	private Node parent;

	public Node(String name) {
		setTransformation(vecmath.identityMatrix());
		this.nodes = new ArrayList<Node>();
		this.name = name;
	}

	// Error: parent Node is not correctly set
	public void addNode(Node node) {
		this.nodes.add(node);
		node.parent = this;
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

	public void setTransformation(Matrix transformation) {
		this.transformation = transformation;
	}

	// Sehr, sehr hässlicher Code der aber funktioniert!
	// TODO: Wenn Ein Node einen weiteren Node enthaelt, der kein Objekt ist
	// und dieser Node eine Trasformation hat, sollte sie nicht angezeigt
	// werden.
	// Muesste leicht zu fixen sein

	static Node previous;
	static Node first;
	static boolean check = true;

	private static String output;

	public void display(int i, Matrix m) {
		if (check) {
			first = this;
			check = false;
		}
		if (nodes.isEmpty()) {
			// Wird aufgerufen wenn 3D Objekt
			this.display(m.mult(first.getTransformation()));
			previous.display(i + 1, m);
		} else {
			if (i < nodes.size()) {
				// Wenn Obejekt ein Kind besitzt. Eine Ebene Tiefer
				previous = this;
				nodes.get(i).display(i, m);
			}
		}
	}

	public void display(Matrix m) {
		display(m, this);
	}

	public void display(Matrix m, Node n) {
		// do something with the current node instead of System.out
		transformation = m.mult(getTransformation());

		List<Node> children = n.nodes;
		for (int i = 0; i < children.size(); i++) {
			Node currentNode = children.get(i);
			if (currentNode.nodes.size() == 0) {
				currentNode.display(transformation);
			}
			display(transformation, currentNode);
		}
	}

	// public void display(Matrix m) {
	// //display(0, m);
	// transformation = transformation.mult(getTransformation());
	//
	// for(Node n : this.nodes) {
	// while(n.nodes.size() > 0) {
	//
	// }
	// n.display(transformation);
	// }
	// }

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
