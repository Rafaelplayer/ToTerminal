package com.ToTerminal;


import com.ToTerminal.utils.Values;
import com.ToTerminal.utils.InternationalizationHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Aplicación principal MiniTerminal
 */
public class MiniTerminalApp extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MiniTerminalApp.class.getResource("/fxml/MainInterface.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        
        // Aplicar estilos CSS
        scene.getStylesheets().add(getClass().getResource("/css/terminal-styles.css").toExternalForm());
        
        stage.setTitle(InternationalizationHelper.getText("app.title"));
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        // Icono de la aplicación (opcional)
        // stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/terminal-icon.png")));
        
        stage.show();
        Values.initialize();
    }

    public static void main(String[] args) {
        launch();
    }
}
