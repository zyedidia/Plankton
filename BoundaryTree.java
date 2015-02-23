package bfp;

public class BoundaryTree {
	public Node root;
	
	public BoundaryTree(Node root) {
		this.root = root;
	}
	
	public Node query(Node queryNode, boolean isTraining) {
		Node result = root.getClosestNode(queryNode);
		if (isTraining) {
			if (!result.hasSameLabel(queryNode)) {
				result.addChild(queryNode);
				if (result.isRouter) {
					query(result, true);
				}
			}
		}
		
		return result;
	}
}
