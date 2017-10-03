package com.andrewvasiliev.game.test01;

import com.andrewvasiliev.game.test01.Classes.Const;
import com.andrewvasiliev.game.test01.Classes.Player;
import com.andrewvasiliev.game.test01.Screens.MainMenu;
import com.andrewvasiliev.game.test01.Screens.GameFieldScreen;
import com.andrewvasiliev.game.test01.Screens.OptionsScreen;
import com.andrewvasiliev.game.test01.Screens.PreStartMenu;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Locale;

//Todo: возможно для отрисовки использовать PolygonRegion (https://stackoverflow.com/questions/15733442/drawing-filled-polygon-with-libgdx и https://gamedev.stackexchange.com/questions/108476/how-to-draw-a-polygon-with-picture-in-libgdx)
//Todo: может, если все соседи твоего цвета, то вместо ячейки рисовать квадрат размером 100%, чтобы меньше треугольников было? а то фпс падает чем больше захватываешь
//Todo: + изменить расположение для треугольных ячеек (сделать в виде цветка)
//Todo: + сделать какой-то базовый класс для генерации и рисования поля
//Todo: +- сделать слияние областей одного цвета (есть маленькие уголки)
//Todo: анимация в главном меню
//Todo: анимация на игровом поле
//Todo: локализация
//Todo: меню настроек (язык, звук)
//Todo: звуки/музыка
//Todo: проверить работу на разных экранах/устройствах
//Todo: устаканить размеры полей (s,m,l,xl)
//Todo: AI
//Todo: + возможно стартовые позиции лучше поставить не в углах, а по середине вверху и внизу
//Todo: сохранять введенные имена игроков в настройках


public class MyGdxGame extends Game {
    public int iWidthMeter, iHeightMeter;
	public GameFieldScreen gameScreen;
    public PreStartMenu preStartMenu;
    public OptionsScreen OptionsMenu;
    public Screen mainMenu;
    public Skin skin;
    public OrthographicCamera camera;
    public Viewport view;
    public Player plr[]; //массив игроков
    public int maxPlr = 2; //максимальное количество игроков в игре
    public ShapeRenderer sr;
    public PolygonSpriteBatch psb;
    public boolean UsePolygon;
    public I18NBundle StrRes;
    public Preferences Settings;



	@Override
	public void create () {
        UsePolygon = false;
        sr = new ShapeRenderer();
        if (UsePolygon) {
            psb = new PolygonSpriteBatch();
        }

        //вариант с использованием всей площади устройства
        //iWidthMeter = Gdx.graphics.getWidth();
        //iHeightMeter = Gdx.graphics.getHeight();

        //вариант приведения размеров к 16x9
        iWidthMeter = Gdx.graphics.getWidth();
        iHeightMeter = iWidthMeter*9/16;

        //вариант установки виртуального разрешения и его масштабирования к клиентскому
        //iWidthMeter = 1600;
        //iHeightMeter = 900;

        camera = new OrthographicCamera(iWidthMeter, iHeightMeter);
        view = new FitViewport(iWidthMeter, iHeightMeter, camera);

        skin = new Skin();
        skin.addRegions(new TextureAtlas("skin/ui-orange.atlas"));

//        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("skin/GTRS_RtpA.ttf"));
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("skin/Scada_bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        //generator.DEFAULT_CHARS
        parameter.characters += "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюяÄäÖöÜüßÑñóáéú";
        float ratio = Gdx.graphics.getWidth() / 960f;

        parameter.size = (int)(48 * ratio);
        BitmapFont menuFont = generator.generateFont(parameter);
        skin.add("menuFont", menuFont);

        parameter.size = (int)(32 * ratio);
        //parameter.borderColor = Color.BLACK;
        parameter.color = Color.WHITE;
        //parameter.borderColor = Color.BLACK;
        //parameter.borderWidth = 1;
        parameter.borderColor = Color.valueOf("00000080");
        parameter.borderWidth = 2f;
        BitmapFont normalFont = generator.generateFont(parameter);
        skin.add("normalFont", normalFont);

        skin.load(Gdx.files.internal("skin/uiskin.json"));


        Settings = Gdx.app.getPreferences(Const.PreferencesName);
        // посмотрим какой язык указанн в настройках
        String Lang = Settings.getString("Language","");
//Lang = "ru";
        FileHandle baseFileHandle = Gdx.files.internal("i18n/StrRes");
        // если в настройках пусто, то пытаемся подгрузить локализацию текущего языка Android
        Locale locale = new Locale(Lang.equals("") ? Locale.getDefault().getLanguage() : Lang );
        StrRes = I18NBundle.createBundle(baseFileHandle, locale);
        // после загрузки языковых ресурсов, посмотрим какой язык загрузился
        if (locale.getLanguage().equals("")) {
            //загрузились ресурсы по-умолчанию, значит текущего языка Android нет в ресурсах
            //установим в настройках английский язык, т.к. он в файле по-умолчанию
            Settings.putString("Language", "en");
            Settings.flush();
        } else if (!Lang.equals(locale.getLanguage())) {
            //т.к. язык, сохраненный в настройках не совпадает с загруженным, то сохраним новый язык
            Settings.putString("Language", locale.getDefault().getLanguage());
            Settings.flush();
        }


        plr = new Player[maxPlr];
        for (int i=0; i<maxPlr; i++)
            plr[i] = new Player();
        //plr[0].SetPlayer("Игрок 1", 1, false, 0, 3);
        //plr[1].SetPlayer("Android", 1, true, 4, 4);

        Gdx.input.setCatchBackKey(true);

        mainMenu = new MainMenu(this);
        gameScreen = new GameFieldScreen(this);
        preStartMenu = new PreStartMenu(this);
        OptionsMenu = new OptionsScreen(this);

        //gameScreen.StartGame();
		this.setScreen(mainMenu);
	}

	@Override
	public void render () {
		super.render();

	}
	
	@Override
	public void dispose () {
        //skin.dispose();
        sr.dispose();
        super.dispose();
	}
}
