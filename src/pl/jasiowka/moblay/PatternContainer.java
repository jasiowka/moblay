package pl.jasiowka.moblay;
/**
 * 
 * @author Piotr Jasiowka
 * @category Amiga ProTracker MOD files player
 *
 */

public class PatternContainer {

	private int[][][] data = new int[64][4][3];
	
	public void setSoundNumber(int lineNumber, int trackNumber, int soundNumber) {
		data[lineNumber][trackNumber][0] = soundNumber;
	}
	
	public int getSoundNumber(int lineNumber, int trackNumber) {
		return data[lineNumber][trackNumber][0];
	}
	
	public void setPeriod(int lineNumber, int trackNumber, int periodIndex) {
		data[lineNumber][trackNumber][1] = periodIndex;
	}
	
	public int getPeriod(int lineNumber, int trackNumber) {
		return data[lineNumber][trackNumber][1];
	}
	
	public void setFXCode(int lineNumber, int trackNumber, int fXCommand) {
		data[lineNumber][trackNumber][2] = fXCommand;
	}
	
	public int getFXCode(int lineNumber, int trackNumber) {
		return data[lineNumber][trackNumber][2];
	}

}
