package game;

public class Parent {
	public int x;
	public int y;
	public float radius;
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
}
