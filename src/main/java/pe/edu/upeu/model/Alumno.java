package pe.edu.upeu.model;

public class Alumno {
    private int id_usuario;      // 👈 Necesario para identificar cuál fila borrar o actualizar
    private String dni;
    private String nombre;
    private String clave;       // 👈 Necesario para la contraseña del alumno
    private int id_salon;       // 👈 El número ID del salón en SQLite
    private String gradoSeccion; // 👈 El texto que se verá en tu tabla ("1ro - A")

    // Constructor vacío
    public Alumno() {
    }

    // Constructor completo
    public Alumno(int id_usuario, String dni, String nombre, String clave, int id_salon, String gradoSeccion) {
        this.id_usuario = id_usuario;
        this.dni = dni;
        this.nombre = nombre;
        this.clave = clave;
        this.id_salon = id_salon;
        this.gradoSeccion = gradoSeccion;
    }

    // ====================================================================
    // ⚙️ GETTERS Y SETTERS (Todos los que usará el Controlador)
    // ====================================================================
    public int getId_usuario() { return id_usuario; }
    public void setId_usuario(int id_usuario) { this.id_usuario = id_usuario; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public int getId_salon() { return id_salon; }
    public void setId_salon(int id_salon) { this.id_salon = id_salon; }

    public String getGradoSeccion() { return gradoSeccion; }
    public void setGradoSeccion(String gradoSeccion) { this.gradoSeccion = gradoSeccion; }
}