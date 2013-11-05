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

import java.util.ArrayList;
import java.util.List;

public class ModuleContainer {

	private String title;
	private String tag;
//	private int speed = 6;
	private int allPatternsCount;
	private int differentPatternsCount;
	private int[] patternsPlaylist = new int[128];
	private List<SoundContainer> sounds = new ArrayList<SoundContainer>();
	private List<PatternContainer> patterns = new ArrayList<PatternContainer>();
//	private int patternPointer = 0;
	
	public void setTitle(String title) { this.title = title; }
	public String getTitle() { return this.title; }
	public void setAllPatternsCount(int allPatternsCount) { this.allPatternsCount = allPatternsCount; }
	public int getAllPatternsCount() { return this.allPatternsCount; }
	public void setDifferentPatternsCount(int differentPatternsCount) { this.differentPatternsCount = differentPatternsCount; }
	public int getDifferentPatternsCount() { return this.differentPatternsCount; }
	
	public void setPatternsPlaylist(int position, int patternNumber) {
		patternsPlaylist[position] = patternNumber;
	}
	
	public int getPatternsPlaylist(int position) {
		return patternsPlaylist[position];
	}
	
	public void setTag(String tag) { this.tag = tag; }
	public String getTag() { return this.tag; }
	public void addSound(SoundContainer sound) { this.sounds.add(sound); }
	
	public SoundContainer getSound(int soundIndex) {
		return this.sounds.get(soundIndex);
	}
	
	public void addPattern(PatternContainer pattern) { this.patterns.add(pattern); }
	
//	public void setPatternPointer(int patternPointer) { this.patternPointer = patternPointer; }
//	public int getPatternPointer() { return this.patternPointer; }
//	
	public PatternContainer getPatternAtPos(int pos) {
		int patternNumber = patternsPlaylist[pos];
		return this.patterns.get(patternNumber);
	}

}
