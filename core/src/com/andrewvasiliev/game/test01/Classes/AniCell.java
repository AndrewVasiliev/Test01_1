package com.andrewvasiliev.game.test01.Classes;

//import com.badlogic.gdx.utils.Disposable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.awt.Shape;
import java.util.Arrays;

/**
 * Created by AvA on 18.02.2017.
 */

public class AniCell  /*implements Disposable*/ {
    private Const.CellShape cellShape;
    private ShapeRenderer sr;
    private float coord[];
    private int vertexCount;

    private float x,y;           //координаты центра ячейки
    private float invertY;
    private int phaseIdx;        //текущий номер фазы анимации
    private float animDuration;  //продолжительность текущей анимации (для расчета фазы, которую надо показать)
    private int colorIdx, colorIdxNext;
    private float animationSpeed; //за сколько секунд должна закончиться анимация
    private boolean animNonStop;


    public AniCell(Const.CellShape inCellShape, ShapeRenderer inSR) {
        sr = inSR;
        cellShape = inCellShape;
        invertY = 1.0f; //не перевернуто по вертикали
        phaseIdx = -1; //состояние покоя
        animDuration = 0.0f;
        animationSpeed = 0.5f;
        colorIdx = 0;
        animNonStop = false;
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
        InitCellCoord(cellShape);
        float zoom = inWidth < inHeight ? inWidth : inHeight;
        zoom = zoom / 100 * inAniZoom; //100 это ширина и высота фигуры при прерасчете

        for (int i = 0; i<Const.phaseCount*vertexCount; i++) {
            coord[i*2] = coord[i*2]*zoom;
            coord[i*2+1] = coord[i*2+1]*zoom;
        }
    }

    private void InitCellCoord (Const.CellShape inCellShape) {
        switch (inCellShape) {
            case RECTANGLE:
                vertexCount = Const.rectangleVertexCount;
                coord = Arrays.copyOf(Const.rectangleCoord, Const.rectangleCoord.length);
                break;
            case TRIANGLE:
                vertexCount = Const.triangleVertexCount;
                coord = Arrays.copyOf(Const.triangleCoord, Const.triangleCoord.length);
                break;
            case RHOMBUS:
                vertexCount = Const.rhombusVertexCount;
                coord = Arrays.copyOf(Const.rhombusCoord, Const.rhombusCoord.length);
                break;
            case HEX:
                vertexCount = Const.hexVertexCount;
                coord = Arrays.copyOf(Const.hexCoord, Const.hexCoord.length);
                break;
        }
    }

    private void DrawShape (float locX, float locY, float invertY, float scale, int idx) {
        for (int k=2; k<vertexCount; k++) {
            sr.triangle(
                    locX + coord[idx] * scale * invertY, locY + coord[idx + 1] * scale * invertY,
                    locX + coord[idx + k*2] * scale * invertY, locY + coord[idx + k*2+1] * scale * invertY,
                    locX + coord[idx + (k-1)*2] * scale * invertY, locY + coord[idx + (k-1)*2+1] * scale * invertY);
        }
    }

    public void draw (float deltaTime) {
        float scale;

        if (phaseIdx != -1) { //-1 состояние покоя и фаза 0
            animDuration += deltaTime;
            phaseIdx = (int)(animDuration/(animationSpeed/(float)Const.phaseCount));
            if (phaseIdx >= Const.phaseCount) {
                phaseIdx = -1;
                animDuration = 0.0f;
            }
        }
        if (animNonStop && (phaseIdx == -1)) {
            phaseIdx = 0;
            colorIdx = colorIdxNext;
            colorIdxNext = GetNextIdxColor();
        }

        sr.begin(ShapeRenderer.ShapeType.Filled);
        int idx = (phaseIdx==-1 ? 0 : phaseIdx) * vertexCount*2; //номер фазы анимации * vetrexCount * 2 (это x и y)

        //отрисуем фигуру заполненными треугольниками
        //сначала отрисуем полную фигуру
        scale = 1.01f;
        sr.setColor(Const.borderColor);
        DrawShape(x, y, invertY, scale, idx);

        //теперь чуть меньшую, чтоб получился контур
        scale = 0.9f;

        if (phaseIdx >= Const.phaseCount/2) {
                //цвет с другой стороны
            sr.setColor(Const.colorArr[colorIdxNext]);
        } else {
            sr.setColor(Const.colorArr[colorIdx]);
        }
        DrawShape(x, y, invertY, scale, idx);
        sr.end();

    }

//    @Override
//    public void dispose() {
//    }
}
