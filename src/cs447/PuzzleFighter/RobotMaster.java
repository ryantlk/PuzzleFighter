package cs447.PuzzleFighter;

import jig.engine.RenderingContext;
import jig.engine.ResourceFactory;
import jig.engine.Sprite;

public abstract class RobotMaster extends Sprite {
	public RobotMaster(String rsc) {
		super(rsc);
	}

	public abstract void attack();
	public abstract void render(RenderingContext rc, boolean left);
	public abstract void update(long deltaMs);

	protected void setImage(String rsc) {
		frames = ResourceFactory.getFactory().getFrames(rsc);
		width = frames.get(0).getWidth();
		height = frames.get(0).getHeight();
		setFrame(0);
	}
}
