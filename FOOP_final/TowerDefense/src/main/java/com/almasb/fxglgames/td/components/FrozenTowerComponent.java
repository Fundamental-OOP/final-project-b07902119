package com.almasb.fxglgames.td.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;

public class FrozenTowerComponent extends TowerComponent{
    @Override
    protected void shoot(Entity enemy) {
        Point2D position = getEntity().getPosition();
        Entity bullet = FXGL.spawn("FrozenBullet", position);
        aimAndAttachDamage(bullet, enemy);
    }
}