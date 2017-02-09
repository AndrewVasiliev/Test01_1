package com.andrewvasiliev.game.test01.Classes;

import com.badlogic.gdx.graphics.Color;
import static com.badlogic.gdx.graphics.Color.BLACK;
import static com.badlogic.gdx.graphics.Color.BLUE;
import static com.badlogic.gdx.graphics.Color.BROWN;
import static com.badlogic.gdx.graphics.Color.CHARTREUSE;
import static com.badlogic.gdx.graphics.Color.CYAN;
import static com.badlogic.gdx.graphics.Color.FIREBRICK;
import static com.badlogic.gdx.graphics.Color.FOREST;
import static com.badlogic.gdx.graphics.Color.GRAY;
import static com.badlogic.gdx.graphics.Color.GREEN;
import static com.badlogic.gdx.graphics.Color.MAGENTA;
import static com.badlogic.gdx.graphics.Color.MAROON;
import static com.badlogic.gdx.graphics.Color.OLIVE;
import static com.badlogic.gdx.graphics.Color.ORANGE;
import static com.badlogic.gdx.graphics.Color.PINK;
import static com.badlogic.gdx.graphics.Color.PURPLE;
import static com.badlogic.gdx.graphics.Color.RED;
import static com.badlogic.gdx.graphics.Color.ROYAL;
import static com.badlogic.gdx.graphics.Color.TAN;
import static com.badlogic.gdx.graphics.Color.VIOLET;
import static com.badlogic.gdx.graphics.Color.WHITE;
/**
 * Created by Andrew on 07.01.2017.
 */

public class Const {
    public enum CellShape { RECTANGLE, TRIANGLE, RHOMBUS, HEX };
    public static Color[] colorArr = {
       //new Color(0xffE800ff), new Color(0x14A2D4ff), new Color(0xAF5E9Cff), new Color(0x00B16Aff), new Color(0xF7941Eff), new Color(0x00529Cff), Color.LIGHT_GRAY
       //new Color(0x999999ff), new Color(0x33FF00ff), new Color(0x00CC99ff), new Color(0x0099FFff), new Color(0x6633FFff), new Color(0xFF66FFff), new Color(0xFF6666ff)
       //new Color(0xFFCC33ff), new Color(0xCC3333ff), new Color(0xCC00CCff), new Color(0x6666CCff), new Color(0x00CCFFff), new Color(0x00FF99ff), new Color(0x66CC00ff)
       //new Color(0xFFCC33ff), new Color(0xFF0066ff), new Color(0x9933FFff), new Color(0x3366CCff), new Color(0x00CC99ff), new Color(0x66CC00ff), new Color(0x999999ff)
       //new Color(0x00A04Aff), new Color(0xFDDC0Dff), new Color(0xE01F3Dff), new Color(0x204496ff), new Color(0xB53AD4ff), new Color(0x34D0BAff), new Color(0x999999ff)
            //new Color(0xcc080cff), new Color(0xfabd02ff), new Color(0xfef200ff), new Color(0x66b132ff), new Color(0x12a6adff), new Color(0x013372ff), new Color(0x55176dff)
       new Color(0xfcf104ff), new Color(0xf79420ff), new Color(0xec1b3bff), new Color(0x5c2f91ff), new Color(0x0673baff), new Color(0x2bb34bff), new Color(0x999999ff)
       ///*MAGENTA*/ Color.PURPLE, /*BLUE*/ Color.ROYAL, Color.CYAN, /*GREEN*/ Color.CHARTREUSE, Color.ORANGE, /*WHITE*/ Color.GRAY, Color.RED
    };
    public static int ColorCount = 7;
    public static Color borderColor = Color.BLACK;
}
