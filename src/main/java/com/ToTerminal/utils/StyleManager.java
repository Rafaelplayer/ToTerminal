package com.ToTerminal.utils;

import com.ToTerminal.models.ConfigData;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Gestor centralizado de estilos para toda la aplicación
 * Implementa el patrón Observer para notificar cambios de estilo a todas las ventanas
 */
public class StyleManager {
    
    private static StyleManager instance;
    private ConfigData currentConfig;
    private final List<StyleChangeListener> listeners = new CopyOnWriteArrayList<>();
    private final List<Stage> registeredStages = new CopyOnWriteArrayList<>();
    
    // Constructor privado para singleton
    private StyleManager() {
        // Inicializar con configuración por defecto
        currentConfig = new ConfigData();
        currentConfig.setDefaultValues();
    }
    
    /**
     * Obtiene la instancia única del StyleManager
     */
    public static synchronized StyleManager getInstance() {
        if (instance == null) {
            instance = new StyleManager();
        }
        return instance;
    }
    
    /**
     * Interfaz para escuchar cambios de estilo
     */
    public interface StyleChangeListener {
        void onStyleChanged(ConfigData newConfig);
    }
    
    /**
     * Registra un listener para cambios de estilo
     */
    public void addStyleChangeListener(StyleChangeListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    /**
     * Desregistra un listener
     */
    public void removeStyleChangeListener(StyleChangeListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Registra una ventana para aplicar estilos automáticamente
     */
    public void registerStage(Stage stage) {
        if (stage != null && !registeredStages.contains(stage)) {
            registeredStages.add(stage);
            // Aplicar estilos actuales a la nueva ventana
            applyStylesToStage(stage);
        }
    }
    
    /**
     * Desregistra una ventana
     */
    public void unregisterStage(Stage stage) {
        registeredStages.remove(stage);
    }
    
    /**
     * Actualiza la configuración y notifica a todos los listeners
     */
    public void updateStyles(ConfigData newConfig) {
        if (newConfig == null) return;
        
        this.currentConfig = newConfig;
        
        // Notificar a todos los listeners
        Platform.runLater(() -> {
            for (StyleChangeListener listener : listeners) {
                try {
                    listener.onStyleChanged(newConfig);
                } catch (Exception e) {
                    System.err.println("Error notificando cambio de estilo: " + e.getMessage());
                }
            }
            
            // Aplicar estilos a todas las ventanas registradas
            for (Stage stage : registeredStages) {
                if (stage != null && stage.isShowing()) {
                    applyStylesToStage(stage);
                }
            }
        });
    }
    
    /**
     * Obtiene la configuración actual
     */
    public ConfigData getCurrentConfig() {
        return currentConfig;
    }
    
    /**
     * Aplica estilos a una ventana específica
     */
    public void applyStylesToStage(Stage stage) {
        if (stage == null || currentConfig == null) return;
        
        Scene scene = stage.getScene();
        if (scene == null) return;
        
        Node root = scene.getRoot();
        if (root != null) {
            applyStylesToNode(root);
        }
    }
    
    /**
     * Aplica estilos a un nodo y todos sus hijos recursivamente
     */
    public void applyStylesToNode(Node node) {
        if (node == null || currentConfig == null) return;
        
        // Aplicar estilos al nodo actual
        applyNodeSpecificStyles(node);
        
        // Aplicar estilos a los hijos si es un contenedor
        if (node instanceof Pane) {
            Pane pane = (Pane) node;
            for (Node child : pane.getChildren()) {
                applyStylesToNode(child);
            }
        }
        
        // Aplicar estilos a elementos específicos de JavaFX
        if (node instanceof TabPane) {
            TabPane tabPane = (TabPane) node;
            for (Tab tab : tabPane.getTabs()) {
                if (tab.getContent() != null) {
                    applyStylesToNode(tab.getContent());
                }
            }
        }
    }
    
    /**
     * Aplica estilos específicos según el tipo de nodo
     */
    private void applyNodeSpecificStyles(Node node) {
        if (currentConfig == null) return;
        
        String bgColor = currentConfig.getBackgroundColor();
        String textColor = currentConfig.getTextColor();
        String fontSize = currentConfig.getFontSize() + "px";
        String fontFamily = getFontFamilyCSSValue(currentConfig.getSelectedFontFamily());
        
        // Estilos base para todos los nodos
        String baseStyle = String.format(
            "-fx-background-color: %s !important; " +
            "-fx-text-fill: %s !important; " +
            "-fx-font-size: %s !important; " +
            "-fx-font-family: %s !important;",
            bgColor, textColor, fontSize, fontFamily
        );
        
        // Estilos específicos según el tipo de nodo
        if (node instanceof TextField) {
            TextField textField = (TextField) node;
            textField.setStyle(baseStyle + 
                " -fx-border-color: " + textColor + " !important;" +
                " -fx-prompt-text-fill: " + textColor + " !important;");
        }
        else if (node instanceof TextArea) {
            TextArea textArea = (TextArea) node;
            textArea.setStyle(baseStyle + 
                " -fx-border-color: " + textColor + " !important;");
        }
        else if (node instanceof ComboBox) {
            ComboBox<?> comboBox = (ComboBox<?>) node;
            comboBox.setStyle(baseStyle + 
                " -fx-border-color: " + textColor + " !important;");
        }
        else if (node instanceof CheckBox) {
            CheckBox checkBox = (CheckBox) node;
            checkBox.setStyle(baseStyle);
        }
        else if (node instanceof Label) {
            Label label = (Label) node;
            label.setStyle(baseStyle);
        }
        else if (node instanceof Button) {
            Button button = (Button) node;
            // Los botones mantienen su estilo original para mejor UX
            // Solo cambiamos el texto
            button.setStyle(baseStyle);
        }
        else if (node instanceof Slider) {
            Slider slider = (Slider) node;
            slider.setStyle(baseStyle);
        }
        else if (node instanceof Spinner) {
            Spinner<?> spinner = (Spinner<?>) node;
            spinner.setStyle(baseStyle);
        }
        else if (node instanceof ColorPicker) {
            ColorPicker colorPicker = (ColorPicker) node;
            colorPicker.setStyle(baseStyle + 
                " -fx-border-color: " + textColor + " !important;");
        }
        else if (node instanceof TabPane) {
            TabPane tabPane = (TabPane) node;
            tabPane.setStyle(baseStyle);
        }
        else {
            // Para otros tipos de nodos, aplicar estilos base
            node.setStyle(baseStyle);
        }
    }
    
    /**
     * Convierte valores internos de fuente a valores CSS válidos
     */
    private String getFontFamilyCSSValue(String fontValue) {
        switch (fontValue) {
            case "courier_new":
                return "'Courier New', monospace";
            case "consolas":
                return "'Consolas', monospace";
            case "monaco":
                return "'Monaco', monospace";
            case "ubuntu_mono":
                return "'Ubuntu Mono', monospace";
            case "jetbrains_mono":
                return "'JetBrains Mono', monospace";
            case "fira_code":
                return "'Fira Code', monospace";
            case "source_code_pro":
                return "'Source Code Pro', monospace";
            default:
                return "'Courier New', monospace";
        }
    }
    
    /**
     * Limpia todos los estilos inline de un nodo y sus hijos
     */
    public void clearStylesFromNode(Node node) {
        if (node == null) return;
        
        // Limpiar estilos del nodo actual
        node.setStyle("");
        
        // Limpiar estilos de los hijos si es un contenedor
        if (node instanceof Pane) {
            Pane pane = (Pane) node;
            for (Node child : pane.getChildren()) {
                clearStylesFromNode(child);
            }
        }
        
        // Limpiar estilos de elementos específicos
        if (node instanceof TabPane) {
            TabPane tabPane = (TabPane) node;
            for (Tab tab : tabPane.getTabs()) {
                if (tab.getContent() != null) {
                    clearStylesFromNode(tab.getContent());
                }
            }
        }
    }
    
    /**
     * Restaura los estilos por defecto (CSS)
     */
    public void restoreDefaultStyles() {
        currentConfig.setDefaultValues();
        
        // Limpiar estilos inline de todas las ventanas registradas
        for (Stage stage : registeredStages) {
            if (stage != null && stage.isShowing()) {
                Scene scene = stage.getScene();
                if (scene != null && scene.getRoot() != null) {
                    clearStylesFromNode(scene.getRoot());
                }
            }
        }
        
        // Notificar a todos los listeners
        updateStyles(currentConfig);
    }
} 