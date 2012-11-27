package cs447.PuzzleFighter;

import jig.engine.util.Vector2D;

public class CrashGem extends ColoredGem {

	public CrashGem(PlayField pf, Vector2D pos, Color color) {
		super(pf, pos, color, "Crash");
	}

	public int crash(Color color) {
		if (this.color != color) {
			return 0;
		}

		int crashCount = 1;

		pf.clear(pos);

		Gem g;
		g = pf.ref(pos.translate(PlayField.UP));
		if (g != null) {
			crashCount += g.crash(color);
		}

		g = pf.ref(pos.translate(PlayField.DOWN));
		if (g != null) {
			crashCount += g.crash(color);
		}

		g = pf.ref(pos.translate(PlayField.LEFT));
		if (g != null) {
			crashCount += g.crash(color);
		}

		g = pf.ref(pos.translate(PlayField.RIGHT));
		if (g != null) {
			crashCount += g.crash(color);
		}

		return crashCount;
	}

	public boolean endTurn() {
		int crashCount = crash(color);
		if (crashCount == 1) {
			pf.set(pos, this);
			return false;
		}
		return true;
	}

	protected static String resource(Color color) {
		switch (color) {
		case RED:
			return PuzzleFighter.SPRITE_SHEET + "#redCrash";
		case GREEN:
			return PuzzleFighter.SPRITE_SHEET + "#greenCrash";
		case BLUE:
			return PuzzleFighter.SPRITE_SHEET + "#blueCrash";
		case YELLOW:
			return PuzzleFighter.SPRITE_SHEET + "#yellowCrash";
		default:
			return PuzzleFighter.SPRITE_SHEET + "#redCrash";
		}
	}
}