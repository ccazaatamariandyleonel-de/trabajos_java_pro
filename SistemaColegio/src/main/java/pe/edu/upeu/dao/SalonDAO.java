package pe.edu.upeu.dao;

import pe.edu.upeu.model.Salon;
import pe.edu.upeu.util.Conexion;

import java.sql.*;
import java.util.*;

public class SalonDAO {

    // Cambiado de listarTodos() a listar()
    public List<Salon> listar() {
        List<Salon> lista = new ArrayList<>();
        // CORRECCIÓN: Tabla SALONES en mayúsculas
        String sql = "SELECT * FROM SALONES ORDER BY grado, seccion";
        try (ResultSet rs = Conexion.obtenerConexion().createStatement().executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listarSalones: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // Cambiado de insertar() a guardar()
    public boolean guardar(Salon s) {
        // CORRECCIÓN: Uso de mayúsculas y mapeo seguro de ANO_ACADEMICO
        String sql = "INSERT INTO SALONES(nombre, grado, seccion, nivel, ano_academico) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = Conexion.obtenerConexion().prepareStatement(sql)) {
            ps.setString(1, s.getNombre());
            ps.setString(2, s.getGrado());
            ps.setString(3, s.getSeccion());
            ps.setString(4, s.getNivel());
            ps.setInt(5, s.getAñoAcademico());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error insertarSalon: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Cambiado de actualizar() a modificar()
    public boolean modificar(Salon s) {
        // CORRECCIÓN: Se añadió 'ano_academico=?' que faltaba en el UPDATE original
        String sql = "UPDATE SALONES SET nombre=?, grado=?, seccion=?, nivel=?, ano_academico=? WHERE id=?";
        try (PreparedStatement ps = Conexion.obtenerConexion().prepareStatement(sql)) {
            ps.setString(1, s.getNombre());
            ps.setString(2, s.getGrado());
            ps.setString(3, s.getSeccion());
            ps.setString(4, s.getNivel());
            ps.setInt(5, s.getAñoAcademico());
            ps.setInt(6, s.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error actualizarSalon: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM SALONES WHERE id=?";
        try (PreparedStatement ps = Conexion.obtenerConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error eliminarSalon: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM SALONES";
        try (ResultSet rs = Conexion.obtenerConexion().createStatement().executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }

    private Salon mapear(ResultSet rs) throws SQLException {
        Salon s = new Salon();
        s.setId(rs.getInt("id"));
        s.setNombre(rs.getString("nombre"));
        s.setGrado(rs.getString("grado"));
        s.setSeccion(rs.getString("seccion"));
        s.setNivel(rs.getString("nivel"));

        // Intento seguro de leer con o sin eñe según cómo esté en la BD real
        try {
            s.setAñoAcademico(rs.getInt("ano_academico"));
        } catch (SQLException e) {
            s.setAñoAcademico(rs.getInt("año_academico"));
        }

        return s;
    }
}