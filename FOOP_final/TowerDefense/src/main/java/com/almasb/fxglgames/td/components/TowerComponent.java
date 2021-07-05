package com.almasb.fxglgames.td.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;
import com.almasb.fxglgames.td.TowerDefenseType;
import com.almasb.fxglgames.td.tower.TowerDataComponent;

import javafx.geometry.Point2D;
import javafx.util.Duration;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public abstract class TowerComponent extends Component {

    protected LocalTimer shootTimer;

    @Override
    public void onAdded() {
        shootTimer = FXGL.newLocalTimer();
        shootTimer.capture();
    }

    @Override
    public void onUpdate(double tpf) {

        if (shootTimer.elapsed(Duration.seconds(getDataComponent().getCd()))) {
            FXGL.getGameWorld()
                .getClosestEntity(entity, e -> e.isType(TowerDefenseType.ENEMY))
                .ifPresent(nearestEnemy -> {
                    double dist = nearestEnemy.getPosition().distance(getEntity().getPosition());
                    // check if the enemy is in the range of this tower
                    if (getDataComponent().getRadius() >= dist) {
                        shoot(nearestEnemy);
                        shootTimer.capture();
                    }
                });
        }
    }

    protected void aimAndAttachDamage(Entity bullet, Entity enemy) {
        Point2D position = getEntity().getPosition();
        Point2D direction = enemy.getPosition().subtract(position);
        bullet.addComponent(new ProjectileComponent(direction, getDataComponent().getBulletSpeed()));
        bullet.addComponent(new DamagingComponent(getDataComponent().getDamage()));
    }

    protected TowerDataComponent getDataComponent() {
        return entity.getComponent(TowerDataComponent.class);
    }

    // shoot enemy or trigger some action
    protected abstract void shoot(Entity enemy);
}
