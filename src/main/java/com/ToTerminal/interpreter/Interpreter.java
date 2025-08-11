package com.ToTerminal.interpreter;

import com.ToTerminal.utils.InternationalizationHelper;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * Clase encargada de interpretar y ejecutar los comandos del terminal
 */
public class Interpreter {
    
    private final VBox terminalOutput;
    private final Label currentPath;
    private final ScrollPane outputScrollPane;
    
    /**
     * Constructor que recibe las dependencias necesarias para ejecutar comandos
     * @param terminalOutput VBox donde se muestran las salidas
     * @param currentPath Label que contiene la ruta actual
     * @param outputScrollPane ScrollPane para auto-scroll
     */
    public Interpreter(VBox terminalOutput, Label currentPath, ScrollPane outputScrollPane) {
        this.terminalOutput = terminalOutput;
        this.currentPath = currentPath;
        this.outputScrollPane = outputScrollPane;
    }
    
    /**
     * Ejecuta el comando especificado
     * @param command comando a ejecutar
     */
    public void executeCommand(String command) {
        String[] parts = command.split("\\s+");
        String mainCommand = parts[0].toLowerCase();
        
        switch (mainCommand) {
            case "help":
                showCommandHelp();
                break;
            case "clear":
                terminalOutput.getChildren().clear();
                break;
            case "pwd":
                addOutputLine(currentPath.getText(), "output");
                break;
            case "whoami":
                addOutputLine("user", "output");
                break;
            case "date":
                addOutputLine(java.time.LocalDateTime.now().toString(), "output");
                break;
            case "echo":
                if (parts.length > 1) {
                    String message = command.substring(5); // Remover "echo "
                    addOutputLine(message, "output");
                } else {
                    addOutputLine("", "output");
                }
                break;
            case "exit":
                System.exit(0);
                break;
            default:
                addOutputLine(InternationalizationHelper.getText("main.commands.unknown", mainCommand), "error");
                addOutputLine(InternationalizationHelper.getText("main.commands.help_suggestion"), "info");
        }
        
        addOutputLine("", "normal");
    }
    
    /**
     * Muestra la ayuda de comandos disponibles
     */
    private void showCommandHelp() {
        addOutputLine(InternationalizationHelper.getText("main.commands.help.title"), "info");
        addOutputLine(InternationalizationHelper.getText("main.commands.help.help"), "output");
        addOutputLine(InternationalizationHelper.getText("main.commands.help.clear"), "output");
        addOutputLine(InternationalizationHelper.getText("main.commands.help.pwd"), "output");
        addOutputLine(InternationalizationHelper.getText("main.commands.help.whoami"), "output");
        addOutputLine(InternationalizationHelper.getText("main.commands.help.date"), "output");
        addOutputLine(InternationalizationHelper.getText("main.commands.help.echo"), "output");
        addOutputLine(InternationalizationHelper.getText("main.commands.help.exit"), "output");
    }
    
    /**
     * Agrega una línea de salida al terminal
     * @param text texto a mostrar
     * @param styleClass clase CSS para el estilo
     */
    private void addOutputLine(String text, String styleClass) {
        Label outputLabel = new Label(text);
        outputLabel.getStyleClass().add("output-line");
        outputLabel.getStyleClass().add(styleClass);
        terminalOutput.getChildren().add(outputLabel);
        
        // Auto-scroll hacia abajo
        javafx.application.Platform.runLater(() -> {
            outputScrollPane.setVvalue(1.0);
        });
    }
}
