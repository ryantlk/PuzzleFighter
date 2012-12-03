package cs447.PuzzleFighter;

import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.Stack;

import jig.engine.Keyboard;
import jig.engine.RenderingContext;
import jig.engine.ResourceFactory;
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
	private ColoredGem[][] grid;
	private GemPair cursor;

	private long inputTimer = 0;
	private long renderTimer = 0;

	private Color randomColor() {
		return colors[randSrc.nextInt(colors.length)];
	}

	private ColoredGem randomGem(Vector2D pos) {
		if (randSrc.nextFloat() > 0.25) {
			return new PowerGem(this, pos, randomColor());
		}
		else {
			return new CrashGem(this, pos, randomColor());
		}
	}

	public PlayField(int width, int height) {
		this.width = width;
		this.height = height;
		this.grid = new ColoredGem[height][width];
		START_TOP = new Vector2D(width/2, 0);
		START_BOT = START_TOP.translate(DOWN);
		//this.cursor = new GemPair(randomGem(START_BOT), randomGem(START_TOP));
		this.cursor = new GemPair(new PowerGem(this, START_BOT, Color.RED), new PowerGem(this, START_TOP, Color.RED));
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
		final Color[] colors = new Color[] { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW };
		int[][] combinegrid = new int[height][width];
		for (Color c : colors) {
			//System.out.println("===" + c + "===");
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					combinegrid[y][x] = 1;

					Gem g = this.ref(new Vector2D(x, y));
					if (g != null && g instanceof PowerGem && ((PowerGem) g).color == c) {
						combinegrid[y][x] = 0;
					}
					///System.out.print(grid[y][x]);
				}
				//System.out.print("\n");
			}
			while(true){
				Vector2D[] thepoints = maxSubMatrix(combinegrid);

				Vector2D tl = thepoints[0];
				Vector2D br = thepoints[1];

				int zwidth = (int)Math.abs(br.getX()-tl.getX());
				int zheight = (int)Math.abs(br.getY()-tl.getY());
				
				if(c == Color.RED){
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						System.out.print(combinegrid[y][x]);
					}
					System.out.print("\n");
				}
				System.out.println(tl.getX() + " " + tl.getY());
				System.out.println(br.getX() + " " + br.getY());
				System.out.println(zwidth + " " + zheight + "....");
				}

				if(zwidth > 0 && zheight > 0){
					boolean something = false;
					for(int i = (int)tl.getX(); i < (int)tl.getX() + zwidth + 1; i++){
						something = false;
						for(int j = (int)tl.getY(); j < (int)tl.getY() + zheight + 1; j++){
							PowerGem check = (PowerGem)this.ref(new Vector2D(i, j));
							if(check.pos.getX() < (int)tl.getX() || //left side outside bounds
									check.pos.getY() < (int)tl.getY() || //top left corner outside bounds
									check.pos.getY() + check.gemHeight - 1 > (int)br.getY() || //bottom left corner outside bounds
									check.pos.getX() + check.gemWidth - 1 > (int)br.getX()){
								something = true;
								for(int k = (int)check.pos.getX(); k < (int)check.pos.getX() + check.gemWidth; k++){
									for(int l = (int)check.pos.getY(); l < (int)check.pos.getY() + check.gemHeight; l++){
										combinegrid[l][k] = 1;
									}
								}
								break;
							}
						}
						if(something)
							break;
					}
					if(!something){
						PowerGem g = (PowerGem) this.ref(new Vector2D(tl.getX(), tl.getY()));
						g.gemWidth = zwidth + 1;
						g.gemHeight = zheight + 1;
						for(int i = (int)tl.getX(); i < (int)tl.getX() + zwidth + 1; i++){
							for(int j = (int)tl.getY(); j < (int)tl.getY() + zheight + 1; j++){
								//pf.ref(new Vector2D(i, j)).setActivation(false);
								this.set(new Vector2D(i, j), g);
								combinegrid[j][i] = 1;
							}
						}
						this.set(new Vector2D(tl.getX(), tl.getY()), g);
					}
				}else{
					break;
				}
			}

			//System.out.println(tl + "," + br);
		}	
	}
	
	public Vector2D[] maxSubMatrix(int[][] matrix){
		int maxArea = -1, tempArea = -1;
		int x1, y1, x2, y2;
		int n = height;
		int m = width;
		x1 = x2 = y1 = y2 = 0;
		int[] d = new int[m];
		for(int i = 0; i < m; i++){
			d[i] = -1;
		}
		int[] d1 = new int[m];
		int[] d2 = new int[m];
		Stack<Integer> stack = new Stack<Integer>();
		for(int i = 0; i < n; i++){
			for(int j = 0; j < m; j++){
				if(matrix[i][j] == 1){
					d[j] = i;
				}
			}
			stack.clear();
			for(int j = 0; j < m; j++){
				while(stack.size() > 0 && d[stack.peek()] <= d[j]){
					stack.pop();
				}
				d1[j] = (stack.size() == 0) ? -1 : stack.peek();
				stack.push(j);
			}
			stack.clear();
			for(int j = m - 1; j >= 0; j--){
				while(stack.size() > 0 && d[stack.peek()] <= d[j]){
					stack.pop();
				}
				d2[j] = (stack.size() == 0) ? m : stack.peek();
				stack.push(j);
			}
			for(int j = 0; j < m; j++){
				tempArea = (i - d[j]) * (d2[j] - d1[j] - 1);
				if(tempArea > maxArea){
					maxArea = tempArea;
					x1 = d1[j] + 1;
					y1 = d[j] + 1;
					x2 = d2[j] - 1;
					y2 = i;
				}
			}
		}
		Vector2D[] thepoints = new Vector2D[2];
		if(maxArea == 0 && y2 < y1){
			thepoints[0] = new Vector2D(0, 0);
			thepoints[1] = new Vector2D(0, 0);
		}
		else{
			thepoints[0] = new Vector2D(x1, y1);
			thepoints[1] = new Vector2D(x2, y2);
		}
		return thepoints;
	}
	
	public void gravitate() {
		if (fall()) {
			return;
		}

		int crashScore = crashGems();
		if (crashScore != 0) {
			turnScore += crashScore;
			return;
		}

		if (turnScore > 0) {
			ResourceFactory.getJIGLogger().info("Combo'd " + turnScore + " gems");
			for (int i = 0; i < turnScore / 2; i++) {
				grid[i / width][i % width] = new TimerGem(this, new Vector2D(i%width,i/width), Color.RED);
			}
			turnScore = 0;
			return;
		}
		//cursor = new GemPair(randomGem(START_BOT), randomGem(START_TOP));
		this.cursor = new GemPair(new PowerGem(this, START_BOT, Color.RED), new PowerGem(this, START_TOP, Color.RED));
	}
	
	public void update(long deltaMs, Keyboard keyboard) {
		renderTimer += deltaMs;
		inputTimer += deltaMs;

		if (inputTimer > 100) {
			inputTimer = 0;
			if (cursor != null) {
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
		}

		if (cursor != null && renderTimer > 500) {
			renderTimer = 0;
			step();
		}
		if (cursor == null && renderTimer > 100) {
			renderTimer = 0;
			gravitate();
			combine();
		}
	}
}
