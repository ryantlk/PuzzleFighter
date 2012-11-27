package cs447.PuzzleFighter;

import jig.engine.Sprite;

abstract public class Gem extends Sprite {
	abstract public int crash(Color color, boolean initialCrash);

	public Gem(String resource) {
		super(resource);
	}
}