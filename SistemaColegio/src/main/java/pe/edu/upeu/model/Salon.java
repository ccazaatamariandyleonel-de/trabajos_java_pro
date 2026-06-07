package pe.edu.upeu.model;

public class Salon {
    private int    id;
    private String nombre;
    private String grado;
    private String seccion;
    private String nivel;
    private int    añoAcademico;

    public Salon() {}
    public int    getId()          { return id; }
    public void   setId(int id)    { this.id = id; }
    public String getNombre()      { return nombre; }
    public void   setNombre(String n){ this.nombre = n; }
    public String getGrado()       { return grado; }
    public void   setGrado(String g){ this.grado = g; }
    public String getSeccion()     { return seccion; }
    public void   setSeccion(String s){ this.seccion = s; }
    public String getNivel()       { return nivel; }
    public void   setNivel(String n){ this.nivel = n; }
    public int    getAñoAcademico(){ return añoAcademico; }
    public void   setAñoAcademico(int a){ this.añoAcademico = a; }
}
