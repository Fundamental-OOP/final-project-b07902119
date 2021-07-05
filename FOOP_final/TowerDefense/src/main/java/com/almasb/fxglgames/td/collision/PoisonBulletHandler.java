package com.almasb.fxglgames.td.collision;

import javafx.util.Duration;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxglgames.td.TowerDefenseType;
import com.almasb.fxglgames.td.components.DamagingComponent;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameTimer;


public class PoisonBulletHandler extends CollisionHandler {

    public PoisonBulletHandler() {
        super(TowerDefenseType.POISONBULLET, TowerDefenseType.ENEMY);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity enemy) {
        bullet.removeFromWorld();

        HealthIntComponent health = enemy.getComponent(HealthIntComponent.class);
        int damage = bullet.getComponent(DamagingComponent.class).getDamage();

        health.setValue(health.getValue() - damage);
        getGameTimer().runOnceAfter(()->{
            try {
                health.setValue(health.getValue() - damage * 2);
            }
            catch (NullPointerException e){
                ;
            }
        }, Duration.seconds(1));
        getGameTimer().runOnceAfter(()->{
            try {
                health.setValue(health.getValue() - damage * 3);
            }
            catch (NullPointerException e){
                ;
            }
        }, Duration.seconds(2));
    }
}
