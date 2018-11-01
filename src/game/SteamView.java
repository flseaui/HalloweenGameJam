package game;

import engine.EnigView;
import engine.OpenGL.EnigWindow;
import engine.OpenGL.FBO;
import engine.OpenGL.Texture;
import engine.OpenGL.VAO;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static game.Shaders.flipShader;
import static game.Shaders.golShader;
import static game.Shaders.steamShader;

public class SteamView extends EnigView {
	public FBO fba;
	public FBO fbb;
	public VAO screenObj;
	public boolean a;
	public Vector2f offsetVector;
	public Texture noiseTexture;
	public Texture otherNoiseTexture;
	public Vector4f offset = new Vector4f();
	public SteamView(EnigWindow window) {
		super(window);
		fba = new FBO(new Texture("res/smokeTestB.png"));
		fbb = new FBO(new Texture("res/smokeTestB.png"));
		noiseTexture = new Texture("res/gradientNoise.png");
		otherNoiseTexture = new Texture("res/noiseB.png");
		offsetVector = new Vector2f(1f/fba.getBoundTexture().getWidth(), 1f/fba.getBoundTexture().getHeight());
		screenObj = new VAO(-1, -1, 2, 2);
	}
	@Override
	public boolean loop() {
		offset.x += 0.01;
		offset.y += 0.01;
		offset.z += 0.005;
		offset.w += 0.005;
		if (a) {
			fba.prepareForTexture();
		}else {
			fbb.prepareForTexture();
		}
		steamShader.enable();
		steamShader.shaders[2].uniforms[0].set(offset);
		/*golShader.enable();
		golShader.shaders[2].uniforms[1].set(offsetVector);*/
		/*if (a) {
			fbb.getBoundTexture().bind();
		}else {
			fba.getBoundTexture().bind();
		}*/
		noiseTexture.bindPosition(1);
		otherNoiseTexture.bind();
		screenObj.fullRender();
		FBO.prepareDefaultRender();
		flipShader.enable();
		if (a) {
			fba.getBoundTexture().bind();
		}else {
			fbb.getBoundTexture().bind();
		}
		screenObj.fullRender();
		a = !a;
		return false;
	}
}
