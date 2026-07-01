package pe.edu.upeu.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pe.edu.upeu.dao.AlumnoDAO;
import pe.edu.upeu.model.Alumno;
import pe.edu.upeu.service.AuthService;
import pe.edu.upeu.dao.UsuarioDAO;
import pe.edu.upeu.util.ConsultaDNI;
import pe.edu.upeu.util.PersonaDto;

import java.util.concurrent.CompletableFuture;

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

        txtDni.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) txtDni.setText(newValue.replaceAll("[^\\d]", ""));
            if (newValue.length() > 8) txtDni.setText(oldValue);
        });
    }

    @FXML
    private void handleBuscarDni() {
        String dni = txtDni.getText().trim();
        if (dni.length() != 8) {
            txtDni.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            return;
        }

        CompletableFuture.runAsync(() -> {
            ConsultaDNI consulta = new ConsultaDNI();
            PersonaDto p = consulta.consultarDNI(dni);

            Platform.runLater(() -> {
                if (p != null && p.getNombre() != null && !p.getNombre().isEmpty()) {
                    txtNombre.setText(p.getNombre());
                    txtApellido.setText(p.getApellidoPaterno() + " " + p.getApellidoMaterno());
                    txtDni.setStyle(null);
                } else {
                    txtDni.setStyle("-fx-border-color: orange;");
                }
            });
        });
    }

    @FXML
    private void handleGuardar() {
        if (!validar()) return;

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
}