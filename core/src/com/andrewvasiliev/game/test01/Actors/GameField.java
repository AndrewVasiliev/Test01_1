package com.andrewvasiliev.game.test01.Actors;

import com.andrewvasiliev.game.test01.Classes.MyCell;
import com.andrewvasiliev.game.test01.Screens.TestMainField;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;

import static com.badlogic.gdx.graphics.Color.BLACK;
import static com.badlogic.gdx.graphics.Color.BLUE;
import static com.badlogic.gdx.graphics.Color.CYAN;
import static com.badlogic.gdx.graphics.Color.GRAY;
import static com.badlogic.gdx.graphics.Color.GREEN;
import static com.badlogic.gdx.graphics.Color.LIGHT_GRAY;
import static com.badlogic.gdx.graphics.Color.MAGENTA;
import static com.badlogic.gdx.graphics.Color.ORANGE;
import static com.badlogic.gdx.graphics.Color.RED;
import static com.badlogic.gdx.graphics.Color.SKY;
import static com.badlogic.gdx.graphics.Color.WHITE;

/**
 * Created by ava on 23.12.16.
 */

public class GameField extends Actor {
    private float leftX, leftY, widthX, heightY, cellWidth, cellHeight;
    private int countCol, countRow;
    private TestMainField locScreen;
    private MyCell cells[];
    private Color[] colorArr = {MAGENTA, BLUE, CYAN,  GREEN, ORANGE, WHITE, RED};
    private Color borderColor = BLACK;
    private float coord[] = {-50,-50 ,50,-50 ,50,50 ,-50,50 ,-55,-56 ,44,-45 ,44,45 ,-55,56 ,-57,-63 ,38,-42 ,38,42 ,-57,63 ,-57,-71 ,31,-39 ,31,39 ,-57,71 ,-53,-80 ,24,-36 ,24,36 ,-53,80 ,-44,-88 ,17,-35 ,17,35 ,-44,88 ,-29,-95 ,10,-34 ,10,34 ,-29,95 ,-10,-99 ,3,-33 ,3,33 ,-10,99 ,10,-99 ,-3,-33 ,-3,33 ,10,99 ,29,-95 ,-10,-34 ,-10,34 ,29,95 ,44,-88 ,-17,-35 ,-17,35 ,44,88 ,53,-80 ,-24,-36 ,-24,36 ,53,80 ,57,-71 ,-31,-39 ,-31,39 ,57,71 ,57,-63 ,-38,-42 ,-38,42 ,57,63 ,55,-56 ,-44,-45 ,-44,45 ,55,56 ,50,-50 ,-50,-50 ,-50,50 ,50,50};
    private int phaseCount = 16;
    private float animationSpeed = 0.25f; //за сколько секунд должна закончиться анимация
    private int vertexCount = 4;

    public GameField(TestMainField testMainField, float x, float y, float width, float height, int countColIn, int countRowIn) {
        leftX = x;
        leftY = y;
        widthX = width;
        heightY = height;
        countCol = countColIn;
        countRow = countRowIn;
        locScreen = testMainField;

        cellWidth = widthX/countCol;
        cellHeight = heightY/countRow;

        for (int i = 0; i<phaseCount*vertexCount/2; i++) {
            coord[i*2] = coord[i*2]*cellWidth/100; //100 это ширина фигуры при прерасчете
            coord[i*2+1] = coord[i*2+1]*cellHeight/100; //100 это ширина фигуры при прерасчете
        }


        Random random = new Random();

        cells = new MyCell[countCol*countRow];
        for (int i=0; i<countRow; i++) {
            for (int j=0; j<countCol; j++) {
                cells[GetIndex(j, i)] = new MyCell();
                cells[GetIndex(j, i)].color = colorArr[random.nextInt(7)];
                cells[GetIndex(j, i)].owner = 0;
                cells[GetIndex(j, i)].phaseIdx = 0;
                cells[GetIndex(j, i)].animDuration = 0f;
                cells[GetIndex(j, i)].setPosition(
                        leftX + (float)j * cellWidth + cellWidth/2.0f,
                        leftY + heightY - (float)i * cellHeight - cellHeight/2.0f );
            }
        }

    }

    private int GetIndex (int col, int row) {
        return row*countCol + col;
    }


    @Override
    public void draw(Batch batch, float alpha) {
        float locX, locY, deltaTime;
        int idx;

        deltaTime = Gdx.graphics.getDeltaTime();
        //locGroup.act(Gdx.graphics.getDeltaTime());
        locScreen.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //locScreen.shapeRenderer.triangle();

//        locScreen.shapeRenderer.rect(leftX, leftY, widthX, heightY);
        for (int i=0; i<countRow; i++) {
            for (int j=0; j<countCol; j++) {

                //для теста анимации
                if (i==0 && j==0 && cells[GetIndex(j, i)].phaseIdx==-1) {
                    cells[GetIndex(j, i)].phaseIdx = 0;
                }
                //для теста анимации

                if (cells[GetIndex(j, i)].phaseIdx != -1) { //-1 состояние покоя и фаза 0
                    cells[GetIndex(j, i)].animDuration += deltaTime;
                    cells[GetIndex(j, i)].phaseIdx = (int)(cells[GetIndex(j, i)].animDuration/animationSpeed/phaseCount);
                    if (cells[GetIndex(j, i)].phaseIdx >= phaseCount) {
                        cells[GetIndex(j, i)].phaseIdx = -1;
                        cells[GetIndex(j, i)].animDuration = 0.0f;
                    }

                }

                idx = (cells[GetIndex(j, i)].phaseIdx==-1 ? 0 : cells[GetIndex(j, i)].phaseIdx) * vertexCount*2; //номер фазы анимации * vetrexCount * 2 (это x и y)
                locX = cells[GetIndex(j, i)].x;
                locY = cells[GetIndex(j, i)].y;

                //отрисуем фигуру заполненными треугольниками
                //сначала отрисуем полную фигуру
                locScreen.shapeRenderer.setColor(borderColor);
                for (int k=2; k<vertexCount; k++) {
                    locScreen.shapeRenderer.triangle(
                            locX + coord[idx + 0], locY + coord[idx + 1],
                            locX + coord[idx + k*2], locY + coord[idx + k*2+1],
                            locX + coord[idx + (k-1)*2], locY + coord[idx + (k-1)*2+1]);
                }

                //теперь чуть меньшую, чтоб получился контур
                float scale = 0.9f;
                locScreen.shapeRenderer.setColor(cells[GetIndex(j, i)].color);
                for (int k=2; k<vertexCount; k++) {
                    locScreen.shapeRenderer.triangle(
                            locX + coord[idx + 0] * scale, locY + coord[idx + 1] * scale,
                            locX + coord[idx + k*2] * scale, locY + coord[idx + k*2+1] * scale,
                            locX + coord[idx + (k-1)*2] * scale, locY + coord[idx + (k-1)*2+1] * scale);
                }

                //отрисуем контур фигуры, например белым цветом
                // !!! линиями не получится, т.к. из-за применения масштаба линии исчезают
/*                locScreen.shapeRenderer.setColor(WHITE);
                for (int k=0; k<(vertexCount-1); k++) {
                    locScreen.shapeRenderer.line(
                            locX + coord[idx + k*2], locY + coord[idx + k*2+1],
                            locX + coord[idx + (k+1)*2], locY + coord[idx + (k+1)*2+1]);
                }
                locScreen.shapeRenderer.line(
                        locX + coord[idx + 0], locY + coord[idx + 1],
                        locX + coord[idx + (vertexCount-1)*2], locY + coord[idx + (vertexCount-1)*2+1]);
*/


            }
        }

        //locScreen.shapeRenderer.line(0,10, 0,100);
        //locScreen.shapeRenderer.line(0,10, 1000,100);


        locScreen.shapeRenderer.end();

    }
}
