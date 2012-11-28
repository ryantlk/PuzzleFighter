package cs447.PuzzleFighter;

import java.awt.geom.AffineTransform;
import java.util.Stack;

import jig.engine.RenderingContext;
import jig.engine.util.Vector2D;

public class PowerGem extends ColoredGem {
	int gemWidth;
	int gemHeight;

	public PowerGem(PlayField pf, Vector2D pos, Color color) {
		super(pf, pos, color, "Power");
		this.gemWidth = 1;
		this.gemHeight = 1;
	}

	public void render(RenderingContext rc) {
		position = pos.scale(new Vector2D(32, 32));
		AffineTransform t = AffineTransform.getTranslateInstance(position.getX(), position.getY());
		t.scale(gemWidth, gemHeight);
		super.render(rc, t);
	}

	public int crash(Color color, boolean initialCrash) {
		if (this.color != color) {
			return 0;
		}

		int crashCount = gemWidth * gemHeight;

		for (int dx = 0; dx < gemWidth; dx++) {
			for (int dy = 0; dy < gemHeight; dy++) {
				pf.clear(pos.translate(new Vector2D(dx, dy)));
			}
		}

		for (int dx = 0; dx < gemWidth; dx++) {
			Gem g = pf.ref(pos.translate(new Vector2D(dx, -1)));
			if (g != null) {
				crashCount += g.crash(color, false);
			}

			g = pf.ref(pos.translate(new Vector2D(dx, gemHeight)));
			if (g != null) {
				crashCount += g.crash(color, false);
			}
		}

		for (int dy = 0; dy < gemWidth; dy++) {
			Gem g = pf.ref(pos.translate(new Vector2D(-1, dy)));
			if (g != null) {
				crashCount += g.crash(color, false);
			}

			g = pf.ref(pos.translate(new Vector2D(gemWidth, dy)));
			if (g != null) {
				crashCount += g.crash(color, false);
			}
		}

		return crashCount;
	}

	public boolean move(Vector2D dv) {
		Vector2D newPos = pos.translate(dv);
		// TODO: Think about fixing for horizontal movement?
		for (int dx = 0; dx < gemWidth; dx++) {
			if (pf.isFilled(newPos.translate(new Vector2D(dx, gemHeight - 1)))) {
				return false;
			}
		}

		for (int dy = 0; dy < gemHeight; dy++) {
			for (int dx = 0; dx < gemWidth; dx++) {
				pf.clear(pos.translate(new Vector2D(dx, dy)));
			}
		}

		for (int dy = 0; dy < gemHeight; dy++) {
			for (int dx = 0; dx < gemWidth; dx++) {
				pf.set(newPos.translate(new Vector2D(dx, dy)), this);
			}
		}

		pos = newPos;
		return true;
	}

	public int endTurn() {
		combine();
		return 0;
	}
	
	public void combine(){
		int[][] green = new int[pf.getHeight()][pf.getWidth()];
		int[][] red = new int[pf.getHeight()][pf.getWidth()];
		int[][] blue = new int[pf.getHeight()][pf.getWidth()];
		int[][] yellow = new int[pf.getHeight()][pf.getWidth()];
		
		for(int i = 0; i < pf.getHeight(); i++){
			for(int j = 0; j < pf.getWidth(); j++){
				green[i][j] = 1;
			}
		}
		for(int i = 0; i < pf.getHeight(); i++){
			for(int j = 0; j < pf.getWidth(); j++){
				red[i][j] = 1;
			}
		}
		for(int i = 0; i < pf.getHeight(); i++){
			for(int j = 0; j < pf.getWidth(); j++){
				blue[i][j] = 1;
			}
		}
		for(int i = 0; i < pf.getHeight(); i++){
			for(int j = 0; j < pf.getWidth(); j++){
				yellow[i][j] = 1;
			}
		}
		
		//create arrays for each of the gem colors which are used
		//to find 
		for(int i = 0; i < pf.getHeight(); i++){
			for(int j = 0; j < pf.getWidth(); j++){
				ColoredGem g = (ColoredGem)pf.ref(new Vector2D(j, i));
				if(g != null){
					Color c = g.getColor();
					switch(c){
						case GREEN:
							green[i][j] = 0;
							break;
						case RED:
							red[i][j] = 0;
							break;
						case BLUE:
							blue[i][j] = 0;
							break;
						case YELLOW:
							yellow[i][j] = 0;
							break;
						default:
							System.out.println("Switch fail in combine().");
					}
				}
			}
		}
		Vector2D[] thepoints;
		thepoints = maxSubMatrix(green);
		thepoints = maxSubMatrix(red);
	}
	
	public Vector2D[] maxSubMatrix(int[][] matrix){
		int maxArea = -1, tempArea = -1;
		int x1, y1, x2, y2;
		int n = pf.getHeight();
		int m = pf.getWidth();
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
		thepoints[0] = new Vector2D(x1, y1);
		thepoints[1] = new Vector2D(x2, y2);
		return thepoints;
	}
}