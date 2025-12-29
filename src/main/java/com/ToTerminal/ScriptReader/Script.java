package com.ToTerminal.ScriptReader;

import java.io.Serial;

/**
 * Representa un script ejecutable en la aplicación.
 * Almacena metadatos como nombre, ruta, lenguaje y descripción.
 * Implementa Serializable para persistencia.
 */
public class Script implements java.io.Serializable{
    private String scriptName;
    private String scriptPath;
    private AvailableLanguage availableLanguage;
    private String scriptDescription;
    
    /**
     * Estado actual del script (ej. si el archivo existe o no).
     */
    public ScriptState state = ScriptState.DEFAULT_STATE;
    
    @Serial
    private static final long serialVersionUID = 707;

    /**
     * Constructor principal para crear un nuevo objeto Script.
     *
     * @param scriptName El nombre visible del script.
     * @param scriptPath La ruta absoluta al archivo del script.
     * @param availableLanguage El lenguaje de programación del script.
     * @param scriptDescription Una breve descripción de lo que hace el script.
     */
    public Script(String scriptName, String scriptPath, AvailableLanguage availableLanguage, String scriptDescription) {
        this.scriptName = scriptName;
        this.scriptPath = scriptPath;
        this.availableLanguage = availableLanguage;
        this.scriptDescription = scriptDescription;
    }

    public String getScriptName() {
        return scriptName;
    }
    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }
    public String getScriptPath() {
        return scriptPath;
    }
    public void setScriptPath(String scriptPath) {
        this.scriptPath = scriptPath;
    }
    public AvailableLanguage getAvailableLanguage() {
        return availableLanguage;
    }
    public void setAvailableLanguage(AvailableLanguage availableLanguage) {
        this.availableLanguage = availableLanguage;
    }
    public String getScriptDescription() {
        return scriptDescription;
    }
    public void setScriptDescription(String scriptDescription) {
        this.scriptDescription = scriptDescription;
    }
    
    /**
     * Marca el script como no encontrado.
     * Útil cuando el archivo físico ha sido eliminado o movido.
     */
    public void markAsNotFound(){
        this.state  = ScriptState.NOT_FOUND;
    }
}
