package com.almasb.fxglgames.td.components;

import com.almasb.fxgl.entity.component.Component;

public class DamagingComponent extends Component {
    private int damage;

    public DamagingComponent(int damage) {
        super();
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }
}
