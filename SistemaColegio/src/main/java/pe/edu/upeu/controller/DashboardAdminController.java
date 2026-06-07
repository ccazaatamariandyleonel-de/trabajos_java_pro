package pe.edu.upeu.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import pe.edu.upeu.App;
import pe.edu.upeu.dao.AlumnoDAO;
import pe.edu.upeu.dao.CursoDAO;
import pe.edu.upeu.dao.DocenteDAO;
import pe.edu.upeu.dao.SalonDAO;
import pe.edu.upeu.session.SesionUsuario;

import java.io.IOException;

/**
 * Dashboard del Administrador con menú lateral.
 * Muestra estadísticas generales y permite navegar a módulos.
 */
public class DashboardAdminController {

    @FXML private Label      lblBienvenida;
    @FXML private Label      lblTotalAlumnos;
    @FXML private Label      lblTotalDocentes;
    @FXML private Label      lblTotalSalones;
    @FXML private Label      lblTotalCursos;
    @FXML private BorderPane panelPrincipal;

    private final AlumnoDAO  alumnoDAO  = new AlumnoDAO();
    private final DocenteDAO docenteDAO = new DocenteDAO();
    private final SalonDAO   salonDAO   = new SalonDAO();
    private final CursoDAO   cursoDAO   = new CursoDAO();

    @FXML
    public void initialize() {
        SesionUsuario s = SesionUsuario.getInstance();
        lblBienvenida.setText("Bienvenido(a), " + s.getNombreCompleto());
        actualizarEstadisticas();
    }

    private void actualizarEstadisticas() {
        lblTotalAlumnos.setText(String.valueOf(alumnoDAO.contarTotal()));
        lblTotalDocentes.setText(String.valueOf(docenteDAO.contarTotal()));
        lblTotalSalones.setText(String.valueOf(salonDAO.contarTotal()));
        lblTotalCursos.setText(String.valueOf(cursoDAO.contarTotal()));
    }

    @FXML private void abrirAlumnos()   { cargarVista("GestionAlumnos"); }
    @FXML private void abrirDocentes()  { cargarVista("GestionDocentes"); }
    @FXML private void abrirSalones()   { cargarVista("GestionSalones"); }
    @FXML private void abrirCursos()    { cargarVista("GestionCursos"); }
    @FXML private void abrirAsistencia(){ cargarVista("GestionAsistencia"); }
    @FXML private void abrirNotas()     { cargarVista("GestionNotas"); }
    @FXML private void abrirReportes()  { cargarVista("Reportes"); }
    @FXML private void abrirConfig()    { cargarVista("Configuracion"); }

    @FXML private void abrirDashboard() {
        actualizarEstadisticas();
        // Limpiar el centro solo si hay una vista de módulo cargada (no el dashboard base)
        Node centro = panelPrincipal.getCenter();
        if (centro != null) {
            panelPrincipal.setCenter(null);
        }
    }

    private void cargarVista(String nombre) {
        try {
            FXMLLoader loader = App.crearLoader(nombre);
            Node vista = loader.load();
            panelPrincipal.setCenter(vista);
        } catch (IOException e) {
            mostrarError("No se pudo cargar el módulo: " + nombre + "\n" + e.getMessage());
        }
    }

    private void mostrarError(String msg) {
        Label lbl = new Label(msg);
        lbl.setStyle("-fx-text-fill: red; -fx-font-size: 14;");
        panelPrincipal.setCenter(lbl);
    }

    @FXML
    private void handleCerrarSesion() {
        SesionUsuario.getInstance().cerrarSesion();
        try {
            App.setRootConTamano("Login", 480, 520);
        } catch (IOException e) {
            System.err.println("Error al cerrar sesión: " + e.getMessage());
        }
    }
}
