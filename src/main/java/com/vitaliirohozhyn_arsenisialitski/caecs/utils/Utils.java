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

/**
 * Klasa, potrzebna do przechowywania statycznych funkcji potrzebnych w różnych
 * częściach kodu
 */
public class Utils {
    /**
     * Zgenerować nowy {@link Color} na podstawie innego z kontrolą różnicy między
     * nimi.
     *
     * @param a_original oryginalny kolor
     * @param contrast   o ile dozwolone jest odchodzenie od oryginalu
     * @return zgenerowany kolor.
     */
    public static Color generateRandomColorFromOriginal(Color a_original, int contrast) {
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

    /**
     * Generacja losowej liczby w pewnym przedziałe
     *
     * @param a_min liczba minimalna
     * @param a_max liczba maksymalna
     * @return zgenerowana liczba
     */
    public static int generateNumberWithinRange(int a_min, int a_max) {
        Random rand = new Random();
        return rand.nextInt(a_max - a_min + 1) + a_min;
    }

    /**
     * Funkcja, zapewniająca to, że liczba mieszczi się w pewnym przedziałe.
     *
     * @param <T> Klasa liczby
     * @param l   liczba, którą porównujemy
     * @param min początek przedziału
     * @param max koniec przedziału
     * @return liczba, która mieszczi się w przedziałe
     */
    public static <T extends Number & Comparable<T>> T clamp(T l, T min, T max) {
        return l.compareTo(max) > 0 ? max : l.compareTo(min) < 0 ? min : l;
    }

    /**
     * Przesunięcie liczby o deltę w stronę liczby finalnej oraz zapewnienie, by ta
     * wartość nie wychodziła poza zakres
     *
     * @param cur   liczba oryginalna
     * @param fin   liczba finalna
     * @param delta delta
     * @return przesunięta liczba
     */
    public static int moveTowards(int cur, int fin, int delta) {
        return Math.abs(fin - cur) <= delta ? fin : cur + (int) (Math.signum(fin - cur)) * delta;
    }

    /**
     * To samo, co i Utils.moveTowards, tylko dla koloru
     *
     * @param cur "wektor" orygnalnego {@link Color}
     * @param fin "wektor" finalnego koloru
     * @param spd "wektor" delty
     * @return przesunięty kolor
     */
    public static Color moveColorTowards(Color cur, Color fin, Color spd) {
        return new Color(
                Utils.clamp(Utils.moveTowards(cur.getRed(), fin.getRed(), spd.getRed()), 0, 255),
                Utils.clamp(Utils.moveTowards(cur.getGreen(), fin.getGreen(), spd.getGreen()), 0, 255),
                Utils.clamp(Utils.moveTowards(cur.getBlue(), fin.getBlue(), spd.getBlue()), 0, 255));
    }

    /**
     * Skrót do otrzymania {@link Entity} na pewnych koordynatach
     *
     * @param a_ecs {@link ECS}
     * @param a_x   pozycja X
     * @param a_y   pozycja Y
     * @return *Możliwe* znaleziony {@link Entity}
     */
    public static Entity getEntityAtCoordinates(ECS a_ecs, int a_x, int a_y) {
        return a_ecs.findFirstEntityByFilter((a_entity) -> {
            PositionComponent pos = a_entity.getFirstComponentOfType(PositionComponent.class);
            return (pos.x == a_x && pos.y == a_y);
        });
    }

    /**
     * Skalowanie wektora
     *
     * @param a_origVector wektor oryginalny
     * @param a_scale      skalar
     * @return przeskalowany wektor
     */
    public static Point2D.Float vectorScale(Point2D.Float a_origVector, float a_scale) {
        return new Point2D.Float(
                a_origVector.x * a_scale,
                a_origVector.y * a_scale);
    }

    /**
     * Suma wektorów
     *
     * @param a_vector1 wektor pierwszy
     * @param a_vector2 wektor drugi
     * @return suma wektorów
     */
    public static Point2D.Float vectorSum(Point2D.Float a_vector1, Point2D.Float a_vector2) {
        return new Point2D.Float(
                a_vector1.x + a_vector2.x,
                a_vector1.y + a_vector2.y);
    }

    /**
     * Część algorytmu Bresenhama do rasteryzacji wektora
     *
     * @param a_start koordynaty początkowe
     * @param a_end   koordynaty końcowe
     * @return zbiór koordynat zgenerowanej linii
     */
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

    /**
     * Część algorytmu Bresenhama do rasteryzacji wektora
     *
     * @param a_start koordynaty początkowe
     * @param a_end   koordynaty końcowe
     * @return zbiór koordynat zgenerowanej linii
     */
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

    /**
     * Całkowity algorytm Bresenhama do rasteryzacji wektora
     *
     * @param a_start koordynaty początkowe
     * @param a_end   koordynaty końcowe
     * @return zbiór koordynat linii
     */
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

    /**
     * Algorytm do wyznaczenia brzegów z obu stron od pewnego "centru" na pewnym
     * poziomie mapy
     *
     * @param a_ecs    {@link ECS}
     * @param a_center koordynata centra
     * @param a_level  poziom na którym szukamy brzegów
     * @return 2 pozycji. Jeśli brzegu nie znaleziono - brzeg mapy
     */
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

    /**
     * Znalezienie odległości między dwoma pozycjami
     *
     * @param a_pos1 pozycja pierwsza
     * @param a_pos2 pozycja druga
     * @return odległość
     */
    public static Double findDistanceBetweenTwoTiles(PositionComponent a_pos1, PositionComponent a_pos2) {
        return Math.sqrt(
                Math.pow(a_pos2.x - a_pos1.x, 2) + Math.pow(a_pos2.y - a_pos1.y, 2));
    }

    /**
     * To samo, co i findDistanceBetweenTwoTiles, tylko bez pierwistaku
     *
     * @param a_pos1 pozycja pierwsza
     * @param a_pos2 pozycja druga
     * @return odległość
     */
    public static Double findDistanceBetweenTwoTileSquared(PositionComponent a_pos1, PositionComponent a_pos2) {
        return Math.pow(a_pos2.x - a_pos1.x, 2) + Math.pow(a_pos2.y - a_pos1.y, 2);
    }
}
