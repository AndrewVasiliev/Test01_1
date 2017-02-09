package com.andrewvasiliev.game.test01.Actors;

import com.andrewvasiliev.game.test01.MyGdxGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.Random;

/**
 * Created by root on 05.11.15.
 */
public class BackgroundActor extends Actor {
    private final  TextureRegion starTexture;
    private static int MAXSTARSINLAYER = 30;
    private static int MAXLAYERS = 5;
    private MyGdxGame locGame;
    private float locHeight, locWight;

    private Vector2[] stars;
    float deltaX, deltaY;


    private int GetIndex (int layer, int idx) {
        return layer*MAXSTARSINLAYER + idx;
    }

    public BackgroundActor(MyGdxGame myGdxGame) {
        locGame = myGdxGame;
        locHeight = locGame.iHeightMeter;
        locWight = locGame.iWidthMeter;
        starTexture = new TextureRegion(new Texture("star5x5.png"));
        Random random = new Random();

        deltaX = 0f;
        deltaY = 0f;
        int idx;
        stars = new Vector2[MAXSTARSINLAYER * MAXLAYERS];
        for (int j = 0; j< MAXLAYERS; j++) {
            for (int i = 0; i < MAXSTARSINLAYER; i++) {
                idx = GetIndex(j, i);
                stars[idx] = new Vector2(random.nextFloat()*locWight, random.nextFloat()*locHeight);
            }
        }

    }

    private float SetNextDeltaX(float deltaTime) {
        deltaX = deltaX + deltaTime * 100f;
        deltaY = FixCoord(deltaY, 0, locWight);
        //v.2.1
        //return deltaX;

        //v.2.2
        return (float)(100 * Math.sin(2.0 * Math.PI * deltaX/locWight));
    }

    private float SetNextDeltaY(float deltaTime) {
        deltaY = deltaY + deltaTime * 100f;
        deltaY = FixCoord(deltaY, 0, locHeight);
        //v.2.1
        //return 0f;

        //v.2.2
        return (float)(50 * Math.sin(2.0 * Math.PI * deltaY/locHeight));
    }

    private float FixCoord (float coor, float min, float max) {
        if (coor > max) { coor -= max; }
        if (coor < min) { coor += max; }
        return coor;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        int idx;
        float x, y, locDeltaX, locDeltaY;

        locDeltaX = SetNextDeltaX(Gdx.graphics.getDeltaTime());
        locDeltaY = SetNextDeltaY(Gdx.graphics.getDeltaTime());
        for (int j = 0; j< MAXLAYERS; j++) {
            for (int i = 0; i < MAXSTARSINLAYER; i++) {
                idx = GetIndex(j, i);
                x = stars[idx].x + locDeltaX * (j+1);
                x = FixCoord(x, 0, locWight);
                y = stars[idx].y + locDeltaY * (j+1);
                y = FixCoord(y, 0, locHeight);
                batch.draw(starTexture, x, y);
            }
        }

        super.draw(batch, alpha);
    }
}
