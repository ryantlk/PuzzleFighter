package cs447.PuzzleFighter;

import java.awt.geom.AffineTransform;
import java.util.Stack;

import jig.engine.RenderingContext;
import jig.engine.util.Vector2D;

public class PowerGem extends ColoredGem {
	int gemWidth;
	int gemHeight;

	public PowerGem(PlayField pf, Vector2D pos, Color color) {
		super(pf, pos, color, "Power");
		this.gemWidth = 1;
		this.gemHeight = 1;
	}

	public void render(RenderingContext rc) {
		position = pos.scale(scale);
		AffineTransform t = AffineTransform.getTranslateInstance(position.getX(), position.getY());
		t.scale(gemWidth, gemHeight);
		super.render(rc, t);
	}

	public int crash(Color color, boolean initialCrash) {
		if (this.color != color) {
			return 0;
		}

		int crashCount = gemWidth * gemHeight;

		for (int dx = 0; dx < gemWidth; dx++) {
			for (int dy = 0; dy < gemHeight; dy++) {
				pf.clear(pos.translate(new Vector2D(dx, dy)));
			}
		}

		for (int dx = 0; dx < gemWidth; dx++) {
			Gem g = pf.ref(pos.translate(new Vector2D(dx, -1)));
			if (g != null) {
				crashCount += g.crash(color, false);
			}

			g = pf.ref(pos.translate(new Vector2D(dx, gemHeight)));
			if (g != null) {
				crashCount += g.crash(color, false);
			}
		}

		for (int dy = 0; dy < gemWidth; dy++) {
			Gem g = pf.ref(pos.translate(new Vector2D(-1, dy)));
			if (g != null) {
				crashCount += g.crash(color, false);
			}

			g = pf.ref(pos.translate(new Vector2D(gemWidth, dy)));
			if (g != null) {
				crashCount += g.crash(color, false);
			}
		}

		return crashCount;
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

	public int endTurn() {
		return 0;
	}
}
