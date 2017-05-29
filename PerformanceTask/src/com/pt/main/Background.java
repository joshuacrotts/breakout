package com.pt.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.joshuacrotts.standards.StandardFade;
import com.joshuacrotts.standards.StandardGame;
import com.joshuacrotts.standards.StandardHandler;
import com.joshuacrotts.standards.StdOps;
import com.pt.entities.Star;

public class Background {
	
	private Game game;
	private StandardHandler starHandler;
	private StandardFade stdFade = new StandardFade(Color.RED, Color.BLUE, .005);
	
	private String imgPath = "";
	private BufferedImage img;
	
	public Background(StandardGame stdGame){
		
		this.game = (Game) stdGame;
		this.starHandler = new StandardHandler();
		this.imgPath = "space0";
		
		try{	
			this.img = ImageIO.read(new File("Resources/Sprites/Backgrounds/"+imgPath+".png"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void tick(){
		if(this.starHandler.size() <= 300){
			new Star(StdOps.rand(700, 800), StdOps.rand(400, 1600), 2,2, this.game, this.starHandler);
		}

		this.starHandler.tick();
	}
	
	public void render(Graphics2D g2){
		g2.setColor(stdFade.combine());
		g2.fillRect(0,0,800,800);
		
		g2.drawImage(this.img, 20, 20, 750,730,null); //Draws the background
		
		this.starHandler.render(g2);
	}
	
	public void setImage(String img){
		try{	
			this.img = ImageIO.read(new File("Resources/Sprites/Backgrounds/"+img+".png"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
