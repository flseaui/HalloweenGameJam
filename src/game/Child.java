package game;

import engine.OpenGL.Texture;
import engine.OpenGL.VAO;
import org.joml.Matrix4f;

import static game.Shaders.textureShader;

public class Child {
	public int x;
	public int y;
	
	public static Texture[] childTex;

	public boolean dead;

	public Child(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public static void renderChildren(Child[] parents, VAO vao) {
		childTex[Math.abs(3 * MainView.frame / MainView.main.window.fps % 4 - 1)].bind();
		for (Child p: parents) {
			if (!p.dead) {
				textureShader.shaders[0].uniforms[0].set(MainView.getPerspectiveMatrix().translate(p.x * 10f, p.y * 10f, 0));
				vao.drawTriangles();
			}
		}
	}
}
