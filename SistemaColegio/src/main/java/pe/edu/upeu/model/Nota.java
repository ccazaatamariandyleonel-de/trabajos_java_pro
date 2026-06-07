package pe.edu.upeu.model;

public class Nota {
    private int    id;
    private int    alumnoId;
    private int    cursoDocenteId;
    private int    bimestre;
    private double nota;

    private String nombreAlumno;
    private String nombreCurso;

    public Nota() {}
    public int    getId()             { return id; }
    public void   setId(int id)       { this.id = id; }
    public int    getAlumnoId()       { return alumnoId; }
    public void   setAlumnoId(int a)  { this.alumnoId = a; }
    public int    getCursoDocenteId() { return cursoDocenteId; }
    public void   setCursoDocenteId(int c){ this.cursoDocenteId = c; }
    public int    getBimestre()       { return bimestre; }
    public void   setBimestre(int b)  { this.bimestre = b; }
    public double getNota()           { return nota; }
    public void   setNota(double n)   { this.nota = n; }
    public String getNombreAlumno()   { return nombreAlumno; }
    public void   setNombreAlumno(String n){ this.nombreAlumno = n; }
    public String getNombreCurso()    { return nombreCurso; }
    public void   setNombreCurso(String n){ this.nombreCurso = n; }
}
