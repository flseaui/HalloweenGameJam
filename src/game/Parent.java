package game;

import java.util.Random;

public class Parent {
	public int x;
	public int y;
	public float radius;
	Random random = new Random();
	public boolean checkPlayer(Player p, Map m) {
		if (p.x * p.x + p.y * p.y < radius * radius) {
			return true;
			/*int tx = x;
			int ty = y;
			if (Math.abs(tx - p.x) > Math.abs(ty - p.y)) {
				tx += (tx - p.x) / Math.abs(tx - p.x);
			}*/
		}
		return false;
	}

	public void moveParent(Map m)
	{
		int movement = random.nextInt(1);
		int axis = random.nextInt(1);
		int direction = random.nextInt(1);

		if (direction == 0)
		{
			movement = -movement;
		}

		if (axis == 0)
		{
			if (m.solid[x + movement][y])
				x += movement;
		}
		else
		{
			if (m.solid[x][y + movement])
				y += movement;
		}
	}

}
