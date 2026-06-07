package pe.edu.upeu.dao;

import pe.edu.upeu.model.Usuario;
import pe.edu.upeu.util.Conexion;

import java.sql.*;

public class UsuarioDAO {

    public Usuario buscarPorUsername(String username) {
        // CORRECCIÓN: Tablas y alias en MAYÚSCULAS para total compatibilidad con H2
        String sql = "SELECT U.id, U.username, U.password, U.rol_id, U.activo, R.nombre as rol " +
                "FROM USUARIOS U " +
                "JOIN ROLES R ON U.rol_id = R.id " +
                "WHERE U.username = ? AND U.activo = 1";

        try (PreparedStatement ps = Conexion.obtenerConexion().prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setUsername(rs.getString("username"));
                    u.setPassword(rs.getString("password"));
                    u.setRolId(rs.getInt("rol_id"));
                    u.setActivo(rs.getInt("activo") == 1);
                    return u;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error UsuarioDAO buscarPorUsername: " + e.getMessage());
            e.printStackTrace(); // Nos muestra el rastro exacto si falla
        }
        return null;
    }

    public String obtenerRol(int rolId) {
        // CORRECCIÓN: Tabla ROLES en mayúsculas
        String sql = "SELECT nombre FROM ROLES WHERE id=?";
        try (PreparedStatement ps = Conexion.obtenerConexion().prepareStatement(sql)) {
            ps.setInt(1, rolId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nombre");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obtenerRol: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void cambiarPassword(int usuarioId, String nuevaPasswordHash) {
        // CORRECCIÓN: Tabla USUARIOS en mayúsculas
        String sql = "UPDATE USUARIOS SET password=? WHERE id=?";
        try (PreparedStatement ps = Conexion.obtenerConexion().prepareStatement(sql)) {
            ps.setString(1, nuevaPasswordHash);
            ps.setInt(2, usuarioId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error cambiarPassword: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Crea usuario + retorna su id */
    public int crearUsuario(String username, String passwordHash, int rolId) {
        // CORRECCIÓN: Tabla USUARIOS en mayúsculas
        String sql = "INSERT INTO USUARIOS(username, password, rol_id) VALUES(?,?,?)";
        try (PreparedStatement ps = Conexion.obtenerConexion()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ps.setInt(3, rolId);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error crearUsuario: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }
}