package com.almasb.fxglgames.td;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.ui.ProgressBar;
import com.almasb.fxglgames.td.components.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxglgames.td.Config.*;

public class TowerDefenseFactory implements EntityFactory {

    @Spawns("Background")
    public Entity newBackground(SpawnData data) {
        return entityBuilder()
                .at(0, 0)
                .view(texture("background.png", WIDTH + 20, HEIGHT + 20))
                .zIndex(-500)
                .build();
    }

    @Spawns("Enemy")
    public Entity spawnEnemy(SpawnData data) {
        int hp = data.get("hp");
        var hpComponent = new HealthIntComponent(hp);
        var hpView = new ProgressBar(false);
        hpView.setFill(Color.RED);
        hpView.setMaxValue(hp);
        hpView.setWidth(50);
        hpView.setTranslateY(-10);
        hpView.currentValueProperty().bind(hpComponent.valueProperty());
        
        return entityBuilder(data)
            .type(TowerDefenseType.ENEMY)
            .viewWithBBox(data.get("path").toString())
            .with(new CollidableComponent(true))
            .view(hpView)
            .with(hpComponent)
            .zIndex(10)
            .with(new EnemyComponent(data.get("damage"),data.get("score"),data.get("money")))
            .build();
    }

    private EntityBuilder towerBuilder(SpawnData data) {
        return entityBuilder(data)
            .type(TowerDefenseType.TOWER)
            .view(new Rectangle(40, 40, data.get("color")))
            .with(new CollidableComponent(true))
            .with("level", 1);
    }

    private void attachLevelText(Entity e) {
        var text = FXGL.getUIFactoryService().newText(e.getProperties().intProperty("level").asString("%d"));
        text.setFill(Color.BLACK);
        text.setTranslateX(20);
        text.setTranslateY(20);
        e.getViewComponent().addChild(text);
    }

    @Spawns("BasicTower")
    public Entity spawnTower(SpawnData data) {
        var e = towerBuilder(data)
            .view("basictower.png")
            .with(new BasicTowerComponent())
            .with(TOWER_DATA.get("BasicTower").copy())
            .build();
        attachLevelText(e);
        return e;
    }

    @Spawns("Bullet")
    public Entity spawnBullet(SpawnData data) {
        return entityBuilder(data)
                .type(TowerDefenseType.BULLET)
                .viewWithBBox(new Rectangle(15, 5, Color.DARKGREY))
                .with(new CollidableComponent(true))
                .with(new OffscreenCleanComponent())
                .build();
    }

    @Spawns("Cannon")
    public Entity spawnCannon(SpawnData data) {
        var e = towerBuilder(data)
            .view("cannon.png")
            .with(new CannonComponent())
            .with(TOWER_DATA.get("Cannon").copy())
            .build();
        attachLevelText(e);
        return e;
    }

    @Spawns("Bomb")
    public Entity spawnBomb(SpawnData data) {
        return entityBuilder(data)
            .type(TowerDefenseType.BOMB)
            .viewWithBBox(new Rectangle(20, 20, Color.RED))
            .with(new CollidableComponent(true))
            .with(new OffscreenCleanComponent())
            .with(new ExplosiveComponent(200))
            .build();
    }

    @Spawns("FrozenTower")
    public Entity spawnFrozenTower(SpawnData data) {
        var e = towerBuilder(data)
            .view("frozentower.png")
            .with(new FrozenTowerComponent())
            .with(TOWER_DATA.get("FrozenTower").copy())
            .build();
        attachLevelText(e);
        return e;
    }

    @Spawns("FrozenBullet")
    public Entity spawnFrozenBullet(SpawnData data) {
        return entityBuilder(data)
                .type(TowerDefenseType.FROZENBULLET)
                .viewWithBBox(new Rectangle(15, 5, Color.LIGHTBLUE))
                .with(new CollidableComponent(true))
                .with(new OffscreenCleanComponent())
                .build();
    }

    @Spawns("PoisonTower")
    public Entity spawnPoisonTower(SpawnData data) {
        var e = towerBuilder(data)
            .view("poisontower.png")
            .with(new PoisonTowerComponent())
            .with(TOWER_DATA.get("PoisonTower").copy())
            .build();
        attachLevelText(e);
        return e;
    }

    @Spawns("PoisonBullet")
    public Entity spawnPoisonBullet(SpawnData data) {
        return entityBuilder(data)
                .type(TowerDefenseType.POISONBULLET)
                .viewWithBBox(new Rectangle(15, 5, Color.PINK))
                .with(new CollidableComponent(true))
                .with(new OffscreenCleanComponent())
                .build();
    }

    @Spawns("Bank")
    public Entity spawnBank(SpawnData data) {
        var e = towerBuilder(data)
                .view("bank.png")
                .with(new BankComponent())
                .with(TOWER_DATA.get("Bank").copy())
                .build();
        attachLevelText(e);
        return e;
    }

    @Spawns("Explosion")
    public Entity newExplosion(SpawnData data) {
        play("explosion.wav");

        var texture = texture("explosion.png", 80 * 16, 80).toAnimatedTexture(16, Duration.seconds(0.5));
        var e = entityBuilder()
                .at(data.getX() - 40, data.getY() - 40)
                .view(texture.loop())
                .build();

        texture.setOnCycleFinished(() -> e.removeFromWorld());

        return e;
    }
}
