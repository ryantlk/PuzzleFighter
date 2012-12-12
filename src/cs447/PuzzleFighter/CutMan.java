package cs447.PuzzleFighter;

import java.awt.geom.AffineTransform;

import jig.engine.RenderingContext;
import jig.engine.ResourceFactory;
import jig.engine.Sprite;
import jig.engine.util.Vector2D;

public class CutMan extends Sprite {
	private long timer;
	private int frame;
	public int attackFrame = 0;
	private CutManWeapon weapon;

	public CutMan() {
		super(PuzzleFighter.CUT_SHEET + "#idle");
		this.position = new Vector2D(0,0);
		this.timer = 0;
		frame = 0;
		setFrame(frame);
		attackFrame = 0;
	}

	public void render(RenderingContext rc, boolean left) {
		AffineTransform t = AffineTransform.getTranslateInstance(position.getX(), position.getY());
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

	private void setImage(String rsc) {
		frames = ResourceFactory.getFactory().getFrames(rsc);
		width = frames.get(0).getWidth();
		height = frames.get(0).getHeight();
		setFrame(0);
	}
}