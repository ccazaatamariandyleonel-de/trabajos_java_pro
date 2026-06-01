package pe.edu.upeu.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import pe.edu.upeu.model.Usuario;
import pe.edu.upeu.util.Conexion;

import java.sql.*;
import java.util.Optional;

public class GestionAlumnosController {

    @FXML private TextField txtDni;
    @FXML private TextField txtNombre;
    @FXML private PasswordField txtClave;
    @FXML private ComboBox<String> cbSalon; // Usamos String tal como lo tienes mapeado

    @FXML private TextField txtBuscar;
    @FXML private Button btnBuscar;
    @FXML private Button btnLimpiarFiltro;
    @FXML private Button btnGuardar;
    @FXML private Button btnActualizar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnEliminar;

    // 🎓 Vinculamos la tabla directamente a tu objeto Usuario
    @FXML private TableView<Usuario> tblAlumnos;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colDni;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colSalon;

    // Lista observable para refrescar la tabla dinámicamente
    private ObservableList<Usuario> listaAlumnos = FXCollections.observableArrayList();
    private int idAlumnoSeleccionado = -1; // Guarda el ID del alumno al hacer clic en la tabla

    @FXML
    public void initialize() {
        // 1. Enlazar columnas con los atributos exactos de tu objeto Usuario.java
        // Asegúrate de que tu clase Usuario tenga getId_usuario o cambia "id_usuario" por "idUsuario" según cómo esté escrito allá.
        colId.setCellValueFactory(new PropertyValueFactory<>("id_usuario"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colSalon.setCellValueFactory(new PropertyValueFactory<>("rol")); // Mostraremos el rol o salón provisorio en lo que se lista

        // 2. Cargar salones reales desde tu base de datos SQLite al ComboBox
        cargarComboSalonesReal();

        // 3. Listar los alumnos guardados en la tabla apenas abra la pantalla
        listarAlumnos("");

        // 4. Tu Listener original para detectar clics en la tabla
        tblAlumnos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Al hacer clic, pasamos los datos a las cajas de texto
                idAlumnoSeleccionado = newSelection.getIdUsuario(); // Guardamos el ID secreto para actualizar/eliminar
                txtDni.setText(newSelection.getDni());
                txtNombre.setText(newSelection.getNombre());
                txtClave.setText(newSelection.getClave());

                // Seleccionar el salón en el combo si coincide con el texto
                // Nota: Si en tu base de datos guardas el ID del salón en vez del texto, esto lo adaptaremos luego.
                System.out.println("Cargando datos de: " + newSelection.getNombre());
            }
        });
    }

    /**
     * 🏫 Carga los salones de tu tabla 'salon' de SQLite directo a tu ComboBox<String>
     */
    private void cargarComboSalonesReal() {
        if (cbSalon != null) {
            cbSalon.getItems().clear();
            String sql = "SELECT grado, seccion FROM salon ORDER BY grado, seccion";
            try (Connection cn = Conexion.obtenerConexion();
                 Statement st = cn.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {

                while (rs.next()) {
                    cbSalon.getItems().add(rs.getString("grado") + " - " + rs.getString("seccion"));
                }
            } catch (SQLException e) {
                System.err.println("Error al cargar salones: " + e.getMessage());
            }
        }
    }

    /**
     * 📊 Extrae los alumnos de SQLite y los inyecta en la TableView
     */
    private void listarAlumnos(String criterio) {
        listaAlumnos.clear();
        // Consulta que busca solo alumnos y filtra por DNI o Nombre si escribes algo en el buscador
        String sql = "SELECT * FROM usuario WHERE rol = 'ALUMNO' AND (dni LIKE ? OR nombre LIKE ?)";

        try (Connection cn = Conexion.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, "%" + criterio + "%");
            ps.setString(2, "%" + criterio + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Usuario al = new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("dni"),
                            rs.getString("nombre"),
                            rs.getString("clave"),
                            rs.getString("rol") // Aquí temporalmente viaja el rol
                    );
                    listaAlumnos.add(al);
                }
                tblAlumnos.setItems(listaAlumnos);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar alumnos: " + e.getMessage());
        }
    }

    @FXML
    private void handleGuardar() {
        if (validarCampos()) {
            String dni = txtDni.getText().trim();
            String nombre = txtNombre.getText().trim();
            String clave = txtClave.getText().trim();
            // Nota: Para no romper tu BD, si no tienes id_salon mapeado aún como INT, usaremos un valor por defecto temporal

            String sql = "INSERT INTO usuario (dni, nombre, clave, rol, id_salon) VALUES (?, ?, ?, 'ALUMNO', 1)";

            try (Connection cn = Conexion.obtenerConexion();
                 PreparedStatement ps = cn.prepareStatement(sql)) {

                ps.setString(1, dni);
                ps.setString(2, nombre);
                ps.setString(3, clave);
                ps.executeUpdate();

                mostrarAlerta("Éxito", "Alumno registrado correctamente en la Base de Datos.", AlertType.INFORMATION);
                handleLimpiarFormulario();
                listarAlumnos(""); // 🔄 REFRESCAR TABLA AL INSTANTE

            } catch (SQLException e) {
                mostrarAlerta("Error SQL", "No se pudo guardar: " + e.getMessage(), AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleActualizar() {
        if (idAlumnoSeleccionado == -1) {
            mostrarAlerta("Atención", "Por favor, selecciona un alumno de la tabla para modificarlo.", AlertType.WARNING);
            return;
        }

        if (validarCampos()) {
            String sql = "UPDATE usuario SET dni = ?, nombre = ?, clave = ? WHERE id_usuario = ?";
            try (Connection cn = Conexion.obtenerConexion();
                 PreparedStatement ps = cn.prepareStatement(sql)) {

                ps.setString(1, txtDni.getText().trim());
                ps.setString(2, txtNombre.getText().trim());
                ps.setString(3, txtClave.getText().trim());
                ps.setInt(4, idAlumnoSeleccionado);
                ps.executeUpdate();

                mostrarAlerta("Éxito", "Datos del alumno actualizados en la Base de Datos.", AlertType.INFORMATION);
                handleLimpiarFormulario();
                listarAlumnos(""); // 🔄 REFRESCAR TABLA

            } catch (SQLException e) {
                mostrarAlerta("Error", "No se pudo actualizar: " + e.getMessage(), AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleEliminar() {
        if (idAlumnoSeleccionado == -1) {
            mostrarAlerta("Atención", "Selecciona primero un alumno de la tabla para eliminarlo.", AlertType.WARNING);
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText(null);
        alert.setContentText("¿Está seguro de eliminar al alumno seleccionado de la base de datos?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String sql = "DELETE FROM usuario WHERE id_usuario = ?";
            try (Connection cn = Conexion.obtenerConexion();
                 PreparedStatement ps = cn.prepareStatement(sql)) {

                ps.setInt(1, idAlumnoSeleccionado);
                ps.executeUpdate();

                mostrarAlerta("Éxito", "Alumno eliminado correctamente.", AlertType.INFORMATION);
                handleLimpiarFormulario();
                listarAlumnos(""); // 🔄 REFRESCAR TABLA

            } catch (SQLException e) {
                mostrarAlerta("Error", "No se pudo eliminar: " + e.getMessage(), AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleBuscar() {
        listarAlumnos(txtBuscar.getText().trim());
    }

    @FXML
    private void handleLimpiarFiltro() {
        txtBuscar.clear();
        listarAlumnos("");
    }

    @FXML
    private void handleLimpiarFormulario() {
        txtDni.clear();
        txtNombre.clear();
        txtClave.clear();
        if (cbSalon != null) cbSalon.setValue(null);
        idAlumnoSeleccionado = -1;
        tblAlumnos.getSelectionModel().clearSelection();
    }

    private boolean validarCampos() {
        if (txtDni.getText().isEmpty() || txtNombre.getText().isEmpty() || txtClave.getText().isEmpty() || cbSalon.getValue() == null) {
            mostrarAlerta("Atención", "Por favor complete todos los campos requeridos (DNI, Nombre, Clave y Salón).", AlertType.WARNING);
            return false;
        }
        return true;
    }

    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}