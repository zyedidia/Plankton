package bfp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class BoundaryForest {
	public BoundaryTree[] trees;
	
	public BoundaryForest(int numTrees) {
		trees = new BoundaryTree[numTrees];
	}
	
	public void train(Node[] trainingData) {
		for (int j = 0; j < trees.length; j++) {
			trainingData = shuffleArray(trainingData);
			Node rootNode = trainingData[0];
			BoundaryTree tree = new BoundaryTree(rootNode);
			trees[j] = tree;

			for (int i = 1; i < trainingData.length; i++) {
				tree.query(trainingData[i], true);
				System.out.println("Tree " + j + "/" + trees.length + ": " + ((double) i / trainingData.length * 100));
			}
		}
	}
	
	public String query(Node queryNode) {
		ArrayList<String> votes = new ArrayList<String>();
		for (BoundaryTree tree : trees) {
			Node result = tree.query(queryNode, false);
			votes.add(result.label);
		}
		
		return mostCommon(votes);
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
	
	public static <T> T mostCommon(List<T> list) {
	    Map<T, Integer> map = new HashMap<>();

	    for (T t : list) {
	        Integer val = map.get(t);
	        map.put(t, val == null ? 1 : val + 1);
	    }

	    Entry<T, Integer> max = null;

	    for (Entry<T, Integer> e : map.entrySet()) {
	        if (max == null || e.getValue() > max.getValue())
	            max = e;
	    }

	    return max.getKey();
	}
}