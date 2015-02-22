package bfp;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ProtoNode implements Serializable {
	public String label;
	public int[][] img;
	
	public ProtoNode(String label, int[][] img) {
		this.label = label;
		this.img = img;
	}
	
	public String label() {
		return label;
	}
	
	public int[][] img() {
		return img;
	}
}
