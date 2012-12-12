package cs447.PuzzleFighter;

public class WallGem extends Gem {
	public WallGem() {
		super(PuzzleFighter.GEM_SHEET + "#redPower");
	}

	public int crash(Color color, boolean initialCrash) {
		return 0;
	}
}