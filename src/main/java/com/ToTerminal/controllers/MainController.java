package com.ToTerminal.controllers;

import com.ToTerminal.interpreter.Interpreter;
import com.ToTerminal.models.ConfigData;
import com.ToTerminal.utils.Values;
import com.ToTerminal.utils.InternationalizationHelper;
import com.ToTerminal.utils.StyleManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador principal para la interfaz del terminal
 */
public class MainController implements Initializable, StyleManager.StyleChangeListener {

    @FXML private MenuBar menuBar;
    @FXML private MenuItem configMenuItem;
    @FXML private MenuItem scriptCreatorMenuItem;
    @FXML private MenuItem helpMenuItem;
    @FXML private MenuItem aboutMenuItem;
    
    @FXML private Label currentPath;
    @FXML private Label terminalStatus;
    @FXML private Label currentTime;
    
    @FXML private ScrollPane outputScrollPane;
    @FXML private VBox terminalOutput;
    @FXML private TextField commandInput;
    @FXML private Label promptLabel;
    
    private List<String> commandHistory = new ArrayList<>();
    private int historyIndex = -1;
    private Interpreter interpreter;
    private StyleManager styleManager;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {


        // Inicializar StyleManager
        styleManager = StyleManager.getInstance();
        styleManager.addStyleChangeListener(this);
        
        // Configurar valores iniciales
        currentPath.setText(Values.DEFAULT_PATH);
        terminalStatus.setText(InternationalizationHelper.getText("main.status.ready"));
        updateTime();

        // Configurar prompt
        promptLabel.setText(Values.PROMPT);
        
        // Mensaje de bienvenida
        addOutputLine(InternationalizationHelper.getText("main.terminal.welcome"), "welcome");
        addOutputLine(InternationalizationHelper.getText("main.terminal.help_hint"), "info");
        addOutputLine("", "normal");
        
        // Inicializar el intérprete de comandos
        interpreter = new Interpreter(terminalOutput, currentPath, outputScrollPane);
        
        // Configurar eventos
        setupEventHandlers();
        
        // Actualizar la hora cada segundo
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1), e -> updateTime())
        );
        timeline.setCycleCount(javafx.animation.Timeline.INDEFINITE);
        timeline.play();
        
        // Registrar esta ventana en el StyleManager
        javafx.application.Platform.runLater(() -> {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            if (stage != null) {
                styleManager.registerStage(stage);
            }
        });
    }
    
    private void setupEventHandlers() {
        // Evento para procesar comandos al presionar Enter
        commandInput.setOnKeyPressed(this::handleKeyPressed);
        
        // Eventos del menú
        configMenuItem.setOnAction(e -> openConfigWindow());
        scriptCreatorMenuItem.setOnAction(e -> openScriptCreatorWindow());
        helpMenuItem.setOnAction(e -> showHelp());
        aboutMenuItem.setOnAction(e -> showAbout());
    }
    
    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            processCommand();
        } else if (event.getCode() == KeyCode.UP) {
            navigateHistory(-1);
            event.consume();
        } else if (event.getCode() == KeyCode.DOWN) {
            navigateHistory(1);
            event.consume();
        }
    }
    
    private void processCommand() {
        String command = commandInput.getText().trim();
        if (!command.isEmpty()) {
            // Agregar al historial
            commandHistory.add(command);
            historyIndex = commandHistory.size();
            
            // Mostrar el comando en la salida
            addOutputLine(promptLabel.getText() + " " + command, "command");
            
            // Procesar el comando
            interpreter.executeCommand(command);
            
            // Limpiar el campo de entrada
            commandInput.clear();
        }
    }
    
    
    
    private void navigateHistory(int direction) {
        if (commandHistory.isEmpty()) return;
        
        historyIndex += direction;
        
        if (historyIndex < 0) {
            historyIndex = 0;
        } else if (historyIndex >= commandHistory.size()) {
            historyIndex = commandHistory.size();
            commandInput.clear();
            return;
        }
        
        commandInput.setText(commandHistory.get(historyIndex));
        commandInput.positionCaret(commandInput.getText().length());
    }
    
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
    
    private void updateTime() {
        LocalTime now = LocalTime.now();
        currentTime.setText(now.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }
    
    @FXML
    private void openConfigWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ConfigInterface.fxml"));
            Scene configScene = new Scene(loader.load(), 600, 500);
            configScene.getStylesheets().add(getClass().getResource("/css/terminal-styles.css").toExternalForm());
            
            Stage configStage = new Stage();
            configStage.setTitle(InternationalizationHelper.getText("config.window.title"));
            configStage.setScene(configScene);
            configStage.initModality(Modality.APPLICATION_MODAL);
            configStage.setResizable(false);
            
            configStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            addOutputLine(InternationalizationHelper.getText("main.windows.config.error"), "error");
        }
    }
    
    @FXML
    private void openScriptCreatorWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ScriptCreatorInterface.fxml"));
            Scene scriptCreatorScene = new Scene(loader.load(), 700, 600);
            scriptCreatorScene.getStylesheets().add(getClass().getResource("/css/terminal-styles.css").toExternalForm());
            
            ScriptCreatorController controller = loader.getController();
            
            // Configurar callback para cuando se cree un script
            controller.setOnScriptCreated(script -> {
                addOutputLine(InternationalizationHelper.getText("main.windows.script_creator.script_created", 
                    script.getScriptName()), "info");
                addOutputLine(InternationalizationHelper.getText("main.windows.script_creator.language", 
                    script.getAvailableLanguage().toString()) + ", " + 
                    InternationalizationHelper.getText("main.windows.script_creator.path", 
                    script.getScriptPath()), "normal");
                addOutputLine("", "normal");
            });
            
            Stage scriptCreatorStage = new Stage();
            scriptCreatorStage.setTitle(InternationalizationHelper.getText("main.windows.script_creator.title"));
            scriptCreatorStage.setScene(scriptCreatorScene);
            scriptCreatorStage.initModality(Modality.APPLICATION_MODAL);
            scriptCreatorStage.setResizable(true);
            scriptCreatorStage.setMinWidth(700);
            scriptCreatorStage.setMinHeight(600);
            
            scriptCreatorStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            addOutputLine(InternationalizationHelper.getText("main.windows.script_creator.error"), "error");
        }
    }
    
    @FXML
    private void showHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(InternationalizationHelper.getText("main.dialogs.help.title"));
        alert.setHeaderText(InternationalizationHelper.getText("main.dialogs.help.header"));
        alert.setContentText(InternationalizationHelper.getText("main.dialogs.help.content"));
        alert.showAndWait();
    }
    
    @FXML
    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(InternationalizationHelper.getText("main.dialogs.about.title"));
        alert.setHeaderText(InternationalizationHelper.getText("main.dialogs.about.header"));
        alert.setContentText(InternationalizationHelper.getText("main.dialogs.about.content"));
        alert.showAndWait();
    }
    
    @FXML
    private void clearScreen() {
        terminalOutput.getChildren().clear();
        addOutputLine(InternationalizationHelper.getText("main.terminal.screen_cleared"), "info");
        addOutputLine("", "normal");
    }
    
    @FXML
    private void exitApplication() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle(InternationalizationHelper.getText("main.dialogs.exit.title"));
        confirmation.setHeaderText(InternationalizationHelper.getText("main.dialogs.exit.header"));
        confirmation.setContentText(InternationalizationHelper.getText("main.dialogs.exit.content"));
        
        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            System.exit(0);
        }
    }
    
    @Override
    public void onStyleChanged(ConfigData newConfig) {
        // Aplicar los nuevos estilos al terminal principal
        if (newConfig != null) {
            // Aplicar estilos al área del terminal
            if (terminalOutput != null) {
                styleManager.applyStylesToNode(terminalOutput);
            }
            
            // Aplicar estilos al área de entrada
            if (commandInput != null) {
                styleManager.applyStylesToNode(commandInput);
            }
            
            // Aplicar estilos a las etiquetas
            if (currentPath != null) {
                styleManager.applyStylesToNode(currentPath);
            }
            
            if (terminalStatus != null) {
                styleManager.applyStylesToNode(terminalStatus);
            }
            
            if (currentTime != null) {
                styleManager.applyStylesToNode(currentTime);
            }
            
            if (promptLabel != null) {
                styleManager.applyStylesToNode(promptLabel);
            }
            
            // Aplicar estilos al ScrollPane
            if (outputScrollPane != null) {
                styleManager.applyStylesToNode(outputScrollPane);
            }
        }
    }
}
