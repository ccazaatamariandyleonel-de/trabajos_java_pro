package pe.edu.upeu.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import pe.edu.upeu.App;
import pe.edu.upeu.model.Usuario;
import pe.edu.upeu.util.Conexion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML private TextField txtDni;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnIngresar;

    @FXML
    private void handleLogin() {
        String dniInput = txtDni.getText().trim();
        String passwordInput = txtPassword.getText().trim();

        if (dniInput.isEmpty() || passwordInput.isEmpty()) {
            mostrarAlerta("Campos vacíos", "Por favor, completa tu DNI y contraseña.", AlertType.WARNING);
            return;
        }

        // ====================================================================
        // 🔐 AUTENTICACIÓN DINÁMICA MEDIANTE LA TABLA 'USUARIOS' EN SQLITE
        // ====================================================================
        Connection cn = Conexion.obtenerConexion();
        if (cn == null) {
            mostrarAlerta("Error de Conexión", "No se pudo conectar a la base de datos.", AlertType.ERROR);
            return;
        }

        // Consulta limpia unificada que soporta ADMIN, DOCENTE o ALUMNO directamente
        String sql = "SELECT * FROM usuario WHERE dni = ? AND clave = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, dniInput);
            ps.setString(2, passwordInput);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Instanciamos el objeto espejo encapsulado de POO
                    Usuario usuarioLogueado = new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("dni"),
                            rs.getString("nombre"),
                            rs.getString("clave"),
                            rs.getString("rol")
                    );

                    mostrarAlerta("¡Acceso Concedido!", "Bienvenido al sistema, " + usuarioLogueado.getNombre() + ".", AlertType.INFORMATION);

                    // Ejecutamos el salto dinámico de interfaz enviando el objeto completo
                    redireccionarSegunRol(usuarioLogueado);

                } else {
                    mostrarAlerta("Acceso Denegado", "El DNI o la contraseña son incorrectos.", AlertType.ERROR);
                }
            }
        } catch (SQLException e) {
            mostrarAlerta("Error SQL", "Ocurrió un error al consultar las credenciales: " + e.getMessage(), AlertType.ERROR);
        }
    }

    /**
     * 🎯 REDIRECCIONADOR MAESTRO DE ESCENAS SEGÚN EL ROL DETECTADO
     */
    private void redireccionarSegunRol(Usuario usuario) {
        String fxmlPath = "";
        String titulo = "";

        // Evaluamos el rol extraído directamente de la fila de tu SQLite
        switch (usuario.getRol().toUpperCase()) {
            case "ADMIN":
                fxmlPath = "/view/DashboardAdmin.fxml";
                titulo = "Panel de Control - Administrador";
                break;
            case "DOCENTE":
                fxmlPath = "/view/DashboardDocente.fxml";
                titulo = "Portal Docente";
                break;
            case "ALUMNO":
                fxmlPath = "/view/DashboardAlumno.fxml";
                titulo = "Intranet Alumno";
                break;
            default:
                mostrarAlerta("Error de Permisos", "El rol asignado no tiene una interfaz configurada.", AlertType.ERROR);
                return;
        }

        try {
            System.out.println("🔄 Desplegando escena desde la ruta raíz: " + fxmlPath);

            // Cargamos la interfaz usando el archivo FXML correspondiente
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // 🔐 INYECCIÓN DE SESIÓN DINÁMICA SEGÚN EL CONTROLADOR DESTINO
            if (usuario.getRol().toUpperCase().equals("ADMIN")) {
                DashboardAdminController adminCtrl = loader.getController();
                adminCtrl.configurarSesion(usuario);
            } else if (usuario.getRol().toUpperCase().equals("DOCENTE")) {
                DashboardDocenteController docenteCtrl = loader.getController();
                docenteCtrl.configurarSesion(usuario);
            } else if (usuario.getRol().equalsIgnoreCase("ALUMNO")) {
                DashboardAlumnoController alumnoCtrl = loader.getController();
                alumnoCtrl.configurarSesion(usuario);
            }

            // 🚀 Operación raíz: Intercambiamos el lienzo del Login por el Dashboard
            App.getScene().setRoot(root);
            System.out.println("✅ Cambio completado exitosamente a: " + titulo);

        } catch (IOException e) {
            mostrarAlerta("Error crítico de E/S", "Fallo al enlazar los canales FXML. Asegúrate de haber creado los archivos FXML y controladores correspondientes.", AlertType.ERROR);
            System.err.println("❌ ERROR AL CAMBIAR DE VENTANA:");
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}