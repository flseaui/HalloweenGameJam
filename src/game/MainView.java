package game;

import engine.*;
import engine.OpenGL.*;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static game.Shaders.textureShader;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class MainView extends EnigView {
	public static MainView main;
	
	//project variables
	public static VAO playerVAO;
	public static Matrix4f perspectiveMatrix;
	
	public static Player mainPlayer;
	public static Map mainMap;
	
	public static Texture[] playerTex;
	
	public static int frame;
	public static float aspectRatio;
	
	public MainView(EnigWindow window) {
		super(window);
		glDisable(GL_DEPTH_TEST);
		playerVAO = new VAO(-5, -5, 10, 10);
		perspectiveMatrix = window.getSquarePerspectiveMatrix(150f);
		mainPlayer = new Player();
		mainMap = new Map("res/levels/level0.png");
		playerTex = new Texture[] {new Texture("res/sprites/player/player0.png"), new Texture("res/sprites/player/player1.png")};
		Child.childTex = new Texture[] {new Texture("res/sprites/child/idle/idle_0.png"), new Texture("res/sprites/child/idle/idle_1.png"), new Texture("res/sprites/child/idle/idle_2.png"), new Texture("res/sprites/child/idle/idle_2.png")};
		Parent.tex = new Texture("res/sprites/parent/walk_down/walk_down_0.png");
	}
	
	@Override
	public boolean loop() {
		++frame;
		
		mainPlayer.checkMovement(window);
		
		renderScene();
		
		if (UserControls.quit(window)) {
			return true;
		}
		return false;
	}
	
	/**
	 * @author (Emmett / sqrt(Emmett))^2
	 * 
	 * sets the aspect ratio of the game
	 * 
	 * @param aspect aspect ratio
	 */
	public static void setAspectRatio(float aspect) {
		aspectRatio = aspect;
	}
	
	private void renderScene() {
		FBO.prepareDefaultRender();
		textureShader.enable();
		playerTex[Math.abs(2 * frame / window.fps % 2)].bind();
		textureShader.shaders[0].uniforms[0].set(new Matrix4f(perspectiveMatrix).translate(mainPlayer.x * 10f, mainPlayer.y * 10f, 0));
		
		playerVAO.prepareRender();
		playerVAO.drawTriangles();
		Parent.renderParents(mainMap.parents, playerVAO);
		Child.renderParents(mainMap.children, playerVAO);
		playerVAO.unbind();
		
		//Math.abs(3 * frame / window.fps % 4 - 1)
		
		
		float windowRatio = (float)window.getWidth() / window.getHeight();
		boolean fullWidth;
		int frameWidth;
		int frameHeight;
		
		if(windowRatio < aspectRatio) {
			fullWidth = true;
			frameWidth = window.getWidth();
			frameHeight = (int)(window.getHeight() * (windowRatio/aspectRatio));
		}else {
			fullWidth = false;
			frameWidth = (int)(window.getWidth() * (aspectRatio/windowRatio));
			frameHeight = window.getHeight();
		}
		
		if(fullWidth) {
			ebetSetSize(0, (int)(window.getHeight()/2 - frameHeight/2), frameWidth, frameHeight);
		} else {
			ebetSetSize((int)Math.round(window.getWidth()/2 - frameWidth/2), 0, frameWidth, frameHeight);
		}
	}
	
	public static Matrix4f getPerspectiveMatrix() {
		return new Matrix4f(perspectiveMatrix);
	}
}
