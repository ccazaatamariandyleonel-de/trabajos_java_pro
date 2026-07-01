package pe.edu.upeu.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import java.util.List;
import pe.edu.upeu.dao.AlumnoDAO;
import pe.edu.upeu.dao.AsistenciaDAO;
import pe.edu.upeu.model.Alumno;
import pe.edu.upeu.model.Asistencia;

public class RegistroAsistenciaDocenteController {

    @FXML private DatePicker dpFechaAsistencia;
    @FXML private TableView<AlumnoAsistencia> tblAsistencia;
    @FXML private TableColumn<AlumnoAsistencia, Integer> colId;
    @FXML private TableColumn<AlumnoAsistencia, String> colNombre;
    @FXML private TableColumn<AlumnoAsistencia, String> colEstado;
    @FXML private Button btnCambiarEstado;

    private ObservableList<AlumnoAsistencia> listaAlumnos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        dpFechaAsistencia.setValue(LocalDate.now());

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tblAsistencia.setItems(listaAlumnos);
        cargarAlumnosDelSalon();
    }

    private void cargarAlumnosDelSalon() {
        listaAlumnos.clear();
        AlumnoDAO dao = new AlumnoDAO();
        try {
            List<Alumno> alumnosBD = dao.listarTodos();

            for (Alumno al : alumnosBD) {
                String nombreCompleto = al.getApellido() + ", " + al.getNombre();
                listaAlumnos.add(new AlumnoAsistencia(al.getId(), nombreCompleto));
            }
        } catch (Exception e) {
            System.err.println("Error al cargar alumnos desde BD: " + e.getMessage());
        }
    }

    @FXML
    private void handleSeleccionarAlumno() {
        AlumnoAsistencia seleccionado = tblAsistencia.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            btnCambiarEstado.setDisable(false);
            btnCambiarEstado.setText(" Cambiar Estado de: " + seleccionado.getId());
        }
    }

    @FXML
    private void handleCambiarEstado() {
        AlumnoAsistencia seleccionado = tblAsistencia.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            String estadoActual = seleccionado.getEstado();
            String nuevoEstado;

            switch (estadoActual) {
                case " Falta": nuevoEstado = " Asistió"; break;
                case " Asistió": nuevoEstado = "⚠ Tardanza"; break;
                default: nuevoEstado = " Falta"; break;
            }
            seleccionado.setEstado(nuevoEstado);
            tblAsistencia.refresh();
        }
    }

    @FXML
    private void handleGuardarAsistenciaBD() {
        // 1. Instanciamos tu DAO de asistencia
        AsistenciaDAO asistDao = new AsistenciaDAO();

        // 2. Obtenemos la fecha en formato String (yyyy-MM-dd)
        String fecha = dpFechaAsistencia.getValue().toString();
        int guardados = 0;

        try {
            // 3. Recorremos los alumnos cargados en la interfaz visual
            for (AlumnoAsistencia al : listaAlumnos) {

                // 4. Creamos una nueva entidad modelo Asistencia
                Asistencia a = new Asistencia();
                a.setAlumnoId(al.getId());
                a.setCursoDocenteId(1); // Se asigna un código de curso referencial para las pruebas
                a.setFecha(fecha);
                a.setEstado(al.getEstado());

                // 5. Invocamos el método guardar de tu AsistenciaDAO
                if (asistDao.guardar(a) > 0) {
                    guardados++;
                }
            }

            // 6. Alerta de confirmación de registro exitoso
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Asistencia Registrada");
            alert.setHeaderText(null);
            alert.setContentText("¡Excelente! Se han persistido " + guardados + " registros de asistencia en la base de datos.");
            alert.showAndWait();

        } catch (Exception e) {
            System.err.println("Error al procesar el guardado de asistencias: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Persistencia");
            alert.setHeaderText("No se pudo completar la operación");
            alert.setContentText("Ocurrió un inconveniente al intentar comunicarse con H2: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public static class AlumnoAsistencia {
        private final int id;
        private final String nombre;
        private String estado;

        public AlumnoAsistencia(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
            this.estado = " Falta";
        }

        public int getId() { return id; }
        public String getNombre() { return nombre; }
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
    }
}