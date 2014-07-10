package com.craygl.taximayhem.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.craygl.taximayhem.AndroidCamera;
import com.craygl.taximayhem.TaxiMayhem;
import com.craygl.taximayhem.data.Achievements;
import com.craygl.taximayhem.data.Assets;

public class GameOverScreen implements Screen {
	private TaxiMayhem game;
	private OrthographicCamera cam;

	private float roadBackgroundY = 0f;
	private float playerSpeed = 100f;

	private int score = 0;

	private Rectangle playAgainBounds;
	private Rectangle mainMenuBounds;

	private Vector3 touchPoint;

	public GameOverScreen(TaxiMayhem game, int score) {
		this.game = game;
		cam = new AndroidCamera(TaxiMayhem.WIDTH, TaxiMayhem.HEIGHT);
		cam.position.set(TaxiMayhem.WIDTH / 2, TaxiMayhem.HEIGHT / 2, 0);
		cam.update();

		this.score = score;

		if (score > Assets.highScore) {
			Assets.highScore = score;
		}

		touchPoint = new Vector3();

		playAgainBounds = new Rectangle((TaxiMayhem.WIDTH / 2) - 100, 300 - 36,
				200, 36);
		mainMenuBounds = new Rectangle((TaxiMayhem.WIDTH / 2) - 100, 220 - 36,
				200, 36);

		updateHighscore();
	}

	@Override
	public void render(float delta) {
		update(delta);
		draw();
	}

	private void update(float delta) {
		if (Gdx.input.isKeyPressed(Keys.BACK)) {
			Assets.playSound(Assets.screenChangeSound);
			game.setScreen(new MainMenuScreen(game));
		}

		// Road moving
		roadBackgroundY -= delta * playerSpeed;
		if (roadBackgroundY <= -800)
			roadBackgroundY = 0f; // move road to loop

		if (Gdx.input.justTouched()) {
			cam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (playAgainBounds != null) {
				if (playAgainBounds.contains(touchPoint.x, touchPoint.y)) {
					Assets.playSound(Assets.screenChangeSound);
					game.setScreen(new GameScreen(game));
				} else if (mainMenuBounds.contains(touchPoint.x, touchPoint.y)) {
					Assets.playSound(Assets.screenChangeSound);
					game.setScreen(new MainMenuScreen(game));
				}
			}
		}
	}

	public void updateHighscore() {
		float fileScore = 0;
		FileHandle file = Gdx.files.local("savefile/taximayhemhighscore.txt");
		if (file.exists()) {
			fileScore = Integer.valueOf(file.readString());
		}
		// Write
		if (Assets.highScore > fileScore) {
			file.writeString(Integer.toString(Assets.highScore), false);

			addToLeaderboard();
		}
	}

	private void addToLeaderboard() {
		if (game.actionResolver.getSignedInGPGS()) {
			game.actionResolver.submitScoreGPGS(score, false);
			if (score >= 50)
				game.actionResolver
						.unlockAchievementGPGS(Achievements.START_YOUR_ENGINES);
			if (score >= 100)
				game.actionResolver
						.unlockAchievementGPGS(Achievements.YOU_TAXI_RANKED_UP);
			if (score >= 150)
				game.actionResolver
						.unlockAchievementGPGS(Achievements.THATS_A_FARE_SCORE);
			if (score >= 200)
				game.actionResolver
						.unlockAchievementGPGS(Achievements.CABTASTIC);
			if (score >= 250)
				game.actionResolver
						.unlockAchievementGPGS(Achievements.OFF_THE_METER);
		}
	}

	private void draw() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batcher.setProjectionMatrix(cam.combined);
		game.batcher.setColor(1, 1, 1, 1);

		game.batcher.disableBlending();
		game.batcher.begin();
		game.batcher.draw(Assets.roadBackground, 0, roadBackgroundY);
		game.batcher.draw(Assets.roadBackground, 0, roadBackgroundY + 800);
		game.batcher.end();

		// Foreground
		game.batcher.enableBlending();
		game.batcher.begin();

		// Draw score text
		game.batcher.setColor(1, 1, 1, 1);
		Assets.gameFont.setColor(1, 1, 1, 1);
		float scoreTextLength = Assets.gameFont.getBounds("Score").width;
		float scoreLength = Assets.gameFont.getBounds("" + score).width;

		Assets.gameFont.draw(game.batcher, "Score", (TaxiMayhem.WIDTH / 2)
				- (scoreTextLength / 2), 600);
		Assets.gameFont.draw(game.batcher, "" + score, (TaxiMayhem.WIDTH / 2)
				- (scoreLength / 2), 560);

		// Draw highscore text
		float highScoreTextLength = Assets.gameFont.getBounds("Highscore").width;
		float highScoreLength = Assets.gameFont
				.getBounds("" + Assets.highScore).width;

		Assets.gameFont.draw(game.batcher, "Highscore", (TaxiMayhem.WIDTH / 2)
				- (highScoreTextLength / 2), 480);
		Assets.gameFont.draw(game.batcher, "" + Assets.highScore,
				(TaxiMayhem.WIDTH / 2) - (highScoreLength / 2), 440);

		// Draw buttons
		float playAgainLength = Assets.gameFont.getBounds("Play Again").width;
		float mainMenuLength = Assets.gameFont.getBounds("Main Menu").width;

		Assets.gameFont.draw(game.batcher, "Play Again", (TaxiMayhem.WIDTH / 2)
				- (playAgainLength / 2), 300);
		Assets.gameFont.draw(game.batcher, "Main Menu", (TaxiMayhem.WIDTH / 2)
				- (mainMenuLength / 2), 220);

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
