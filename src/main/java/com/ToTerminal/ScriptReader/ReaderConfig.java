package com.ToTerminal.ScriptReader;

import com.ToTerminal.utils.Values;

import java.io.*;
import java.util.ArrayList;

public class ReaderConfig implements Serializable {
    private ArrayList<Script> scripts;
    private static final long serialVersionUID = 829L;

public ReaderConfig() {
        this.scripts = new ArrayList<>();
        // Cargar scripts desde el archivo si existe
        try {
            java.io.File f = new java.io.File(Values.SCRIPTS_DIR + "/scripts.dat");
            if (f.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Values.SCRIPTS_DIR + "/scripts.dat"));
                ReaderConfig config = (ReaderConfig) ois.readObject();
                this.scripts = config.getScripts();
                ois.close();
            }else {
                // Si no existe, inicializar con una lista vacía
                this.scripts = new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void safe(){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Values.SCRIPTS_DIR + "/scripts.dat"));
            oos.writeObject(this);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Script> getScripts() {
        return scripts;
    }
    public void setScript(Script s) {
        this.scripts.add(s);
    }
    public void changeScript(String scriptName, Script newScript) {
        for (int i = 0; i < scripts.size(); i++) {
            if (scripts.get(i).getScriptName().equals(scriptName)) {
                scripts.set(i, newScript);
                return;
            }
        }
    }
}

