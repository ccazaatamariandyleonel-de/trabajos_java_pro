package pe.edu.upeu.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import pe.edu.upeu.App;
import pe.edu.upeu.model.Usuario;

import java.io.IOException;

public class DashboardAlumnoController {

    @FXML private Label lblNombreAlumno;
    @FXML private StackPane conteCentroAlumno;

    private Usuario usuarioSesion;

    /**
     * 🔐 MÁGIA RECIBIDORA DE SESIÓN: Esto solucionará tu error
     */
    public void configurarSesion(Usuario usuario) {
        this.usuarioSesion = usuario;
        if (lblNombreAlumno != null && usuario != null) {
            lblNombreAlumno.setText("ALUMNO: " + usuario.getNombre().toUpperCase());
        }
    }

    @FXML
    private void handleCerrarSesion() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            App.getScene().setRoot(root);
            System.out.println("✅ Sesión del alumno cerrada.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}