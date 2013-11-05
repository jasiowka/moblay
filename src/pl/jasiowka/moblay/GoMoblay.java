package pl.jasiowka.moblay;
/**
 * 
 * @author Piotr Jasiowka
 * @category Amiga ProTracker MOD files player
 *
 */

public class GoMoblay {

	public static void main(String[] args) {
		Parser p = new Parser();
		ModuleContainer m = p.loadFromFile("./mods/polly.mod");
		Player player = new Player();
		player.play(m);
	}

}
