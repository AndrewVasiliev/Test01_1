package com.andrewvasiliev.game.test01.Classes;

/**
 * Created by Andrew on 07.01.2017.
 */

public class Player {
    public boolean isAndroid; //признак компьютерного противника
    public int deepLevel; //количество ходов вперед, на которое Android будет просчитывать ход. Сложность противника.
    public String name; //имя игрока
    public int score; // количество набранных очков
    public int colorIdx; // текущий цвет игрока (индекс в массиве Const.Color)
    //public int plrIdx; // индекс игрока

    public void SetPlayer (String inName, int inScore, boolean inIsAndroid, int inDeepLevel, int inColor/*, int inplrIdx*/) {
        name = inName;
        score = inScore;
        isAndroid = inIsAndroid;
        deepLevel = inDeepLevel;
        colorIdx = inColor;
        //plrIdx = inplrIdx;
    }
}
