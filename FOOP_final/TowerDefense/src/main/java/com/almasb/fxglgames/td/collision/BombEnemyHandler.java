package com.almasb.fxglgames.td.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxglgames.td.TowerDefenseType;
import com.almasb.fxglgames.td.components.DamagingComponent;
import com.almasb.fxglgames.td.components.ExplosiveComponent;
import com.almasb.fxglgames.td.event.EnemyKilledEvent;

import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

public class BombEnemyHandler extends CollisionHandler {
    public BombEnemyHandler() {
        super(TowerDefenseType.BOMB, TowerDefenseType.ENEMY);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity enemy) {
        bullet.removeFromWorld();

        int damage = bullet.getComponent(DamagingComponent.class).getDamage();
        double radius = bullet.getComponent(ExplosiveComponent.class).getRadius();
        var enemiesInRadius = FXGL.getGameWorld().getEntitiesFiltered(e -> {
            double dist = e.getPosition().distance(bullet.getPosition());
            return e.isType(TowerDefenseType.ENEMY) && dist < radius;
        });

        for (Entity e : enemiesInRadius) {
            HealthIntComponent health = e.getComponent(HealthIntComponent.class);
            health.setValue(health.getValue() - damage);
            if (health.getValue() <= 0) {
                FXGL.getEventBus().fireEvent(new EnemyKilledEvent(enemy));
                spawn("Explosion", enemy.getCenter());
                enemy.removeFromWorld();
            }
        }
    }
}
