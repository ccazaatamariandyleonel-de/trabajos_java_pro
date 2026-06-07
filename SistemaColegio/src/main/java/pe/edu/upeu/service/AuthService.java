package pe.edu.upeu.service;

import org.mindrot.jbcrypt.BCrypt;
import pe.edu.upeu.dao.AlumnoDAO;
import pe.edu.upeu.dao.DocenteDAO;
import pe.edu.upeu.dao.UsuarioDAO;
import pe.edu.upeu.model.Alumno;
import pe.edu.upeu.model.Docente;
import pe.edu.upeu.model.Usuario;
import pe.edu.upeu.session.SesionUsuario;
import pe.edu.upeu.util.Conexion;

import java.sql.*;

public class AuthService {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final AlumnoDAO  alumnoDAO  = new AlumnoDAO();
    private final DocenteDAO docenteDAO = new DocenteDAO();

    public boolean autenticar(String username, String password) {
        Usuario usuario = usuarioDAO.buscarPorUsername(username);
        if (usuario == null) {
            System.out.println("--> [AuthService] El usuario '" + username + "' no existe.");
            return false;
        }

        if (!BCrypt.checkpw(password, usuario.getPassword())) {
            System.out.println("--> [AuthService] Clave incorrecta para: " + username);
            return false;
        }

        String rol = usuarioDAO.obtenerRol(usuario.getRolId());
        String nombreCompleto = obtenerNombreCompleto(usuario, rol);
        int entidadId = obtenerEntidadId(usuario, rol);

        SesionUsuario.getInstance().iniciarSesion(
                usuario.getId(), usuario.getUsername(), nombreCompleto, rol, entidadId
        );
        return true;
    }

    private String obtenerNombreCompleto(Usuario u, String rol) {
        try {
            Connection cn = Conexion.obtenerConexion();
            // CORRECCIÓN: Nombres de tablas en MAYÚSCULAS para H2
            String tabla = switch (rol) {
                case "ADMINISTRADOR" -> "ADMINISTRADORES";
                case "DOCENTE"       -> "DOCENTES";
                case "ALUMNO"        -> "ALUMNOS";
                default -> null;
            };
            if (tabla == null) return u.getUsername();
            PreparedStatement ps = cn.prepareStatement(
                    "SELECT nombre, apellido FROM " + tabla + " WHERE usuario_id=?");
            ps.setInt(1, u.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("nombre") + " " + rs.getString("apellido");
        } catch (SQLException e) {
            System.err.println("Error obtenerNombre: " + e.getMessage());
            e.printStackTrace(); // Esto te dirá exactamente qué falló en la consola
        }
        return u.getUsername();
    }

    private int obtenerEntidadId(Usuario u, String rol) {
        try {
            Connection cn = Conexion.obtenerConexion();
            // CORRECCIÓN: Nombres de tablas en MAYÚSCULAS para H2
            String tabla = switch (rol) {
                case "ADMINISTRADOR" -> "ADMINISTRADORES";
                case "DOCENTE"       -> "DOCENTES";
                case "ALUMNO"        -> "ALUMNOS";
                default -> null;
            };
            if (tabla == null) return 0;
            PreparedStatement ps = cn.prepareStatement(
                    "SELECT id FROM " + tabla + " WHERE usuario_id=?");
            ps.setInt(1, u.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            System.err.println("Error obtenerEntidadId: " + e.getMessage());
            e.printStackTrace(); // Esto te dirá exactamente qué falló en la consola
        }
        return 0;
    }

    public static String hashPassword(String plain) {
        return BCrypt.hashpw(plain, BCrypt.gensalt(10));
    }
}