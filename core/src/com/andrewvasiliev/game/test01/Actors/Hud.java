package com.andrewvasiliev.game.test01.Actors;

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

/**
 * Created by ava on 29.12.16.
 */

public class Hud extends Actor {
    private float hudWidth, hudHeight, leftX, leftY;
    private float diametr;
    private Vector2 colorButton[];
    private TestMainField locScreen;


    public Hud(TestMainField testMainField, float x, float y, float hudWidth, float hudHeight) {
        this.hudHeight = hudHeight;
        this.hudWidth = hudWidth;
        leftX = x;
        leftY = y;
        locScreen = testMainField;

        diametr = hudHeight*0.9f; //размер цветных кнопок
        colorButton = new Vector2[MyCell.ColorCount];
        for (int i=0; i< MyCell.ColorCount; i++) {
            colorButton[i] = new Vector2();
            colorButton[i].y = leftY /*+ hudHeight/2.0f*/;
            colorButton[i].x = leftX + (hudWidth - diametr * MyCell.ColorCount)/2 + i * diametr + diametr/2;
        }

        //установим размеры нашего интерфейса (только тут будут срабатывать нажатия)
        setBounds(leftX, leftY, hudWidth, hudHeight);

        this.addListener(new InputListener() {
            // a - 29, w - 51, d - 32, s - 47

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //проверим нажали ли мы на цветную кнопку (проверим по простому - квадратную область)
                for (int i=0; i< MyCell.ColorCount; i++) {
                    if (x>=(colorButton[i].x-diametr/2) && x<=(colorButton[i].x+diametr/2) &&
                            y>=(colorButton[i].y-diametr/2) && )
                    colorButton[i].y = leftY /*+ hudHeight/2.0f*/;
                    colorButton[i].x = leftX + (hudWidth - diametr * MyCell.ColorCount)/2 + i * diametr + diametr/2;
                }


                Gdx.app.log("Touch", "touch down");
                System.out.println("touch");
                return true;
            }
        });



    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //super.draw(batch, parentAlpha);

        //deltaTime = Gdx.graphics.getDeltaTime();
        locScreen.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i=0; i< MyCell.ColorCount; i++) {
            locScreen.shapeRenderer.setColor(MyCell.colorArr[i]);
            locScreen.shapeRenderer.circle(colorButton[i].x, colorButton[i].y, diametr*0.9f/2);
        }
        locScreen.shapeRenderer.end();

    }

    public void dispose()   {

    }
}
