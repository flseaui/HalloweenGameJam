package game;

import engine.OpenGL.EnigWindow;

public class Player {
	int x = 0;
	int y = 0;
	int animFrame;
	public void checkMovement(EnigWindow window) {
		if (UserControls.up(window)) {
			y += 1;
		}
		if (UserControls.down(window)) {
			y -= 1;
		}
		if (UserControls.left(window)) {
			x -= 1;
		}
		if (UserControls.right(window)) {
			x += 1;
		}
	}

}
