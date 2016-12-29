package com.andrewvasiliev.game.test01.Actors;

import com.andrewvasiliev.game.test01.Classes.MyCell;
import com.andrewvasiliev.game.test01.Screens.TestMainField;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;


/**
 * Created by ava on 23.12.16.
 */

public class GameField extends Actor {
    private float leftX, leftY, widthX, heightY, cellWidth, cellHeight;
    private int countCol, countRow;
    private TestMainField locScreen;
    private MyCell cells[];
    private float innerR;

    private MyCell.CellShape cellShape;

    //квадрат без прыжка
    private float rectangleCoord[] = {-50,-50 ,50,-50 ,50,50 ,-50,50 ,-55,-56 ,44,-45 ,44,45 ,-55,56 ,-57,-63 ,38,-42 ,38,42 ,-57,63 ,-57,-71 ,31,-39 ,31,39 ,-57,71 ,-53,-80 ,24,-36 ,24,36 ,-53,80 ,-44,-88 ,17,-35 ,17,35 ,-44,88 ,-29,-95 ,10,-34 ,10,34 ,-29,95 ,-10,-99 ,3,-33 ,3,33 ,-10,99 ,10,-99 ,-3,-33 ,-3,33 ,10,99 ,29,-95 ,-10,-34 ,-10,34 ,29,95 ,44,-88 ,-17,-35 ,-17,35 ,44,88 ,53,-80 ,-24,-36 ,-24,36 ,53,80 ,57,-71 ,-31,-39 ,-31,39 ,57,71 ,57,-63 ,-38,-42 ,-38,42 ,57,63 ,55,-56 ,-44,-45 ,-44,45 ,55,56 ,50,-50 ,-50,-50 ,-50,50 ,50,50};
    private int rectangleVertexCount = 4;

    //треугольник
    private float triangleCoord[] = {-50,-50 ,0,50 ,50,-50 ,-55,-56 ,0,50 ,44,-45 ,-57,-63 ,0,50 ,38,-42 ,-57,-71 ,0,50 ,31,-39 ,-53,-80 ,0,50 ,24,-36 ,-44,-88 ,0,50 ,17,-35 ,-29,-95 ,0,50 ,10,-34 ,-10,-99 ,0,50 ,3,-33 ,10,-99 ,0,50 ,-3,-33 ,29,-95 ,0,50 ,-10,-34 ,44,-88 ,0,50 ,-17,-35 ,53,-80 ,0,50 ,-24,-36 ,57,-71 ,0,50 ,-31,-39 ,57,-63 ,0,50 ,-38,-42 ,55,-56 ,0,50 ,-44,-45 ,50,-50 ,0,50 ,-50,-50};
    private int triangleVertexCount = 3;

    //ромб
    private float rhombusCoord[] = {-50,0 ,0,50 ,50,0 ,0,-50 ,-55,0 ,0,50 ,44,0 ,0,-50 ,-57,0 ,0,50 ,38,0 ,0,-50 ,-57,0 ,0,50 ,31,0 ,0,-50 ,-53,0 ,0,50 ,24,0 ,0,-50 ,-44,0 ,0,50 ,17,0 ,0,-50 ,-29,0 ,0,50 ,10,0 ,0,-50 ,-10,0 ,0,50 ,3,0 ,0,-50 ,10,0 ,0,50 ,-3,0 ,0,-50 ,29,0 ,0,50 ,-10,0 ,0,-50 ,44,0 ,0,50 ,-17,0 ,0,-50 ,53,0 ,0,50 ,-24,0 ,0,-50 ,57,0 ,0,50 ,-31,0 ,0,-50 ,57,0 ,0,50 ,-38,0 ,0,-50 ,55,0 ,0,50 ,-44,0 ,0,-50 ,50,0 ,0,50 ,-50,0 ,0,-50};
    private int rhombusVertexCount = 4;

    //гексагон
    private float hexCoord[] = {50,0 ,25,43 ,-25,43 ,-50,0 ,-25,-43 ,25,-43 ,44,0 ,23,41 ,-26,46 ,-55,0 ,-26,-46 ,23,-41 ,38,0 ,21,39 ,-25,48 ,-57,0 ,-25,-48 ,21,-39 ,31,0 ,18,38 ,-24,51 ,-57,0 ,-24,-51 ,18,-38 ,24,0 ,14,37 ,-21,53 ,-53,0 ,-21,-53 ,14,-37 ,17,0 ,10,36 ,-16,55 ,-44,0 ,-16,-55 ,10,-36 ,10,0 ,6,35 ,-10,57 ,-29,0 ,-10,-57 ,6,-35 ,3,0 ,2,35 ,-3,58 ,-10,0 ,-3,-58 ,2,-35 ,-3,0 ,-2,35 ,3,58 ,10,0 ,3,-58 ,-2,-35 ,-10,0 ,-6,35 ,10,57 ,29,0 ,10,-57 ,-6,-35 ,-17,0 ,-10,36 ,16,55 ,44,0 ,16,-55 ,-10,-36 ,-24,0 ,-14,37 ,21,53 ,53,0 ,21,-53 ,-14,-37 ,-31,0 ,-18,38 ,24,51 ,57,0 ,24,-51 ,-18,-38 ,-38,0 ,-21,39 ,25,48 ,57,0 ,25,-48 ,-21,-39 ,-44,0 ,-23,41 ,26,46 ,55,0 ,26,-46 ,-23,-41 ,-50,0 ,-25,43 ,25,43 ,50,0 ,25,-43 ,-25,-43};
    private int hexVertexCount = 6;

    private float coord[];
    private int vertexCount;


    private int phaseCount = 16;
    private float animationSpeed = 0.5f; //за сколько секунд должна закончиться анимация

    public GameField(TestMainField testMainField, float x, float y, float width, float height, int countColIn, MyCell.CellShape cellType) {
        leftX = x;
        leftY = y;
        widthX = width;
        heightY = height;
        countCol = countColIn;
        locScreen = testMainField;

        cellWidth = widthX/countCol;
        cellHeight = cellWidth;

        cellShape = cellType;

        switch (cellShape) {
            case RECTANGLE:
                vertexCount = rectangleVertexCount;
                coord = rectangleCoord;
                countRow = (int)(heightY / cellWidth);
                break;
            case TRIANGLE:
                vertexCount = triangleVertexCount;
                coord = triangleCoord;
                countRow = (int)(heightY / cellWidth) * 2;
                break;
            case RHOMBUS:
                vertexCount = rhombusVertexCount;
                coord = rhombusCoord;
                countRow = (int)(heightY / cellWidth) * 2 - 1;
                break;
            case HEX:
                vertexCount = hexVertexCount;
                coord = hexCoord;
                cellWidth = widthX/countCol/1.5f;
                innerR = cellWidth * (float) Math.sqrt(3) / 2.0f; //внутренний радиус гекса
                //попробуем вычислять кол-во рядов исходя из размеров фигуры
                countRow = (int)(heightY / innerR) * 2;
                cellHeight = cellWidth;//heightY/countRow*2.0f;
                leftX += cellWidth / 4; //сдвинем на половину радиуса, т.к. справа получается пустота шириной в радиус
                break;
        }

        for (int i = 0; i<phaseCount*vertexCount; i++) {
            coord[i*2] = coord[i*2]*cellWidth/100; //100 это ширина фигуры при прерасчете
            coord[i*2+1] = coord[i*2+1]*cellHeight/100; //100 это ширина фигуры при прерасчете
        }


        Random random = new Random();

        cells = new MyCell[countCol*countRow];

        float _x = 0.0f;
        float _y = 0.0f;
        boolean even = false; //четный ряд
        for (int i=0; i<countRow; i++) {
            for (int j=0; j<countCol; j++) {
                even = (i & 1) == 0; // четный ряд?
                cells[GetIndex(j, i)] = new MyCell();
                cells[GetIndex(j, i)].owner = 0;

                switch (cellShape) {
                    case RECTANGLE:
                        cells[GetIndex(j, i)].invertY = 1.0f;
                        _x = leftX + (float)j * cellWidth + cellWidth/2.0f;
                        _y = leftY + heightY - (float)i * cellHeight - cellHeight/2.0f;
                        break;
                    case TRIANGLE:
                        cells[GetIndex(j, i)].invertY = even ? -1.0f : 1.0f;
                        _x = leftX + (float)j * cellWidth + cellWidth/2.0f +
                                (even ? 0 : cellWidth/2); //для четных рядов сдвигаем
                        _y = leftY + heightY - (float)i * cellHeight/2.0f - cellHeight/2.0f +
                                (even ? 0.0f : cellHeight/2.0f); //для четных рядов сдвигаем
                        if (!even && j==(countCol-1)) {
                            cells[GetIndex(j, i)].owner = -1; //метим лишние(выходят за пределы экрана) ячейки
                        }
                        break;
                    case RHOMBUS:
                        cells[GetIndex(j, i)].invertY = 1.0f;
                        _x = leftX + (float)j * cellWidth + cellWidth/2.0f +
                                (even ? 0 : cellWidth/2); //для нечетных рядов сдвигаем
                        _y = leftY + heightY - (float)i * cellHeight/2.0f - cellHeight/2.0f;
                        if (!even && j==(countCol-1)) {
                            cells[GetIndex(j, i)].owner = -1; //метим лишние(выходят за пределы экрана) ячейки
                        }
                        break;
                    case HEX:
                        cells[GetIndex(j, i)].invertY = 1.0f;
                        _x = leftX + (float)j * cellWidth/2.0f*3 + cellWidth/2.0f +
                                (even ? 0 : cellWidth/2.0f*1.5f); //для нечетных рядов сдвигаем
                        //_y = leftY + heightY - (float)i * cellHeight/2.0f - cellHeight/2.0f;
                        _y = leftY + heightY - (float)i * innerR/2.0f - innerR/2.0f; //т.к. гекс по высоте меньше чем по ширине, то используем в расчетах внутренний радиус
                        if (!even && j==(countCol-1)) {
                            cells[GetIndex(j, i)].owner = -1; //метим лишние(выходят за пределы экрана) ячейки
                        }
                        break;
                }

                cells[GetIndex(j, i)].color = MyCell.colorArr[random.nextInt(MyCell.ColorCount)];
                cells[GetIndex(j, i)].phaseIdx = 0;
                cells[GetIndex(j, i)].animDuration = 0f;
                cells[GetIndex(j, i)].setPosition(_x,_y);
            }
        }




    }

    private int GetIndex (int col, int row) {
        return row*countCol + col;
    }


    @Override
    public void draw(Batch batch, float alpha) {
        float locX, locY, invertY, deltaTime;
        int idx;

        super.draw(batch, alpha);

        deltaTime = Gdx.graphics.getDeltaTime();
        //locGroup.act(Gdx.graphics.getDeltaTime());
        locScreen.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //locScreen.shapeRenderer.triangle();

//        locScreen.shapeRenderer.rect(leftX, leftY, widthX, heightY);
        for (int i=0; i<countRow; i++) {
            for (int j=0; j<countCol; j++) {
                if (cells[GetIndex(j, i)].owner == -1) {continue;} //owner=-1 неотображаемые ячейки (лишние)

                //для теста анимации
                if ((i==10 || i==11) && j==10 && cells[GetIndex(j, i)].phaseIdx==-1) {
                    cells[GetIndex(j, i)].phaseIdx = 0;
                }
                //для теста анимации

                if (cells[GetIndex(j, i)].phaseIdx != -1) { //-1 состояние покоя и фаза 0
                    cells[GetIndex(j, i)].animDuration += deltaTime;
                    cells[GetIndex(j, i)].phaseIdx = (int)(cells[GetIndex(j, i)].animDuration/(animationSpeed/(float)phaseCount));
                    if (cells[GetIndex(j, i)].phaseIdx >= phaseCount) {
                        cells[GetIndex(j, i)].phaseIdx = -1;
                        cells[GetIndex(j, i)].animDuration = 0.0f;
                    }

                }

                idx = (cells[GetIndex(j, i)].phaseIdx==-1 ? 0 : cells[GetIndex(j, i)].phaseIdx) * vertexCount*2; //номер фазы анимации * vetrexCount * 2 (это x и y)
                locX = cells[GetIndex(j, i)].x;
                locY = cells[GetIndex(j, i)].y;
                invertY = cells[GetIndex(j, i)].invertY;

                //отрисуем фигуру заполненными треугольниками
                //сначала отрисуем полную фигуру
                locScreen.shapeRenderer.setColor(MyCell.borderColor);
                for (int k=2; k<vertexCount; k++) {
                    locScreen.shapeRenderer.triangle(
                            locX + coord[idx + 0], locY + coord[idx + 1] * invertY,
                            locX + coord[idx + k*2], locY + coord[idx + k*2+1] * invertY,
                            locX + coord[idx + (k-1)*2], locY + coord[idx + (k-1)*2+1] * invertY);
                }

                //теперь чуть меньшую, чтоб получился контур
                float scale = 0.9f;
                locScreen.shapeRenderer.setColor(cells[GetIndex(j, i)].color);
                for (int k=2; k<vertexCount; k++) {
                    locScreen.shapeRenderer.triangle(
                            locX + coord[idx + 0] * scale, locY + coord[idx + 1] * scale * invertY,
                            locX + coord[idx + k*2] * scale, locY + coord[idx + k*2+1] * scale * invertY,
                            locX + coord[idx + (k-1)*2] * scale, locY + coord[idx + (k-1)*2+1] * scale * invertY);
                }

            }
        }

        locScreen.shapeRenderer.line(0,0, 100,100);

        locScreen.shapeRenderer.end();
    }
}
