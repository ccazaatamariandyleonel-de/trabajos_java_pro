package pe.edu.upeu;

public class MainLauncher {
    public static void main(String[] args) {
        // Este puente engaña a la máquina virl de Java.
        // Al no heredar de Application, JavaFX se carga como una librería común
        // y no te vuelve a pedir componentes modulares.
        App.main(args);
    }
}
