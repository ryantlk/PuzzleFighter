package cs447.PuzzleFighter;

import jig.engine.RenderingContext;
import jig.engine.util.Vector2D;

abstract public class ColoredGem extends Gem {
	public static final Vector2D scale = new Vector2D(32, 32);

	protected PlayField pf;
	Color color;
	Vector2D pos;

	abstract public int endTurn();

	public ColoredGem(PlayField pf, Vector2D pos, Color color, String type) {
		super(resource(color, type));
		this.pf = pf;
		this.color = color;
		this.pos = pos;
	}

	public Color getColor() {
		return color;
	}

	public void render(RenderingContext rc) {
		position = pos.scale(scale);
		super.render(rc);
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

	private static String resource(Color color, String type) {
		switch (color) {
		case RED:
			return PuzzleFighter.GEM_SHEET + "#red" + type;
		case GREEN:
			return PuzzleFighter.GEM_SHEET + "#green" + type;
		case BLUE:
			return PuzzleFighter.GEM_SHEET + "#blue" + type;
		case YELLOW:
			return PuzzleFighter.GEM_SHEET + "#yellow" + type;
		default:
			return PuzzleFighter.GEM_SHEET + "#red" + type;
		}
	}
}