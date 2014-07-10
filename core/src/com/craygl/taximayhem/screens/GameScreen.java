package com.craygl.taximayhem.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.craygl.taximayhem.AndroidCamera;
import com.craygl.taximayhem.Crack;
import com.craygl.taximayhem.Enemy;
import com.craygl.taximayhem.TaxiMayhem;
import com.craygl.taximayhem.data.Assets;

public class GameScreen implements Screen {
	private TaxiMayhem game;
	private OrthographicCamera cam;

	private int score = 0;

	private float roadBackgroundY = 0f;
	private float playerSpeed = 800f;

	private float playerXPos = TaxiMayhem.WIDTH / 2;
	private float playerYPos = -250f; // start off the screen

	private boolean started = false;
	
	private boolean dead = false;
	private float deadTimer = 3f;

	private int laneNum = 2; // 1 left, 2 middle, 3 right

	private Vector3 touchPoint;
	private Rectangle moveLeftBounds;
	private Rectangle moveRightBounds;

	List<Crack> cracks = new ArrayList<Crack>();
	
	List<Enemy> enemies = new ArrayList<Enemy>();
	private float enemyRespawnTime = 1.2f;
	private float enemyRespawnTimer = enemyRespawnTime * 2.5f;

	private float speedMultiplier = 1f;
	private float maxSpeed = 1.685f;

	private int lastEnemyRow = -1;
	
	private float explosionStateTime = 0;

	public GameScreen(TaxiMayhem game) {
		this.game = game;

		cam = new AndroidCamera(TaxiMayhem.WIDTH, TaxiMayhem.HEIGHT);
		cam.position.set(TaxiMayhem.WIDTH / 2, TaxiMayhem.HEIGHT / 2, 0);
		cam.update();

		touchPoint = new Vector3();

		moveLeftBounds = new Rectangle(0, 0, 230, 800);
		moveRightBounds = new Rectangle(250, 0, 230, 800);

		score = 0;
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
		
		if (dead) {
			deadTimer -= delta;
			if (deadTimer <= 0 || (deadTimer <= 2 && Gdx.input.justTouched())) {
				game.setScreen(new GameOverScreen(game, score));
			}
			
			explosionStateTime += delta;
			
			return;
		}
		
		// Player Entrance
		if (!started) {
			if (playerYPos < 50) {
				playerYPos += delta * 100;
			} else {
				started = true;
			}
			if (playerYPos > 50) {
				playerYPos = 50;
			}
		}

		// Moving player
		if (Gdx.input.justTouched()) {
			cam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (started) {
				if (moveLeftBounds.contains(touchPoint.x, touchPoint.y)) { // Clicked
																			// Play
					if (laneNum > 1) {
						laneNum--;
					}
				} else if (moveRightBounds.contains(touchPoint.x, touchPoint.y)) { // Clicked
																					// Options
					if (laneNum < 3) {
						laneNum++;
					}
				}
			}
		}

		playerXPos = lerp(playerXPos, getLaneXPos(laneNum), delta * 7);

		// Enemies

		enemyRespawnTimer -= delta * speedMultiplier;
		if (enemyRespawnTimer <= 0) {
			enemyRespawnTimer = enemyRespawnTime; // reset timer
			addEnemyRow();
		}

		boolean enemiesDestroyed = false;
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).update(delta, speedMultiplier);
			if (enemies.get(i).destroy()) {
				enemies.remove(i);
				i--;
				enemiesDestroyed = true;
			}
		}
		if (enemiesDestroyed) {
			score++;
		}
		
		// Spawn and move cracks on road
		if (Assets.rng.nextInt(50) == 0) { // Spawn cracks in road
			cracks.add(new Crack());
		}
		for (int i = 0; i < cracks.size(); i++) {
			cracks.get(i).update(delta, playerSpeed);
			if (cracks.get(i).destroy()) {
				cracks.remove(i);
				i--;
			}
		}
		
		speedMultiplier += delta * 0.04;
		playerSpeed += delta * 10;
		if (speedMultiplier > maxSpeed) {
			speedMultiplier = maxSpeed;
			playerSpeed = 950;
		}
		
		if (playerColliding()) {
			dead = true;
			die();
		}
	}

	private void die() {
		playerSpeed = 0f;
		Assets.playSound(Assets.explosionSound);
	}

	private boolean playerColliding() {
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).getBoundBox().overlaps(getPlayerRect())) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unused")
	private boolean playerColliding(Rectangle r) {
		return getPlayerRect().overlaps(r);
	}

	private Rectangle getPlayerRect() {
		return new Rectangle(
				(playerXPos - Assets.taxiTexture.getWidth() / 2),
				(playerYPos - Assets.taxiTexture.getHeight() / 2),
				(Assets.taxiTexture.getWidth()),
				(Assets.taxiTexture.getHeight()));
	}

	private float lerp(float a, float b, float f) {
		return a + f * (b - a);
	}

	private float getLaneXPos(int l) {
		switch (l) {
		case 1:
			return 95;
		case 2:
			return 240;
		case 3:
			return 385;
		default:
			return 240;
		}
	}

	private void addEnemyRow() {
		int rand = Assets.rng.nextInt(9);

		if (rand == lastEnemyRow) {
			lastEnemyRow = -1;
			addEnemyRow();
			return;
		}

		lastEnemyRow = rand;

		switch (Assets.rng.nextInt(9)) {
		case 0:
			enemies.add(new Enemy(1));
			break;
		case 1:
			enemies.add(new Enemy(2));
			break;
		case 2:
			enemies.add(new Enemy(3));
			break;
		case 3:
		case 4:
			enemies.add(new Enemy(1));
			enemies.add(new Enemy(2));
			break;
		case 5:
		case 6:
			enemies.add(new Enemy(1));
			enemies.add(new Enemy(3));
			break;
		case 7:
		case 8:
			enemies.add(new Enemy(2));
			enemies.add(new Enemy(3));
			break;
		default:
			enemies.add(new Enemy(2));
			break;
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

		// Draw cracks on road
		game.batcher.setColor(1,1,1,.3f);
		for (int i = 0; i < cracks.size(); i++) {
			cracks.get(i).draw(game);
		}

		// Draw player
		if (!dead || deadTimer > 1.6f) {
			game.batcher.setColor(1, 1, 1, 1);
			game.batcher.draw(Assets.taxiTexture, playerXPos
					- (Assets.taxiTexture.getWidth() / 2), playerYPos);
		}
		
		// Draw enemies
		//game.batcher.setColor(1, .7f, 0, 1);
		game.batcher.setColor(1, 1, 1, 1);
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(game);
		}
		game.batcher.setColor(1, 1, 1, 1);
		
		
		if (dead) {
			game.batcher.draw(Assets.explosionAnimation.getKeyFrame(explosionStateTime, false), playerXPos - 128, playerYPos - 45, 256, 256);
		}

		// Draw get ready text
		if (!started) {
			Assets.menuTitleFont.draw(game.batcher, "GET READY", 75, 700);
		}

		//Assets.menuBodyFont.setColor(.2f,.2f,.2f,1);
		Assets.gameFont.setColor(1,1,1,1);
		Assets.gameFont.draw(game.batcher, "Score: " + score, 43, 780);
		Assets.gameFont.draw(game.batcher, "Hiscore: " + Assets.highScore, 235, 780);

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
