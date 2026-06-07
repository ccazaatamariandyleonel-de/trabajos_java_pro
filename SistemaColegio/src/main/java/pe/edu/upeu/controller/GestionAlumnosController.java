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
 * Controlador CRUD de Alumnos.
 */
public class GestionAlumnosController {

    @FXML private TableView<Alumno>        tablaAlumnos;
    @FXML private TableColumn<Alumno, String> colDni, colNombre, colApellido, colSalon;
    @FXML private TextField txtBuscar, txtDni, txtNombre, txtApellido, txtEmail, txtFechaNac;
    @FXML private Button btnGuardar, btnEliminar, btnNuevo;

    private final AlumnoDAO  alumnoDAO  = new AlumnoDAO();
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
    }

    private void cargarDatos() {
        datos.setAll(alumnoDAO.listarTodos());
    }

    private void buscar(String termino) {
        if (termino == null || termino.isBlank()) {
            datos.setAll(alumnoDAO.listarTodos());
        } else {
            datos.setAll(alumnoDAO.buscar(termino));
        }
    }

    private void seleccionarAlumno(Alumno a) {
        alumnoSeleccionado = a;
        txtDni.setText(a.getDni());
        txtNombre.setText(a.getNombre());
        txtApellido.setText(a.getApellido());
        txtEmail.setText(a.getEmail() != null ? a.getEmail() : "");
        txtFechaNac.setText(a.getFechaNac() != null ? a.getFechaNac() : "");
        btnEliminar.setDisable(false);
    }

    @FXML
    private void handleNuevo() {
        alumnoSeleccionado = null;
        txtDni.clear(); txtNombre.clear(); txtApellido.clear();
        txtEmail.clear(); txtFechaNac.clear();
        btnEliminar.setDisable(true);
        txtDni.requestFocus();
    }

    @FXML
    private void handleGuardar() {
        if (txtDni.getText().isBlank() || txtNombre.getText().isBlank() || txtApellido.getText().isBlank()) {
            alerta("DNI, Nombre y Apellido son obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        if (alumnoSeleccionado == null) {
            // Crear usuario para el alumno
            String username     = txtDni.getText().trim();
            String passHash     = AuthService.hashPassword(txtDni.getText().trim()); // contraseña inicial = DNI
            int    usuarioId    = usuarioDAO.crearUsuario(username, passHash, 3); // 3 = ROL ALUMNO
            if (usuarioId < 0) {
                alerta("No se pudo crear el usuario (¿DNI duplicado?).", Alert.AlertType.ERROR);
                return;
            }
            Alumno nuevo = new Alumno();
            nuevo.setUsuarioId(usuarioId);
            nuevo.setDni(txtDni.getText().trim());
            nuevo.setNombre(txtNombre.getText().trim());
            nuevo.setApellido(txtApellido.getText().trim());
            nuevo.setEmail(txtEmail.getText().trim());
            nuevo.setFechaNac(txtFechaNac.getText().trim());
            if (alumnoDAO.insertar(nuevo)) {
                alerta("Alumno registrado. Contraseña inicial: " + txtDni.getText().trim(), Alert.AlertType.INFORMATION);
                cargarDatos(); handleNuevo();
            }
        } else {
            alumnoSeleccionado.setDni(txtDni.getText().trim());
            alumnoSeleccionado.setNombre(txtNombre.getText().trim());
            alumnoSeleccionado.setApellido(txtApellido.getText().trim());
            alumnoSeleccionado.setEmail(txtEmail.getText().trim());
            alumnoSeleccionado.setFechaNac(txtFechaNac.getText().trim());
            if (alumnoDAO.actualizar(alumnoSeleccionado)) {
                alerta("Alumno actualizado correctamente.", Alert.AlertType.INFORMATION);
                cargarDatos(); handleNuevo();
            }
        }
    }

    @FXML
    private void handleEliminar() {
        if (alumnoSeleccionado == null) return;
        Alert conf = new Alert(Alert.AlertType.CONFIRMATION,
            "¿Eliminar al alumno " + alumnoSeleccionado.getNombreCompleto() + "?",
            ButtonType.YES, ButtonType.NO);
        conf.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                alumnoDAO.eliminar(alumnoSeleccionado.getId());
                cargarDatos(); handleNuevo();
            }
        });
    }

    private void alerta(String msg, Alert.AlertType tipo) {
        Alert a = new Alert(tipo);
        a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
}
