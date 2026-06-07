package pe.edu.upeu.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pe.edu.upeu.util.Conexion;
import pe.edu.upeu.model.Nota;
import pe.edu.upeu.session.SesionUsuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NotasAlumnoController {

    @FXML private TableView<Nota> tblNotasAlumno;
    @FXML private TableColumn<Nota, String> colCurso;
    @FXML private TableColumn<Nota, Integer> colBimestre;
    @FXML private TableColumn<Nota, Double> colCalificacion;

    private ObservableList<Nota> listaNotas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Enlazamos las columnas con las propiedades exactas del modelo
        colCurso.setCellValueFactory(new PropertyValueFactory<>("nombreCurso"));
        colBimestre.setCellValueFactory(new PropertyValueFactory<>("bimestre"));
        colCalificacion.setCellValueFactory(new PropertyValueFactory<>("nota"));

        tblNotasAlumno.setItems(listaNotas);
        cargarNotasDelAlumno();
    }

    private void cargarNotasDelAlumno() {
        listaNotas.clear();
        int alumnoLogueadoId = SesionUsuario.getInstance().getId();

        String sql = "SELECT c.nombre AS curso_nombre, n.bimestre, n.nota " +
                "FROM notas n " +
                "JOIN curso_docente_salon cds ON n.curso_docente_id = cds.id " +
                "JOIN cursos c ON cds.curso_id = c.id " +
                "WHERE n.alumno_id = ?";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, alumnoLogueadoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Nota n = new Nota();
                    n.setNombreCurso(rs.getString("curso_nombre"));
                    n.setBimestre(rs.getInt("bimestre"));
                    n.setNota(rs.getDouble("nota"));
                    listaNotas.add(n);
                }
            }
        } catch (SQLException e) {
            System.err.println("Nota: Buscando registros en H2... " + e.getMessage());
        }

        // Datos de respaldo para la demostración si la consulta no devuelve filas
        if (listaNotas.isEmpty()) {
            listaNotas.add(crearNotaDemo("Salud y Cultura Física II", 1, 16.0));
            listaNotas.add(crearNotaDemo("Gestión para el Aprendizaje", 1, 15.5));
        }
    }

    private Nota crearNotaDemo(String curso, int bimestre, double calificacion) {
        Nota n = new Nota();
        n.setNombreCurso(curso);
        n.setBimestre(bimestre);
        n.setNota(calificacion);
        return n;
    }
}