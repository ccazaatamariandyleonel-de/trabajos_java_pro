package pe.edu.upeu.dao;

import pe.edu.upeu.model.Alumno;
import pe.edu.upeu.util.Conexion;

import java.sql.*;
import java.util.*;

public class AlumnoDAO {

    public List<Alumno> listarTodos() {
        List<Alumno> lista = new ArrayList<>();
        String sql = """
            SELECT a.*, s.nombre as salon_nombre
            FROM alumnos a
            LEFT JOIN alumno_salon als ON a.id = als.alumno_id
            LEFT JOIN salones s ON als.salon_id = s.id
            ORDER BY a.apellido, a.nombre
            """;
        try (ResultSet rs = Conexion.obtenerConexion().createStatement().executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error listarAlumnos: " + e.getMessage());
        }
        return lista;
    }

    public List<Alumno> listarPorSalon(int salonId) {
        List<Alumno> lista = new ArrayList<>();
        String sql = """
            SELECT a.*, s.nombre as salon_nombre
            FROM alumnos a
            JOIN alumno_salon als ON a.id = als.alumno_id
            JOIN salones s ON als.salon_id = s.id
            WHERE s.id = ?
            ORDER BY a.apellido, a.nombre
            """;
        try (PreparedStatement ps = Conexion.obtenerConexion().prepareStatement(sql)) {
            ps.setInt(1, salonId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error listarPorSalon: " + e.getMessage());
        }
        return lista;
    }

    public List<Alumno> buscar(String termino) {
        List<Alumno> lista = new ArrayList<>();
        String sql = """
            SELECT a.*, s.nombre as salon_nombre
            FROM alumnos a
            LEFT JOIN alumno_salon als ON a.id = als.alumno_id
            LEFT JOIN salones s ON als.salon_id = s.id
            WHERE a.nombre LIKE ? OR a.apellido LIKE ? OR a.dni LIKE ?
            ORDER BY a.apellido, a.nombre
            """;
        try (PreparedStatement ps = Conexion.obtenerConexion().prepareStatement(sql)) {
            String t = "%" + termino + "%";
            ps.setString(1, t); ps.setString(2, t); ps.setString(3, t);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error buscarAlumno: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertar(Alumno a) {
        String sql = "INSERT INTO alumnos(usuario_id,dni,nombre,apellido,fecha_nac,email) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement ps = Conexion.obtenerConexion().prepareStatement(sql)) {
            ps.setInt(1, a.getUsuarioId());
            ps.setString(2, a.getDni());
            ps.setString(3, a.getNombre());
            ps.setString(4, a.getApellido());
            ps.setString(5, a.getFechaNac());
            ps.setString(6, a.getEmail());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error insertarAlumno: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Alumno a) {
        String sql = "UPDATE alumnos SET dni=?,nombre=?,apellido=?,fecha_nac=?,email=? WHERE id=?";
        try (PreparedStatement ps = Conexion.obtenerConexion().prepareStatement(sql)) {
            ps.setString(1, a.getDni());
            ps.setString(2, a.getNombre());
            ps.setString(3, a.getApellido());
            ps.setString(4, a.getFechaNac());
            ps.setString(5, a.getEmail());
            ps.setInt(6, a.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error actualizarAlumno: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        try (PreparedStatement ps = Conexion.obtenerConexion()
                .prepareStatement("DELETE FROM alumnos WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error eliminarAlumno: " + e.getMessage());
            return false;
        }
    }

    public Alumno buscarPorUsuarioId(int usuarioId) {
        String sql = "SELECT a.*, '' as salon_nombre FROM alumnos a WHERE a.usuario_id=?";
        try (PreparedStatement ps = Conexion.obtenerConexion().prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("Error buscarPorUsuario: " + e.getMessage());
        }
        return null;
    }

    public int contarTotal() {
        try (ResultSet rs = Conexion.obtenerConexion().createStatement()
                .executeQuery("SELECT COUNT(*) FROM alumnos")) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }

    private Alumno mapear(ResultSet rs) throws SQLException {
        Alumno a = new Alumno();
        a.setId(rs.getInt("id"));
        a.setUsuarioId(rs.getInt("usuario_id"));
        a.setDni(rs.getString("dni"));
        a.setNombre(rs.getString("nombre"));
        a.setApellido(rs.getString("apellido"));
        a.setFechaNac(rs.getString("fecha_nac"));
        a.setEmail(rs.getString("email"));
        a.setSalonNombre(rs.getString("salon_nombre"));
        return a;
    }
}
