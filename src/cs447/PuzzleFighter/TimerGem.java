package cs447.PuzzleFighter;

import jig.engine.util.Vector2D;

public class TimerGem extends ColoredGem {
	int frame;

	public TimerGem(PlayField pf, Vector2D pos, Color color) {
		super(pf, pos, color, "Timer");
		setFrame(0);
		frame = 0;
	}

	public int crash(Color color, boolean initialCrash) {
		if (initialCrash) {
			return 0;
		}

		pf.clear(pos);
		return 1;
	}

	public int endTurn() {
		return 0;
	}

	public boolean stepTimer() {
		frame += 1;

		if (frame < 5) {
			setFrame(frame);
			return false;
		}

		frame = 0;
		return true;
	}
}