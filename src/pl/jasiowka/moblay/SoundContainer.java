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
