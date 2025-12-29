package com.ToTerminal;


import com.ToTerminal.utils.Values;
import com.ToTerminal.utils.InternationalizationHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Aplicación principal MiniTerminal.
 * Esta clase es el punto de entrada de la aplicación JavaFX toTerminal.
 */
public class MiniTerminalApp extends Application {
    
    /**
     * Inicia la aplicación JavaFX.
     * Carga la interfaz principal, configura la escena y muestra la ventana.
     *
     * @param stage El escenario principal de la aplicación.
     * @throws IOException Si falla la carga del archivo FXML.
     */
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

        
        stage.show();
        Values.initialize();
    }

    /**
     * Método principal de entrada.
     * Lanza la aplicación JavaFX.
     *
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        launch();
    }
}
