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

	public Node(String name) {
		setTransformation(vecmath.identityMatrix());
		this.nodes = new ArrayList<Node>();
		this.name = name;
	}

	// Error: parent Node is not correctly set
	public void addNode(Node node) {
		this.nodes.add(node);
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

	// Sehr, sehr h�sslicher Code der aber funktioniert!
	// TODO: Wenn Ein Node einen weiteren Node enthaelt, der kein Objekt ist
	// und dieser Node eine Trasformation hat, sollte sie nicht angezeigt
	// werden.
	// Muesste leicht zu fixen sein

	static Node previous;
	static Node first;
	static boolean check = true;

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
	    System.out.println(this.name);
	    transformation = transformation.mult(getTransformation());

	    List<Node> children = n.nodes;
	    for (int i = 0; i < children.size(); i++) {
	        Node currentNode = children.get(i);
	        if (currentNode.nodes.size() == 0) {	  
	        	currentNode.display(transformation);
	        }
            display(m, currentNode);
	    }
	}
//	public void display(Matrix m) {
//		//display(0, m);
//		transformation = transformation.mult(getTransformation());
//		
//		for(Node n : this.nodes) {
//			while(n.nodes.size() > 0) {
//				
//			}
//			n.display(transformation);
//		}
//	}

	@Override
	public String toString() {
		return this.toString(0);
	}

	public String toString(int i) {
		String output = "";
		for (Node n : this.nodes) {
			output += "Node [name=" + n.name + "]" + "\n";
			if (n.nodes.size() > 0)
				i++;
			String tabs = "";
			for (int j = 0; j < i; j++)
				tabs += "\t";
			output += tabs + n.toString(i);
		}
		return output;
	}

	public static void main(String args[]) {
		Node n1 = new Node("N1");
		Node n2 = new Node("N2");
		Node n3 = new Node("N3");
		Node n4 = new Node("N4");
		n1.addNode(n2);
		n2.addNode(n3);
		n1.addNode(n4);
		System.out.println(n1.toString());
	}
}
