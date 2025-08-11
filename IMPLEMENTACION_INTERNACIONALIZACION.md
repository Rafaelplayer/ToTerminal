# Implementación de Internacionalización en MiniTerminal

## Resumen

He analizado todos los archivos Java del proyecto MiniTerminal y he creado un sistema completo de internacionalización que incluye:

1. **Archivos de idioma JSON** para español e inglés
2. **Clase LanguageManager** para gestionar los idiomas
3. **Documentación completa** del sistema
4. **Ejemplos de integración** en los controladores existentes

## Archivos Creados

### 1. Archivos de Idioma
- `src/main/resources/languages/es.json` - Idioma español (principal)
- `src/main/resources/languages/en.json` - Idioma inglés
- `src/main/resources/languages/config.json` - Configuración del sistema
- `src/main/resources/languages/README.md` - Documentación del sistema

### 2. Clases Java
- `src/main/java/com/ToTerminal/utils/LanguageManager.java` - Gestor principal de idiomas
- `src/main/java/com/ToTerminal/utils/InternationalizationExample.java` - Ejemplos de integración

## Dependencias Requeridas

Agregar al `pom.xml`:

```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
</dependency>
```

## Textos Identificados y Organizados

### Interfaz Principal (MainController)
- **Menús**: Terminal, Scripts, Configuración, Ayuda
- **Elementos de estado**: Ruta, Estado, Hora
- **Mensajes del terminal**: Bienvenida, ayuda, comandos
- **Diálogos**: Ayuda, Acerca de, Confirmación de salida

### Ventana de Configuración (ConfigController)
- **Pestañas**: Apariencia, Terminal, Avanzado, Scripts
- **Temas**: Verde Clásico, Verde Oscuro, Verde Neón, Personalizado
- **Fuentes**: Courier New, Consolas, Monaco, Ubuntu Mono, etc.
- **Opciones del terminal**: Prompt, historial, efectos de sonido
- **Configuración avanzada**: Auto-completado, logging, comandos de inicio

### Creador de Scripts (ScriptCreatorController)
- **Formulario**: Nombre, ruta, lenguaje, descripción
- **Validaciones**: Mensajes de error y éxito
- **Lenguajes**: Java, Node.js
- **Vista previa**: Campos de previsualización
- **Filtros de archivo**: Extensiones por lenguaje

### Intérprete de Comandos (Interpreter)
- **Comandos disponibles**: help, clear, pwd, whoami, date, echo, exit
- **Mensajes de ayuda**: Descripción de cada comando
- **Mensajes de error**: Comandos no reconocidos

## Estructura de Claves del Sistema

```
app.*                    # Información de la aplicación
├── name                # Nombre de la app
├── version             # Versión
└── title               # Título de ventanas

main.*                  # Interfaz principal
├── menu.*              # Menús y elementos de menú
├── status.*            # Barra de estado
├── terminal.*          # Terminal y comandos
├── commands.*          # Sistema de comandos
├── dialogs.*           # Diálogos y alertas
└── windows.*           # Ventanas secundarias

config.*                # Ventana de configuración
├── tabs.*              # Pestañas de configuración
├── appearance.*        # Configuración de apariencia
├── terminal.*          # Configuración del terminal
├── advanced.*          # Configuración avanzada
├── buttons.*           # Botones de acción
└── dialogs.*           # Diálogos de configuración

script_creator.*         # Creador de scripts
├── header.*            # Encabezado de la ventana
├── basic_info.*        # Información básica del script
├── description.*       # Descripción del script
├── help.*              # Textos de ayuda
├── preview.*           # Vista previa
├── validation.*        # Mensajes de validación
├── file_chooser.*      # Selector de archivos
├── dialogs.*           # Diálogos de confirmación
└── buttons.*           # Botones de acción

languages.*              # Nombres de lenguajes de programación
common.*                 # Elementos comunes (botones, mensajes)
```

## Pasos para Implementar

### 1. Agregar Dependencia Gson
```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
</dependency>
```

### 2. Inicializar el Sistema
En `MiniTerminalApp.java`:

```java
@Override
public void start(Stage stage) throws IOException {
    // Inicializar sistema de idiomas
    LanguageManager.getInstance().initializeLanguageSystem();
    
    // ... resto del código existente
}
```

### 3. Integrar en Controladores
En cada controlador, reemplazar textos hardcodeados:

```java
// ANTES
stage.setTitle("Configuración - MiniTerminal");

// DESPUÉS
LanguageManager lang = LanguageManager.getInstance();
stage.setTitle(lang.getText("config.window.title"));
```

### 4. Actualizar Archivos FXML
Reemplazar textos en archivos FXML:

```xml
<!-- ANTES -->
<Menu text="Terminal">

<!-- DESPUÉS -->
<Menu text="%main.menu.terminal">
```

## Ventajas del Sistema Implementado

1. **Centralización**: Todos los textos en archivos JSON organizados
2. **Mantenibilidad**: Fácil actualización y corrección de textos
3. **Escalabilidad**: Agregar nuevos idiomas es sencillo
4. **Consistencia**: Misma estructura para todos los idiomas
5. **Fallback**: Sistema de respaldo robusto
6. **Parámetros**: Soporte para textos dinámicos
7. **Detección automática**: Identifica idioma del sistema

## Ejemplos de Uso

### Texto Simple
```java
String title = LanguageManager.getInstance().getText("app.title");
```

### Texto con Parámetros
```java
String message = LanguageManager.getInstance().getText(
    "main.windows.script_creator.script_created", 
    scriptName, language, path
);
```

### Cambio de Idioma
```java
LanguageManager.getInstance().setCurrentLanguage("en");
```

### Texto con Fallback
```java
String text = LanguageManager.getInstance().getTextWithFallback("some.key");
```

## Agregar Nuevos Idiomas

1. Crear archivo `{codigo}.json` (ej: `fr.json`)
2. Copiar estructura de `es.json`
3. Traducir todos los valores
4. Agregar en `config.json`
5. Cargar en `LanguageManager`

## Consideraciones de Rendimiento

- Los archivos de idioma se cargan una vez al inicio
- El cambio de idioma es instantáneo
- Uso mínimo de memoria
- Carga lazy de idiomas no utilizados

## Próximos Pasos Recomendados

1. **Implementar gradualmente**: Empezar con un controlador
2. **Probar cambios**: Verificar que todos los textos se muestren correctamente
3. **Agregar más idiomas**: Francés, alemán, etc.
4. **Implementar persistencia**: Guardar preferencia de idioma del usuario
5. **Agregar selector de idioma**: En la interfaz de configuración

## Soporte y Mantenimiento

- El sistema es autodocumentado
- Fácil de extender y modificar
- Compatible con la arquitectura existente
- No requiere cambios en la lógica de negocio

Este sistema de internacionalización proporciona una base sólida para hacer que MiniTerminal sea accesible a usuarios de diferentes idiomas, manteniendo la calidad y consistencia de la experiencia del usuario. 