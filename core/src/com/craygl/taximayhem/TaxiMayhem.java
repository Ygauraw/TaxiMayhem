package com.craygl.taximayhem;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.craygl.taximayhem.data.Assets;
import com.craygl.taximayhem.screens.SplashScreen;

public class TaxiMayhem extends Game {
	
	public static final String TITLE = "Taxi Mayhem";
	
    public static final int WIDTH = 480;
    public static final int HEIGHT = 800; // used later to set window size
    
    public SpriteBatch batcher;
    
    public ActionResolver actionResolver;
	
    public TaxiMayhem(ActionResolver actionResolver) {
    	this.actionResolver = actionResolver;
    }
    
	@Override
	public void create () {
		batcher = new SpriteBatch();
		
		Assets.load();
		
		setScreen(new SplashScreen(this));
	}

}
