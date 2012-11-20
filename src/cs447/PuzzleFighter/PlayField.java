package cs447.PuzzleFighter;

import java.awt.event.KeyEvent;

import jig.engine.Keyboard;
import jig.engine.RenderingContext;
import jig.engine.util.Vector2D;

public class PlayField {
	public static final Vector2D    UP = new Vector2D( 0, -1);
	public static final Vector2D  DOWN = new Vector2D( 0, +1);
	public static final Vector2D  LEFT = new Vector2D(-1,  0);
	public static final Vector2D RIGHT = new Vector2D(+1,  0);
	public static final Gem WALL = new Gem(null, null, GemType.WALL);

	private int width;
	private int height;
	private Gem[][] grid;
	private GemPair cursor;

	private long inputTimer = 0;
	private long renderTimer = 0;

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
					g.render(rc);
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

	public void set(Vector2D pos, Gem g) {
		int x = (int) pos.getX();
		int y = (int) pos.getY();

		grid[y][x] = g;
	}

	public void clear(Vector2D pos) {
		set(pos, null);
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

		for (int y = height-1; y >= 0; y--) {
			for (int x = 0; x < width; x++) {
				if (grid[y][x] != null) {
					Gem g = grid[y][x];
					while (g.move(DOWN))
						;
				}
			}
		}
		cursor = new GemPair(width/2, 0, this);
	}

	public void update(long deltaMs, Keyboard keyboard) {
		renderTimer += deltaMs;
		inputTimer += deltaMs;

		if (inputTimer > 100) {
			inputTimer = 0;
			boolean ccw = keyboard.isPressed(KeyEvent.VK_Q);
			boolean cw = keyboard.isPressed(KeyEvent.VK_E);
			boolean left = keyboard.isPressed(KeyEvent.VK_A);
			boolean right = keyboard.isPressed(KeyEvent.VK_D);
			boolean down = keyboard.isPressed(KeyEvent.VK_S);

			if (ccw && !cw) {
				cursor.rotateCounterClockwise();
			}
			if (cw && !ccw) {
				cursor.rotateClockwise();
			}
			if (down) {
				move(PlayField.DOWN);
			}
			if (left && !right) {
				move(PlayField.LEFT);
			}
			if (right && !left) {
				move(PlayField.RIGHT);
			}

		}

		if (renderTimer > 500) {
			renderTimer = 0;
			step();
		}
	}
}
