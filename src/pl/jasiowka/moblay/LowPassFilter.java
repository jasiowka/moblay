package pl.jasiowka.moblay;
/**
 * 
 * @author Piotr Jasiowka
 * @category Amiga ProTracker MOD files player
 *
 */

public class LowPassFilter {
	
	private static final int BUFFER_SIZE = 5;
	int[] buffer = new int[BUFFER_SIZE];
	private boolean bufferIsFull;
	private int bufferPos = 0;

	public int getFiltered(int sample) {
		
		int res = 0;
		
		if (!bufferIsFull) {
			buffer[bufferPos++] = sample; 
			if (bufferPos == BUFFER_SIZE)
				for (int i = 0; i < BUFFER_SIZE; i++)
					res += buffer[i];
				res = res / BUFFER_SIZE;
				bufferPos = 0;
				bufferIsFull = true;
		}
		else {
			buffer[bufferPos] = sample;
			for (int i = 0; i < BUFFER_SIZE; i++)
				res += buffer[i];
			res = res / BUFFER_SIZE;
			bufferPos = (bufferPos + 1) % BUFFER_SIZE;
		}
		return res;
	}

}
