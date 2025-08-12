module com.ToTerminal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.jdi;
    requires com.google.gson;

    opens com.ToTerminal to javafx.fxml;
    opens com.ToTerminal.controllers to javafx.fxml;
    
    exports com.ToTerminal;
    exports com.ToTerminal.controllers;
    exports com.ToTerminal.ScriptReader;
    exports com.ToTerminal.utils;
    exports com.ToTerminal.models;
}
