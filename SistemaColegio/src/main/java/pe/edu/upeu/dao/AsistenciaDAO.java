package pe.edu.upeu.dao;

import pe.edu.upeu.util.Conexion;
import pe.edu.upeu.model.Asistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AsistenciaDAO {

    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;

    // Método para guardar cada asistencia en la tabla de H2
    public int guardar(Asistencia a) {
        String sql = "INSERT INTO asistencias (alumno_id, curso_docente_id, fecha, estado) VALUES (?, ?, ?, ?)";
        try {
            // Usamos el método exacto de tu clase util.Conexion
            con = Conexion.obtenerConexion();
            ps = con.prepareStatement(sql);

            ps.setInt(1, a.getAlumnoId());
            ps.setInt(2, a.getCursoDocenteId());
            ps.setString(3, a.getFecha());
            ps.setString(4, a.getEstado());

            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error SQL al guardar asistencia: " + e.getMessage());
            return 0;
        }
    }

    // Método opcional por si luego necesitas consultar las asistencias ya guardadas
    public List<Asistencia> listarPorCursoYFecha(int idCursoDocente, String fecha) {
        List<Asistencia> lista = new ArrayList<>();
        String sql = "SELECT asis.*, alu.nombreAlumno FROM asistencias asis " +
                "JOIN alumnos alu ON asis.alumno_id = alu.id " +
                "WHERE asis.curso_docente_id = ? AND asis.fecha = ?";
        try {
            con = Conexion.obtenerConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idCursoDocente);
            ps.setString(2, fecha);
            rs = ps.executeQuery();

            while (rs.next()) {
                Asistencia a = new Asistencia();
                a.setId(rs.getInt("id"));
                a.setAlumnoId(rs.getInt("alumno_id"));
                a.setCursoDocenteId(rs.getInt("curso_docente_id"));
                a.setFecha(rs.getString("fecha"));
                a.setEstado(rs.getString("estado"));
                a.setNombreAlumno(rs.getString("nombreAlumno"));
                lista.add(a);
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al listar asistencias: " + e.getMessage());
        }
        return lista;
    }
}