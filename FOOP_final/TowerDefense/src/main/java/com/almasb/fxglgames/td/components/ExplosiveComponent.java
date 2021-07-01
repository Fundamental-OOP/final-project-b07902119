package com.almasb.fxglgames.td.components;

import com.almasb.fxgl.entity.component.Component;

public class ExplosiveComponent extends Component {
    
    private double radius;

    public ExplosiveComponent(double radius) {
        super();
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }
}
