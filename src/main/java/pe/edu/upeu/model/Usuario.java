package pe.edu.upeu.model;

public class Usuario {
    private int idUsuario;
    private String dni;
    private String nombre;
    private String clave;
    private String rol;

    // Constructor vacío (Obligatorio)
    public Usuario() {}

    // Constructor completo
    public Usuario(int idUsuario, String dni, String nombre, String clave, String rol) {
        this.idUsuario = idUsuario;
        this.dni = dni;
        this.nombre = nombre;
        this.clave = clave;
        this.rol = rol;
    }

    // GETTERS Y SETTERS (De aquí sale el getNombre())
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}