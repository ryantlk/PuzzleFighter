package cs447.PuzzleFighter;

import java.awt.Color;

import jig.engine.PaintableCanvas;
import jig.engine.RenderingContext;
import jig.engine.PaintableCanvas.JIGSHAPE;
import jig.engine.hli.StaticScreenGame;

public class PuzzleFighter extends StaticScreenGame {
	public final static int height = 500;
	public final static int width = 500;

	private PlayField pf;

	public PuzzleFighter() {
		super(width, height, false);
		PaintableCanvas.loadDefaultFrames("redGem", 25, 25, 2, JIGSHAPE.RECTANGLE, Color.RED);
		PaintableCanvas.loadDefaultFrames("greenGem", 25, 25, 2, JIGSHAPE.RECTANGLE, Color.GREEN);
		PaintableCanvas.loadDefaultFrames("blueGem", 25, 25, 2, JIGSHAPE.RECTANGLE, Color.BLUE);
		PaintableCanvas.loadDefaultFrames("yellowGem", 25, 25, 2, JIGSHAPE.RECTANGLE, Color.YELLOW);

		pf = new PlayField(6, 13);
	}

	public void render(RenderingContext rc) {
		super.render(rc);
		pf.render(rc);
	}

	public void update(long deltaMs) {
		pf.update(deltaMs, keyboard);
	}

	public static void main(String[] args) {
		PuzzleFighter game = new PuzzleFighter();
		game.run();
	}
}