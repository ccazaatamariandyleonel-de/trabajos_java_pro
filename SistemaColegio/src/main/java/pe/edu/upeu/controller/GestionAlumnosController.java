package pe.edu.upeu.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pe.edu.upeu.dao.AlumnoDAO;
import pe.edu.upeu.model.Alumno;
import pe.edu.upeu.service.AuthService;
import pe.edu.upeu.dao.UsuarioDAO;

/**
 * Controlador CRUD de Alumnos - Validación visual (sin alertas)
 */
public class GestionAlumnosController {

    @FXML private TableView<Alumno> tablaAlumnos;
    @FXML private TableColumn<Alumno, String> colDni, colNombre, colApellido, colSalon;
    @FXML private TextField txtBuscar, txtDni, txtNombre, txtApellido, txtEmail, txtFechaNac;
    @FXML private Button btnGuardar, btnEliminar, btnNuevo;

    private final AlumnoDAO alumnoDAO = new AlumnoDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final ObservableList<Alumno> datos = FXCollections.observableArrayList();
    private Alumno alumnoSeleccionado = null;

    @FXML
    public void initialize() {
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colSalon.setCellValueFactory(new PropertyValueFactory<>("salonNombre"));

        tablaAlumnos.setItems(datos);
        cargarDatos();

        tablaAlumnos.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevo) -> {
            if (nuevo != null) seleccionarAlumno(nuevo);
        });

        txtBuscar.textProperty().addListener((obs, old, val) -> buscar(val));

        // Filtro estricto para DNI
        txtDni.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) txtDni.setText(newValue.replaceAll("[^\\d]", ""));
            if (newValue.length() > 8) txtDni.setText(oldValue);
        });
    }

    private void limpiarBordes() {
        txtDni.setStyle(null);
        txtNombre.setStyle(null);
        txtApellido.setStyle(null);
        txtEmail.setStyle(null);
    }

    private boolean validar() {
        limpiarBordes();
        boolean error = false;
        String style = "-fx-border-color: red; -fx-border-width: 2px;";

        if (txtDni.getText().trim().length() != 8) { txtDni.setStyle(style); error = true; }
        if (txtNombre.getText().isBlank()) { txtNombre.setStyle(style); error = true; }
        if (txtApellido.getText().isBlank()) { txtApellido.setStyle(style); error = true; }
        if (txtEmail.getText().isBlank()) { txtEmail.setStyle(style); error = true; }

        return !error;
    }

    @FXML
    private void handleGuardar() {
        if (!validar()) return; // Si hay error, se marcan en rojo y se detiene la ejecución

        if (alumnoSeleccionado == null) {
            String passHash = AuthService.hashPassword(txtDni.getText().trim());
            int usuarioId = usuarioDAO.crearUsuario(txtDni.getText().trim(), passHash, 3);

            if (usuarioId < 0) return;

            Alumno nuevo = new Alumno();
            nuevo.setUsuarioId(usuarioId);
            nuevo.setDni(txtDni.getText().trim());
            nuevo.setNombre(txtNombre.getText().trim());
            nuevo.setApellido(txtApellido.getText().trim());
            nuevo.setEmail(txtEmail.getText().trim());
            nuevo.setFechaNac(txtFechaNac.getText().trim());

            if (alumnoDAO.insertar(nuevo)) {
                cargarDatos();
                handleNuevo();
            }
        } else {
            alumnoSeleccionado.setDni(txtDni.getText().trim());
            alumnoSeleccionado.setNombre(txtNombre.getText().trim());
            alumnoSeleccionado.setApellido(txtApellido.getText().trim());
            alumnoSeleccionado.setEmail(txtEmail.getText().trim());
            alumnoSeleccionado.setFechaNac(txtFechaNac.getText().trim());

            if (alumnoDAO.actualizar(alumnoSeleccionado)) {
                cargarDatos();
                handleNuevo();
            }
        }
    }

    private void cargarDatos() { datos.setAll(alumnoDAO.listarTodos()); }

    private void buscar(String termino) {
        if (termino == null || termino.isBlank()) datos.setAll(alumnoDAO.listarTodos());
        else datos.setAll(alumnoDAO.buscar(termino));
    }

    private void seleccionarAlumno(Alumno a) {
        alumnoSeleccionado = a;
        txtDni.setText(a.getDni());
        txtNombre.setText(a.getNombre());
        txtApellido.setText(a.getApellido());
        txtEmail.setText(a.getEmail() != null ? a.getEmail() : "");
        txtFechaNac.setText(a.getFechaNac() != null ? a.getFechaNac() : "");
        btnEliminar.setDisable(false);
        limpiarBordes();
    }

    @FXML
    private void handleNuevo() {
        alumnoSeleccionado = null;
        txtDni.clear(); txtNombre.clear(); txtApellido.clear();
        txtEmail.clear(); txtFechaNac.clear();
        btnEliminar.setDisable(true);
        limpiarBordes();
        txtDni.requestFocus();
    }

    @FXML
    private void handleEliminar() {
        if (alumnoSeleccionado == null) return;
        alumnoDAO.eliminar(alumnoSeleccionado.getId());
        cargarDatos();
        handleNuevo();
    }
}