package game;

import org.joml.Vector2f;

public class Line {
	public int inda;
	public int indb;
	public Vector2f delta;
	public Vector2f deltaNormalized;
	public Line(int firstInd, int secondInd, Level lvl) {
		inda = firstInd;
		indb = secondInd;
		delta = new Vector2f(lvl.points[indb].x - lvl.points[inda].x, lvl.points[indb].y - lvl.points[inda].y);
		deltaNormalized = delta.normalize(new Vector2f());
	}
}
