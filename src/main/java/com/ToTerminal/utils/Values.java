package com.ToTerminal.utils;

public final class Values {
    // Constantes para la aplicación MiniTerminal
    public static final String APP_NAME = "MiniTerminal";
    public static final String DEFAULT_PATH = System.getProperty("user.home");
    public static final String PROMPT = System.getProperty("user.name") + "@ToTerminal:~$";
    public static final String SCRIPTS_DIR = System.getProperty("user.home") + "/.ToTerminal/scripts";
    public Values(){
    }

    public static  void initialize(){
        // Crear directorio de scripts si no existe
        java.io.File scriptsDir = new java.io.File(SCRIPTS_DIR);
        if (!scriptsDir.exists()) {
            scriptsDir.mkdirs();
        }
    }
}
