package com.andrewvasiliev.game.test01.Actors;

import com.andrewvasiliev.game.test01.Classes.MyCell;
import com.andrewvasiliev.game.test01.Screens.TestMainField;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by ava on 23.12.16.
 */

public class GameField extends Actor {
    private float leftX, leftY, widthX, heightY, cellWidth, cellHeight;
    private int countCol, countRow;
    private TestMainField locScreen;
    private MyCell cells[];

    public GameField(TestMainField testMainField, float x, float y, float width, float height, int countColIn, int countRowIn) {
        leftX = x;
        leftY = y;
        widthX = width;
        heightY = height;
        countCol = countColIn;
        countRow = countRowIn;
        locScreen = testMainField;

        cellWidth = widthX/countCol;
        cellHeight = heightY/countRow;

        cells = new MyCell[countCol*countRow];
        for (int i=0; i<countRow; i++) {
            for (int j=0; j<countCol; j++) {
                cells[GetIndex(j, i)] = new MyCell();
                cells[GetIndex(j, i)].setPosition(
                        leftX + (float)j * cellWidth + cellWidth/2.0f,
                        leftY + heightY - (float)i * cellHeight - cellHeight/2.0f );
            }
        }

    }

    private int GetIndex (int col, int row) {
        return row*countCol + col;
    }


    @Override
    public void draw(Batch batch, float alpha) {
        //locGroup.act(Gdx.graphics.getDeltaTime());
        locScreen.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        locScreen.shapeRenderer.setColor(0 / 255.0f, 255 / 255.0f, 255 / 255.0f, 1);
//        locScreen.shapeRenderer.rect(leftX, leftY, widthX, heightY);
        for (int i=0; i<countRow; i++) {
            for (int j=0; j<countCol; j++) {
                locScreen.shapeRenderer.rect(
                        cells[GetIndex(j, i)].x-cellWidth/10f*4f,
                        cells[GetIndex(j, i)].y-cellHeight/10f*4f,
                        cellWidth/10f*8f,
                        cellHeight/10f*8f);
            }
        }


        locScreen.shapeRenderer.end();

    }
}
