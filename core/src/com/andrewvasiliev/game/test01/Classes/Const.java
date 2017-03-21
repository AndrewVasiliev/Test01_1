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
/*
    //квадрат
    public static float rectangleCoord[] = {-50,-50 ,50,-50 ,50,50 ,-50,50 ,-55,-56 ,44,-45 ,44,45 ,-55,56 ,-57,-63 ,38,-42 ,38,42 ,-57,63 ,-57,-71 ,31,-39 ,31,39 ,-57,71 ,-53,-80 ,24,-36 ,24,36 ,-53,80 ,-44,-88 ,17,-35 ,17,35 ,-44,88 ,-29,-95 ,10,-34 ,10,34 ,-29,95 ,-10,-99 ,3,-33 ,3,33 ,-10,99 ,10,-99 ,-3,-33 ,-3,33 ,10,99 ,29,-95 ,-10,-34 ,-10,34 ,29,95 ,44,-88 ,-17,-35 ,-17,35 ,44,88 ,53,-80 ,-24,-36 ,-24,36 ,53,80 ,57,-71 ,-31,-39 ,-31,39 ,57,71 ,57,-63 ,-38,-42 ,-38,42 ,57,63 ,55,-56 ,-44,-45 ,-44,45 ,55,56 ,50,-50 ,-50,-50 ,-50,50 ,50,50};
    public static int rectangleVertexCount = 4;

    //треугольник
    public static float triangleCoord[] = {-50,-50 ,0,50 ,50,-50 ,-55,-56 ,0,50 ,44,-45 ,-57,-63 ,0,50 ,38,-42 ,-57,-71 ,0,50 ,31,-39 ,-53,-80 ,0,50 ,24,-36 ,-44,-88 ,0,50 ,17,-35 ,-29,-95 ,0,50 ,10,-34 ,-10,-99 ,0,50 ,3,-33 ,10,-99 ,0,50 ,-3,-33 ,29,-95 ,0,50 ,-10,-34 ,44,-88 ,0,50 ,-17,-35 ,53,-80 ,0,50 ,-24,-36 ,57,-71 ,0,50 ,-31,-39 ,57,-63 ,0,50 ,-38,-42 ,55,-56 ,0,50 ,-44,-45 ,50,-50 ,0,50 ,-50,-50};
    public static int triangleVertexCount = 3;

    //ромб
    public static float rhombusCoord[] = {-50,0 ,0,50 ,50,0 ,0,-50 ,-55,0 ,0,50 ,44,0 ,0,-50 ,-57,0 ,0,50 ,38,0 ,0,-50 ,-57,0 ,0,50 ,31,0 ,0,-50 ,-53,0 ,0,50 ,24,0 ,0,-50 ,-44,0 ,0,50 ,17,0 ,0,-50 ,-29,0 ,0,50 ,10,0 ,0,-50 ,-10,0 ,0,50 ,3,0 ,0,-50 ,10,0 ,0,50 ,-3,0 ,0,-50 ,29,0 ,0,50 ,-10,0 ,0,-50 ,44,0 ,0,50 ,-17,0 ,0,-50 ,53,0 ,0,50 ,-24,0 ,0,-50 ,57,0 ,0,50 ,-31,0 ,0,-50 ,57,0 ,0,50 ,-38,0 ,0,-50 ,55,0 ,0,50 ,-44,0 ,0,-50 ,50,0 ,0,50 ,-50,0 ,0,-50};
    public static int rhombusVertexCount = 4;

    //гексагон
    public static float hexCoord[] = {50,0 ,25,43 ,-25,43 ,-50,0 ,-25,-43 ,25,-43 ,44,0 ,23,41 ,-26,46 ,-55,0 ,-26,-46 ,23,-41 ,38,0 ,21,39 ,-25,48 ,-57,0 ,-25,-48 ,21,-39 ,31,0 ,18,38 ,-24,51 ,-57,0 ,-24,-51 ,18,-38 ,24,0 ,14,37 ,-21,53 ,-53,0 ,-21,-53 ,14,-37 ,17,0 ,10,36 ,-16,55 ,-44,0 ,-16,-55 ,10,-36 ,10,0 ,6,35 ,-10,57 ,-29,0 ,-10,-57 ,6,-35 ,3,0 ,2,35 ,-3,58 ,-10,0 ,-3,-58 ,2,-35 ,-3,0 ,-2,35 ,3,58 ,10,0 ,3,-58 ,-2,-35 ,-10,0 ,-6,35 ,10,57 ,29,0 ,10,-57 ,-6,-35 ,-17,0 ,-10,36 ,16,55 ,44,0 ,16,-55 ,-10,-36 ,-24,0 ,-14,37 ,21,53 ,53,0 ,21,-53 ,-14,-37 ,-31,0 ,-18,38 ,24,51 ,57,0 ,24,-51 ,-18,-38 ,-38,0 ,-21,39 ,25,48 ,57,0 ,25,-48 ,-21,-39 ,-44,0 ,-23,41 ,26,46 ,55,0 ,26,-46 ,-23,-41 ,-50,0 ,-25,43 ,25,43 ,50,0 ,25,-43 ,-25,-43};
    public static int hexVertexCount = 6;

    public static int phaseCount = 16;
*/
    public static String PreferencesName = "SavedGame";
}
