package com.almasb.fxglgames.td.components;
import com.almasb.fxgl.entity.Entity;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.inc;

public class BankComponent extends TowerComponent {

    @Override
    public void onUpdate(double tpf) {
        if (shootTimer.elapsed(Duration.seconds(getDataComponent().getCd()))) {
            inc("money", getDataComponent().getDamage());
            shootTimer.capture();
        }
    }

    @Override
    protected void shoot(Entity enemy) { }

}