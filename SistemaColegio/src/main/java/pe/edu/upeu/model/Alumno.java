package pe.edu.upeu.model;

public class Alumno {
    private int    id;
    private int    usuarioId;
    private String dni;
    private String nombre;
    private String apellido;
    private String fechaNac;
    private String email;
    // Info del salón (join)
    private String salonNombre;

    public Alumno() {}
    public Alumno(String dni, String nombre, String apellido) {
        this.dni = dni; this.nombre = nombre; this.apellido = apellido;
    }

    public int    getId()        { return id; }
    public void   setId(int id)  { this.id = id; }
    public int    getUsuarioId() { return usuarioId; }
    public void   setUsuarioId(int u) { this.usuarioId = u; }
    public String getDni()       { return dni; }
    public void   setDni(String d){ this.dni = d; }
    public String getNombre()    { return nombre; }
    public void   setNombre(String n){ this.nombre = n; }
    public String getApellido()  { return apellido; }
    public void   setApellido(String a){ this.apellido = a; }
    public String getFechaNac()  { return fechaNac; }
    public void   setFechaNac(String f){ this.fechaNac = f; }
    public String getEmail()     { return email; }
    public void   setEmail(String e){ this.email = e; }
    public String getSalonNombre(){ return salonNombre; }
    public void   setSalonNombre(String s){ this.salonNombre = s; }
    public String getNombreCompleto(){ return nombre + " " + apellido; }
}
