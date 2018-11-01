package game;
import java.util.Random;
import engine.OpenGL.Texture;
import engine.OpenGL.VAO;
import org.joml.Matrix4f;

import static game.Shaders.textureShader;

public class Parent {
	public int x;
	public int y;
	public float radius;
	
	public static Texture tex;
	
	public Parent(int x, int y) {
		this.x = x;
		this.y = y;
	}

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

	public void move(Map m) {
		int moveDir = (int) (Math.random() * 4);
		if (moveDir == 0) {
			if (!m.solid[x + 1][y]) {
				++x;
			}
		}else if (moveDir == 1) {
			if (!m.solid[x - 1][y]) {
				--x;
			}
		}else if (moveDir == 2) {
			if (!m.solid[x][y + 1]) {
				++y;
			}
		}else if (moveDir == 3) {
			if (!m.solid[x][y - 1]) {
				--y;
			}
		}
	}
	
	public static void renderParents(Parent[] parents, VAO vao) {
		tex.bind();
		for (Parent p: parents) {
			textureShader.shaders[0].uniforms[0].set(MainView.getPerspectiveMatrix().translate(p.x * 10f, p.y * 10f, 0));
			vao.drawTriangles();
		}
	}
}
