package game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Map {
	
	public int width;
	public int height;
	
	public int[][] tiles;
	public boolean[][] solid;
	
	public Child[] children;
	public Parent[] parents;
	
	/*public static final int[][] tileReference = {
	
	};*/
	//0 = floor
	//1 = wall
	//2 = void
	public Map(String path) {
		
		try {
			BufferedImage b = ImageIO.read(getClass().getClassLoader().getResourceAsStream(path));
			int[] array = new int[b.getWidth() * b.getWidth()];
			b.getRGB(0, 0, b.getWidth(), b.getHeight(), array, 0, b.getWidth());
			int x = 0;
			
			solid = new boolean[b.getWidth()][b.getHeight()];
			tiles = new int[b.getWidth()][b.getHeight()];
			ArrayList<Child> childrenTemp = new ArrayList<>();
			ArrayList<Parent> parentTemp = new ArrayList<>();
			
			for (int i = 0; i < array.length; ++i) {
				//System.out.println(array[i]);
				solid[i % b.getWidth()][i / b.getHeight()] = array[i] == 0xff7f7f7f;
				if (array[i] == 0xffff0000) {
					tiles[i % b.getWidth()][i / b.getHeight()] = 0;
					parentTemp.add(new Parent(i % b.getWidth(), i / b.getHeight()));
				} else if (array[i] == 0xff0000ff) {
					tiles[i % b.getWidth()][i / b.getHeight()] = 0;
					childrenTemp.add(new Child(i % b.getWidth(), i / b.getHeight()));
				} else {
					tiles[i % b.getWidth()][i / b.getHeight()] = array[i];
				}
			}
			
			children = new Child[childrenTemp.size()];
			for (int i = 0; i < childrenTemp.size(); ++i) {
				children[i] = childrenTemp.get(i);
			}
			parents = new Parent[parentTemp.size()];
			for (int i = 0; i < parentTemp.size(); ++i) {
				parents[i] = parentTemp.get(i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*public static void render() {
		for (int x = 0; x < width) {
		
		}
	}*/
}
