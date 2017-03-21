package com.andrewvasiliev.game.test01.Actors;

import com.andrewvasiliev.game.test01.Classes.Const;
import com.andrewvasiliev.game.test01.Screens.GameFieldScreen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Created by ava on 29.12.16.
 */

public class Hud extends Actor {
    private float hudWidth, hudHeight, leftX, leftY;
    public float diametrV, diametrH;
    public Vector2[] colorButton;
    private GameFieldScreen locScreen;
    private ShapeRenderer sr;
    public int colorIdx; // номер нажатой кнопки/цвета



    public Hud(GameFieldScreen gameFieldScreen, float x, float y, float hudWidthIn, final float hudHeightIn) {
        hudHeight = hudHeightIn;
        hudWidth = hudWidthIn;
        leftX = x;
        leftY = y;
        locScreen = gameFieldScreen;

        diametrV = hudHeight; //размер цветных кнопок
        diametrH =  hudWidth/ Const.ColorCount;

        colorButton = new Vector2[Const.ColorCount];
        for (int i=0; i< Const.ColorCount; i++) {
            colorButton[i] = new Vector2();
            colorButton[i].y = leftY - diametrV/2;
            colorButton[i].x = leftX + i * diametrH;
        }

        //установим размеры нашего интерфейса (только тут будут срабатывать нажатия)
        setBounds(leftX, leftY, hudWidth, hudHeight);

        this.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //проверим нажали ли мы на цветную кнопку (проверим по простому - квадратную область)
                System.out.println("Hud touchDown " + x + ", " + y);
                if (locScreen.hudEnabled) { //если нажатие ожидается, то....
                    for (int i = 0; i < Const.ColorCount; i++) {
                        //нельзя нажать цвета, которые сейчас заняты игроками
                        if ((locScreen.locGame.plr[0].colorIdx == i)||((locScreen.locGame.plr[1].colorIdx == i))) { continue;}
                        if (x >= (colorButton[i].x) && x <= (colorButton[i].x + diametrH) &&
                                y >= (colorButton[i].y) && y <= (colorButton[i].y + hudHeight + diametrV / 2)) {
                            //Gdx.app.log("Touch", "touch down");
                            colorIdx = i;
                            //System.out.format("%d%n", i);
                        }
                    }
                }
                return true;
            }
        });
        sr = locScreen.shapeRenderer;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
        //рисуем полосы заработанных очков
        float _scoreX = hudWidth * locScreen.locGame.plr[0].score /(float)(locScreen.locGame.plr[0].score + locScreen.locGame.plr[1].score);
        sr.setColor(Const.colorArr[locScreen.locGame.plr[0].colorIdx]);
        sr.rect(leftX, hudHeight/2f, _scoreX, hudHeight/2f);
        sr.setColor(Const.colorArr[locScreen.locGame.plr[1].colorIdx]);
        sr.rect(leftX+_scoreX, hudHeight/2f, hudWidth-_scoreX, hudHeight/2f);
        //рисуем отметки посередине полос очков
        _scoreX = hudWidth/2f+leftX;
        sr.setColor(Color.BLACK);
        sr.triangle(_scoreX, hudHeight/2f+10f,  _scoreX-10f, hudHeight/2f, _scoreX+10f, hudHeight/2f);
        sr.triangle(_scoreX, hudHeight-10f,  _scoreX-10f, hudHeight, _scoreX+10f, hudHeight);

        //рисуем цветные кнопки
        for (int i=0; i< Const.ColorCount; i++) {
            //не рисуем цвета, которые сейчас заняты игроками
            if ((locScreen.locGame.plr[0].colorIdx == i)||((locScreen.locGame.plr[1].colorIdx == i))) { continue;}

            sr.setColor(Const.colorArr[i]);
            sr.ellipse(colorButton[i].x, colorButton[i].y, diametrH, diametrV*0.9f);
        }
        sr.end();
        batch.begin();
        super.draw(batch, parentAlpha);
    }

    public void dispose()   {
        //shapeRenderer.dispose();
    }
}
