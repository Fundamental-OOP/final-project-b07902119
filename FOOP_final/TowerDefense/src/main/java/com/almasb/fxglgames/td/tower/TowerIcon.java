package com.almasb.fxglgames.td.tower;

import com.almasb.fxglgames.td.Config;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class TowerIcon extends Pane {

    public TowerIcon(String type, Color color) {
        getChildren().add(new Rectangle(100, 100, color));

        Label nameLabel = new Label(type);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 12));
        nameLabel.setTextFill(Color.BLACK);
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(100);
        getChildren().add(nameLabel);
        
        int cost = Config.TOWER_DATA.get(type).getCost();
        Label costLabel = new Label(Integer.toString(cost));
        costLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 20));
        costLabel.setTextFill(Color.BLACK);
        costLabel.setWrapText(true);
        costLabel.setMaxWidth(80);
        costLabel.setTranslateY(40);
        getChildren().add(costLabel);
    }
}
