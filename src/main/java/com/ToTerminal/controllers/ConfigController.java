package com.ToTerminal.controllers;

import com.ToTerminal.ScriptReader.Script;
import com.ToTerminal.ScriptReader.ScriptReader;
import com.ToTerminal.models.ConfigData;
import com.ToTerminal.models.ScriptView;
import com.ToTerminal.utils.InternationalizationHelper;
import com.ToTerminal.utils.Values;
import com.ToTerminal.utils.StyleManager;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controlador para la ventana de configuración
 */
public class ConfigController implements Initializable {

    @FXML private TabPane configTabPane;
    
    // Pestaña Apariencia
    @FXML private Tab appearanceTab;
    @FXML private ColorPicker backgroundColorPicker;
    @FXML private ColorPicker textColorPicker;
    @FXML private Slider fontSizeSlider;
    @FXML private Label fontSizeLabel;
    @FXML private ComboBox<String> fontFamilyComboBox;
    
    // Pestaña Terminal
    @FXML private Tab terminalTab;
    @FXML private TextField promptTextField;
    @FXML private CheckBox showTimeCheckBox;
    @FXML private CheckBox showPathCheckBox;
    @FXML private Spinner<Integer> historyLimitSpinner;
    
    // Pestaña Avanzado
    @FXML private Tab advancedTab;
    @FXML private CheckBox autoCompleteCheckBox;
    @FXML private TextField startupCommandTextField;
    @FXML private CheckBox logCommandsCheckBox;
    @FXML private TextField logFilePathTextField;
    @FXML private Button browseLogFileButton;

    @FXML private Tab scriptsTab;
    @FXML private HBox scriptContent;

    // Botones
    @FXML private Button applyButton;
    @FXML private Button cancelButton;
    @FXML private Button resetDefaultsButton;
    @FXML private Button editButton;
    @FXML private javafx.scene.layout.Region bottomSpacer;
    @FXML private BorderPane bd;

    private ConfigData loadedSettings;
    private StyleManager styleManager;

    public Script selectedScript;
    public ScriptView selectedScriptView;
    public ScriptView lastSelectedScriptView;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicializar StyleManager
        styleManager = StyleManager.getInstance();
        
        loadCurrentSettings();
        setupAppearanceTab();
        setupTerminalTab();
        setupAdvancedTab();
        setupButtons();
        setupSettings();
        updateScripstList();
        
        // Registrar esta ventana en el StyleManager
        Platform.runLater(() -> {
            Stage stage = (Stage) configTabPane.getScene().getWindow();
            if (stage != null) {
                styleManager.registerStage(stage);
            }
        });

        // Alternar barra inferior según la pestaña seleccionada
        configTabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            boolean scriptsSelected = newTab == scriptsTab;
            toggleBottomBarForScripts(scriptsSelected);
            if(scriptsSelected){
                bd.getBottom().setStyle("-fx-alignment: center;");
            }else{
                bd.getBottom().setStyle("-fx-alignment: center-right;");
            }
        });
        toggleBottomBarForScripts(configTabPane.getSelectionModel().getSelectedItem() == scriptsTab);
    }
    
    private void setupAppearanceTab() {
        // Configurar selector de fuente
        fontFamilyComboBox.getItems().addAll(
            InternationalizationHelper.getText("config.appearance.font.options.courier_new"),
            InternationalizationHelper.getText("config.appearance.font.options.consolas"),
            InternationalizationHelper.getText("config.appearance.font.options.monaco"),
            InternationalizationHelper.getText("config.appearance.font.options.ubuntu_mono"),
            InternationalizationHelper.getText("config.appearance.font.options.jetbrains_mono"),
            InternationalizationHelper.getText("config.appearance.font.options.fira_code"),
            InternationalizationHelper.getText("config.appearance.font.options.source_code_pro")
        );
        fontFamilyComboBox.setValue(InternationalizationHelper.getText("config.appearance.font.options.courier_new"));
        
        // Agregar listener para cambios de fuente
        fontFamilyComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (loadedSettings != null && newVal != null) {
                String fontValue = getFontValueFromKey(newVal);
                loadedSettings.setSelectedFontFamily(fontValue);
                applyAppearanceStyles();
            }
        });

        fontSizeSlider.setMin(8);
        fontSizeSlider.setMax(24);
        fontSizeSlider.setValue(12);
        fontSizeSlider.setMajorTickUnit(2);
        fontSizeSlider.setMinorTickCount(1);
        fontSizeSlider.setShowTickLabels(true);
        fontSizeSlider.setShowTickMarks(true);

        fontSizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            fontSizeLabel.setText(String.format("%.0f pt", newVal.doubleValue()));
            // Aplicar cambios de tamaño de fuente en tiempo real
            if (loadedSettings != null) {
                loadedSettings.setFontSize(newVal.intValue());
                applyAppearanceStyles();
            }
        });
        fontSizeLabel.setText("12 pt");

        backgroundColorPicker.setValue(javafx.scene.paint.Color.web("#004b23"));
        textColorPicker.setValue(javafx.scene.paint.Color.web("#ccff33"));
        
        // Agregar listeners para cambios en tiempo real
        backgroundColorPicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (loadedSettings != null && newVal != null) {
                loadedSettings.setBackgroundColorFromColor(newVal);
                applyAppearanceStyles();
            }
        });
        
        textColorPicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (loadedSettings != null && newVal != null) {
                loadedSettings.setTextColorFromColor(newVal);
                applyAppearanceStyles();
            }
        });
    }
    
    private void setupTerminalTab() {
        promptTextField.setText("user@ToTerminal:~$");

        showTimeCheckBox.setSelected(true);
        showPathCheckBox.setSelected(true);

        historyLimitSpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 1000, 100, 10)
        );
    }
    
    private void setupAdvancedTab() {
        autoCompleteCheckBox.setSelected(true);
        logCommandsCheckBox.setSelected(false);

        startupCommandTextField.setPromptText(InternationalizationHelper.getText("config.advanced.startup.placeholder"));
        logFilePathTextField.setText("~/ToTerminal.log");

        logCommandsCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            logFilePathTextField.setDisable(!newVal);
            browseLogFileButton.setDisable(!newVal);
        });
        
        // Inicialmente deshabilitar campos de log
        logFilePathTextField.setDisable(true);
        browseLogFileButton.setDisable(true);
    }
    
    private void setupButtons() {
        applyButton.setOnAction(e -> applySettings());
        cancelButton.setOnAction(e -> closeWindow());
        resetDefaultsButton.setOnAction(e -> resetToDefaults());
        browseLogFileButton.setOnAction(e -> browseLogFile());
        editButton.setOnAction(e -> editelection());
    }
    
    private void loadCurrentSettings() {
        File f = new File(Values.CONFIG_DATA_DIR + "/" + Values.CONFIG_FILE_NAME);
        if(!f.exists()){
            loadedSettings = new ConfigData();
            loadedSettings.setDefaultValues();
            try{
                ObjectOutputStream ob = new ObjectOutputStream(new FileOutputStream(f.getAbsolutePath()));
                ob.writeObject(loadedSettings);
                ob.close();
            }catch(IOException e){
                e.printStackTrace();
            }
           return;
        }

        try{
            ObjectInputStream ob = new ObjectInputStream(new FileInputStream(f));
            ConfigData d = (ConfigData) ob.readObject();
            loadedSettings = d;
            ob.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void saveSettingsToFile(){
        File f = new File(Values.CONFIG_DATA_DIR + "/" + Values.CONFIG_FILE_NAME);
        try{
            ObjectOutputStream ob = new ObjectOutputStream(new FileOutputStream(f.getAbsolutePath()));
            ob.writeObject(loadedSettings);
            ob.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void setupSettings(){
        if (loadedSettings == null) {
            return;
        }
        
        // Verificar que todos los controles estén inicializados
        if (!areControlsInitialized()) {
            System.out.println("Advertencia: Los controles no están completamente inicializados");
            return;
        }
        
        // Configurar pestaña de Apariencia
        setupAppearanceFromConfig();
        
        // Configurar pestaña de Terminal
        setupTerminalFromConfig();
        
        // Configurar pestaña Avanzado
        setupAdvancedFromConfig();
        
        // Actualizar estilos globales a través del StyleManager
        if (styleManager != null) {
            styleManager.updateStyles(loadedSettings);
        }
    }
    
    private boolean areControlsInitialized() {
        return fontFamilyComboBox != null && 
               fontSizeSlider != null && 
               backgroundColorPicker != null && 
               textColorPicker != null && 
               promptTextField != null && 
               showTimeCheckBox != null && 
               showPathCheckBox != null && 
               historyLimitSpinner != null && 
               autoCompleteCheckBox != null && 
               startupCommandTextField != null && 
               logCommandsCheckBox != null && 
               logFilePathTextField != null;
    }
    
    private void setupAppearanceFromConfig() {
        // Configurar familia de fuente
        String fontKey = getFontKeyFromValue(loadedSettings.getSelectedFontFamily());
        if (fontKey != null) {
            fontFamilyComboBox.setValue(InternationalizationHelper.getText(fontKey));
        }
        
        // Configurar tamaño de fuente
        fontSizeSlider.setValue(loadedSettings.getFontSize());
        fontSizeLabel.setText(loadedSettings.getFontSize() + " pt");
        
        // Configurar colores
        backgroundColorPicker.setValue(loadedSettings.getBackgroundColorAsColor());
        textColorPicker.setValue(loadedSettings.getTextColorAsColor());
        
        // Aplicar estilos inline con !important para sobrescribir CSS
        applyAppearanceStyles();
    }
    
    private void applyAppearanceStyles() {
        if (loadedSettings == null) return;
        
        String bgColor = loadedSettings.getBackgroundColor();
        String textColor = loadedSettings.getTextColor();
        String fontSize = loadedSettings.getFontSize() + "px";
        String fontFamily = getFontFamilyCSSValue(loadedSettings.getSelectedFontFamily());
        
        // Aplicar estilos al contenedor principal de configuración
        if (configTabPane != null) {
            configTabPane.setStyle(String.format(
                "-fx-background-color: %s !important; " +
                "-fx-text-fill: %s !important;",
                bgColor, textColor
            ));
        }
        
        // Aplicar estilos a las pestañas
        if (appearanceTab != null) {
            appearanceTab.setStyle(String.format(
                "-fx-background-color: %s !important; " +
                "-fx-text-fill: %s !important;",
                bgColor, textColor
            ));
        }
        
        if (terminalTab != null) {
            terminalTab.setStyle(String.format(
                "-fx-background-color: %s !important; " +
                "-fx-text-fill: %s !important;",
                bgColor, textColor
            ));
        }
        
        if (advancedTab != null) {
            advancedTab.setStyle(String.format(
                "-fx-background-color: %s !important; " +
                "-fx-text-fill: %s !important;",
                bgColor, textColor
            ));
        }
        
        // Aplicar estilos a elementos específicos
        applyStylesToFormElements(bgColor, textColor, fontSize, fontFamily);
        
        // Aplicar estilos a la pestaña de scripts
        applyStylesToScriptsTab(bgColor, textColor, fontSize, fontFamily);
    }
    
    private void applyStylesToScriptsTab(String bgColor, String textColor, String fontSize, String fontFamily) {
        if (scriptsTab != null) {
            scriptsTab.setStyle(String.format(
                "-fx-background-color: %s !important; " +
                "-fx-text-fill: %s !important;",
                bgColor, textColor
            ));
        }
        
        if (scriptContent != null) {
            scriptContent.setStyle(String.format(
                "-fx-background-color: %s !important; " +
                "-fx-text-fill: %s !important;",
                bgColor, textColor
            ));
        }
    }
    
    private void applyStylesToFormElements(String bgColor, String textColor, String fontSize, String fontFamily) {
        // Aplicar estilos a campos de texto
        if (promptTextField != null) {
            promptTextField.setStyle(String.format(
                "-fx-background-color: %s !important; " +
                "-fx-text-fill: %s !important; " +
                "-fx-font-size: %s !important; " +
                "-fx-font-family: %s !important;",
                bgColor, textColor, fontSize, fontFamily
            ));
        }
        
        if (startupCommandTextField != null) {
            startupCommandTextField.setStyle(String.format(
                "-fx-background-color: %s !important; " +
                "-fx-text-fill: %s !important; " +
                "-fx-font-size: %s !important; " +
                "-fx-font-family: %s !important;",
                bgColor, textColor, fontSize, fontFamily
            ));
        }
        
        if (logFilePathTextField != null) {
            logFilePathTextField.setStyle(String.format(
                "-fx-background-color: %s !important; " +
                "-fx-text-fill: %s !important; " +
                "-fx-font-size: %s !important; " +
                "-fx-font-family: %s !important;",
                bgColor, textColor, fontSize, fontFamily
            ));
        }
        
        // Aplicar estilos a ComboBoxes
        if (fontFamilyComboBox != null) {
            fontFamilyComboBox.setStyle(String.format(
                "-fx-background-color: %s !important; " +
                "-fx-text-fill: %s !important; " +
                "-fx-font-size: %s !important; " +
                "-fx-font-family: %s !important;",
                bgColor, textColor, fontSize, fontFamily
            ));
        }
        
        // Aplicar estilos a labels
        if (fontSizeLabel != null) {
            fontSizeLabel.setStyle(String.format(
                "-fx-text-fill: %s !important; " +
                "-fx-font-size: %s !important; " +
                "-fx-font-family: %s !important;",
                textColor, fontSize, fontFamily
            ));
        }
    }
    
    private String getFontFamilyCSSValue(String fontValue) {
        // Convertir valores internos a nombres de fuente CSS válidos
        switch (fontValue) {
            case "courier_new":
                return "'Courier New', monospace";
            case "consolas":
                return "'Consolas', monospace";
            case "monaco":
                return "'Monaco', monospace";
            case "ubuntu_mono":
                return "'Ubuntu Mono', monospace";
            case "jetbrains_mono":
                return "'JetBrains Mono', monospace";
            case "fira_code":
                return "'Fira Code', monospace";
            case "source_code_pro":
                return "'Source Code Pro', monospace";
            default:
                return "'Courier New', monospace";
        }
    }
    
    /**
     * Método público para obtener los estilos de configuración actual
     * Puede ser llamado desde otros controladores para aplicar estilos
     */
    public String getCurrentBackgroundColor() {
        return loadedSettings != null ? loadedSettings.getBackgroundColor() : "#004b23";
    }
    
    public String getCurrentTextColor() {
        return loadedSettings != null ? loadedSettings.getTextColor() : "#ccff33";
    }
    
    public String getCurrentFontSize() {
        return loadedSettings != null ? (loadedSettings.getFontSize() + "px") : "12px";
    }
    
    public String getCurrentFontFamily() {
        return loadedSettings != null ? getFontFamilyCSSValue(loadedSettings.getSelectedFontFamily()) : "'Courier New', monospace";
    }
    
    /**
     * Método para aplicar estilos a un nodo específico (útil para el terminal principal)
     */
    public void applyStylesToNode(javafx.scene.Node node) {
        if (loadedSettings == null || node == null) return;
        
        String bgColor = loadedSettings.getBackgroundColor();
        String textColor = loadedSettings.getTextColor();
        String fontSize = loadedSettings.getFontSize() + "px";
        String fontFamily = getFontFamilyCSSValue(loadedSettings.getSelectedFontFamily());
        
        node.setStyle(String.format(
            "-fx-background-color: %s !important; " +
            "-fx-text-fill: %s !important; " +
            "-fx-font-size: %s !important; " +
            "-fx-font-family: %s !important;",
            bgColor, textColor, fontSize, fontFamily
        ));
    }
    
    /**
     * Método para limpiar todos los estilos inline y permitir que el CSS tome control
     */
    private void clearInlineStyles() {
        // Limpiar estilos del contenedor principal
        if (configTabPane != null) {
            configTabPane.setStyle("");
        }
        
        // Limpiar estilos de las pestañas
        if (appearanceTab != null) appearanceTab.setStyle("");
        if (terminalTab != null) terminalTab.setStyle("");
        if (advancedTab != null) advancedTab.setStyle("");
        if (scriptsTab != null) scriptsTab.setStyle("");
        
        // Limpiar estilos de los elementos de formulario
        if (promptTextField != null) promptTextField.setStyle("");
        if (startupCommandTextField != null) startupCommandTextField.setStyle("");
        if (logFilePathTextField != null) logFilePathTextField.setStyle("");
        if (fontFamilyComboBox != null) fontFamilyComboBox.setStyle("");
        if (fontSizeLabel != null) fontSizeLabel.setStyle("");
        
        // Limpiar estilos del área de scripts
        if (scriptContent != null) scriptContent.setStyle("");
    }
    
    private void setupTerminalFromConfig() {
        // Configurar prompt personalizado
        promptTextField.setText(loadedSettings.getCustomPrompt());
        
        // Configurar opciones de visualización
        showTimeCheckBox.setSelected(loadedSettings.isShowTimeInPrompt());
        showPathCheckBox.setSelected(loadedSettings.isShowPathInPrompt());
        
        // Configurar límite de historial
        if (historyLimitSpinner.getValueFactory() != null) {
            historyLimitSpinner.getValueFactory().setValue(loadedSettings.getHistoryLimit());
        }
    }
    
    private void setupAdvancedFromConfig() {
        // Configurar autocompletado
        autoCompleteCheckBox.setSelected(loadedSettings.isEnableAutoComplete());
        
        // Configurar comando de inicio
        startupCommandTextField.setText(loadedSettings.getStartupCommand());
        
        // Configurar registro de comandos
        logCommandsCheckBox.setSelected(loadedSettings.isEnableCommandLogging());
        logFilePathTextField.setText(loadedSettings.getLogFilePath());
        
        // Habilitar/deshabilitar campos de log según la configuración
        logFilePathTextField.setDisable(!loadedSettings.isEnableCommandLogging());
        browseLogFileButton.setDisable(!loadedSettings.isEnableCommandLogging());
    }
    
    private String getFontKeyFromValue(String fontValue) {
        // Mapear valores de fuente a claves de internacionalización
        switch (fontValue) {
            case "courier_new":
                return "config.appearance.font.options.courier_new";
            case "consolas":
                return "config.appearance.font.options.consolas";
            case "monaco":
                return "config.appearance.font.options.monaco";
            case "ubuntu_mono":
                return "config.appearance.font.options.ubuntu_mono";
            case "jetbrains_mono":
                return "config.appearance.font.options.jetbrains_mono";
            case "fira_code":
                return "config.appearance.font.options.fira_code";
            case "source_code_pro":
                return "config.appearance.font.options.source_code_pro";
            default:
                return "config.appearance.font.options.courier_new";
        }
    }
    
    private String getFontValueFromKey(String fontKey) {
        // Mapear claves de internacionalización a valores internos
        if (fontKey.equals(InternationalizationHelper.getText("config.appearance.font.options.courier_new"))) {
            return "courier_new";
        } else if (fontKey.equals(InternationalizationHelper.getText("config.appearance.font.options.consolas"))) {
            return "consolas";
        } else if (fontKey.equals(InternationalizationHelper.getText("config.appearance.font.options.monaco"))) {
            return "monaco";
        } else if (fontKey.equals(InternationalizationHelper.getText("config.appearance.font.options.ubuntu_mono"))) {
            return "ubuntu_mono";
        } else if (fontKey.equals(InternationalizationHelper.getText("config.appearance.font.options.jetbrains_mono"))) {
            return "jetbrains_mono";
        } else if (fontKey.equals(InternationalizationHelper.getText("config.appearance.font.options.fira_code"))) {
            return "fira_code";
        } else if (fontKey.equals(InternationalizationHelper.getText("config.appearance.font.options.source_code_pro"))) {
            return "source_code_pro";
        }
        return "courier_new";
    }
    
    @FXML
    private void applySettings() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(InternationalizationHelper.getText("config.dialogs.applied.title"));
        alert.setHeaderText(InternationalizationHelper.getText("config.dialogs.applied.header"));
        alert.setContentText(InternationalizationHelper.getText("config.dialogs.applied.content"));

        saveSettings();
        
        alert.showAndWait();
        closeWindow();
    }
    
    private void saveSettings() {
        System.out.println("Guardando configuración...");
        
        // Crear nueva instancia de ConfigData con los valores actuales
        ConfigData newConfig = new ConfigData();
        
        // Guardar configuración de Apariencia
        newConfig.setSelectedFontFamily(getFontValueFromKey(fontFamilyComboBox.getValue()));
        newConfig.setFontSize((int) fontSizeSlider.getValue());
        newConfig.setBackgroundColorFromColor(backgroundColorPicker.getValue());
        newConfig.setTextColorFromColor(textColorPicker.getValue());
        
        // Guardar configuración del Terminal
        newConfig.setCustomPrompt(promptTextField.getText());
        newConfig.setShowTimeInPrompt(showTimeCheckBox.isSelected());
        newConfig.setShowPathInPrompt(showPathCheckBox.isSelected());
        newConfig.setHistoryLimit(historyLimitSpinner.getValue());
        
        // Guardar configuración Avanzada
        newConfig.setEnableAutoComplete(autoCompleteCheckBox.isSelected());
        newConfig.setStartupCommand(startupCommandTextField.getText());
        newConfig.setEnableCommandLogging(logCommandsCheckBox.isSelected());
        newConfig.setLogFilePath(logFilePathTextField.getText());
        
        // Actualizar la configuración cargada
        loadedSettings = newConfig;
        
        // Guardar en archivo
        saveSettingsToFile();
        
        // Actualizar estilos globales
        if (styleManager != null) {
            styleManager.updateStyles(loadedSettings);
        }
        
        System.out.println("Configuración guardada exitosamente");
    }
    
    @FXML
    private void resetToDefaults() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle(InternationalizationHelper.getText("config.dialogs.reset.title"));
        confirmation.setHeaderText(InternationalizationHelper.getText("config.dialogs.reset.header"));
        confirmation.setContentText(InternationalizationHelper.getText("config.dialogs.reset.content"));
        
        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            // Restaurar valores por defecto en el objeto ConfigData
            loadedSettings.setDefaultValues();
            
            // Limpiar estilos inline para permitir que el CSS tome control
            clearInlineStyles();
            
            // Aplicar la configuración por defecto a la interfaz
            setupSettings();
            
            // Restaurar estilos por defecto en el StyleManager
            if (styleManager != null) {
                styleManager.restoreDefaultStyles();
            }
            
            // Habilitar/deshabilitar campos de log según la nueva configuración
            logFilePathTextField.setDisable(!loadedSettings.isEnableCommandLogging());
            browseLogFileButton.setDisable(!loadedSettings.isEnableCommandLogging());
        }
    }
    
    @FXML
    private void browseLogFile() {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle(InternationalizationHelper.getText("config.dialogs.file_chooser.title"));
        fileChooser.getExtensionFilters().addAll(
            new javafx.stage.FileChooser.ExtensionFilter(InternationalizationHelper.getText("config.dialogs.file_chooser.log_files"), "*.log"),
            new javafx.stage.FileChooser.ExtensionFilter(InternationalizationHelper.getText("config.dialogs.file_chooser.text_files"), "*.txt"),
            new javafx.stage.FileChooser.ExtensionFilter(InternationalizationHelper.getText("config.dialogs.file_chooser.all_files"), "*.*")
        );
        
        java.io.File selectedFile = fileChooser.showSaveDialog(browseLogFileButton.getScene().getWindow());
        if (selectedFile != null) {
            logFilePathTextField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void updateScripstList(){
        ArrayList<Script> list = new ScriptReader().getScripts();
        if(list == null){
            return;
        }
        for(Script s : list){
             scriptContent.getChildren().add(new ScriptView(s,this));
        }
    }
    
    @FXML
    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void previewTheme() {
        System.out.println("Vista previa del tema: No disponible");
    }

    private void toggleBottomBarForScripts(boolean scriptsSelected) {
        editButton.setVisible(scriptsSelected);
        editButton.setManaged(scriptsSelected);

        resetDefaultsButton.setVisible(!scriptsSelected);
        resetDefaultsButton.setManaged(!scriptsSelected);

        cancelButton.setVisible(!scriptsSelected);
        cancelButton.setManaged(!scriptsSelected);

        applyButton.setVisible(!scriptsSelected);
        applyButton.setManaged(!scriptsSelected);

        if (bottomSpacer != null) {
            bottomSpacer.setVisible(!scriptsSelected);
            bottomSpacer.setManaged(!scriptsSelected);
        }
    }

    private void editelection(){
        if(selectedScript == null){
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ScriptCreatorInterface.fxml"));
        try {
            Scene scriptCreatorScene = new Scene(loader.load(), 700, 600);
            scriptCreatorScene.getStylesheets().add(getClass().getResource("/css/terminal-styles.css").toExternalForm());

            ScriptCreatorController controller = loader.getController();
            controller.setEditMode(selectedScript);

            Stage scriptCreatorStage = new Stage();
            scriptCreatorStage.setTitle(InternationalizationHelper.getText("main.windows.script_creator.edit_title"));
            scriptCreatorStage.setScene(scriptCreatorScene);
            scriptCreatorStage.initModality(Modality.APPLICATION_MODAL);
            scriptCreatorStage.setResizable(true);
            scriptCreatorStage.setMinWidth(700);
            scriptCreatorStage.setMinHeight(600);

            scriptCreatorStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Stage stage = (Stage) editButton.getScene().getWindow();
        stage.close();
    }

    public void updateSelectionStyle(){
        if(lastSelectedScriptView == null){
            selectedScriptView. setStyle("-fx-background-color: #d0e0f0; -fx-border-color: #aaa; -fx-border-width: 1px; -fx-border-radius: 5px;");
            lastSelectedScriptView = selectedScriptView;
            return;
        }
        if(lastSelectedScriptView != selectedScriptView){
            lastSelectedScriptView.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px;");
            selectedScriptView. setStyle("-fx-background-color: #d0e0f0; -fx-border-color: #aaa; -fx-border-width: 1px; -fx-border-radius: 5px;");
            lastSelectedScriptView = selectedScriptView;
        }
    }

}
