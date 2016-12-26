package com.andrewvasiliev.game.test01.Classes;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by ava on 23.12.16.
 */

public class MyCell {
    public float x,y;       //координаты центра ячейки
    public Color color;     //цвет ячейки
    public int owner;       //какому игроку принадлежит ячейка
    public int phaseIdx;    //текущий номер фазы анимации
    public float animDuration;  //продолжительность текущей анимации (для расчета фазы, которую надо показать)

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
