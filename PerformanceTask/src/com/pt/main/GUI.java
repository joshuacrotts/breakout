package com.pt.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.joshuacrotts.standards.StandardGame;
import com.joshuacrotts.standards.StandardHandler;

public class GUI {

	@SuppressWarnings("unused")
	private Game game;
	@SuppressWarnings("unused")
	private StandardHandler stdHandler;
	
	public static int lives = 3;
	public static int score = 0;
	
	
	public GUI(StandardGame game, StandardHandler stdHandler){
		this.stdHandler = stdHandler;
		this.game = (Game) game;
	}
	
	public void tick(){
		
	}
	
	public void render(Graphics2D g2){
		
		Color old = g2.getColor();
		Font oldF = g2.getFont();
		
		Font smallerFont = Menu.returnFont.deriveFont(14f);
		
		g2.setFont(smallerFont);
		g2.setColor(Color.WHITE);
		g2.drawString("Score: "+score, 30, 45);
		g2.drawString("Lives: "+lives, 670, 45);
		
		g2.setColor(old);
		g2.setFont(oldF);
		
	}
}
