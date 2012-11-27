package cs447.PuzzleFighter;

import jig.engine.RenderingContext;
import jig.engine.util.Vector2D;

public class ColoredGem extends Gem {
	protected PlayField pf;
	Color color;
	Vector2D pos;

	public ColoredGem(PlayField pf, Vector2D pos, Color color) {
		super(colorResource(color));
		this.pf = pf;
		this.color = color;
		this.pos = pos;
	}

	public void render(RenderingContext rc) {
		position = pos.scale(new Vector2D(25, 25));
		super.render(rc);
	}

	public void crash(Color color) {
		if (this.color != color) {
			return;
		}

		pf.clear(pos);

		Gem g;
		g = pf.ref(pos.translate(PlayField.UP));
		if (g != null) {
			g.crash(color);
		}

		g = pf.ref(pos.translate(PlayField.DOWN));
		if (g != null) {
			g.crash(color);
		}

		g = pf.ref(pos.translate(PlayField.LEFT));
		if (g != null) {
			g.crash(color);
		}

		g = pf.ref(pos.translate(PlayField.RIGHT));
		if (g != null) {
			g.crash(color);
		}
	}

	public boolean move(Vector2D dv) {
		Vector2D newPos = pos.translate(dv);
		if (pf.isFilled(newPos)) {
			return false;
		}
		pf.clear(pos);
		pf.set(newPos, this);
		this.pos = newPos;
		return true;
	}

	public boolean rotateClockwise(Vector2D about) {
		Vector2D newPos = pos.translate(about.scale(-1));
		newPos = new Vector2D(-newPos.getY(), newPos.getX());
		newPos = newPos.translate(about);
		if (pf.isFilled(newPos)) {
			return false;
		}
		pf.clear(pos);
		pf.set(newPos, this);
		pos = newPos;
		return true;
	}

	public boolean rotateCounterClockwise(Vector2D about) {
		Vector2D newPos = pos.translate(about.scale(-1));
		newPos = new Vector2D(newPos.getY(), -newPos.getX());
		newPos = newPos.translate(about);
		if (pf.isFilled(newPos)) {
			return false;
		}
		pf.clear(pos);
		pf.set(newPos, this);
		pos = newPos;
		return true;
	}

	private static String colorResource(Color color) {
		switch (color) {
		case RED:
			return "redGem";
		case GREEN:
			return "greenGem";
		case YELLOW:
		case BLUE:
		default:
			return "redGem";
		}
	}
}