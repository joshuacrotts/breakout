package com.pt.entities.items;

import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.joshuacrotts.standards.StandardAnimator;
import com.joshuacrotts.standards.StandardDraw;
import com.joshuacrotts.standards.StandardGame;
import com.joshuacrotts.standards.StandardHandler;
import com.joshuacrotts.standards.StandardID;
import com.joshuacrotts.standards.StandardParticle;
import com.joshuacrotts.standards.StdOps;
import com.pt.entities.Ball;
import com.pt.main.Game;
import com.pt.main.SongBox;

public class Multi extends Item {

	/*Items need to reference the handler directly, instead of passing a standardgame
	 *instance and having it reference the handler that way.*/
	private Game stdGame;
	private StandardHandler stdHandler;
	private SongBox songBox;

	public Multi(int x, int y, StandardGame stdGame, StandardHandler stdHandler, SongBox songBox) {
		super(x, y);

		this.setId(StandardID.Multi);

		this.stdGame = (Game) stdGame;
		this.stdHandler = stdHandler;
		this.songBox = songBox;

		for(int i = 0; i<this.MAX_FRAMES; i++){
			try {
				this.frames.add(ImageIO.read(new File("Resources/Sprites/Items/Multi/multi"+i+".png")));
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
				this.stdHandler.addEntity(new StandardParticle((int)this.getX()+(this.getWidth()/2),(int)this.getY(),200f,StandardDraw.PANSY_PURPLE,this.stdHandler,true));
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
						
						for(int j = 0; j<2; j++)
							this.stdHandler.addEntity(new Ball(StdOps.rand(300,500), StdOps.rand(200,300), this.stdGame, this.stdGame.difficulty));
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
		g2.drawImage(this.getCurrentSprite(), (int)this.getX(),(int) this.getY(), null);

	}

}
