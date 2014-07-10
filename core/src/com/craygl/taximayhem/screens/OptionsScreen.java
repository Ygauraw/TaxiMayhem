package com.craygl.taximayhem.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.craygl.taximayhem.AndroidCamera;
import com.craygl.taximayhem.TaxiMayhem;
import com.craygl.taximayhem.data.Assets;

public class OptionsScreen implements Screen {
	private TaxiMayhem game;
	private OrthographicCamera cam;
	
	private Rectangle soundClickBounds;
	private Rectangle helpClickBounds;
	private Rectangle backClickBounds;
	private Vector3 touchPoint;

	private float playerSpeed = 100f;
	
	public OptionsScreen(TaxiMayhem game) {
		this.game = game;
		
		cam = new AndroidCamera(TaxiMayhem.WIDTH, TaxiMayhem.HEIGHT);
		cam.position.set(TaxiMayhem.WIDTH / 2, TaxiMayhem.HEIGHT / 2, 0);
		
		cam.update();
		
		soundClickBounds = new Rectangle(30, 540, 300, 44);
		helpClickBounds = new Rectangle(30, 445, 360, 44);
		backClickBounds = new Rectangle(30, 350, 360, 44);
		touchPoint = new Vector3();
	}
	
	@Override
	public void render(float delta) {
		update(delta);
		draw();
	}
	
	private void update(float delta) {
		if (Gdx.input.isKeyPressed(Keys.BACK)) {
			Assets.playSound(Assets.screenChangeSound);
			game.setScreen(new MainMenuScreen(game, false));
		}
		
		Assets.menuBackgroundPos -= delta * playerSpeed;
		if (Assets.menuBackgroundPos <= -800)
			Assets.menuBackgroundPos = 0f; // move road to loop
		
		if (Gdx.input.justTouched()) {
			cam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			
			if (soundClickBounds.contains(touchPoint.x, touchPoint.y)) { // Clicked Sound
				Assets.toggleMute();
			} else if (helpClickBounds.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.screenChangeSound);
				game.setScreen(new HelpScreen(game));
			} else if (backClickBounds.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.screenChangeSound);
				game.setScreen(new MainMenuScreen(game, false));
			}
			
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
		
		Assets.menuBodyFont.setColor(Assets.isMuted ? Color.RED : new Color(.3f, .6f, .2f, 1));
		Assets.menuBodyFont.draw(game.batcher, Assets.isMuted ? "- SOUND OFF" : "- SOUND ON", 62 + 10, 580);
		
		Assets.menuBodyFont.setColor(1,1,1, 1);
		
		Assets.menuTitleFont.draw(game.batcher, "Options", 62, 700);
		
		Assets.menuBodyFont.draw(game.batcher, "- Help & Credits", 62 + 10, 485);
		Assets.menuBodyFont.draw(game.batcher, "- Back to Menu", 62 + 10, 390);
		
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
