package pe.edu.upeu.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pe.edu.upeu.dao.SalonDAO;
import pe.edu.upeu.model.Salon;

import java.util.List;

public class GestionSalonController {

    @FXML private TextField txtBuscar, txtNombre, txtGrado, txtSeccion, txtNivel, txtAnoAcademico;
    @FXML private TableView<Salon> tablaSalones;
    @FXML private TableColumn<Salon, String> colNombre, colGrado, colSeccion, colNivel;
    @FXML private TableColumn<Salon, Integer> colAnoAcademico;
    @FXML private Button btnGuardar, btnEliminar, btnNuevo;

    private final SalonDAO salonDAO = new SalonDAO();
    private Salon salonSeleccionado = null;

    @FXML
    public void initialize() {
        // 1. Vincular las columnas de la tabla con los atributos de el modelo Salon
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colGrado.setCellValueFactory(new PropertyValueFactory<>("grado"));
        colSeccion.setCellValueFactory(new PropertyValueFactory<>("seccion"));
        colNivel.setCellValueFactory(new PropertyValueFactory<>("nivel"));
        colAnoAcademico.setCellValueFactory(new PropertyValueFactory<>("añoAcademico"));

        // 2. Cargar la lista inicial desde la base de datos
        listarSalones();

        // 3. Detectar clics en la tabla para cargar los datos en los cuadros de texto
        tablaSalones.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                salonSeleccionado = newSelection;
                llenarFormulario(newSelection);
                btnEliminar.setDisable(false);
            }
        });
    }

    private void listarSalones() {
        List<Salon> lista = salonDAO.listar();
        ObservableList<Salon> data = FXCollections.observableArrayList(lista);
        tablaSalones.setItems(data);
    }

    private void llenarFormulario(Salon s) {
        txtNombre.setText(s.getNombre());
        txtGrado.setText(s.getGrado());
        txtSeccion.setText(s.getSeccion());
        txtNivel.setText(s.getNivel());
        txtAnoAcademico.setText(String.valueOf(s.getAñoAcademico()));
    }

    @FXML
    private void handleNuevo() {
        salonSeleccionado = null;
        limpiarCampos();
        btnEliminar.setDisable(true);
    }

    @FXML
    private void handleGuardar() {
        // Validación básica de campos obligatorios
        if (txtNombre.getText().isEmpty() || txtGrado.getText().isEmpty() || txtSeccion.getText().isEmpty()) {
            mostrarAlerta("Error", "Los campos Nombre, Grado y Sección son totalmente obligatorios.");
            return;
        }

        // Validar que el año sea un número válido
        int ano;
        try {
            ano = Integer.parseInt(txtAnoAcademico.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El campo Año Académico debe contener un número válido (Ej. 2026).");
            return;
        }

        if (salonSeleccionado == null) {
            // Operación: NUEVO SALÓN
            Salon nuevo = new Salon();
            nuevo.setNombre(txtNombre.getText().trim());
            nuevo.setGrado(txtGrado.getText().trim());
            nuevo.setSeccion(txtSeccion.getText().trim());
            nuevo.setNivel(txtNivel.getText().trim());
            nuevo.setAñoAcademico(ano);

            salonDAO.guardar(nuevo);
            mostrarAlerta("Éxito", "Nuevo salón registrado correctamente.");
        } else {
            // Operación: MODIFICAR EXISTENTE
            salonSeleccionado.setNombre(txtNombre.getText().trim());
            salonSeleccionado.setGrado(txtGrado.getText().trim());
            salonSeleccionado.setSeccion(txtSeccion.getText().trim());
            salonSeleccionado.setNivel(txtNivel.getText().trim());
            salonSeleccionado.setAñoAcademico(ano);

            salonDAO.modificar(salonSeleccionado);
            mostrarAlerta("Éxito", "Los datos del salón han sido actualizados.");
        }

        listarSalones();
        limpiarCampos();
    }

    @FXML
    private void handleEliminar() {
        if (salonSeleccionado != null) {
            salonDAO.eliminar(salonSeleccionado.getId());
            listarSalones();
            limpiarCampos();
            mostrarAlerta("Éxito", "Salón eliminado satisfactoriamente.");
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtGrado.clear();
        txtSeccion.clear();
        txtNivel.clear();
        txtAnoAcademico.clear();
        salonSeleccionado = null;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}