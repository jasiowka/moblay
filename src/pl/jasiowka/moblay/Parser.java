package pl.jasiowka.moblay;
/**
 * 
 * @author Piotr Jasiowka
 * @category Amiga ProTracker MOD files player
 *
 */

import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class Parser {

	private static final String[] MOD_TAGS
		= {"M.K.", "M!K!", "FLT4", "4CHN"};
	
	private ModuleContainer module;
	
	private int soundsCount;
	
	private int differentPatternsCount;
	
	private String readText(DataInputStream input, int length)
	throws IOException {
		StringBuffer buffer = new StringBuffer("");
		for (int i = 0; i < length; i++) {
			char c = (char)input.readByte();
			if (c != 0)
				buffer.append(c);
		}
		return buffer.toString();
	}
	
	private void readModuleTag(DataInputStream input) throws IOException {
		input.mark(1084);
		input.skip(1080);
		module.setTag(readText(input, 4));
		input.reset();
		soundsCount = 15;
		for (int i = 0; i < MOD_TAGS.length; i++)
			if (module.getTag().equals(MOD_TAGS[i])) {
				soundsCount = 31;
				break;
			}
	}
	
	private void readSoundsHeaders(DataInputStream input) throws IOException {
		for (int i = 0; i < soundsCount; i++) {
			SoundContainer sound = new SoundContainer();
			sound.setName(readText(input, 22));
			sound.setLength(input.readUnsignedShort() * 2);
			int finetune = input.readUnsignedByte() & 0x0f;
			if (finetune > 7)
				finetune -= 16;
			sound.setFinetune(finetune);
			sound.setVolume(input.readUnsignedByte());
			int loopBegin = input.readUnsignedShort() * 2;
			int loopLength = input.readUnsignedShort() * 2;
			
			//fix loop begin
			if ((loopLength > 2) && (loopBegin+loopLength > sound.getLength())
					 && (loopBegin/2+loopLength <= sound.getLength()))
					{
						loopBegin /= 2;
					}
			sound.setLoopBegin(loopBegin);
			System.out.print("Sound: "+loopBegin+"___");
			
			int loopEnd = loopBegin + loopLength;
			sound.setLoopEnd(loopEnd);
			sound.setLoopped(loopLength > 2);
			module.addSound(sound);
			System.out.println("Sound: "+sound.getLength());
		}
	}
	
	private void readModuleHeader(DataInputStream input) throws IOException {
		//read module length (in patterns)
		module.setAllPatternsCount(input.readUnsignedByte());
		//skip one byte (probably not needed/important)
		input.skipBytes(1);
		//read patterns order data
		for (int i = 0; i < 128; i++)
			module.setPatternsPlaylist(i, input.readUnsignedByte());
		//skip module tag if it was read previously
		if (soundsCount == 31)
			input.skipBytes(4);
		//
		differentPatternsCount = 0;
        for (int i = 0; i < module.getAllPatternsCount(); i++)
            if (module.getPatternsPlaylist(i) > differentPatternsCount)
                differentPatternsCount = module.getPatternsPlaylist(i);
        differentPatternsCount++;
        module.setDifferentPatternsCount(differentPatternsCount);
	}
	
	private void readPatternsData(DataInputStream input) throws IOException {
		for (int patternNumber = 0; patternNumber < differentPatternsCount; patternNumber++) {
			PatternContainer pattern = new PatternContainer();
			for (int lineNumber = 0; lineNumber < 64; lineNumber++) {
				for (int trackNumber = 0; trackNumber < 4; trackNumber++) {
					int byte0 = input.readUnsignedByte();
					int byte1 = input.readUnsignedByte();
					int byte2 = input.readUnsignedByte();
					int byte3 = input.readUnsignedByte();
					// if there is not sound in line, soundNumber = -1
					int soundNumber = ((byte0 & 0xf0) + ((byte2 & 0xf0) >> 4)) - 1;
					pattern.setSoundNumber(lineNumber, trackNumber, soundNumber);
					int period = ((byte0 & 0x0f) << 8) + byte1;
					pattern.setPeriod(lineNumber, trackNumber, period);
					pattern.setFXCode(lineNumber, trackNumber, ((byte2 & 0x0f) << 8) + byte3);
					System.out.print(period+"    ");
				}System.out.println("");
			}
			module.addPattern(pattern);
		}
	}
	
	private void readSoundsData(DataInputStream input) throws IOException {
		for (int soundIndex = 0; soundIndex < soundsCount; soundIndex++) {
			int soundLength = module.getSound(soundIndex).getLength();
			if (soundLength > 0)
				for (int i = 0; i < soundLength; i++)
					module.getSound(soundIndex).setSample(i, input.readByte() /*  *64  */);
		}
	}
	
	public ModuleContainer loadFromFile(String filename) {
		
		try {
			FileInputStream fis = new FileInputStream(filename);
			BufferedInputStream bis = new BufferedInputStream(fis);
			DataInputStream dis = new DataInputStream(bis);
			
			module = new ModuleContainer();
			
			/* read module tag */
			readModuleTag(dis);
			System.out.println("Module tag: "+soundsCount);
			
			/* read module title */
			module.setTitle(readText(dis, 20));
			
			/* read samples headers */
			readSoundsHeaders(dis);
			
			readModuleHeader(dis);
			
	        /* read patterns data */
			readPatternsData(dis);
			
			/* read samples data */
			readSoundsData(dis);
			
			/* close file input stream */
			dis.close();
			
			
			System.out.println("sound1 length: "+module.getSound(0).getLength());
			System.out.println("sound1 loop begin: "+module.getSound(0).getLoopBegin());
			System.out.println("sound1 loop end: "+module.getSound(0).getLoopEnd());
			
			return module;
			
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
