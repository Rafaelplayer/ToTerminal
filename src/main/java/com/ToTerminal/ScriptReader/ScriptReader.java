package com.ToTerminal.ScriptReader;

import com.ToTerminal.utils.Values;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Clase encargada de leer y gestionar los scripts guardados.
 * Maneja la persistencia de la lista de scripts y verifica su disponibilidad.
 */
public class ScriptReader {
    private ArrayList<Script> scripts;
    private final String dataDirectory = Values.SCRIPTS_DIR;
    
    /**
     * Constructor que inicializa el lector de scripts.
     * Intenta cargar la configuración de scripts existente desde el archivo de datos.
     * Si no existe, crea una lista nueva.
     * Actualiza el estado de los scripts (si existen o no).
     */
    public ScriptReader() {
        File f = new File(dataDirectory + "/scripts.dat");
        if(f.exists()){
            try {
                ObjectInputStream ois = new ObjectInputStream( new FileInputStream(dataDirectory + "/scripts.dat"));
                ReaderConfig config = (ReaderConfig) ois.readObject();
                this.scripts = config.getScripts();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }else{
            this.scripts = new ArrayList<>();
        }
        updateState();

    }
    /**
     * Actualiza el estado de cada script en la lista.
     * Verifica si el archivo del script existe físicamente en el disco.
     */
    public void updateState(){
        for (Script script : scripts) {
            if (!new File(script.getScriptPath()).exists()) {
                script.markAsNotFound();
            }
        }
    }

    /**
     * Obtiene la lista de scripts cargados.
     * @return Lista de objetos Script.
     */
    public ArrayList<Script> getScripts() {
        return scripts;
    }
}