package cs447.PuzzleFighter;

import jig.engine.RenderingContext;
import jig.engine.Sprite;
import jig.engine.util.Vector2D;

public class Gem extends Sprite {
	public GemType type;

	public Gem(GemType type) {
		super("redGem");
		this.type = type;
	}

	public void render(RenderingContext rc, Vector2D pos) {
		position = pos.scale(new Vector2D(25, 25));
		super.render(rc);
	}
}