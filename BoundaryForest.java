package bfp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class BoundaryForest {
	public BoundaryTree[] trees;
	public int[] numRight;
	public int[] count;
	public String[] planktonTypes;
	
	public BoundaryForest(int numTrees, String[] planktonTypes) {
		trees = new BoundaryTree[numTrees];
		numRight = new int[numTrees + 1];
		count = new int[numTrees + 1];
		this.planktonTypes = planktonTypes;
	}
	
	public void train(ProtoNode[] trainingDataRaw) {
		for (int j = 0; j < trees.length; j++) {
			Node[] trainingData = new Node[trainingDataRaw.length];
			for (int i = 0; i < trainingDataRaw.length; i++) {
				trainingData[i] = new Node(trainingDataRaw[i]);
			}
			trainingData = shuffleArray(trainingData);
			Node rootNode = trainingData[0];
			BoundaryTree tree = new BoundaryTree(rootNode);
			trees[j] = tree;
			
			System.out.println("Tree " + (j + 1) + "/" + trees.length);

			for (int i = 1; i < trainingData.length; i++) {
				tree.query(trainingData[i], true);
			}
		}
	}
	
	public String query(Node queryNode) {
		ArrayList<String> votes = new ArrayList<String>();
		for (BoundaryTree tree : trees) {
			Node result = tree.query(queryNode, false);
			votes.add(result.label());
		}
		
		for (String s : planktonTypes) {
			count[Collections.frequency(votes, s)] += 1;
			if (s.equals(queryNode.label())) {
				numRight[Collections.frequency(votes, s)] += 1;
			}
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
