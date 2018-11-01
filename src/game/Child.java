package game;

import engine.OpenGL.Texture;
import engine.OpenGL.VAO;
import org.joml.Matrix4f;

import static game.Shaders.textureShader;

public class Child {
	public int x;
	public int y;
	
	public static Texture[] childTex;
	public static Texture[] childKillTextures;

	public boolean dead;

	public boolean fullyDead;

	public Child(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public static void renderChildren(Child[] children, VAO vao) {
		for (Child child: children) {
			if (child.fullyDead) continue;

			if (child.dead) {
				int index = Math.abs(12 * MainView.frame / MainView.main.window.fps % 13 - 1);
				if (index == 11)
					child.fullyDead = true;
				childKillTextures[index].bind();
			}
			else
			{
				childTex[Math.abs(3 * MainView.frame / MainView.main.window.fps % 4 - 1)].bind();
			}
			textureShader.shaders[0].uniforms[0].set(MainView.getPerspectiveMatrix().translate(child.x * 10f, child.y * 10f, 0));
			vao.drawTriangles();
		}
	}
}
