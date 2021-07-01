package com.almasb.fxglgames.td.event;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class HptoZero extends Event {

    public static final EventType<HptoZero> ANY
            = new EventType<>(Event.ANY, "HptoZero");

    public HptoZero() {
        super(ANY);
    }
}