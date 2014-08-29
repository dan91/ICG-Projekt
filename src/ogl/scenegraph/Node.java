package ogl.scenegraph;

import java.util.ArrayList;
import java.util.List;
import static ogl.vecmathimp.FactoryDefault.vecmath;

import ogl.vecmath.*;

public class Node {
	private Matrix transformation;

	private List<Node> nodes;
	private String name;

	public Node(String name) {
		setTransformation(vecmath.identityMatrix());		
		this.nodes = new ArrayList<Node>();
		this.name = name;
	}

	public void addNode(Node node) {
		this.nodes.add(node);
	}

	public void removeNode(int index) {
		this.nodes.remove(index);
	}

	public Matrix getTransformation() {
		return this.transformation;
	}

	public void setTransformation(Matrix transformation) {
		this.transformation = transformation;
	}

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
		n3.addNode(n4);
		System.out.println(n1.toString());
	}
}
