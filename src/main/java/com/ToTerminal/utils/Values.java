package com.ToTerminal.utils;

import java.io.File;

public final class Values {
    // Constantes para la aplicación MiniTerminal
    public static final String APP_NAME = "MiniTerminal";
    public static final String DEFAULT_PATH = System.getProperty("user.home");
    public static final String PROMPT = System.getProperty("user.name") + "@ToTerminal:~$";
    public static final String SCRIPTS_DIR = System.getProperty("user.home") + "/.ToTerminal/scripts";
    public static final String CONFIG_DATA_DIR = System.getProperty("user.home") + "/.ToTerminal/data";
    public static final String CONFIG_FILE_NAME = "conf.dat";
    public static String currentPath = DEFAULT_PATH;
    public Values(){
    }

    public static  void initialize(){
        // Crear directorio de scripts si no existe
        File scriptsDir = new File(SCRIPTS_DIR);
        File configDir = new File(CONFIG_DATA_DIR);
        if (!scriptsDir.exists()) {
            scriptsDir.mkdirs();
        }
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        
    }
}
