package pe.edu.upeu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // Carga tu vista de Login instalada en resources/view
        scene = new Scene(loadFXML("/view/Login"), 400, 450);
        stage.setTitle("Sistema Colegio - Control de Asistencia");
        stage.setScene(scene);
        stage.show();
    }

    public static Scene getScene() {
        return scene;
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    // ============================================================
    // 🎯 ESTE MÉTODO CORRIGE EL ERROR DE COMPONENTES FALTANTES
    // ============================================================
    public static void main(String[] args) {
        // Al ejecutar desde aquí, si da error de componentes,
        // es porque IntelliJ no ha leído las dependencias del pom.xml.
        launch(args);
    }
}