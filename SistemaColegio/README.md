# Sistema Académico - Colegio UPEU

## Cómo ejecutar en IntelliJ IDEA Community

### Requisitos
- Java 17 (JDK 17)
- Maven 3.8+
- IntelliJ IDEA Community Edition

### Pasos

1. **Abrir el proyecto**
   - File → Open → selecciona la carpeta `SistemaColegio`
   - IntelliJ detectará el `pom.xml` automáticamente

2. **Esperar que Maven descargue las dependencias**
   - Verás la barra de progreso abajo

3. **Ejecutar la aplicación**
   - Opción A (recomendada): Maven tool window → Plugins → javafx → javafx:run
   - Opción B: Clic derecho en `App.java` → Run 'App.main()'
   - Opción C: Terminal → `mvn javafx:run`

### Credenciales por defecto
| Usuario | Contraseña | Rol          |
|---------|------------|--------------|
| admin   | admin123   | Administrador|

### Dónde se crea la base de datos
`C:\Users\TuUsuario\SistemaColegio\colegio_db.db`
(En Linux/Mac: `~/SistemaColegio/colegio_db.db`)

### Arquitectura del proyecto
```
src/main/java/pe/edu/upeu/
├── App.java                  ← Clase principal JavaFX
├── controller/               ← Controladores FXML (MVC)
│   ├── LoginController       ← Autenticación
│   ├── DashboardAdminController
│   ├── DashboardDocenteController
│   ├── DashboardAlumnoController
│   └── GestionAlumnosController
├── model/                    ← Entidades POO
│   ├── Usuario, Alumno, Docente, Salon, Curso, Nota, Asistencia
├── dao/                      ← Acceso a datos SQLite
│   ├── UsuarioDAO, AlumnoDAO, DocenteDAO, SalonDAO, CursoDAO
├── service/                  ← Lógica de negocio
│   └── AuthService           ← Login con BCrypt
├── session/
│   └── SesionUsuario         ← Sesión activa (Singleton)
└── util/
    ├── Conexion              ← Singleton SQLite (ruta portátil)
    └── InicializadorBD       ← Crea tablas al iniciar

src/main/resources/
├── view/                     ← Archivos FXML
└── css/estilos.css           ← Estilos globales
```
