package cs447.PuzzleFighter;


import jig.engine.RenderingContext;
import jig.engine.ResourceFactory;
import jig.engine.hli.StaticScreenGame;

public class PuzzleFighter extends StaticScreenGame {
	public final static int height = 500;
	public final static int width = 500;

	private PlayField pf;

	final static String RSC_PATH = "cs447/PuzzleFighter/resources/";
	final static String SPRITE_SHEET = RSC_PATH + "gems.png";

	public PuzzleFighter() {
		super(width, height, false);

		ResourceFactory.getFactory().loadResources(RSC_PATH, "resources.xml");

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