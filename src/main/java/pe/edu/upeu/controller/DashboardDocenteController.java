package pe.edu.upeu.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import pe.edu.upeu.App;
import pe.edu.upeu.model.Usuario;

import java.io.IOException;

public class DashboardDocenteController {

    @FXML private Label lblNombreDocente;
    @FXML private StackPane conteCentroDocente;

    private Usuario usuarioSesion;

    /**
     * 🔐 MÁGIA RECIBIDORA DE SESIÓN: Esto solucionará tu error
     */
    public void configurarSesion(Usuario usuario) {
        this.usuarioSesion = usuario;
        if (lblNombreDocente != null && usuario != null) {
            lblNombreDocente.setText("DOCENTE: " + usuario.getNombre().toUpperCase());
        }
    }

    @FXML
    private void handleCerrarSesion() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            App.getScene().setRoot(root);
            System.out.println("✅ Sesión del docente cerrada.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}