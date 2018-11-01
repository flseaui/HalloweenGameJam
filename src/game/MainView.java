package game;

import engine.*;
import engine.OpenAL.Sound;
import engine.OpenAL.SoundSource;
import engine.OpenGL.*;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.*;

import static game.Shaders.textureShader;
import static game.UserControls.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class MainView extends EnigView {
	public static MainView main;
	
	//project variables
	public static VAO playerVAO, carVAO;
	public static Matrix4f perspectiveMatrix;
	
	public static Player mainPlayer;
	public static Map mainMap;

	public static DeathCounter deathCounter;

	public static Texture[] playerTex;
	public static Texture ghostBustersCar;

	public Sound mainTheme;
	public Sound gameOverTheme;

	public SoundSource soundSource;


	public static int frame;
	public static float aspectRatio;

	public boolean carTime;
	public int carX, carY;


	public static Vector2f camPos = new Vector2f();

	public MainView(EnigWindow window) {
		super(window);
		glDisable(GL_DEPTH_TEST);
		playerVAO = new VAO(-5, -5, 10, 10);
		carVAO = new VAO(-5, -5, 60, 30);
		perspectiveMatrix = window.getSquarePerspectiveMatrix(150f);
		mainPlayer = new Player();
		mainMap = new Map("res/levels/level0.png");
		deathCounter = new DeathCounter();
		playerTex = new Texture[] {new Texture("res/sprites/player/player0.png"), new Texture("res/sprites/player/player1.png")};
		ghostBustersCar = new Texture("res/sprites/ghostbusters_car.png");
		Child.childTex = new Texture[] {new Texture("res/sprites/child/idle/idle_0.png"), new Texture("res/sprites/child/idle/idle_1.png"), new Texture("res/sprites/child/idle/idle_2.png"), new Texture("res/sprites/child/idle/idle_2.png")};
		Parent.tex = new Texture("res/sprites/parent/walk_down/walk_down_0.png");
		DeathCounter.timesTexture = new Texture("res/sprites/ui/kill_count/times.png");
		DeathCounter.iconTexture = new Texture("res/sprites/ui/kill_count/icon.png");
		DeathCounter.counterTextures = new Texture[] { new Texture("res/sprites/ui/kill_count/0.png"), new Texture("res/sprites/ui/kill_count/1.png"), new Texture("res/sprites/ui/kill_count/2.png"), new Texture("res/sprites/ui/kill_count/3.png"), new Texture("res/sprites/ui/kill_count/4.png"), new Texture("res/sprites/ui/kill_count/5.png"), new Texture("res/sprites/ui/kill_count/6.png"), new Texture("res/sprites/ui/kill_count/7.png"), new Texture("res/sprites/ui/kill_count/8.png"), new Texture("res/sprites/ui/kill_count/9.png") };

		soundSource = new SoundSource();
		gameOverTheme = new Sound("res/bustTheme.wav");
		mainTheme = new Sound("res/mainTheme.wav");
		soundSource.setLoop();
		soundSource.playSound(mainTheme);
		Map.wallTexture = new Texture("res/sprites/tiles/brick_wall.png");
		Map.floorTexture = new Texture("res/sprites/tiles/tile_floor_0.png");
	}
	
	@Override
	public boolean loop() {
		++frame;
		
		mainPlayer.checkMovement(window);
		updateCameraPos(window);

		if (frame % window.fps == 0) {
			for (Parent p : mainMap.parents) {
				p.move(mainMap);
			}
		}


		if (UserControls.test(window))
			if (DeathCounter.deaths >= 9)
				gameOver();
			else
				DeathCounter.deaths++;

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

	public void gameOver()
	{

		carX = mainPlayer.x * 10 - 30;
		carY = mainPlayer.y * 10 - 15;
		soundSource.setNoLoop();
		soundSource.stop();
		soundSource.playSound(gameOverTheme);
		carTime = true;
	}

	private void renderScene() {
		FBO.prepareDefaultRender();

		textureShader.enable();
		playerVAO.prepareRender();

		mainMap.render(playerVAO);

		playerTex[Math.abs(2 * frame / window.fps % 2)].bind();
		textureShader.shaders[0].uniforms[0].set(getPerspectiveMatrix().translate(mainPlayer.x * 10f, mainPlayer.y * 10f, 0));

		playerVAO.drawTriangles();
		Parent.renderParents(mainMap.parents, playerVAO);
		Child.renderParents(mainMap.children, playerVAO);
		DeathCounter.renderCounter(deathCounter, playerVAO);
		playerVAO.unbind();


		if (carTime)
		{
			ghostBustersCar.bind();
			textureShader.shaders[0].uniforms[0].set(new Matrix4f(perspectiveMatrix).translate(carX, carY, 0));
			carVAO.prepareRender();
			carVAO.drawTriangles();
		}

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
	
	public static void updateCameraPos(EnigWindow w) {
		if (w.keys[cup] > 0) {
			camPos.y -= 0.5f;
		}
		if (w.keys[cdown] > 0) {
			camPos.y += 0.5f;
		}
		if (w.keys[cleft] > 0) {
			camPos.x += 0.5f;
		}
		if (w.keys[cright] > 0) {
			camPos.x -= 0.5f;
		}
	}

	public static Matrix4f getPerspectiveMatrix() {
		return new Matrix4f(perspectiveMatrix).translate(camPos.x, camPos.y, 0);
	}
}
