package pe.edu.upeu.dao;

import pe.edu.upeu.model.Curso;
import pe.edu.upeu.util.Conexion;

import java.sql.*;
import java.util.*;

public class CursoDAO {

    public List<Curso> listar() {
        List<Curso> lista = new ArrayList<>();
        String sql = "SELECT * FROM CURSOS ORDER BY nombre";
        try (ResultSet rs = Conexion.obtenerConexion().createStatement().executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listarCursos: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public boolean guardar(Curso c) {
        String sql = "INSERT INTO CURSOS(codigo, nombre, descripcion) VALUES(?,?,?)";
        try (PreparedStatement ps = Conexion.obtenerConexion().prepareStatement(sql)) {
            ps.setString(1, c.getCodigo());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getDescripcion());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error insertarCurso: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean modificar(Curso c) {
        String sql = "UPDATE CURSOS SET codigo=?, nombre=?, descripcion=? WHERE id=?";
        try (PreparedStatement ps = Conexion.obtenerConexion().prepareStatement(sql)) {
            ps.setString(1, c.getCodigo());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getDescripcion());
            ps.setInt(4, c.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error actualizarCurso: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM CURSOS WHERE id=?";
        try (PreparedStatement ps = Conexion.obtenerConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error eliminarCurso: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM CURSOS";
        try (ResultSet rs = Conexion.obtenerConexion().createStatement().executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error contarCursos: " + e.getMessage());
        }
        return 0;
    }
    private Curso mapear(ResultSet rs) throws SQLException {
        Curso c = new Curso();
        c.setId(rs.getInt("id"));
        c.setCodigo(rs.getString("codigo"));
        c.setNombre(rs.getString("nombre"));
        c.setDescripcion(rs.getString("descripcion"));
        return c;
    }
}