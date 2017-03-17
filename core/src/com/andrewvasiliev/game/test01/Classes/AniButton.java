package com.andrewvasiliev.game.test01.Classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by AvA on 18.02.2017.
 */

public class AniButton extends Button implements Disposable {
    private Const.CellShape cellType = Const.CellShape.HEX;
    private ShapeRenderer sr;
    private AniCell aniCell;
    private float aniZoom = 0.7f;

    public AniButton(Skin skin, String styleName, Const.CellShape inCellType) {
        super(skin, styleName);
        sr = new ShapeRenderer();
        cellType = inCellType;
        aniCell = new AniCell (cellType, sr);
        CorrectAniPosition();
        CorrectAniZoom(aniZoom);
        aniCell.setAnimationSpeed(0.8f);
        aniCell.setAnimNonStop(false);
    }

    private void CorrectAniZoom (float inAniZoom) {
        aniCell.CalcZoom(getWidth(), getHeight(), inAniZoom);
    }

    @Override
    protected void sizeChanged() {
        if (aniCell != null) {
            CorrectAniZoom(aniZoom);
        }
        super.sizeChanged();
    }

    private void CorrectAniPosition () {
        aniCell.setPosition(getX() + getWidth() / 2.0f, getY() + getHeight() / 2.0f);
    }

    @Override
    protected void positionChanged() {
        CorrectAniPosition();
        super.positionChanged();
    }


    @Override
    public void setChecked(boolean isChecked) {
        super.setChecked(isChecked);
        // :( когда нажимается кнопка это событие не вызывается. так что будем в draw всегда его проверять
        //aniCell.setAnimNonStop(isChecked);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();
        aniCell.setPosition(getX()+getWidth()/2.0f, getY()+getHeight()/2.0f);
        //событие setChecked не вызывается когда нажимается кнопка, поэтому будем всегда его проверять :(
        aniCell.setAnimNonStop(isChecked());
        aniCell.draw(Gdx.graphics.getDeltaTime());
/*
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.GREEN);
        float x,y, wi, he;

        x = getX();
        y = getY();
        wi = getWidth();
        he = getHeight();

        sr.line(x,y, x+wi,y+he);

        sr.end();
*/
        batch.begin();
    }

    @Override
    public void dispose() {
        sr.dispose();
    }
}
