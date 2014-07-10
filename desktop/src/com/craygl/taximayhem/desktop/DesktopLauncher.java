package com.craygl.taximayhem.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.craygl.taximayhem.TaxiMayhem;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = TaxiMayhem.TITLE;
		config.width = TaxiMayhem.WIDTH; // sets window width
		config.height = TaxiMayhem.HEIGHT; // sets window height
		new LwjglApplication(new TaxiMayhem(new ActionResolverDesktop()), config);
	}
}
