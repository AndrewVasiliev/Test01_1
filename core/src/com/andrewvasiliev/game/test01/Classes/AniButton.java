package com.andrewvasiliev.game.test01.Classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by AvA on 18.02.2017.
 */

public class AniButton extends Button {
    private Const.CellShape cellType = Const.CellShape.RECTANGLE;
    private ShapeRenderer sr;
    private AniCell aniCell;

    public AniButton(Skin skin, String styleName, Const.CellShape inCellType) {
        super(skin, styleName);
        cellType = inCellType;
        sr = new ShapeRenderer();
    }


    @Override
    protected void sizeChanged() {
        aniCell.CalcZoom(getWidth(), getHeight());
        super.sizeChanged();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        float deltaTime = Gdx.graphics.getDeltaTime();

        batch.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.GREEN);
        float x,y, wi, he;

        x = getX();
        y = getY();
        wi = getWidth();
        he = getHeight();

        sr.line(x,y, x+wi,y+he);

        sr.end();
        batch.begin();

    }

    @Override
    protected void positionChanged() {
        aniCell.setPosition(getX(), getY());
        super.positionChanged();
    }


}
