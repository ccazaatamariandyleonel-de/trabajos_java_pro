package pe.edu.upeu.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import pe.edu.upeu.App;
import pe.edu.upeu.model.Usuario;

import java.io.IOException;

public class DashboardAdminController {

    @FXML private Label lblNombreUsuario;
    @FXML private StackPane conteCentro; // El contenedor donde se cargan las vistas

    private Usuario usuarioSesion;

    /**
     * 🔐 Configura la sesión del administrador que viene desde el Login
     */
    public void configurarSesion(Usuario usuario) {
        this.usuarioSesion = usuario;
        if (lblNombreUsuario != null && usuario != null) {
            lblNombreUsuario.setText("ADMIN: " + usuario.getNombre().toUpperCase());
        }
    }

    // ====================================================================
    // 🎯 ACCIONES DEL MENÚ LATERAL (CON RUTAS ABSOLUTAS CORRECTAS "/")
    // ====================================================================

    @FXML
    private void mostrarInicio() {
        // Si tienes una vista para el inicio, la cargas aquí. Por ahora limpia o recarga.
        System.out.println("🏠 Cargando Vista de Inicio / Métricas...");
        // CargarVista("/view/InicioMetricas.fxml"); // Descomenta si tienes este fxml
    }

    @FXML
    private void mostrarGestionAlumnos() {
        System.out.println("🎓 Intentando cargar Gestión de Alumnos...");
        CargarVista("/view/GestionAlumnos.fxml");
    }

    @FXML
    private void mostrarGestionDocentes() {
        System.out.println("👨‍🏫 Intentando cargar Gestión de Docentes...");
        CargarVista("/view/GestionDocentes.fxml");
    }

    @FXML
    private void mostrarSalonesCursos() {
        System.out.println("🏫 Intentando cargar Salones y Cursos...");
        CargarVista("/view/SalonesCursos.fxml");
    }

    @FXML
    private void mostrarHorarios() {
        System.out.println("📅 Intentando cargar Horarios...");
        CargarVista("/view/Horarios.fxml"); // Asegúrate de que el nombre coincida con tu archivo
    }

    /**
     * 🔒 MÉTODOS DE CERRAR SESIÓN (Regresa al Login de forma limpia)
     */
    @FXML
    private void handleCerrarSesion() {
        System.out.println("🔄 Regresando a la pantalla de inicio...");
        try {
            // Cargamos el Login usando la ruta absoluta que ya validamos
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();

            // Cambiamos la escena raíz
            App.getScene().setRoot(root);
            System.out.println("✅ Sesión cerrada con éxito.");
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo regresar al Login: " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * 🛠️ MÉTODO MAESTRO INYECTOR DE VISTAS
     * Carga cualquier FXML dentro del panel central de manera dinámica
     */
    private void CargarVista(String fxmlPath) {
        try {
            // Limpiamos lo que sea que esté en el centro para que no se amontone
            conteCentro.getChildren().clear();

            // Cargamos el nuevo sub-formulario usando la ruta con el slash "/"
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent subVista = loader.load();

            // Lo metemos al contenedor central
            conteCentro.getChildren().add(subVista);
            System.out.println("✅ Vista cargada con éxito: " + fxmlPath);

        } catch (IOException e) {
            mostrarAlerta("Error de Carga", "No se pudo abrir el formulario: " + fxmlPath + "\nVerifica si el archivo existe o tiene errores.", AlertType.ERROR);
            System.err.println("❌ ERROR CRÍTICO AL CARGAR SUBVISTA:");
            e.printStackTrace();
        } catch (NullPointerException e) {
            mostrarAlerta("Recurso no encontrado", "La ruta '" + fxmlPath + "' no existe en resources.", AlertType.ERROR);
            System.err.println("❌ RUTA NULA: " + fxmlPath);
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