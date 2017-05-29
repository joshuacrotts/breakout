package com.pt.entities;

import java.awt.Color;
import java.awt.Graphics2D;

import com.joshuacrotts.standards.StandardGameObject;
import com.joshuacrotts.standards.StandardHandler;
import com.joshuacrotts.standards.StandardTrail;
import com.joshuacrotts.standards.StdOps;
import com.pt.main.Game;

public class Star extends StandardGameObject{

	private Game stdGame;
	private StandardHandler starHandler;
	private Color color;
	
	public Star(int x, int y, int width, int height, Game stdGame, StandardHandler starHandler){
		super(x,y,width,height);
		
		this.stdGame = stdGame;
		this.starHandler = starHandler;
		
		this.setVelX(StdOps.rand(-20,-15));
		this.setVelY(this.getVelX());
		
		this.starHandler.addEntity(this);
	}

	@Override
	public void tick() {
		if(this.getX() <= -25){
			this.starHandler.removeEntity(this);
		}
		this.setX((short)this.getX() +(short) this.getVelX());
		this.setY((short)this.getY() + (short)this.getVelY());
		
		new StandardTrail((short)this.getX(),(short)this.getY(),(short)this.getWidth(),(short)this.getHeight(),this.color, .05f, this.stdGame, starHandler);
		
	}

	@Override
	public void render(Graphics2D g2) {
		switch(StdOps.rand(0, 3)){
			case 0: this.color = Color.BLACK; break;
			case 1: this.color = Color.WHITE; break;
			case 2: this.color = Color.GRAY; break;
			case 3: this.color = Color.DARK_GRAY; break;
		}
		
		g2.setColor(this.color);
		g2.fillRect((short)this.getX(),(short) this.getY(), this.getWidth(), this.getHeight());
		
	}
}
