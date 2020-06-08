/**
 *
 * @authors: Erick Akins & Daniel Ramirez
 *
 **/
package myremoteproj;

import java.io.File; 
import java.io.IOException; 
import java.util.Scanner; 

import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException; 

public class SimpleAudioPlayer 
{        
	// to store current position 
	Long currentFrame; 
	Clip clip; 
    int value = 0;
	
	// current status of clip 
	String status; 
	
	AudioInputStream audioInputStream; 
	static String filePath = "/home/pi/piano2.wav"; 

	// constructor to initialize streams and clip 
	public SimpleAudioPlayer() 
		throws UnsupportedAudioFileException, 
		IOException, LineUnavailableException 
	{ 

	} 

	public void alarm() 
	{ 
		try
		{ 			
            // New line path to play correct audio tone on Pi
            filePath = "/home/pi/piano2.wav";
                        
            // Tests to see if file exists
            File tmpDir = new File(filePath);
            boolean exists = tmpDir.exists();
            System.out.println(exists); 
            System.out.println(AudioSystem.getMixerInfo());
                    
            // Construct new SimpleAudioPlayer object
            SimpleAudioPlayer audioPlayer = new SimpleAudioPlayer(); 
			
			// Retrieve audio tone file and play in a loop
            audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile()); 
            clip = AudioSystem.getClip();
            clip.open(audioInputStream); 
            clip.loop(Clip.LOOP_CONTINUOUSLY);         
			audioPlayer.play(); 

			// If user presses pushbutton, stop audio
            if (value == 1)
                audioPlayer.stop();
		} 		
		catch (Exception ex) 
		{ 
			System.out.println("Error with playing sound."); 
			ex.printStackTrace(); 
		
		} 
	} 
	
	// Work as the user enters his choice 
	private void gotoChoice(int c) 
			throws IOException, LineUnavailableException, UnsupportedAudioFileException 
	{ 
		switch (c) 
		{ 
			case 1: 
				pause(); 
				break; 
			case 2: 
				resumeAudio(); 
				break; 
			case 3: 
				restart(); 
				break; 
			case 4: 
				stop(); 
				break; 
			case 5: 
				System.out.println("Enter time (" + 0 + 
				", " + clip.getMicrosecondLength() + ")"); 
				Scanner sc = new Scanner(System.in); 
				long c1 = sc.nextLong(); 
				jump(c1); 
				break; 
	
		} 
	
	} 
	
	// Method to play the audio 
	public void play() 
	{ 
		//start the clip 
		clip.start(); 
		
		status = "play"; 
	} 
	
	// Method to pause the audio 
	public void pause() 
	{ 
		if (status.equals("paused")) 
		{ 
			System.out.println("audio is already paused"); 
			return; 
		} 
		this.currentFrame = 
		this.clip.getMicrosecondPosition(); 
		clip.stop(); 
		status = "paused"; 
	} 
	
	// Method to resume the audio 
	public void resumeAudio() throws UnsupportedAudioFileException, 
								IOException, LineUnavailableException 
	{ 
		if (status.equals("play")) 
		{ 
			System.out.println("Audio is already "+ 
			"being played"); 
			return; 
		} 
		clip.close(); 
		resetAudioStream(); 
		clip.setMicrosecondPosition(currentFrame); 
		this.play(); 
	} 
	
	// Method to restart the audio 
	public void restart() throws IOException, LineUnavailableException, 
											UnsupportedAudioFileException 
	{ 
		clip.stop(); 
		clip.close(); 
		resetAudioStream(); 
		currentFrame = 0L; 
		clip.setMicrosecondPosition(0); 
		this.play(); 
	} 
	
	// Method to stop the audio 
	public void stop() throws UnsupportedAudioFileException, 
	IOException, LineUnavailableException 
	{ 
		currentFrame = 0L; 
		clip.stop(); 
		clip.close(); 
	} 
	
	// Method to jump over a specific part 
	public void jump(long c) throws UnsupportedAudioFileException, IOException, 
														LineUnavailableException 
	{ 
		if (c > 0 && c < clip.getMicrosecondLength()) 
		{ 
			clip.stop(); 
			clip.close(); 
			resetAudioStream(); 
			currentFrame = c; 
			clip.setMicrosecondPosition(c); 
			this.play(); 
		} 
	} 
	
	// Method to reset audio stream 
	public void resetAudioStream() throws UnsupportedAudioFileException, IOException, 
											LineUnavailableException 
	{ 
		audioInputStream = AudioSystem.getAudioInputStream( 
		new File(filePath).getAbsoluteFile()); 
		clip.open(audioInputStream); 
		clip.loop(Clip.LOOP_CONTINUOUSLY); 
	} 

} 
