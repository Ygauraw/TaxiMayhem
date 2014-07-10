package com.craygl.taximayhem;

import com.craygl.taximayhem.data.Assets;

public class Crack {
	private int type;
	private float xPos;
	private float yPos;
	
	private float rotation;
	private float scale;

	public Crack() {
		xPos = Assets.rng.nextInt(420-128) + 30;
		yPos = 800;
		rotation = Assets.rng.nextInt(360);
		scale = Assets.rng.nextFloat();
		
		type = Assets.rng.nextInt(2) + 1;
	}

	public void update(float delta, float speed) {
		yPos -= delta * speed;
	}

	public void draw(TaxiMayhem game) {
		if (type == 1) {
			game.batcher.draw(Assets.crack1, xPos, yPos, 64, 64, 128, 128, scale, scale, rotation);
		} else if (type == 2) {
			game.batcher.draw(Assets.crack2, xPos, yPos, 64, 64, 128, 128, scale, scale, rotation);
		}
	}
	
	public boolean destroy() {
		return (yPos < -128);
	}
}
