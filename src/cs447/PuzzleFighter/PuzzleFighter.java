package cs447.PuzzleFighter;

public class PuzzleFighter {

	/**
	 * @param args Ignored
	 */
	public static void main(String[] args) {
		PlayField pf = new PlayField(6, 13);
		System.out.println("--------");
		for (int i = 0; i < 32; i++) {
			if (i == 8) {
				pf.move(PlayField.LEFT);
			}
			pf.step();
			System.out.print(pf);
			System.out.println("--------");
		}
	}
}