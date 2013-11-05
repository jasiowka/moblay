package pl.jasiowka.moblay;
/**
 * 
 * @author Piotr Jasiowka
 * @category Amiga ProTracker MOD files player
 *
 */

public final class EffectProcessor {

	TrackResampler track;
	Player player;
	
	private int lineReadPeriod;
	private SoundContainer lineReadSound;
	
	private int command;
	private int argx;
	private int argy;
	private int argxy;
	private int previousArgxy;
	
	private int arpStoredPeriod;
	private int arpStoredPeriodIndex;
	private int arpPeriodIndex;
	private boolean portPeriodRestore;int li = 0;
	
	private int volSlideStep;
	private int volSlide;
	
	private int portPeriod;
	
	private int vibrato;
	private int vibAmp;
	private int vibSpeed;
	private int vibPosition = 0;
	private int vibStoredPeriod;
	//private boolean continueVib;
	private boolean vibPeriodRestore;
	//private boolean vibRestart;
	
	private int vibVolStep;
	private int vibVolVolume;
	
	private int tonePortFromPeriod;
	private int tonePortToPeriod;
	private int tonePortStep;
	private boolean continuePort;
	private boolean portRestart;
	
	private int tonePortPeriod;
	private int tonePortVolStep;
	private int tonePortVolVolume;
	
//	private int period;
//	private int finetune;
//	private int volume;
//	
//	private int basePeriod;
//	private int baseFinetune;
//	private int baseVolume;
//	
//	private int basePeriodIndex;
//	
//	private Module module;
//	
	public EffectProcessor(Player player, TrackResampler track) {
		this.player = player;
		this.track = track;
	}
	
	public void process(int tickCounter) {
		switch (command) {
			//
			// Arpeggio
			//
			case 0x00:
				if (argxy > 0) {
					if (tickCounter == player.getSpeed() - 1)
						portPeriodRestore = true;
					switch (tickCounter % 3) {
						case 0: arpPeriodIndex = arpStoredPeriodIndex; break;
						case 1: arpPeriodIndex = arpStoredPeriodIndex + argx; break;
						case 2: arpPeriodIndex = arpStoredPeriodIndex + argy; break;
					}
					if (arpPeriodIndex > 71)
						arpPeriodIndex = 71;
					track.setPeriod(Resources.PERIODS[arpPeriodIndex]);
				}
				break;
			//
			// Slide up (portamento up)
			//
			case 0x01:
				if (argxy > 0) {
					track.setPeriod(portPeriod);
					portPeriod = track.getPeriod() - argxy;
					if (portPeriod < 28)
						portPeriod = 28;
				}
				break;
			//
			// Slide down (portamento down)
			//
			case 0x02:
				if (argxy > 0) {
					track.setPeriod(portPeriod);
					portPeriod = track.getPeriod() + argxy;
					if (portPeriod > 1815)
						portPeriod = 1815;
				}
				break;
			//
			// Tone portamento + volume slide
			//
			case 0x05:
				tonePortVolVolume = track.getVolume() + tonePortVolStep;
				if (tonePortVolVolume > 64)
					tonePortVolVolume = 64;
				if (tonePortVolVolume < 0)
					tonePortVolVolume = 0;
				track.setVolume(tonePortVolVolume);
			//
			// Tone portamento
			//
			case 0x03:
				tonePortPeriod = track.getPeriod() + tonePortStep;
				li++;
				if (((tonePortStep > 0) && (tonePortPeriod > tonePortToPeriod)) ||
					((tonePortStep < 0) && (tonePortPeriod < tonePortToPeriod)))
					tonePortPeriod = tonePortToPeriod;
				if (tonePortStep != 0) {
					track.setPeriod(tonePortPeriod);
					System.out.println(li+"  "+tonePortStep);
				}
				break;
			//
			// Vibrato + volume slide
			//
			case 0x06:
				vibVolVolume = track.getVolume() + vibVolStep;
				if (vibVolVolume > 64)
					vibVolVolume = 64;
				if (vibVolVolume < 0)
					vibVolVolume = 0;
				track.setVolume(vibVolVolume);
			//
			// Vibrato
			//
			case 0x04:
				if (tickCounter == player.getSpeed() - 1)
					vibPeriodRestore = true;
				vibrato = (vibAmp * Resources.SINUS[vibPosition]) / 128;
				vibPosition = (vibPosition + vibSpeed) % 64;
				track.setPeriod(vibStoredPeriod + vibrato);
				break;
			//
			// Tremolo
			//
			case 0x07:
				break;
			//
			// NOT USED COMMAND
			//
			case 0x08:
				break;
			//
			// Set sample offset
			//
			case 0x09:
				// Look into prepareForEffect() method
				break;
			//
			// Volume slide
			//
			case 0x0a:
				if (argxy > 0) {
					volSlide = track.getVolume() + volSlideStep;
					if (volSlide > 64)
						volSlide = 64;
					else if (volSlide < 0)
						volSlide = 0;
					track.setVolume(volSlide);
				}
				break;
			//
			// Position jump
			//
			case 0x0b:
				if (argxy < player.getModule().getAllPatternsCount())
					player.jumpToPattern(argxy, 0);
				break;
			//
			// Set volume
			//
			case 0x0c:
				// Look into prepareForEffect() method
				break;
			//
			// Pattern break
			//
			case 0x0d:
				int newPatternPosition = player.getCurrentPatternPosition() + 1;
				int newLineNumber = (argx * 10) + argy;
				if ((newPatternPosition < player.getModule().getAllPatternsCount())
					&& (newLineNumber < 64))
					player.jumpToPattern(newPatternPosition, newLineNumber);
				break;
			//
			// Set speed
			//
			case 0x0f:
				// Look into prepareForEffect() method
				break;
			//
			// (Extended) Set filter
			//
			case 0xe0:
				break;
			//
			// (Extended) Fineslide up
			//
			case 0xe1:
				break;
			//
			// (Extended) Fineslide down
			//
			case 0xe2:
				break;
			//
			// (Extended) Glissando control
			//
			case 0xe3:
				break;
			//
			// (Extended) Set vibrato waveform
			//
			case 0xe4:
				break;
			//
			// (Extended) Set finetune
			//
			case 0xe5:
				break;
			//
			// (Extended) Pattern loop
			//
			case 0xe6:
				break;
			//
			// (Extended) Set tremolo waveform
			//
			case 0xe7:
				break;
			//
			// NOT USED COMMAND
			//
			case 0xe8:
				break;
			//
			// (Extended) Retrigger note
			//
			case 0xe9:
				break;
			//
			// (Extended) Fine volume slide up
			//
			case 0xea:
				break;
			//
			// (Extended) Fine volume slide down
			//
			case 0xeb:
				break;
			//
			// (Extended) Cut note
			//
			case 0xec:
				break;
			//
			// (Extended) Note delay
			//
			case 0xed:
				break;
			//
			// (Extended) Pattern delay
			//
			case 0xee:
				break;
			//
			// (Extended) Invert loop
			//
			case 0xef:
				break;
			//
			//
			//
			default:
//				track.setBasePeriod(basePeriod);
//				track.setBaseFinetune(baseFinetune);
//				track.setBaseVolume(baseVolume);
		}
		
	}
	
	public void setProcessor(int period, int soundNumber, int fXCode) {
		// Cleaning up after some effects
		if (portPeriodRestore) {
			track.setPeriod(arpStoredPeriod);
			portPeriodRestore = false;
		}
		if (vibPeriodRestore) {
			track.setPeriod(vibStoredPeriod);
			vibPeriodRestore = false;
		}
		lineReadPeriod = period;
		lineReadSound = null;
		if (soundNumber > -1)
			lineReadSound = player.getModule().getSound(soundNumber);
		command = fXCode >> 8;
		if (command == 0x0e)
			command = 0xe0 + ((fXCode >> 4) & 0x0f);
		previousArgxy = argxy;
		argxy = fXCode & 0xff;
		argx = (argxy >> 4) & 0x0f;
		argy = argxy & 0x0f;
		this.prepareForEffect();
	}
	
	private void prepareForEffect() {
		switch (command) {
			//
			// Arpeggio
			//
			case 0x00:
				if (argxy > 0) {
					this.standardLineConfiguration();
					if (lineReadPeriod > 0)
						arpStoredPeriod = lineReadPeriod;
					else
						arpStoredPeriod = track.getPeriod();
					
					arpStoredPeriodIndex = Resources.getNearestPeriodIndex(arpStoredPeriod);
					portPeriodRestore = false;
				}
				else {
					// Line without effect command
					this.standardLineConfiguration();
				}
				break;
			//
			// Slide up (portamento up)
			//
			case 0x01:
				this.standardLineConfiguration();
				portPeriod = track.getPeriod();
				break;
			//
			// Slide down (portamento down)
			//
			case 0x02:
				this.standardLineConfiguration();
				portPeriod = track.getPeriod();
				break;
			//
			// Tone portamento + volume slide
			//
			case 0x05:
				if (((argx > 0) && (argy > 0)) ||
				((argx == 0) && (argy == 0)))
					tonePortVolStep = 0;
				else if (argx > 0)
					tonePortVolStep = argx;
				else if (argy > 0)
					tonePortVolStep = -argy;
			//
			// Tone portamento
			//
			case 0x03:
				// TODO Implement effect e3 switch
				this.portLineConfiguration();
				continuePort = !((argxy > 0) && (argxy != previousArgxy));
				if ((!continuePort && (command != 0x05)) || (portRestart)) {
					portRestart = false;
					tonePortFromPeriod = track.getPreviousPeriod();
					tonePortToPeriod = track.getPeriod();
					if (tonePortToPeriod == tonePortFromPeriod)
						tonePortStep = 0;
					else if (tonePortToPeriod > tonePortFromPeriod)
						tonePortStep = argxy;
					else if (tonePortToPeriod < tonePortFromPeriod)
						tonePortStep = -argxy;
					tonePortPeriod = tonePortFromPeriod - tonePortStep;
					track.setPeriod(tonePortPeriod);
				}
				break;
			//
			// Vibrato + volume slide
			//
			case 0x06:
				if (((argx > 0) && (argy > 0)) ||
				((argx == 0) && (argy == 0)))
					vibVolStep = 0;
				else if (argx > 0)
					vibVolStep = argx;
				else if (argy > 0)
					vibVolStep = -argy;
			//
			// Vibrato
			//
			case 0x04:
				this.standardLineConfiguration();
				//if ((command != 0x06) || (vibRestart)) {
					//vibRestart = false;
					vibStoredPeriod = track.getPeriod();
					if (argx > 0) vibSpeed = argx;
					if (argy > 0) vibAmp = argy;
					vibPeriodRestore = false;
				//}
				break;
			//
			// Set sample offset
			//
			case 0x09:
				this.standardLineConfiguration();
				track.setPosition(argxy);
				break;
			//
			// Volume slide
			//
			case 0x0a:
				this.standardLineConfiguration();
				if ((argx > 0) && (argy > 0))
					volSlideStep = 0;
				else if (argx > 0)
					volSlideStep = argx;
				else if (argy > 0)
					volSlideStep = -argy;
				break;
			//
			// Position jump
			//
			case 0x0b:
				this.standardLineConfiguration();
				break;
			//
			// Set volume
			//
			case 0x0c:
				this.standardLineConfiguration();
				if (argxy > 64)
					argxy = 64;
				track.setVolume(argxy);
				break;
			//
			// Pattern break
			//
			case 0x0d:
				this.standardLineConfiguration();
				break;
			//
			// Set speed
			//
			case 0x0f:
				this.standardLineConfiguration();
				if (argxy > 0) {
					if (argxy < 32)
						player.setSpeed(argxy);
					else {
						// TODO if argxy > 31 then 'beats per minute' configuration must be used
					}
				}
				break;
			//
			// Cut sample
			//
			case 0xec:
				//TODO all stuff
				this.standardLineConfiguration();
				break;
		}
	}
	
	public void standardLineConfiguration() {
		if (lineReadPeriod > 0) {
			if (lineReadSound != null) {
				track.setSound(lineReadSound);
				track.setPeriod(lineReadPeriod);
			}
			else {
				track.retriggerSound();
				track.setPeriod(lineReadPeriod);
			}
		}
		else {
			if (lineReadSound != null) {
				track.setSound(lineReadSound);
			}
		}
	}
	
	public void portLineConfiguration() {
		if (lineReadPeriod > 0) {
			if (lineReadSound != null) {
				track.setSound(lineReadSound);
				track.setPeriod(lineReadPeriod);
				portRestart = true;
			}
			else {
				track.retriggerSound();
				// SOMETHING WRONG HERE !! check polly.mod, pos 18
				//if (!continuePort)
					track.setPeriod(lineReadPeriod);
			}
		}
		else {
			if (lineReadSound != null) {
				track.setSound(lineReadSound);
				portRestart = true;
			}
		}
	}

}