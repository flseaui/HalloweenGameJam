package game;

import engine.OpenGL.ShaderProgram;

public class Shaders {
	public static ShaderProgram textureShader;
	public static ShaderProgram flipShader;
	public static void createMainShaders() {
		textureShader = new ShaderProgram("textureShaders");
		flipShader = new ShaderProgram("flipShader");
	}
}
