package com.pt.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.joshuacrotts.standards.StandardGame;
import com.joshuacrotts.standards.StandardHandler;
import com.joshuacrotts.standards.StandardID;
import com.joshuacrotts.standards.StandardParticle;
import com.joshuacrotts.standards.StdOps;
import com.pt.entities.Ball;
import com.pt.entities.Brick;
import com.pt.entities.items.Item;
import com.pt.entities.items.Large;
import com.pt.entities.items.Multi;

public class Level {

	private Game stdGame;
	private StandardHandler stdHandler;
	private SongBox songBox;
	
	private String levelPath;
	private Scanner file;

	private final int FARTHEST_LEFT_BRICK = 55;
	
	private boolean won = false;

	private ArrayList<Brick> bricks;

	public Level(String level, StandardGame stdGame, StandardHandler stdHandler, SongBox songBox){
		this.levelPath = level;
		this.stdGame = (Game) stdGame;
		this.stdHandler = stdHandler;
		this.songBox = songBox;
		this.bricks = new ArrayList<Brick>();

		try{
			this.file = new Scanner(new File(this.levelPath));
		}catch(Exception e){
			e.printStackTrace();
			System.err.printf("%s", "Error! Could not find file. Either it is not made, or it is placed incorrectly.");
		}

		this.init();
	}

	/**
	 * Initializes the level.
	 * Needs to be called when the level is cleared (if the player dies; it's a way of reloading all
	 * of the bricks)
	 */
	public void init(){
		int x = 0;
		int y = 30;

		if(file == null){
			System.err.println(this.levelPath);
			System.err.println("FILE IS NULL");
			System.exit(0);
		}

		try{
			while(this.file.hasNext()){

				String line = this.file.nextLine();

				y+=30;
				x= FARTHEST_LEFT_BRICK;

				for(int i = 0; i<line.length(); i++){
					if (line.charAt(i) >= '1' && line.charAt(i) <= '7'){
						this.bricks.add(new Brick(x,y,getColor(line.charAt(i))));
					}
					x+=40;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void tick(){
		
		/*
		 * Collision detection between the ball and the bricks of the level.
		 * Also spawns the randomly generated items
		 * 
		 */
		//Algorithm because of linear search pattern.
		for(int i = 0; i<stdGame.handler.size(); i++){
			if(this.stdGame.handler.get(i).getId() == StandardID.Enemy && this.stdGame.handler.get(i).getY() < 400){
				for(int j = 0; j<this.bricks.size(); j++){
					//Mathematical logic because of collision detection
					if(this.bricks.get(j).getBounds().intersects(this.stdGame.handler.getEntities().get(i).getBounds())){
						//Negates the velocity of the ball
						this.stdGame.handler.get(i).setVelY(-this.stdGame.handler.get(i).getVelY());
						//Calls the breakBrick() method to play sfx for the bricks being broken.
						this.songBox.breakBrick(); //Calls another method to handle how the bricks are broken.
						GUI.score+=100; //Adds 100 to the score
						int large = StdOps.rand(0, Item.LARGE_RARITY);//Implements math because of the random numbers.
						
						int multi = StdOps.rand(0, Item.MULTI_RARITY);
						
						if(large == 0)
							this.stdGame.handler.addEntity(new Large((int)this.bricks.get(j).getX()+30, 
															(int)this.bricks.get(j).getY()+this.bricks.get(j).getHeight(),
															this.stdGame, this.stdGame.handler, this.songBox));
						if(multi == 0)
							this.stdGame.handler.addEntity(new Multi((int)this.bricks.get(j).getX()+30, 
									(int)this.bricks.get(j).getY()+this.bricks.get(j).getHeight(),
															this.stdGame, this.stdGame.handler, this.songBox));

						for(int k = 0; k<20; k++){
							this.stdGame.handler.addEntity(new StandardParticle((int)this.bricks.get(j).getX()+30, 
									(int)this.bricks.get(j).getY()+this.bricks.get(j).getHeight(),
															20f, this.bricks.get(j).getColor(), this.stdGame.handler));
						}
						this.bricks.remove(j);
						j--;

					}
				}
			}
		}


		if(this.bricks.isEmpty()){
			this.stdGame.won = true;
			return;
		}
		else
			this.stdGame.won = false;
		
		/*This clause is for if there are multiple balls on the screen
		 *checks if there are any instances of a ball. If not, it 
		 *changes the state of the game to lost.
		 */
		
		boolean lost = true;
		if(!this.stdGame.won){
			for(Object o : this.stdHandler.getEntities()){
				if(o instanceof Ball){
					lost = false;
					break;
				}
			}
		}
		if(lost)
			this.stdGame.lost = lost;


	}

	public void render(Graphics2D g2){
		for(int i = 0; i<bricks.size(); i++)
			this.bricks.get(i).render(g2);
	}

	public void reload(){
		this.bricks.clear();

		for(int i = 0; i<this.stdHandler.size(); i++){

			if(this.stdHandler.get(i).getId() != StandardID.Player){
				this.stdHandler.removeEntity(this.stdHandler.get(i));
				i--;
			}
		}

		try {
			this.file = new Scanner(new File(this.levelPath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.init();

		return;
	}
	
	public void clear(){
		for(int i = 0; i<this.stdHandler.size(); i++){

			if(this.stdHandler.get(i).getId() != StandardID.Player){
				this.stdHandler.removeEntity(this.stdHandler.get(i));
				i--;
			}
		}
	}

	public Color getColor(char c){

		//	System.out.println(c);
		if(c == '1'){
			return Color.RED;
		}

		if(c == '2'){
			return Color.BLUE;
		}

		if(c == '3'){
			return Color.GREEN;
		}

		if(c == '4'){
			return Color.YELLOW;
		}

		if(c == '5'){
			return Color.ORANGE;
		}

		if(c == '6'){
			return Color.PINK;
		}

		if(c == '7'){
			return Color.MAGENTA;
		}

		if(c == '8'){
			return Color.BLACK;
		}

		if(c == '9'){
			return Color.WHITE;
		}
		return null;

	}

	public String getLevelPath(){
		return this.levelPath;
	}
}
