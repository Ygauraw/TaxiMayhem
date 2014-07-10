package com.craygl.taximayhem.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.craygl.taximayhem.AndroidCamera;
import com.craygl.taximayhem.TaxiMayhem;
import com.craygl.taximayhem.data.Assets;

public class MainMenuScreen implements Screen {
	private TaxiMayhem game;
	
	private OrthographicCamera cam;
	
	public static final String TITLE_STRING = "TAXI MAYHEM";
	private static final String PLAY_STRING = "- Play";
	private static final String OPTIONS_STRING = "- Options [?]";
	private static final String EXIT_STRING = "- Exit";
	
	private float playerSpeed = 100f;
	
	private TextBounds titleTextBounds;

	private Rectangle playClickBounds;
	private Rectangle leaderboardClickBounds;
	private Rectangle achievementClickBounds;
	private Rectangle optionsClickBounds;
	private Rectangle exitClickBounds;

	private Rectangle taxiClickBounds;
	
	private Vector3 touchPoint;
	
	private float menuTextAlpha = 0f; // fade menu in
	
	public MainMenuScreen(TaxiMayhem game) {
		this(game, true);
	}
	
	public MainMenuScreen(TaxiMayhem game, boolean animate) {
		this.game = game;
		
		cam = new AndroidCamera(TaxiMayhem.WIDTH, TaxiMayhem.HEIGHT);
		cam.position.set(TaxiMayhem.WIDTH / 2, TaxiMayhem.HEIGHT / 2, 0);
		
		cam.update();
		
		if (!animate) {
			menuTextAlpha = 1;
		}
		
		titleTextBounds = Assets.menuTitleFont.getBounds(TITLE_STRING);

		playClickBounds = new Rectangle(30, 540, 160, 44);
		leaderboardClickBounds = new Rectangle(30, 445, 360, 44);
		achievementClickBounds = new Rectangle(30, 350, 360, 44);
		optionsClickBounds = new Rectangle(30, 255, 280, 44);
		exitClickBounds = new Rectangle(30, 160, 160, 44);
		
		touchPoint = new Vector3();
		
	}
	
	@Override
	public void render(float delta) {
		update(delta);
		draw();
	}
	
	private void update(float delta) {
		// ANIMATION
		if (menuTextAlpha < 1) {
			menuTextAlpha += (delta / 2);
		}
		if (menuTextAlpha > 1) {
			menuTextAlpha = 1;
		}
		
		// INPUT HANDLING
		
		Assets.menuBackgroundPos -= delta * playerSpeed;
		if (Assets.menuBackgroundPos <= -800)
			Assets.menuBackgroundPos = 0f; // move road to loop
		
		
		if (Gdx.input.justTouched()) {
			cam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			
			//System.out.println("X: " + touchPoint.x + " Y: " + touchPoint.y);
			
			//touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			
			if (playClickBounds.contains(touchPoint.x, touchPoint.y)) { // Clicked Play
				Assets.playSound(Assets.screenChangeSound);
				game.setScreen(new GameScreen(game));
			} else if (leaderboardClickBounds.contains(touchPoint.x, touchPoint.y)) { // Clicked Exit
				if (game.actionResolver.getSignedInGPGS()) {
					game.actionResolver.getLeaderboardGPGS(false);
				} else {
					game.actionResolver.loginGPGS();
				}
			} else if (achievementClickBounds.contains(touchPoint.x, touchPoint.y)) { // Clicked Exit
				if (game.actionResolver.getSignedInGPGS()) {
					game.actionResolver.getAchievementsGPGS();
				} else {
					game.actionResolver.loginGPGS();
				}
			} else if (optionsClickBounds.contains(touchPoint.x, touchPoint.y)) { // Clicked Options
				Assets.playSound(Assets.screenChangeSound);
				game.setScreen(new OptionsScreen(game));
			} else if (exitClickBounds.contains(touchPoint.x, touchPoint.y)) { // Clicked Exit
				Gdx.app.exit();
			}
		}
	}
	
	private void draw() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batcher.setProjectionMatrix(cam.combined);
		
		game.batcher.setColor(1,1,1,1f);
		
		game.batcher.disableBlending();
		game.batcher.begin();
		game.batcher.draw(Assets.roadBackground, 0, Assets.menuBackgroundPos);
		game.batcher.draw(Assets.roadBackground, 0, Assets.menuBackgroundPos + 800);
		game.batcher.end();

		game.batcher.enableBlending();
		game.batcher.begin();
		game.batcher.draw(Assets.menuOverlay, -15, 0, 500, 800); // stretch by 30 pixels
		
		game.batcher.setColor(1, 1, 1, 1);
		//Assets.menuBodyFont.setColor(.2f, .2f, .2f, menuTextAlpha);
		
		Assets.menuTitleFont.draw(game.batcher, TITLE_STRING, (TaxiMayhem.WIDTH / 2) - (titleTextBounds.width / 2), 700);
		
		Assets.menuBodyFont.draw(game.batcher, PLAY_STRING, 62 + 10, 580);
		Assets.menuBodyFont.draw(game.batcher, "- Leaderboards", 62 + 10, 485);
		Assets.menuBodyFont.draw(game.batcher, "- Achievements", 62 + 10, 390);
		Assets.menuBodyFont.draw(game.batcher, OPTIONS_STRING, 62 + 10, 295);
		Assets.menuBodyFont.draw(game.batcher, EXIT_STRING, 62 + 10, 200);
		
		game.batcher.end();	
	}
	
	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		Gdx.input.setCatchBackKey(false);
	}

	@Override
	public void hide() {
		Gdx.input.setCatchBackKey(true);
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
