package cs447.PuzzleFighter;

import java.awt.Color;
import java.awt.event.KeyEvent;

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
		PaintableCanvas.loadDefaultFrames("redGem", 25, 25, 1, JIGSHAPE.RECTANGLE, Color.RED);
		PaintableCanvas.loadDefaultFrames("greenGem", 25, 25, 1, JIGSHAPE.RECTANGLE, Color.GREEN);
		PaintableCanvas.loadDefaultFrames("blueGem", 25, 25, 1, JIGSHAPE.RECTANGLE, Color.BLUE);
		PaintableCanvas.loadDefaultFrames("yellowGem", 25, 25, 1, JIGSHAPE.RECTANGLE, Color.YELLOW);

		pf = new PlayField(6, 13);
		/* System.out.println("--------");
		for (int i = 0; i < 32; i++) {
			if (i == 8) {
				pf.move(PlayField.LEFT);
			}
			pf.step();
			System.out.print(pf);
			System.out.println("--------");
		} */
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