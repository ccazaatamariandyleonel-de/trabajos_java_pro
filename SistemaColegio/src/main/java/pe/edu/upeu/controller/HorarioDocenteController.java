package pe.edu.upeu.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class HorarioDocenteController {

    @FXML private TableView<ClaseHorario> tblHorario;
    @FXML private TableColumn<ClaseHorario, String> colDia;
    @FXML private TableColumn<ClaseHorario, String> colHora;
    @FXML private TableColumn<ClaseHorario, String> colCurso;
    @FXML private TableColumn<ClaseHorario, String> colAula;

    private ObservableList<ClaseHorario> listaHorario = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Mapeamos las columnas con las propiedades del objeto
        colDia.setCellValueFactory(new PropertyValueFactory<>("dia"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colCurso.setCellValueFactory(new PropertyValueFactory<>("curso"));
        colAula.setCellValueFactory(new PropertyValueFactory<>("aula"));

        // Rellenamos con datos de prueba estables
        listaHorario.add(new ClaseHorario("Lunes", "08:00 - 09:30", "Programación Orientada a Objetos", "Laboratorio C"));
        listaHorario.add(new ClaseHorario("Miércoles", "10:00 - 11:30", "Estructuras de Datos", "Aula 402"));
        listaHorario.add(new ClaseHorario("Viernes", "08:00 - 10:00", "Ciberseguridad en PYMES", "Laboratorio A"));

        tblHorario.setItems(listaHorario);
    }

    public static class ClaseHorario {
        private final String dia;
        private final String hora;
        private final String curso;
        private final String aula;

        public ClaseHorario(String dia, String hora, String curso, String aula) {
            this.dia = dia;
            this.hora = hora;
            this.curso = curso;
            this.aula = aula;
        }

        public String getDia() { return dia; }
        public String getHora() { return hora; }
        public String getCurso() { return curso; }
        public String getAula() { return aula; }
    }
}