package com.pt.entities;

import java.awt.Graphics2D;

import com.joshuacrotts.standards.StandardDraw;
import com.joshuacrotts.standards.StandardFade;
import com.joshuacrotts.standards.StandardGame;
import com.joshuacrotts.standards.StandardGameObject;
import com.joshuacrotts.standards.StandardID;
import com.joshuacrotts.standards.StandardTrail;
import com.joshuacrotts.standards.StdOps;
import com.pt.main.Game;

public class Ball extends StandardGameObject{

	private Game stdGame;
	private StandardFade stdFade = new StandardFade(StandardDraw.YELLOW, StandardDraw.RUSTY_RED, .008);
	
	private final int LEFT_BORDER = 20;
	private final int RIGHT_BORDER = 30;
	
	public Ball(int x, int y, StandardGame stdGame, int difficulty){
		super(x,y, 8,8);
		
		this.stdGame = (Game) stdGame;

		this.setId(StandardID.Enemy);
		
		if(difficulty == 1){
			do{
				this.setVelX(StdOps.rand(-5, 5));
				this.setVelY(StdOps.rand(-5, 5));
			}while((this.getVelX() <= 3 && this.getVelX() >= -3) || (this.getVelY() <= 3 && this.getVelY() >= -3));
		}
		
		if(difficulty == 2){
			do{
				this.setVelX(StdOps.rand(-10, 10));
				this.setVelY(StdOps.rand(-10, 10));
			}while((this.getVelX() <= 7 && this.getVelX() >= -7) || (this.getVelY() <= 7 && this.getVelY() >= -7));
		}
		
		if(difficulty == 3){
			do{
				this.setVelX(StdOps.rand(-20, 20));
				this.setVelY(StdOps.rand(-20, 20));
			}while((this.getVelX() <= 12 && this.getVelX() >= -12) || (this.getVelY() <= 12 && this.getVelY() >= -12));
		}
	}
	
	public void tick(){
		if(this.getX() <= this.LEFT_BORDER || this.getX() >= this.stdGame.getWindow().returnWidth()-this.RIGHT_BORDER){
			this.setVelX(-this.getVelX());
		}
		
		if(this.getY() <= this.LEFT_BORDER){
			this.setVelY(-this.getVelY());
		}
		
		if(this.getY() > 800){
			//this.stdGame.lost = true; This can't be here because it will default to lost even if it was only ONE ball that went out of bounds.
			this.stdGame.handler.removeEntity(this);
		}
		
		this.setX((int)this.getX() + (int)this.getVelX());
		this.setY((int)this.getY() + (int)this.getVelY());
		
		new StandardTrail((short)this.getX(),(short) this.getY(), (short)this.getWidth(), (short)this.getHeight(), this.stdFade.combine(), .1f, stdGame, this.stdGame.handler);
	}
	
	public void render(Graphics2D g2){
		g2.setColor(this.stdFade.combine());
		g2.fillOval((int)this.getX(), (int)this.getY(), this.getWidth(), this.getHeight());
		//g2.drawImage(getCurrentSprite(), this.getX(),this.getY(), null);
	//	g2.setColor(Color.red);
	//	g2.draw(getBounds());
	}
}
