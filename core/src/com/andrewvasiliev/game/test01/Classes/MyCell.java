package com.andrewvasiliev.game.test01.Classes;

import com.badlogic.gdx.graphics.Color;



/**
 * Created by ava on 23.12.16.
 */


public class MyCell {
    public float x,y;       //координаты центра ячейки
    public float invertY;
    public int phaseIdx;    //текущий номер фазы анимации
    public float animDuration;  //продолжительность текущей анимации (для расчета фазы, которую надо показать)
    public int colorIdx, colorIdxNext; //текущий и следующий (тот цвет которым походил игрок. для анимации) цвет ячейки
    public int owner;       //какому игроку принадлежит ячейка
    public int[] nearby;// = {-1, -1, -1, -1, -1, -1}; //массив индексов соседних ячеек для этой ячейки. -1 означает что сосед был бы за пределами поля

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
