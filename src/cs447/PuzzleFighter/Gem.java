package cs447.PuzzleFighter;

import java.awt.geom.AffineTransform;

import jig.engine.RenderingContext;
import jig.engine.Sprite;
import jig.engine.util.Vector2D;

public class Gem extends Sprite {
	private PlayField pf;

	public GemType type;
	public Color color;
	public Vector2D pos;

	public int gemWidth;
	public int gemHeight;

	public Gem(PlayField pf, Vector2D pos, GemType type) {
		this(pf, pos, type, "redGem");
	}

	public Gem(PlayField pf, Vector2D pos, GemType type, String resource) {
		super(resource);
		this.pf = pf;
		this.pos = pos;
		this.type = type;
		this.color = gemColor(resource);
		gemWidth = 1;
		gemHeight = 1;
	}

	public void render(RenderingContext rc) {
		position = pos.scale(new Vector2D(25, 25));
		AffineTransform t = AffineTransform.getTranslateInstance(position.getX(), position.getY());
		t.scale(gemWidth, gemHeight);
		super.render(rc, t);
	}

	public void crash() {
		for (int dx = 0; dx < gemWidth; dx++) {
			for (int dy = 0; dy < gemHeight; dy++) {
				pf.clear(pos.translate(new Vector2D(dx, dy)));
			}
		}

		for (int dx = 0; dx < gemWidth; dx++) {
			Gem g = pf.ref(pos.translate(new Vector2D(dx, -1)));
			if (g != null && g.type != GemType.WALL && g.color == color) {
				g.crash();
			}

			g = pf.ref(pos.translate(new Vector2D(dx, gemHeight)));
			if (g != null && g.type != GemType.WALL && g.color == color) {
				g.crash();
			}
		}

		for (int dy = 0; dy < gemWidth; dy++) {
			Gem g = pf.ref(pos.translate(new Vector2D(-1, dy)));
			if (g != null && g.type != GemType.WALL && g.color == color) {
				g.crash();
			}

			g = pf.ref(pos.translate(new Vector2D(gemWidth, dy)));
			if (g != null && g.type != GemType.WALL && g.color == color) {
				g.crash();
			}
		}
	}

	public boolean move(Vector2D dv) {
		Vector2D newPos = pos.translate(dv);
		// TODO: Think about fixing for horizontal movement?
		for (int dx = 0; dx < gemWidth; dx++) {
			if (pf.isFilled(newPos.translate(new Vector2D(dx, gemHeight - 1)))) {
				return false;
			}
		}

		for (int dy = 0; dy < gemHeight; dy++) {
			for (int dx = 0; dx < gemWidth; dx++) {
				pf.clear(pos.translate(new Vector2D(dx, dy)));
			}
		}

		for (int dy = 0; dy < gemHeight; dy++) {
			for (int dx = 0; dx < gemWidth; dx++) {
				pf.set(newPos.translate(new Vector2D(dx, dy)), this);
			}
		}

		pos = newPos;
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

	private Color gemColor(String rscName) {
		if (rscName.equals("redGem")) {
			return Color.RED;
		}
		else if (rscName.equals("greenGem")) {
			return Color.GREEN;
		}
		else {
			return null;
		}
	}
}