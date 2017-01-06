package com.andrewvasiliev.game.test01;

import com.andrewvasiliev.game.test01.Screens.MainMenu;
import com.andrewvasiliev.game.test01.Screens.TestMainField;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MyGdxGame extends Game {
    public int iWidthMeter, iHeightMeter;
	public Screen gameScreen, mainMenu;
    public Skin skin;


	
	@Override
	public void create () {
        //iWidth = Gdx.graphics.getWidth();
        //iHeight = Gdx.graphics.getHeight();
        iWidthMeter = 1600;
        iHeightMeter = 900;


        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        //generator.DEFAULT_CHARS
        parameter.characters += 'АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя';
        float ratio = Gdx.graphics.getWidth() / 960f;
        parameter.size = 48 * (int)ratio;
        BitmapFont skinFont = generator.generateFont(parameter);
        //skin.addRegions(TextureAtlas(Gdx.files.internal("skin.atlas")));
        //skin.add("normaltext", skinFont);
        //skin.load(Gdx.files.internal("skin.json"))



        gameScreen = new TestMainField(this);
        mainMenu = new MainMenu(this);


		this.setScreen(gameScreen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
        super.dispose();
        skin.dispose();
	}
}
