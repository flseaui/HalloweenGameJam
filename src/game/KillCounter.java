package game;

import engine.OpenGL.Texture;
import engine.OpenGL.VAO;
import org.joml.Matrix4f;

import static game.Shaders.textureShader;

public class KillCounter {

    public static Texture[] counterTextures;
    public static Texture iconTexture;
    public static Texture timesTexture;

    public static int deaths;

    public static void renderCounter(KillCounter counters, VAO vao) {
        iconTexture.bind();
        textureShader.shaders[0].uniforms[0].set(new Matrix4f(MainView.perspectiveMatrix).translate(-127, 69, 0));
        vao.drawTriangles();
        timesTexture.bind();
        textureShader.shaders[0].uniforms[0].set(new Matrix4f(MainView.perspectiveMatrix).translate(-117, 69, 0));
        vao.drawTriangles();
        counterTextures[deaths].bind();
        textureShader.shaders[0].uniforms[0].set(new Matrix4f(MainView.perspectiveMatrix).translate(-107, 69, 0));
        vao.drawTriangles();
    }
}
