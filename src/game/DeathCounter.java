package game;

import engine.OpenGL.Texture;
import engine.OpenGL.VAO;
import org.joml.Matrix4f;

import static game.Shaders.textureShader;

public class DeathCounter {

    public static Texture[] counterTextures;
    public static Texture iconTexture;
    public static Texture timesTexture;

    public static int deaths;

    public static void renderCounter(DeathCounter counters, VAO vao) {
        iconTexture.bind();
        textureShader.shaders[0].uniforms[0].set(new Matrix4f(MainView.perspectiveMatrix).translate(10, 0, 0));
        vao.drawTriangles();
        timesTexture.bind();
        textureShader.shaders[0].uniforms[0].set(new Matrix4f(MainView.perspectiveMatrix).translate(20, 0, 0));
        vao.drawTriangles();
        counterTextures[deaths].bind();
        textureShader.shaders[0].uniforms[0].set(new Matrix4f(MainView.perspectiveMatrix).translate(30, 0, 0));
        vao.drawTriangles();
    }

}
