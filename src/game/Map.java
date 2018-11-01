package game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Map {
	public boolean[][] walls;
	public Map(String path) {
		try {
			BufferedImage b = ImageIO.read(getClass().getClassLoader().getResourceAsStream(path));
			int[] array = new int[b.getWidth() * b.getWidth()];
			b.getRGB(0, 0, b.getWidth(), b.getHeight(), array, 0, b.getWidth());
			int x = 0;
			for (int i = 0; i < array.length; ++i) {
				walls[i % b.getWidth()][i / b.getHeight()] = array[i] == 0x7f7f7f;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}