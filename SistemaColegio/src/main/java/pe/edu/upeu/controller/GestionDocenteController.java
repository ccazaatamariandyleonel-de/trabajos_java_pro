package pe.edu.upeu.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pe.edu.upeu.dao.DocenteDAO;
import pe.edu.upeu.dao.UsuarioDAO;
import pe.edu.upeu.model.Docente;
import pe.edu.upeu.service.AuthService;

import java.util.List;

public class GestionDocenteController {

    // Se eliminó txtTelefono de la lista
    @FXML private TextField txtBuscar, txtDni, txtNombre, txtApellido, txtEmail, txtEspecialidad;
    @FXML private TableView<Docente> tablaDocentes;
    @FXML private TableColumn<Docente, String> colDni, colNombre, colApellido, colEspecialidad;
    @FXML private Button btnGuardar, btnEliminar, btnNuevo;

    private final DocenteDAO docenteDAO = new DocenteDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Docente docenteSeleccionado = null;

    @FXML
    public void initialize() {
        // 1. Configurar columnas de la tabla
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));

        // 2. Cargar datos
        listarDocentes();

        // 3. Evento al seleccionar fila
        tablaDocentes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                docenteSeleccionado = newSelection;
                llenarFormulario(newSelection);
                btnEliminar.setDisable(false);
            }
        });
    }

    private void listarDocentes() {
        List<Docente> lista = docenteDAO.listar();
        ObservableList<Docente> data = FXCollections.observableArrayList(lista);
        tablaDocentes.setItems(data);
    }

    private void llenarFormulario(Docente d) {
        txtDni.setText(d.getDni());
        txtNombre.setText(d.getNombre());
        txtApellido.setText(d.getApellido());
        txtEmail.setText(d.getEmail());
        txtEspecialidad.setText(d.getEspecialidad());
        // Se quitó la línea del teléfono
    }

    @FXML
    private void handleNuevo() {
        docenteSeleccionado = null;
        limpiarCampos();
        btnEliminar.setDisable(true);
    }

    @FXML
    private void handleGuardar() {
        if (txtDni.getText().isEmpty() || txtNombre.getText().isEmpty()) {
            mostrarAlerta("Error", "DNI y Nombre son obligatorios.");
            return;
        }

        if (docenteSeleccionado == null) {
            // Lógica para NUEVO DOCENTE
            String passHash = AuthService.hashPassword(txtDni.getText());
            int usuarioId = usuarioDAO.crearUsuario(txtDni.getText(), passHash, 2); // 2 = Rol DOCENTE

            // 2. Crear docente
            Docente nuevo = new Docente();
            nuevo.setDni(txtDni.getText());
            nuevo.setNombre(txtNombre.getText());
            nuevo.setApellido(txtApellido.getText());
            nuevo.setEmail(txtEmail.getText());
            nuevo.setEspecialidad(txtEspecialidad.getText());
            // Se quitó nuevo.setTelefono
            nuevo.setUsuarioId(usuarioId);

            docenteDAO.guardar(nuevo);
            mostrarAlerta("Éxito", "Docente registrado. Usuario: " + txtDni.getText());
        } else {
            // Lógica para ACTUALIZAR
            docenteSeleccionado.setNombre(txtNombre.getText());
            docenteSeleccionado.setApellido(txtApellido.getText());
            docenteSeleccionado.setEmail(txtEmail.getText());
            docenteSeleccionado.setEspecialidad(txtEspecialidad.getText());
            // Se quitó docenteSeleccionado.setTelefono

            docenteDAO.modificar(docenteSeleccionado);
            mostrarAlerta("Éxito", "Datos actualizados.");
        }

        listarDocentes();
        limpiarCampos();
    }

    @FXML
    private void handleEliminar() {
        if (docenteSeleccionado != null) {
            docenteDAO.eliminar(docenteSeleccionado.getId());
            listarDocentes();
            limpiarCampos();
            mostrarAlerta("Éxito", "Docente eliminado.");
        }
    }

    private void limpiarCampos() {
        txtDni.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtEmail.clear();
        txtEspecialidad.clear();
        // Se quitó txtTelefono.clear()
        docenteSeleccionado = null;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}