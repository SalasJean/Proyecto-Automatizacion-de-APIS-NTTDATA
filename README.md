# petstore-api-test

Proyecto de automatización de pruebas para servicios REST sobre la API pública de Swagger Petstore (`https://petstore.swagger.io/v2`), enfocado en el recurso `pet`. Desarrollado como práctica del proyecto integrador del curso **"Automatización de Servicios" de NTT DATA**, implementando el patrón **Screenplay** con **Java 17**, **Maven**, **Serenity BDD**, **Cucumber**, **Rest Assured** y **JUnit**.

---

## Objetivo

Automatizar y validar de manera confiable, mantenible y escalable las operaciones CRUD (creación, consulta por ID, consulta por estado, actualización con JSON, actualización con formulario x-www-form-urlencoded, eliminación y carga de imágenes multipart) sobre el recurso `pet` de la API Petstore, garantizando la cobertura de escenarios positivos (*happy path*) y negativos bajo estándares profesionales de aseguramiento de calidad (QA Automation).

---

## Requisitos

- **Java Development Kit (JDK)**: Versión 17 (LTS) o superior instalada y configurada en la variable de entorno `JAVA_HOME`.
- **Apache Maven**: Versión 3.8.0 o superior configurada en el `PATH`.
- **Conectividad a Internet**: Requerida tanto para la descarga inicial de dependencias como para interactuar con la API pública en vivo (`https://petstore.swagger.io/v2`).

---

## Estructura del Proyecto

El proyecto sigue una arquitectura limpia basada en **Screenplay Pattern**, manteniendo el código de producción en `src/main/java` y las pruebas y recursos en `src/test`:

```text
petstore-api-test/
├── pom.xml                                    # Gestión de dependencias y plugins de Maven (Surefire, Serenity)
├── README.md                                  # Documentación técnica del proyecto
└── src/
    ├── main/
    │   └── java/
    │       └── com/nttdata/testing/
    │           ├── models/
    │           │   └── Pet.java               # Modelo POJO y fábrica para la construcción centralizada del body JSON
    │           ├── questions/
    │           │   ├── ResponseCode.java      # Question: obtiene el código de estado HTTP (statusCode)
    │           │   ├── ResponseField.java     # Question: extrae valores o listas del body JSON mediante JsonPath
    │           │   └── ResponseHeader.java    # Question: extrae cabeceras HTTP (ej. Content-Type)
    │           └── tasks/
    │               ├── DeletePet.java         # Task: DELETE /pet/{petId}
    │               ├── GetPetById.java        # Task: GET /pet/{petId}
    │               ├── GetPetsByStatus.java   # Task: GET /pet/findByStatus?status={status}
    │               ├── PostPet.java           # Task: POST /pet (creación con body JSON)
    │               ├── PostPetImage.java      # Task: POST /pet/{petId}/uploadImage (multipart/form-data)
    │               ├── PostPetWithForm.java   # Task: POST /pet/{petId} (application/x-www-form-urlencoded)
    │               └── PutPet.java            # Task: PUT /pet (actualización con body JSON)
    └── test/
        ├── java/
        │   └── com/nttdata/testing/
        │       ├── RunnerTest.java            # Test Runner Cucumber + Serenity con JUnit 4
        │       └── stepDefinitions/
        │           ├── ParameterDefinition.java # Configuración del escenario (@Before) y tipo {actor}
        │           └── PetStepDefinition.java   # Traductor de pasos Gherkin hacia Tasks y Questions
        └── resources/
            ├── serenity.conf                  # Configuración centralizada de Serenity y URL base
            ├── features/
            │   └── APIPet.feature             # 8 escenarios de prueba documentados en Gherkin
            └── files/
                └── mascota.png                # Archivo de imagen de prueba para carga multipart
```

---

## Cómo Ejecutar

### Ejecución completa del ciclo de pruebas y generación de reportes
Para compilar, ejecutar todos los escenarios y generar el reporte agregado de Serenity, ejecute:

```bash
mvn clean verify
```

### Ejecución filtrada por tags de Cucumber
Si desea ejecutar un subconjunto específico de escenarios:

- **Creación**:
  ```bash
  mvn test "-Dcucumber.filter.tags=@Crear"
  ```
- **Consulta (ID, estado y negativo)**:
  ```bash
  mvn test "-Dcucumber.filter.tags=@Consultar"
  ```
- **Actualización (JSON y formulario)**:
  ```bash
  mvn test "-Dcucumber.filter.tags=@Actualizar"
  ```
- **Eliminación**:
  ```bash
  mvn test "-Dcucumber.filter.tags=@Eliminar"
  ```
- **Avanzado (Subida de imagen)**:
  ```bash
  mvn test "-Dcucumber.filter.tags=@Avanzado"
  ```

---

## Ubicación del Reporte de Serenity

Una vez finalizada la ejecución de `mvn clean verify`, el reporte interactivo en HTML de Serenity BDD se genera automáticamente en la siguiente ruta exacta:

```text
target/site/serenity/index.html
```

Ruta absoluta en el sistema:
```text
C:\Users\SALAS\Documents\SpringToolSuite\java-projects\petstore-api-test\target\site\serenity\index.html
```

Puede abrir este archivo en cualquier navegador web moderno para visualizar el estado de cada escenario, detalles de las peticiones HTTP enviadas, respuestas del servidor, tiempos de respuesta y evidencia paso a paso.

---

## Decisiones de Diseño

1. **Adopción Estricta del Patrón Screenplay (Separación de Responsabilidades)**:
   - *Por qué*: A diferencia del modelo tradicional basado en Page Object o controladores monolíticos, Screenplay centra las pruebas en el Actor y sus Habilidades (`CallAnApi`). Cada `Task` realiza **una única petición HTTP atómica** (un solo verbo y endpoint), mientras que las `Questions` se limitan exclusivamente a leer la respuesta sin generar efectos colaterales. Las aserciones y validaciones residen exclusivamente en los `StepDefinitions`. Esta separación reduce el acoplamiento, facilita el mantenimiento y genera una trazabilidad clara y legible para perfiles técnicos y de negocio en los reportes de Serenity.

2. **Centralización del Payload y Modelo de Dominio (`Pet.java`)**:
   - *Por qué*: Para cumplir con el principio DRY (*Don't Repeat Yourself*), la estructura del JSON de una mascota se encapsuló en la clase `com.nttdata.testing.models.Pet`. Esto garantiza que peticiones tanto de creación (`PostPet`) como de actualización (`PutPet`) reutilicen el mismo formato de serialización. Además, asegura el cumplimiento del contrato Swagger OpenAPI v2 donde el arreglo `photoUrls` es requerido de forma obligatoria por la API para evitar errores `400 Bad Request`.

3. **Externalización y Fuente Única de Verdad para la Configuración (`serenity.conf`)**:
   - *Por qué*: La URL base (`https://petstore.swagger.io/v2`) y las configuraciones de ejecución se gestionan en un único archivo HOCON (`serenity.conf`), eliminando cualquier cadena "quemada" (hardcodeada) en el código Java. En los step definitions se accede a este valor mediante `EnvironmentSpecificConfiguration`. Esto permite cambiar de entorno (por ejemplo desarrollo, pruebas o producción) pasando una variable de entorno o perfil de Maven sin modificar una sola línea de código fuente ni de los archivos `.feature`.
