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

public final class Resources {

	public static final int[] SINUS = {
		0,24,49,74,97,120,141,161,
		180,197,212,224,235,244,250,253,
		255,253,250,244,235,224,212,197,
		180,161,141,120,97,74,49,24,
		0,-24,-49,-74,-97,-120,-141,-161,
		-180,-197,-212,-224,-235,-244,-250,-253,
		-255,-253,-250,-244,-235,-224,-212,-197,
		-180,-161,-141,-120,-97,-74,-49,-24
	};
	
	public static final int[] VOLUMES = {
		0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,
		17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,
		33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,
		49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64
	};
	
	
//	public static final double[] VOLUMES = {
//		0.000,0.016,0.031,0.047,0.062,0.078,0.094,0.109,
//		0.125,0.141,0.156,0.172,0.187,0.203,0.219,0.234,
//		0.250,0.266,0.281,0.297,0.312,0.328,0.344,0.359,
//		0.375,0.391,0.406,0.422,0.437,0.453,0.469,0.484,
//		0.500,0.516,0.531,0.547,0.562,0.578,0.594,0.609,
//		0.625,0.641,0.656,0.672,0.687,0.703,0.719,0.734,
//		0.750,0.766,0.781,0.797,0.812,0.828,0.844,0.859,
//		0.875,0.891,0.906,0.922,0.937,0.953,0.969,0.984,
//		1.000
//	};
	
	
	public static final int[] PERIODS = {
		1712,1616,1524,1440,1356,1280,1208,1140,1076,1016,960,907,
		856,808,762,720,678,640,604,570,538,508,480,453,
		428,404,381,360,339,320,302,285,269,254,240,226,
		214,202,190,180,170,160,151,143,135,127,120,113,
		107,101,95,90,85,80,75,71,67,63,60,56,
		53,50,47,45,42,40,37,35,33,31,30,28
	};
	
	public static final int getNearestPeriodIndex(int period) {
		int foundIndex = 0;
		for (int i = 0; i < 71; i++) {
			double a = PERIODS[i];
			double b = PERIODS[i+1];
			if ((period <= a) && (period >= b)) {
				double c = (a - b) / 2;
				c = b + c;
				if (period >= c) {
					foundIndex = i;
					break;
				} else {
					foundIndex = i+1;
					break;
				}
			}
		}
		return foundIndex;
	}

}
