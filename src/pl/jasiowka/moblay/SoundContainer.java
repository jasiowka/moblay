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

public class SoundContainer {

	private String name;
	private int length;
	private int finetune;
	private int volume;
	private int loopBegin;
	private int loopEnd;
	private boolean loopped;
	private int[] samples;
	//private int samplePointer = 0;
	
	public void setName(String name) { this.name = name; }
	public String getName() { return this.name; }
	
	public void setLength(int length) {
		this.length = length;
		if (length > 0)
			this.samples = new int[length];
		else {
			this.samples = new int[1];
			this.samples[0] = 0;
		}
	}
	
	public int getLength() { return this.length; }
	public void setFinetune(int finetune) { this.finetune = finetune; }
	public int getFinetune() { return this.finetune; }
	public void setVolume(int volume) { this.volume = volume; }
	public int getVolume() { return this.volume; }
	public void setLoopBegin(int loopBegin) { this.loopBegin = loopBegin; }
	public int getLoopBegin() { return this.loopBegin; }
	public void setLoopEnd(int loopEnd) { this.loopEnd = loopEnd; }
	public int getLoopEnd() { return this.loopEnd; }
	public void setLoopped(boolean loopped) { this.loopped = loopped; }
	public boolean isLoopped() { return this.loopped; }
	
	public void setSample(int position, int sample) {
		samples[position] = sample;
	}
	
	public int[] getSamples() {
		return samples;
	}
	
	//public void setSamplePointer(int samplePointer) {
	//	this.samplePointer = samplePointer;
	//}
	
	//public int getSamplePointer() {
	//	return samplePointer;
	//}

}
