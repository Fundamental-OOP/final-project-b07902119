package com.almasb.fxglgames.td.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxglgames.td.TowerDefenseApp;
import com.almasb.fxglgames.td.event.EnemyKilledEvent;
import com.almasb.fxglgames.td.event.HptoZero;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.getDisplay;
import static com.almasb.fxgl.dsl.FXGL.getGameController;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class EnemyComponent extends Component {

    private List<Point2D> waypoints;
    private Point2D nextWaypoint;
    private int damage,score,money;
    private double tpfspeed, speed = 60;

    public EnemyComponent(int damage, int score, int money){
        super();
        this.damage = damage;
        this.score = score;
        this.money = money;
    }

    @Override
    public void onAdded() {
        waypoints = ((TowerDefenseApp) FXGL.getApp()).getWaypoints();

        nextWaypoint = waypoints.remove(0);
    }

    @Override
    public void onUpdate(double tpf) {
        tpfspeed = tpf * speed * 2;

        Point2D velocity = nextWaypoint.subtract(entity.getPosition())
                .normalize()
                .multiply(tpfspeed);

        entity.translate(velocity);

        if(entity.getComponent(HealthIntComponent.class).getValue() <= 0){
            FXGL.getEventBus().fireEvent(new EnemyKilledEvent(entity));
            spawn("Explosion", entity.getCenter());
            entity.removeFromWorld();
        }

        if (nextWaypoint.distance(entity.getPosition()) < tpfspeed) {
            entity.setPosition(nextWaypoint);

            if (!waypoints.isEmpty()) {
                nextWaypoint = waypoints.remove(0);
            } else {
                inc("hp",- this.damage);
                entity.removeFromWorld();
                this.score = 0;
                this.money = 0;
                FXGL.getEventBus().fireEvent(new EnemyKilledEvent(entity));
                if(geti("hp")<=0) FXGL.getEventBus().fireEvent(new HptoZero());

            }
        }
    }

    public void SpeedSet(double rate){
        speed *= rate;
        getGameTimer().runOnceAfter(()->{
            speed /= rate;
        }, Duration.seconds(1));
    }

    public int getScore(){
        return score;
    }

    public int getMoney(){
        return money;
    }
}
