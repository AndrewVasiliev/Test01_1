package com.andrewvasiliev.game.test01.Actors;

import com.andrewvasiliev.game.test01.Classes.BaseCell;
import com.andrewvasiliev.game.test01.Classes.Const;
import com.andrewvasiliev.game.test01.Classes.MyCell;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import java.util.Random;

/**
 * Created by Andrew on 12.03.2017.
 */

public class BackgroundField  extends BaseField {

    public BackgroundField(ShapeRenderer inSR, float x, float y, float width, float height) {
        super (inSR, x, y, width, height, false);

        SetSolidMode(true);
        setBounds(x, y, width, height);

        addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                System.out.println("touchDown " + x + ", " + y);
                TouchDown(x, y);

            }

            public boolean longPress (Actor actor, float x, float y) {
                System.out.println("long press " + x + ", " + y);
                return true;
            }

            public void fling (InputEvent event, float velocityX, float velocityY, int button) {
                System.out.println("fling " + velocityX + ", " + velocityY);
            }

            public void zoom (InputEvent event, float initialDistance, float distance) {
                System.out.println("zoom " + initialDistance + ", " + distance);
            }
        });
    }

    private void TouchDown(float _x, float _y) {
        //найдем, примерно, в какую ячейку кликнули
        float cellHalfWidth = cellWidth / 2;
        float cellHalfHeight = cellHeight / 2;
        int idx = -1;
        for (int i=0; i<cells.length; i++) {
            if ((cells[i].x - cellHalfWidth <= _x) && (cells[i].x + cellHalfWidth >= _x)
                    && (cells[i].y - cellHalfHeight <= _y) && (cells[i].y + cellHalfHeight >= _y)) {
                idx = i;
                break;
            }
        }
        if (idx == -1) {
            return;
        }
        cells[idx].phaseIdx = 0;
    }

}
