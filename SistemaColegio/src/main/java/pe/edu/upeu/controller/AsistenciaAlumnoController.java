package pe.edu.upeu.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pe.edu.upeu.util.Conexion;
import pe.edu.upeu.session.SesionUsuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AsistenciaAlumnoController {

    @FXML private TableView<FilaAsistencia> tblAsistenciaAlumno;
    @FXML private TableColumn<FilaAsistencia, String> colFecha;
    @FXML private TableColumn<FilaAsistencia, String> colCurso;
    @FXML private TableColumn<FilaAsistencia, String> colEstado;

    private ObservableList<FilaAsistencia> listaAsistencias = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colCurso.setCellValueFactory(new PropertyValueFactory<>("curso"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tblAsistenciaAlumno.setItems(listaAsistencias);
        cargarAsistencias();
    }

    private void cargarAsistencias() {
        listaAsistencias.clear();
        int alumnoId = SesionUsuario.getInstance().getId();

        // Consulta SQL ajustada para obtener los datos reales de la BD
        String sql = "SELECT a.fecha, a.estado, c.nombre AS curso_nombre " +
                "FROM asistencias a " +
                "JOIN curso_docente_salon cds ON a.curso_docente_id = cds.id " +
                "JOIN cursos c ON cds.curso_id = c.id " +
                "WHERE a.alumno_id = ? ORDER BY a.fecha DESC";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, alumnoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Tomamos el estado exactamente como lo guardó el docente (ej: "✅ Asistió")
                    listaAsistencias.add(new FilaAsistencia(
                            rs.getString("fecha"),
                            rs.getString("curso_nombre"),
                            rs.getString("estado")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar asistencias del alumno desde H2: " + e.getMessage());
        }

        // Se eliminó el bloque 'if (listaAsistencias.isEmpty())' que ponía datos falsos.
        // Ahora, si no hay datos en la BD, la tabla se mostrará vacía correctamente.
    }

    public static class FilaAsistencia {
        private final String fecha;
        private final String curso;
        private final String estado;

        public FilaAsistencia(String fecha, String curso, String estado) {
            this.fecha = fecha; this.curso = curso; this.estado = estado;
        }
        public String getFecha() { return fecha; }
        public String getCurso() { return curso; }
        public String getEstado() { return estado; }
    }
}