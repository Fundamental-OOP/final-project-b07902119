package com.almasb.fxglgames.td;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.almasb.fxglgames.td.tower.TowerDataComponent;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class Config {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    public static final int ENEMY_BASE_HP = 1000;
    public static final List<Point2D> ENEMY_PATH = Arrays.asList(
        new Point2D(50, 50),
        new Point2D(700, 50),
        new Point2D(700, 300),
        new Point2D(50, 300),
        new Point2D(50, 450), 
        new Point2D(700, 500)
    );

    public static final Point2D ENEMY_SPAWN_POINT = new Point2D(50, 50);

    public static final Map<String, TowerDataComponent> TOWER_DATA = new HashMap<>();
    static {
        TOWER_DATA.put("Bank", new TowerDataComponent("Bank", 50, Color.ORANGE, 1, 0, 0, 0));
        TOWER_DATA.put("BasicTower", new TowerDataComponent("BasicTower", 80, Color.YELLOW, .25, 40, 600, 500));
        TOWER_DATA.put("PoisonTower", new TowerDataComponent("PoisonTower", 500, Color.OLIVE, 1, 20, 300, 300));
        TOWER_DATA.put("FrozenTower", new TowerDataComponent("FrozenTower", 800, Color.SKYBLUE, 1, 0, 300, 300));
        TOWER_DATA.put("Cannon", new TowerDataComponent("Cannon", 1500, Color.DARKCYAN, 5, 200, 200, 300));
    }
}
