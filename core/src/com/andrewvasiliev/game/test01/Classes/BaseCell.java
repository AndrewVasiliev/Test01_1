package com.andrewvasiliev.game.test01.Classes;

//import com.badlogic.gdx.utils.Disposable;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


import java.util.Arrays;

/**
 * Created by AvA on 18.02.2017.
 */

public class BaseCell  /*implements Disposable*/ {
    private Const.CellShape cellShape;
    private float coord[];
    private int vertexCount;

    //private float x,y;           //координаты центра ячейки
    //private float invertY;
    //private int phaseIdx;        //текущий номер фазы анимации
    //private float animDuration;  //продолжительность текущей анимации (для расчета фазы, которую надо показать)
    //private int colorIdx, colorIdxNext;
    //private float animationSpeed; //за сколько секунд должна закончиться анимация
    //private boolean animNonStop;
    private float maxScale, minScale;


    public BaseCell(Const.CellShape inCellShape, float inWidth, float inHeight) {
        cellShape = inCellShape;
        //invertY = 1.0f; //не перевернуто по вертикали
        //phaseIdx = -1; //состояние покоя
        //animDuration = 0.0f;
        //animationSpeed = 0.5f;
        //colorIdx = 0;
        //animNonStop = false;
        maxScale = 1.0f;
        minScale = 0.8f;
        InitCellCoord(cellShape);
        CorrectSize(inWidth, inHeight);
    }

    /*private int GetNextIdxColor () {
        return (colorIdx < (Const.ColorCount-1)) ? colorIdx + 1 : 0;
    }*/

    /*public void setColorIdx (int inColorIdx) {
        colorIdx = inColorIdx;
        colorIdxNext = GetNextIdxColor();
    }*/

    /*public void setAnimationSpeed (float inAnimationSpeed) {
        animationSpeed = inAnimationSpeed;
    }*/

    public void setScale (float inMaxScale, float inMinScale) {
        maxScale = inMaxScale;
        minScale = inMinScale;
    }

    /*public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }*/

    /*public void setAnimNonStop (boolean inAnimNonStop) {
        animNonStop = inAnimNonStop;
    }*/

    /*public void setInvertY (boolean inInvertY) {
        invertY = inInvertY ? -1.0f : 1.0f;
    }*/

    private float GetWidth () {
        float minX = coord[0*2];
        float maxX = coord[0*2];
        for (int k=1; k<vertexCount; k++) {
            minX = minX > coord[k*2] ? coord[k*2] : minX;
            maxX = maxX < coord[k*2] ? coord[k*2] : maxX;
        }
        return maxX-minX;
    }

    private float GetHeight () {
        float minY = coord[0*2+1];
        float maxY = coord[0*2+1];
        for (int k=1; k<vertexCount; k++) {
            minY = minY > coord[k*2+1] ? coord[k*2+1] : minY;
            maxY = maxY < coord[k*2+1] ? coord[k*2+1] : maxY;
        }
        return maxY-minY;
    }

    private void CorrectSize (float inWidth, float inHeight) {
        float zoomX = inWidth / GetWidth();
        float zoomY = inHeight / GetHeight();

        for (int i = 0; i<Const.phaseCount*vertexCount; i++) {
            coord[i*2] = coord[i*2]*zoomX;
            coord[i*2+1] = coord[i*2+1]*zoomY;
        }
    }
/*
    public void CalcZoom (float inWidth, float inHeight, float inAniZoom) {
        InitCellCoord(cellShape);
        float zoom = inWidth < inHeight ? inWidth : inHeight;
        zoom = zoom / 100 * inAniZoom; //100 это ширина и высота фигуры при прерасчете

        for (int i = 0; i<Const.phaseCount*vertexCount; i++) {
            coord[i*2] = coord[i*2]*zoom;
            coord[i*2+1] = coord[i*2+1]*zoom;
        }
    }*/

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

    private void DrawShape (float locX, float locY, float invertY, float scale, int idx, ShapeRenderer inSR) {
        for (int k=2; k<vertexCount; k++) {
            inSR.triangle(
                    locX + coord[idx] * scale * invertY, locY + coord[idx + 1] * scale * invertY,
                    locX + coord[idx + k*2] * scale * invertY, locY + coord[idx + k*2+1] * scale * invertY,
                    locX + coord[idx + (k-1)*2] * scale * invertY, locY + coord[idx + (k-1)*2+1] * scale * invertY);
        }
    }

    public void draw (float deltaTime, float x, float y, float invertY, int phaseIdx, int colorIdx, int colorIdxNext, ShapeRenderer inSR) {
        float scale;
/*
        if (phaseIdx != -1) { //-1 состояние покоя и фаза 0
            animDuration += deltaTime;
            phaseIdx = (int)(animDuration/(animationSpeed/(float)Const.phaseCount));
            if (phaseIdx >= Const.phaseCount-1) {
                phaseIdx = -1;
                animDuration = 0.0f;
            }
        }
        if (animNonStop && (phaseIdx == -1)) {
            phaseIdx = 0;
            colorIdx = colorIdxNext;
            colorIdxNext = GetNextIdxColor();
        }
*/
        //sr.begin(ShapeRenderer.ShapeType.Filled);
        int idx = (phaseIdx==-1 ? 0 : phaseIdx) * vertexCount*2; //номер фазы анимации * vetrexCount * 2 (это x и y)

        //System.out.format("(%f,%f)-(%f,%f)-(%f,%f)%n", coord[idx],coord[idx+1], coord[idx+2],coord[idx+3], coord[idx+4],coord[idx+5]);

        //отрисуем фигуру заполненными треугольниками
        //сначала отрисуем полную фигуру
        scale = maxScale;
        inSR.setColor(Const.borderColor);
        DrawShape(x, y, invertY, scale, idx, inSR);

        //теперь чуть меньшую, чтоб получился контур
        scale = minScale;

        if (phaseIdx >= Const.phaseCount/2) {
            //цвет с другой стороны
            inSR.setColor(Const.colorArr[colorIdxNext]);
        } else {
            inSR.setColor(Const.colorArr[colorIdx]);
        }
        DrawShape(x, y, invertY, scale, idx, inSR);
        //sr.end();

    }

//    @Override
//    public void dispose() {
//    }
}
