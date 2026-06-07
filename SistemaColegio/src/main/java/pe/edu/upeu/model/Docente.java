package pe.edu.upeu.model;

public class Docente {
    private int    id;
    private int    usuarioId;
    private String dni;
    private String nombre;
    private String apellido;
    private String email;
    private String especialidad;

    public Docente() {}
    public int    getId()          { return id; }
    public void   setId(int id)    { this.id = id; }
    public int    getUsuarioId()   { return usuarioId; }
    public void   setUsuarioId(int u){ this.usuarioId = u; }
    public String getDni()         { return dni; }
    public void   setDni(String d) { this.dni = d; }
    public String getNombre()      { return nombre; }
    public void   setNombre(String n){ this.nombre = n; }
    public String getApellido()    { return apellido; }
    public void   setApellido(String a){ this.apellido = a; }
    public String getEmail()       { return email; }
    public void   setEmail(String e){ this.email = e; }
    public String getEspecialidad() { return especialidad; }
    public void   setEspecialidad(String e){ this.especialidad = e; }
    public String getNombreCompleto(){ return nombre + " " + apellido; }
}
