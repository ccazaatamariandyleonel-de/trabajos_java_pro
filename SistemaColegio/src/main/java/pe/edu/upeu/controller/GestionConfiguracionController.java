package pe.edu.upeu.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class GestionConfiguracionController {

    @FXML private TextField txtInstitucion;
    @FXML private ComboBox<String> cbAnioEscolar;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnGuardarConfig;

    @FXML
    public void initialize() {
        System.out.println("Cargando la interfaz de Configuración...");

        // Esta validación evita que la pantalla falle si el FXML tarda en enlazar el combo
        if (cbAnioEscolar != null) {
            cbAnioEscolar.setItems(FXCollections.observableArrayList("2025", "2026", "2027"));
            cbAnioEscolar.setValue("2026");
        }
    }

    @FXML
    private void handleGuardarConfig() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText("Configuración guardada correctamente.");
        alert.showAndWait();
    }

}