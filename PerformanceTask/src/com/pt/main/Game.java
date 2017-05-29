package com.pt.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferStrategy;

import com.joshuacrotts.interactors.StandardHandlerInteractor;
import com.joshuacrotts.standards.StandardDraw;
import com.joshuacrotts.standards.StandardFade;
import com.joshuacrotts.standards.StandardGame;
import com.joshuacrotts.standards.StandardHandler;
import com.joshuacrotts.standards.StandardWindow;
import com.joshuacrotts.standards.StdOps;
import com.pt.entities.Ball;
import com.pt.entities.Paddle;

/**
 * This class is for creating the main Game. This incorporates the Runnable 
 * interface for instantiating the thread, the Window, and the Handler, basically
 * everything that the StandardGame uses. (Both StandardGame and Game were created by me.
 * 
 * Listeners will need to be added into the constructor of this class. Add them in the constructor
 * directly, or add them in the method initIterators() or initObjects(). These methods may make the
 * game run slightly slower because it presumes every object to bode a listener of some sort, which
 * some may not.
 * 
 * I am going against my own advice from when I made StandardGame; I told myself to not extend StandardGame,
 * though, I'm doing it for simplistic's sake, it helps when referencing the Window, and other handlers. 
 * 
 *@author Me
 *
 * Sources for background images: 
 * https://www.walldevil.com/791519-star-nebula-wallpaper.html
 * https://images8.alphacoders.com/397/397989.jpg
 * https://wallpaperscraft.com/image/space_flight_sky_stars_82970_1920x1080.jpg
 * http://eskipaper.com/images/awesome-blue-space-wallpaper-1.jpg
 * http://p1.pichost.me/i/66/1912004.jpg
 * http://cdn.wonderfulengineering.com/wp-content/uploads/2014/04/space-wallpapers-4.jpg
 * 
 * Sprites:
 * https://www.vg-resource.com/thread-21297-post-474734.html
 * https://www.spriters-resource.com/mobile/arkanoidios/sheet/65937/
 * 
 * TO DO LIST:
 * Add negation for the y velocity of the ball when it hits a brick: DONE
 * Change direction of ball based on where it hits the paddle: N/A 
 * Add new levels and transition from level to level: DONE
 * Add paused feature: DONE
 * Add more powerups and implement them: DONE
 * Add Fix the borders/boundaries (paddle/ball shouldn't go outside the picture: DONE 
 * Add the sound effects: DONE
 * Add the GUI for score and timer: DONE
 * Add a read/write feature for scores: 
 * Add the level completion feature: DONE
 * Add the death feature: DONE
 * Add the changing backgrounds: DONE
 * Polish the StandardFade: DONE
 * 
 */
public class Game extends StandardGame {

	private static final long serialVersionUID = -7267473597604645224L;

	//Objects that are grouped.
	public StandardHandler handler;
	public StandardHandlerInteractor actorHandler;
	public Menu menu;
	public Background bg;
	public GUI gui;
	public SongBox songBox;

	//Thread information
	private Thread thread;
	private boolean running = false;
	private boolean consoleFPS = true;
	private boolean titleFPS = true;
	public boolean paused = false;
	public boolean started = false;

	//Graphics information
	private RenderingHints antialias;

	//Frame information
	private int currentFPS;
	@SuppressWarnings("unused")
	private int frames;

	//Game States
	public enum State {Menu,Game, GameOver};
	public State gameState = State.Menu;
	public int difficulty = 0;
	public boolean firstPass = true;
	public boolean lost = false;
	public boolean won = false;

	//Level information (Information concerning how levels are constructed)
	private final int MAX_LEVELS = 7;
	public Level[] levels = new Level[MAX_LEVELS];
	public int levelNum = 0;
			 //0 <= n < MAX_LEVELS

	//Objects
	private Paddle player;

	//Colors
	private StandardFade stdFade = new StandardFade(StandardDraw.YELLOW, StandardDraw.RUSTY_RED, .008);
	private int alpha = 0; //for lost state

	//Debug States
	public boolean drawGrids = false;

	public Game(int width, int height, String title){
		super(width,height,title);

		this.handler = new StandardHandler();
		this.actorHandler = new StandardHandlerInteractor();
		this.songBox = new SongBox();
		this.menu = new Menu(this, this.songBox);
		this.bg = new Background(this);
		this.gui = new GUI(this, this.handler);


		//Adds the anti-aliasing parameters for the RenderingHints object
		this.antialias = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		//See below for documentation on these methods.
		this.initInteractors();
		this.initObjects();
		this.initLevels();

		//Add listeners here
		this.addKeyListener(this.menu);
		this.addMouseListener(this.menu);
		this.addMouseMotionListener(this.menu);

		this.framesToConsole(false);

		this.player = new Paddle(300,700,this); //Initializes the player @ x: 300 y: 700

		this.addKeyListener(player);
		this.handler.addEntity(player);

		this.start();
	}

	private synchronized void start(){
		if(running)
			return;
		else{
			this.thread = new Thread(this);
			this.thread.start();
			this.running = true;
		}
	}

	private synchronized void stop(){
		if(!this.running)
			return;
		else{
			try{
				this.thread.join();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			this.running = false;
			System.exit(0);
		}
	}
	/**
	 * This game loop was provided by online sources, though there are many examples
	 * of a game loop online.
	 * 
	 * @author RealTutsGML
	 */
	public void run() {
		requestFocus(); //Focuses the click/input on the frame/canvas.
		long lastTime = System.nanoTime(); //The current system's nanotime.
		double ns = 1000000000.0 / 60.0; //Retrieves how many nano-seconds are currently in one tick/update.
		double delta = 0; //How many unprocessed nanoseconds have gone by so far.
		long timer = System.currentTimeMillis();
		int frames = 0; //The frames per second.
		int updates = 0; //The updates per second.
		//The game loop
		while (running) {

			boolean renderable = false; //Determines if the game should render the actual graphics.

			long now = System.nanoTime();//At this point, the current system's nanotime once again.
			delta += (now - lastTime) / ns;
			lastTime = now;
			//If the amount of unprocessed ticks is or goes above one...
			//Also determines if the game should update or not/render. Approximately sixty frames per second.
			while (delta >= 1) {
				tick();

				delta--;
				updates++;

				renderable = true;
			}

			if(renderable){
				frames++;
				render();
			}

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				if(this.titleFPS)
					window.setTitle(window.getTitle() + " | " + updates + " ups, " + frames + " fps");

				if(this.consoleFPS)
					System.out.println(window.getTitle() + " | " + updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}
		}

		this.stop();
	}

	/**
	 * This method should tick everything that needs to be updated via positioning, 
	 * mouse input, etc. ABSTRACTION IN PLACE HERE.
	 * 
	 * Why? All of the tick-able methods are placed here in their properly abstracted form;
	 * there is no focus on how the ticks are made, we simply know they are being ticked. It is 
	 * incredibly inefficient to have the background's tick code, the menu's tick code, handler's tick
	 * code, etc. all in this one method, when they can be separated into their proper classes.
	 */
	private void tick(){
		/***PUT ALL TICKABLE METHODS IN THIS METHOD; ALL CALCULATIONS, EVERYTHING***/
		bg.tick();
		this.controlSounds();

		if(this.gameState == State.Menu){
			menu.tick();
			songBox.songs.get(0).FXPlay();
		}

		if(!paused && !lost && !won){
			if(this.gameState == State.Game && this.started){
				handler.tick();	
				levels[levelNum].tick();
				gui.tick();
			}
		}

		actorHandler.tick();
		/*****END OF TICK METHOD INFORMATION AND METHODS***/
	}

	/**
	 * This method handles the graphics, the buffer strategy, and all of the game states/what
	 * should be placed and when and in what order. ABSTRACTION IN PLACE HERE!!!
	 * 
	 * Why? Well, all of the render() methods are being called here instead of actually being
	 * hard-coded here; they're coded in different classes. This render method makes up the bigger
	 * algorithm of multiple mini-render methods.
	 */
	private void render(){
		BufferStrategy bs = this.getBufferStrategy();

		if(bs == null){
			createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		Graphics2D g2 = (Graphics2D) g;//Casts the Graphics from the BufferStrategy into the Graphics2D library.

		//This adds anti-aliasing to the graphics (instantiated above in constructor)
		g2.setRenderingHints(this.antialias);

		/*******************PLACE ALL DRAWING INSTRUCTIONS WITHIN THIS SECTION OF THE RENDER METHOD******************/

		bg.render(g2);

		/*
		 * This just draws the grid lines for placement of different things.
		 */
		if(this.drawGrids){
			this.drawGrid(g2);
			Color old = g2.getColor();
			g2.setColor(Color.RED);
			g2.drawString("Entities: "+this.handler.size(), 600, 300);
			g2.drawString("SFX Amt: "+songBox.sfx.size(), 600, 400);
			g2.setColor(old);
		}

		if(this.gameState == State.Menu || this.gameState == State.GameOver){
			menu.render(g2);
		}

		if(this.gameState == State.Game){
			handler.render(g2);
			levels[levelNum].render(g2);
			gui.render(g2);
		}

		if(paused){
			//Draws the transparent blackness of a paused screen.
			Color old = g2.getColor();
			g2.setColor(new Color(0,0,0,127)); //Black with half transparent (alpha = 127)
			g2.fillRect(0, 0, this.window.returnWidth(), this.window.returnHeight());

			//Draws the white "PAUSED" string on the screen (only when paused)
			Font oldF = g2.getFont();
			g2.setFont(Menu.returnFont);
			g2.setColor(Color.WHITE);
			g2.drawString("PAUSED", StdOps.rand(349, 350), StdOps.rand(374, 375));

			//Resets the font and color to what they were before the paused feature activated.
			g2.setFont(oldF);
			g2.setColor(old);

		}

		/*
		 * This handles the initial pause state, so the game doesn't start 
		 * immediately as the difficulty is chosen.
		 */
		if(!started && this.gameState == State.Game){
			g2.setColor(this.stdFade.combine());
			g2.setFont(Menu.returnFont);
			g2.drawString("Press Enter", 300, 300);

		}

		//The lost OR won clause 
		if(lost || won){
			if(firstPass){//allows for a temporary clock to let the screen fade to black.

				if(alpha < 255){
					alpha++;
				}else{
					firstPass = false;

					if(won){
						levelNum++;
						this.bg.setImage("space"+levelNum);
					}

					if(lost){
						GUI.lives--;
						if(GUI.lives == 0){
							this.levels[levelNum].clear();
							this.reloadAllLevels();
							this.bg.setImage("space0");
							this.gameState = State.GameOver;

						}
					}
					//songBox.clearSFX();
					this.levels[levelNum].reload();
					if(this.gameState != State.GameOver)
						this.handler.addEntity(new Ball(300,300,this,this.difficulty));
				}

			}else{
				if(alpha > 0){
					alpha--;
				}else{
					lost = false;
					started = false;
					firstPass = true;
					won = false;
				}

			}

			g2.setColor(new Color(0,0,0,alpha));
			g2.fillRect(0, 0, this.window.returnWidth(), this.window.returnHeight());
		}

		/************************DO NOT PLACE ANY MORE DRAWING INSTRUCTIONS WITHIN THIS SECTION OF THE RENDER METHOD**********************/

		g.dispose();
		g2.dispose();

		bs.show();
	}

	/**
	 * One should delete the first line [(actorHandler.addInteractor(new Standard...)] in this method when they begin to actually
	 * program their game. Do NOT delete the for loop.
	 */
	private void initInteractors(){
		//actorHandler.addInteractor(new StandardButton((short) 300,(short)300,(short)200,(short)100,"Test Button", Color.RED));

		for(int i = 0; i<actorHandler.size(); i++){
			addKeyListener((KeyListener) actorHandler.get(i));
			addMouseListener((MouseListener) actorHandler.get(i));
			addMouseMotionListener((MouseMotionListener) actorHandler.get(i));
		}

	}

	/**
	 * This method will initialize any objects that DO have Interactors on them.
	 * For example, if a Player object will have keyboard input, one should
	 * set the 'interact-able' variable in StandardGameObject to true, implement
	 * KeyListener, then add it to the StandardHandler.
	 */
	private void initObjects(){
		for(int i = 0; i<handler.size(); i++){
			if(handler.get(i).isInteractable()){
				addKeyListener((KeyListener) handler.get(i));
				addMouseListener((MouseListener) handler.get(i));
				addMouseMotionListener((MouseMotionListener) handler.get(i));
			}
		}
	}

	/**
	 * This method will initialize the levels and place the bricks in them, and whatnot.
	 */
	private void initLevels(){
		for(int i = 0; i<levels.length; i++){
			//Exception is handled in the Level constructor.
			levels[i] = new Level("Resources/Levels/level"+(i+1)+".txt", this, this.handler, this.songBox);
		}
	}

	/**
	 * This method will reload every level in the game, in the case of a game over situation.
	 */
	public void reloadAllLevels(){
		for(int i = 0; i<levels.length; i++){
			levels[i].reload();
		}
	}

	/**
	 * This method controls all of the sound effects;
	 * for some reason, it wants to make the sound volume
	 * way less than it should be, so I have to manually fix it.
	 */
	private void controlSounds(){
		try{
			if(this.gameState == State.Menu){
				if(songBox.songs.get(0).getFXVolume() != 1){
					songBox.songs.get(0).FXResetVolume();

				}
				songBox.songs.get(0).FXPlay();
			}else{
				songBox.songs.get(0).FXStop();
			}

			if(this.gameState == State.Game){

				if(songBox.songs.get(levelNum+1).getFXVolume() != 1 && (!lost && !won)){
					songBox.songs.get(levelNum+1).FXResetVolume();

				}
				songBox.songs.get(levelNum+1).FXPlay();
				songBox.songs.get(levelNum).FXStop();
			}else{
				songBox.songs.get(levelNum+1).FXStop();
			}
		}catch(IndexOutOfBoundsException e){
			return;
		}

	}

	public Game getGame(){
		return this;
	}

	public int getFPS(){
		return this.currentFPS;
	}

	/**
	 * These methods will take in a @param print, and allow for printing of the
	 * frames per second to either the console, title, or both, or neither.
	 */
	public void framesToConsole(boolean print){
		this.consoleFPS = print;
	}

	public void framesToTitle(boolean print){
		this.titleFPS = print;
	}

	//Getter for the window on a StandardGame object.
	public StandardWindow getWindow(){
		return this.window;
	}

	//Getter for the hander on a StandardGame object.
	public StandardHandler getHandler(){
		return this.handler;
	}

	//Getter for the interactor handler on a StandardGame object.
	public StandardHandlerInteractor getInteractors(){
		return this.actorHandler;
	}

	/**
	 * Loop is utilized from Tony Smith's assignments; draws a grid to help
	 * with positioning.
	 * @param g2
	 * @author Tony Smith @ WSFCS
	 */
	private void drawGrid(Graphics2D g2)
	{
		// The following two for loops draw a grid on the screen to help you 
		// position your object.  Once you have finished your object, you can 
		// comment out the call to this method.
		//Draw horizontal lines
		g2.setColor(Color.LIGHT_GRAY);									
		for (int i=0; i<=800; i=i+50)
		{
			g2.drawString(""+i, 5, i);
			Line2D.Double horizonalLine = new Line2D.Double(5, i, 800, i);	
			g2.draw(horizonalLine);
		}
		//Draw vertical lines
		for (int i=0; i<=800; i=i+50)
		{
			g2.drawString(""+i, i, 10);
			Line2D.Double horizonalLine = new Line2D.Double(i, 10, i, 800);	
			g2.draw(horizonalLine);
		}
		g2.setColor(Color.BLACK);									
		//**End of grid
	}

}

