package com.vitaliirohozhyn_arsenisialitski.caecs.utils;

import java.awt.Color;
import java.util.Random;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECS;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Entity;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.PositionComponent;

public class Utils {
    public static Color generateRandomColorFromOriginal(Color a_original, int contrast) {
        Random rand = new Random();
        int newRed = generateNumberWithinRange(-10, 10) * contrast + a_original.getRed();
        newRed = clamp(newRed, 0, 255);
        int newGreen = generateNumberWithinRange(-10, 10) * contrast + a_original.getGreen();
        newGreen = clamp(newGreen, 0, 255);
        int newBlue = generateNumberWithinRange(-10, 10) * contrast + a_original.getBlue();
        newBlue = clamp(newBlue, 0, 255);
        Color fin = new Color(
                newRed,
                newGreen,
                newBlue);
        return fin;
    }

    public static int generateNumberWithinRange(int a_min, int a_max) {
        Random rand = new Random();
        return rand.nextInt(a_max - a_min + 1) + a_min;
    }

    public static int clamp(int val, int min, int max) {
        return val > max ? max : val < min ? min : val;
    }

    public static Entity getEntityAtCoordinates(ECS a_ecs, int a_x, int a_y) {
        return a_ecs.findFirstEntityByFilter((a_entity) -> {
            PositionComponent pos = a_entity.getFirstComponentOfType(PositionComponent.class);
            return (pos.x == a_x && pos.y == a_y);
        });
    }
}
