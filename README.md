# ToTerminal / MiniTerminal

Un emulador de terminal minimalista desarrollado con JavaFX, diseñado con una paleta de colores verde retro que evoca los terminales clásicos.

## Características

### Interfaz Principal
- **Terminal funcional** con línea de comandos interactiva
- **Barra de menú** con opciones de configuración y ayuda
- **Barra de estado** que muestra la ruta actual, estado del terminal y hora
- **Historial de comandos** navegable con las flechas ↑↓
- **Auto-scroll** en la salida del terminal
- **Colores temáticos** basados en paleta verde retro

### Interfaz de Configuración
La ventana de configuración incluye tres pestañas principales:

#### 🎨 Pestaña Apariencia
- Selección de temas predefinidos
- Selectores de color personalizados para fondo y texto
- Configuración de fuente (familia y tamaño)
- Vista previa de cambios

#### ⚙️ Pestaña Terminal
- Personalización del prompt
- Configuración de elementos de la interfaz (tiempo, ruta)
- Límite de historial de comandos
- Efectos de sonido opcionales

#### 🔧 Pestaña Avanzado
- Auto-completado de comandos
- Sensibilidad a mayúsculas/minúsculas
- Comando de inicio personalizable
- Registro de actividad (logging)

## Paleta de Colores

El proyecto utiliza una cuidadosa paleta de colores verdes:

- `#004b23` - Verde muy oscuro (fondo principal)
- `#006400` - Verde oscuro (barras y controles)
- `#007200` - Verde medio oscuro (gradientes)
- `#008000` - Verde estándar (bordes y separadores)
- `#38b000` - Verde claro (elementos activos)
- `#70e000` - Verde lima (etiquetas y elementos destacados)
- `#9ef01a` - Verde lima brillante (prompt y títulos)
- `#ccff33` - Verde lima neón (texto principal)

## Comandos Incluidos

El terminal incluye varios comandos básicos:

- `help` - Muestra la lista de comandos disponibles
- `clear` - Limpia la pantalla del terminal
- `pwd` - Muestra el directorio de trabajo actual
- `whoami` - Muestra el usuario actual
- `date` - Muestra la fecha y hora actual
- `echo` - Repite el texto proporcionado
- `exit` - Sale de la aplicación

## Requisitos del Sistema

- **Java**: JDK 11 o superior
- **JavaFX**: Versión 19
- **Maven**: Para gestión de dependencias y construcción

## Instalación y Ejecución

### 1. Clonar/Descargar el proyecto
```bash
cd ~/Documentos/Proyectos/mini-terminal
```

### 2. Compilar y ejecutar con Maven
```bash
# Compilar el proyecto
mvn clean compile

# Ejecutar la aplicación
mvn javafx:run
```

### 3. Alternativa: Ejecutar desde JAR
```bash
# Crear JAR ejecutable
mvn clean package

# Ejecutar JAR (requiere JavaFX en module path)
java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -jar target/mini-terminal-1.0.0.jar
```

## Estructura del Proyecto

```
mini-terminal/
├── src/main/
│   ├── java/
│   │   ├── com/miniterminal/
│   │   │   ├── MiniTerminalApp.java          # Clase principal
│   │   │   └── controllers/
│   │   │       ├── MainController.java      # Controlador principal
│   │   │       └── ConfigController.java    # Controlador de configuración
│   │   └── module-info.java                 # Definición del módulo
│   └── resources/
│       ├── fxml/
│       │   ├── MainInterface.fxml            # Interfaz principal
│       │   └── ConfigInterface.fxml          # Interfaz de configuración
│       └── css/
│           └── terminal-styles.css           # Estilos CSS
├── pom.xml                                   # Configuración Maven
└── README.md                                 # Este archivo
```

## Personalización

### Modificar Comandos
Para agregar nuevos comandos, edita el método `executeCommand()` en `MainController.java`:

```java
case "tucomando":
    // Tu lógica aquí
    addOutputLine("Resultado del comando", "output");
    break;
```

### Cambiar Colores
Los colores se definen en `terminal-styles.css`. Modifica las variables CSS para personalizar la apariencia:

```css
.root {
    -fx-background-color: #tu-color-de-fondo;
    -fx-text-fill: #tu-color-de-texto;
}
```

### Agregar Funcionalidades
- Los controladores están en el paquete `com.ToTerminal.controllers`
- Las interfaces FXML en `src/main/resources/fxml`
- Los estilos CSS en `src/main/resources/css`

## Desarrollo Futuro

Posibles mejoras para implementar:

- [ ] Sistema de archivos virtual
- [ ] Más comandos de terminal (ls, cd, cat, etc.)
- [ ] Autocompletado de comandos
- [ ] Persistencia de configuración
- [ ] Soporte para scripts
- [ ] Temas adicionales
- [ ] Emulación de terminal real

## Contribuir

Este proyecto está diseñado como base educativa. Siéntete libre de:

1. Hacer fork del proyecto
2. Crear una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear un Pull Request

## Licencia

Proyecto educativo - Uso libre para aprendizaje y desarrollo.

## Autor 
Rafaelito Vicioso Fleurimond.

Desarrollado como proyecto educativo para aprender JavaFX y desarrollo de interfaces gráficas.

---

*¡Disfruta explorando el mundo de los terminales retro! 🖥️✨*
