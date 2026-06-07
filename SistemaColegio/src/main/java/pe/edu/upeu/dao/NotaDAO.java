package pe.edu.upeu.dao;

import pe.edu.upeu.util.Conexion;
import pe.edu.upeu.model.Nota;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NotaDAO {

    private Connection con;
    private PreparedStatement ps;

    public int guardar(Nota n) {
        String sql = "INSERT INTO notas (alumno_id, curso_docente_id, bimestre, nota) VALUES (?, ?, ?, ?)";
        try {
            con = Conexion.obtenerConexion();
            ps = con.prepareStatement(sql);

            ps.setInt(1, n.getAlumnoId());
            ps.setInt(2, n.getCursoDocenteId());
            ps.setInt(3, n.getBimestre());
            ps.setDouble(4, n.getNota());

            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error SQL al guardar nota: " + e.getMessage());
            return 0;
        }
    }
}