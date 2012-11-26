package cs447.PuzzleFighter;

import jig.engine.RenderingContext;
import jig.engine.util.Vector2D;

public class GemPair {
	public Gem gem1;
	public Gem gem2;

	public GemPair(int x, int y, PlayField pf) {
		this.gem1 = new Gem(pf, new Vector2D(x, y+1), GemType.GEM);
		this.gem2 = new Gem(pf, new Vector2D(x, y), GemType.GEM, "greenGem");
	}

	public boolean move(Vector2D dv) {
		Gem t1;
		Gem t2;
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
