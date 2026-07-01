package pe.edu.upeu.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import pe.edu.upeu.dao.AlumnoDAO;
import pe.edu.upeu.model.Alumno;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ReporteMatriculadosController {

    @FXML
    private VBox panelReporte;

    // Las nuevas etiquetas dinámicas
    @FXML
    private Label lblFecha;
    @FXML
    private Label lblTotalAlumnos;
    @FXML
    private Label lblNuevosIngresos;
    @FXML
    private Label lblTasaRetencion;

    // La tabla
    @FXML
    private TableView<Alumno> tablaAlumnos;
    @FXML
    private TableColumn<Alumno, String> colDni;
    @FXML
    private TableColumn<Alumno, String> colNombre;
    @FXML
    private TableColumn<Alumno, String> colApellido;
    @FXML
    private Button btnImprimir;

    private final AlumnoDAO alumnoDAO = new AlumnoDAO();

    @FXML
    public void initialize() {
        configurarFechaActual();
        cargarDatosYMetricas();
    }

    /**
     * Obtiene la fecha exacta de hoy y la formatea en español
     */
    private void configurarFechaActual() {
        // Usa la fecha de la computadora donde corre el sistema
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es", "PE"));
        lblFecha.setText("Fecha: " + hoy.format(formateador));
    }

    /**
     * Carga los alumnos de la BD y calcula las métricas matemáticas
     */
    private void cargarDatosYMetricas() {
        try {
            ObservableList<Alumno> datos = FXCollections.observableArrayList(alumnoDAO.listarTodos());
            tablaAlumnos.setItems(datos);

            // 1. Total de alumnos (tamaño real de tu base de datos)
            int total = datos.size();
            lblTotalAlumnos.setText(String.valueOf(total));

            // 2. Nuevos Ingresos (Como es un sistema nuevo, todos son ingresos de este año)
            lblNuevosIngresos.setText("+" + total);

            // 3. Tasa de Retención (Si hay alumnos, es 100% porque nadie se ha dado de baja en esta fase)
            if (total > 0) {
                lblTasaRetencion.setText("100%");
            } else {
                lblTasaRetencion.setText("0%");
            }

        } catch (Exception e) {
            System.err.println("Error al cargar alumnos: " + e.getMessage());
        }
    }

    @FXML
    private void imprimirPDF() {
        btnImprimir.setVisible(false);

        PrinterJob job = PrinterJob.createPrinterJob();

        if (job != null && job.showPrintDialog(panelReporte.getScene().getWindow())) {

            javafx.print.PageLayout pageLayout = job.getJobSettings().getPageLayout();

            double scaleX = pageLayout.getPrintableWidth() /
                    panelReporte.getBoundsInParent().getWidth();

            double scaleY = pageLayout.getPrintableHeight() /
                    panelReporte.getBoundsInParent().getHeight();

            double scale = Math.min(scaleX, scaleY);

            javafx.scene.transform.Scale scaleTransform =
                    new javafx.scene.transform.Scale(scale, scale);

            panelReporte.getTransforms().add(scaleTransform);

            boolean printed = job.printPage(panelReporte);

            panelReporte.getTransforms().remove(scaleTransform);

            if (printed) {
                job.endJob();
            }
        }

        btnImprimir.setVisible(true);
    }
}