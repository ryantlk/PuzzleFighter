package cs447.PuzzleFighter;

import jig.engine.RenderingContext;
import jig.engine.util.Vector2D;

public class PlayField {
	public static final Vector2D    UP = new Vector2D( 0, -1);
	public static final Vector2D  DOWN = new Vector2D( 0, +1);
	public static final Vector2D  LEFT = new Vector2D(-1,  0);
	public static final Vector2D RIGHT = new Vector2D(+1,  0);
	public static final Gem WALL = new Gem(GemType.WALL);

	private int width;
	private int height;
	private Gem[][] grid;
	private GemPair cursor;

	private long timer;

	public PlayField(int width, int height) {
		this.width = width;
		this.height = height;
		this.grid = new Gem[height][width];
		this.cursor = new GemPair(width/2, 0, this);
	}

	public void render(RenderingContext rc) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Gem g = grid[y][x];
				if (g != null) {
					g.render(rc, new Vector2D(x, y));
				}
			}
		}
		cursor.render(rc);
	}

	public Gem ref(Vector2D pos) {
		int x = (int) pos.getX();
		int y = (int) pos.getY();
		if (x < 0 || x >= width || y < 0 || y >= height) {
			return WALL; 
		}

		return grid[y][x];
	}

	public void addGem(Vector2D pos, Gem g) {
		int x = (int) pos.getX();
		int y = (int) pos.getY();

		grid[y][x] = g;
	}

	public boolean isFilled(Vector2D pos) {
		return (ref(pos) != null);
	}

	public boolean move(Vector2D dv) {
		return cursor.move(dv);
	}

	public void step() {
		if (cursor.move(DOWN)) {
			return;
		}

		// TODO: gravity stuff
		addGem(cursor.pos1, cursor.gem1);
		addGem(cursor.pos2, cursor.gem2);
		cursor = new GemPair(width/2, 0, this);
	}

	public void update(long deltaMs) {
		timer += deltaMs;
		if (timer > 500) {
			timer = 0;
			step();
		}
	}
}
