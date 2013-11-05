package pl.jasiowka.moblay;
/**
 * 
 * @author Piotr Jasiowka
 * @category Amiga ProTracker MOD files player
 *
 */

public class TrackResampler {

	private SoundContainer baseSound = null;
	
	private int baseSoundSamplePointer = 0;
	
	private int finetune;
	private int volume;
	private int period;
	
	private int lastUsedPeriod;
	private SoundContainer lastUsedSound;
	
	/*
	 * Vars used by resampler
	 */
	private int baseRate;
	/* 44,1 kHz sound will be outputed */
	private int outputRate = 44100;
	private int measure;
    private int lastSample;
	
	public void setSound(SoundContainer sound) {
		lastUsedSound = baseSound; 
		baseSound = sound;
		baseSoundSamplePointer = 0;
		lastSample = 0;//baseSound.getSamples()[baseSoundSamplePointer];
		finetune = baseSound.getFinetune();
		volume = baseSound.getVolume();
		measure = outputRate;
	}
	
	public void retriggerSound() {
		//lastUsedSound = baseSound; 
		//baseSound = sound;
		baseSoundSamplePointer = 0;
		lastSample = 0;//baseSound.getSamples()[baseSoundSamplePointer];
		measure = outputRate;
	}
	
	public void setPeriod(int period) {
		lastUsedPeriod = this.period; 
		this.period = period;
		if (period > 0) {
			baseRate = 7093789 / (period * 2);
			baseRate = (int)(baseRate * Math.pow(1.007246412, finetune));
		}
		else
			baseRate = 0;
		measure = outputRate;
	}
	
	public int getPeriod() { return this.period; }
	public int getPreviousPeriod() { return this.lastUsedPeriod; }
	
	public void setFinetune(int finetune) { this.finetune = finetune; }
	public int getFinetune() { return finetune; }
	public void setVolume(int volume) { this.volume = volume; }
	public int getVolume() { return volume; }
	
	private int _getNextSample() {
		int nextSample = 0;
		if ((!baseSound.isLoopped()) && (baseSoundSamplePointer == baseSound.getLength()))
			nextSample = 0;
		else {
			nextSample = baseSound.getSamples()[baseSoundSamplePointer++];
			if (baseSound.isLoopped() && (baseSoundSamplePointer == baseSound.getLoopEnd()))
				baseSoundSamplePointer = baseSound.getLoopBegin();
			//nextSample = baseSound.getSamples()[baseSoundSamplePointer++];
		}
		return nextSample;
	}
	
	// Simple Bresenham resampling
	public int getResampled() {
		int result = 0;
		if (baseSound != null) {
			if (measure >= baseRate) {
				result = lastSample;
				measure -= baseRate;
			}
			else {
				lastSample = _getNextSample();//sourceSound.getNextSample();
				measure += outputRate;
				result = this.getResampled(); 
			}
		}
        return (result * Resources.VOLUMES[volume]) / 64;
	}
	
	public SoundContainer getLastUsedSound() { return lastUsedSound; }
	
	public void setPosition(int position) {
		position *= 512;
		if (position < baseSound.getLength())
			baseSoundSamplePointer = position;
	}

}
