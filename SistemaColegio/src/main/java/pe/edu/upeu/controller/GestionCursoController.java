package pe.edu.upeu.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pe.edu.upeu.dao.CursoDAO;
import pe.edu.upeu.model.Curso;

import java.util.List;

public class GestionCursoController {

    @FXML private TextField txtBuscar, txtCodigo, txtNombre, txtDescripcion;
    @FXML private TableView<Curso> tablaCursos;
    @FXML private TableColumn<Curso, String> colCodigo, colNombre, colDescripcion;
    @FXML private Button btnGuardar, btnEliminar, btnNuevo;

    private final CursoDAO cursoDAO = new CursoDAO();
    private Curso cursoSeleccionado = null;

    @FXML
    public void initialize() {
        // 1. Configurar columnas de la tabla
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        // 2. Cargar datos desde la BD
        listarCursos();

        // 3. Evento al hacer clic en una fila de la tabla
        tablaCursos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cursoSeleccionado = newSelection;
                llenarFormulario(newSelection);
                btnEliminar.setDisable(false);
            }
        });
    }

    private void listarCursos() {
        List<Curso> lista = cursoDAO.listar();
        ObservableList<Curso> data = FXCollections.observableArrayList(lista);
        tablaCursos.setItems(data);
    }

    private void llenarFormulario(Curso c) {
        txtCodigo.setText(c.getCodigo());
        txtNombre.setText(c.getNombre());
        txtDescripcion.setText(c.getDescripcion());
    }

    @FXML
    private void handleNuevo() {
        cursoSeleccionado = null;
        limpiarCampos();
        btnEliminar.setDisable(true);
    }

    @FXML
    private void handleGuardar() {
        if (txtCodigo.getText().isEmpty() || txtNombre.getText().isEmpty()) {
            mostrarAlerta("Error", "El Código y el Nombre del curso son obligatorios.");
            return;
        }

        if (cursoSeleccionado == null) {
            // NUEVO CURSO
            Curso nuevo = new Curso();
            nuevo.setCodigo(txtCodigo.getText().trim());
            nuevo.setNombre(txtNombre.getText().trim());
            nuevo.setDescripcion(txtDescripcion.getText().trim());

            cursoDAO.guardar(nuevo);
            mostrarAlerta("Éxito", "Curso registrado con éxito.");
        } else {
            // MODIFICAR CURSO
            cursoSeleccionado.setCodigo(txtCodigo.getText().trim());
            cursoSeleccionado.setNombre(txtNombre.getText().trim());
            cursoSeleccionado.setDescripcion(txtDescripcion.getText().trim());

            cursoDAO.modificar(cursoSeleccionado);
            mostrarAlerta("Éxito", "Curso actualizado correctamente.");
        }

        listarCursos();
        limpiarCampos();
    }

    @FXML
    private void handleEliminar() {
        if (cursoSeleccionado != null) {
            cursoDAO.eliminar(cursoSeleccionado.getId());
            listarCursos();
            limpiarCampos();
            mostrarAlerta("Éxito", "Curso eliminado.");
        }
    }

    private void limpiarCampos() {
        txtCodigo.clear();
        txtNombre.clear();
        txtDescripcion.clear();
        cursoSeleccionado = null;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}