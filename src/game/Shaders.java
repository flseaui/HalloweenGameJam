package game;

import engine.OpenGL.ShaderProgram;

public class Shaders {
	public static ShaderProgram textureShader;
	public static ShaderProgram colorShader;
	public static ShaderProgram steamShader;
	public static ShaderProgram flipShader;
	public static ShaderProgram golShader;
	public static void createMainShaders() {
		colorShader = new ShaderProgram("roomShader");
		textureShader = new ShaderProgram("textureShaders");
		steamShader = new ShaderProgram("snokeShader");
		flipShader = new ShaderProgram("flipShader");
		golShader = new ShaderProgram("GOLShader");
	}
}
