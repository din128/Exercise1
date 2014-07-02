package com.pennypop.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * This is where you screen code will go, any UI should be in here
 * 
 * @author Dan Lee Jo
 */
public class GameScreen implements Screen {
	// Constants
	private final int WIDTH = 1280;
	private final int HEIGHT = 720;
	private final int NUMXSQUARE = 7;
	private final int NUMYSQUARE = 6;
	private final int SQUARESIZE = 100;
	
	private final Stage stage;
	private final SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;
		
	public GameScreen() {
		spriteBatch = new SpriteBatch();
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
				false, spriteBatch);	
		shapeRenderer = new ShapeRenderer();
		
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		stage.dispose();
	}

	@Override
	public void render(float delta) {
		createGrid(NUMXSQUARE, NUMYSQUARE);				
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, false);
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void pause() {
		// Irrelevant on desktop, ignore this
	}

	@Override
	public void resume() {
		// Irrelevant on desktop, ignore this
	}
	
	private void createGrid (int x, int y) {
		shapeRenderer.begin(ShapeType.Rectangle);	
		shapeRenderer.setColor(Color.BLACK);
		for (int j = 0; j < y; j++) {
			for (int i = 0; i < x; i++) {
				shapeRenderer.rect(SQUARESIZE*i, HEIGHT - SQUARESIZE - SQUARESIZE*j, SQUARESIZE, SQUARESIZE);			
			}
		}
		shapeRenderer.end();	
	}
}
