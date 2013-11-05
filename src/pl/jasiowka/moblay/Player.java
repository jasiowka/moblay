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

import java.nio.ByteBuffer;

public class Player {

	private ModuleContainer module;
	private PatternContainer pattern;
	private EffectProcessor[] fXProc;
	private TrackResampler[] track;	
	
	private int currentPatternPosition = 0;
	private int lineNumber = 0;
	
	private int newPatternPosition;
	private int newLineNumber;
	private boolean jumpToAnotherPattern;
	
	private int speed;
	
	public Player() {
		track = new TrackResampler[4];
		fXProc = new EffectProcessor[4];
		for (int i = 0; i < 4; i++) {
			track[i] = new TrackResampler();
			fXProc[i] = new EffectProcessor(this, track[i]);
		}
	}
	
	//--------------------------------------------------------------------------------------------
	
	public void play(ModuleContainer module) {
		
		this.module = module;
		speed = 6;
		
		currentPatternPosition = 0;
		int allPatternPositions = module.getAllPatternsCount();
		
		
		int SIZ = 131072; 
		ByteBuffer outputBuffer = ByteBuffer.allocate(SIZ);
		JSEAudioOutputDevice audioOutputDevice = new JSEAudioOutputDevice(SIZ);
		audioOutputDevice.start();
		
		LowPassFilter lowPassFilterL = new LowPassFilter();
		LowPassFilter lowPassFilterR = new LowPassFilter();
		
		//
		// Single pattern processing
		//
		for (currentPatternPosition = 0; currentPatternPosition < allPatternPositions; currentPatternPosition++) {
			
			if (jumpToAnotherPattern) {
				currentPatternPosition = newPatternPosition;
				jumpToAnotherPattern = false;
			}
			else
				newLineNumber = 0;
			
			pattern = module.getPatternAtPos(currentPatternPosition);
			
			//
			// Single line processing
			// (Each pattern has always 64 lines)
			//
			for (lineNumber = newLineNumber; lineNumber < 64; lineNumber++) {
				
				//
				//
				//
				for (int trackNumber = 0; trackNumber < 4; trackNumber++) {
				//int trackNumber = 0;
					int period = pattern.getPeriod(lineNumber, trackNumber);
					int soundNumber = pattern.getSoundNumber(lineNumber, trackNumber);
					int fXCode = pattern.getFXCode(lineNumber, trackNumber);
					fXProc[trackNumber].setProcessor(period, soundNumber, fXCode);
				}
				
				//
				// Single 'tick' performance.
				// Single 'tick' = 0.25 s = 882 samples @ 44100 Hz
				// speed = 'ticks' per single line
				//
				for (int tickCounter = 0; tickCounter < speed; tickCounter++) {
					
					//
					// Effects processing within a single line
					// for all tracks
					//
					for (int trackNumber = 0; trackNumber < 4; trackNumber++)
						fXProc[trackNumber].process(tickCounter);
					
					
					for (int sampleCounter = 0; sampleCounter < 882; sampleCounter++) {
//						int leftChannel = 32*(track[0].getResampled() + track[3].getResampled());
//						int rightChannel = 32*(track[1].getResampled() + track[2].getResampled());
						//int leftChannel = 64*track[0].getResampled();
						//int rightChannel = leftChannel;
						
						int leftChannel = track[0].getResampled() + track[3].getResampled();
						int rightChannel = track[1].getResampled() + track[2].getResampled();
						
						// Low-pass filtering
						leftChannel = lowPassFilterL.getFiltered(leftChannel);
						rightChannel = lowPassFilterR.getFiltered(rightChannel);
						
						// Channel panning
						int tmp = leftChannel;
						leftChannel = (40 * leftChannel) + (24 * rightChannel);
						rightChannel = (40 * rightChannel) + (24 * tmp);
						
						outputBuffer.putShort((short)leftChannel);
						outputBuffer.putShort((short)rightChannel);
						if (!outputBuffer.hasRemaining()) {
							audioOutputDevice.write(outputBuffer.array(), 0, SIZ);
							outputBuffer.clear();
						}
					}
					
				} // End of single 'tick' performance
				
				// If (jumpToAnotherPattern == true), skip all remaining lines to escape
				// current pattern and start the new one, we want to jump to
				if (jumpToAnotherPattern) lineNumber = 63;
						
			} // End of single line processing
			
			// If patternJump and playing the last pattern,
			// change the patternAtPos...?...
			if (jumpToAnotherPattern) currentPatternPosition = 0;
		
		} // End of single pattern processing
		
		// Drain outputBuffer if it's not empty...
		if (outputBuffer.hasRemaining())
			audioOutputDevice.write(outputBuffer.array(), 0, outputBuffer.position());
		// ... and stop outputDevice
		audioOutputDevice.stop();
		
	
	}
	
	//--------------------------------------------------------------------------------------------
	
	public void setSpeed(int speed) { this.speed = speed; }
	public int getSpeed() { return this.speed; }
	public ModuleContainer getModule() { return this.module; }
	public void jumpToPattern(int pos, int line) {
		jumpToAnotherPattern = true;
		newPatternPosition = pos;
		newLineNumber = line;
	}
	public int getCurrentPatternPosition() { return currentPatternPosition; }

}
