package cs447.PuzzleFighter;

import jig.engine.util.Vector2D;

public class CrashGem extends ColoredGem {

	public CrashGem(PlayField pf, Vector2D pos, Color color) {
		super(pf, pos, color, "Crash");
	}

	public int crash(Color color, boolean initialCrash) {
		if (this.color != color) {
			return 0;
		}

		int crashCount = 1;

		pf.clear(pos);

		Gem g;
		g = pf.ref(pos.translate(PlayField.UP));
		if (g != null) {
			crashCount += g.crash(color, false);
		}

		g = pf.ref(pos.translate(PlayField.DOWN));
		if (g != null) {
			crashCount += g.crash(color, false);
		}

		g = pf.ref(pos.translate(PlayField.LEFT));
		if (g != null) {
			crashCount += g.crash(color, false);
		}

		g = pf.ref(pos.translate(PlayField.RIGHT));
		if (g != null) {
			crashCount += g.crash(color, false);
		}

		return crashCount;
	}

	public int endTurn() {
		int crashCount = 0;

		pf.clear(pos);

		Gem g;
		g = pf.ref(pos.translate(PlayField.UP));
		if (g != null) {
			crashCount += g.crash(color, true);
		}

		g = pf.ref(pos.translate(PlayField.DOWN));
		if (g != null) {
			crashCount += g.crash(color, true);
		}

		g = pf.ref(pos.translate(PlayField.LEFT));
		if (g != null) {
			crashCount += g.crash(color, true);
		}

		g = pf.ref(pos.translate(PlayField.RIGHT));
		if (g != null) {
			crashCount += g.crash(color, true);
		}

		if (crashCount == 0) {
			pf.set(pos, this);
			return crashCount;
		}
		else {
			crashCount += crash(color, false);
			return crashCount;
		}
	}

	protected static String resource(Color color) {
		switch (color) {
		case RED:
			return PuzzleFighter.GEM_SHEET + "#redCrash";
		case GREEN:
			return PuzzleFighter.GEM_SHEET + "#greenCrash";
		case BLUE:
			return PuzzleFighter.GEM_SHEET + "#blueCrash";
		case YELLOW:
			return PuzzleFighter.GEM_SHEET + "#yellowCrash";
		default:
			return PuzzleFighter.GEM_SHEET + "#redCrash";
		}
	}
}