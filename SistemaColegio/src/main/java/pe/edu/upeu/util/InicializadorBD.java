package pe.edu.upeu.util;

import java.sql.*;

/**
 * Crea todas las tablas de la BD si no existen, e inserta datos iniciales.
 */
public class InicializadorBD {

    public static void inicializar() {
        Connection cn = Conexion.obtenerConexion();
        if (cn == null) return;
        try (Statement st = cn.createStatement()) {

            // ROLES
            st.execute("""
                CREATE TABLE IF NOT EXISTS roles (
                    id   INTEGER PRIMARY KEY AUTO_INCREMENT,
                    nombre TEXT NOT NULL UNIQUE
                )""");

            // USUARIOS (tabla central de autenticación)
            st.execute("""
                CREATE TABLE IF NOT EXISTS usuarios (
                    id         INTEGER PRIMARY KEY AUTO_INCREMENT,
                    username   TEXT    NOT NULL UNIQUE,
                    password   TEXT    NOT NULL,
                    rol_id     INTEGER NOT NULL,
                    activo     INTEGER NOT NULL DEFAULT 1,
                    FOREIGN KEY (rol_id) REFERENCES roles(id)
                )""");

            // ADMINISTRADORES
            st.execute("""
                CREATE TABLE IF NOT EXISTS administradores (
                    id          INTEGER PRIMARY KEY AUTO_INCREMENT,
                    usuario_id  INTEGER NOT NULL UNIQUE,
                    nombre      TEXT    NOT NULL,
                    apellido    TEXT    NOT NULL,
                    email       TEXT,
                    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
                )""");

            // DOCENTES
            st.execute("""
                CREATE TABLE IF NOT EXISTS docentes (
                    id          INTEGER PRIMARY KEY AUTO_INCREMENT,
                    usuario_id  INTEGER NOT NULL UNIQUE,
                    dni         TEXT    NOT NULL UNIQUE,
                    nombre      TEXT    NOT NULL,
                    apellido    TEXT    NOT NULL,
                    email       TEXT,
                    especialidad TEXT,
                    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
                )""");

            // SALONES
            st.execute("""
                CREATE TABLE IF NOT EXISTS salones (
                    id      INTEGER PRIMARY KEY AUTO_INCREMENT,
                    nombre  TEXT    NOT NULL UNIQUE,
                    grado   TEXT    NOT NULL,
                    seccion TEXT    NOT NULL,
                    nivel   TEXT    NOT NULL DEFAULT 'Secundaria',
                    año_academico INTEGER NOT NULL DEFAULT 2025
                )""");

            // ALUMNOS
            st.execute("""
                CREATE TABLE IF NOT EXISTS alumnos (
                    id          INTEGER PRIMARY KEY AUTO_INCREMENT,
                    usuario_id  INTEGER NOT NULL UNIQUE,
                    dni         TEXT    NOT NULL UNIQUE,
                    nombre      TEXT    NOT NULL,
                    apellido    TEXT    NOT NULL,
                    fecha_nac   TEXT,
                    email       TEXT,
                    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
                )""");

            // ALUMNO_SALON (asignación de alumnos a salones)
            st.execute("""
                CREATE TABLE IF NOT EXISTS alumno_salon (
                    alumno_id  INTEGER NOT NULL,
                    salon_id   INTEGER NOT NULL,
                    año_academico INTEGER NOT NULL DEFAULT 2025,
                    PRIMARY KEY (alumno_id, año_academico),
                    FOREIGN KEY (alumno_id) REFERENCES alumnos(id),
                    FOREIGN KEY (salon_id)  REFERENCES salones(id)
                )""");

            // CURSOS
            st.execute("""
                CREATE TABLE IF NOT EXISTS cursos (
                    id      INTEGER PRIMARY KEY AUTO_INCREMENT,
                    nombre  TEXT NOT NULL,
                    codigo  TEXT NOT NULL UNIQUE,
                    descripcion TEXT
                )""");

            // CURSO_DOCENTE_SALON (un docente enseña un curso en un salón)
            st.execute("""
                CREATE TABLE IF NOT EXISTS curso_docente_salon (
                    id         INTEGER PRIMARY KEY AUTO_INCREMENT,
                    curso_id   INTEGER NOT NULL,
                    docente_id INTEGER NOT NULL,
                    salon_id   INTEGER NOT NULL,
                    año_academico INTEGER NOT NULL DEFAULT 2025,
                    FOREIGN KEY (curso_id)   REFERENCES cursos(id),
                    FOREIGN KEY (docente_id) REFERENCES docentes(id),
                    FOREIGN KEY (salon_id)   REFERENCES salones(id)
                )""");

            // HORARIOS
            st.execute("""
                CREATE TABLE IF NOT EXISTS horarios (
                    id               INTEGER PRIMARY KEY AUTO_INCREMENT,
                    salon_id         INTEGER NOT NULL,
                    curso_docente_id INTEGER NOT NULL,
                    dia_semana       TEXT    NOT NULL,
                    hora_inicio      TEXT    NOT NULL,
                    hora_fin         TEXT    NOT NULL,
                    FOREIGN KEY (salon_id)         REFERENCES salones(id),
                    FOREIGN KEY (curso_docente_id) REFERENCES curso_docente_salon(id)
                )""");

            // ASISTENCIAS
            st.execute("""
                CREATE TABLE IF NOT EXISTS asistencias (
                    id         INTEGER PRIMARY KEY AUTO_INCREMENT,
                    alumno_id  INTEGER NOT NULL,
                    curso_docente_id INTEGER NOT NULL,
                    fecha      TEXT    NOT NULL,
                    estado     TEXT    NOT NULL DEFAULT 'A',
                    FOREIGN KEY (alumno_id)        REFERENCES alumnos(id),
                    FOREIGN KEY (curso_docente_id) REFERENCES curso_docente_salon(id)
                )""");

            // NOTAS
            st.execute("""
                CREATE TABLE IF NOT EXISTS notas (
                    id         INTEGER PRIMARY KEY AUTO_INCREMENT,
                    alumno_id  INTEGER NOT NULL,
                    curso_docente_id INTEGER NOT NULL,
                    bimestre   INTEGER NOT NULL DEFAULT 1,
                    nota       REAL    NOT NULL,
                    FOREIGN KEY (alumno_id)        REFERENCES alumnos(id),
                    FOREIGN KEY (curso_docente_id) REFERENCES curso_docente_salon(id)
                )""");

            // JUSTIFICACIONES
            st.execute("""
                CREATE TABLE IF NOT EXISTS justificaciones (
                    id            INTEGER PRIMARY KEY AUTO_INCREMENT,
                    alumno_id     INTEGER NOT NULL,
                    asistencia_id INTEGER NOT NULL,
                    motivo        TEXT    NOT NULL,
                    estado        TEXT    NOT NULL DEFAULT 'PENDIENTE',
                    fecha_envio   TEXT    NOT NULL,
                    FOREIGN KEY (alumno_id)     REFERENCES alumnos(id),
                    FOREIGN KEY (asistencia_id) REFERENCES asistencias(id)
                )""");

            // CONFIGURACION
            st.execute("""
                CREATE TABLE IF NOT EXISTS configuracion (
                    clave TEXT PRIMARY KEY,
                    valor TEXT NOT NULL
                )""");

            // ACTIVIDAD (log de acciones)
            st.execute("""
                CREATE TABLE IF NOT EXISTS actividad (
                    id         INTEGER PRIMARY KEY AUTO_INCREMENT,
                    usuario_id INTEGER NOT NULL,
                    accion     TEXT    NOT NULL,
                    detalle    TEXT,
                    fecha      TEXT    NOT NULL,
                    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
                )""");

            // ---- Datos iniciales ----
            insertarDatosIniciales(cn);

            System.out.println("Base de datos inicializada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al inicializar BD: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void insertarDatosIniciales(Connection cn) throws SQLException {
        // Roles
        ejecutarSiNoExiste(cn,
            "SELECT id FROM roles WHERE nombre='ADMINISTRADOR'",
            "INSERT INTO roles(nombre) VALUES('ADMINISTRADOR')");
        ejecutarSiNoExiste(cn,
            "SELECT id FROM roles WHERE nombre='DOCENTE'",
            "INSERT INTO roles(nombre) VALUES('DOCENTE')");
        ejecutarSiNoExiste(cn,
            "SELECT id FROM roles WHERE nombre='ALUMNO'",
            "INSERT INTO roles(nombre) VALUES('ALUMNO')");

        // Usuario administrador por defecto (password: admin123 en BCrypt dinámico)
        String passwordEncriptada = org.mindrot.jbcrypt.BCrypt.hashpw("admin123", org.mindrot.jbcrypt.BCrypt.gensalt(10));

        ejecutarSiNoExiste(cn,
                "SELECT id FROM USUARIOS WHERE username='admin'",
                "INSERT INTO USUARIOS(username, password, rol_id) VALUES('admin', '" + passwordEncriptada + "', 1)");

        ejecutarSiNoExiste(cn,
                "SELECT id FROM ADMINISTRADORES WHERE usuario_id=(SELECT id FROM USUARIOS WHERE username='admin')",
                "INSERT INTO ADMINISTRADORES(usuario_id, nombre, apellido, email) " +
                        "VALUES((SELECT id FROM USUARIOS WHERE username='admin'), 'Administrador', 'Principal', 'admin@colegio.edu.pe')");
        // Configuración por defecto
        ejecutarSiNoExiste(cn,
            "SELECT clave FROM configuracion WHERE clave='nombre_colegio'",
            "INSERT INTO configuracion(clave, valor) VALUES('nombre_colegio','Colegio UPEU')");
        ejecutarSiNoExiste(cn,
            "SELECT clave FROM configuracion WHERE clave='año_academico'",
            "INSERT INTO configuracion(clave, valor) VALUES('año_academico','2025')");
        ejecutarSiNoExiste(cn,
            "SELECT clave FROM configuracion WHERE clave='tema'",
            "INSERT INTO configuracion(clave, valor) VALUES('tema','CLARO')");
    }

    private static void ejecutarSiNoExiste(Connection cn, String check, String insert)
            throws SQLException {
        try (ResultSet rs = cn.createStatement().executeQuery(check)) {
            if (!rs.next()) {
                cn.createStatement().executeUpdate(insert);
            }
        }
    }
}
