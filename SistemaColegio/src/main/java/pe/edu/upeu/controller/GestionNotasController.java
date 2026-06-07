package pe.edu.upeu.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pe.edu.upeu.dao.CursoDAO; // <-- IMPORTANTE: Nuevo import
import pe.edu.upeu.dao.NotaDAO;
import pe.edu.upeu.model.Curso; // <-- IMPORTANTE: Nuevo import
import pe.edu.upeu.model.Nota;

import java.util.List;

public class GestionNotasController {

    // 1. Cambiamos el ComboBox para que maneje Objetos Curso reales
    @FXML private ComboBox<Curso> cbCursos;
    @FXML private ComboBox<String> cbSalones; // Lo dejamos así temporalmente hasta conectar salones
    @FXML private TableView<Nota> tablaNotas;
    @FXML private TableColumn<Nota, String> colAlumno;
    @FXML private TableColumn<Nota, Double> colNota1;
    @FXML private TableColumn<Nota, Integer> colNota2;
    @FXML private TableColumn<Nota, String> colNota3;
    @FXML private TableColumn<Nota, String> colPromedio;

    @FXML private Button btnFiltrar, btnCalcular, btnGuardarNotas;

    private NotaDAO notaDAO;
    private CursoDAO cursoDAO; // <-- Instancia del DAO de cursos
    private ObservableList<Nota> listaNotasObservable;

    @FXML
    public void initialize() {
        System.out.println("Pantalla de Notas inicializada correctamente.");

        notaDAO = new NotaDAO();
        cursoDAO = new CursoDAO(); // <-- Inicializamos el DAO
        listaNotasObservable = FXCollections.observableArrayList();

        colAlumno.setCellValueFactory(new PropertyValueFactory<>("nombreAlumno"));
        colNota1.setCellValueFactory(new PropertyValueFactory<>("nota"));
        colNota2.setCellValueFactory(new PropertyValueFactory<>("bimestre"));

        btnGuardarNotas.setDisable(true);

        // 2. CONEXIÓN REAL: Jalamos los cursos de la base de datos H2
        cargarCursosEnComboBox();

        cbSalones.getItems().addAll("1° Secundaria A", "2° Secundaria B");
    }

    private void cargarCursosEnComboBox() {
        // Obtenemos la lista directamente desde el CursoDAO
        List<Curso> listaCursosBD = cursoDAO.listar();

        if (listaCursosBD.isEmpty()) {
            System.out.println("Ojo: No hay cursos registrados en la base de datos H2 aún.");
        } else {
            // Agregamos todos los objetos Curso al combo. Gracias al toString(), mostrará el nombre.
            cbCursos.getItems().addAll(listaCursosBD);
        }
    }

    @FXML
    private void handleFiltrarAlumnos() {
        // Validamos que se haya seleccionado un curso antes de continuar
        Curso cursoSeleccionado = cbCursos.getSelectionModel().getSelectedItem();
        if (cursoSeleccionado == null) {
            mostrarAlerta("Advertencia", "Por favor, seleccione un curso del menú.", Alert.AlertType.WARNING);
            return;
        }

        System.out.println("Filtrando alumnos para el curso: " + cursoSeleccionado.getNombre());
        listaNotasObservable.clear();

        Nota n = new Nota();
        n.setAlumnoId(202);
        // 3. USO REAL: Le asignamos el ID real del curso seleccionado en el combo
        n.setCursoDocenteId(cursoSeleccionado.getId());
        n.setNombreAlumno("Maniatado Cáceres, Elard");
        n.setBimestre(1);
        n.setNota(15.5);

        listaNotasObservable.add(n);
        tablaNotas.setItems(listaNotasObservable);
        btnGuardarNotas.setDisable(false);
    }

    @FXML
    private void handleCalcular() {
        System.out.println("Notas verificadas y listas para guardar.");
    }

    @FXML
    private void handleGuardar() {
        if (listaNotasObservable.isEmpty()) {
            mostrarAlerta("Advertencia", "No hay calificaciones en la tabla.", Alert.AlertType.WARNING);
            return;
        }

        int guardados = 0;
        for (Nota n : listaNotasObservable) {
            int rs = notaDAO.guardar(n);
            if (rs > 0) guardados++;
        }

        if (guardados > 0) {
            mostrarAlerta("Éxito", "Se guardó la nota correctamente en tu tabla H2.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "No se pudo guardar la calificación.", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}