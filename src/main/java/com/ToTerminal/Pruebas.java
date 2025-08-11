package com.ToTerminal;

import com.ToTerminal.ScriptReader.Script;
import com.ToTerminal.ScriptReader.ScriptReader;

import java.io.IOException;
import java.io.InputStream;

public class Pruebas {
    public static void main(String[] args) {
        ScriptReader sr = new ScriptReader();
        Script s = sr.getScripts().getFirst();
        ProcessBuilder pb = new ProcessBuilder("java", s.getScriptPath());
        try {
            Process p = pb.start();

            // Leer la salida estándar (stdout)
            InputStream stdout = p.getInputStream();
            byte[] bytes = new byte[1024];
            int bytesRead;
            while ((bytesRead = stdout.read(bytes)) != -1) {
                System.out.println(new String(bytes, 0, bytesRead));
            }

            // Leer el flujo de error (stderr) para evitar bloqueos
            InputStream stderr = p.getErrorStream();
            while ((bytesRead = stderr.read(bytes)) != -1) {
                System.err.println(new String(bytes, 0, bytesRead));
                System.err.println("Error");
            }

            // Esperar a que el proceso termine
            int exitCode = p.waitFor();
            System.out.println("Proceso terminado con código de salida: " + exitCode);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}