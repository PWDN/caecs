package com.vitaliirohozhyn_arsenisialitski.caecs.utils;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECS;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Entity;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.MaterialStateComponent;
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

    public static <T extends Number & Comparable<T>> T clamp(T l, T min, T max) {
        return l.compareTo(max) > 0 ? max : l.compareTo(min) < 0 ? min : l;
    }

    public static int moveTowards(int cur, int fin, int spd) {
        return Math.abs(fin - cur) <= spd ? fin : cur + (int) (Math.signum(fin - cur)) * spd;
    }

    public static Color moveColorTowards(Color cur, Color fin, Color spd) {
        return new Color(
                Utils.clamp(Utils.moveTowards(cur.getRed(), fin.getRed(), spd.getRed()), 0, 255),
                Utils.clamp(Utils.moveTowards(cur.getGreen(), fin.getGreen(), spd.getGreen()), 0, 255),
                Utils.clamp(Utils.moveTowards(cur.getBlue(), fin.getBlue(), spd.getBlue()), 0, 255));
    }

    public static Entity getEntityAtCoordinates(ECS a_ecs, int a_x, int a_y) {
        return a_ecs.findFirstEntityByFilter((a_entity) -> {
            PositionComponent pos = a_entity.getFirstComponentOfType(PositionComponent.class);
            return (pos.x == a_x && pos.y == a_y);
        });
    }

    public static Point2D.Float vectorScale(Point2D.Float a_origVector, float a_scale) {
        return new Point2D.Float(
                a_origVector.x * a_scale,
                a_origVector.y * a_scale);
    }

    public static Point2D.Float vectorSum(Point2D.Float a_vector1, Point2D.Float a_vector2) {
        return new Point2D.Float(
                a_vector1.x + a_vector2.x,
                a_vector1.y + a_vector2.y);
    }

    public static HashSet<java.awt.Point> drawLineLow(java.awt.Point a_start, java.awt.Point a_end) {
        HashSet<java.awt.Point> res = new HashSet<java.awt.Point>();
        int dx = a_end.x - a_start.x;
        int dy = a_end.y - a_start.y;
        int yi = 1;
        if (dy < 0) {
            yi = -1;
            dy = -dy;
        }
        int D = (2 * dy) - dx;
        int y = a_start.y;
        for (int x = a_start.x; x <= a_end.x + 1; x++) {
            res.add(new java.awt.Point(x, y));
            if (D > 0) {
                y = y + yi;
                D = D + (2 * (dy - dx));
            } else {
                D = D + 2 * dy;
            }
        }
        return res;
    }

    public static HashSet<java.awt.Point> drawLineHigh(java.awt.Point a_start, java.awt.Point a_end) {
        HashSet<java.awt.Point> res = new HashSet<java.awt.Point>();
        int dx = a_end.x - a_start.x;
        int dy = a_end.y - a_start.y;
        int xi = 1;
        if (dx < 0) {
            xi = -1;
            dx = -dx;
        }
        int D = (2 * dx) - dy;
        int x = a_start.x;
        for (int y = a_start.y; y <= a_end.y + 1; y++) {
            res.add(new java.awt.Point(x, y));
            if (D > 0) {
                x = x + xi;
                D = D + (2 * (dx - dy));
            } else {
                D = D + 2 * dx;
            }
        }
        return res;
    }

    public static HashSet<java.awt.Point> drawLine(java.awt.Point a_start, java.awt.Point a_end) { // Bresenham's line
                                                                                                   // algorithm
        if (Math.abs(a_end.y - a_start.y) < Math.abs(a_end.x - a_start.x)) {
            if (a_start.x > a_end.x) {
                return Utils.drawLineLow(a_end, a_start);
            } else {
                return Utils.drawLineLow(a_start, a_end);
            }
        } else {
            if (a_start.y > a_end.y) {
                return Utils.drawLineHigh(a_end, a_start);
            } else {
                return Utils.drawLineHigh(a_start, a_end);
            }
        }
    }

    public static HashMap<Integer, Integer> getBordersOfTypeFromCenter(ECS a_ecs, Integer a_center, Integer a_level) {
        HashMap<Integer, Integer> res = new HashMap<Integer, Integer>();
        HashSet<Entity> TilesOnLevel = a_ecs.findEntitiesByFilter((a_entity_in) -> {
            PositionComponent position_in = a_entity_in.getFirstComponentOfType(PositionComponent.class);
            return (a_level == position_in.y);
        });
        ArrayList<Entity> forSort = new ArrayList<Entity>(TilesOnLevel);
        Collections.sort(forSort, (elem1, elem2) -> {
            PositionComponent pos1 = elem1.getFirstComponentOfType(PositionComponent.class);
            PositionComponent pos2 = elem2.getFirstComponentOfType(PositionComponent.class);
            return pos1.x > pos2.x ? 1 : -1;
        });
        Integer lineStart = forSort.get(0).getFirstComponentOfType(PositionComponent.class).x;
        Integer lineEnd = forSort.get(forSort.size() - 1).getFirstComponentOfType(PositionComponent.class).x;
        for (Entity i : forSort) { // finding the borders
            MaterialStateComponent matStComp = i.getFirstComponentOfType(MaterialStateComponent.class);
            if (matStComp.materialState == MaterialState.LIQUID)
                continue;
            PositionComponent position_in = i.getFirstComponentOfType(PositionComponent.class);
            if (position_in.x < a_center) {
                if (lineStart < position_in.x) {
                    lineStart = position_in.x;
                }
                continue;
            }
            if (position_in.x > a_center) {
                if (lineEnd > position_in.x) {
                    lineEnd = position_in.x;
                }
                continue;
            }
        }
        lineStart = lineStart == a_center ? -1 : lineStart;
        lineEnd = lineEnd == a_center ? 21 : lineEnd;
        res.put(0, lineStart);
        res.put(1, lineEnd);
        return res;
    }

    public static Double findDistanceBetweenTwoTiles(PositionComponent a_pos1, PositionComponent a_pos2) {
        return Math.sqrt(
                Math.pow(a_pos2.x - a_pos1.x, 2) + Math.pow(a_pos2.y - a_pos1.y, 2));
    }

    public static Double findDistanceBetweenTwoTileSquared(PositionComponent a_pos1, PositionComponent a_pos2) {
        return Math.pow(a_pos2.x - a_pos1.x, 2) + Math.pow(a_pos2.y - a_pos1.y, 2);
    }
}
