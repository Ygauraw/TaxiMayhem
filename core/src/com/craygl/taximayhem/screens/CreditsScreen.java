package com.craygl.taximayhem.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.craygl.taximayhem.AndroidCamera;
import com.craygl.taximayhem.TaxiMayhem;
import com.craygl.taximayhem.data.Assets;

public class CreditsScreen implements Screen {
	private TaxiMayhem game;
	private OrthographicCamera cam;

	private float playerSpeed = 100f;
	
	public CreditsScreen(TaxiMayhem game) {
		this.game = game;
		cam = new AndroidCamera(TaxiMayhem.WIDTH, TaxiMayhem.HEIGHT);
		cam.position.set(TaxiMayhem.WIDTH / 2, TaxiMayhem.HEIGHT / 2, 0);
		cam.update();
	}
	
	@Override
	public void render(float delta) {
		update(delta);
		draw();
	}
	
	private void update(float delta) {
		if (Gdx.input.isKeyPressed(Keys.BACK)) {
			Assets.playSound(Assets.screenChangeSound);
			game.setScreen(new HelpScreen(game));
		}
		
		Assets.menuBackgroundPos -= delta * playerSpeed;
		if (Assets.menuBackgroundPos <= -800)
			Assets.menuBackgroundPos = 0f; // move road to loop
		
		if (Gdx.input.justTouched()) {
			Assets.playSound(Assets.screenChangeSound);
			game.setScreen(new OptionsScreen(game));
		}
	}
	
	private void draw() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batcher.setProjectionMatrix(cam.combined);
		game.batcher.setColor(1,1,1,1);
		
		game.batcher.disableBlending();
		game.batcher.begin();
		game.batcher.draw(Assets.roadBackground, 0, Assets.menuBackgroundPos);
		game.batcher.draw(Assets.roadBackground, 0, Assets.menuBackgroundPos + 800);
		game.batcher.end();

		game.batcher.enableBlending();
		game.batcher.begin();
		game.batcher.draw(Assets.menuOverlay, -15, 0, 500, 800); // stretch by 30 pixels
		
		//Assets.menuBodyFont.setColor(.2f, .2f, .2f, 1);
		
		Assets.menuTitleFont.draw(game.batcher, "Credits", 62, 700);
		

		Assets.menuBodyFont.draw(game.batcher, "Music:", 62 + 10, 580);
		Assets.menuBodyFont.draw(game.batcher, " Kubbi - Overworld", 62 + 10, 530);
		Assets.menuBodyFont.draw(game.batcher, "Created with", 62 + 10, 455);
		Assets.menuBodyFont.draw(game.batcher, " Libgdx", 62 + 10, 405);
		Assets.menuBodyFont.draw(game.batcher, "www.craygl.com", 62 + 10, 330);
		
		
		game.batcher.end();		
	}
	
	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		
	}

}
