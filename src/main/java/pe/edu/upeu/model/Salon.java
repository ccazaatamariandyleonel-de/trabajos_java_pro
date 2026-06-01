package pe.edu.upeu.model;

public class Salon {
    private int idSalon;
    private String grado;
    private String seccion;

    // Constructor vacío (Obligatorio para Java)
    public Salon() {}

    // Constructor con parámetros
    public Salon(int idSalon, String grado, String seccion) {
        this.idSalon = idSalon;
        this.grado = grado;
        this.seccion = seccion;
    }

    // ====================================================================
    // ⚙️ GETTERS Y SETTERS (Para que el controlador lea y escriba datos)
    // ====================================================================
    public int getIdSalon() {
        return idSalon;
    }

    public void setIdSalon(int idSalon) {
        this.idSalon = idSalon;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    /**
     * 🎨 El secreto para el ComboBox:
     * Hace que en el menú desplegable se lea "1ro - A" en vez de un código raro.
     */
    @Override
    public String toString() {
        return grado + " - " + seccion;
    }
}