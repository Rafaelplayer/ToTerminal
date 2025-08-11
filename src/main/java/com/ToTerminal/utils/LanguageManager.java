package com.ToTerminal.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Gestor de idiomas para la aplicación MiniTerminal
 * Maneja la carga y acceso a los archivos de idioma JSON
 */
public class LanguageManager {
    
    private static final String DEFAULT_LANGUAGE = "en";
    private static final String LANGUAGE_PATH = "/languages/";
    private static final String LANGUAGE_EXTENSION = ".json";
    
    private static LanguageManager instance;
    private Map<String, JsonObject> languageFiles;
    private String currentLanguage;
    private Gson gson;
    
    private LanguageManager() {
        languageFiles = new HashMap<>();
        gson = new Gson();

        String systemLanguage = Locale.getDefault().getLanguage();
        if (getLanguageDisplayName(systemLanguage) != systemLanguage) {
            currentLanguage = systemLanguage;
            loadLanguage(systemLanguage);
            System.out.println("cargando");
            return;
        }
        System.out.println("cargado");
        currentLanguage = DEFAULT_LANGUAGE;
        loadLanguage(DEFAULT_LANGUAGE);
    }
    
    
    /**
     * Obtiene la instancia singleton del LanguageManager
     * @return Instancia del LanguageManager
     */
    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }
    
    /**
     * Carga un archivo de idioma específico
     * @param languageCode Código del idioma (ej: "es", "en")
     */
    public void loadLanguage(String languageCode) {
        if (languageFiles.containsKey(languageCode)) {
            currentLanguage = languageCode;
            return;
        }
        
        try {
            String resourcePath = LANGUAGE_PATH + languageCode + LANGUAGE_EXTENSION;
            InputStream inputStream = getClass().getResourceAsStream(resourcePath);
            
            if (inputStream != null) {
                JsonObject languageData = gson.fromJson(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8), 
                    JsonObject.class
                );
                languageFiles.put(languageCode, languageData);
                currentLanguage = languageCode;
                System.out.println("Idioma cargado: " + languageCode);
            } else {
                System.err.println("No se pudo cargar el archivo de idioma: " + resourcePath);
                // Cargar idioma por defecto si falla
                if (!languageCode.equals(DEFAULT_LANGUAGE)) {
                    loadLanguage(DEFAULT_LANGUAGE);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar el idioma " + languageCode + ": " + e.getMessage());
            // Cargar idioma por defecto si falla
            if (!languageCode.equals(DEFAULT_LANGUAGE)) {
                loadLanguage(DEFAULT_LANGUAGE);
            }
        }
    }
    
    /**
     * Obtiene un texto del idioma actual
     * @param key Clave del texto (ej: "main.menu.terminal")
     * @return Texto en el idioma actual o la clave si no se encuentra
     */
    public String getText(String key) {
        return getText(key, currentLanguage);
    }
    
    /**
     * Obtiene un texto de un idioma específico
     * @param key Clave del texto
     * @param languageCode Código del idioma
     * @return Texto en el idioma especificado o la clave si no se encuentra
     */
    public String getText(String key, String languageCode) {
        JsonObject languageData = languageFiles.get(languageCode);
        if (languageData == null) {
            return key;
        }
        
        String[] keyParts = key.split("\\.");
        JsonObject current = languageData;
        
        for (int i = 0; i < keyParts.length - 1; i++) {
            if (current.has(keyParts[i]) && current.get(keyParts[i]).isJsonObject()) {
                current = current.getAsJsonObject(keyParts[i]);
            } else {
                return key;
            }
        }
        
        if (current.has(keyParts[keyParts.length - 1])) {
            return current.get(keyParts[keyParts.length - 1]).getAsString();
        }
        
        return key;
    }
    
    /**
     * Obtiene un texto con parámetros de sustitución
     * @param key Clave del texto
     * @param params Parámetros para sustituir {0}, {1}, etc.
     * @return Texto con parámetros sustituidos
     */
    public String getText(String key, Object... params) {
        String text = getText(key);
        for (int i = 0; i < params.length; i++) {
            text = text.replace("{" + i + "}", String.valueOf(params[i]));
        }
        return text;
    }
    
    /**
     * Obtiene el idioma actual
     * @return Código del idioma actual
     */
    public String getCurrentLanguage() {
        return currentLanguage;
    }
    
    /**
     * Establece el idioma actual
     * @param languageCode Código del idioma
     */
    public void setCurrentLanguage(String languageCode) {
        if (!languageFiles.containsKey(languageCode)) {
            loadLanguage(languageCode);
        }
        currentLanguage = languageCode;
    }
    
    /**
     * Obtiene la lista de idiomas disponibles
     * @return Array con los códigos de idioma disponibles
     */
    public String[] getAvailableLanguages() {
        return languageFiles.keySet().toArray(new String[0]);
    }
    
    /**
     * Verifica si un idioma está disponible
     * @param languageCode Código del idioma
     * @return true si el idioma está disponible
     */
    public boolean isLanguageAvailable(String languageCode) {
        return languageFiles.containsKey(languageCode);
    }
    
    /**
     * Obtiene el nombre del idioma en su propio idioma
     * @param languageCode Código del idioma
     * @return Nombre del idioma
     */
    public String getLanguageDisplayName(String languageCode) {
        switch (languageCode) {
            case "es":
                return "Español";
            case "en":
                return "English";
            default:
                return languageCode;
        }
    }
    
    /**
     * Carga todos los idiomas disponibles
     */
    public void loadAllLanguages() {
        loadLanguage("es");
        loadLanguage("en");
    }
    
    /**
     * Obtiene un texto del idioma actual con fallback al idioma por defecto
     * @param key Clave del texto
     * @return Texto en el idioma actual o en el idioma por defecto
     */
    public String getTextWithFallback(String key) {
        String text = getText(key);
        if (text.equals(key) && !currentLanguage.equals(DEFAULT_LANGUAGE)) {
            text = getText(key, DEFAULT_LANGUAGE);
        }
        return text;
    }
} 