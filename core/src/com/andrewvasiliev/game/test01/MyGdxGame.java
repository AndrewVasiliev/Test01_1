package com.andrewvasiliev.game.test01;

import com.andrewvasiliev.game.test01.Actors.BackgroundActor;
import com.andrewvasiliev.game.test01.Classes.Player;
import com.andrewvasiliev.game.test01.Screens.MainMenu;
import com.andrewvasiliev.game.test01.Screens.TestMainField;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MyGdxGame extends Game {
    public int iWidthMeter, iHeightMeter;
	public TestMainField gameScreen;
    public Screen mainMenu;
    public Skin skin;
    public OrthographicCamera camera;
    public Viewport view;
    public Player plr[]; //массив игроков
    public int maxPlr = 2; //максимальное количество игроков в игре




	@Override
	public void create () {
        //iWidth = Gdx.graphics.getWidth();
        //iHeight = Gdx.graphics.getHeight();
        iWidthMeter = 1600;
        iHeightMeter = 900;

        camera = new OrthographicCamera(iWidthMeter, iHeightMeter);
        view = new FitViewport(iWidthMeter, iHeightMeter, camera);

        skin = new Skin();
        skin.addRegions(new TextureAtlas("skin/ui-orange.atlas"));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("skin/GTRS_RtpA.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        //generator.DEFAULT_CHARS
        parameter.characters += "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя";
        float ratio = Gdx.graphics.getWidth() / 960f;

        parameter.size = (int)(/*48*/96 * ratio);
        BitmapFont menuFont = generator.generateFont(parameter);
        skin.add("menuFont", menuFont);

        parameter.size = (int)(48 * ratio);
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        BitmapFont normalFont = generator.generateFont(parameter);
        skin.add("normalFont", normalFont);

        skin.load(Gdx.files.internal("skin/uiskin.json"));



        plr = new Player[maxPlr];
        for (int i=0; i<maxPlr; i++)
            plr[i] = new Player();

        mainMenu = new MainMenu(this);
        gameScreen = new TestMainField(this);

		this.setScreen(gameScreen);
	}

	@Override
	public void render () {
		super.render();

	}
	
	@Override
	public void dispose () {
        //skin.dispose();
        super.dispose();
	}
}
