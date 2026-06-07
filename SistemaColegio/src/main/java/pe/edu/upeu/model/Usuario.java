package pe.edu.upeu.model;

public class Usuario {
    private int    id;
    private String username;
    private String password;
    private int    rolId;
    private boolean activo;

    public Usuario() {}
    public Usuario(int id, String username, String password, int rolId, boolean activo) {
        this.id = id; this.username = username; this.password = password;
        this.rolId = rolId; this.activo = activo;
    }

    public int    getId()        { return id; }
    public void   setId(int id)  { this.id = id; }
    public String getUsername()  { return username; }
    public void   setUsername(String u) { this.username = u; }
    public String getPassword()  { return password; }
    public void   setPassword(String p) { this.password = p; }
    public int    getRolId()     { return rolId; }
    public void   setRolId(int r){ this.rolId = r; }
    public boolean isActivo()    { return activo; }
    public void   setActivo(boolean a){ this.activo = a; }
}
