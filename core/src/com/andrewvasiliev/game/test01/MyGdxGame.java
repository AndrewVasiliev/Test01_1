package com.andrewvasiliev.game.test01;

import com.andrewvasiliev.game.test01.Screens.TestMainField;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends Game {
    public int iWidthMeter, iHeightMeter;


	
	@Override
	public void create () {
        //iWidth = Gdx.graphics.getWidth();
        //iHeight = Gdx.graphics.getHeight();
        iWidthMeter = 1600;
        iHeightMeter = 900;

		this.setScreen(new TestMainField(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
	}
}
