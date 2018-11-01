package game;

import engine.EnigView;
import engine.OpenGL.EnigWindow;
import engine.OpenGL.FBO;
import engine.OpenGL.ShaderProgram;

public class PerlinView extends EnigView {
	public ShaderProgram shader;
	public PerlinView(EnigWindow window) {
		super(window);
		shader = new ShaderProgram("perlinShader");
	}
	@Override
	public boolean loop() {
		FBO.prepareDefaultRender();
		shader.enable();
		Main.screenObj.fullRender();
		return false;
	}
}
