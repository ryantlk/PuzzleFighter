package cs447.PuzzleFighter;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import jig.engine.util.Vector2D;

public class RemotePlayfield extends PlayField {
	private ObjectInputStream ois;

	public RemotePlayfield(int width, int height, Socket socket) throws IOException {
		super(width, height, null, true);

		InputStream is = socket.getInputStream();
		ois = new ObjectInputStream(is);

		this.fighter = !secondary ? new CutMan() : new MegaMan();
		this.cursor = null;
	}

	public int update(long deltaMs, boolean down, boolean left, boolean right, boolean ccw, boolean cw) {
		updateCount++;
		fighter.update(deltaMs);
		try {
			if(ois.available() > 0){
				ois.readInt();
				Packet pack = null;
				try {
					pack = (Packet)ois.readObject();
				} catch (IOException ex) {
					Logger.getLogger(PlayField.class.getName()).log(Level.SEVERE, null, ex);
				} catch (ClassNotFoundException ex) {
					Logger.getLogger(PlayField.class.getName()).log(Level.SEVERE, null, ex);
				}
				for(int i = 0; i < height; i++){
					for(int j = 0; j < width; j++){
						grid[i][j] = null;
					}
				}
				if(pack.attacking){
					fighter.attack();
				}
				for(int i = 0; i < height; i++){
					for(int j = 0; j < width; j++){
						if(pack.grid[i][j] != null){
							if(pack.grid[i][j].type.contentEquals("Crash")){
								grid[i][j] = new CrashGem(this, new Vector2D(j, i), pack.grid[i][j].color);
							}else if(pack.grid[i][j].type.contentEquals("Diamond")){
								grid[i][j] = new Diamond(this, new Vector2D(j, i), Color.RED);
							}else if(pack.grid[i][j].type.contentEquals("Power")){
								grid[i][j] = new PowerGem(this, new Vector2D(j, i), pack.grid[i][j].color, pack.grid[i][j].width, pack.grid[i][j].height);
							}else{
								grid[i][j] = new TimerGem(this, new Vector2D(j, i), pack.grid[i][j].color);
								((TimerGem)grid[i][j]).setFrame(pack.grid[i][j].frame);
							}
						}
					}
				}
				return pack.garbage;
			}
		} catch (IOException ex) {
			Logger.getLogger(PlayField.class.getName()).log(Level.SEVERE, null, ex);
		}
		return 0;
	}
}
