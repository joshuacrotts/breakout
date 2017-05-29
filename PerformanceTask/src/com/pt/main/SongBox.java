package com.pt.main;

import java.util.ArrayList;
import java.util.List;

import com.joshuacrotts.standards.StandardAudio;

/**One thing I must mention: these songs all come from a game that I was inspired from to make
 * Breakout. Someone made a clone of the original Breakout Atari game, and I took inspiration
 * from him. ABSOLUTELY NO CODE is copied from his GitHub; only the songs are used AND credited.
 * Inspiration from the game is taken, of course, but no code whatsoever was plagiarised or copied 
 * from his GitHub.
 * 
 * 
 * Menu Song: 1: This soundtrack came from a game that a couple of students made in my APCSA class a couple
 * of years ago:  http://wsfcs.k12.nc.us//site/Default.aspx?PageID=71150
 * Level 1 Song: 2: https://github.com/CodeNed/Break-It/tree/master/res/sfx/songs
 * Level 2 Song: 3: https://www.youtube.com/watch?v=vBK0bxuDfs8
 * Level 3 Song: 4: https://www.youtube.com/watch?v=vBK0bxuDfs8
 * Level 4 Song: 5: https://www.youtube.com/watch?v=6y_NJg-xoeE
 * 
 * Menu Select: The SFX comes from the Nintendo Entertainment System game: Castlevania, by Konami.
 * Explosion Sound Effect: sfx.get(1):https://github.com/CodeNed/Break-It/blob/master/res/sfx/explode_1.mp3
 * Whoosh Sound Effect: sfx.get(2): https://www.youtube.com/watch?v=JKbsMDHh-Q4
 * 
 * @author Me
 *
 */

public class SongBox {
	
	public List<StandardAudio> songs;//ArrayList for the songs
	public List<StandardAudio> sfx;//ArrayList for the sound effects
	
	public StandardAudio[] bufferBricks = new StandardAudio[5];
	public StandardAudio[] whooshBuffers = new StandardAudio[3];
	
	public SongBox(){
		
		this.songs = new ArrayList<StandardAudio>();
		this.songs.add(new StandardAudio("Resources/Audio/Music/menu.wav",false)); //Menu music
		this.songs.add(new StandardAudio("Resources/Audio/Music/level1.wav",false));//Level 1 music
		this.songs.add(new StandardAudio("Resources/Audio/Music/level2.wav",false));//Level 2 music
		this.songs.add(new StandardAudio("Resources/Audio/Music/level3.wav",false));//Level 3 music
		this.songs.add(new StandardAudio("Resources/Audio/Music/level4.wav",false));//Level 4 music
		
		this.sfx = new ArrayList<StandardAudio>();
		this.sfx.add(new StandardAudio("Resources/Audio/SFX/menuselect.wav",true));//Clicking on a button
		
		for(int i = 0; i<bufferBricks.length; i++)
			this.bufferBricks[i] = new StandardAudio("Resources/Audio/SFX/explode_1.wav");//Breaking a brick 1
		
		for(int i = 0; i<whooshBuffers.length; i++)
			this.whooshBuffers[i] = new StandardAudio("Resources/Audio/SFX/whoosh.wav");//Getting an item
	}
	
	public void resetVolumes(){
		for(int i = 0; i<this.songs.size(); i++){
			this.songs.get(i).FXResetVolume();
		}
		
		for(int i = 0; i<this.sfx.size(); i++){
			this.sfx.get(i).FXResetVolume();
		}
	}
	
	/**
	 * A buffer for the bricks has to be made so more than one sound effect of them
	 * can be played at the same time; this was not working originally with the
	 * JavaFX library; it was instantiating a new StandardAudio object of the same type
	 * and simply playing it whenever a brick was destroyed, yet, this lowered the game's
	 * performance on a substantial level. This method will parse through the sound effects,
	 * and if one is being played, it will simply skip to the next one. There are five
	 * possible breaking buffers.
	 */
	public void breakBrick(){
		for(int i = 0; i<this.bufferBricks.length; i++){
			if(!this.bufferBricks[i].isPlaying()){
				this.bufferBricks[i].rAndP();
				return;
			}
		}
	}
	
	public void gainItem(){
		for(int i = 0; i<whooshBuffers.length; i++){
			if(!this.whooshBuffers[i].isPlaying()){
				this.whooshBuffers[i].rAndP();
				return;
			}
		}
	}
}
