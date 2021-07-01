package com.almasb.fxglgames.td.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxglgames.td.TowerDefenseType;
import com.almasb.fxglgames.td.components.DamagingComponent;
import com.almasb.fxglgames.td.event.EnemyKilledEvent;

import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class BulletEnemyHandler extends CollisionHandler {

    public BulletEnemyHandler() {
        super(TowerDefenseType.BULLET, TowerDefenseType.ENEMY);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity enemy) {
        bullet.removeFromWorld();

        HealthIntComponent health = enemy.getComponent(HealthIntComponent.class);
        int damage = bullet.getComponent(DamagingComponent.class).getDamage();
        health.setValue(health.getValue() - damage);

        if (health.getValue() <= 0) {
            FXGL.getEventBus().fireEvent(new EnemyKilledEvent(enemy));
            spawn("Explosion", enemy.getCenter());
            enemy.removeFromWorld();
        }
    }
}
