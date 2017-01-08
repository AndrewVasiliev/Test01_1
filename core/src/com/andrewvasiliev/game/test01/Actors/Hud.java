package com.andrewvasiliev.game.test01.Actors;

import com.andrewvasiliev.game.test01.Classes.Const;
import com.andrewvasiliev.game.test01.Classes.MyCell;
import com.andrewvasiliev.game.test01.Screens.TestMainField;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.VertexArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Created by ava on 29.12.16.
 */

public class Hud extends Actor {
    private float hudWidth, hudHeight, leftX, leftY;
    private float diametrV, diametrH;
    private Vector2 colorButton[];
    private TestMainField locScreen;
    private ShapeRenderer shapeRenderer;



    public Hud(TestMainField testMainField, float x, float y, float hudWidthIn, final float hudHeightIn) {
        hudHeight = hudHeightIn;
        hudWidth = hudWidthIn;
        leftX = x;
        leftY = y;
        locScreen = testMainField;

        diametrV = hudHeight; //размер цветных кнопок
        diametrH =  hudWidth/ Const.ColorCount;

        colorButton = new Vector2[Const.ColorCount];
        for (int i=0; i< Const.ColorCount; i++) {
            colorButton[i] = new Vector2();
            colorButton[i].y = leftY /*+ hudHeight/2.0f*/ - diametrV/2;
            //colorButton[i].x = leftX + (hudWidth - diametrV * MyCell.ColorCount)/2 + i * diametrV + diametrV/2;
            colorButton[i].x = leftX + /*diametrH/2 +*/ i * diametrH;
        }

        //установим размеры нашего интерфейса (только тут будут срабатывать нажатия)
        setBounds(leftX, leftY, hudWidth, hudHeight);

        this.addListener(new InputListener() {
            // a - 29, w - 51, d - 32, s - 47

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //проверим нажали ли мы на цветную кнопку (проверим по простому - квадратную область)
                for (int i=0; i< Const.ColorCount; i++) {
                    if (x>=(colorButton[i].x) && x<=(colorButton[i].x+diametrH) &&
                            y>=(colorButton[i].y) && y<=(colorButton[i].y+hudHeight + diametrV/2)) {
                        //Gdx.app.log("Touch", "touch down");
                        System.out.format("%d%n", i);
                    }
                }


                //Gdx.app.log("Touch", "touch down");
                //System.out.println("touch");
                return true;
            }
        });


        //shapeRenderer = new ShapeRenderer();
        shapeRenderer = locScreen.shapeRenderer;



    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        //shapeRenderer.setProjectionMatrix(locScreen.locGame.camera.combined);
        //deltaTime = Gdx.graphics.getDeltaTime();
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i=0; i< Const.ColorCount; i++) {
            shapeRenderer.setColor(Const.colorArr[i]);
            shapeRenderer.ellipse(colorButton[i].x, colorButton[i].y, diametrH, diametrV*0.9f);
        }
        shapeRenderer.end();
        batch.begin();
        super.draw(batch, parentAlpha);
    }

    public void dispose()   {
        shapeRenderer.dispose();
    }
}
