# MovieLibrary

Aplicación Android para gestionar un mini catálogo personal de películas. Permite registrar películas vistas o por ver, marcarlas como vistas, editarlas y eliminarlas. Toda la información se guarda en una base de datos local con Room.

Este proyecto fue desarrollado como **Taller Práctico 4** del curso, con foco en aplicar la arquitectura **MVVM** junto con **Navigation Component**, **Safe Args**, **LiveData** y **Room**.

---

## Funcionalidades

- Ver el listado completo de películas registradas.
- Agregar nuevas películas a través de un formulario.
- Ver el detalle de una película seleccionada.
- Marcar o desmarcar una película como vista.
- Editar los datos de una película existente.
- Eliminar películas del catálogo.
- Persistencia local: las películas se mantienen aunque se cierre la app.
- La UI se actualiza automáticamente al cambiar los datos (gracias a LiveData).

---

## Tecnologías y librerías

| Componente | Uso |
|---|---|
| **Kotlin** | Lenguaje base del proyecto |
| **Room** | Persistencia local sobre SQLite |
| **Navigation Component** | Navegación entre Fragments |
| **Safe Args** | Paso de argumentos type-safe entre pantallas |
| **LiveData** | Observación reactiva del estado de la UI |
| **ViewModel** | Gestión del estado de la UI |
| **ViewBinding** | Acceso seguro a las vistas |
| **Coroutines** | Operaciones asíncronas |
| **Material Components** | Componentes visuales |

---

## Arquitectura

El proyecto sigue el patrón **MVVM (Model-View-ViewModel)** con un **Repository** intermediando entre el ViewModel y la fuente de datos:
UI (Fragment)  ──>  ViewModel  ──>  Repository  ──>  DAO  ──>  Room (SQLite)
↑                                                         │
└─────────────────  LiveData  ──────────────────────────  ┘

### Estructura de paquetes
com.example.movielibrary
├── model              # Clase de dominio Movie
├── data
│   ├── MovieMapper    # Conversiones Entity ↔ Movie
│   └── db             # MovieEntity, MovieDao, AppDatabase
├── repository         # MovieRepository
├── viewmodel          # MovieViewModel + Factory
├── ui                 # Fragments y Adapter del RecyclerView
├── MainActivity.kt    # Activity única con NavHostFragment
└── MovieLibraryApp.kt # Application personalizada (singleton de DB y Repo)

---

## Cómo correr el proyecto

### Requisitos

- Android Studio Hedgehog o superior.
- JDK 17.
- SDK Android 24+ (compileSdk 34).

### Pasos

1. Clonar el repositorio:
```bash
   git clone https://github.com/TU_USUARIO/MovieLibrary-Taller4.git
```
2. Abrir el proyecto en Android Studio (`File → Open` → selecciona la carpeta).
3. Esperar a que termine el sync de Gradle.
4. Conectar un dispositivo físico o lanzar un emulador (API 24+).
5. Pulsar **Run**.

---

## Flujo de navegación
MovieListFragment  ──(seleccionar película)──>  MovieDetailFragment
│                                                │
│                                                ├──> MovieEditFragment (modo editar)
│                                                │
└──(FAB +)──────────────────────────────────────>  MovieEditFragment (modo crear)

El paso de argumentos entre pantallas se implementó primero con **Bundle manual** y luego se migró a **Safe Args**, que genera clases tipadas (`MovieListFragmentDirections`, `MovieDetailFragmentArgs`, etc.) y elimina los errores típicos de claves mal escritas o tipos incorrectos.

---

## Reflexión sobre los componentes utilizados

### ViewModel
Es la capa donde vive el estado de la UI. En este proyecto, `MovieViewModel` mantiene la lista de películas y se encarga de pedirle al repository las operaciones de insertar, actualizar o borrar. Su gran ventaja es que sobrevive a cambios de configuración como rotaciones de pantalla, así que cuando el Fragment se recrea, el estado sigue ahí sin tener que volver a consultar la base de datos.

### LiveData
Es lo que conecta el ViewModel con la UI de forma reactiva. En lugar de tener que actualizar manualmente el RecyclerView cada vez que cambia algo, el Fragment simplemente observa el `LiveData<List<Movie>>` que expone el ViewModel. Cuando Room emite una nueva lista (porque se insertó, actualizó o borró una película), la UI se refresca automáticamente. Además, respeta el ciclo de vida del Fragment, lo cual previene memory leaks: solo se notifica cuando el Fragment está en estado activo.

### Repository
Es la pieza que oculta de dónde vienen los datos. `MovieRepository` recibe el DAO de Room y expone funciones que devuelven el modelo de dominio `Movie`, no la entidad `MovieEntity`. Esto significa que el ViewModel no sabe nada de Room: si en el futuro cambiara la fuente de datos por una API REST, solo habría que modificar el repository y nada más en la app rompería. Esta separación es clave para mantener un código mantenible y testeable.

### Navigation Component, NavController y Safe Args
El **Navigation Component** es el sistema oficial de Android para mover al usuario entre pantallas. El archivo `nav_graph.xml` declara visualmente todas las pantallas (Fragments) y las acciones que las conectan. El **NavController** es el objeto que ejecuta esa navegación desde el código (`findNavController().navigate(...)`).

**Safe Args** es un plugin que genera clases tipadas (como `MovieDetailFragmentArgs`) a partir de los argumentos declarados en el grafo. La diferencia con un `Bundle` manual es enorme: con `Bundle`, las claves son strings y los errores solo aparecen en tiempo de ejecución; con Safe Args, cualquier error de tipo o de nombre lo detecta el compilador antes de correr la app. Durante el taller implementamos primero la versión con `Bundle` para entender los riesgos, y luego migramos a Safe Args para apreciar la diferencia.

### Room (Entity, DAO, Database)
**Room** es la librería oficial de persistencia local de Android, una capa sobre SQLite que valida las consultas SQL en tiempo de compilación. Tiene tres piezas principales:

- **Entity** (`MovieEntity`): describe cómo se guarda cada fila en la tabla. Las anotaciones `@Entity`, `@PrimaryKey` y `@ColumnInfo` definen el esquema.
- **DAO** (`MovieDao`): es la interfaz con las operaciones disponibles (consultar, insertar, actualizar, eliminar). Room genera la implementación automáticamente.
- **Database** (`AppDatabase`): es el punto de entrada que une todo. Se implementa como Singleton para que solo exista una conexión en toda la app.

Lo que más valoré fue que Room devuelve `LiveData` directamente desde el DAO, así que la integración con el ViewModel y la UI fluye sin esfuerzo: una sola consulta y la UI ya queda viva ante cualquier cambio.

---

## Conclusión

Esta arquitectura permite que cada capa tenga una sola responsabilidad: la UI muestra datos, el ViewModel los gestiona, el Repository los obtiene y Room los persiste. El resultado es una app pequeña pero con una base sólida sobre la que se puede crecer sin caer en código espagueti. El uso de LiveData y Safe Args, en particular, hace que muchos bugs típicos de Android (memory leaks, crashes por argumentos mal pasados, UI desincronizada) simplemente no puedan ocurrir.

---

## Autor

**Esteban Avila**  
Taller 4 – Desarrollo Móvil