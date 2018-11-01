package game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Map {
	
	public int width;
	public int height;
	
	public int[][] tiles;
	public boolean[][] solid;
	
	public Child[] children;
	public Parent[] parents;
	public Player ghost;
	
	public static final int[][] tileReference = {
			
	};
	
	public Map(String path) {
		
		try {
			BufferedImage b = ImageIO.read(getClass().getClassLoader().getResourceAsStream(path));
			int[] array = new int[b.getWidth() * b.getWidth()];
			b.getRGB(0, 0, b.getWidth(), b.getHeight(), array, 0, b.getWidth());
			int x = 0;
			for (int i = 0; i < array.length; ++i) {
				solid[i % b.getWidth()][i / b.getHeight()] = array[i] == 0x7f7f7f;
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
