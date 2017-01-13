package com.andrewvasiliev.game.test01.Classes;

/**
 * Created by Andrew on 07.01.2017.
 */

public class Player {
    public boolean isAndroid; //признак компьютерного противника
    public int deepLevel; //количество ходов вперед, на которое Android будет просчитывать ход. Сложность противника.
    public String name; //имя игрока
    public int score; // количество набранных очков
    public int color; // текущий цвет игрока (индекс в массиве Const.Color)

    public void SetPlayer (String inName, int inScore, boolean inIsAndroid, int inDeepLevel, int inColor) {
        name = inName;
        score = inScore;
        isAndroid = inIsAndroid;
        deepLevel = inDeepLevel;
        color = inColor;
    }
}
