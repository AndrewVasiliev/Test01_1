package com.andrewvasiliev.game.test01.Actors;

import com.andrewvasiliev.game.test01.MyGdxGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.Random;

/**
 * Created by root on 05.11.15.
 */
public class BackgroundActor extends Actor {
    private BackgroundStar[] stars;
    private Group locGroup;
    private final  TextureRegion starTexture;
    private static int MAXSTARSINLAYER = 30;
    private static int MAXLAYERS = 5;
    private MyGdxGame locGame;
    private float locHeight, locWight;

    public BackgroundActor(MyGdxGame myGdxGame) {
        locGame = myGdxGame;

        locGroup = new Group();

        locHeight = locGame.iHeightMeter;
        locWight = locGame.iWidthMeter;

        starTexture = new TextureRegion(new Texture("star5x5.png"));
        stars = new BackgroundStar[MAXSTARSINLAYER * MAXLAYERS];
        Random random = new Random();
        for (int j = 0; j< MAXLAYERS; j++) {
            for (int i = 0; i < MAXSTARSINLAYER; i++) {
                stars[j*MAXSTARSINLAYER + i] = new BackgroundStar(starTexture, j+1, locWight );

                stars[j*MAXSTARSINLAYER + i].setPosition(random.nextFloat()*locWight, random.nextFloat()*locHeight);

                // Set the name of the Jet to it's index within the loop
                stars[j*MAXSTARSINLAYER + i].setName("layer" + Integer.toString(j) + "star" + Integer.toString(i));

                // Add them to the stage
                locGroup.addActor(stars[j*MAXSTARSINLAYER + i]);
            }
        }



    }

    @Override
    public void draw(Batch batch, float alpha) {
        locGroup.act(Gdx.graphics.getDeltaTime());
        locGroup.draw(batch, alpha);
        super.draw(batch, alpha);
    }
}
