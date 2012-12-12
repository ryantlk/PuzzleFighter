package cs447.PuzzleFighter;

import java.awt.geom.AffineTransform;

import jig.engine.RenderingContext;
import jig.engine.Sprite;
import jig.engine.util.Vector2D;

public class MegaMan extends RobotMaster {
	private long timer;
	private long chargeTimer;
	private int frame;
	private boolean attacking;
	private MegaBuster weapon;

	public MegaMan() {
		super(PuzzleFighter.MEGA_SHEET + "#idle");
		this.timer = 0;
		this.chargeTimer = 0;
		this.attacking = false;
		this.setFrame(0);
	}

	public void attack() {
		this.attacking = true;
		this.frame = 0;
		this.setImage(PuzzleFighter.MEGA_SHEET + "#charge");
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
		this.timer += deltaMs;

		if (weapon != null) {
			if (weapon.update(deltaMs)) {
				weapon = null;
			}
		}

		if (!attacking) {
			if (timer >= 100) {
				timer = 0;
				frame = (frame + 1) % 4;
				setFrame(frame);
			}
		}
		else if (attacking) {
			this.chargeTimer += deltaMs;

			if (chargeTimer < 1000 && timer >= 10) {
				timer = 0;
				frame = (frame + 1) % 3;
				setFrame(frame);
			}
			else if (chargeTimer > 1000 && chargeTimer < 2000 && timer >= 50) {
				timer = 0;
				frame = 3 + (frame + 1) % 3;
				setFrame(frame);
			}
			else if (chargeTimer > 2000 && chargeTimer < 3000) {
				if (frame != 6) {
					frame = 6;
					setFrame(6);
					this.weapon = new MegaBuster();
				}
			}
			else if (chargeTimer > 2400) {
				attacking = false;
				frame = 0;
				timer = 0;
				chargeTimer = 0;
				setFrame(frame);
				setImage(PuzzleFighter.MEGA_SHEET + "#idle");
			}
		}
	}

	private class MegaBuster extends Sprite {
		long timer;
		long frameTimer;
		int frame;
		
		public MegaBuster() {
			super(PuzzleFighter.MEGA_SHEET + "#buster");
			timer = frameTimer = 0;
			frame = 0;
		}

		public boolean update(long deltaMs) {
			this.timer += deltaMs;
			this.frameTimer += deltaMs;
			if (frameTimer > 75) {
				frameTimer = 0;
				frame = (frame + 1) % 2;
				setFrame(frame);
			}
			
			return (timer > 400);
		}

		public void render(RenderingContext rc, AffineTransform t) {
			t.translate(-70.0 * timer / 400.0, 12);
			super.render(rc, t);
		}
	}
}
