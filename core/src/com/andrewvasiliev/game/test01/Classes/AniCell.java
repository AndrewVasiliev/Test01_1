package com.andrewvasiliev.game.test01.Classes;

//import com.badlogic.gdx.utils.Disposable;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by AvA on 18.02.2017.
 */

public class AniCell  /*implements Disposable*/ {
    private Const.CellShape cellShape;
    private ShapeRenderer sr;

    private float x,y;           //координаты центра ячейки
    private float invertY;
    private int phaseIdx;        //текущий номер фазы анимации
    private float animDuration;  //продолжительность текущей анимации (для расчета фазы, которую надо показать)
    private int colorIdx, colorIdxNext;
    private float animationSpeed; //за сколько секунд должна закончиться анимация
    private boolean animNonStop;
    private float maxScale, minScale;
    private BaseCell cell;


    public AniCell(Const.CellShape inCellShape, ShapeRenderer inSR) {
        sr = inSR;
        cellShape = inCellShape;
        invertY = 1.0f; //не перевернуто по вертикали
        phaseIdx = -1; //состояние покоя
        animDuration = 0.0f;
        animationSpeed = 0.5f;
        //colorIdx = 0;
        setColorIdx(0);
        animNonStop = false;
        maxScale = 1.0f;
        minScale = 0.8f;
        InitCellCoord(cellShape);
    }

    private int GetNextIdxColor () {
        return (colorIdx < (Const.ColorCount-1)) ? colorIdx + 1 : 0;
    }

    public void setColorIdx (int inColorIdx) {
        colorIdx = inColorIdx;
        colorIdxNext = GetNextIdxColor();
    }

    public void setAnimationSpeed (float inAnimationSpeed) {
        animationSpeed = inAnimationSpeed;
    }

    public void setScale (float inMaxScale, float inMinScale) {
        maxScale = inMaxScale;
        minScale = inMinScale;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setAnimNonStop (boolean inAnimNonStop) {
        animNonStop = inAnimNonStop;
    }

    public void setInvertY (boolean inInvertY) {
        invertY = inInvertY ? -1.0f : 1.0f;
    }

    public void CalcZoom (float inWidth, float inHeight, float inAniZoom) {
        ////InitCellCoord(cellShape);
        float zoom = inWidth < inHeight ? inWidth : inHeight;
        zoom = zoom * inAniZoom; //100 это ширина и высота фигуры при прерасчете

        cell = new BaseCell(cellShape, zoom);
    }

    private void InitCellCoord (Const.CellShape inCellShape) {
        cell = new BaseCell(inCellShape, 100);
    }

    public void draw (float deltaTime) {
        if ((phaseIdx != -1) || (animNonStop)) { //-1 состояние покоя и фаза 0
            animDuration += deltaTime;
            phaseIdx = (int)(animDuration/(animationSpeed/(float)cell.GetPhaseCount()));
            if (phaseIdx >= cell.GetPhaseCount()) {
                phaseIdx = -1;
                //animDuration = 0.0f;
                colorIdx = colorIdxNext;
                colorIdxNext = GetNextIdxColor();
                if (animNonStop) {
                    phaseIdx = 0;
                    animDuration -= animationSpeed;
                } else {
                    animDuration = 0.0f;
                }
            }
        }

        sr.begin(ShapeRenderer.ShapeType.Filled);
        cell.draw(x, y, invertY, phaseIdx, colorIdx, colorIdxNext, sr, Const.borderColor);
        sr.end();

    }

//    @Override
//    public void dispose() {
//    }
}
