package pe.edu.upeu.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    // 🎯 RUTA ABSOLUTA CONFIGURADA CORRECTAMENTE
    private static final String URL = "jdbc:sqlite:C:\\POO-G2-2026-1P\\sistema_colegio.db";
    private static Connection conexion = null;

    public static Connection obtenerConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                // 🎯 CORREGIDO: Este es el nombre real de la clase que despierta a SQLite
                Class.forName("org.sqlite.JDBC");

                conexion = DriverManager.getConnection(URL);
                System.out.println("🚀 Conectado con éxito a la base de datos real en: C:\\POO-G2-2026-1P\\sistema_colegio.db");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Error: No se encontró la clase del driver org.sqlite.JDBC.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar a la ruta: " + URL);
            e.printStackTrace();
        }
        return conexion;
    }

    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("🔒 Conexión con SQLite cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}