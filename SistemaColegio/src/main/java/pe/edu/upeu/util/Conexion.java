package pe.edu.upeu.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.h2.tools.Server; // <-- IMPORTANTE: No olvides esta importación

public class Conexion {

    private static final String DB_PATH = "./data/colegio_db";
    private static final String URL = "jdbc:h2:" + DB_PATH;

    private static Connection conexion = null;
    private static boolean servidorIniciado = false; // Bandera para que inicie solo una vez

    public static Connection obtenerConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                new File("./data").mkdirs();

                // --- ARRANCAR H2 CONSOLE WEB (SOLO UNA VEZ) ---
                if (!servidorIniciado) {
                    try {
                        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
                        System.out.println("--> ¡H2 Console iniciada con éxito!");
                        System.out.println("--> Entra en tu navegador a: http://localhost:8082");
                        servidorIniciado = true;
                    } catch (SQLException e) {
                        System.err.println("Advertencia H2 Console: " + e.getMessage());
                        // Si ya estaba iniciado por otra instancia, no pasa nada
                        servidorIniciado = true;
                    }
                }
                // ----------------------------------------------

                Class.forName("org.h2.Driver");
                conexion = DriverManager.getConnection(URL, "sa", "");
                System.out.println("Conexion H2 OK en: " + DB_PATH);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error conexion H2: " + e.getMessage());
        }
        return conexion;
    }

    public static void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                conexion = null;
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar: " + e.getMessage());
        }
    }

    public static String getRutaBD() { return DB_PATH; }
}