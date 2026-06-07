package pe.edu.upeu.model;

public class Curso {
    private int    id;
    private String nombre;
    private String codigo;
    private String descripcion;

    public Curso() {}
    public int    getId()          { return id; }
    public void   setId(int id)    { this.id = id; }
    public String getNombre()      { return nombre; }
    public void   setNombre(String n){ this.nombre = n; }
    public String getCodigo()      { return codigo; }
    public void   setCodigo(String c){ this.codigo = c; }
    public String getDescripcion() { return descripcion; }
    public void   setDescripcion(String d){ this.descripcion = d; }
    public String toString()       { return nombre; }
}
