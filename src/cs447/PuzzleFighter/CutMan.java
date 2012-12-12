package cs447.PuzzleFighter;

import java.awt.geom.AffineTransform;

import jig.engine.RenderingContext;
import jig.engine.ResourceFactory;
import jig.engine.Sprite;
import jig.engine.util.Vector2D;

public class CutMan extends RobotMaster {
	private long timer;
	private int frame;
	public int attackFrame;
	private CutManWeapon weapon;

	public CutMan() {
		super(PuzzleFighter.CUT_SHEET + "#idle");
		this.timer = 0;
		frame = 0;
		setFrame(frame);
		attackFrame = 0;
	}

	public void render(RenderingContext rc, boolean left) {
		AffineTransform t = new AffineTransform();
		if (left) {
			t.translate(400, 0);
		}
		else {
			t.scale(-1, 1);
			t.translate(210, 0);
		}
		t.scale(3, 3);
		render(rc, t);
		if (weapon != null) {
			weapon.render(rc, t);
		}
	}

	public void update(long deltaMs) {
		timer += deltaMs;

		if (weapon != null) {
			if (weapon.update(deltaMs)) {
				weapon = null;
				attackFrame = 0;
				setImage(PuzzleFighter.CUT_SHEET + "#idle");
			}
		}
		
		if (attackFrame == 0) {
			if (timer >= 200) {
				timer = 0;
				frame = (frame + 1) % 2;
				setFrame(frame);				
			}
		}
		else if (attackFrame == 1) {
			if (timer >= 300) {
				setFrame(1);
				this.weapon = new CutManWeapon();
				attackFrame = 2;
			}
		}
	}

	public void attack() {
		this.attackFrame = 1;
		setImage(PuzzleFighter.CUT_SHEET + "#attack");
		setFrame(0);
	}

	private class CutManWeapon extends Sprite {
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
}