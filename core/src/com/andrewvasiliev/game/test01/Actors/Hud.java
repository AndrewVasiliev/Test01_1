package com.andrewvasiliev.game.test01.Actors;

import com.andrewvasiliev.game.test01.Classes.MyCell;
import com.andrewvasiliev.game.test01.Screens.TestMainField;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.VertexArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

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
}
