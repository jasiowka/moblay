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
