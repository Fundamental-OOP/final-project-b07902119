package com.almasb.fxglgames.td;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxglgames.td.collision.BombEnemyHandler;
import com.almasb.fxglgames.td.collision.BulletEnemyHandler;
import com.almasb.fxglgames.td.collision.FrozenBulletHandler;
import com.almasb.fxglgames.td.collision.PoisonBulletHandler;
import com.almasb.fxglgames.td.components.EnemyComponent;
import com.almasb.fxglgames.td.event.EnemyKilledEvent;
import com.almasb.fxglgames.td.event.HptoZero;
import com.almasb.fxglgames.td.tower.TowerDataComponent;
import com.almasb.fxglgames.td.tower.TowerIcon;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxglgames.td.Config.*;

public class TowerDefenseApp extends GameApplication {

    private int levelEnemies = 0;
    private boolean boss = false;
    BooleanProperty enemiesLeft = new SimpleBooleanProperty();

    private List<Point2D> waypoints = new ArrayList<>();

    public List<Point2D> getWaypoints() {
        return new ArrayList<>(waypoints);
    }

    @Override
    protected void onPreInit() {
        getSettings().setGlobalSoundVolume(0.1);
        getSettings().setGlobalMusicVolume(0.5);
        loopBGM("Never Gonna Give You Up Original.mp3");
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Tower Defense");
        settings.setVersion("1.126");
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setIntroEnabled(false);
        settings.setProfilingEnabled(false);
        settings.setCloseConfirmation(false);
        settings.setApplicationMode(ApplicationMode.DEVELOPER);
    }

    @Override
    protected void initInput() {
        Input input = getInput();

        input.addAction(new UserAction("Place / Upgrade Tower") {
            private Rectangle2D Bounds1 = new Rectangle2D(40, 80, 600, 160);
            private Rectangle2D Bounds2 = new Rectangle2D(120, 320, 600, 80);

            @Override
            protected void onActionBegin() {
                Point2D position = getMousePosition();
                if (Bounds1.contains(position) || Bounds2.contains(position)) {
                    var towers = getGameWorld().getEntitiesAt(position);
                    if (towers.size() == 0) {
                        placeTower(position);
                    }
                    else {
                        upgradeTower(towers.get(0));
                    }
                }
            }
        }, MouseButton.PRIMARY);
        input.addAction(new UserAction("Sell tower") {
            private Rectangle2D Bounds1 = new Rectangle2D(40, 80, 600, 160);
            private Rectangle2D Bounds2 = new Rectangle2D(120, 320, 600, 80);

            @Override
            protected void onActionBegin() {
                Point2D position = getMousePosition();
                if (Bounds1.contains(position) || Bounds2.contains(position)) {
                    var towers = getGameWorld().getEntitiesAt(position);
                    if (towers.size() > 0) {
                        towers.get(0).removeFromWorld();
                        inc("money", 10);
                    }
                }
            }
        }, MouseButton.SECONDARY);
    }

    private void placeTower(Point2D p) {
        if (geti("money") < selectedTower.getCost())
            return;
        inc("money", -selectedTower.getCost());
        getGameWorld().spawn(
            selectedTower.getType(),
            new SpawnData(p).put("color", selectedTower.getColor()));
    }

    private void upgradeTower(Entity tower) {
        var data = tower.getComponent(TowerDataComponent.class);

        if (!data.checkMaxLevel() && geti("money") >= 10) {
            inc("money", -10);
            data.levelUp();
        }
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("numEnemies", 1126);
        vars.put("money", 100);
        vars.put("level", 0);
        vars.put("score", 0);
        vars.put("hp", 10);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new TowerDefenseFactory());

        waypoints.addAll(ENEMY_PATH);

        enemiesLeft.bind(getip("numEnemies").greaterThan(0));

        getGameTimer().runAtIntervalWhile(this::spawnEnemy, Duration.seconds(0.5), enemiesLeft);

        getEventBus().addEventHandler(EnemyKilledEvent.ANY, this::onEnemyKilled);
        getEventBus().addEventHandler(HptoZero.ANY, e-> gameOver());
        spawn("Background");
    }

    private Pane getEnemyPathPane() {
        Pane pane = new Pane();
        for (int i = 0; i < waypoints.size() - 1; i++) {
            var p = waypoints.get(i);
            var q = waypoints.get(i + 1);
            Line line = new Line(p.getX(), p.getY(), q.getX() , q.getY());
            line.setFill(null);
            line.setStroke(Color.RED);
            line.setStrokeWidth(2);
            pane.getChildren().add(line);
        }
        return pane;
    }

    private Line getBackgroundLine(Point2D p, Point2D q) {
        Line line = new Line(p.getX(), p.getY(), q.getX() , q.getY());
        line.setFill(null);
        line.setStroke(Color.rgb(0, 0, 0, 0.2));
        line.setStrokeWidth(2);
        return line;
    }

    private Pane getBlocks() {
        Pane pane = new Pane();

        for (int i = 40; i <= 680; i+=40) {
            var p = new Point2D(i, 80);
            var q = new Point2D(i,280);
            pane.getChildren().add(getBackgroundLine(p, q));
        }
        for (int i = 80; i <= 280; i+=40) {
            var p = new Point2D(40,i);
            var q = new Point2D(680,i);
            pane.getChildren().add(getBackgroundLine(p, q));
        }
        for (int i = 120; i <= 760; i+=40) {
            var p = new Point2D(i, 320);
            var q = new Point2D(i,440);
            pane.getChildren().add(getBackgroundLine(p, q));
        }
        for (int i = 320; i <= 440; i+=40) {
            var p = new Point2D(120,i);
            var q = new Point2D(760,i);
            pane.getChildren().add(getBackgroundLine(p, q));
        }
        return pane;
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new BulletEnemyHandler());
        getPhysicsWorld().addCollisionHandler(new BombEnemyHandler());
        getPhysicsWorld().addCollisionHandler(new FrozenBulletHandler());
        getPhysicsWorld().addCollisionHandler(new PoisonBulletHandler());
    }

    private TowerDataComponent selectedTower = TOWER_DATA.get("BasicTower");

    private int textX = 600;
    private void initMoneyUI() {
        Text textMoney = new Text();
        textMoney.setTranslateX(textX);
        textMoney.setTranslateY(520);
        textMoney.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 14));
        textMoney.setFill(Color.WHITE);
        textMoney.textProperty().bind(getWorldProperties().intProperty("money").asString("Money: %d"));
        getGameScene().addUINode(textMoney);
    }

    private void initScoreUI() {
        Text textScore = new Text();
        textScore.setTranslateX(textX);
        textScore.setTranslateY(540);
        textScore.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 14));
        textScore.setFill(Color.WHITE);
        textScore.textProperty().bind(getWorldProperties().intProperty("score").asString("Score: %d"));
        getGameScene().addUINode(textScore);
    }

    private void initHpUI() {
        Text textHp = new Text();
        textHp.setTranslateX(textX);
        textHp.setTranslateY(560);
        textHp.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 14));
        textHp.setFill(Color.WHITE);
        textHp.textProperty().bind(getWorldProperties().intProperty("hp").asString("HP: %d"));
        getGameScene().addUINode(textHp);
    }

    private void initHintUI() {
        Text text = new Text("Kill 1126 enemies to win the game!");
        text.setTranslateX(textX - 40);
        text.setTranslateY(580);
        text.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 12));
        text.setFill(Color.RED);
        getGameScene().addUINode(text);
    }

    private void initTowerUI() {
        Set<String> set = TOWER_DATA.keySet();
		Iterator<String> it = set.iterator();
        int i = 0;
		while (it.hasNext()) {
			TowerDataComponent data = TOWER_DATA.get(it.next());
            Color color = data.getColor();
            String type = data.getType();
            TowerIcon icon = new TowerIcon(type, color);
            icon.setTranslateX(10 + i * 110);
            icon.setTranslateY(500);
            icon.setOnMouseClicked(e -> {
                selectedTower = data;
            });

            getGameScene().addUINode(icon);
            i++;
		}
    }

    @Override
    protected void initUI() {
        Rectangle uiBG = new Rectangle(getAppWidth(), 100);
		uiBG.setTranslateY(500);
        getGameScene().addUINode(uiBG);
        getGameScene().addUINode(getEnemyPathPane());
        getGameScene().addUINode(getBlocks());

        initMoneyUI();
        initScoreUI();
        initHpUI();
        initHintUI();
        initTowerUI();
    }

    private void spawnEnemy() {
        inc("numEnemies",-1);
        if(boss){
            boss = false;
            getGameWorld().spawn("Enemy",
                    new SpawnData(ENEMY_SPAWN_POINT.getX(), ENEMY_SPAWN_POINT.getY())
                            .put("hp", ENEMY_BASE_HP * geti("score") * geti("score") / 1000).put("path", "boss.png").put("damage", 5).put("score", 10).put("money", 5 * geti("score") / 10));
        }
        else{
            getGameWorld().spawn("Enemy",
                    new SpawnData(ENEMY_SPAWN_POINT.getX(), ENEMY_SPAWN_POINT.getY())
                            .put("hp", ENEMY_BASE_HP + geti("score") * 10).put("path", "enemy.png").put("damage", 1).put("score", 1).put("money", 1));
        }
    }

    private void onEnemyKilled(EnemyKilledEvent event) {
        levelEnemies++;
        if(levelEnemies%20==0) boss = true;
        if(!enemiesLeft.getValue() && getGameWorld().getEntitiesByComponent(EnemyComponent.class).isEmpty()){
            getDisplay().showMessageBox("You win. Thanks for playing!", getGameController()::exit);
        }

        Entity enemy = event.getEnemy();
        Point2D position = enemy.getPosition();

        inc("score", enemy.getComponent(EnemyComponent.class).getScore());
        inc("money", enemy.getComponent(EnemyComponent.class).getMoney());

    }

    private void gameOver() {
        getDisplay().showMessageBox("Game Over. Thanks for playing!", getGameController()::exit);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private Point2D getMousePosition(){
        double x = getInput().getMouseXWorld();
        double y = getInput().getMouseYWorld();
        x = ((int)(x/40))*40;
        y = ((int)(y/40))*40;
        return new Point2D(x,y);
    }
}
