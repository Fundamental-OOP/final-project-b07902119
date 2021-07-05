package com.almasb.fxglgames.td.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxglgames.td.TowerDefenseType;
import com.almasb.fxglgames.td.components.DamagingComponent;
import com.almasb.fxglgames.td.components.EnemyComponent;
import com.almasb.fxglgames.td.event.EnemyKilledEvent;

import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

public class FrozenBulletHandler extends CollisionHandler {
    private double rate = 0.5;

    public FrozenBulletHandler() {
        super(TowerDefenseType.FROZENBULLET, TowerDefenseType.ENEMY);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity enemy) {
        bullet.removeFromWorld();

        enemy.getComponent(EnemyComponent.class).SpeedSet(rate);
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
