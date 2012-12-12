package cs447.PuzzleFighter;

import java.util.Random;
import java.util.Stack;

import jig.engine.RenderingContext;
import jig.engine.util.Vector2D;

public class PlayField {
	public static final Vector2D    UP = new Vector2D( 0, -1);
	public static final Vector2D  DOWN = new Vector2D( 0, +1);
	public static final Vector2D  LEFT = new Vector2D(-1,  0);
	public static final Vector2D RIGHT = new Vector2D(+1,  0);
	public static final Gem WALL = new WallGem();
	private final Vector2D START_TOP;
	private final Vector2D START_BOT;

	private final static Color[] colors = new Color[] { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW };
	private Random randSrc = new Random();

	private int width;
	private int height;
	private int turnScore;
	private int gemCount;
	private ColoredGem[][] grid;
	private GemPair cursor;

	private long inputTimer = 0;
	private long renderTimer = 0;

	public int garbage;

	private RobotMaster fighter;
	private boolean left;

	private Color randomColor() {
		return colors[randSrc.nextInt(colors.length)];
	}

	private ColoredGem randomGem(Vector2D pos) {
		if(gemCount % 25 == 0 && gemCount != 0){
			gemCount++;
			return new Diamond(this, pos, Color.RED);
		}
		if (randSrc.nextFloat() > 0.25) {
			gemCount++;
			return new PowerGem(this, pos, randomColor());
		}
		else {
			gemCount++;
			return new CrashGem(this, pos, randomColor());
		}
	}

	public PlayField(int width, int height, boolean left) {
		this.width = width;
		this.height = height;
		this.grid = new ColoredGem[height][width];
		this.turnScore = 0;
		this.garbage = 0;
		START_TOP = new Vector2D(width/2, 0);
		START_BOT = START_TOP.translate(DOWN);
		this.cursor = new GemPair(randomGem(START_BOT), new PowerGem(this, START_TOP, Color.RED));
		//this.cursor = new GemPair(new Diamond(this, START_BOT, Color.RED), new PowerGem(this, START_TOP, Color.RED));
		this.fighter = left ? new CutMan() : new MegaMan();
		this.left = left;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public void render(RenderingContext rc) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Gem g = grid[y][x];
				if (g != null && (cursor == null || !cursor.contains(g))) {
					g.render(rc);
				}
			}
		}
		if (cursor != null) {
			cursor.render(rc);
		}
		fighter.render(rc, left);
	}

	public Gem ref(Vector2D pos) {
		int x = (int) pos.getX();
		int y = (int) pos.getY();
		if (x < 0 || x >= width || y < 0 || y >= height) {
			return WALL;
		}

		return grid[y][x];
	}

	public void set(Vector2D pos, ColoredGem g) {
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
		if (!cursor.move(DOWN)) {
			cursor = null;
			stepTimers();
		}
	}

	public boolean fall() {
		boolean hadEffect = false;

		for (int y = height-1; y >= 0; y--) {
			for (int x = 0; x < width; x++) {
				if (grid[y][x] != null) {
					hadEffect |= grid[y][x].move(DOWN);
				}
			}
		}

		return hadEffect;
	}

	public int crashGems() {
		int crashScore = 0;

		for (int y = height-1; y >= 0; y--) {
			for (int x = 0; x < width; x++) {
				ColoredGem g = grid[y][x];
				if (g != null) {
					crashScore += g.endTurn();
				}
			}
		}

		return crashScore;
	}

	public void stepTimers() {
		for (int y = height-1; y >= 0; y--) {
			for (int x = 0; x < width; x++) {
				ColoredGem g = grid[y][x];
				if (g instanceof TimerGem) {
					TimerGem tg = (TimerGem) g;
					if (tg.stepTimer()) {
						grid[y][x] = new PowerGem(this, tg.pos, tg.color);
					}
				}
			}
		}
	}
	
	public void combine(){
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				if(grid[y][x] instanceof PowerGem){
					((PowerGem)grid[y][x]).combine();
				}
			}
		}	
	}
	
	public boolean gravitate() {
		if (fall()) {
			return true;
		}

		int crashScore = crashGems();
		if (crashScore != 0) {
			turnScore += crashScore;
			return true;
		}

		return false;
	}
	
	public int update(long deltaMs, boolean down, boolean left, boolean right, boolean ccw, boolean cw) {
		renderTimer += deltaMs;
		inputTimer += deltaMs;

		fighter.update(deltaMs);

		if (inputTimer > 100) {
			inputTimer = 0;
			if (cursor != null) {

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
		}

		if (cursor != null && renderTimer > 500) {
			renderTimer = 0;
			step();
		}
		if (cursor == null && renderTimer > 100) {
			renderTimer = 0;
			boolean moreToDo = gravitate();
			combine();
			if (!moreToDo) {
				if (garbage > 0) {
					garbage /= 2;
					for (int i = 0; i < garbage; i++) {
						grid[i / width][i % width] = new TimerGem(this, new Vector2D(i%width,i/width), Color.RED);
					}
					garbage = 0;
					fighter.attack();
					return 0;
				}
				else {
					cursor = new GemPair(randomGem(START_BOT), randomGem(START_TOP));
					//cursor = new GemPair(new PowerGem(this, START_BOT, Color.RED), new PowerGem(this, START_TOP, Color.RED));
					int tmp = turnScore;
					turnScore = 0;
					return tmp;
				}
			}
		}
		return 0;
	}
}