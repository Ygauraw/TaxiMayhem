package com.craygl.taximayhem.data;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	// Whole game
	public static Music menuMusic;
	public static Random rng;
	public static int highScore;
	
	public static float menuBackgroundPos;

	// Main Menu

	public static BitmapFont menuTitleFont;
	public static BitmapFont menuBodyFont;

	public static Sound screenChangeSound;
	public static Sound carHornSound;
	
	public static Texture menuOverlay;

	// Game screen
	public static BitmapFont gameFont;
	public static Texture roadBackground;

	public static Texture taxiTexture;
	public static Texture taxiBrakeTexture;

	public static Texture explosionTexture;
	public static Animation explosionAnimation;
	public static Sound explosionSound;
	
	public static Texture cracksTexture;
	public static TextureRegion crack1;
	public static TextureRegion crack2;

	// Settings
	public static boolean isMuted;

	public static void load() {
		rng = new Random();
		highScore = 0;
		
		menuOverlay = loadTexture("img/menuOverlay.png");
		menuBackgroundPos = 0;
		
		FileHandle file = Gdx.files.local("savefile/taximayhemhighscore.txt");
		if (file.exists()) {
			highScore = Integer.valueOf(file.readString());
		}

		menuTitleFont = new BitmapFont(
				Gdx.files.internal("fonts/menuTitleFont.fnt"),
				Gdx.files.internal("fonts/menuTitleFont.png"), false);
		menuBodyFont = new BitmapFont(
				Gdx.files.internal("fonts/menuBodyFont.fnt"),
				Gdx.files.internal("fonts/menuBodyFont.png"), false);
		gameFont = new BitmapFont(
				Gdx.files.internal("fonts/regFont.fnt"),
				Gdx.files.internal("fonts/regFont.png"), false);

		screenChangeSound = Gdx.audio.newSound(Gdx.files
				.internal("sounds/screenChangeSound.wav"));
		carHornSound = Gdx.audio.newSound(Gdx.files
				.internal("sounds/carHornSound.wav"));

		menuMusic = Gdx.audio.newMusic(Gdx.files
				.internal("sounds/KubbiOverworld.ogg"));
		menuMusic.setLooping(true);
		menuMusic.setVolume(0.5f);

		roadBackground = loadTexture("img/road.png");

		taxiTexture = loadTexture("img/taxiTexture.png");
		taxiBrakeTexture = loadTexture("img/taxiBrake.png");

		explosionTexture = loadTexture("img/explosionSpritesheet.png");

		TextureRegion[] explosionTR = new TextureRegion[49];
		for (int i = 0; i < explosionTR.length; i++) {
			explosionTR[i] = new TextureRegion(explosionTexture, (i % 7) * 128,
					(i / 7) * 128, 128, 128);
		}

		explosionAnimation = new Animation(0.08f, explosionTR);
		explosionSound = Gdx.audio.newSound(Gdx.files
				.internal("sounds/explosionSound.wav"));
		
		cracksTexture = loadTexture("img/cracksTexture.png");
		crack1 = new TextureRegion(cracksTexture, 0, 0, 128, 128);
		crack2 = new TextureRegion(cracksTexture, 128, 0, 128, 128);

		isMuted = true;
	}

	public static void toggleMute() {
		isMuted = !isMuted;
		if (!isMuted) {
			menuMusic.play();
		} else {
			menuMusic.pause();
		}
	}

	public static void playSound(Sound sound) {
		playSound(sound, 1);
	}

	public static void playSound(Sound sound, float volume) {
		if (!isMuted)
			sound.play(volume);
	}

	public static Texture loadTexture(String file) {
		return new Texture(Gdx.files.internal(file));
	}
}
