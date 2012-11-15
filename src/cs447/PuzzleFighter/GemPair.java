package cs447.PuzzleFighter;

import jig.engine.RenderingContext;
import jig.engine.util.Vector2D;

public class GemPair {
	private PlayField pf;
	public Gem gem1;
	public Gem gem2;
	public Vector2D pos1;
	public Vector2D pos2;

	public GemPair(int x, int y, PlayField pf) {
		this.gem1 = new Gem(GemType.GEM);
		this.gem2 = new Gem(GemType.GEM, "greenGem");
		this.pos1 = new Vector2D(x, y);
		this.pos2 = new Vector2D(x, y-1);
		this.pf = pf;
	}

	public boolean move(Vector2D dv) {
		Vector2D newPos1 = pos1.translate(dv);
		Vector2D newPos2 = pos2.translate(dv);

		if (pf.isFilled(newPos1) || pf.isFilled(newPos2)) {
			return false;
		}

		pos1 = newPos1;
		pos2 = newPos2;

		return true;
	}

	public void rotateClockwise() {
		Vector2D newPos2 = pos2.translate(pos1.scale(-1));
		newPos2 = new Vector2D(-newPos2.getY(), newPos2.getX());
		newPos2 = newPos2.translate(pos1);
		if (!pf.isFilled(newPos2)) {
			pos2 = newPos2;
		}
	}

	public void rotateCounterClockwise() {
		Vector2D newPos2 = pos2.translate(pos1.scale(-1));
		newPos2 = new Vector2D(newPos2.getY(), -newPos2.getX());
		newPos2 = newPos2.translate(pos1);
		if (!pf.isFilled(newPos2)) {
			pos2 = newPos2;
		}
	}

	public void render(RenderingContext rc) {
		gem1.render(rc, pos1);
		gem2.render(rc, pos2);
	}
}
