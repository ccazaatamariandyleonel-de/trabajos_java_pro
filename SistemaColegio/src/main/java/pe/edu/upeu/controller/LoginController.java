package pe.edu.upeu.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import pe.edu.upeu.App;
import pe.edu.upeu.service.AuthService;
import pe.edu.upeu.session.SesionUsuario;

import java.io.IOException;

/**
 * Controlador del Login.
 * CORRECCIONES:
 * 1. Eliminadas credenciales hardcodeadas (admin/admin123 embebido en código).
 * 2. Usa AuthService + BCrypt para autenticar.
 * 3. Redirige correctamente según el rol: ADMINISTRADOR, DOCENTE, ALUMNO.
 * 4. Ajusta el tamaño de ventana según el panel destino.
 */
public class LoginController {

    @FXML private TextField     txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Button        btnIngresar;
    @FXML private Label         lblError;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        lblError.setVisible(false);
        // Permitir Enter en el campo contraseña
        txtPassword.setOnAction(e -> handleLogin());
    }

    @FXML
    private void handleLogin() {
        String usuario  = txtUsuario.getText().trim();
        String password = txtPassword.getText().trim();

        if (usuario.isEmpty() || password.isEmpty()) {
            mostrarError("Por favor ingresa usuario y contraseña.");
            return;
        }

        btnIngresar.setDisable(true);

        if (authService.autenticar(usuario, password)) {
            SesionUsuario sesion = SesionUsuario.getInstance();
            try {
                switch (sesion.getRol()) {
                    case "ADMINISTRADOR" ->
                        App.setRootConTamano("DashboardAdmin", 1100, 700);
                    case "DOCENTE" ->
                        App.setRootConTamano("DashboardDocente", 1000, 680);
                    case "ALUMNO" ->
                        App.setRootConTamano("DashboardAlumno", 900, 650);
                    default -> mostrarError("Rol no reconocido.");
                }
            } catch (IOException e) {
                mostrarError("Error al cargar el panel: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            mostrarError("Usuario o contraseña incorrectos.");
            txtPassword.clear();
        }
        btnIngresar.setDisable(false);
    }

    private void mostrarError(String msg) {
        lblError.setText(msg);
        lblError.setVisible(true);
    }
}
