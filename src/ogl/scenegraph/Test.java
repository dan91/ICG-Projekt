package ogl.scenegraph;

public class Test {
public static void main(String[] args) {
	int l = 2;
    int t = "QEA".hashCode() % 3000;
    int a = "NFP".hashCode() % 3000;
    for (int c = 0; c <= a; c++)
       l = (l ^ c) % t;
    System.out.println(l);
}
}
