package com.ToTerminal.utils;

/**
 * Clase helper para facilitar la internacionalización en MiniTerminal
 * Proporciona métodos estáticos para acceder al sistema de idiomas
 */
public class InternationalizationHelper {
    
    private static LanguageManager languageManager;
    
    /**
     * Obtiene la instancia del LanguageManager
     * @return Instancia del LanguageManager
     */
    public static LanguageManager getLanguageManager() {
        if (languageManager == null) {
            languageManager = LanguageManager.getInstance();
        }
        return languageManager;
    }
    
    /**
     * Obtiene un texto del idioma actual
     * @param key Clave del texto
     * @return Texto en el idioma actual
     */
    public static String getText(String key) {
        return getLanguageManager().getText(key);
    }
    
    /**
     * Obtiene un texto con parámetros
     * @param key Clave del texto
     * @param params Parámetros para sustituir
     * @return Texto con parámetros sustituidos
     */
    public static String getText(String key, Object... params) {
        return getLanguageManager().getText(key, params);
    }
    
    /**
     * Obtiene el idioma actual
     * @return Código del idioma actual
     */
    public static String getCurrentLanguage() {
        return getLanguageManager().getCurrentLanguage();
    }
    
    /**
     * Cambia el idioma actual
     * @param languageCode Código del nuevo idioma
     */
    public static void setCurrentLanguage(String languageCode) {
        getLanguageManager().setCurrentLanguage(languageCode);
    }
    
    /**
     * Inicializa el sistema de idiomas
     */
    public static void initialize() {
        getLanguageManager().loadAllLanguages();
    }
} 