package com.almasb.fxglgames.td.tower;

import com.almasb.fxgl.entity.component.Component;

import javafx.scene.paint.Color;

public class TowerDataComponent extends Component {
    private String type;
    private int cost;
    private Color color;
    private double cd;
    private int damage;
    private double radius;
    private double bulletSpeed;
    private int level;
    private int maxLevel;

    public TowerDataComponent(
        String type,
        int cost,
        Color color,
        double cd,
        int damage,
        double bulletSpeed,
        double radius) {
        this.type = type;
        this.cost = cost;
        this.color = color;
        this.cd = cd;
        this.damage = damage;
        this.bulletSpeed = bulletSpeed;
        this.radius = radius;
        this.level = 1;
        this.maxLevel = 5;
    }

    public TowerDataComponent copy() {
        return new TowerDataComponent(type, cost, color, cd, damage, bulletSpeed, radius);
    }

    public String getType() {
        return this.type;
    }

    public int getCost() {
        return this.cost;
    }

    public Color getColor() {
        return this.color;
    }

    public double getCd() {
        return cd;
    }

    public int getDamage() {
        return damage + level * 5; // TODO
    }

    public double getBulletSpeed() {
        return bulletSpeed;
    }

    public double getRadius() {
        return radius;
    }

    public void levelUp() {
        level++;
        entity.getProperties().setValue("level", level);
    }

    public boolean checkMaxLevel() {
        return level == maxLevel;
    }
}
