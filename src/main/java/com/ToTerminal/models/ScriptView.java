package com.ToTerminal.models;

import com.ToTerminal.ScriptReader.Script;
import com.ToTerminal.ScriptReader.ScriptState;
import com.ToTerminal.controllers.ConfigController;
import com.ToTerminal.utils.StyleManager;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Vista personalizada para mostrar información de scripts
 * Sigue el patrón de diseño establecido y se integra con el StyleManager
 */
public class ScriptView extends VBox {
    
    // Referencias a los elementos de la interfaz
    private Label scriptNameLabel;
    private Label scriptLanguageLabel;
    private Label scriptPathLabel;
    private TextArea scriptDescriptionArea;
    private Label scriptStatusLabel;
    private Label scriptSizeLabel;
    private Label scriptModifiedLabel;
    
    // Referencias a los datos
    private Script script;
    private ConfigController controller;
    private StyleManager styleManager;
    
    // Constantes de diseño
    private static final double PREF_WIDTH = 300;
    private static final double PREF_HEIGHT = 180;
    private static final double DESCRIPTION_HEIGHT = 80;
    private static final Insets PADDING = new Insets(10);
    private static final int SPACING = 8;
    
    /**
     * Constructor principal
     * @param script Script a mostrar
     * @param controller Controlador de configuración
     */
    public ScriptView(Script script, ConfigController controller) {
        this.script = script;
        this.controller = controller;
        this.styleManager = StyleManager.getInstance();
        
        initializeComponents();
        setupLayout();
        applyStyles();
        setupEventHandlers();
        updateContent();
    }
    
    /**
     * Inicializa todos los componentes de la interfaz
     */
    private void initializeComponents() {
        // Label del nombre del script
        scriptNameLabel = new Label();
        scriptNameLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        scriptNameLabel.setWrapText(true);
        
        // Label del lenguaje
        scriptLanguageLabel = new Label();
        scriptLanguageLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
        scriptLanguageLabel.setStyle("-fx-text-fill: #666;");
        
        // Label de la ruta
        scriptPathLabel = new Label();
        scriptPathLabel.setFont(Font.font("System", FontWeight.NORMAL, 10));
        scriptPathLabel.setStyle("-fx-text-fill: #888;");
        scriptPathLabel.setWrapText(true);
        
        // Área de descripción
        scriptDescriptionArea = new TextArea();
        scriptDescriptionArea.setEditable(false);
        scriptDescriptionArea.setWrapText(true);
        scriptDescriptionArea.setPrefHeight(DESCRIPTION_HEIGHT);
        scriptDescriptionArea.setPrefRowCount(3);
        
        // Label de estado
        scriptStatusLabel = new Label();
        scriptStatusLabel.setFont(Font.font("System", FontWeight.BOLD, 11));
        
        // Label de tamaño
        scriptSizeLabel = new Label();
        scriptSizeLabel.setFont(Font.font("System", FontWeight.NORMAL, 10));
        scriptSizeLabel.setStyle("-fx-text-fill: #666;");
        
        // Label de fecha de modificación
        scriptModifiedLabel = new Label();
        scriptModifiedLabel.setFont(Font.font("System", FontWeight.NORMAL, 10));
        scriptModifiedLabel.setStyle("-fx-text-fill: #666;");
    }
    
    /**
     * Configura el layout y la disposición de los componentes
     */
    private void setupLayout() {
        // Configuración principal del VBox
        setPrefWidth(PREF_WIDTH);
        setPrefHeight(PREF_HEIGHT);
        setPadding(PADDING);
        setSpacing(SPACING);
        setAlignment(Pos.TOP_LEFT);
        
        // Header con nombre y lenguaje
        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.getChildren().addAll(scriptNameLabel, scriptLanguageLabel);
        
        // Información de ruta
        VBox pathBox = new VBox(2);
        pathBox.getChildren().add(scriptPathLabel);
        
        // Área de descripción
        VBox descriptionBox = new VBox(5);
        descriptionBox.getChildren().add(scriptDescriptionArea);
        
        // Footer con información adicional
        HBox footerBox = new HBox(15);
        footerBox.setAlignment(Pos.CENTER_LEFT);
        footerBox.getChildren().addAll(scriptStatusLabel, scriptSizeLabel, scriptModifiedLabel);
        
        // Agregar todos los componentes al VBox principal
        getChildren().addAll(headerBox, pathBox, descriptionBox, footerBox);
        
        // Configurar estilos del contenedor
        setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px;");
    }
    
    /**
     * Aplica estilos usando el StyleManager
     */
    private void applyStyles() {
        if (styleManager != null) {
            // Aplicar estilos al contenedor principal
            styleManager.applyStylesToNode(this);
            
            // Aplicar estilos a los componentes individuales
            styleManager.applyStylesToNode(scriptNameLabel);
            styleManager.applyStylesToNode(scriptLanguageLabel);
            styleManager.applyStylesToNode(scriptPathLabel);
            styleManager.applyStylesToNode(scriptDescriptionArea);
            styleManager.applyStylesToNode(scriptStatusLabel);
            styleManager.applyStylesToNode(scriptSizeLabel);
            styleManager.applyStylesToNode(scriptModifiedLabel);
        }
    }
    
    /**
     * Configura los manejadores de eventos
     */
    private void setupEventHandlers() {
        // Evento de clic para seleccionar el script
        setOnMouseClicked(event -> {
            if (controller != null) {
                controller.selectedScript = script;
                controller.selectedScriptView = this;
                controller.updateSelectionStyle();
            }
        });
        
        // Eventos de hover para mejorar la UX
        setOnMouseEntered(event -> {
            setStyle("-fx-background-color: #e8e8e8; -fx-border-color: #aaa; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);");
        });
        
        setOnMouseExited(event -> {
            if (controller != null && controller.selectedScriptView != this) {
                setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px;");
            }
        });
    }
    
    /**
     * Actualiza el contenido con la información del script
     */
    private void updateContent() {
        if (script == null) return;
        
        // Nombre del script
        scriptNameLabel.setText(script.getScriptName());
        
        // Lenguaje del script
        scriptLanguageLabel.setText(script.getAvailableLanguage().toString());
        
        // Ruta del script
        scriptPathLabel.setText(script.getScriptPath());
        
        // Descripción del script
        scriptDescriptionArea.setText(script.getScriptDescription());
        
        // Estado del script
        updateScriptStatus();
        
        // Información adicional
        updateAdditionalInfo();
    }
    
    /**
     * Actualiza el estado del script con colores apropiados
     */
    private void updateScriptStatus() {
        if (script.state == ScriptState.NOT_FOUND) {
            scriptStatusLabel.setText("❌ NO ENCONTRADO");
            scriptStatusLabel.setStyle("-fx-text-fill: #d32f2f; -fx-font-weight: bold;");
        } else if (script.state == ScriptState.DEFAULT_STATE) {
            scriptStatusLabel.setText("✅ DISPONIBLE");
            scriptStatusLabel.setStyle("-fx-text-fill: #388e3c; -fx-font-weight: bold;");
        } else {
            scriptStatusLabel.setText("❓ DESCONOCIDO");
            scriptStatusLabel.setStyle("-fx-text-fill: #757575; -fx-font-weight: bold;");
        }
    }
    
    /**
     * Actualiza información adicional del script
     */
    private void updateAdditionalInfo() {
        // Tamaño del archivo (si está disponible)
        try {
            java.io.File scriptFile = new java.io.File(script.getScriptPath());
            if (scriptFile.exists()) {
                long sizeInBytes = scriptFile.length();
                String sizeText = formatFileSize(sizeInBytes);
                scriptSizeLabel.setText("📁 " + sizeText);
                
                // Fecha de modificación
                long lastModified = scriptFile.lastModified();
                String dateText = formatDate(lastModified);
                scriptModifiedLabel.setText("📅 " + dateText);
            } else {
                scriptSizeLabel.setText("📁 N/A");
                scriptModifiedLabel.setText("📅 N/A");
            }
        } catch (Exception e) {
            scriptSizeLabel.setText("📁 N/A");
            scriptModifiedLabel.setText("📅 N/A");
        }
    }
    
    /**
     * Formatea el tamaño del archivo en unidades legibles
     */
    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        return String.format("%.1f GB", bytes / (1024.0 * 1024.0 * 1024.0));
    }
    
    /**
     * Formatea la fecha de modificación
     */
    private String formatDate(long timestamp) {
        try {
            java.time.Instant instant = java.time.Instant.ofEpochMilli(timestamp);
            java.time.LocalDateTime dateTime = java.time.LocalDateTime.ofInstant(instant, java.time.ZoneId.systemDefault());
            return dateTime.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        } catch (Exception e) {
            return "N/A";
        }
    }
    
    /**
     * Aplica el estilo de selección
     */
    public void applySelectionStyle() {
        setStyle("-fx-background-color: #d0e0f0; -fx-border-color: #aaa; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 8, 0, 0, 0);");
    }
    
    /**
     * Aplica el estilo normal (no seleccionado)
     */
    public void applyNormalStyle() {
        setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px;");
    }
    
    /**
     * Actualiza los estilos usando el StyleManager
     */
    public void refreshStyles() {
        Platform.runLater(this::applyStyles);
    }
    
    /**
     * Obtiene el script asociado
     */
    public Script getScript() {
        return script;
    }
    
    /**
     * Obtiene el controlador asociado
     */
    public ConfigController getController() {
        return controller;
    }
}
