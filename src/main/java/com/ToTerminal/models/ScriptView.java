package com.ToTerminal.models;

import com.ToTerminal.ScriptReader.Script;
import com.ToTerminal.ScriptReader.ScriptState;
import com.ToTerminal.controllers.ConfigController;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class ScriptView extends Pane {
    Label sName;
    TextArea sDescription;

    public ScriptView(Script base, ConfigController controller) {
        sName = new Label(base.getScriptName());
        sName.setFont(new Font(20));
        sDescription = new TextArea(base.getScriptDescription());
        getChildren().add(sDescription);
        getChildren().add(sName);

        if(base.state == ScriptState.NOT_FOUND){
            sName.setStyle("-fx-text-fill: red;");
            sDescription.setStyle("-fx-text-fill: red;");
        } else {
            sName.setStyle("-fx-text-fill: black;");
            sDescription.setStyle("-fx-text-fill: black;");
        }

        sDescription.setPrefHeight(20);
        sDescription.setPrefWidth(100);
        sDescription.setWrapText(true);
        sDescription.setEditable(false);
        sDescription.setLayoutY(30);
        setLayoutY(10);
        setPrefWidth(200);
        setPrefHeight(100);
        setStyle("-fx-padding: 10;");

        setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px;");
        sName.setStyle("-fx-text-fill: #333; -fx-font-weight: bold;");
        sDescription.setStyle("-fx-text-fill: #666; -fx-font-size: 14px; -fx-background-color: #f9f9f9; -fx-border-color: #ddd; -fx-border-width: 1px; -fx-border-radius: 5px;");

        setOnMouseClicked(event -> {
            if (controller != null) {
                controller.selectedScript = base;
                controller.selectedScriptView = this;
                controller.updateSelectionStyle();
            }
        });
    }
}
