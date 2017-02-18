package com.andrewvasiliev.game.test01.Classes;

//import com.badlogic.gdx.utils.Disposable;

import java.util.Arrays;

/**
 * Created by AvA on 18.02.2017.
 */

public class AniCell  /*implements Disposable*/ {
    private Const.CellShape cellShape;
    private float coord[];
    private int vertexCount;

    private float x,y;           //координаты центра ячейки
    private float invertY;
    private int phaseIdx;        //текущий номер фазы анимации
    private float animDuration;  //продолжительность текущей анимации (для расчета фазы, которую надо показать)


    public AniCell(Const.CellShape inCellShape) {
        cellShape = inCellShape;
        invertY = 1.0f; //не перевернуто по вертикали
        phaseIdx = -1; //состояние покоя
        animDuration = 0.0f;
        InitCellCoord(cellShape);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setInvertY (boolean inInvertY) {
        invertY = inInvertY ? -1.0f : 1.0f;
    }

    public void CalcZoom (float inWidth, float inHeight) {
        InitCellCoord(cellShape);
        float zoom = inWidth < inHeight ? inWidth : inHeight;
        zoom = zoom / 100; //100 это ширина и высота фигуры при прерасчете

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

//    @Override
//    public void dispose() {
//    }
}
