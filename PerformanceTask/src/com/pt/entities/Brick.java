package com.pt.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.joshuacrotts.standards.StandardGameObject;
import com.joshuacrotts.standards.StandardID;

public class Brick extends StandardGameObject{

	private Color c;

	public Brick(int x, int y, Color c){
		super(x,y,40,20);
		this.c = c;
		this.setId(StandardID.Obstacle);

		//System.out.println(c);
		
		if(c != null){
			try{
				this.setCurrentSprite(ImageIO.read(new File("Resources/Sprites/Bricks/"+this.getStringPair(this.c)+"brick.png")));
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	public void tick(){

	}

	public void render(Graphics2D g2){
		if(c == null)
			return;
		
		g2.setColor(new Color(0,0,0,127));
		g2.fillRect((short)this.getX()+this.getWidth()/3, (short)this.getY()+this.getHeight()/2, this.getWidth(), this.getHeight());
		g2.drawImage(this.getCurrentSprite(),(short)this.getX(),(short) this.getY(), 40, this.getHeight(), null);
		
		
		//System.out.println(this);
	}

	public String getStringPair(Color c){
		
		//System.out.println(c);
		if(c == Color.RED)
			return "red";
		if(c == Color.BLUE)
			return "blue";
		if(c == Color.GREEN)
			return "green";
		if(c == Color.ORANGE)
			return "orange";
		if(c == Color.PINK)
			return "pink";
		if(c == Color.YELLOW)
			return "yellow";
		if(c == Color.MAGENTA)
			return "purple";
		
		return null;
	}

	public String toString(){
		return "X: "+this.getX()+"\tY: "+this.getY()+"\tWidth: "+this.getWidth()+"\tHeight: "+this.getHeight()+"\tColor: "+this.c;
	}
	
	public Color getColor(){
		return this.c;
	}
}
