package cs447.PuzzleFighter;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import jig.engine.RenderingContext;
import jig.engine.util.Vector2D;

public class PowerGem extends ColoredGem {
	int gemWidth;
	int gemHeight;

	public PowerGem(PlayField pf, Vector2D pos, Color color, int gemWidth, int gemHeight) {
		super(pf, pos, color, "Power");
		this.gemWidth = gemWidth;
		this.gemHeight = gemHeight;
	}

	public PowerGem(PlayField pf, Vector2D pos, Color color) {
		this(pf, pos, color, 1, 1);
	}

	public void render(RenderingContext rc) {
		position = pos.scale(scale);
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
	
	public void combine(){
		int posx = (int)this.pos.getX();
		int posy = (int)this.pos.getY();
		Color c = this.getColor();
		if(this.gemHeight == 1 && this.gemWidth == 1){
			Gem right = pf.ref(new Vector2D(posx + 1, posy));
			Gem bottom = pf.ref(new Vector2D(posx, posy + 1));
			Gem botright = pf.ref(new Vector2D(posx + 1, posy + 1));
			//check that neighbors are all size 1posx1 and color is the same
			if(right != null && bottom != null && botright != null &&
					right instanceof PowerGem && 
					bottom instanceof PowerGem &&
					botright instanceof PowerGem &&
					((PowerGem)right).getColor() == c &&
					((PowerGem)bottom).getColor() == c &&
					((PowerGem)botright).getColor() == c &&
					((PowerGem)right).gemWidth == 1 &&
					((PowerGem)right).gemHeight == 1 &&
					((PowerGem)bottom).gemWidth == 1 &&
					((PowerGem)bottom).gemHeight == 1 &&
					((PowerGem)botright).gemWidth == 1 &&
					((PowerGem)botright).gemHeight == 1){
				this.gemHeight = this.gemWidth = 2;
				pf.set(new Vector2D(posx + 1, posy), this);
				pf.set(new Vector2D(posx, posy + 1), this);
				pf.set(new Vector2D(posx + 1, posy + 1), this);
			}
		}else{
			Gem left = null;
			Gem right = null;
			Gem top = null;
			Gem bottom = null;
			boolean merge;
			//top
			ArrayList<Gem> toplist = new ArrayList();
			merge = true;
			for(int x = posx; x < posx + this.gemWidth; x++){
				toplist.add(pf.ref(new Vector2D(x, posy - 1)));
				if(x == posx){
					left = pf.ref(new Vector2D(x, posy - 1));
				}else if(x == posx + this.gemWidth - 1){
					right = pf.ref(new Vector2D(x, posy - 1));
				}
			}
			for(Gem g : toplist){
				if(g == null || !(g instanceof PowerGem) || 
						((PowerGem)g).pos.getY() != ((PowerGem)left).pos.getY() ||
						((PowerGem)g).getColor() != c){
					merge = false;
					break;
				}
			}
			if(merge && (int)((PowerGem)left).pos.getX() == posx && 
					(int)((PowerGem)right).pos.getX() + ((PowerGem)right).gemWidth - 1 == posx + this.gemWidth - 1){
				this.position = ((PowerGem)left).position;
				this.pos = ((PowerGem)left).pos;
				this.gemHeight = this.gemHeight + ((PowerGem)left).gemHeight;
				for(int x = (int)this.pos.getX(); x < (int)this.pos.getX() + this.gemWidth; x++){
					for(int y = (int)this.pos.getY(); y < (int)this.pos.getY() + this.gemHeight; y++){
						pf.set(new Vector2D(x, y), this);
					}
				}
			}
			
			//bottom
			ArrayList<Gem> bottomlist = new ArrayList();
			merge = true;
			for(int x = posx; x < posx + this.gemWidth; x++){
				bottomlist.add(pf.ref(new Vector2D(x, posy + this.gemHeight)));
				if(x == posx){
					left = pf.ref(new Vector2D(x, posy + this.gemHeight));
				}else if(x == posx + this.gemWidth - 1){
					right = pf.ref(new Vector2D(x, posy + this.gemHeight));
				}
			}
			for(Gem g : bottomlist){
				if(g == null || !(g instanceof PowerGem) || 
						(int)((PowerGem)g).pos.getY() + ((PowerGem)g).gemHeight != (int)((PowerGem)left).pos.getY() + ((PowerGem)left).gemHeight ||
						((PowerGem)g).getColor() != c){
					merge = false;
					break;
				}
			}
			if(merge && (int)((PowerGem)left).pos.getX() == posx && 
					(int)((PowerGem)right).pos.getX() + ((PowerGem)right).gemWidth - 1 == posx + this.gemWidth - 1){
				this.gemHeight = this.gemHeight + ((PowerGem)left).gemHeight;
				for(int x = (int)this.pos.getX(); x < (int)this.pos.getX() + this.gemWidth; x++){
					for(int y = (int)this.pos.getY(); y < (int)this.pos.getY() + this.gemHeight; y++){
						pf.set(new Vector2D(x, y), this);
					}
				}
			}
			
			//right
			ArrayList<Gem> rightlist = new ArrayList();
			merge = true;
			for(int y = posy; y < posy + this.gemHeight; y++){
				rightlist.add(pf.ref(new Vector2D(posx + this.gemWidth, y)));
				if(y == posy){
					top = pf.ref(new Vector2D(posx + this.gemWidth, y));
				}else if(y == posy + this.gemHeight - 1){
					bottom = pf.ref(new Vector2D(posx + this.gemWidth, y));
				}
			}
			for(Gem g : rightlist){
				if(g == null || !(g instanceof PowerGem) || 
						(int)((PowerGem)g).pos.getX() + ((PowerGem)g).gemWidth != (int)((PowerGem)top).pos.getX() + ((PowerGem)top).gemWidth ||
						((PowerGem)g).getColor() != c){
					merge = false;
					break;
				}
			}
			if(merge && (int)((PowerGem)top).pos.getY() == posy && 
					(int)((PowerGem)bottom).pos.getY() + ((PowerGem)bottom).gemHeight - 1 == posy + this.gemHeight - 1){
				this.gemWidth = this.gemWidth + ((PowerGem)top).gemWidth;
				for(int x = (int)this.pos.getX(); x < (int)this.pos.getX() + this.gemWidth; x++){
					for(int y = (int)this.pos.getY(); y < (int)this.pos.getY() + this.gemHeight; y++){
						pf.set(new Vector2D(x, y), this);
					}
				}
			}
			
			
			//left
			ArrayList<Gem> leftlist = new ArrayList();
			merge = true;
			for(int y = posy; y < posy + this.gemHeight; y++){
				leftlist.add(pf.ref(new Vector2D(posx - 1, y)));
				if(y == posy){
					top = pf.ref(new Vector2D(posx - 1, y));
				}else if(y == posy + this.gemHeight - 1){
					bottom = pf.ref(new Vector2D(posx - 1, y));
				}
			}
			for(Gem g : leftlist){
				if(g == null || !(g instanceof PowerGem) || 
						(int)((PowerGem)g).pos.getX() != (int)((PowerGem)top).pos.getX() ||
						((PowerGem)g).getColor() != c){
					merge = false;
					break;
				}
			}
			if(merge && (int)((PowerGem)top).pos.getY() == posy && 
					(int)((PowerGem)bottom).pos.getY() + ((PowerGem)bottom).gemHeight - 1 == posy + this.gemHeight - 1){
				this.position = ((PowerGem)top).position;
				this.pos = ((PowerGem)top).pos;
				this.gemWidth = this.gemWidth + ((PowerGem)top).gemWidth;
				for(int x = (int)this.pos.getX(); x < (int)this.pos.getX() + this.gemWidth; x++){
					for(int y = (int)this.pos.getY(); y < (int)this.pos.getY() + this.gemHeight; y++){
						pf.set(new Vector2D(x, y), this);
					}
				}
			}
		}
	}

	public int endTurn() {
		return 0;
	}
}
