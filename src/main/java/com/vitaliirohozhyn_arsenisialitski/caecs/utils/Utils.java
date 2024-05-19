package com.vitaliirohozhyn_arsenisialitski.caecs.utils;

import java.awt.Color;
import java.util.Random;

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
}
