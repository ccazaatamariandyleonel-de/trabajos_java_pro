package pe.edu.upeu.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import pe.edu.upeu.App;
import pe.edu.upeu.session.SesionUsuario;
import java.io.IOException;

public class DashboardAlumnoController {

    @FXML
    private Label lblBienvenida;

    @FXML
    private VBox panelCentral;

    @FXML
    public void initialize() {
        // Configuramos el texto inicial de bienvenida
        lblBienvenida.setText("Bienvenido(a), " + SesionUsuario.getInstance().getNombreCompleto());

        // Ahora sí podemos llamarlo desde el arranque con total seguridad
        handleMisCursos();
    }

    // 1. Botón "Mi Dashboard" - Reutiliza el panel original sin usar FXML extras
    @FXML
    private void handleMisCursos() {
        System.out.println("Regresando al inicio del Dashboard (Vista limpia)...");

        // Limpiamos el contenedor central
        panelCentral.getChildren().clear();

        // Reconfiguramos la etiqueta de bienvenida principal
        lblBienvenida.setText("Bienvenido(a), " + SesionUsuario.getInstance().getNombreCompleto());
        lblBienvenida.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1A365D;");

        // Creamos el subtítulo de forma dinámica por código
        Label lblSubtitulo = new Label("Selecciona una opción del menú lateral.");
        lblSubtitulo.setStyle("-fx-text-fill: #666; -fx-font-size: 14px;");

        // Volvemos a meterlos al VBox para restaurar la vista de inicio
        panelCentral.getChildren().addAll(lblBienvenida, lblSubtitulo);
    }

    // 2. Botón "Mis Notas"
    @FXML
    private void handleVerNotas() {
        System.out.println("Cargando vista de Notas del Alumno...");
        cargarPantallaInterna("NotasAlumno");
    }

    // 3. Botón "Mi Asistencia"
    @FXML
    private void handleVerAsistencia() {
        System.out.println("Cargando vista de Asistencias del Alumno...");
        cargarPantallaInterna("AsistenciaAlumno");
    }

    // 4. Botón "Mi Horario"
    @FXML
    private void handleVerHorario() {
        System.out.println("Cargando vista de Horario del Alumno...");
        cargarPantallaInterna("HorarioAlumno");
    }

    private void cargarPantallaInterna(String nombreFxml) {
        try {
            // Limpiamos el contenedor antes de inyectar la tabla elegida
            panelCentral.getChildren().clear();

            String rutaCompleta = "/view/" + nombreFxml + ".fxml";
            System.out.println("Buscando el archivo físico en: " + rutaCompleta);

            java.net.URL urlFxml = getClass().getResource(rutaCompleta);

            if (urlFxml == null) {
                System.err.println(" ERROR: No se encontró el archivo " + nombreFxml + ".fxml");
                return;
            }

            FXMLLoader loader = new FXMLLoader(urlFxml);
            Parent vistaInterna = loader.load();

            panelCentral.getChildren().add(vistaInterna);
            System.out.println(" ¡Vista " + nombreFxml + " cargada con éxito!");

        } catch (IOException e) {
            System.err.println("Error al cargar la pantalla interna " + nombreFxml + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCerrarSesion() {
        SesionUsuario.getInstance().cerrarSesion();
        try {
            App.setRootConTamano("Login", 480, 520);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}