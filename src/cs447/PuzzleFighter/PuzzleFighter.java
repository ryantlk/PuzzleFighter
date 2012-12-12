package cs447.PuzzleFighter;


import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

import jig.engine.RenderingContext;
import jig.engine.ResourceFactory;
import jig.engine.hli.StaticScreenGame;

public class PuzzleFighter extends StaticScreenGame {
	public final static int height = 416;
	public final static int width = 700;

	private PlayField pfLeft;
	private PlayField pfRight;

	final static String RSC_PATH = "cs447/PuzzleFighter/resources/";
	final static String GEM_SHEET = RSC_PATH + "gems.png";
	final static String CUT_SHEET = RSC_PATH + "cutman.png";
	final static String MEGA_SHEET = RSC_PATH + "megaman.png";

	public PuzzleFighter() {
		super(width, height, false);

		ResourceFactory.getFactory().loadResources(RSC_PATH, "resources.xml");

		pfLeft = new PlayField(6, 13, true);
		pfRight = new PlayField(6, 13, false);

	}

	public void render(RenderingContext rc) {
		super.render(rc);
		pfLeft.render(rc);
		rc.setTransform(AffineTransform.getTranslateInstance(508, 0));
		pfRight.render(rc);
	}

	public void update(long deltaMs) {
		boolean down1 = keyboard.isPressed(KeyEvent.VK_S);
		boolean left1 = keyboard.isPressed(KeyEvent.VK_A);
		boolean right1 = keyboard.isPressed(KeyEvent.VK_D);
		boolean ccw1 = keyboard.isPressed(KeyEvent.VK_Q);
		boolean cw1 = keyboard.isPressed(KeyEvent.VK_E);
		pfRight.garbage += pfLeft.update(deltaMs, down1, left1, right1, ccw1, cw1);

		boolean down2 = keyboard.isPressed(KeyEvent.VK_K);
		boolean left2 = keyboard.isPressed(KeyEvent.VK_J);
		boolean right2 = keyboard.isPressed(KeyEvent.VK_L);
		boolean ccw2 = keyboard.isPressed(KeyEvent.VK_U);
		boolean cw2 = keyboard.isPressed(KeyEvent.VK_O);
		pfLeft.garbage += pfRight.update(deltaMs, down2, left2, right2, ccw2, cw2);
	}

	public static void main(String[] args) {
		PuzzleFighter game = new PuzzleFighter();
		game.run();
	}
}