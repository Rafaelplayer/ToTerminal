package com.ToTerminal.controllers;

import com.ToTerminal.ScriptReader.Script;
import com.ToTerminal.ScriptReader.ScriptReader;
import com.ToTerminal.models.ScriptView;
import com.ToTerminal.utils.InternationalizationHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
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
    @FXML private ComboBox<String> themeComboBox;
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
    @FXML private CheckBox soundEffectsCheckBox;
    
    // Pestaña Avanzado
    @FXML private Tab advancedTab;
    @FXML private CheckBox autoCompleteCheckBox;
    @FXML private CheckBox caseSensitiveCheckBox;
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

    public Script selectedScript;
    public ScriptView selectedScriptView;
    public ScriptView lastSelectedScriptView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupAppearanceTab();
        setupTerminalTab();
        setupAdvancedTab();
        setupButtons();
        loadCurrentSettings();
        updateScripstList();

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
        // Configurar ComboBox de temas
        themeComboBox.getItems().addAll(
            InternationalizationHelper.getText("config.appearance.theme.options.classic_green"),
            InternationalizationHelper.getText("config.appearance.theme.options.dark_green"),
            InternationalizationHelper.getText("config.appearance.theme.options.neon_green"),
            InternationalizationHelper.getText("config.appearance.theme.options.custom")
        );
        themeComboBox.setValue(InternationalizationHelper.getText("config.appearance.theme.options.classic_green"));
        
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

        fontSizeSlider.setMin(8);
        fontSizeSlider.setMax(24);
        fontSizeSlider.setValue(12);
        fontSizeSlider.setMajorTickUnit(2);
        fontSizeSlider.setMinorTickCount(1);
        fontSizeSlider.setShowTickLabels(true);
        fontSizeSlider.setShowTickMarks(true);

        fontSizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            fontSizeLabel.setText(String.format("%.0f pt", newVal.doubleValue()));
        });
        fontSizeLabel.setText("12 pt");

        backgroundColorPicker.setValue(javafx.scene.paint.Color.web("#004b23"));
        textColorPicker.setValue(javafx.scene.paint.Color.web("#ccff33"));
    }
    
    private void setupTerminalTab() {
        promptTextField.setText("user@ToTerminal:~$");

        showTimeCheckBox.setSelected(true);
        showPathCheckBox.setSelected(true);
        soundEffectsCheckBox.setSelected(false);

        historyLimitSpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 1000, 100, 10)
        );
    }
    
    private void setupAdvancedTab() {
        autoCompleteCheckBox.setSelected(true);
        caseSensitiveCheckBox.setSelected(false);
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
        // Aquí cargarías la configuración actual desde un archivo de propiedades
        // o desde la configuración guardada de la aplicación
        // Por ahora usamos valores por defecto
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
        System.out.println("Tema: " + themeComboBox.getValue());
        System.out.println("Fuente: " + fontFamilyComboBox.getValue());
        System.out.println("Tamaño fuente: " + fontSizeSlider.getValue());
        System.out.println("Prompt: " + promptTextField.getText());
        System.out.println("Mostrar tiempo: " + showTimeCheckBox.isSelected());
        System.out.println("Auto completar: " + autoCompleteCheckBox.isSelected());
    }
    
    @FXML
    private void resetToDefaults() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle(InternationalizationHelper.getText("config.dialogs.reset.title"));
        confirmation.setHeaderText(InternationalizationHelper.getText("config.dialogs.reset.header"));
        confirmation.setContentText(InternationalizationHelper.getText("config.dialogs.reset.content"));
        
        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            themeComboBox.setValue(InternationalizationHelper.getText("config.appearance.theme.options.classic_green"));
            fontFamilyComboBox.setValue(InternationalizationHelper.getText("config.appearance.font.options.courier_new"));
            fontSizeSlider.setValue(12);
            backgroundColorPicker.setValue(javafx.scene.paint.Color.web("#004b23"));
            textColorPicker.setValue(javafx.scene.paint.Color.web("#ccff33"));
            
            promptTextField.setText("user@ToTerminal:~$");
            showTimeCheckBox.setSelected(true);
            showPathCheckBox.setSelected(true);
            soundEffectsCheckBox.setSelected(false);
            historyLimitSpinner.getValueFactory().setValue(100);
            
            autoCompleteCheckBox.setSelected(true);
            caseSensitiveCheckBox.setSelected(false);
            logCommandsCheckBox.setSelected(false);
            startupCommandTextField.clear();
            logFilePathTextField.setText("~/ToTerminal.log");
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
        String selectedTheme = themeComboBox.getValue();
        System.out.println("Vista previa del tema: " + selectedTheme);
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
