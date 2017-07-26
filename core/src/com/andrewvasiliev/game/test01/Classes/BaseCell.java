package com.andrewvasiliev.game.test01.Classes;

//import com.badlogic.gdx.utils.Disposable;


import com.andrewvasiliev.game.test01.MyGdxGame;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AvA on 18.02.2017.
 */

public class BaseCell  /*implements Disposable*/ {
    private Const.CellShape cellShape;
    private float coord[];
    private int vertexCount, vertexCount2x;
    private float locHeight, locWidth;
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
    private MyGdxGame locGame;

    Map<Color,TextureRegion> TextureRegionMap;
    List<short[]> EarClippingList;
    List<float[]> PhaseCoordList;
    Map<Long,PolygonRegion> PolygonRegionMap;



    public BaseCell(Const.CellShape inCellShape, float inWidth/*, float inHeight*/, MyGdxGame lg) {
        sqrt3 = (float)Math.sqrt(3f);
        locGame = lg;
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
        locWidth = inWidth;
        CorrectSize(inWidth/*, inHeight*/); //в зависимости от фигуры, высота будет отличаться
        if (locGame.UsePolygon) {
            TextureRegionMap=new HashMap<Color,TextureRegion>();
            EarClippingList = new ArrayList<short[]>();
            PhaseCoordList = new ArrayList<float[]>();
            PolygonRegionMap=new HashMap<Long,PolygonRegion>();
            GeneratePolygonData();
        }
    }

    private TextureRegion GetTextureRegion (Color inColor) {
        if (!TextureRegionMap.containsKey(inColor)) {
            Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pix.setColor(inColor);
            pix.fill();
            //Texture t = new Texture(pix);
            //t.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            TextureRegionMap.put(inColor, new TextureRegion(/*t*/new Texture(pix)));
            pix.dispose();
        }
        return TextureRegionMap.get(inColor);
    }

    private void GeneratePolygonData () {
        int coordIdx = 0;
        for (int i=0; i<phaseCount; i++) {
            PhaseCoordList.add(new float[vertexCount2x]);

            for (int ncoor = 0; ncoor < vertexCount; ncoor++) {
                PhaseCoordList.get(i)[ncoor * 2] = coord[coordIdx++];
                PhaseCoordList.get(i)[ncoor * 2 + 1] = coord[coordIdx++];
            }
            EarClippingList.add(new EarClippingTriangulator().computeTriangles(PhaseCoordList.get(i)).toArray());
        }
    }

    private PolygonRegion GetPolygonRegion (Color inColor, int phaseIdx) {
        long hash = (long)inColor.toIntBits() << 32 | phaseIdx;

        if (!PolygonRegionMap.containsKey(hash)) {
            PolygonRegionMap.put(hash, new PolygonRegion(GetTextureRegion(inColor),
                    PhaseCoordList.get(phaseIdx), EarClippingList.get(phaseIdx)));
        }
        return PolygonRegionMap.get(hash);
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

    public void FillFullCell (float locX, float locY, float cellWidth, float cellHeight, int colorIdx) {

        if (locGame.UsePolygon) { return; }

        float dx = cellWidth / 2.0f;
        float dy = cellHeight / 2.0f;
        float x1 = locX - dx;
        float y1 = locY + dy;
        float x3 = locX + dx;
        float y3 = locY - dy;
        locGame.sr.setColor(Const.colorArr[colorIdx]);
        locGame.sr.triangle(x1, y1,  x1, y3,  x3, y3);
        locGame.sr.triangle(x1, y1,  x3, y1,  x3, y3);
    }

    private void DrawShape (float locX, float locY, float invertY, float scale, int idx) {
        float scale_invertY = scale * invertY;
        int k2, k;
        for (k = 2; k<vertexCount; k++) {
            k2 = k + k;
            locGame.sr.triangle(
                    locX + coord[idx] * scale,          locY + coord[idx + 1] * scale_invertY,
                    locX + coord[idx + k2] * scale,     locY + coord[idx + k2 + 1] * scale_invertY,
                    locX + coord[idx + k2 - 2] * scale, locY + coord[idx + k2 - 1] * scale_invertY
            );
        }
    }


    private void DrawPolyShape (float locX, float locY, float invertY, float scale, int phaseIdx, Color inColor) {

        phaseIdx = phaseIdx == -1 ? 0 : phaseIdx;
        //v1
        //PolygonRegion polygonRegion = new PolygonRegion(GetTextureRegion(inColor), PhaseCoordList.get(phaseIdx), EarClippingList.get(phaseIdx));
        //locGame.psb.draw(polygonRegion, locX, locY, scale, scale);

        //v2
        locGame.psb.draw(GetPolygonRegion(inColor, phaseIdx), locX, locY, scale, scale);
    }

    public void draw (float x, float y, float invertY, int phaseIdx, int colorIdx, int colorIdxNext,Color borderColor, boolean useBorderColor) {

        if (locGame.UsePolygon) {
            if (useBorderColor) {
                DrawPolyShape(x, y, invertY, maxScale, phaseIdx, borderColor);
            }
            DrawPolyShape(x, y, invertY, minScale, phaseIdx, Const.colorArr[phaseIdx >= phaseCount / 2 ? colorIdxNext : colorIdx]);
        } else {
            int idx = (phaseIdx == -1 ? 0 : phaseIdx) * vertexCount2x; //номер фазы анимации * vetrexCount * 2 (это x и y)

            //отрисуем фигуру заполненными треугольниками
            //сначала отрисуем полную фигуру
            if (useBorderColor) {
                locGame.sr.setColor(borderColor);
                DrawShape(x, y, invertY, maxScale, idx);
            }
            //теперь чуть меньшую, чтоб получился контур
            locGame.sr.setColor(Const.colorArr[phaseIdx >= phaseCount / 2 ? colorIdxNext : colorIdx]);
            DrawShape(x, y, invertY, minScale, idx);
        }
    }

    public void drawbridge(float bx, float by, float biy, int n, float nx, float ny, float niy,
                           boolean prevNearby, boolean nextNearby) {

        if (locGame.UsePolygon) { return; }

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
                bidx = n;
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
        int bidx2X = bidx + bidx; //bidx * 2;
        int nidx2X = nidx + nidx; //nidx * 2;

        if (cellShape == Const.CellShape.TRIANGLE) {
            locGame.sr.triangle(
                    bx + coord[bidx2X] * minScale              , by + coord[bidx2X + 1] * scale_biy,
                    bx + coord[FixIdx(bidx + 1) * 2] * minScale, by + coord[FixIdx(bidx + 1) * 2 + 1] * scale_biy,
                    nx + coord[((niy == -1.0f) ? FixIdx(nidx+1) : nidx) * 2] * minScale, ny + coord[((niy == -1.0f) ? FixIdx(nidx+1) : nidx) * 2 + 1] * scale_niy
            );
            locGame.sr.triangle(
                    bx + coord[bidx2X] * minScale              , by + coord[bidx2X + 1] * scale_biy,
                    nx + coord[nidx2X] * minScale              , ny + coord[nidx2X + 1] * scale_niy,
                    nx + coord[FixIdx(nidx + 1) * 2] * minScale, ny + coord[FixIdx(nidx + 1) * 2 + 1] * scale_niy
            );

            //ушки у перемычек
            if (nextNearby) {
                locGame.sr.triangle(
                        bx + coord[FixIdx(bidx + 1) * 2] * minScale, by + coord[FixIdx(bidx + 1) * 2 + 1] * scale_biy,
                        bx + coord[FixIdx(bidx + 1) * 2]           , by + coord[FixIdx(bidx + 1) * 2 + 1] * biy,
                        nx + coord[FixIdx(nidx + 1) * 2] * minScale, ny + coord[FixIdx(nidx + 1) * 2 + 1] * scale_niy
                );
            }


            if (prevNearby) {
                locGame.sr.triangle(
                        bx + coord[bidx2X] * minScale, by + coord[bidx2X + 1] * scale_biy,
                        nx + coord[nidx2X]           , ny + coord[nidx2X + 1] * niy,
                        nx + coord[nidx2X] * minScale, ny + coord[nidx2X + 1] * scale_niy
                );
            }

        } else {
            locGame.sr.triangle(
                    bx + coord[bidx2X] * minScale              , by + coord[bidx2X + 1] * scale_biy,
                    bx + coord[FixIdx(bidx + 1) * 2] * minScale, by + coord[FixIdx(bidx + 1) * 2 + 1] * scale_biy,
                    nx + coord[nidx2X] * minScale              , ny + coord[nidx2X + 1] * scale_niy
            );
            locGame.sr.triangle(
                    bx + coord[bidx2X] * minScale              , by + coord[bidx2X + 1] * scale_biy,
                    nx + coord[nidx2X] * minScale              , ny + coord[nidx2X + 1] * scale_niy,
                    nx + coord[FixIdx(nidx + 1) * 2] * minScale, ny + coord[FixIdx(nidx + 1) * 2 + 1] * scale_niy
                    //nx + coord[((nidx + 1) % vertexCount) * 2] * minScale, ny + coord[((nidx + 1) % vertexCount) * 2 + 1] * scale_niy
            );

            //ушки у перемычек
            if (nextNearby) {
                locGame.sr.triangle(
                        bx + coord[FixIdx(bidx + 1) * 2] * minScale, by + coord[FixIdx(bidx + 1) * 2 + 1] * scale_biy,
                        bx + coord[FixIdx(bidx + 1) * 2]           , by + coord[FixIdx(bidx + 1) * 2 + 1] * biy,
                        nx + coord[nidx2X] * minScale              , ny + coord[nidx2X + 1] * scale_niy
                );
            }
            if (prevNearby) {
                locGame.sr.triangle(
                        bx + coord[bidx2X] * minScale              , by + coord[bidx2X + 1] * scale_biy,
                        nx + coord[FixIdx(nidx + 1) * 2]           , ny + coord[FixIdx(nidx + 1) * 2 + 1] * niy,
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
