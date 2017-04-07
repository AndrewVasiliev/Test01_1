package com.andrewvasiliev.game.test01.Classes;

//import com.badlogic.gdx.utils.Disposable;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by AvA on 18.02.2017.
 */

public class BaseCell  /*implements Disposable*/ {
    private Const.CellShape cellShape;
    private float coord[];
    private int vertexCount, vertexCount2x;
    private float locHeight;
    private int phaseCount;



    //private Quaternion testQ;






    //private float x,y;           //координаты центра ячейки
    //private float invertY;
    //private int phaseIdx;        //текущий номер фазы анимации
    //private float animDuration;  //продолжительность текущей анимации (для расчета фазы, которую надо показать)
    //private int colorIdx, colorIdxNext;
    //private float animationSpeed; //за сколько секунд должна закончиться анимация
    //private boolean animNonStop;
    private float maxScale, minScale;
    private float sqrt3;


    public BaseCell(Const.CellShape inCellShape, float inWidth/*, float inHeight*/) {
        sqrt3 = (float)Math.sqrt(3f);
        cellShape = inCellShape;
        locHeight = 1.0f;
        //invertY = 1.0f; //не перевернуто по вертикали
        //phaseIdx = -1; //состояние покоя
        //animDuration = 0.0f;
        //animationSpeed = 0.5f;
        //colorIdx = 0;
        //animNonStop = false;
        maxScale = 1.0f;
        minScale = 0.9f;
        InitCellCoord(inCellShape);
        CorrectSize(inWidth/*, inHeight*/); //в зависимости от фигуры, высота будет отличаться
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
        float minX = coord[0];
        float maxX = coord[0];
        for (int k=1; k<vertexCount; k++) {
            minX = minX > coord[k*2] ? coord[k*2] : minX;
            maxX = maxX < coord[k*2] ? coord[k*2] : maxX;
        }
        return maxX-minX;
    }

    public float GetHeight () {
        return locHeight;
    }

    private void CalcHeight() {
        float minY = coord[1];
        float maxY = coord[1];
        for (int k=1; k<vertexCount; k++) {
            minY = minY > coord[k*2+1] ? coord[k*2+1] : minY;
            maxY = maxY < coord[k*2+1] ? coord[k*2+1] : maxY;
        }
        locHeight = maxY-minY;
    }

    private void CorrectSize (float inWidth/*, float inHeight*/) {
        float zoom = inWidth / GetWidth();
        //float zoomY = inHeight / GetHeight();

        for (int i = 0; i<phaseCount*vertexCount; i++) {
            coord[i*2] = coord[i*2]*zoom;
            coord[i*2+1] = coord[i*2+1]*zoom;
        }
        CalcHeight();
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
        Vector3[] baseC;
        float angle = 5.0f;
        phaseCount = Math.round(180f / angle);
        baseC = new Vector3[6];
        for (int i=0; i<baseC.length; i++) {
            baseC[i] = new Vector3();
        }

        switch (inCellShape) {
            case RECTANGLE:
                vertexCount = 4;
                baseC[0].set(-50f, -50f, 0);
                baseC[1].set(-50f,  50f, 0);
                baseC[2].set( 50f,  50f, 0);
                baseC[3].set( 50f, -50f, 0);
                break;
            case TRIANGLE:
                vertexCount = 3;
                baseC[0].set(-50f, -sqrt3 / 6.0f * 100.0f, 0);
                baseC[1].set(  0f,  sqrt3 / 3.0f * 100.0f, 0);
                baseC[2].set( 50f, -sqrt3 / 6.0f * 100.0f, 0);
                break;
            case RHOMBUS:
                vertexCount = 4;
                baseC[0].set(  0f, -50f, 0);
                baseC[1].set(-50f,   0f, 0);
                baseC[2].set(  0f,  50f, 0);
                baseC[3].set( 50f,   0f, 0);
                break;
            case HEX:
                vertexCount = 6;
                baseC[0].set(-50f / 2f, -sqrt3 * 50f / 2f, 0);
                baseC[1].set(-50f,       0f, 0);
                baseC[2].set(-50f / 2f,  sqrt3 * 50f / 2f, 0);
                baseC[3].set( 50f / 2f,  sqrt3 * 50f / 2f, 0);
                baseC[4].set( 50f,       0f, 0);
                baseC[5].set( 50f / 2f, -sqrt3 * 50f / 2f, 0);
                break;
        }

        vertexCount2x = vertexCount * 2;
        coord = new float[vertexCount2x * phaseCount];
        //для HEX и TRIANGLE наклонная ось
        //Vector3 RotateAxis = new Vector3(1f, 1f / sqrt3, 0f);
        //наклонная ось по Y
        Vector3 RotateAxis = new Vector3(0f, 1f, 0f);
        Quaternion q = new Quaternion();
        Quaternion p = new Quaternion();
        Quaternion q2 = new Quaternion();
        Quaternion q3 = new Quaternion();
        int coordIdx = 0;

        for (int i=0; i<phaseCount; i++) {
            float phi = (float)Math.PI / 180.0f * angle * i;
            float cosphi = (float)Math.cos(phi / 2.0f);
            float sinphi = (float)Math.sin(phi / 2.0f);
            q.set(RotateAxis.x * sinphi, RotateAxis.y * sinphi, RotateAxis.z * sinphi, cosphi);
            q.nor();

            for (int ncoor=0; ncoor<vertexCount; ncoor++) {
                p.set(baseC[ncoor].x, baseC[ncoor].y, baseC[ncoor].z, 0);
                q3.set(q);
                q2.set(q);
                q2.nor().conjugate();
                p.set(q3.mul(p.mul(q2)));
                //coord[i * vertexCount2x + ncoor * 2    ] = d3tod2 (p.x, p.z);
                //coord[i * vertexCount2x + ncoor * 2 + 1] = d3tod2 (p.y, p.z);
                coord[coordIdx++] = d3tod2 (p.x, p.z);
                coord[coordIdx++] = d3tod2 (p.y, p.z);
            }
        }
    }

    private float d3tod2 (float a1, float a2) {
        return (600.0f * a1/(a2 + 600.0f));
    }

    public int GetPhaseCount () {
       return phaseCount;
    }

    private void DrawShape (float locX, float locY, float invertY, float scale, int idx, ShapeRenderer inSR) {
        float scale_invertY = scale * invertY;
        int k2, k;
        for (k = 2; k<vertexCount; k++) {
            k2 = k + k;
            inSR.triangle(
                    locX + coord[idx] * /*scale_invertY*/ scale,          locY + coord[idx + 1] * scale_invertY,
                    locX + coord[idx + k2] * /*scale_invertY*/ scale,     locY + coord[idx + k2 + 1] * scale_invertY,
                    locX + coord[idx + k2 - 2] * /*scale_invertY*/ scale, locY + coord[idx + k2 - 1] * scale_invertY);
        }
    }

    public void draw (float x, float y, float invertY, int phaseIdx, int colorIdx, int colorIdxNext, ShapeRenderer inSR, Color borderColor) {
        int idx = (phaseIdx==-1 ? 0 : phaseIdx) * vertexCount2x; //номер фазы анимации * vetrexCount * 2 (это x и y)

        //отрисуем фигуру заполненными треугольниками
        //сначала отрисуем полную фигуру
//для теста попробуем не рисовать контур
        //inSR.setColor(borderColor);
        //DrawShape(x, y, invertY, maxScale, idx, inSR);

        //теперь чуть меньшую, чтоб получился контур
        if (phaseIdx >= phaseCount/2) {
            //цвет с другой стороны
            inSR.setColor(Const.colorArr[colorIdxNext]);
        } else {
            inSR.setColor(Const.colorArr[colorIdx]);
        }
        DrawShape(x, y, invertY, minScale, idx, inSR);
    }

    public void drawbridge(ShapeRenderer inSR, float bx, float by, float biy, int bcidx, int n, boolean even, float nx, float ny, float niy,
                           boolean prevNearby, boolean nextNearby) {
        int bidx = 0; //первая вершина стороны базовой фигуры для рисования перемычки
        int nidx = 0; //первая вершина стороны соседней фигуры для рисования перемычки
        float scale_biy = minScale * biy;
        float scale_niy = minScale * niy;
        switch (cellShape) {
            case RECTANGLE:
                bidx = n;
                nidx = (n - 2) < 0 ? n + 2 : n - 2;
                break;
            case TRIANGLE:
                //return;
                bidx = n;
                /*
                if (biy == 1.0f) {
                    nidx = (n == 2) ? n : n ^ 1;
                } else {
                    return;
                }*/
                nidx = (n == 2) ? n : n ^ 1;
                break;
            case RHOMBUS:
                bidx = n;
                nidx = (n - 2) < 0 ? n + 2 : n - 2;
                break;
            case HEX:
                bidx = n;
                nidx = (n - 3) < 0 ? n + 3 : n - 3;
                break;
        }
        if (cellShape == Const.CellShape.TRIANGLE) {

            inSR.triangle(
                    bx + coord[bidx * 2] * minScale            , by + coord[bidx * 2 + 1] * scale_biy,
                    bx + coord[FixIdx(bidx + 1) * 2] * minScale, by + coord[FixIdx(bidx + 1) * 2 + 1] * scale_biy,
                    nx + coord[((niy == -1.0f) ? FixIdx(nidx+1) : nidx) * 2] * minScale, ny + coord[((niy == -1.0f) ? FixIdx(nidx+1) : nidx) * 2 + 1] * scale_niy
            );
            inSR.triangle(
                    bx + coord[bidx * 2] * minScale            , by + coord[bidx * 2 + 1] * scale_biy,
                    nx + coord[nidx * 2] * minScale            , ny + coord[nidx * 2 + 1] * scale_niy,
                    nx + coord[FixIdx(nidx + 1) * 2] * minScale, ny + coord[FixIdx(nidx + 1) * 2 + 1] * scale_niy
            );

            //ушки у перемычек

            if (nextNearby) {
                inSR.triangle(
                        bx + coord[FixIdx(bidx + 1) * 2] * minScale, by + coord[FixIdx(bidx + 1) * 2 + 1] * scale_biy,
                        bx + coord[FixIdx(bidx + 1) * 2]           , by + coord[FixIdx(bidx + 1) * 2 + 1] * biy,
                        nx + coord[FixIdx(nidx + 1) * 2] * minScale, ny + coord[FixIdx(nidx + 1) * 2 + 1] * scale_niy
                );
            }


            if (prevNearby) {
                inSR.triangle(
                        bx + coord[bidx * 2] * minScale, by + coord[bidx * 2 + 1] * scale_biy,
                        nx + coord[nidx * 2]           , ny + coord[nidx * 2 + 1] * niy,
                        nx + coord[nidx * 2] * minScale, ny + coord[nidx * 2 + 1] * scale_niy
                );
            }

        } else {
            inSR.triangle(
                    bx + coord[bidx * 2] * minScale            , by + coord[bidx * 2 + 1] * scale_biy,
                    bx + coord[FixIdx(bidx + 1) * 2] * minScale, by + coord[FixIdx(bidx + 1) * 2 + 1] * scale_biy,
                    nx + coord[nidx * 2] * minScale            , ny + coord[nidx * 2 + 1] * scale_niy
            );
            inSR.triangle(
                    bx + coord[bidx * 2] * minScale            , by + coord[bidx * 2 + 1] * scale_biy,
                    nx + coord[nidx * 2] * minScale            , ny + coord[nidx * 2 + 1] * scale_niy,
                    nx + coord[FixIdx(nidx + 1) * 2] * minScale, ny + coord[FixIdx(nidx + 1) * 2 + 1] * scale_niy
                    //nx + coord[((nidx + 1) % vertexCount) * 2] * minScale, ny + coord[((nidx + 1) % vertexCount) * 2 + 1] * scale_niy
            );

            //ушки у перемычек
            if (nextNearby) {
                inSR.triangle(
                        bx + coord[FixIdx(bidx + 1) * 2] * minScale, by + coord[FixIdx(bidx + 1) * 2 + 1] * scale_biy,
                        bx + coord[FixIdx(bidx + 1) * 2], by + coord[FixIdx(bidx + 1) * 2 + 1] * biy,
                        nx + coord[nidx * 2] * minScale, ny + coord[nidx * 2 + 1] * scale_niy
                );
            }
            if (prevNearby) {
                inSR.triangle(
                        bx + coord[bidx * 2] * minScale, by + coord[bidx * 2 + 1] * scale_biy,
                        nx + coord[FixIdx(nidx + 1) * 2], ny + coord[FixIdx(nidx + 1) * 2 + 1] * niy,
                        nx + coord[FixIdx(nidx + 1) * 2] * minScale, ny + coord[FixIdx(nidx + 1) * 2 + 1] * scale_niy
                );
            }
        }
    }

    private int FixIdx (int idx) {
        return (idx == vertexCount) ? 0 : idx ;
    }


//    @Override
//    public void dispose() {
//    }
}
