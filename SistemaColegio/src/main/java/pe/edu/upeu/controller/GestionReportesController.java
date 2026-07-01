package pe.edu.upeu.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GestionReportesController {

    @FXML private Button btnPDFAlumnos, btnPDFAsistencia, btnPDFNotas;

    @FXML
    public void initialize() {
        System.out.println("Pantalla de Reportes conectada y lista para enrutar.");
    }

    @FXML
    private void handleReporteAlumnos() {
        // La ruta DEBE incluir la carpeta "view/"
        abrirVentana("/view/ReporteMatriculados.fxml", "Reporte Oficial - Matriculados");
    }
    @FXML
    private void handleReporteAsistencia() {
        // Llama al diseño de asistencias
        abrirVentana("/ReporteAsistencias.fxml", "Reporte de Asistencias");
    }

    @FXML
    private void handleReporteNotas() {
        // Llama al diseño de notas
        abrirVentana("/ReporteNotas.fxml", "Cuadro de Méritos");
    }

    /**
     * Motor principal para abrir ventanas emergentes con los reportes.
     */
    private void abrirVentana(String rutaFxml, String titulo) {
        try {
            // Obtenemos el recurso desde el cargador de clases
            var url = getClass().getResource(rutaFxml);

            if (url == null) {
                // Esto te dirá exactamente qué está pasando
                System.err.println("¡Error! No pude encontrar: " + rutaFxml);
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Esto imprimirá el error real en la consola
        }
    }
}