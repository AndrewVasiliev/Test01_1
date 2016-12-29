package com.andrewvasiliev.game.test01.Classes;

import com.badlogic.gdx.graphics.Color;

import static com.badlogic.gdx.graphics.Color.BLACK;
import static com.badlogic.gdx.graphics.Color.CHARTREUSE;
import static com.badlogic.gdx.graphics.Color.CYAN;
import static com.badlogic.gdx.graphics.Color.FOREST;
import static com.badlogic.gdx.graphics.Color.GREEN;
import static com.badlogic.gdx.graphics.Color.MAGENTA;
import static com.badlogic.gdx.graphics.Color.OLIVE;
import static com.badlogic.gdx.graphics.Color.ORANGE;
import static com.badlogic.gdx.graphics.Color.RED;
import static com.badlogic.gdx.graphics.Color.ROYAL;
import static com.badlogic.gdx.graphics.Color.WHITE;

/**
 * Created by ava on 23.12.16.
 */


public class MyCell {
    public enum CellShape { RECTANGLE, TRIANGLE, RHOMBUS, HEX };
    public static Color[] colorArr = {MAGENTA, /*BLUE*/ ROYAL, CYAN,  /*GREEN*/ CHARTREUSE, ORANGE, WHITE, RED};
    public static int ColorCount = 7;
    public static Color borderColor = BLACK;

    public float x,y;       //координаты центра ячейки
    public float invertY;
    public Color color;     //цвет ячейки
    public int owner;       //какому игроку принадлежит ячейка
    public int phaseIdx;    //текущий номер фазы анимации
    public float animDuration;  //продолжительность текущей анимации (для расчета фазы, которую надо показать)

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
