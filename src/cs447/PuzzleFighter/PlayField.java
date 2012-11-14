package cs447.PuzzleFighter;

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

	public PlayField(int width, int height) {
		this.width = width;
		this.height = height;
		this.grid = new Gem[height][width];
		this.cursor = new GemPair(width/2, 0, this);
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

	public String toString() {
		StringBuilder sb = new StringBuilder(height * (width + 3));
		int x1 = (int) cursor.pos1.getX();
		int y1 = (int) cursor.pos1.getY();
		int x2 = (int) cursor.pos2.getX();
		int y2 = (int) cursor.pos2.getY();

		for (int y = 0; y < height; y++) {
			sb.append('|');
			for (int x = 0; x < width; x++) {
				if (grid[y][x] != null) {
					sb.append('g');
				}
				else if ( (y == y1 && x == x1) || (y == y2 && x == x2) ) {
					sb.append('G');
				}
				else {
					sb.append(' ');
				}
			}
			sb.append('|');
			sb.append('\n');
		}
		return sb.toString();
	}
}
