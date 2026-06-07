package pe.edu.upeu.model;

public class Asistencia {
    private int    id;
    private int    alumnoId;
    private int    cursoDocenteId;
    private String fecha;
    private String estado; // A=Asistio, F=Falta, T=Tardanza, J=Justificado

    // Para mostrar en tabla
    private String nombreAlumno;
    private String nombreCurso;

    public Asistencia() {}
    public int    getId()             { return id; }
    public void   setId(int id)       { this.id = id; }
    public int    getAlumnoId()       { return alumnoId; }
    public void   setAlumnoId(int a)  { this.alumnoId = a; }
    public int    getCursoDocenteId() { return cursoDocenteId; }
    public void   setCursoDocenteId(int c){ this.cursoDocenteId = c; }
    public String getFecha()          { return fecha; }
    public void   setFecha(String f)  { this.fecha = f; }
    public String getEstado()         { return estado; }
    public void   setEstado(String e) { this.estado = e; }
    public String getNombreAlumno()   { return nombreAlumno; }
    public void   setNombreAlumno(String n){ this.nombreAlumno = n; }
    public String getNombreCurso()    { return nombreCurso; }
    public void   setNombreCurso(String n){ this.nombreCurso = n; }
}
