/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs447.PuzzleFighter;

import jig.engine.util.Vector2D;

/**
 *
 * @author ryantlk
 */
public class Diamond extends ColoredGem{

	public Diamond(PlayField pf, Vector2D pos, Color color){
		super(pf, pos, Color.RED, "Diamond");
	}
	
	@Override
	public int endTurn() {
		int crashCount = 0;
		pf.clear(pos);
		Gem g;
		g = pf.ref(pos.translate(PlayField.DOWN));
		if(!(g instanceof WallGem)){
			Color c = ((ColoredGem)g).getColor();
			System.out.println(g.toString());
			for(int x = 0; x < pf.getWidth(); x++){
				for(int y = 0; y < pf.getHeight(); y++){
					ColoredGem h = (ColoredGem)pf.ref(new Vector2D(x, y));
					if(h != null && h.getColor() == c){
						crashCount += h.crash(c, false);
					}
				}
			}
		}else{
			return 5;
		}
		return crashCount;
	}

	@Override
	public int crash(Color color, boolean initialCrash) {
		return 0;
	}
}
