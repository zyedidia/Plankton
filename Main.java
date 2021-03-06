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
import java.util.Arrays;
import java.util.Collections;

public class Main {
	static String ext = "_32x32";
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		ProtoNode[] trainingData = listToArray(getTrainingData(true));
		ProtoNode[] testingData = listToArray(getTestingData(true));
		String[] planktonTypes = getPlanktonTypes();

		BoundaryForest forest = new BoundaryForest(25, planktonTypes);

		forest.train(trainingData);
		
		// Testing time
		int correct = 0;
		int total = 0;
		for (int i = 0; i < testingData.length; i++) {
			if (i % 1000 == 0) {
				System.out.printf("%5.1f done %5.1f correct\n", ((double) i / testingData.length * 100), 
						((double) correct / total * 100));
			}
			String result = forest.query(new Node(testingData[i]));
			if (result.equals(testingData[i].label())) {
				correct++;
			}
			total++;
		}

		for (int i = 0; i < forest.count.length; i++) {
			System.out.println("Conversion " + i + " is " + ((double) forest.numRight[i] / forest.count[i]));
		}

		System.out.println("Got " + ((double) correct / total * 100) + "% correct");
		
		long end = System.currentTimeMillis();
		System.out.println("Finished in " + ((end - start) / 1000.0) + " seconds");
	}

	public static ArrayList<ProtoNode> getTrainingData(boolean dataSerialized) {
		ArrayList<ProtoNode> nodes = new ArrayList<ProtoNode>();

		if (dataSerialized) {
			nodes = deserializeNodeList("train.ser");
		} else {
			File trainingDir = new File("train/pictures" + ext);
			for (int j = 0; j < trainingDir.listFiles().length; j++) {
				File dir = trainingDir.listFiles()[j];
				for (int i = 0; i < dir.listFiles().length / 2; i++) {
					File picture = dir.listFiles()[i];
					nodes.add(new ProtoNode(dir.getName(),
							getGrayScaleValues(getImg("train/pictures" + ext + "/"
									+ dir.getName() + "/" + picture.getName()))));
				}
			}
			
			serializeNodeList(nodes, "train.ser");
		}

		Collections.shuffle(nodes);

		return nodes;
	}
	
	public static ArrayList<ProtoNode> getTestingData(boolean dataSerialized) {
		ArrayList<ProtoNode> nodes = new ArrayList<ProtoNode>();
		if (dataSerialized) {
			nodes = deserializeNodeList("test.ser");
		} else {
			File testDir = new File("train/pictures" + ext);
			for (File dir : testDir.listFiles()) {
				for (int i = dir.listFiles().length / 2; i < dir.listFiles().length; i++) {
					File picture = dir.listFiles()[i];
					nodes.add(new ProtoNode(dir.getName(), 
							getGrayScaleValues(getImg("train/pictures" + ext + "/" + dir.getName() 
									+ "/" + picture.getName()))));
				}
			}
			
			serializeNodeList(nodes, "test.ser");
		}
		
		Collections.shuffle(nodes);

		return nodes;
	}
	
	public static void serializeNodeList(ArrayList<ProtoNode> n, String filename) {
        try {
        	FileOutputStream fileOut = new FileOutputStream(filename);
        	ObjectOutputStream out = new ObjectOutputStream(fileOut);
        	out.writeObject(n);
        	out.close();
        	fileOut.close();
        	System.out.printf("Serialized data is saved in " + filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<ProtoNode> deserializeNodeList(String filename) {
        ArrayList<ProtoNode> nodes = new ArrayList<ProtoNode>();
		try {
			FileInputStream fileIn = new FileInputStream(filename);
			ObjectInputStream in;
			in = new ObjectInputStream(fileIn);
			nodes = (ArrayList<ProtoNode>) in.readObject();
			in.close();
			fileIn.close();
			System.out.println("Loaded " + filename);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

        return nodes;
	}
	
	public static String[] getPlanktonTypes() {
		File dir = new File("train/pictures" + ext);
		String[] types = new String[dir.listFiles().length];
		for (int i = 0; i < dir.listFiles().length; i++) {
			types[i] = dir.listFiles()[i].getName();
		}
		
		System.out.println("Got types");
		System.out.println(Arrays.toString(types));
		
		return types;
	}
	
	public static ProtoNode[] listToArray(ArrayList<ProtoNode> nodes) {
		return nodes.toArray(new ProtoNode[nodes.size()]);
	}
}
