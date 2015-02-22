package bfp;

import java.util.ArrayList;

public class Node {
	public static int maxChildren = 20;
	public String label;
	public int[][] img;
	public ArrayList<Node> children;
	public boolean isRouter;

	public Node(String label, int[][] arr) {
		this.label = label;
		this.img = arr;
		children = new ArrayList<Node>();
	}
	
	public int distanceBetween(Node otherNode) {
		int[][] img1 = this.img;
		int[][] img2 = otherNode.img;
		int sum = 0;
		for (int i = 0; i < img1.length; i++) {
			for (int j = 0; j < img1[i].length; j++) {
				int diff = img1[i][j] - img2[i][j];
				sum += diff * diff;
			}
		}
		
		return sum;
	}
	
	public boolean hasSameLabel(Node otherNode) {
		return label.equals(otherNode.label);
	}
	
	public void addChild(Node node) {
		if (children.size() <= maxChildren) {
			children.add(node);
			if (children.size() == maxChildren) {
				isRouter = true;
			}
		}
	}
	
	public Node getClosestNode(Node queryNode) {
		if (children.size() == 0) {
			return this;
		}
		
        Node closestNode = (isRouter) ? children.get(0) : this;
        int closestDistance = queryNode.distanceBetween(closestNode);
		for (Node child : children) {
			int distance = queryNode.distanceBetween(child);
			if (distance < closestDistance) {
				closestDistance = distance;
				closestNode = child;
			}
		}
		
		if (closestNode.equals(this)) {
			return this;
		}
		
		return closestNode.getClosestNode(queryNode);
	}
	
	public String toString() {
		return label + ": " + img[0][0] + ", " + img[0][1];
	}
}