package com.ToTerminal.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase que contiene todos los parámetros de configuración de la aplicación
 * que deben ser serializados y almacenados persistentemente.
 * 
 * Esta clase implementa Serializable para permitir el almacenamiento
 * en archivos o bases de datos.
 */
public class ConfigData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // ========================================
    // CONFIGURACIÓN DE APARIENCIA
    // ========================================
    
    /**
     * Familia de fuente seleccionada
     */
    private String selectedFontFamily;
    
    /**
     * Tamaño de fuente en puntos
     */
    private int fontSize;
    
    /**
     * Color de fondo del terminal (en formato hexadecimal)
     */
    private String backgroundColor;
    
    /**
     * Color del texto del terminal (en formato hexadecimal)
     */
    private String textColor;
    
    // ========================================
    // CONFIGURACIÓN DEL TERMINAL
    // ========================================
    
    /**
     * Prompt personalizado del terminal
     */
    private String customPrompt;
    
    /**
     * Indica si se debe mostrar la hora en el prompt
     */
    private boolean showTimeInPrompt;
    
    /**
     * Indica si se debe mostrar la ruta actual en el prompt
     */
    private boolean showPathInPrompt;
    
    /**
     * Límite de comandos en el historial
     */
    private int historyLimit;
    
    // ========================================
    // CONFIGURACIÓN AVANZADA
    // ========================================
    
    /**
     * Indica si se debe habilitar el autocompletado
     */
    private boolean enableAutoComplete;
    
    /**
     * Comando que se ejecuta al iniciar la aplicación
     */
    private String startupCommand;
    
    /**
     * Indica si se deben registrar los comandos ejecutados
     */
    private boolean enableCommandLogging;
    
    /**
     * Ruta del archivo de registro de comandos
     */
    private String logFilePath;
    
    // ========================================
    // CONSTRUCTORES
    // ========================================
    
    /**
     * Constructor por defecto que inicializa con valores predeterminados
     */
    public ConfigData() {
        setDefaultValues();
    }
    
    /**
     * Constructor que permite inicializar con valores específicos
     */
    public ConfigData(String selectedFontFamily, int fontSize, 
                     String backgroundColor, String textColor, String customPrompt,
                     boolean showTimeInPrompt, boolean showPathInPrompt, int historyLimit,
                     boolean enableAutoComplete, String startupCommand,
                     boolean enableCommandLogging, String logFilePath) {
        this.selectedFontFamily = selectedFontFamily;
        this.fontSize = fontSize;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.customPrompt = customPrompt;
        this.showTimeInPrompt = showTimeInPrompt;
        this.showPathInPrompt = showPathInPrompt;
        this.historyLimit = historyLimit;
        this.enableAutoComplete = enableAutoComplete;
        this.startupCommand = startupCommand;
        this.enableCommandLogging = enableCommandLogging;
        this.logFilePath = logFilePath;
    }
    
    // ========================================
    // MÉTODOS PARA ESTABLECER VALORES POR DEFECTO
    // ========================================
    
    /**
     * Establece todos los valores por defecto de la configuración
     */
    public void setDefaultValues() {
        this.selectedFontFamily = "courier_new";
        this.fontSize = 12;
        this.backgroundColor = "#004b23";
        this.textColor = "#ccff33";
        this.customPrompt = "user@ToTerminal:~$";
        this.showTimeInPrompt = true;
        this.showPathInPrompt = true;
        this.historyLimit = 100;
        this.enableAutoComplete = true;
        this.startupCommand = "";
        this.enableCommandLogging = false;
        this.logFilePath = "~/ToTerminal.log";
    }
    
    // ========================================
    // GETTERS Y SETTERS
    // ========================================
    
    public String getSelectedFontFamily() {
        return selectedFontFamily;
    }
    
    public void setSelectedFontFamily(String selectedFontFamily) {
        this.selectedFontFamily = selectedFontFamily;
    }
    
    public int getFontSize() {
        return fontSize;
    }
    
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }
    
    public String getBackgroundColor() {
        return backgroundColor;
    }
    
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    
    public String getTextColor() {
        return textColor;
    }
    
    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }
    
    public String getCustomPrompt() {
        return customPrompt;
    }
    
    public void setCustomPrompt(String customPrompt) {
        this.customPrompt = customPrompt;
    }
    
    public boolean isShowTimeInPrompt() {
        return showTimeInPrompt;
    }
    
    public void setShowTimeInPrompt(boolean showTimeInPrompt) {
        this.showTimeInPrompt = showTimeInPrompt;
    }
    
    public boolean isShowPathInPrompt() {
        return showPathInPrompt;
    }
    
    public void setShowPathInPrompt(boolean showPathInPrompt) {
        this.showPathInPrompt = showPathInPrompt;
    }
    
    public int getHistoryLimit() {
        return historyLimit;
    }
    
    public void setHistoryLimit(int historyLimit) {
        this.historyLimit = historyLimit;
    }
    
    public boolean isEnableAutoComplete() {
        return enableAutoComplete;
    }
    
    public void setEnableAutoComplete(boolean enableAutoComplete) {
        this.enableAutoComplete = enableAutoComplete;
    }
    
    public String getStartupCommand() {
        return startupCommand;
    }
    
    public void setStartupCommand(String startupCommand) {
        this.startupCommand = startupCommand;
    }
    
    public boolean isEnableCommandLogging() {
        return enableCommandLogging;
    }
    
    public void setEnableCommandLogging(boolean enableCommandLogging) {
        this.enableCommandLogging = enableCommandLogging;
    }
    
    public String getLogFilePath() {
        return logFilePath;
    }
    
    public void setLogFilePath(String logFilePath) {
        this.logFilePath = logFilePath;
    }
    
    // ========================================
    // MÉTODOS DE UTILIDAD
    // ========================================
    
    /**
     * Convierte el color hexadecimal a un objeto Color de JavaFX
     * @return Color de JavaFX correspondiente al color hexadecimal
     */
    public javafx.scene.paint.Color getBackgroundColorAsColor() {
        return javafx.scene.paint.Color.web(backgroundColor);
    }
    
    /**
     * Convierte el color hexadecimal a un objeto Color de JavaFX
     * @return Color de JavaFX correspondiente al color hexadecimal
     */
    public javafx.scene.paint.Color getTextColorAsColor() {
        return javafx.scene.paint.Color.web(textColor);
    }
    
    /**
     * Establece el color de fondo desde un objeto Color de JavaFX
     * @param color Color de JavaFX
     */
    public void setBackgroundColorFromColor(javafx.scene.paint.Color color) {
        this.backgroundColor = String.format("#%02x%02x%02x", 
            (int) (color.getRed() * 255),
            (int) (color.getGreen() * 255),
            (int) (color.getBlue() * 255));
    }
    
    /**
     * Establece el color del texto desde un objeto Color de JavaFX
     * @param color Color de JavaFX
     */
    public void setTextColorFromColor(javafx.scene.paint.Color color) {
        this.textColor = String.format("#%02x%02x%02x", 
            (int) (color.getRed() * 255),
            (int) (color.getGreen() * 255),
            (int) (color.getBlue() * 255));
    }
    
    // ========================================
    // MÉTODOS DE COMPARACIÓN Y REPRESENTACIÓN
    // ========================================
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ConfigData that = (ConfigData) obj;
        
        return fontSize == that.fontSize &&
               showTimeInPrompt == that.showTimeInPrompt &&
               showPathInPrompt == that.showPathInPrompt &&
               historyLimit == that.historyLimit &&
               enableAutoComplete == that.enableAutoComplete &&
               enableCommandLogging == that.enableCommandLogging &&
               Objects.equals(selectedFontFamily, that.selectedFontFamily) &&
               Objects.equals(backgroundColor, that.backgroundColor) &&
               Objects.equals(textColor, that.textColor) &&
               Objects.equals(customPrompt, that.customPrompt) &&
               Objects.equals(startupCommand, that.startupCommand) &&
               Objects.equals(logFilePath, that.logFilePath);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(selectedFontFamily, fontSize, backgroundColor, 
                          textColor, customPrompt, showTimeInPrompt, showPathInPrompt, 
                          historyLimit, enableAutoComplete, startupCommand, enableCommandLogging, logFilePath);
    }
    
    @Override
    public String toString() {
        return "ConfigData{" +
               "selectedFontFamily='" + selectedFontFamily + '\'' +
               ", fontSize=" + fontSize +
               ", backgroundColor='" + backgroundColor + '\'' +
               ", textColor='" + textColor + '\'' +
               ", customPrompt='" + customPrompt + '\'' +
               ", showTimeInPrompt=" + showTimeInPrompt +
               ", showPathInPrompt=" + showPathInPrompt +
               ", historyLimit=" + historyLimit +
               ", enableAutoComplete=" + enableAutoComplete +
               ", startupCommand='" + startupCommand + '\'' +
               ", enableCommandLogging=" + enableCommandLogging +
               ", logFilePath='" + logFilePath + '\'' +
               '}';
    }
} 