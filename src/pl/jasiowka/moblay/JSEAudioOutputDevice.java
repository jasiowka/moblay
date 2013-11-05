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

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.DataLine.Info;

public class JSEAudioOutputDevice {

	private AudioFormat audioFormat;
	private Info info;
	private SourceDataLine dataLine;
	//private int bufferSize;
	
	public JSEAudioOutputDevice(int bufferSize) {
		//this.bufferSize = bufferSize;
		audioFormat = new AudioFormat(44100, 16, 2, true, true);
		info = new Info(SourceDataLine.class, audioFormat);
		try {
			dataLine = (SourceDataLine) AudioSystem.getLine(info);
			dataLine.open(audioFormat, bufferSize);
		} catch (LineUnavailableException e1) {
			//throw new PlayWaveException(e1);
		}
	}

	public void write(byte[] buffer, int offset, int length) {
		dataLine.write(buffer, offset, length);
	}
	
	public void start() {
		dataLine.start();
	}
	
	public void stop() {
		dataLine.drain();
		dataLine.stop();
		dataLine.close();
	}

}
