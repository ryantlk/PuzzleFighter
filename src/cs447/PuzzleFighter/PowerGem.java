package cs447.PuzzleFighter;

import java.awt.geom.AffineTransform;

import jig.engine.RenderingContext;
import jig.engine.util.Vector2D;

public class PowerGem extends ColoredGem {
	int gemWidth;
	int gemHeight;

	public PowerGem(PlayField pf, Vector2D pos, Color color) {
		super(pf, pos, color);
		this.gemWidth = 1;
		this.gemHeight = 1;
	}

	public void render(RenderingContext rc) {
		position = pos.scale(new Vector2D(25, 25));
		AffineTransform t = AffineTransform.getTranslateInstance(position.getX(), position.getY());
		t.scale(gemWidth, gemHeight);
		super.render(rc, t);
	}

	public void crash(Color color) {
		if (this.color != color) {
			return;
		}

		for (int dx = 0; dx < gemWidth; dx++) {
			for (int dy = 0; dy < gemHeight; dy++) {
				pf.clear(pos.translate(new Vector2D(dx, dy)));
			}
		}

		for (int dx = 0; dx < gemWidth; dx++) {
			Gem g = pf.ref(pos.translate(new Vector2D(dx, -1)));
			if (g != null) {
				g.crash(color);
			}

			g = pf.ref(pos.translate(new Vector2D(dx, gemHeight)));
			if (g != null) {
				g.crash(color);
			}
		}

		for (int dy = 0; dy < gemWidth; dy++) {
			Gem g = pf.ref(pos.translate(new Vector2D(-1, dy)));
			if (g != null) {
				g.crash(color);
			}

			g = pf.ref(pos.translate(new Vector2D(gemWidth, dy)));
			if (g != null) {
				g.crash(color);
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
}