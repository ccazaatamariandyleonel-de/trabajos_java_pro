package pe.edu.upeu.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import pe.edu.upeu.App;
import pe.edu.upeu.session.SesionUsuario;
import java.io.IOException;

public class DashboardDocenteController {

    @FXML
    private Label lblBienvenida;

    @FXML
    private VBox panelCentral;

    @FXML
    public void initialize() {
        // 1. Ponemos el texto de bienvenida inicial de forma directa y segura
        if (lblBienvenida != null) {
            lblBienvenida.setText("Bienvenido(a), Docente " + SesionUsuario.getInstance().getNombreCompleto());
        }

        // Removido handleMisCursos() de aquí para evitar fallos de inicialización asíncrona
    }

    // 1. Botón "Mi Dashboard" - Restaura el saludo original dinámicamente de forma segura
    @FXML
    private void handleMisCursos() {
        System.out.println("Regresando al inicio del Dashboard Docente...");

        // Validación de seguridad obligatoria para evitar el NullPointerException
        if (panelCentral == null) {
            System.err.println("⚠️ El panelCentral no está enlazado correctamente en el FXML.");
            return;
        }

        // Limpiamos el contenedor central
        panelCentral.getChildren().clear();

        // Reconfiguramos la etiqueta de bienvenida principal si existe
        if (lblBienvenida != null) {
            lblBienvenida.setText("Bienvenido(a), Docente " + SesionUsuario.getInstance().getNombreCompleto());
            lblBienvenida.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");
            panelCentral.getChildren().add(lblBienvenida);
        }

        // Creamos el subtítulo de forma dinámica por código
        Label lblSubtitulo = new Label("Selecciona una opción del menú lateral para gestionar tus clases.");
        lblSubtitulo.setStyle("-fx-text-fill: #666; -fx-font-size: 14px;");

        // Lo añadimos al panel de forma segura
        panelCentral.getChildren().add(lblSubtitulo);
    }

    // 2. Botón "Mis Notas" (Aquí el docente REGISTRA notas)
    @FXML
    private void handleVerNotas() {
        System.out.println("Cargando vista de Registro de Notas (Docente)...");
        cargarPantallaInterna("RegistroNotasDocente");
    }

    // 3. Botón "Mi Asistencia" (Aquí el docente MARCA asistencia)
    @FXML
    private void handleVerAsistencia() {
        System.out.println("Cargando vista de Registro de Asistencias (Docente)...");
        cargarPantallaInterna("RegistroAsistenciaDocente");
    }

    // 4. Botón "Mi Horario" (Aquí el docente ve sus CLASES dictadas)
    @FXML
    private void handleVerHorario() {
        System.out.println("Cargando vista de Horario de Clases (Docente)...");
        cargarPantallaInterna("HorarioDocente");
    }

    // MÉTODO AUXILIAR: El motor que intercambia los FXML dentro del panel derecho
    private void cargarPantallaInterna(String nombreFxml) {
        try {
            if (panelCentral == null) {
                System.err.println("⚠️ No se pueden cargar pantallas porque panelCentral es nulo.");
                return;
            }

            // 1. Limpiamos el contenedor central antes de meter la nueva pantalla
            panelCentral.getChildren().clear();

            // 2. CORRECCIÓN DE RUTA: Nos aseguramos de buscar desde la raíz de resources
            String rutaCompleta = "/view/" + nombreFxml + ".fxml";
            System.out.println("Docente - Buscando archivo físico en: " + rutaCompleta);

            java.net.URL urlFxml = getClass().getResource(rutaCompleta);

            if (urlFxml == null) {
                System.err.println("❌ ERROR CRÍTICO: No se encontró el archivo " + nombreFxml + ".fxml en resources/view/");
                return;
            }

            // 3. Cargamos e inyectamos la vista de forma segura
            FXMLLoader loader = new FXMLLoader(urlFxml);
            Parent vistaInterna = loader.load();

            panelCentral.getChildren().add(vistaInterna);
            System.out.println("✅ ¡Vista Docente [" + nombreFxml + "] cargada con éxito!");

        } catch (IOException e) {
            System.err.println("Error crítico al abrir la pantalla interna del docente " + nombreFxml + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCerrarSesion() {
        // CORREGIDO: Volvemos a tu método real en español
        SesionUsuario.getInstance().cerrarSesion();
        try {
            App.setRootConTamano("Login", 480, 520);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}