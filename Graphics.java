package bfp;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import edu.princeton.cs.introcs.Draw;

public class Graphics {
	static int numNodes = 10000;
	static String[] colors = {"red", "blue", "green", "orange"};
	static Draw draw = new Draw("Boundary Graph");
	static Random rand = new Random();
	
	public static void main(String[] args) {
		draw.setCanvasSize(512, 512);
		draw.setXscale(0, 511);
		draw.setYscale(0, 511);
		
		Node[] trainingData = getTrainingData();
		Node rootNode = trainingData[0];
		System.out.println(rootNode);
		BoundaryTree tree = new BoundaryTree(rootNode);
		for (int i = 1; i < trainingData.length; i++) {
			System.out.println(trainingData[i]);
			tree.query(trainingData[i], true);
		}
		
		drawTree(tree);
	}
	
	public static Node[] getTrainingData() {
		ArrayList<Node> nodes = new ArrayList<Node>();
		
		for (int i = 0; i < numNodes; i++) {
			int randX = getRand(1, 510);
			int randY = getRand(1, 510);
			String label = "";
			if (randX > 255) {
				if (randY > 255) {
					label = colors[0];
				} else {
					label = colors[1];
				}
			} else {
				if (randY > 255) {
					label = colors[2];
				} else {
					label = colors[3];
				}
			}
			nodes.add(new Node(new ProtoNode(label, new int[][] {{randX, randY}})));
		}

		return nodes.toArray(new Node[nodes.size()]);
	}
	
	public static int getRand(int min, int max) {
	    return rand.nextInt((max - min) + 1) + min;
	}
	
	public static void drawTree(BoundaryTree tree) {
		drawChildren(tree.root);
	}
	
	public static void drawChildren(Node n) {
		drawNode(n);
		for (Node child : n.children) {
			draw.setPenColor(Color.black);
			draw.line(n.img()[0][0], n.img()[0][1], child.img()[0][0], child.img()[0][1]);
			drawChildren(child);
		}
	}
	
	public static void drawNode(Node n) {
	    Field field;
	    Color color = new Color(0, 0, 0);
		try {
			field = Class.forName("java.awt.Color").getField(n.label());
			color = (Color) field.get(null);
		} catch (NoSuchFieldException | SecurityException
				| ClassNotFoundException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		draw.setPenColor(color);
		if (n.isRouter) {
			draw.filledCircle(n.img()[0][0], n.img()[0][1], 15);
		} else {
			draw.circle(n.img()[0][0], n.img()[0][1], 15);
		}
		draw.text(n.img()[0][0], n.img()[0][1], n.img()[0][0] + ", " + n.img()[0][1]);
	}
}
