package pe.edu.upeu.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import pe.edu.upeu.App;
import java.io.IOException;

public class MenuPrincipalController {

    @FXML private Label lblBienvenida;
    @FXML private Button btnAsistencia;
    @FXML private Button btnAlumnos;
    @FXML private Button btnCursos;
    @FXML private Button btnCerrarSesion;

    // Método que ejecuta JavaFX automáticamente al cargar la pantalla
    public void initialize() {
        // Aquí meteremos lógica más adelante
    }

    // Este método recibe el nombre desde el Login y lo pinta en la pantalla
    public void setMensajeBienvenida(String nombre) {
        lblBienvenida.setText("¡Bienvenido(a), " + nombre + "!");
    }

    @FXML
    private void handleCerrarSesion() {
        try {
            // Regresa al Login usando el método nativo del Archetype
            App.setRoot("view/Login");
        } catch (IOException e) {
            System.err.println("Error al regresar al Login: " + e.getMessage());
        }
    }
}