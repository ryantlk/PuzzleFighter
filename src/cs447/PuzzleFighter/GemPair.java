package cs447.PuzzleFighter;

import jig.engine.RenderingContext;
import jig.engine.util.Vector2D;

public class GemPair {
	public ColoredGem gem1;
	public ColoredGem gem2;

	public GemPair(ColoredGem gem1, ColoredGem gem2) {
		this.gem1 = gem1;
		this.gem2 = gem2;
	}

	public boolean move(Vector2D dv) {
		ColoredGem t1;
		ColoredGem t2;
		t1 = gem1;
		t2 = gem2;

		if (!t1.move(dv)) {
			t2 = gem1;
			t1 = gem2;
		}
		else {
			t1.move(dv.scale(-1));
		}

		if (!t1.move(dv)){
			return false;
		}
		if (!t2.move(dv)) {
			t1.move(dv.scale(-1));
			return false;
		}
		return true;
	}

	public boolean rotateClockwise() {
		return gem2.rotateClockwise(gem1.pos);
	}

	public boolean rotateCounterClockwise() {
		return gem2.rotateCounterClockwise(gem1.pos);
	}

	public void render(RenderingContext rc) {
		gem1.render(rc);
		gem2.render(rc);
	}

	public boolean contains(Gem g) {
		return (gem1 == g || gem2 == g);
	}
}
