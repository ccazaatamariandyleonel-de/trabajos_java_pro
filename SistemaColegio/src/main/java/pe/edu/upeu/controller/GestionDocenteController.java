package pe.edu.upeu.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pe.edu.upeu.dao.DocenteDAO;
import pe.edu.upeu.dao.UsuarioDAO;
import pe.edu.upeu.model.Docente;
import pe.edu.upeu.service.AuthService;
import java.util.List;

public class GestionDocenteController {

    @FXML private TextField txtBuscar, txtDni, txtNombre, txtApellido, txtEmail, txtEspecialidad;
    @FXML private TableView<Docente> tablaDocentes;
    @FXML private TableColumn<Docente, String> colDni, colNombre, colApellido, colEspecialidad;
    @FXML private Button btnGuardar, btnEliminar, btnNuevo;

    private final DocenteDAO docenteDAO = new DocenteDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Docente docenteSeleccionado = null;

    @FXML
    public void initialize() {
        // --- VALIDACIONES DE ENTRADA (MÁSCARAS) ---
        txtDni.textProperty().addListener((obs, old, val) -> {
            if (!val.matches("\\d*")) txtDni.setText(old);
            if (txtDni.getText().length() > 8) txtDni.setText(txtDni.getText().substring(0, 8));
        });

        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));

        listarDocentes();
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

        if (txtDni.getText().length() < 8) { txtDni.setStyle(style); error = true; }
        if (txtNombre.getText().isBlank()) { txtNombre.setStyle(style); error = true; }
        if (txtApellido.getText().isBlank()) { txtApellido.setStyle(style); error = true; }
        if (!txtEmail.getText().contains("@")) { txtEmail.setStyle(style); error = true; }

        return !error;
    }

    @FXML
    private void handleGuardar() {
        if (!validar()) return; // Valida visualmente y detiene si hay error

        if (docenteSeleccionado == null) {
            String passHash = AuthService.hashPassword(txtDni.getText());
            int usuarioId = usuarioDAO.crearUsuario(txtDni.getText(), passHash, 2);

            Docente nuevo = new Docente();
            nuevo.setDni(txtDni.getText());
            nuevo.setNombre(txtNombre.getText());
            nuevo.setApellido(txtApellido.getText());
            nuevo.setEmail(txtEmail.getText());
            nuevo.setEspecialidad(txtEspecialidad.getText());
            nuevo.setUsuarioId(usuarioId);

            docenteDAO.guardar(nuevo);
        } else {
            docenteSeleccionado.setNombre(txtNombre.getText());
            docenteSeleccionado.setApellido(txtApellido.getText());
            docenteSeleccionado.setEmail(txtEmail.getText());
            docenteSeleccionado.setEspecialidad(txtEspecialidad.getText());

            docenteDAO.modificar(docenteSeleccionado);
        }

        listarDocentes();
        limpiarCampos();
    }

    private void listarDocentes() {
        List<Docente> lista = docenteDAO.listar();
        tablaDocentes.setItems(FXCollections.observableArrayList(lista));
    }

    private void limpiarCampos() {
        txtDni.clear(); txtNombre.clear(); txtApellido.clear();
        txtEmail.clear(); txtEspecialidad.clear();
        docenteSeleccionado = null;
        limpiarBordes();
        btnEliminar.setDisable(true);
    }

    @FXML private void handleNuevo() { limpiarCampos(); }

    @FXML
    private void handleEliminar() {
        if (docenteSeleccionado != null) {
            docenteDAO.eliminar(docenteSeleccionado.getId());
            listarDocentes();
            limpiarCampos();
        }
    }
}