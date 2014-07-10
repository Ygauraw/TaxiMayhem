package com.craygl.taximayhem.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.craygl.taximayhem.AndroidCamera;
import com.craygl.taximayhem.TaxiMayhem;
import com.craygl.taximayhem.data.Assets;

public class SplashScreen implements Screen {
	private TaxiMayhem game;
	private OrthographicCamera cam;
	
	private Texture splashTexture = new Texture(Gdx.files.internal("img/splash.png"));
	
	private float timeTillProgress = 5f; // 5 seconds till main menu
	
	private Color c;
	
	public SplashScreen(TaxiMayhem game) {
		this.game = game;
		
		cam = new AndroidCamera(TaxiMayhem.WIDTH, TaxiMayhem.HEIGHT);
		cam.position.set(TaxiMayhem.WIDTH / 2, TaxiMayhem.HEIGHT / 2, 0);
		
		cam.update();
		
		c = new Color(1,1,1,0);
	}
	
	@Override
	public void render(float delta) {
		update(delta);
		draw();
	}

	private void update(float delta) {
		if (Gdx.input.justTouched()) {
			goToMainMenu();
		}
		
		c.lerp(new Color(1,1,1,1), delta / 1.3f);
		
		timeTillProgress -= delta;
		if (timeTillProgress <= 0) { // when the animation is finished, go to MainMenu()
			
			goToMainMenu();
		}
	}

	private void draw() {
		Gdx.gl.glClearColor(1, 1, 1, 1); // sets clear color to whites
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // clear the batch
		
		game.batcher.setProjectionMatrix(cam.combined);
		
		game.batcher.begin();
		game.batcher.setColor(c);
		game.batcher.draw(splashTexture, 0, 0);
		game.batcher.end();
		
	}

	@Override
	public void resize(int width, int height) {
		//stage.getViewport().setCamera(new AndroidCamera(TaxiMayhem.WIDTH, TaxiMayhem.HEIGHT));
		
	}

	@Override
	public void show() {
		Gdx.input.setCatchBackKey(true);
	}
	
	private void goToMainMenu() {
		Assets.toggleMute(); // Unmute at start
		game.setScreen(new MainMenuScreen(game));
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		splashTexture.dispose();
	}

}
