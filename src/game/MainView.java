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
	
	public MainView(EnigWindow window) {
		super(window);
		playerVAO = new VAO(-5, -10, 10, 20);
		perspectiveMatrix = window.getSquarePerspectiveMatrix(150f);
		glDisable(GL_DEPTH_TEST);
	}
	
	@Override
	public boolean loop() {
		
		renderScene();
		
		if (UserControls.quit(window)) {
			return true;
		}
		return false;
	}
	
	private void renderScene() {
		FBO.prepareDefaultRender();
		
	}
	
	public static Matrix4f getPerspectiveMatrix() {
		return new Matrix4f(perspectiveMatrix);
	}
}
