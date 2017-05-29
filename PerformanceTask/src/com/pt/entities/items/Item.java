package com.pt.entities.items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.joshuacrotts.standards.StandardAnimator;
import com.joshuacrotts.standards.StandardGameObject;

public abstract class Item extends StandardGameObject{
	
	private StandardAnimator animation;
	protected ArrayList<BufferedImage> frames;
	protected byte MAX_FRAMES = 8;
	
	public static final int LARGE_RARITY = 8;
	public static final int MULTI_RARITY = 8;
	
	private boolean alive = true;
	
	private ItemList id;
	
	public Item(int x, int y){
		super(x,y, 44, 22);
		
		this.frames = new ArrayList<BufferedImage>();
		
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics2D g2);

	public boolean isAlive(){
		return this.alive;
	}
	
	public void setAlive(boolean b){
		this.alive = b;
	}
	
	public ItemList getItemID() {
		return id;
	}

	public void setItemId(ItemList id) {
		this.id = id;
	}

	public StandardAnimator getAnimation() {
		return animation;
	}

	public void setAnimation(StandardAnimator animation) {
		this.animation = animation;
	}
}
