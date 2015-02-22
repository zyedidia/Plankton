package bfp;

import static bfp.ImageManipulator.getGrayScaleValues;
import static bfp.ImageManipulator.getImg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Main {
	static String ext = "_32x32";
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		Node[] trainingData = listToArray(getTrainingData(false));
		Node[] testingData = listToArray(getTestingData(false));

		BoundaryForest forest = new BoundaryForest(15);

		forest.train(trainingData);
		
		// Testing time
		int correct = 0;
		int total = 0;
		for (int i = 0; i < testingData.length; i++) {
			String result = forest.query(testingData[i]);
			if (result.equals(testingData[i].label)) {
				System.out.println("Correct");
				correct++;
			} else {
				System.out.println("Incorrect");
			}
			total++;
		}
		System.out.println("Got " + ((double) correct / total * 100) + "% correct");
		
		long end = System.currentTimeMillis();
		System.out.println("Finished in " + ((end - start) / 1000.0) + " seconds");
	}

	public static ArrayList<Node> getTrainingData(boolean dataSerialized) {
		ArrayList<Node> nodes = new ArrayList<Node>();

		if (dataSerialized) {
			nodes = deserializeNodeList("train.ser");
		} else {
			File trainingDir = new File("train/pictures" + ext);
			for (int j = 0; j < trainingDir.listFiles().length; j++) {
				File dir = trainingDir.listFiles()[j];
				for (int i = 0; i < dir.listFiles().length / 2; i++) {
					File picture = dir.listFiles()[i];
//					System.out.println(((double) j / trainingDir.listFiles().length * 100) + "% done");
					nodes.add(new Node(dir.getName(),
							getGrayScaleValues(getImg("train/pictures" + ext + "/"
									+ dir.getName() + "/" + picture.getName()))));
				}
			}
			
			serializeNodeList(nodes, "train.ser");
		}

		Collections.shuffle(nodes);

		return nodes;
	}
	
	public static ArrayList<Node> getTestingData(boolean dataSerialized) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		if (dataSerialized) {
			nodes = deserializeNodeList("test.ser");
		} else {
			File testDir = new File("train/pictures" + ext);
			for (File dir : testDir.listFiles()) {
				for (int i = dir.listFiles().length / 2; i < dir.listFiles().length; i++) {
					File picture = dir.listFiles()[i];
					nodes.add(new Node(dir.getName(), getGrayScaleValues(getImg("train/pictures" + ext + "/" + dir.getName() + "/" + picture.getName()))));
				}
			}
			
			serializeNodeList(nodes, "test.ser");
		}

		return nodes;
	}
	
	public static void serializeNodeList(ArrayList<Node> n, String filename) {
        try {
        	FileOutputStream fileOut = new FileOutputStream(filename);
        	ObjectOutputStream out = new ObjectOutputStream(fileOut);
        	out.writeObject(n);
        	out.close();
        	fileOut.close();
        	System.out.printf("Serialized data is saved in " + filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Node> deserializeNodeList(String filename) {
        ArrayList<Node> nodes = new ArrayList<Node>();
		try {
			FileInputStream fileIn = new FileInputStream(filename);
			ObjectInputStream in;
			in = new ObjectInputStream(fileIn);
			nodes = (ArrayList<Node>) in.readObject();
			in.close();
			fileIn.close();
			System.out.println("Loaded " + filename);
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return nodes;
	}
	
	public static Node[] listToArray(ArrayList<Node> nodes) {
		return nodes.toArray(new Node[nodes.size()]);
	}

	public static Node[] shuffleArray(Node[] ar) {
		Random rnd = new Random();
		for (int i = ar.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			Node a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
		return ar;
	}
}
