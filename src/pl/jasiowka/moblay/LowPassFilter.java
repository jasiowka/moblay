/*
 * Copyright (C) 2013 by Piotr Jasiowka. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
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
