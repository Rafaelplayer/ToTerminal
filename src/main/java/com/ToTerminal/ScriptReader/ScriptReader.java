package com.ToTerminal.ScriptReader;

import com.ToTerminal.utils.Values;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class ScriptReader {
    private ArrayList<Script> scripts;
    private final String dataDirectory = Values.SCRIPTS_DIR;
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
    public void updateState(){
        for (Script script : scripts) {
            if (!new File(script.getScriptPath()).exists()) {
                script.markAsNotFound();
            }
        }
    }

    public ArrayList<Script> getScripts() {
        return scripts;
    }
}