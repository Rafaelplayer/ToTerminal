package com.ToTerminal.ScriptReader;

import java.io.Serial;

public class Script implements java.io.Serializable{
    private String scriptName;
    private String scriptPath;
    private AvailableLanguage availableLanguage;
    private String scriptDescription;
    public ScriptState state = ScriptState.DEFAULT_STATE;
    @Serial
    private static final long serialVersionUID = 707;

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
    public void markAsNotFound(){
        this.state  = ScriptState.NOT_FOUND;
    }
}
