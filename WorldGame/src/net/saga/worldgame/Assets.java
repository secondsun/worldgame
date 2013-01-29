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

import static com.badlydrawngames.general.Rectangles.setRectangle;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlydrawngames.general.CameraHelper;
import com.badlydrawngames.general.CollisionGeometry;
import com.badlydrawngames.general.Config;

public class Assets {

	private static final String TEXT_FONT = "arial.ttf";

	private static final float PLAYER_BORDER_WIDTH = Config.asFloat("Player.borderWidthPercent", 25.0f);
	private static final float PLAYER_BORDER_HEIGHT = Config.asFloat("Player.borderHeightPercent", 6.7f);
	private static final float PLAYER_FRAME_DURATION = Config.asFloat("Player.frameDuration", 0.2f);

	private static TextureAtlas atlas;

	public static final float VIRTUAL_WIDTH = 30.0f;
	public static final float VIRTUAL_HEIGHT = 20.0f;

	public static TextureRegion pureWhiteTextureRegion;
	public static TextureRegion playerWalkingRight1;
	public static TextureRegion playerWalkingRight2;
	public static TextureRegion playerWalkingLeft1;
	public static TextureRegion playerWalkingLeft2;

	public static Animation playerWalkingRightAnimation;
	public static Animation playerWalkingLeftAnimation;

	
	public static CollisionGeometry playerGeometry;

	public static BitmapFont textFont;
	

	public static float pixelDensity;
	public static float playerWidth;
	public static float playerHeight;

	public static void load () {
		pixelDensity = calculatePixelDensity();
		String textureDir = "data/textures/" + (int)pixelDensity;
		String textureFile = textureDir + "/pack";
		atlas = new TextureAtlas(Gdx.files.internal(textureFile), Gdx.files.internal(textureDir));
		loadTextures();
		createAnimations();
		loadFonts();
		loadSounds();
		initialiseGeometries();
	}

	private static void loadTextures () {
		pureWhiteTextureRegion = atlas.findRegion("8x8");
		playerWalkingRight1 = atlas.findRegion("HeroRight1");
		playerWalkingRight2 = atlas.findRegion("HeroRight2");
		playerWalkingLeft1 = atlas.findRegion("HeroLeft1");
		playerWalkingLeft2 = atlas.findRegion("HeroLeft2");
	}

	private static float calculatePixelDensity () {
		FileHandle textureDir = Gdx.files.internal("data/textures");
		FileHandle[] availableDensities = textureDir.list();
		FloatArray densities = new FloatArray();
		for (int i = 0; i < availableDensities.length; i++) {
			try {
				float density = Float.parseFloat(availableDensities[i].name());
				densities.add(density);
			} catch (NumberFormatException ex) {
				// Ignore anything non-numeric, such as ".svn" folders.
			}
		}
		densities.shrink(); // Remove empty slots to get rid of zeroes.
		densities.sort(); // Now the lowest density comes first.
		return CameraHelper.bestDensity(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, densities.items);
	}

	private static void createAnimations () {
		playerWalkingRightAnimation = new Animation(PLAYER_FRAME_DURATION, Assets.playerWalkingRight1, Assets.playerWalkingRight2);
		playerWalkingLeftAnimation = new Animation(PLAYER_FRAME_DURATION, Assets.playerWalkingLeft1, Assets.playerWalkingLeft2);

	}

	private static void loadFonts () {
		String fontDir = "data/fonts/" ;
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontDir + TEXT_FONT));

		textFont = generator.generateFont(15);
		textFont.setScale(1.0f / pixelDensity);
	}

	private static void loadSounds () {
	}

	private static Sound[] loadSounds (String dir) {
		FileHandle dh = Gdx.files.internal("data/sounds/" + dir);
		FileHandle[] fhs = dh.list();
		List<Sound> sounds = new ArrayList<Sound>();
		for (int i = 0; i < fhs.length; i++) {
			String name = fhs[i].name();
			if (name.endsWith(".ogg")) {
				sounds.add(loadSound(dir + "/" + name));
			}
		}
		Sound[] result = new Sound[0];
		return sounds.toArray(result);
	}

	private static Sound loadSound (String filename) {
		return Gdx.audio.newSound(Gdx.files.internal("data/sounds/" + filename));
	}

	private static void initialiseGeometries () {

		playerWidth = toWidth(playerWalkingRight1);
		playerHeight = toHeight(playerWalkingRight1);
		
		// TODO: The below is a complete hack just to provide the player and captain with some collision geometry
		// so that he doesn't die when he's clearly not in contact with a wall, bullet or enemy. Ideally it would
		// be generated from the bitmap, or loaded.

		// Configure player collision geometry.
		Array<Rectangle> playerRectangles = new Array<Rectangle>();
		Rectangle r = new Rectangle();
		float x = (playerWidth * PLAYER_BORDER_WIDTH / 100.0f) / 2.0f;
		float y = (playerHeight * PLAYER_BORDER_HEIGHT / 100.0f) / 2.0f;
		float w = playerWidth - 2 * x;
		float h = playerHeight - 2 * y;
		setRectangle(r, x, y, w, h);
		playerRectangles.add(r);
		playerGeometry = new CollisionGeometry(playerRectangles);

	}

	private static float toWidth (TextureRegion region) {
		return region.getRegionWidth() / pixelDensity;
	}

	private static float toHeight (TextureRegion region) {
		return region.getRegionHeight() / pixelDensity;
	}

	public static void playSound (Sound sound) {
		sound.play(1);
	}
}
