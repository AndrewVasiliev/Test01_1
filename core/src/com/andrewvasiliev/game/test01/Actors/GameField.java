package com.andrewvasiliev.game.test01.Actors;

import com.andrewvasiliev.game.test01.Classes.Const;
import com.andrewvasiliev.game.test01.Classes.MyCell;
import com.andrewvasiliev.game.test01.Screens.GameFieldScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Queue;

import java.util.Arrays;
import java.util.Random;


/**
 * Created by ava on 23.12.16.
 */

public class GameField extends Actor {
    private float leftX, leftY, widthX, heightY, cellWidth, cellHeight;
    private int countCol, countRow;
    private GameFieldScreen locScreen;
    public MyCell[] cells;
    private float innerR;
    private ShapeRenderer shapeRenderer;
    public int NOBODYCELL = -1; //ячейка никому не принадлежит
    private int WASTECELL = -2; //лишняя ячейка. не отображается. присутствуют в гексах
    private int maxNearby; //количество соседних ячеек. зависит от формы ячеек

    private Const.CellShape cellShape;

    //квадрат
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

    public GameField(GameFieldScreen gameFieldScreen, float x, float y, float width, float height) {
        locScreen = gameFieldScreen;
        leftX = x;
        leftY = y;
        widthX = width;
        heightY = height;
        countCol = 0;
        countRow = 0;
        //shapeRenderer = new ShapeRenderer();
        shapeRenderer = locScreen.shapeRenderer;
    }

    public void GenerateField (int countColIn, Const.CellShape cellType) {
        countCol = countColIn;

        cellWidth = widthX/countCol;
        cellHeight = cellWidth;

        cellShape = cellType;

        switch (cellShape) {
            case RECTANGLE:
                maxNearby = 4;
                vertexCount = rectangleVertexCount;
                coord = Arrays.copyOf(rectangleCoord, rectangleCoord.length);
                countRow = (int)(heightY / cellWidth);
                break;
            case TRIANGLE:
                maxNearby = 4;
                vertexCount = triangleVertexCount;
                coord = Arrays.copyOf(triangleCoord, triangleCoord.length);
                countRow = (int)(heightY / cellWidth) * 2;
                break;
            case RHOMBUS:
                maxNearby = 4;
                vertexCount = rhombusVertexCount;
                coord = Arrays.copyOf(rhombusCoord, rhombusCoord.length);
                countRow = (int)(heightY / cellWidth) * 2 - 1;
                break;
            case HEX:
                maxNearby = 6;
                vertexCount = hexVertexCount;
                coord = Arrays.copyOf(hexCoord, hexCoord.length);
                cellWidth = widthX/countCol/1.5f;
                innerR = cellWidth * (float) Math.sqrt(3) / 2.0f; //внутренний радиус гекса
                //попробуем вычислять кол-во рядов исходя из размеров фигуры
                countRow = (int)(heightY / innerR) * 2;
                cellHeight = cellWidth;//heightY/countRow*2.0f;
                //leftX += cellWidth / 4; //сдвинем на половину радиуса, т.к. справа получается пустота шириной в радиус
                break;
        }

        for (int i = 0; i<phaseCount*vertexCount; i++) {
            coord[i*2] = coord[i*2]*cellWidth/100; //100 это ширина фигуры при прерасчете
            coord[i*2+1] = coord[i*2+1]*cellHeight/100; //100 это ширина фигуры при прерасчете
        }


        Random random = new Random();

        cells = new MyCell[countCol * countRow];
        for (int i=0; i<cells.length; i++) {
            cells[i] = new MyCell();
        }

        float _x = 0.0f;
        float _y = 0.0f;
        boolean even; //четный ряд
        int currIdx;
        int nx,ny;  //координаты возможных соседей для ячейки
        int firstPlrIdx = 0, secondPlrIdx = 0;
        for (int i=0; i<countRow; i++) {
            for (int j=0; j<countCol; j++) {
                even = (i & 1) == 0; // четный ряд?
                currIdx = GetIndex(j, i);
                //cells[currIdx] = new MyCell();
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
                                (even ? 0 : cellWidth/2); //для четных рядов сдвигаем
                        _y = leftY + heightY - (float)i * cellHeight/2.0f - cellHeight/2.0f +
                                (even ? 0.0f : cellHeight/2.0f); //для четных рядов сдвигаем
                        if (!even && j==(countCol-1)) {
                            cells[currIdx].owner = WASTECELL; //метим лишние(выходят за пределы экрана) ячейки
                        }
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
                                (even ? 0 : cellWidth/2); //для нечетных рядов сдвигаем
                        _y = leftY + heightY - (float)i * cellHeight/2.0f - cellHeight/2.0f;
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
                        _x = leftX + cellWidth/4.0f + (float)j * cellWidth/2.0f*3 + cellWidth/2.0f +
                                (even ? 0 : cellWidth/2.0f*1.5f); //для нечетных рядов сдвигаем
                        //_y = leftY + heightY - (float)i * cellHeight/2.0f - cellHeight/2.0f;
                        _y = leftY + heightY - (float)i * innerR/2.0f - innerR/2.0f; //т.к. гекс по высоте меньше чем по ширине, то используем в расчетах внутренний радиус
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
                //установим угловые ячеки как стартовые позиции
                if (i==(countRow-1) && j==0) { //левый нижний угол
                    cells[currIdx].owner = 0;
                    locScreen.locGame.plr[0].colorIdx = cells[currIdx].colorIdx;
                    firstPlrIdx = currIdx;
                }
                if (i==0 && j==(countCol-1)) { //правый верхний угол
                    cells[currIdx].owner = 1;
                    locScreen.locGame.plr[1].colorIdx = cells[currIdx].colorIdx;
                    secondPlrIdx = currIdx;
                }


            }
        }
        //скорректируем цвета игроков, чтобы они не совпадали друг с другом
        if (cells[firstPlrIdx].colorIdx == cells[secondPlrIdx].colorIdx) {
            cells[secondPlrIdx].colorIdx++;
            if (cells[secondPlrIdx].colorIdx >= Const.ColorCount) {
                cells[secondPlrIdx].colorIdx = 0;
            }
            cells[secondPlrIdx].colorIdxNext =  cells[secondPlrIdx].colorIdx;
            locScreen.locGame.plr[1].colorIdx = cells[secondPlrIdx].colorIdx;
        }
        //скорректируем цвета соседних ячеек, чтобы они не совпадали со стартовыми цветами игроков
        //для игрока 1
        for (int k=0; k<maxNearby; k++) {
            if (cells[firstPlrIdx].nearby[k] == -1) {continue;}
            int nearbyIdx = cells[firstPlrIdx].nearby[k];
            if (cells[nearbyIdx].colorIdx == cells[firstPlrIdx].colorIdx) {
                cells[nearbyIdx].colorIdx++;
                if (cells[nearbyIdx].colorIdx >= Const.ColorCount) {
                    cells[nearbyIdx].colorIdx = 0;
                }
                cells[nearbyIdx].colorIdxNext =  cells[nearbyIdx].colorIdx;
            }
        }
        //для игрока 2
        for (int k=0; k<maxNearby; k++) {
            if (cells[secondPlrIdx].nearby[k] == -1) {continue;}
            int nearbyIdx = cells[secondPlrIdx].nearby[k];
            if (cells[nearbyIdx].colorIdx == cells[secondPlrIdx].colorIdx) {
                cells[nearbyIdx].colorIdx++;
                if (cells[nearbyIdx].colorIdx >= Const.ColorCount) {
                    cells[nearbyIdx].colorIdx = 0;
                }
                cells[nearbyIdx].colorIdxNext =  cells[nearbyIdx].colorIdx;
            }
        }
    }

    private int GetIndex (int col, int row) {
        return row*countCol + col;
    }

    private void DrawShape (float locX, float locY, float invertY, float scale, int idx) {
        for (int k=2; k<vertexCount; k++) {
            shapeRenderer.triangle(
                    locX + coord[idx] * scale * invertY, locY + coord[idx + 1] * scale * invertY,
                    locX + coord[idx + k*2] * scale * invertY, locY + coord[idx + k*2+1] * scale * invertY,
                    locX + coord[idx + (k-1)*2] * scale * invertY, locY + coord[idx + (k-1)*2+1] * scale * invertY);
        }
    }

    @Override
    public void draw(Batch batch, float alpha) {
        float locX, locY, invertY, deltaTime;
        int idx;

        batch.end();
       // shapeRenderer.setProjectionMatrix(locScreen.locGame.camera.combined);

        deltaTime = Gdx.graphics.getDeltaTime();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        int currIdx;
        float scale;
        for (int i=0; i<countRow; i++) {
            for (int j=0; j<countCol; j++) {
                currIdx = GetIndex(j, i);
                if (cells[currIdx].owner == WASTECELL) {continue;} //неотображаемые ячейки (лишние)


                if (cells[currIdx].phaseIdx != -1) { //-1 состояние покоя и фаза 0
                    cells[currIdx].animDuration += deltaTime;
                    cells[currIdx].phaseIdx = (int)(cells[currIdx].animDuration/(animationSpeed/(float)phaseCount));
                    if (cells[currIdx].phaseIdx >= phaseCount) {
                        cells[currIdx].phaseIdx = -1;
                        cells[currIdx].animDuration = 0.0f;
                        cells[currIdx].colorIdx = cells[currIdx].colorIdxNext;
                    }
                }

                idx = (cells[currIdx].phaseIdx==-1 ? 0 : cells[currIdx].phaseIdx) * vertexCount*2; //номер фазы анимации * vetrexCount * 2 (это x и y)
                locX = cells[currIdx].x;
                locY = cells[currIdx].y;
                invertY = cells[currIdx].invertY;

                //отрисуем фигуру заполненными треугольниками
                //сначала отрисуем полную фигуру
                scale = 1.01f;
                if (locScreen.currentPlayer == cells[currIdx].owner) {
                    shapeRenderer.setColor(Color.WHITE); // ячейка активного игрока
                } else {
                    shapeRenderer.setColor(Const.borderColor);
                }
                DrawShape(locX, locY, invertY, scale, idx);

                //теперь чуть меньшую, чтоб получился контур
                scale = 0.9f;

                if ((cells[currIdx].owner != NOBODYCELL) && (cells[currIdx].phaseIdx == -1)) {
                    scale = 1.01f;
                }

                if (cells[currIdx].phaseIdx >= phaseCount/2) {
                    //цвет с другой стороны
                    shapeRenderer.setColor(Const.colorArr[cells[currIdx].colorIdxNext]);
                } else {
                    shapeRenderer.setColor(Const.colorArr[cells[currIdx].colorIdx]);
                }
                DrawShape(locX, locY, invertY, scale, idx);
            }
        }

        shapeRenderer.end();
        batch.begin();
        super.draw(batch, alpha);    }

    public void dispose()   {
        shapeRenderer.dispose();
    }

    public int CountScore(int playerIdx, MyCell[] locCells) {
        int score = 0;
        for (int i=0; i<countCol*countRow; i++)
            if (locCells[i].owner == playerIdx) {score++;}
        return score;
    }

    public void FillColor (int colorIn, int locPlayerIdx, MyCell[] locCells) {
        MyCell ce;
        Queue<MyCell> qe = new Queue();
        for (int i=0; i<countCol*countRow; i++)
            if (locCells[i].owner == locPlayerIdx) {
                locCells[i].colorIdxNext = colorIn; //устанавливаем новый цвет
                locCells[i].phaseIdx = 0; //устанавливаем начальную фазу анимации
                qe.addLast(locCells[i]);
            }
        while (qe.size > 0) {
            ce = qe.removeFirst();
            for (int k=0; k<maxNearby; k++) {
                if (ce.nearby[k] == -1) {continue;}
                if ((locCells[ce.nearby[k]].owner == NOBODYCELL) && (locCells[ce.nearby[k]].colorIdx == colorIn)) {
                    //если ячейка свободна и ее цвет совпадает с цветом "хода", то присвоим ее
                    locCells[ce.nearby[k]].owner = locPlayerIdx;
                    locCells[ce.nearby[k]].phaseIdx = 0;
                    qe.addLast(locCells[ce.nearby[k]]); //добавим ее в очередь, вдруг и у нее есть наши "соседи"
                }
            }
        }
    }

    public void PlayerMove(int colorIdx) {
        //ход очередного игрока цветом colorIn
        int playerIdx = locScreen.currentPlayer;

        locScreen.locGame.plr[playerIdx].colorIdx = colorIdx;
        FillColor(colorIdx, playerIdx, cells);

        locScreen.locGame.plr[playerIdx].score = CountScore(playerIdx, cells);

    }

    public boolean isPossibleMoves (int playerIdx) {
        for (int i=0; i<countCol*countRow; i++)
            if (cells[i].owner == playerIdx) {
                for (int k=0; k<maxNearby; k++) {
                    if (cells[i].nearby[k] == -1) {continue;}
                    if (cells[cells[i].nearby[k]].owner == NOBODYCELL) {
                        //у игрока еще есть возможность для расширения его владений
                        return true;
                    }
                }
            }
        //игрок уже захватил все, доступное ему пространство.
        return false;
    }
}
