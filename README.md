# Foro Hub API - README

## Descripción
Este proyecto es una API REST para la gestión de un foro. La API permite a los usuarios realizar operaciones CRUD sobre tópicos, respuestas, usuarios, cursos y perfiles. Además, incluye un sistema de autenticación y autorización basado en roles (TipoPerfil) junto con un excelente manejo de errores.

## Índice
1. [Tecnologías Utilizadas](#tecnologías-utilizadas)
2. [Configuración del Entorno](#configuración-del-entorno)
   - [Requisitos Previos](#requisitos-previos)
   - [Configuración de Variables de Entorno](#configuración-de-variables-de-entorno)
3. [Estructura del Proyecto](#estructura-del-proyecto)
4. [Dependencias Principales](#dependencias-principales)
5. [Configuración de Seguridad](#configuración-de-seguridad)
   - [Autenticación](#autenticación)
   - [Restricciones en Endpoints](#restricciones-en-endpoints)
6. [Ejemplos de Endpoints](#ejemplos-de-endpoints)
7. [Instalación y Configuración](#instalación-y-configuración)
    
## Autor
**Juan Sebastian Giraldo Aguirre**
- [LinkedIn](https://www.linkedin.com/in/juanse951/)
- [GitHub](https://github.com/juanse951)
- [TikTok](https://www.tiktok.com/@paghaninitv)
- juanse951@gmail.com

## Tecnologías Utilizadas
- **Java 17** Versión 17 en adelante
- **Spring Boot**  Versión 3 en adelante
- **Maven** Versión 4 en adelante
- **Spring Security**
- **JWT** (JSON Web Tokens)
- **Flyway** Para migraciones de base de datos
- **Hibernate / JPA**
- **MySQL** Versión 8 en adelante
- **Swagger** para documentación de la API
- **Lombok** Para reducir el código boilerplate
- **IntelliJ IDEA** (opcional, como IDE recomendado)
- **Insomnia** Para realizar pruebas de las rutas de la API

## Configuración del Entorno

### Requisitos Previos

- **Java JDK:** Descarga e instala la versión [17 o superior desde Oracle](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html).
- **Maven:** Asegúrate de tener [Maven 4 o superior instalado](https://maven.apache.org/download.cgi).
- **MySQL:** Instala la versión [8 o superior desde MySQL](https://dev.mysql.com/downloads/installer/) y configura una base de datos para la aplicación.
- **IDE:** Se recomienda [IntelliJ IDEA](https://www.jetbrains.com/idea/), aunque puedes usar cualquier IDE compatible con Java.
- **Spring Boot:** Necesario para el desarrollo de la aplicación. Para crear el proyecto, puedes usar [Spring Initializr](https://start.spring.io/).
- **Insomnia:** Opcional para realizar pruebas manuales de la API. Puedes descargarlo desde [Insomnia](https://insomnia.rest/download).

### Configuración de Variables de Entorno

- **JWT Secret:**  
  Crea una variable de entorno `JWT_SECRET`. En el archivo `application.properties`, configura:  
  `api.security.secret=${JWT_SECRET}` también debes configurar el tiempo de expiración del JWT.
  En el archivo `application.properties`, agrega la siguiente línea para establecer la expiración en milisegundos (por ejemplo, 2 horas):
  `api.security.expiration=7200000`

- **Usuario de MySQL:**  
  Crea una variable de entorno `DB_USER`. En el archivo `application.properties`, configura:  
  `spring.datasource.username=${DB_USER}`
  
- **Contraseña de MySQL:**  
  Crea una variable de entorno `DB_PASSWORD`. En el archivo `application.properties`, configura:  
  `spring.datasource.password=${DB_PASSWORD}`

- **URL de la base de datos MySQL:**
  Crea una variable de entorno `DB_HOST` que contenga la dirección del servidor de tu base de datos (por ejemplo, localhost o una URL de servidor remoto)

- **Nombre Base de Datos de MySQL:**
  Crea una variable de entorno `DB_NAME` con el nombre de la base de datos. En el archivo `application.properties`, configura:
  `spring.datasource.url=jdbc:mysql://${DB_HOST}/${DB_NAME}`

  # Estructura del Proyecto

La organización del código sigue una arquitectura limpia y modular basada en buenas prácticas de desarrollo. A continuación, se describe la estructura principal del proyecto:

## Paquetes principales

### controller
Contiene los controladores REST que gestionan las solicitudes HTTP y delegan la lógica de negocio a los servicios correspondientes.

### domain
Agrupa la lógica del dominio de la aplicación, organizada en subpaquetes:
- *curso*: Incluye las entidades y clases relacionadas con los cursos.
- *respuesta*: Contiene las entidades y lógica relacionadas con las respuestas en el foro.
- *topico*: Maneja las entidades y operaciones relacionadas con los tópicos del foro.
- *usuario*: Gestiona las entidades y datos de los usuarios registrados.

### infra
Contiene configuraciones e implementaciones específicas para la infraestructura del proyecto, organizado en subpaquetes:
- *errores*: Define clases para el manejo de errores y excepciones personalizadas.
- *exceptions*: Amplía las excepciones del sistema con clases específicas del dominio.
- *security*: Implementa la configuración de seguridad, autenticación y manejo de tokens JWT.
- *springdoc*: Gestiona la configuración de documentación de APIs utilizando SpringDoc.

### service
Contiene la lógica de negocio de la aplicación, encargándose de la interacción entre los controladores y las capas de acceso a datos.

## Directorio resources
- *db.migration*: Almacena los scripts de migración de la base de datos para la gestión de cambios en el esquema.
- *static*: Archivos estáticos utilizados por la aplicación (imágenes, CSS, JavaScript, etc.).
- *templates*: Plantillas HTML para la generación de contenido dinámico.
- *application.properties*: Archivo de configuración principal del proyecto.

---

Esta estructura asegura la separación de responsabilidades, facilitando el mantenimiento y escalabilidad del sistema.

  # Dependencias Principales

- **Spring Web**  
  Utilizado para la construcción de APIs REST, permitiendo manejar solicitudes HTTP y generar respuestas en formato JSON, entre otros.

- **Spring Boot DevTools**  
  Herramienta que facilita el desarrollo al proporcionar recarga automática de cambios, simplificando la experiencia de desarrollo y pruebas.

- **Lombok**  
  Biblioteca que reduce el código repetitivo, generando automáticamente los métodos `getters`, `setters`, `toString()`, `equals()`, `hashCode()`, entre otros.

- **Spring Data JPA**  
  Permite interactuar con bases de datos utilizando JPA (Java Persistence API), facilitando la gestión de datos y la persistencia de objetos Java en una base de datos relacional.

- **Flyway Migration**  
  Herramienta para gestionar la evolución de la base de datos mediante migraciones de esquemas, permitiendo llevar un control sobre los cambios realizados en la base de datos.

- **MySQL Driver**  
  Conector necesario para interactuar con bases de datos MySQL, permitiendo que la aplicación se conecte y realice operaciones sobre la base de datos.

- **Spring Boot Starter Validation**  
  Proporciona las herramientas necesarias para realizar validaciones de datos en la aplicación, utilizando anotaciones como `@NotNull`, `@Size`, entre otras.

- **Spring Security**  
  Framework de seguridad que se encarga de gestionar la autenticación y autorización, protegiendo rutas y recursos de la aplicación según los roles y permisos del usuario.

- **Spring Security Test**  
  Conjunto de herramientas para realizar pruebas de seguridad en la aplicación, simulando diferentes escenarios de autenticación y autorización.

- **Spring Boot Starter Test**  
  Conjunto de herramientas para realizar pruebas unitarias y de integración en la aplicación, facilitando la verificación del correcto funcionamiento del código.

- **Java JWT**  
  Biblioteca que facilita la creación y verificación de JSON Web Tokens (JWT), utilizados para la autenticación y autorización basada en tokens.

- **Springdoc OpenAPI Starter WebMVC UI**  
  Genera automáticamente la documentación interactiva de la API utilizando OpenAPI, facilitando la visualización de los endpoints y la interacción con ellos a través de una interfaz web.

- **Springdoc OpenAPI UI**  
  Herramienta que crea una interfaz gráfica interactiva para la documentación de la API, permitiendo a los desarrolladores probar los endpoints de la API directamente desde la interfaz de usuario.

- **MySQL Connector Java (Versión 8.0.29)**  
  Driver para conectar la aplicación con bases de datos MySQL en su versión específica, asegurando la compatibilidad entre la base de datos y la aplicación.

## Configuración de Seguridad
El sistema de seguridad utiliza Spring Security y JWT para autenticar y autorizar las solicitudes. Los accesos a los endpoints están restringidos según los roles definidos:

### Nota Importante
El primer usuario registrado en la base de datos será automáticamente asignado como **ADMIN**. Este usuario tendrá acceso completo a todas las funcionalidades del sistema.

- **USER**: Acceso básico para crear, listar y buscar recursos.
- **MODERATOR**: Permisos para actualizar ciertos recursos además de los permisos de USER.
- **ADMIN**: Acceso total, incluyendo eliminación y configuración de perfiles.

La API utiliza un esquema de autenticación basado en tokens Bearer. Asegúrate de incluir el token en el encabezado `Authorization` en cada solicitud:
```bash
Authorization: Bearer <tu_token>
```

## Autenticación

- **POST** `http://localhost:8080/login` - Genera un token JWT para autenticar las solicitudes posteriores.

### Requisitos Previos
Para que el proceso de autenticación funcione correctamente, asegúrate de que:

#### Correo electronico y Contraseña:
- Se haya creado previamente un usuario en la tabla `usuarios` de la base de datos.
- La contraseña debe estar almacenada de forma segura utilizando el algoritmo de hash **bcrypt**.

**Ejemplo**: Puedes crear un usuario manualmente con una herramienta como **MySQL Workbench**, o hacerlo mediante un script en tu aplicación backend que utilice **bcrypt** para encriptar la contraseña.

### Ejemplo de Solicitud de Autenticación

A continuación se muestra un ejemplo de la solicitud de autenticación utilizando **Insomnia** o cualquier herramienta similar para hacer solicitudes HTTP:

```json
{
    "correoElectronico": "usuario",
    "contrasena": "contraseña@J"
}
```

### Respuesta Esperada

Si las credenciales son válidas, la API devolverá un token JWT que puedes usar para autenticar las solicitudes posteriores. Ejemplo de respuesta:

```json
{
    "jwTtoken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

## Restricciones en Endpoints
### Autenticación
- **POST** `/login`: Permite a los usuarios autenticarse y obtener un token JWT. *Acceso público.*

### Gestión de Usuarios
- **POST** `/usuario/registrar`: Registra un nuevo usuario. *Accesible por USER, MODERATOR, ADMIN.*
- **GET** `/usuario/listado`: Lista todos los usuarios. *Accesible por USER, MODERATOR, ADMIN.*
- **GET** `/usuario/buscar/{id}`: Busca un usuario por su ID. *Accesible por USER, MODERATOR, ADMIN.*
- **PUT** `/usuario/actualizar/{id}`: Actualiza la información de un usuario. *Accesible por USER, MODERATOR, ADMIN.*
- **DELETE** `/usuario/eliminar/{id}`: Elimina un usuario. *Accesible solo por ADMIN.*

### Gestión de Perfiles
- **GET** `/perfil/listado`: Lista todos los perfiles. *Accesible por USER, MODERATOR, ADMIN.*
- **PUT** `/perfil/actualizar/{id}`: Actualiza un perfil. *Accesible solo por ADMIN.*

### Gestión de Tópicos
- **POST** `/topico/registrar`: Crea un nuevo tópico. *Accesible por USER, MODERATOR, ADMIN.*
- **GET** `/topico/listado`: Lista todos los tópicos. *Accesible por USER, MODERATOR, ADMIN.*
- **GET** `/topico/buscar/{id}`: Busca un tópico por su ID. *Accesible por USER, MODERATOR, ADMIN.*
- **PUT** `/topico/actualizar/{id}`: Actualiza un tópico existente. *Accesible por MODERATOR, ADMIN.*
- **DELETE** `/topico/eliminar/{id}`: Elimina un tópico. *Accesible solo por ADMIN.*

### Gestión de Respuestas
- **POST** `/respuesta/registrar/{topicoId}`: Crea una nueva respuesta asociada a un tópico. *Accesible por USER, MODERATOR, ADMIN.*
- **GET** `/respuesta/listado`: Lista todas las respuestas. *Accesible por USER, MODERATOR, ADMIN.*
- **GET** `/respuesta/buscar/{id}`: Busca una respuesta por su ID. *Accesible por USER, MODERATOR, ADMIN.*
- **PUT** `/respuesta/actualizar/{id}`: Actualiza una respuesta existente. *Accesible por MODERATOR, ADMIN.*
- **DELETE** `/respuesta/eliminar/{id}`: Elimina una respuesta. *Accesible solo por ADMIN.*

### Gestión de Cursos
- **POST** `/curso/registrar`: Registra un nuevo curso. *Accesible por USER, MODERATOR, ADMIN.*
- **GET** `/curso/listado`: Lista todos los cursos. *Accesible por USER, MODERATOR, ADMIN.*
- **GET** `/curso/buscar/{id}`: Busca un curso por su ID. *Accesible por USER, MODERATOR, ADMIN.*
- **GET** `/curso/categoria`: Lista todas las categorías disponibles. *Accesible por USER, MODERATOR, ADMIN.*
- **PUT** `/curso/actualizar/{id}`: Actualiza la información de un curso. *Accesible por MODERATOR, ADMIN.*
- **DELETE** `/curso/eliminar/{id}`: Elimina un curso. *Accesible solo por ADMIN.*

  ## Ejemplos de Endpoints 
### Usuarios
#### Registrar Usuario
- **POST** `/usuario/registrar`
- **Body:**
  ```json
  {
  "nombre": "Juan Perez",
  "correoElectronico": "juan.perez@example.com",
  "contrasena": "123456@Juan"
  }
  ```
- **Respuesta:**
  ```json
  {
	"id": 3,
	"nombre": "Juan Perez",
	"correoElectronico": "juan.perez@example.com",
	"perfil": "USER"
  }
  ```

#### Actualizar Usuario
- **PUT** `/usuario/actualizar/{id}`
- **Path Variable:** `id` (ID del usuario)
- **Body:**
  ```json
  {
    "nombre": "Juan Perez",
    "correoElectronico": "juan.perez@example.com",
    "contrasena": "123456@Juan"
  }
  ```
- **Respuesta:**
  ```json
  {
	"id": 1,
	"nombre": "Juan Perez",
	"correoElectronico": "juan.perez@example.com",
	"perfil": "USER"
  }
  ```

#### Listar Usuarios
- **GET** `/usuario/listado?paginacion=1`
- **Respuesta:**
  ```json
  {
    "contenido": [
      {
        "id": 1,
        "nombre": "Juan Perez",
        "correoElectronico": "juan.perez@example.com",
        "perfil": "USER"
      }
    ],
    "totalPaginas": 1,
    "totalElementos": 1
  }
  ```

#### Eliminar Usuario
- **DELETE** `/usuario/eliminar/{id}`
- **Path Variable:** `id` (ID del usuario)
- **Respuesta:**
  ```json
  {
    "mensaje": "El Usuario con ID 3 fue eliminado exitosamente."
  }
  ```

### Tópicos
#### Registrar Tópico
- **POST** `/topico/registrar`
- **Body:**
  ```json
  {
    "titulo": "Nuevo Tópico",
    "mensaje": "Descripción del tópico.",
    "autor_id": 1,
    "curso_id": 1
  }
  ```
- **Respuesta:**
  ```json
  {
	"id": 1,
	"titulo": "Nuevo Tópico",
	"mensaje": "Descripción del tópico.",
	"fechaCreacion": "2025-01-15T13:15:10",
	"status": "ACTIVO",
	"autor": "Juan Perez",
	"curso": "Cursos modernos"
  }
  ```

### Respuestas
#### Registrar Respuesta
- **POST** `/respuesta/registrar/{topicoId}`
- **Path Variable:** `topicoId` (ID del tópico asociado)
- **Body:**
  ```json
  {
  "mensaje": "Esta es una respuesta al tópico.",
  "autor_id": 1
  }
  ```
- **Respuesta:**
  ```json
  {
	"id": 1,
	"mensaje": "Esta es una respuesta al tópico.",
	"topico": "Nuevo topico",
	"fechaCreacion": "2025-01-15T13:20:38.7035289",
	"autor": "Juan Perez",
	"solucion": "PENDIENTE"
  }
  ```

## Instalación y Configuración

1. Clona este repositorio:
   ```bash
   git clone https://github.com/juanse951/foro-hub-api.git
   ```
2. Configura el archivo `application.properties` para la conexión a la base de datos MySQL:
   ```properties
   spring.datasource.url=jdbc:mysql://${DB_HOST}/${DB_NAME}
   spring.datasource.username=${DB_USER}
   spring.datasource.password=${DB_PASSWORD}
   spring.jpa.hibernate.ddl-auto=update
   ```
3. Ejecuta la aplicación:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Accede a la API en `http://localhost:8080` y a la documentación Swagger en: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).


## Contribuciones
Las contribuciones son bienvenidas. Por favor, abre un issue o envía un pull request con tus propuestas.

¡Gracias por usar Foro Hub API!



