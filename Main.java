package bfp;

import static bfp.ImageManipulator.getGrayScaleValues;
import static bfp.ImageManipulator.getImg;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class Main {
	public static void main(String[] args) {
		Node[] trainingData = getTrainingData();

		// Training time
		Node rootNode = trainingData[0];
		BoundaryTree tree = new BoundaryTree(rootNode);
		for (int i = 1; i < trainingData.length; i++) {
			tree.query(trainingData[i], true);
			System.out.println("Training: " + ((double) i / trainingData.length * 100) + "% done");
		}
		
		// Testing time
		File test = new File("train/pictures_20x20");
		int correct = 0;
		int total = 0;
		for (File dir : test.listFiles()) {
			for (int i = dir.listFiles().length / 2; i < dir.listFiles().length; i++) {
				File picture = dir.listFiles()[i];
				Node query = new Node("", getGrayScaleValues(getImg("train/pictures_20x20/" + dir.getName() + "/" + picture.getName())));
				Node result = tree.query(query, false);
				if (result.label.equals(dir.getName())) {
					System.out.println("Correct");
					correct++;
				} else {
					System.out.println("Incorrect");
				}
				total++;
			}
		}
		System.out.println("Got " + ((double) correct / total * 100) + "% correct");
	}
	
	public static Node[] getTrainingData() {
		ArrayList<Node> nodes = new ArrayList<Node>();
		
		File trainingDir = new File("train/pictures_20x20/");
		for (int j = 0; j < trainingDir.listFiles().length; j++) {
			File dir = trainingDir.listFiles()[j];
			for (int i = 0; i < dir.listFiles().length / 2; i++) {
				File picture = dir.listFiles()[i];
				System.out.println(((double) j / trainingDir.listFiles().length * 100) + "% done");
				nodes.add(new Node(dir.getName(), getGrayScaleValues(getImg("train/pictures_20x20/" + dir.getName() + "/" + picture.getName()))));
			}
		}
		
		Collections.shuffle(nodes);
		
		return nodes.toArray(new Node[nodes.size()]);
	}
}
