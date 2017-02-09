package com.andrewvasiliev.game.test01.Actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;


/**
 * Created by root on 05.11.15.
 */
public class BackgroundStar extends Actor {
    private TextureRegion starSprite;
    private int layer;
    private float maxX;
    private float dx;


    public BackgroundStar (TextureRegion sp, int layerIn, float maxXIn) {
        maxX = maxXIn;
        dx = 0;
        layer = layerIn;
        starSprite = sp;
        setBounds(getX(), getY(), starSprite.getRegionWidth(), starSprite.getRegionHeight());
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.draw(starSprite, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public void act (float delta){
        //dx = getX();
        //dx= getY();
        dx = delta * layer * 10f/*0.2f*/;
        moveBy(dx, 0);
        if (getX() > maxX) {
            setX(getX() - maxX);
        }

    }

}
