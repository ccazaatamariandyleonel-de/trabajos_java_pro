package pe.edu.upeu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pe.edu.upeu.util.Conexion;
import pe.edu.upeu.util.InicializadorBD;

import java.io.IOException;

/**
 * Clase principal de la aplicación.
 * CORRECCIÓN: Se añade inicialización de BD al arranque y cierre correcto de conexión.
 * CORRECCIÓN: loadFXML ahora busca en /view/ dentro del classpath (getResource correcta).
 */
public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        // Inicializar base de datos y tablas al arrancar
        InicializadorBD.inicializar();

        scene = new Scene(cargarFXML("Login"), 480, 520);
        scene.getStylesheets().add(
            getClass().getResource("/css/estilos.css").toExternalForm());

        stage.setTitle("Sistema Académico - Colegio UPEU");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /** Cambia la vista raíz de la escena actual. */
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(cargarFXML(fxml));
    }

    /** Cambia el tamaño de la ventana al navegar. */
    public static void setRootConTamano(String fxml, double width, double height) throws IOException {
        scene.setRoot(cargarFXML(fxml));
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
        primaryStage.centerOnScreen();
    }

    public static Scene getScene()       { return scene; }
    public static Stage getStage()       { return primaryStage; }

    /**
     * CORRECCIÓN CLAVE: getResource("/view/Nombre.fxml") con la barra inicial
     * busca desde la raíz del classpath, lo que funciona tanto en desarrollo
     * como en el JAR empaquetado.
     */
    public static Parent cargarFXML(String nombre) throws IOException {
        FXMLLoader loader = new FXMLLoader(
            App.class.getResource("/view/" + nombre + ".fxml"));
        if (loader.getLocation() == null) {
            throw new IOException("No se encontró el FXML: /view/" + nombre + ".fxml");
        }
        return loader.load();
    }

    /** Carga FXML y devuelve el loader (para acceder al controlador). */
    public static FXMLLoader crearLoader(String nombre) {
        return new FXMLLoader(App.class.getResource("/view/" + nombre + ".fxml"));
    }

    @Override
    public void stop() {
        // Cerrar conexión BD correctamente al salir
        Conexion.cerrar();
        System.out.println("Aplicación cerrada.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
