package com.almasb.fxglgames.td.components;

import com.almasb.fxgl.entity.component.Component;

public class RadiusComponent extends Component {
    private double radius;

    public RadiusComponent(double radius) {
        super();
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }
}
