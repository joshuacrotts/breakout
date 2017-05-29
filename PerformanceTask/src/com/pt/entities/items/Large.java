package com.pt.entities.items;

import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.joshuacrotts.standards.StandardAnimator;
import com.joshuacrotts.standards.StandardDraw;
import com.joshuacrotts.standards.StandardHandler;
import com.joshuacrotts.standards.StandardID;
import com.joshuacrotts.standards.StandardParticle;
import com.pt.entities.Paddle;
import com.pt.main.Game;
import com.pt.main.SongBox;

public class Large extends Item {

	/*Items need to reference the handler directly, instead of passing a standardgame
	*instance and having it reference the handler that way.*/
	private Game stdGame;
	private StandardHandler stdHandler;
	private SongBox songBox;
	
	public Large(int x, int y, Game stdGame, StandardHandler stdHandler, SongBox songBox) {
		super(x, y);
		
		this.setId(StandardID.Large);
		
		this.stdGame = stdGame;
		this.stdHandler = stdHandler;
		this.songBox = songBox;
		
		for(int i = 0; i<this.MAX_FRAMES; i++){
			try {
				this.frames.add(ImageIO.read(new File("Resources/Sprites/Items/Large/large"+i+".png")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		this.setVelY(2);
		
		this.setAnimation(new StandardAnimator(this.frames,120,this));
		
	}
	
	@Override
	public void tick() {
		
		if(this.getY() > 800){
			
			for(int i = 0; i<100; i++){
				this.stdHandler.addEntity(new StandardParticle((int)this.getX()+(this.getWidth()/2),(int)this.getY(),200f,StandardDraw.CARDINAL_RED,this.stdHandler,true));
			}
			
			this.stdHandler.removeEntity(this);
			this.setAlive(false);
		}
			
		
		if(this.isAlive()){
			this.getAnimation().animate();
			
			//Only loop through the entire list if it's necessary
			if(this.getY() >= 650){
				for(int i = 0; i<stdHandler.size(); i++){
					if((this.stdHandler.get(i).getId() == StandardID.Player) && (this.getBounds().intersects(this.stdHandler.get(i).getBounds()))){

						songBox.gainItem();
						
						((Paddle) this.stdHandler.get(i)).setLarge(true);
						((Paddle) this.stdHandler.get(i)).setWidth(this.stdHandler.get(i).getWidth()+20);//Activates the powerup within here.
						this.stdHandler.removeEntity(this);
						break;
					}
				}
			}
			
			this.setY((int)this.getY() + (int)this.getVelY());
		}
		
		
	}

	@Override
	public void render(Graphics2D g2) {
		g2.drawImage(this.getCurrentSprite(), (int)this.getX(), (int)this.getY(), null);

	}

}
