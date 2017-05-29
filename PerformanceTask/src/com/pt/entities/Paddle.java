package com.pt.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.joshuacrotts.standards.StandardDraw;
import com.joshuacrotts.standards.StandardFade;
import com.joshuacrotts.standards.StandardGame;
import com.joshuacrotts.standards.StandardGameObject;
import com.joshuacrotts.standards.StandardID;
import com.pt.main.Game;
import com.pt.main.Game.State;

public class Paddle extends StandardGameObject implements KeyListener{

	private Game stdGame;
	
	private final int LEFT_BORDER = 20;
	private final int RIGHT_BORDER = 30;
	private final int NORMAL_WIDTH = 100;
	private final int NORMAL_HEIGHT = 10;
	
	//Alternating colors for the paddle
	private Color normal = new Color(0,0,255);//blue
	private StandardFade largeFade = new StandardFade(StandardDraw.TEA_ROSE, StandardDraw.GOLD, 0.005);
	
	//States of the Paddle; if they have a powerup activated or not
	private int timer = 500;
	private boolean isLarge = false;
	
	public Paddle(int x, int y, StandardGame stdGame){
		super(x,y, StandardID.Player);
		
		this.setWidth(100);
		this.setHeight(10);
		
		this.stdGame = (Game) stdGame;
		
		this.stdGame.handler.addEntity(this);
	}
	
	public void tick(){
		
		//Checks the conditions of the Large powerup
		if(this.isLarge){
			timer--;
			if(timer <= 0){
				this.isLarge = false;
				this.setWidth(100);
				timer = 500;
				return;
			}
		}
		
		
		if(this.getX() <= LEFT_BORDER){
			this.setX(LEFT_BORDER);
		}
		
		if(this.getX() >= this.stdGame.getWindow().returnWidth()-this.getWidth()-RIGHT_BORDER){
			this.setX(this.stdGame.getWindow().returnWidth()-this.getWidth()-RIGHT_BORDER);
		}
		
		this.setX((short)this.getX() + (short)this.getVelX());
		this.setY((short)this.getY() +(short) this.getVelY());
	}
	
	public void render(Graphics2D g2){
		if(this.isLarge)
			g2.setColor(this.largeFade.combine());
		else
			g2.setColor(Color.BLUE);
		g2.fillRect((short)this.getX(),(short) this.getY(), this.getWidth(), this.getHeight());
		
		//g2.setColor(Color.red);
		//g2.draw(getBounds());
	}
	
	public void setLarge(boolean isLarge){
		this.isLarge = true;
	}
	
	public boolean isLarge(){
		return this.isLarge;
	}
	
	public void keyPressed(KeyEvent e){
		switch(e.getKeyCode()){
		
		case KeyEvent.VK_LEFT: this.setVelX(-5); break;
		case KeyEvent.VK_RIGHT: this.setVelX(5); break;
		case KeyEvent.VK_ENTER: this.stdGame.started = true; break;
		case KeyEvent.VK_F: if(this.stdGame.drawGrids){ this.stdGame.drawGrids = false; break; } else {this.stdGame.drawGrids = true; break;}
		case KeyEvent.VK_P: 
			if(this.stdGame.gameState == State.Game){
				if(this.stdGame.paused){ 
					this.stdGame.paused = false; 
					break; 
				} else {
					this.stdGame.paused = true; 
					break;
				}
			}
		}
	}
	
	public void keyReleased(KeyEvent e){
		this.setVelX(0);
	}
	
	public void keyTyped(KeyEvent e){
		
	}
}
