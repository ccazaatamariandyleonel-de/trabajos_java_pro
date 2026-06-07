package pe.edu.upeu.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pe.edu.upeu.dao.AsistenciaDAO;
import pe.edu.upeu.model.Asistencia;

import java.time.LocalDate;

public class GestionAsistenciaController {

    // 1. Cambiamos los tipos genéricos <String> por tu modelo real <Asistencia>
    @FXML private ComboBox<String> cbSalones;
    @FXML private DatePicker dpFecha;
    @FXML private TableView<Asistencia> tablaAsistencia;
    @FXML private TableColumn<Asistencia, Integer> colDni; // Usaremos el AlumnoID como identificador en esta columna
    @FXML private TableColumn<Asistencia, String> colApellidos;
    @FXML private TableColumn<Asistencia, String> colNombres;
    @FXML private TableColumn<Asistencia, String> colEstado;
    @FXML private Button btnCargarLista, btnGuardarAsistencia;

    private AsistenciaDAO asistenciaDAO;
    private ObservableList<Asistencia> listaAsistenciaObservable;

    @FXML
    public void initialize() {
        System.out.println("Pantalla de Asistencia inicializada correctamente.");

        // Inicializamos el DAO y la lista que controlará la tabla
        asistenciaDAO = new AsistenciaDAO();
        listaAsistenciaObservable = FXCollections.observableArrayList();

        // 2. Conectamos las columnas con las variables exactas de tu Asistencia.java
        // Nota: PropertyValueFactory busca automáticamente los métodos "getNombreAlumno", "getEstado", etc.
        colDni.setCellValueFactory(new PropertyValueFactory<>("alumnoId"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombreAlumno"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Como en tu modelo juntaste el nombre en "nombreAlumno", apuntaremos ambos campos ahí por ahora
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("nombreAlumno"));

        // 3. Ponemos la fecha de hoy por defecto en el DatePicker
        dpFecha.setValue(LocalDate.now());

        // 4. Dejamos el botón guardar apagado hasta que carguen alumnos
        btnGuardarAsistencia.setDisable(true);

        // Datos quemados temporales para el ComboBox
        cbSalones.getItems().addAll("Matemática - 1° Secundaria", "Comunicación - 2° Secundaria");
    }

    @FXML
    private void handleCargarAlumnos() {
        System.out.println("Cargando alumnos para el control de asistencia...");
        listaAsistenciaObservable.clear();

        // Creamos un alumno de prueba usando tu modelo real
        Asistencia alumnoEjemplo = new Asistencia();
        alumnoEjemplo.setAlumnoId(101);
        alumnoEjemplo.setCursoDocenteId(1);
        alumnoEjemplo.setFecha(dpFecha.getValue().toString());
        alumnoEjemplo.setEstado("A"); // A = Asistió
        alumnoEjemplo.setNombreAlumno("Maniatado Cáceres, Elard");

        // Agregamos a la lista y la montamos en la tabla
        listaAsistenciaObservable.add(alumnoEjemplo);
        tablaAsistencia.setItems(listaAsistenciaObservable);

        // Habilitamos el botón guardar
        btnGuardarAsistencia.setDisable(false);
    }

    @FXML
    private void handleGuardar() {
        System.out.println("Guardando asistencia del día en la BD H2...");

        if (listaAsistenciaObservable.isEmpty()) {
            mostrarAlerta("Advertencia", "No hay alumnos cargados en la lista.", Alert.AlertType.WARNING);
            return;
        }

        int registrosExitosos = 0;

        // Recorremos la tabla fila por fila y guardamos en H2 usando tu DAO
        for (Asistencia asis : listaAsistenciaObservable) {
            // Aseguramos que jale la fecha seleccionada en el DatePicker
            asis.setFecha(dpFecha.getValue().toString());

            int rs = asistenciaDAO.guardar(asis);
            if (rs > 0) {
                registrosExitosos++;
            }
        }

        if (registrosExitosos > 0) {
            mostrarAlerta("Éxito", "Se registraron " + registrosExitosos + " asistencias correctamente.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "No se pudo guardar el registro en la base de datos.", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}