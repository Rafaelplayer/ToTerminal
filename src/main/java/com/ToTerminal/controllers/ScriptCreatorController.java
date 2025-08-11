package com.ToTerminal.controllers;

import com.ToTerminal.ScriptReader.AvailableLanguage;
import com.ToTerminal.ScriptReader.ReaderConfig;
import com.ToTerminal.ScriptReader.Script;
import com.ToTerminal.utils.InternationalizationHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * Controlador para la ventana de creación de scripts
 */
public class ScriptCreatorController implements Initializable {

    @FXML private TextField scriptNameField;
    @FXML private TextField scriptPathField;
    @FXML private Button browsePathButton;
    @FXML private ComboBox<AvailableLanguage> languageComboBox;
    @FXML private TextArea scriptDescriptionArea;
    
    // Botones de acción
    @FXML private Button createButton;
    @FXML private Button cancelButton;
    @FXML private Button clearButton;
    
    // Labels de validación
    @FXML private Label nameValidationLabel;
    @FXML private Label pathValidationLabel;
    @FXML private Label descriptionValidationLabel;
    
    // Labels de vista previa
    @FXML private Label previewNameLabel;
    @FXML private Label previewLanguageLabel;
    @FXML private Label previewPathLabel;
    
    private Script createdScript = null;
    private static Consumer<Script> onScriptCreated;
    private  Script editingScript = null;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupLanguageComboBox();
        setupValidation();
        setupEventHandlers();
        clearForm();
    }
    
    private void setupLanguageComboBox() {
        languageComboBox.getItems().addAll(AvailableLanguage.values());
        languageComboBox.setValue(AvailableLanguage.Java); // Valor por defecto
        
        // Personalizar la visualización de los elementos
        languageComboBox.setCellFactory(listView -> new ListCell<AvailableLanguage>() {
            @Override
            protected void updateItem(AvailableLanguage item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                    setGraphic(null);
                } else {
                    setText(getLanguageDisplayName(item));
                }
            }
        });
        
        languageComboBox.setButtonCell(new ListCell<AvailableLanguage>() {
            @Override
            protected void updateItem(AvailableLanguage item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    setText(getLanguageDisplayName(item));
                }
            }
        });
    }
    
    private static String getLanguageDisplayName(AvailableLanguage language) {
        switch (language) {
            case NodeJS:
                return InternationalizationHelper.getText("languages.nodejs");
            case Java:
                return InternationalizationHelper.getText("languages.java");
            default:
                return language.toString();
        }
    }
    
    private void setupValidation() {
        // Limpiar labels de validación inicialmente
        nameValidationLabel.setText("");
        pathValidationLabel.setText("");
        descriptionValidationLabel.setText("");
        
        // Agregar listeners para validación en tiempo real
        scriptNameField.textProperty().addListener((obs, oldText, newText) -> {
            validateScriptName();
            updateCreateButtonState();
            updatePreview();
        });
        
        scriptPathField.textProperty().addListener((obs, oldText, newText) -> {
            validateScriptPath();
            updateCreateButtonState();
            updatePreview();
        });
        
        scriptDescriptionArea.textProperty().addListener((obs, oldText, newText) -> {
            validateDescription();
            updateCreateButtonState();
        });
        
        languageComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            updatePreview();
        });
    }
    
    private void setupEventHandlers() {
        browsePathButton.setOnAction(e -> browseScriptPath());
        createButton.setOnAction(e -> createScript());
        cancelButton.setOnAction(e -> closeWindow());
        clearButton.setOnAction(e -> clearForm());
    }
    
    private boolean validateScriptName() {
        String name = scriptNameField.getText().trim();
        if (name.isEmpty()) {
            nameValidationLabel.setText(InternationalizationHelper.getText("script_creator.validation.name.required"));
            nameValidationLabel.getStyleClass().clear();
            nameValidationLabel.getStyleClass().addAll("validation-label", "error");
            return false;
        } else if (name.length() < 3) {
            nameValidationLabel.setText(InternationalizationHelper.getText("script_creator.validation.name.min_length"));
            nameValidationLabel.getStyleClass().clear();
            nameValidationLabel.getStyleClass().addAll("validation-label", "error");
            return false;
        } else if (!name.matches("^[a-zA-Z0-9_\\-\\s]+$")) {
            nameValidationLabel.setText(InternationalizationHelper.getText("script_creator.validation.name.invalid_chars"));
            nameValidationLabel.getStyleClass().clear();
            nameValidationLabel.getStyleClass().addAll("validation-label", "error");
            return false;
        } else {
            nameValidationLabel.setText(InternationalizationHelper.getText("script_creator.validation.name.valid"));
            nameValidationLabel.getStyleClass().clear();
            nameValidationLabel.getStyleClass().addAll("validation-label", "success");
            return true;
        }
    }
    
    private boolean validateScriptPath() {
        String path = scriptPathField.getText().trim();
        if (path.isEmpty()) {
            pathValidationLabel.setText(InternationalizationHelper.getText("script_creator.validation.path.required"));
            pathValidationLabel.getStyleClass().clear();
            pathValidationLabel.getStyleClass().addAll("validation-label", "error");
            return false;
        } else {
            File file = new File(path);
            if (!file.exists()) {
                pathValidationLabel.setText(InternationalizationHelper.getText("script_creator.validation.path.not_exists"));
                pathValidationLabel.getStyleClass().clear();
                pathValidationLabel.getStyleClass().addAll("validation-label", "warning");
                return true;
            } else if (!file.isFile()) {
                pathValidationLabel.setText(InternationalizationHelper.getText("script_creator.validation.path.not_file"));
                pathValidationLabel.getStyleClass().clear();
                pathValidationLabel.getStyleClass().addAll("validation-label", "error");
                return false;
            } else {
                pathValidationLabel.setText(InternationalizationHelper.getText("script_creator.validation.path.valid"));
                pathValidationLabel.getStyleClass().clear();
                pathValidationLabel.getStyleClass().addAll("validation-label", "success");
                return true;
            }
        }
    }
    
    private boolean validateDescription() {
        String description = scriptDescriptionArea.getText().trim();
        if (description.isEmpty()) {
            descriptionValidationLabel.setText(InternationalizationHelper.getText("script_creator.validation.description.required"));
            descriptionValidationLabel.getStyleClass().clear();
            descriptionValidationLabel.getStyleClass().addAll("validation-label", "error");
            return false;
        } else if (description.length() < 10) {
            descriptionValidationLabel.setText(InternationalizationHelper.getText("script_creator.validation.description.min_length"));
            descriptionValidationLabel.getStyleClass().clear();
            descriptionValidationLabel.getStyleClass().addAll("validation-label", "error");
            return false;
        } else if (description.length() > 500) {
            descriptionValidationLabel.setText(InternationalizationHelper.getText("script_creator.validation.description.max_length"));
            descriptionValidationLabel.getStyleClass().clear();
            descriptionValidationLabel.getStyleClass().addAll("validation-label", "error");
            return false;
        } else {
            int remaining = 500 - description.length();
            descriptionValidationLabel.setText(InternationalizationHelper.getText("script_creator.validation.description.valid", remaining));
            descriptionValidationLabel.getStyleClass().clear();
            descriptionValidationLabel.getStyleClass().addAll("validation-label", "success");
            return true;
        }
    }
    
    private void updateCreateButtonState() {
        boolean isValid = validateScriptName() && validateScriptPath() && validateDescription();
        createButton.setDisable(!isValid);
    }
    
    private void updatePreview() {
        // Actualizar vista previa del nombre
        String name = scriptNameField.getText().trim();
        if (previewNameLabel != null) {
            if (name.isEmpty()) {
                previewNameLabel.setText(InternationalizationHelper.getText("script_creator.preview.placeholder_name"));
                previewNameLabel.getStyleClass().removeAll("preview-value");
                previewNameLabel.getStyleClass().add("preview-placeholder");
            } else {
                previewNameLabel.setText(name);
                previewNameLabel.getStyleClass().removeAll("preview-placeholder");
                previewNameLabel.getStyleClass().add("preview-value");
            }
        }
        
        // Actualizar vista previa del lenguaje
        if (previewLanguageLabel != null) {
            AvailableLanguage language = languageComboBox.getValue();
            if (language != null) {
                previewLanguageLabel.setText(getLanguageDisplayName(language));
            }
        }
        
        // Actualizar vista previa de la ruta
        String path = scriptPathField.getText().trim();
        if (previewPathLabel != null) {
            if (path.isEmpty()) {
                previewPathLabel.setText(InternationalizationHelper.getText("script_creator.preview.placeholder_path"));
                previewPathLabel.getStyleClass().removeAll("preview-value");
                previewPathLabel.getStyleClass().add("preview-placeholder");
            } else {
                previewPathLabel.setText(path);
                previewPathLabel.getStyleClass().removeAll("preview-placeholder");
                previewPathLabel.getStyleClass().add("preview-value");
            }
        }
    }
    
    @FXML
    private void browseScriptPath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(InternationalizationHelper.getText("script_creator.file_chooser.title"));
        
        // Configurar filtros según el lenguaje seleccionado
        AvailableLanguage selectedLanguage = languageComboBox.getValue();
        if (selectedLanguage == AvailableLanguage.Java) {
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(InternationalizationHelper.getText("script_creator.file_chooser.java_files"), "*.java"),
                new FileChooser.ExtensionFilter(InternationalizationHelper.getText("script_creator.file_chooser.jar_files"), "*.jar"),
                new FileChooser.ExtensionFilter(InternationalizationHelper.getText("script_creator.file_chooser.all_files"), "*.*")
            );
        } else if (selectedLanguage == AvailableLanguage.NodeJS) {
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(InternationalizationHelper.getText("script_creator.file_chooser.js_files"), "*.js"),
                new FileChooser.ExtensionFilter(InternationalizationHelper.getText("script_creator.file_chooser.ts_files"), "*.ts"),
                new FileChooser.ExtensionFilter(InternationalizationHelper.getText("script_creator.file_chooser.json_files"), "*.json"),
                new FileChooser.ExtensionFilter(InternationalizationHelper.getText("script_creator.file_chooser.all_files"), "*.*")
            );
        } else {
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(InternationalizationHelper.getText("script_creator.file_chooser.all_files"), "*.*")
            );
        }
        
        File selectedFile = fileChooser.showOpenDialog(browsePathButton.getScene().getWindow());
        if (selectedFile != null) {
            scriptPathField.setText(selectedFile.getAbsolutePath());
            
            // Si el nombre está vacío, sugerir el nombre del archivo sin extensión
            if (scriptNameField.getText().trim().isEmpty()) {
                String fileName = selectedFile.getName();
                int lastDot = fileName.lastIndexOf('.');
                String nameWithoutExtension = (lastDot > 0) ? fileName.substring(0, lastDot) : fileName;
                scriptNameField.setText(nameWithoutExtension);
            }
        }
    }
    
    @FXML
    private void createScript() {
        if (validateForm()) {
            String name = scriptNameField.getText().trim();
            String path = scriptPathField.getText().trim();
            AvailableLanguage language = languageComboBox.getValue();
            String description = scriptDescriptionArea.getText().trim();
            
            createdScript = new Script(name, path, language, description);
            ReaderConfig config = new ReaderConfig();
            config.setScript(createdScript);
            config.safe();


            // Mostrar confirmación
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(InternationalizationHelper.getText("script_creator.dialogs.created.title"));
            alert.setHeaderText(InternationalizationHelper.getText("script_creator.dialogs.created.header"));
            alert.setContentText(InternationalizationHelper.getText("script_creator.dialogs.created.content", 
                name, getLanguageDisplayName(language), path));
            
            alert.showAndWait();
            
            // Notificar al callback si existe
            if (onScriptCreated != null) {
                onScriptCreated.accept(createdScript);
            }
            
            closeWindow();
        }
    }
    
    private  boolean validateForm() {
        return validateScriptName() && validateScriptPath() && validateDescription();
    }
    
    @FXML
    private void clearForm() {
        scriptNameField.clear();
        scriptPathField.clear();
        languageComboBox.setValue(AvailableLanguage.Java);
        scriptDescriptionArea.clear();
        
        // Limpiar mensajes de validación
        nameValidationLabel.setText("");
        pathValidationLabel.setText("");
        descriptionValidationLabel.setText("");
        
        // Deshabilitar botón de crear
        createButton.setDisable(true);
        
        // Enfocar el primer campo
        scriptNameField.requestFocus();
    }
    public  void setEditMode(Script s){
        scriptNameField.setText(s.getScriptName());
        scriptPathField.setText(s.getScriptPath());
        languageComboBox.setValue(s.getAvailableLanguage());
        scriptDescriptionArea.setText(s.getScriptDescription());
        editingScript = s;

        createButton.setText(InternationalizationHelper.getText("script_creator.buttons.update"));
        createButton.setOnAction(e -> updateScript(s.getScriptName()));
    }

    private void updateScript(String sName) {
        if (validateForm()) {
            String name = scriptNameField.getText().trim();
            String path = scriptPathField.getText().trim();
            AvailableLanguage language = languageComboBox.getValue();
            String description = scriptDescriptionArea.getText().trim();

            editingScript.setScriptName(name);
            editingScript.setScriptPath(path);
            editingScript.setAvailableLanguage(language);
            editingScript.setScriptDescription(description);

            ReaderConfig config = new ReaderConfig();
            config.changeScript(sName,editingScript);
            config.safe();

            // Mostrar confirmación
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(InternationalizationHelper.getText("script_creator.dialogs.updated.title"));
            alert.setHeaderText(InternationalizationHelper.getText("script_creator.dialogs.updated.header"));
            alert.setContentText(InternationalizationHelper.getText("script_creator.dialogs.updated.content", 
                name, getLanguageDisplayName(language), path));

            alert.showAndWait();

            // Notificar al callback si existe
            if (onScriptCreated != null) {
                onScriptCreated.accept(editingScript);
            }

            closeWindow();
        }
    }

    @FXML
    private  void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Establece un callback que se ejecutará cuando se cree un script
     * @param callback Función que recibe el script creado
     */
    public void setOnScriptCreated(Consumer<Script> callback) {
        this.onScriptCreated = callback;
    }
    
    /**
     * Obtiene el script creado (null si se canceló)
     * @return El script creado o null
     */
    public Script getCreatedScript() {
        return createdScript;
    }
    
    /**
     * Establece valores iniciales en el formulario
     * @param name Nombre inicial del script
     * @param path Ruta inicial del script
     * @param language Lenguaje inicial
     * @param description Descripción inicial
     */
    public void setInitialValues(String name, String path, AvailableLanguage language, String description) {
        if (name != null) scriptNameField.setText(name);
        if (path != null) scriptPathField.setText(path);
        if (language != null) languageComboBox.setValue(language);
        if (description != null) scriptDescriptionArea.setText(description);
    }
}
