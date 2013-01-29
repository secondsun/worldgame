/*
 * Copyright 2011 Rod Hyde (rod@badlydrawngames.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package net.saga.worldgame;

import static net.saga.worldgame.Assets.VIRTUAL_HEIGHT;
import static net.saga.worldgame.Assets.VIRTUAL_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlydrawngames.general.CameraHelper;
import com.badlydrawngames.general.CameraHelper.ViewportMode;
import com.badlydrawngames.general.GameScreen;
import com.badlydrawngames.general.SimpleButton;

public class MainMenuScreen extends GameScreen<WorldGameGame> {

	final String TITLE = "VERY ANGRY ROBOTS";
	final String JIBBER_JABBER = "Robots are deadly\nWalls are deadly\nDoors are deadly\nShots are deadly\n\nDestroy robots to get the high score";
	SpriteBatch spriteBatch;
	private OrthographicCamera menuCam;
	private SimpleButton playButton;
	
	private Vector3 touchPoint;
	private boolean wasTouched;

	public MainMenuScreen(WorldGameGame game) {
		super(game);
		menuCam = CameraHelper.createCamera2(ViewportMode.PIXEL_PERFECT,
				VIRTUAL_WIDTH, VIRTUAL_HEIGHT, Assets.pixelDensity);
		spriteBatch = new SpriteBatch();
		spriteBatch.setProjectionMatrix(menuCam.combined);
		touchPoint = new Vector3();
		createButtons();
	}

	private void createButtons() {
		playButton = createPlayButton();
		playButton.setWidth(VIRTUAL_WIDTH / 4);
		playButton.setHeight(VIRTUAL_HEIGHT / 4);
		playButton.bottomOn(1.0f);
		playButton.centerHorizontallyOn(VIRTUAL_WIDTH / 2);
	}

	private SimpleButton createPlayButton() {
		return new SimpleButton("Play", Assets.textFont);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		spriteBatch.begin();
		Assets.textFont.setColor(Color.WHITE);
		float y = VIRTUAL_HEIGHT - Assets.textFont.getCapHeight() / 2.0f;
		Assets.textFont.drawWrapped(spriteBatch, TITLE, 0, y, VIRTUAL_WIDTH, HAlignment.CENTER);
		Assets.textFont.drawWrapped(spriteBatch, JIBBER_JABBER, VIRTUAL_WIDTH / 8, 3 * VIRTUAL_HEIGHT / 4,
			3 * VIRTUAL_WIDTH / 4, HAlignment.CENTER);
		drawButtons();
		spriteBatch.end();
	}
	private void startGame() {
	}

	private void updateButtons(float delta) {
		touchPoint = screenToViewport(Gdx.input.getX(), Gdx.input.getY());
		boolean justTouched = Gdx.input.justTouched();
		boolean isTouched = Gdx.input.isTouched();
		boolean justReleased = wasTouched && !isTouched;
		wasTouched = isTouched;
		playButton.update(delta, justTouched, isTouched, justReleased,
				touchPoint.x, touchPoint.y);
	}

	private void drawButtons() {
		playButton.draw(spriteBatch);
	}

	private Vector3 screenToViewport(float x, float y) {
		menuCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
		return touchPoint;
	}
}
