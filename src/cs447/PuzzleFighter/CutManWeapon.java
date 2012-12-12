package cs447.PuzzleFighter;

import java.awt.geom.AffineTransform;

import jig.engine.RenderingContext;
import jig.engine.Sprite;
import jig.engine.util.Vector2D;

public class CutManWeapon extends Sprite {
	long animTimer;
	long frameTimer;
	int frame = 0;

	public CutManWeapon() {
		super(PuzzleFighter.CUT_SHEET + "#weapon");
		this.animTimer = 0;
		this.frameTimer = 0;
	}

	public boolean update(long deltaMs) {
		animTimer += deltaMs;
		frameTimer += deltaMs;
		if (frameTimer > 150) {
			frame = (frame + 1) % 4;
			setFrame(frame);
			frameTimer = 0;
		}
		if (animTimer > 1000) {
			return true;
		}
		return false;
	}

	public void render(RenderingContext rc, AffineTransform t) {
		t.translate(-40.0, 12.0);
		t.translate(Math.sin(Math.PI/2.0 + Math.PI * 2 * animTimer / 1000.0) * 30, Math.sin(Math.PI * 2 * animTimer / 500.0) * 7.0);
		super.render(rc, t);
	}
}