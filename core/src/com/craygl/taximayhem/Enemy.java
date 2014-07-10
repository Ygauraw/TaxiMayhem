package com.craygl.taximayhem;

import com.badlogic.gdx.math.Rectangle;
import com.craygl.taximayhem.data.Assets;

public class Enemy {

	private float xPos;
	private float yPos;
	
	private float xOffset = 0f;

	public Enemy() {
		this(Assets.rng.nextInt(3) + 1);
	}

	public Enemy(int laneNum) {
		// 95 240 385

		switch (laneNum) {
		case 1:
			this.xPos = 95;
			break;
		case 2:
			this.xPos = 240;
			break;
		case 3:
			this.xPos = 385;
			break;
		default:
			this.xPos = 240;
			break;
		}
		
		this.yPos = 800;
		
		this.xOffset = (Assets.taxiTexture.getWidth() / 2);
	}

	public void update(float delta, float multiplier) {
		yPos -= delta * 300 * multiplier;
		if (yPos < 200)
			yPos -= delta * 310 * multiplier;
	}

	public void draw(TaxiMayhem game) {
		if (yPos < 200) { // Brake!!
			game.batcher.draw(Assets.taxiBrakeTexture, xPos - xOffset, yPos);
		} else {
			game.batcher.draw(Assets.taxiTexture, xPos - xOffset, yPos);
		}
		
	}

	public boolean destroy() {
		return (yPos < 0 - Assets.taxiTexture.getHeight());
	}

	public Rectangle getBoundBox() {
		return new Rectangle((xPos - Assets.taxiTexture.getWidth() / 2),
				(yPos - (Assets.taxiTexture.getHeight() / 2) + 10),
				(Assets.taxiTexture.getWidth()),
				(Assets.taxiTexture.getHeight()) - 20);
	}
}
