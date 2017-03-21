package com.andrewvasiliev.game.test01.Actors;

import com.andrewvasiliev.game.test01.Classes.BaseCell;
import com.andrewvasiliev.game.test01.Classes.Const;
import com.andrewvasiliev.game.test01.Classes.MyCell;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Andrew on 12.03.2017.
 */

public class BackgroundField  extends Actor {
    private ShapeRenderer sr;
    private float leftX, leftY, widthX, heightY, cellWidth, cellHeight;
    private int countCol, countRow;
    private int phaseCount;
    private float animationSpeed = 1.0f; //0.5f; //за сколько секунд должна закончиться анимация
    private Const.CellShape cellShape;
    private BaseCell cell;
    private int NOBODYCELL = -1; //ячейка никому не принадлежит
    private int WASTECELL = -2; //лишняя ячейка. не отображается. присутствуют в гексах
    private int maxNearby; //количество соседних ячеек. зависит от формы ячеек
    private MyCell[] cells;
    private float innerR;

    public BackgroundField(ShapeRenderer inSR, float x, float y, float width, float height) {
        leftX = x;
        leftY = y;
        widthX = width;
        heightY = height;
        countCol = 0;
        countRow = 0;
        sr = inSR;
        //phaseCount = 16;
        setBounds(x, y, width, height);

        addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                System.out.println("touchDown " + x + ", " + y);
                TouchDown(x, y);

            }

            public boolean longPress (Actor actor, float x, float y) {
                System.out.println("long press " + x + ", " + y);
                return true;
            }

            public void fling (InputEvent event, float velocityX, float velocityY, int button) {
                System.out.println("fling " + velocityX + ", " + velocityY);
            }

            public void zoom (InputEvent event, float initialDistance, float distance) {
                System.out.println("zoom " + initialDistance + ", " + distance);
            }
        });
    }

    public void GenerateField (int countColIn, Const.CellShape cellType) {
        countCol = countColIn;

        cellWidth = widthX/countCol;
        cellHeight = cellWidth;

        cellShape = cellType;

        switch (cellShape) {
            case RECTANGLE:
                maxNearby = 4;
                countRow = (int)(heightY / cellWidth);
                break;
            case TRIANGLE:
                maxNearby = 4;
                countRow = (int)(heightY / cellWidth) * 2;
                countCol += 1; //увеличим чтобы заполнить весь экран
                break;
            case RHOMBUS:
                maxNearby = 4;
                countRow = (int)(heightY / cellWidth) * 2 +1;
                countCol += 1;
                break;
            case HEX:
                maxNearby = 6;
                cellWidth = widthX/countCol/1.5f;
                innerR = cellWidth * (float) Math.sqrt(3) / 2.0f; //внутренний радиус гекса
                //попробуем вычислять кол-во рядов исходя из размеров фигуры
                countRow = (int)(heightY / innerR) * 2 + 3;
                countCol += 1;
                //cellHeight = cellWidth;//heightY/countRow*2.0f;
                //leftX += cellWidth / 4; //сдвинем на половину радиуса, т.к. справа получается пустота шириной в радиус
                break;
        }

        cell = new BaseCell(cellShape, cellWidth/*, cellHeight*/);
        phaseCount = cell.GetPhaseCount();
        cellHeight = (float)Math.floor(cell.GetHeight());


        Random random = new Random();

//для теста
        //countCol = 3;
        //countRow = 1;
//для теста

        cells = new MyCell[countCol * countRow];
        for (int i=0; i<cells.length; i++) {
            cells[i] = new MyCell();
        }

        float _x = 0.0f;
        float _y = 0.0f;
        boolean even; //четный ряд
        int currIdx;
        int nx,ny;  //координаты возможных соседей для ячейки
        for (int i=0; i<countRow; i++) {
            for (int j=0; j<countCol; j++) {
                even = (i & 1) == 0; // четный ряд?
                currIdx = GetIndex(j, i);
                cells[currIdx].owner = NOBODYCELL; // никому не принадлежит

                cells[currIdx].nearby = new int[maxNearby]; //массив индексов соседних ячеек

                switch (cellShape) {
                    case RECTANGLE:
                        cells[currIdx].invertY = 1.0f;
                        _x = leftX + (float)j * cellWidth + cellWidth/2.0f;
                        _y = leftY + heightY - (float)i * cellHeight - cellHeight/2.0f;
                        //заполним индексы соседних ячеек
                        for (int k=0; k<maxNearby; k++) {
                            nx = j;
                            ny = i;
                            switch (k) {
                                case 0: nx--; break;
                                case 1: ny--; break;
                                case 2: nx++; break;
                                case 3: ny++; break;
                            }
                            cells[currIdx].nearby[k] = (nx>=0 && nx <countCol && ny>=0 && ny<countRow) ? GetIndex(nx, ny) : -1;
                        }
                        break;
                    case TRIANGLE:
                        cells[currIdx].invertY = even ? -1.0f : 1.0f;
                        _x = leftX + (float)j * cellWidth + cellWidth/2.0f +
                                (even ? 0 : cellWidth/2) - cellWidth/2; //для четных рядов сдвигаем и просто сдвигаем влево, чтобы заполнить весь єкран
                        _y = leftY + heightY - (float)Math.floor(even ? (float)Math.sqrt(3) * cellWidth / 6f : (float)Math.sqrt(3) * cellWidth / 3f) -
                                (float)(i/2) * cellHeight;
                        /*if (!even && j==(countCol-1)) {
                            cells[currIdx].owner = WASTECELL; //метим лишние(выходят за пределы экрана) ячейки
                        }*/
                        //заполним индексы соседних ячеек
                        for (int k=0; k<maxNearby; k++) {
                            nx = j;
                            ny = i;
                            if (even) { //четная строка. перевернутая ячейка
                                switch (k) {
                                    case 0: nx--; ny++; break;
                                    case 1: ny++; break;
                                    case 2: ny--; break;
                                    case 3: ny--; nx--; break;
                                }
                            } else { //нечетная строка.
                                switch (k) {
                                    case 0: ny--; break;
                                    case 1: nx++; ny--; break;
                                    case 2: nx++; ny++; break;
                                    case 3: ny++; break;
                                }
                            }
                            cells[currIdx].nearby[k] = (nx>=0 && nx <countCol && ny>=0 && ny<countRow) ? GetIndex(nx, ny) : -1;
                        }
                        break;
                    case RHOMBUS:
                        cells[currIdx].invertY = 1.0f;
                        _x = leftX + (float)j * cellWidth + cellWidth/2.0f +
                                (even ? 0 : cellWidth/2) - cellWidth/2; //для нечетных рядов сдвигаем
                        _y = leftY + heightY - (float)i * cellHeight/2.0f - cellHeight/2.0f + cellHeight/2.0f;
                        if (!even && j==(countCol-1)) {
                            cells[currIdx].owner = WASTECELL; //метим лишние(выходят за пределы экрана) ячейки
                        }
                        //заполним индексы соседних ячеек
                        for (int k=0; k<maxNearby; k++) {
                            nx = j;
                            ny = i;
                            if (even) { //четная строка.
                                switch (k) {
                                    case 0: nx--; ny++; break;
                                    case 1: nx--; ny--; break;
                                    case 2: ny--; break;
                                    case 3: ny++; break;
                                }
                            } else { //нечетная строка.
                                switch (k) {
                                    case 0: ny++; break;
                                    case 1: ny--; break;
                                    case 2: nx++; ny--; break;
                                    case 3: nx++; ny++; break;
                                }
                            }
                            cells[currIdx].nearby[k] = (nx>=0 && nx <countCol && ny>=0 && ny<countRow) ? GetIndex(nx, ny) : -1;
                        }
                        break;
                    case HEX:
                        cells[currIdx].invertY = 1.0f;
                        _x = leftX + (float)j * cellWidth/2.0f*3 +
                                (even ? 0 : cellWidth/2.0f*1.5f); //для нечетных рядов сдвигаем
                        //_y = leftY + heightY - (float)i * cellHeight/2.0f - cellHeight/2.0f;
                        _y = leftY + heightY - (float)i * innerR/2.0f; //т.к. гекс по высоте меньше чем по ширине, то используем в расчетах внутренний радиус
                        /*if (!even && j==(countCol-1)) {
                            cells[currIdx].owner = WASTECELL; //метим лишние(выходят за пределы экрана) ячейки
                        }*/
                        //заполним индексы соседних ячеек
                        for (int k=0; k<maxNearby; k++) {
                            nx = j;
                            ny = i;
                            if (even) { //четная строка.
                                switch (k) {
                                    case 0: nx--; ny++; break;
                                    case 1: nx--; ny--; break;
                                    case 2: ny-=2; break;
                                    case 3: ny--; break;
                                    case 4: ny++; break;
                                    case 5: ny+=2; break;
                                }
                            } else { //нечетная строка.
                                switch (k) {
                                    case 0: ny++; break;
                                    case 1: ny--; break;
                                    case 2: ny-=2; break;
                                    case 3: nx++; ny--; break;
                                    case 4: nx++; ny++; break;
                                    case 5: ny+=2; break;
                                }
                            }
                            cells[currIdx].nearby[k] = (nx>=0 && nx <countCol && ny>=0 && ny<countRow) ? GetIndex(nx, ny) : -1;
                        }
                        break;
                }

                cells[currIdx].colorIdx = random.nextInt(Const.ColorCount);
                cells[currIdx].colorIdxNext = cells[currIdx].colorIdx;
                cells[currIdx].phaseIdx = 0;
                cells[currIdx].animDuration = 0f;
                cells[currIdx].setPosition(_x,_y);
            }
        }
    }

    private int GetIndex (int col, int row) {
        return row*countCol + col;
    }

    private void TouchDown(float _x, float _y) {
        //найдем, примерно, в какую ячейку кликнули
        float cellHalfWidth = cellWidth / 2;
        float cellHalfHeight = cellHeight / 2;
        int idx = -1;
        for (int i=0; i<cells.length; i++) {
            if ((cells[i].x - cellHalfWidth <= _x) && (cells[i].x + cellHalfWidth >= _x)
                    && (cells[i].y - cellHalfHeight <= _y) && (cells[i].y + cellHalfHeight >= _y)) {
                idx = i;
                break;
            }
        }
        if (idx == -1) {
            return;
        }
        cells[idx].phaseIdx = 0;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        float locDelta = Gdx.graphics.getDeltaTime();
        batch.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
        for (int i=0; i<cells.length; i++) {
            if (cells[i].owner == WASTECELL) {continue;} //неотображаемые ячейки (лишние)

            if (cells[i].phaseIdx != -1) { //-1 состояние покоя и фаза 0
                cells[i].animDuration += locDelta;
                cells[i].phaseIdx = (int)(cells[i].animDuration/(animationSpeed/(float)phaseCount));
                if (cells[i].phaseIdx >= phaseCount) {

                    cells[i].phaseIdx = -1;
                    cells[i].animDuration = 0.0f;

                    //для теста бесконечного вращения (начало)
                    //cells[i].phaseIdx = 0;
                    //cells[i].animDuration -= animationSpeed;
                    //для теста (конец)


                    cells[i].colorIdx = cells[i].colorIdxNext;
                }
            }

            cell.draw(cells[i].x, cells[i].y, cells[i].invertY, cells[i].phaseIdx, cells[i].colorIdx, cells[i].colorIdxNext, sr, Const.borderColor);
        }
        sr.end();
        batch.begin();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return super.hit(x, y, touchable);
        //return x > 0 && x < getWidth() && y > 0 && y < getHeight() ? this : null;
    }
}
