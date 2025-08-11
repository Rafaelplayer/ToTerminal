# Sistema de Internacionalización - MiniTerminal

Este directorio contiene todos los archivos necesarios para la internacionalización de la aplicación MiniTerminal.

## Estructura de archivos

```
languages/
├── config.json          # Configuración del sistema de idiomas
├── es.json             # Archivo de idioma español
├── en.json             # Archivo de idioma inglés
└── README.md           # Este archivo
```

## Archivos de idioma (.json)

Cada archivo de idioma contiene todas las cadenas de texto que se muestran al usuario, organizadas en una estructura jerárquica por funcionalidad.

### Estructura de las claves

Las claves siguen el formato `seccion.subseccion.elemento`:

- `app.*` - Información general de la aplicación
- `main.*` - Interfaz principal del terminal
- `config.*` - Ventana de configuración
- `script_creator.*` - Ventana de creación de scripts
- `languages.*` - Nombres de lenguajes de programación
- `common.*` - Elementos comunes (botones, mensajes, etc.)

### Ejemplo de uso

```java
LanguageManager langManager = LanguageManager.getInstance();

// Obtener texto simple
String title = langManager.getText("app.title");

// Obtener texto con parámetros
String message = langManager.getText("main.windows.script_creator.script_created", 
                                   "miScript", "Java", "/ruta/script.java");

// Cambiar idioma
langManager.setCurrentLanguage("en");
String englishTitle = langManager.getText("app.title");
```

## Configuración (config.json)

El archivo `config.json` contiene la configuración del sistema de idiomas:

- `default_language`: Idioma por defecto
- `available_languages`: Lista de idiomas disponibles
- `fallback_language`: Idioma de respaldo si falla la carga
- `auto_detect`: Si debe detectar automáticamente el idioma del sistema

## Agregar un nuevo idioma

Para agregar un nuevo idioma:

1. Crear un archivo `{codigo}.json` (ej: `fr.json` para francés)
2. Copiar la estructura del archivo `es.json`
3. Traducir todos los valores al nuevo idioma
4. Agregar el idioma en `config.json`
5. Cargar el idioma en `LanguageManager`

### Ejemplo de nuevo idioma

```json
{
  "app": {
    "name": "MiniTerminal",
    "version": "v1.0",
    "title": "MiniTerminal v1.0"
  },
  "main": {
    "menu": {
      "terminal": "Terminal",
      "scripts": "Scripts",
      "config": "Configuration",
      "help": "Aide"
    }
  }
}
```

## Uso en la aplicación

### 1. Inicialización

```java
// En la clase principal
LanguageManager langManager = LanguageManager.getInstance();
langManager.loadAllLanguages();
```

### 2. Cambio de idioma

```java
// Cambiar a inglés
langManager.setCurrentLanguage("en");

// Cambiar a español
langManager.setCurrentLanguage("es");
```

### 3. Obtener textos

```java
// Texto simple
String title = langManager.getText("config.tabs.appearance");

// Texto con parámetros
String message = langManager.getText("main.windows.script_creator.script_created", 
                                   scriptName, language, path);

// Texto con fallback
String text = langManager.getTextWithFallback("some.key");
```

## Ventajas del sistema

1. **Centralización**: Todos los textos están en un solo lugar
2. **Mantenibilidad**: Fácil de actualizar y corregir
3. **Escalabilidad**: Agregar nuevos idiomas es sencillo
4. **Consistencia**: Misma estructura para todos los idiomas
5. **Fallback**: Sistema de respaldo si falla la carga del idioma

## Notas importantes

- Siempre usar claves descriptivas y organizadas
- Mantener la misma estructura en todos los archivos de idioma
- Probar la carga de idiomas antes de publicar
- Usar parámetros para textos dinámicos
- Implementar fallbacks para casos de error

## Dependencias

El sistema requiere la librería Gson para el parsing de JSON:

```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
</dependency>
``` 