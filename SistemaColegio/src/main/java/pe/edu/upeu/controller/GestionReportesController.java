package pe.edu.upeu.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class GestionReportesController {

    @FXML private Button btnPDFAlumnos, btnPDFAsistencia, btnPDFNotas;

    @FXML
    public void initialize() {
        System.out.println("Pantalla de Reportes lista.");
    }

    @FXML private void handleReporteAlumnos() { System.out.println("Generando PDF de alumnos..."); }
    @FXML private void handleReporteAsistencia() { System.out.println("Generando PDF de asistencias..."); }
    @FXML private void handleReporteNotas() { System.out.println("Generando PDF de notas..."); }
}