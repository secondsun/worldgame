package net.saga.worldgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;

public class WorldGameGame extends Game implements ApplicationListener {
	
	private net.saga.worldgame.MainMenuScreen mainMenuScreen;
	
	@Override
	public void create() {		
		Assets.load();
		mainMenuScreen = new MainMenuScreen(this);
		setScreen(mainMenuScreen);
	}
}
