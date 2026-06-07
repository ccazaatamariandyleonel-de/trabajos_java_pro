package pe.edu.upeu.session;

/**
 * Singleton que guarda el usuario autenticado durante toda la sesión.
 * Patrón: Session Manager.
 */
public class SesionUsuario {

    private static SesionUsuario instancia;

    private int    id;
    private String username;
    private String nombreCompleto;
    private String rol;       // ADMINISTRADOR, DOCENTE, ALUMNO
    private int    entidadId; // id en tabla administradores/docentes/alumnos

    private SesionUsuario() {}

    public static SesionUsuario getInstance() {
        if (instancia == null) instancia = new SesionUsuario();
        return instancia;
    }

    public void iniciarSesion(int id, String username, String nombreCompleto,
                              String rol, int entidadId) {
        this.id            = id;
        this.username      = username;
        this.nombreCompleto = nombreCompleto;
        this.rol           = rol;
        this.entidadId     = entidadId;
    }

    public void cerrarSesion() {
        id = 0; username = null; nombreCompleto = null; rol = null; entidadId = 0;
    }

    public boolean isLogueado()         { return username != null; }
    public boolean esAdmin()            { return "ADMINISTRADOR".equals(rol); }
    public boolean esDocente()          { return "DOCENTE".equals(rol); }
    public boolean esAlumno()           { return "ALUMNO".equals(rol); }

    public int    getId()               { return id; }
    public String getUsername()         { return username; }
    public String getNombreCompleto()   { return nombreCompleto; }
    public String getRol()              { return rol; }
    public int    getEntidadId()        { return entidadId; }
}
