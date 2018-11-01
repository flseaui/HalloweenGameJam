package game;

import engine.*;
import engine.OpenGL.*;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class MainView extends EnigView {
	public static MainView main;
	
	//project variables
	public static VAO playerVAO;
	public static Matrix4f perspectiveMatrix;
	public Player mainPlayer;
	public Level gameLevel;

	public MainView(EnigWindow window) {
		super(window);
		playerVAO = new VAO(-5, -10, 10, 20);
		perspectiveMatrix = window.getSquarePerspectiveMatrix(150f);
		gameLevel = new Level("res/levelTest.lvl");
		glDisable(GL_DEPTH_TEST);
		mainPlayer = new Player();
	}
	
	@Override
	public boolean loop() {
		System.out.println(gameLevel.getTag());
		mainPlayer.updatePosition(gameLevel);
		mainPlayer.updatePower(gameLevel);
		
		renderScene();
		
		if (UserControls.quit(window)) {
			return true;
		}
		return false;
	}
	
	private void renderScene() {
		FBO.prepareDefaultRender();
		
		gameLevel.render(mainPlayer.position);
		
		mainPlayer.render();
	}
	
	public static Matrix4f getPerspectiveMatrix() {
		return new Matrix4f(perspectiveMatrix);
	}
}
