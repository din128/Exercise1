package com.pennypop.project;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * This is where you screen code will go, any UI should be in here
 * 
 * @author Dan Lee Jo
 */
public class MainScreen implements Screen {

	private final Stage stage;
	private final SpriteBatch spriteBatch;
	private BitmapFont pennyFont;
	private Table UIViews;
	private Table APIResults;
	private Texture apiTexture;
	private Texture sfxTexture;
	
	
	public MainScreen() {
		spriteBatch = new SpriteBatch();
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
				false, spriteBatch);	
	
		// Create title and buttons
		UIViews = new Table();
		UIViews.setFillParent(true);
		UIViews.left();	
		createTitle();
		UIViews.row();
		createSFXButton();
		createAPIButton();	
		stage.addActor(UIViews);
		
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		stage.dispose();
		pennyFont.dispose();
		apiTexture.dispose();
		sfxTexture.dispose();
	}

	@Override
	public void render(float delta) {
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

	private void createTitle() {
		pennyFont = new BitmapFont(Gdx.files.internal("assets/font.fnt"),
				Gdx.files.internal("assets/font.png"), false);

		LabelStyle pennypopStyle = new LabelStyle (pennyFont, Color.RED);
		Label pennypopLabel = new Label ("PennyPop", pennypopStyle);
		UIViews.add(pennypopLabel);
	}
	
	private void createSFXButton() {
		sfxTexture = new Texture(Gdx.files.internal("assets/sfxButton.png"));
		Image sfxButton = new Image(sfxTexture);
		UIViews.add(sfxButton);
		
		sfxButton.addListener(new ClickListener() {
		    @Override
		    public void clicked(InputEvent event, float x, float y) {
		    	ProjectApplication.playSFX();
		    }
		});
	}
	
	private void createAPIButton() {
		apiTexture = new Texture(Gdx.files.internal("assets/apiButton.png"));
		Image apiImage = new Image(apiTexture);	
		UIViews.add(apiImage);
		
		apiImage.addListener(new ClickListener() {
		    @Override
		    public void clicked(InputEvent event, float x, float y) {
		    	// Create API results
				APIResults = new Table();
				APIResults.reset();
				APIResults.setFillParent(true);
				APIResults.right();		
				stage.addActor(APIResults);
		    	
				try {
					Color brown = new Color(0.5f,0.4f,0,1);
					LabelStyle brownStyle = new LabelStyle (pennyFont, brown);
					LabelStyle blueStyle = new LabelStyle (pennyFont, Color.BLUE);
					LabelStyle redStyle = new LabelStyle (pennyFont, Color.RED);
					
					// Extract information from JSON
					JSONObject jsonRes = ProjectApplication.getAPIResults();
					JSONObject clouds = jsonRes.getJSONObject("clouds");
					JSONObject wind = jsonRes.getJSONObject("wind");
					JSONObject main = jsonRes.getJSONObject("main");
					
					String city = jsonRes.getString("name");
					double numClouds = clouds.getDouble("all");		
					double windSpeed = wind.getDouble("speed");
					double temp = main.getDouble("temp"); // Temperature in Kelvin?				
	
					temp = Math.floor((temp * 1.8 - 459.67) * 100) / 100; // Kelvin to Farenheit... looks more reasonable
					boolean clear = false;
					if (numClouds == 0) {
						clear = true;
					} else {
						clear = false;
					}
					
					// Add data to table				
					Label currWeather = new Label ("CurrentWeather", brownStyle);
					APIResults.add(currWeather);
					APIResults.row();
			
					Label currCity = new Label (city, blueStyle);
					APIResults.add(currCity);
					APIResults.row();
		
					Label skyCondition;
					if (clear) {
						skyCondition = new Label ("Sky is clear", redStyle);		
					} else {
						skyCondition = new Label ("Sky is cloudy", redStyle);
					}
					APIResults.add(skyCondition);
					APIResults.row();
					
					String degrees = "";
					degrees = temp + " degrees, ";
					Label degreesLabel = new Label (degrees, redStyle);
					APIResults.add(degreesLabel);
					
					String windSpeedString = "";
					windSpeedString = windSpeed + "mph wind";
					Label windSpeedLabel = new Label (windSpeedString, redStyle);
					APIResults.add(windSpeedLabel);
			
				} catch (JSONException e) {
					e.printStackTrace();
				}
		    }
		});
	}
}
