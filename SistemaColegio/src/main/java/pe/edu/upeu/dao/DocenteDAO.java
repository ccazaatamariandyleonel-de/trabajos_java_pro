package pe.edu.upeu.dao;

import pe.edu.upeu.model.Docente;
import pe.edu.upeu.util.Conexion;

import java.sql.*;
import java.util.*;

public class DocenteDAO {

    public List<Docente> listar() {
        List<Docente> lista = new ArrayList<>();
        String sql = "SELECT * FROM DOCENTES ORDER BY apellido, nombre";
        try (ResultSet rs = Conexion.obtenerConexion().createStatement().executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listarDocentes: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public boolean guardar(Docente d) {
        String sql = "INSERT INTO DOCENTES(usuario_id, dni, nombre, apellido, email, especialidad) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement ps = Conexion.obtenerConexion().prepareStatement(sql)) {
            ps.setInt(1, d.getUsuarioId());
            ps.setString(2, d.getDni());
            ps.setString(3, d.getNombre());
            ps.setString(4, d.getApellido());
            ps.setString(5, d.getEmail());
            ps.setString(6, d.getEspecialidad());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error insertarDocente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean modificar(Docente d) {
        String sql = "UPDATE DOCENTES SET dni=?, nombre=?, apellido=?, email=?, especialidad=? WHERE id=?";
        try (PreparedStatement ps = Conexion.obtenerConexion().prepareStatement(sql)) {
            ps.setString(1, d.getDni());
            ps.setString(2, d.getNombre());
            ps.setString(3, d.getApellido());
            ps.setString(4, d.getEmail());
            ps.setString(5, d.getEspecialidad());
            ps.setInt(6, d.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error actualizarDocente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM DOCENTES WHERE id=?";
        try (PreparedStatement ps = Conexion.obtenerConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error eliminarDocente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Docente buscarPorUsuarioId(int usuarioId) {
        String sql = "SELECT * FROM DOCENTES WHERE usuario_id=?";
        try (PreparedStatement ps = Conexion.obtenerConexion().prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error buscarDocenteUsuario: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM DOCENTES";
        try (ResultSet rs = Conexion.obtenerConexion().createStatement().executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }

    // CORRECCIÓN: Método mapear completamente limpio sin teléfonos
    private Docente mapear(ResultSet rs) throws SQLException {
        Docente d = new Docente();
        d.setId(rs.getInt("id"));
        d.setUsuarioId(rs.getInt("usuario_id"));
        d.setDni(rs.getString("dni"));
        d.setNombre(rs.getString("nombre"));
        d.setApellido(rs.getString("apellido"));
        d.setEmail(rs.getString("email"));
        d.setEspecialidad(rs.getString("especialidad"));
        return d;
    }
}