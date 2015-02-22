package bfp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageManipulator {
	public static BufferedImage resizeImg(BufferedImage originalImage, int newWidth, int newHeight) {
		BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
		g.dispose();

		return resizedImage;
	}
	
	public static BufferedImage resizeImg(String imgName, int newWidth, int newHeight) {
		return resizeImg(getImg(imgName), newWidth, newHeight);
	}
	
	public static BufferedImage getImg(String imgPath) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(imgPath));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return img;
	}
	
	public static void writeImgToFile(String filename, BufferedImage img) {
		try {
			ImageIO.write(img, "jpg", new File(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int[][] getGrayScaleValues(BufferedImage img) {
		int[][] grayscales = new int[img.getWidth()][img.getHeight()];
		
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				int red = new Color(img.getRGB(i, j)).getRed();
				int green = new Color(img.getRGB(i, j)).getGreen();
				int blue = new Color(img.getRGB(i, j)).getBlue();
				
				int grayscale = (red + green + blue) / 3;
				grayscales[i][j] = grayscale;
			}
		}
		
		return grayscales;
	}
	
	public static void resizeDir(String dirName, int width, int height) {
		String ext = "_" + width + "x" + height;
		File dir = new File("train/" + dirName);
		File newDir = new File("train/" + dirName + ext);
		newDir.mkdir();
		
		System.out.println(newDir.getName());
		
		for (File f : dir.listFiles()) {
			String filename = f.getName().substring(0, f.getName().indexOf('.')) + ext + ".jpg";
			writeImgToFile("train/" + newDir.getName() + "/" + filename, resizeImg(getImg(f.getAbsolutePath()), width, height));
		}
	}
	

	
	@SuppressWarnings("unused")
	private static void print2dArray(int[][] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				System.out.print(arr[i][j] + ", ");
			}
			System.out.println();
		}
	}
}	

