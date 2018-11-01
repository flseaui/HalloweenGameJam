package game;

import org.joml.Vector2f;
import org.joml.Vector4f;

import static game.Shaders.colorShader;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class Player {
	public Vector2f position;
	public Vector2f velocity;
	public Vector4f[] states;
	public int state = 59;
	public Player() {
		position = new Vector2f();
		velocity = new Vector2f();
		states = new Vector4f[60];
	}
	public Player(Vector2f pos) {
		position = pos;
		velocity = new Vector2f();
		states = new Vector4f[60];
	}
	public void updatePosition(Level gameLevel) {
		updatePosition(gameLevel, 0.04f);
	}
	public void updatePosition(Level gameLevel, float gravity) {
		if (MainView.main.window.keys[GLFW_KEY_W] > 0 || MainView.main.window.keys[GLFW_KEY_SPACE] > 0) {
			if (velocity.y > 0) {
				velocity.y += 0.01f;
			}
		}
		velocity.y -= gravity;
		if (MainView.main.window.keys[GLFW_KEY_LEFT_SHIFT] > 0) {
			velocity.mul(0.01f);
		}
		Level.CollisionStats cStats = gameLevel.getOffsetProgress(position, new Vector2f(10, 20), velocity);
		position.add(velocity.x * cStats.collisionProportion, velocity.y * cStats.collisionProportion);
		if (velocity.lengthSquared() > 0.01) {
			position.sub(velocity.normalize(0.001f, new Vector2f()));
		}
		if (cStats.collisionLine > -1) {
			velocity.x *= 0.8;
			float jumpLength = 0;
			if (MainView.main.window.keys[GLFW_KEY_SPACE] > 0) {
				jumpLength = Math.abs(gameLevel.lines[cStats.collisionLine].deltaNormalized.x * 1.5f);
			}
			if (MainView.main.window.keys[GLFW_KEY_A] > 0) {
				velocity.x -= 0.2f;
			}
			if (MainView.main.window.keys[GLFW_KEY_D] > 0) {
				velocity.x += 0.2f;
			}
			Vector2f groundNormal = gameLevel.lines[cStats.collisionLine].deltaNormalized;
			float frictionForce = Math.abs(velocity.x * groundNormal.y - velocity.y * groundNormal.x) * 0.05f;
			float dotLength = velocity.dot(groundNormal);
			if (dotLength > 0) {
				if (velocity.y > 0) {
					dotLength -= frictionForce + Math.abs(groundNormal.y);
				}else {
					dotLength -= frictionForce;
				}
				if (dotLength < 0) {
					dotLength = 0;
				}
			}else {
				if (velocity.y > 0) {
					dotLength += frictionForce + Math.abs(groundNormal.y);
				}else {
					dotLength += frictionForce;
				}
				if (dotLength > 0) {
					dotLength =- 0;
				}
			}
			groundNormal.mul(dotLength, velocity);
			cStats = gameLevel.getOffsetProgress(position, new Vector2f(10, 20), velocity);
			position.add(velocity.x * cStats.collisionProportion, velocity.y * cStats.collisionProportion);
			if (velocity.lengthSquared() > 0.01) {
				position.sub(velocity.normalize(0.001f, new Vector2f()));
			}
			if (cStats.collisionLine > -1) {
				if (MainView.main.window.keys[GLFW_KEY_SPACE] > 0) {
					jumpLength = Math.max(Math.abs(gameLevel.lines[cStats.collisionLine].deltaNormalized.x), jumpLength);
				}
				groundNormal = gameLevel.lines[cStats.collisionLine].deltaNormalized;
				frictionForce = Math.abs(velocity.x * groundNormal.y - velocity.y * groundNormal.x) * 0.05f;
				dotLength = velocity.dot(groundNormal);
				if (dotLength > 0) {
					if (velocity.y > 0) {
						dotLength -= frictionForce + Math.abs(groundNormal.y);
					}else {
						dotLength -= frictionForce;
					}
					if (dotLength < 0) {
						dotLength = 0;
					}
				}else {
					if (velocity.y > 0) {
						dotLength += frictionForce + Math.abs(groundNormal.y);
					}else {
						dotLength += frictionForce;
					}
					if (dotLength > 0) {
						dotLength =- 0;
					}
				}
				groundNormal.mul(dotLength, velocity);
				cStats = gameLevel.getOffsetProgress(position, new Vector2f(10, 20), velocity);
				position.add(velocity.x * cStats.collisionProportion, velocity.y * cStats.collisionProportion);
				if (velocity.lengthSquared() > 0.01) {
					position.sub(velocity.normalize(0.001f, new Vector2f()));
				}
			}
			velocity.y += jumpLength;
		}
		velocity.mul(cStats.collisionProportion);
	}
	public void updatePower(Level gameLevel) {
		for (int i = 0; i < states.length - 1; ++i) {
			states[i] = states[i + 1];
		}
		states[states.length - 1] = null;
		states[state] = new Vector4f(position.x, position.y, velocity.x, velocity.y);
		if (MainView.main.window.keys[GLFW_KEY_Q] > 0) {
			state -= 2;
			if (state > 0) {
				if (states[state] != null) {
					position.x = states[state].x;
					position.y = states[state].y;
					velocity.x = states[state].z * -1;
					velocity.y = states[state].w * -1;
				}
			}else {
				state = 0;
			}
		}
		if (MainView.main.window.keys[GLFW_KEY_E] > 0) {
			state += 1;
			if (state >= states.length) {
				state = states.length - 1;
			}
			if (states[state] != null) {
				position.x = states[state].x;
				position.y = states[state].y;
				velocity.x = states[state].z;
				velocity.y = states[state].w;
			}else {
				velocity.y *= 2;
				velocity.x *= 2;
				updatePosition(gameLevel, 0.04f/2f);
				velocity.x /= 2;
				velocity.y /= 2;
			}
		}
	}
	public void render() {
		colorShader.enable();
		colorShader.shaders[0].uniforms[0].set(MainView.perspectiveMatrix);
		colorShader.shaders[2].uniforms[0].set(new Vector4f(1f, 0f, 1f, 1f));
		MainView.playerVAO.fullRender();
	}
}
