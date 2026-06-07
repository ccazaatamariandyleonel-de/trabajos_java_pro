package pe.edu.upeu.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import pe.edu.upeu.dao.NotaDAO;
import pe.edu.upeu.model.Nota;

public class RegistroNotasDocenteController {

    @FXML private TextField txtIdAlumno;
    @FXML private ComboBox<String> cbBimestre;
    @FXML private TextField txtNota;

    @FXML
    public void initialize() {
        // Rellenamos el combo de bimestres
        cbBimestre.getItems().clear();
        cbBimestre.getItems().addAll("1° Bimestre", "2° Bimestre", "3° Bimestre", "4° Bimestre");
    }

    @FXML
    private void handleGuardarNota() {
        String idStr = txtIdAlumno.getText().trim();
        String bimestreStr = cbBimestre.getValue();
        String notaStr = txtNota.getText().trim();

        // 1. Validamos que no haya campos vacíos
        if (idStr.isEmpty() || bimestreStr == null || notaStr.isEmpty()) {
            mostrarAlerta("Campos Vacíos", "Por favor, complete todos los campos antes de guardar.", AlertType.WARNING);
            return;
        }

        try {
            int idAlumno = Integer.parseInt(idStr);
            double calificacion = Double.parseDouble(notaStr);

            // 2. Extraemos solo el número del ComboBox (Ej: de "1° Bimestre" sacamos el 1)
            int numeroBimestre = Integer.parseInt(bimestreStr.substring(0, 1));

            // 3. Creamos el objeto usando TUS métodos reales del modelo Nota
            Nota nuevaNota = new Nota();
            nuevaNota.setAlumnoId(idAlumno);
            nuevaNota.setCursoDocenteId(1); // Código referencial para pruebas
            nuevaNota.setBimestre(numeroBimestre);
            nuevaNota.setNota(calificacion);

            // 4. Invocamos TÚ método real del DAO
            NotaDAO dao = new NotaDAO();
            int resultado = dao.guardar(nuevaNota);

            // 5. Validamos si H2 guardó la fila (resultado > 0)
            if (resultado > 0) {
                mostrarAlerta("Éxito", "¡Nota registrada correctamente en la Base de Datos!", AlertType.INFORMATION);
                handleLimpiar();
            } else {
                mostrarAlerta("Error", "No se pudo registrar la nota. Verifique si el ID del alumno existe.", AlertType.ERROR);
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Formato", "El ID debe ser un número entero y la nota un número decimal (Ej: 16.5).", AlertType.ERROR);
        }
    }

    @FXML
    private void handleLimpiar() {
        txtIdAlumno.clear();
        cbBimestre.setValue(null);
        txtNota.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}