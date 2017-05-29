package com.pt.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import com.joshuacrotts.standards.StandardDraw;
import com.joshuacrotts.standards.StandardFade;
import com.joshuacrotts.standards.StandardHandler;
import com.joshuacrotts.standards.StdOps;
import com.pt.entities.Ball;
import com.pt.main.Game.State;

public class Menu implements MouseListener, MouseMotionListener, KeyListener{

	private StandardHandler starHandler;
	private StandardFade stdFade = new StandardFade(Color.RED, Color.BLUE,.005);
	private Game stdGame;
	private SongBox songBox;

	private String imgName;
	private BufferedImage img;
	
	private String taunt = "";

	private Font titleFont;
	private Font smallFont;
	public static Font returnFont; //This font is used quite globally. Maybe I should access them via StdOps.

	//Button color combinations
	private Color playButton;
	private Color helpButton;
	private Color exitButton;
	private Color easyButton;
	private Color medButton;
	private Color hardButton;
	private Color returnButton;
	private Color playAgain;
	
	//Enum for the states of the menu itself (help, difficulty, main)
	private enum menuState {Main,Difficulty,Help};
	private menuState mState = menuState.Main;
	private menuState previousState;
	
	public Menu(Game stdGame, SongBox songBox){
		this.stdGame = stdGame;
		this.songBox = songBox;
		this.initFonts();
	}

	public void tick(){

	}

	public void render(Graphics2D g2){
		
		if(this.stdGame.gameState == State.GameOver){
			Font f = g2.getFont(); //Retrieves current font
			
			g2.setFont(this.titleFont);//Applies the title font to draw the Game Over string
			g2.setColor(this.stdFade.combine());
			g2.drawString("GAME OVER", StdOps.rand(129, 130), StdOps.rand(199, 200));
			
			Font scoreFont = this.titleFont.deriveFont(30f);
			
			g2.setFont(scoreFont);
			g2.drawString("Your score was: "+GUI.score, StdOps.rand(174, 175), StdOps.rand(319, 320));
			
			g2.setColor(Color.WHITE);
			g2.setFont(this.smallFont);
			g2.drawString("Play Again?", StdOps.rand(198, 199), StdOps.rand(419, 420));
			
			//Below starts the Play Again button
			g2.setColor(this.playAgain);
			g2.drawString("Again!", StdOps.rand(295, 296), StdOps.rand(539, 540));
			g2.drawRoundRect(280, 480, 250, 100,30,30);
			
			//Below is the color for the Exit button
			g2.setColor(this.exitButton);
			g2.drawString("Exit", StdOps.rand(330, 331), StdOps.rand(659, 660));
			g2.drawRoundRect(295, 600, 220, 100,30,30);
			return;
			
		}

		if(this.stdGame.gameState == State.Menu && this.mState == menuState.Main){
			Font f = g2.getFont();//Retrieves current font for the grid-lines.

			g2.setColor(this.stdFade.combine());
			g2.setFont(this.titleFont);//Applies new font (BACKTO1982.TTF)
			g2.drawString("Breakout", StdOps.rand(139, 140), StdOps.rand(199, 200)); //All upper-case

			//Below are coordinates/colors for the Play Button and the text corresponding.
			g2.setColor(Color.WHITE);
			g2.setColor(this.playButton);
			g2.setFont(this.smallFont);
			g2.drawString("Play", StdOps.rand(324, 325), StdOps.rand(439, 440));
			g2.drawRoundRect(295, 380, 220, 100,30,30);

			//Below are coordinates/colors for the Help Button and the text corresponding.
			g2.setColor(this.helpButton);
			g2.drawString("Help", StdOps.rand(324, 325), StdOps.rand(559, 560));
			g2.drawRoundRect(295, 500, 220, 100,30,30);

			//Below are coordinates/colors for the Exit Button and the text corresponding.
			g2.setColor(this.exitButton);
			g2.drawString("Exit", StdOps.rand(329, 330), StdOps.rand(679,680));
			g2.drawRoundRect(295, 620, 220, 100,30,30);
			
			g2.setFont(f);

			//drawGrid(g2);
		}
		
		if(this.mState == menuState.Difficulty){
			Font f = g2.getFont();//Retrieves current font for the gridlines.

			g2.setColor(stdFade.combine());
			g2.setFont(this.titleFont);//Applies new font (BACKTO1982.TTF)
			g2.drawString("Breakout", StdOps.rand(139, 140), StdOps.rand(199, 200)); //All uppercase

			//Below are coordinates/colors for the Passive Button and the text corresponding.
			g2.setColor(Color.WHITE);
			g2.setColor(this.easyButton);
			g2.setFont(this.smallFont);
			g2.drawString("Passive", StdOps.rand(264, 265), StdOps.rand(439, 440));
			g2.drawRoundRect(255, 380, 310, 100,30,30);

			//Below are coordinates/colors for the Standard Button and the text corresponding.
			g2.setColor(this.medButton);
			g2.drawString("Standard", StdOps.rand(234, 235), StdOps.rand(559, 560));
			g2.drawRoundRect(225, 500, 370, 100,30,30);

			//Below are coordinates/colors for the Pro Button and the text corresponding.
			g2.setColor(this.hardButton);
			g2.drawString("Pro", StdOps.rand(345, 346), StdOps.rand(680,681));
			g2.drawRoundRect(255, 620, 310, 100,30,30);

			//Below is the return button coordinates and the text corresponding.
			g2.setFont(Menu.returnFont);
			g2.setColor(this.returnButton);
			g2.drawString("Return", StdOps.rand(81, 82), StdOps.rand(698,699));
			g2.drawRoundRect(55, 670, 150, 50,30,30);
			
			//Draws the taunt text.
			g2.setFont(Menu.returnFont);
			g2.setColor(Color.YELLOW);
			g2.drawString(this.taunt, 180,320);
			
			g2.setFont(f);
			
			//drawGrid(g2);
		}
		
		if(this.mState == menuState.Help){
			Font f = g2.getFont();//Retrieves current font for the gridlines.

			//Draws the title once again, for the help screen solely.
			g2.setColor(stdFade.combine());
			g2.setFont(this.titleFont);//Applies new font (BACKTO1982.TTF)
			g2.drawString("Breakout", StdOps.rand(139, 140), StdOps.rand(199, 200)); //All upper-case
			
			//Below is all of the strings for the help text.
			g2.setFont(Menu.returnFont);
			g2.setColor(Color.WHITE);
			g2.drawString("To play this game, use the ARROW KEYS to move", 70, 320);
			g2.drawString("the paddle. The ball will hit the paddle and ", 80, 360);
			g2.drawString("bounce and hit the bricks above. Keep going to", 60, 400);
			g2.drawString("become the ultimate Breakout champion!", 120, 440);
			
			//Below is the return button coordinates and the text corresponding.
			g2.setFont(Menu.returnFont);
			g2.setColor(this.returnButton);
			g2.drawString("Return", StdOps.rand(81, 82), StdOps.rand(698,699));
			g2.drawRoundRect(55, 670, 150, 50,30,30);
			
			g2.setFont(f);
			
			//drawGrid(g2);
		}
	}


	private void initFonts(){
		this.titleFont = StdOps.initFont("Resources/Fonts/BACKTO1982.TTF", 72f);
		this.smallFont = StdOps.initFont("Resources/Fonts/BACKTO1982.TTF", 48f);
		Menu.returnFont = StdOps.initFont("Resources/Fonts/BACKTO1982.TTF", 18f);
	}


	/**
	 * This entire method is determining if the mouse is **OVER** a button.
	 * No clicking is done in this method; make sure if statements are in order
	 * to make sure buttons can't be drawn or clicked or mouse-overed if the 
	 * game state is not the menu.
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		
		//***Clause for if the user is on the GAME STATE of Game Over***
		if(this.stdGame.gameState == State.GameOver){
			//Mouse over the Play Again button
			if(StdOps.mouseOver(e.getX(),e.getY(), 295, 480, 220, 100)){
				this.playAgain = StandardDraw.CRIMSON;
			}else{
				this.playAgain = Color.WHITE;
			}
			
			if(StdOps.mouseOver(e.getX(), e.getY(), 295, 600, 220, 100)){
				this.exitButton = StandardDraw.RED_VIOLET;
			}else{
				this.exitButton = Color.WHITE;
			}
		}
		
		//***Clause for if the user is on the main menu, as opposed to help, etc.***
		if(this.mState == menuState.Main){

			//Mouse over the Play button.
			if(StdOps.mouseOver(e.getX(), e.getY(),295, 380, 220, 100)){
				this.playButton = StandardDraw.BLUE_CRAYOLA;
			}else{
				this.playButton = Color.WHITE;
			}

			//Mouse over the help button.
			if(StdOps.mouseOver(e.getX(), e.getY(),295, 500, 220, 100)){
				this.helpButton = StandardDraw.BRIGHT_GREEN;
			}else{
				this.helpButton = Color.WHITE;
			}

			//Mouse over the exit button.
			if(StdOps.mouseOver(e.getX(), e.getY(),295, 620, 220, 100)){
				this.exitButton = StandardDraw.RED_VIOLET;
			}else{
				this.exitButton = Color.WHITE;
			}
		}
		
		//***Clause for if the user is on the difficulty selection screen.***
		if(this.mState == menuState.Difficulty){
			//Mouse over the Passive button.
			if(StdOps.mouseOver(e.getX(), e.getY(),255, 380, 310, 100)){
				this.easyButton = StandardDraw.GOLDENROD;
				this.taunt = "Passive is for beginners solely.";
				//System.out.println("EASY");
			}else{
				this.easyButton = Color.WHITE;
				//this.taunt = "";
			}

			//Mouse over the Standard button.
			if(StdOps.mouseOver(e.getX(), e.getY(),225, 500, 370, 100)){
				this.medButton = StandardDraw.CYBER_YELLOW;
				this.taunt = "Standard is not hard, but not easy.";
			}else{
				this.medButton = Color.WHITE;
				//this.taunt = "";
			}

			//Mouse over the Pro button.
			if(StdOps.mouseOver(e.getX(), e.getY(),255, 620, 310, 100)){
				this.hardButton = StandardDraw.RED_IMPERIAL;
				this.taunt = "Pro... all I can say is: good luck.";
			}else{
				this.hardButton = Color.WHITE;
				//this.taunt = "";
			}
			
			//Mouse over the return button.
			if(StdOps.mouseOver(e.getX(), e.getY(),55, 670, 150, 50)){
				this.returnButton = StandardDraw.SPANISH_BLUE;
			}else{
				this.returnButton = Color.WHITE;
				
			}
		}
		
		/***Clause for when the user is on the help screen and presses the return button/etc.***/
		if(this.mState == menuState.Help){
			//Mouse over the return button.
			if(StdOps.mouseOver(e.getX(), e.getY(),55, 670, 150, 50)){
				this.returnButton = StandardDraw.SPANISH_BLUE;
			}else{
				this.returnButton = Color.WHITE;
				
			}
		}
	}

	/**@Override
	 * This method is for when the user actually **CLICKS** the button, 
	 * no hovering capabilities are handled in the method. It will
	 * change the game states, etc.
	 */
	public void mouseClicked(MouseEvent e) {
		//Clause for if the user is on the main menu, as opposed to help, etc.
		if(this.stdGame.gameState == State.GameOver){
			//Mouse over the Play Again button
			if(StdOps.mouseOver(e.getX(),e.getY(), 295, 480, 220, 100)){
				
				songBox.sfx.get(0).FXPlay();
				
				GUI.lives = 1;
				GUI.score = 0;
				this.stdGame.levelNum = 0;
				
				this.stdGame.gameState = State.Menu;
				this.mState = menuState.Main;
				
				return;
			}
			
			if(StdOps.mouseOver(e.getX(), e.getY(), 295, 600, 220, 100)){
				songBox.sfx.get(0).FXPlay();
				System.exit(0);
			}
		}
		
		if(this.stdGame.gameState == State.Menu && this.mState == menuState.Main){

			//Mouse clicks the Play button.
			if(StdOps.mouseOver(e.getX(), e.getY(),295, 380, 220, 100)){

				songBox.sfx.get(0).FXPlay();
				
				this.previousState = this.mState;
				this.mState = menuState.Difficulty;
				return;
				
			}

			//Mouse clicks the help button.
			if(StdOps.mouseOver(e.getX(), e.getY(),295, 500, 220, 100)){
				songBox.sfx.get(0).FXPlay();
				this.previousState = this.mState;
				this.mState = menuState.Help;
			}

			//Mouse clicks the exit button.
			if(StdOps.mouseOver(e.getX(), e.getY(),295, 620, 220, 100)){
				songBox.sfx.get(0).FXPlay();
				System.exit(0);
			}
		}
		
		//Clause for is the user is in the difficulty state.
		if(this.stdGame.gameState == State.Menu && this.mState == menuState.Difficulty){
			//Mouse clicks the Passive button.
			if(StdOps.mouseOver(e.getX(), e.getY(),255, 380, 310, 100)){
				songBox.sfx.get(0).FXPlay();
				this.stdGame.gameState = State.Game;
				this.stdGame.difficulty = 1;
				this.stdGame.handler.addEntity(new Ball(400,400,this.stdGame,1));
			}

			//Mouse clicks the Standard button.
			if(StdOps.mouseOver(e.getX(), e.getY(),225, 500, 370, 100)){
				songBox.sfx.get(0).FXPlay();
				this.stdGame.gameState = State.Game;
				this.stdGame.difficulty = 2;
				this.stdGame.handler.addEntity(new Ball(400,400,this.stdGame,2));
			}

			//Mouse clicks the Pro button.
			if(StdOps.mouseOver(e.getX(), e.getY(),255, 620, 310, 100)){
				songBox.sfx.get(0).FXPlay();
				this.stdGame.gameState = State.Game;
				this.stdGame.difficulty = 3;
				this.stdGame.handler.addEntity(new Ball(400,400,this.stdGame,3));
			}
		}
		
		
		//Conditions for if the mouse is over the return button and clicks it.
		if(this.stdGame.gameState == State.Menu){
			
			if(StdOps.mouseOver(e.getX(), e.getY(),55, 670, 150, 50)){
				songBox.sfx.get(0).FXPlay();
				this.mState = this.previousState;				
			}
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	
	@SuppressWarnings("unused")
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
	
	
	/**
	 * Getters and Setters Below
	 */
	
	public StandardFade getStdFade() {
		return stdFade;
	}

	public void setStdFade(StandardFade stdFade) {
		this.stdFade = stdFade;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public BufferedImage getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}

}
