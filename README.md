# petstore-api-test

Proyecto de automatización de pruebas sobre la API REST de Swagger Petstore (`https://petstore.swagger.io/v2`), enfocado en el recurso `pet`. Desarrollado con **Java 17**, **Maven**, **Serenity BDD** bajo el patrón **Screenplay**, **Cucumber**, **Rest Assured** y **AssertJ**.

---

## Requisitos

- **Java JDK**: 17 o superior.
- **Apache Maven**: 3.9.9 -> de preferencia esta version
- Conectarse a la api de swaggerui, petstore.

---

## Estructura del Proyecto

```text
petstore-api-test/
├── pom.xml                                    # Dependencias y config de plugins
├── README.md                                  # Guía del proyecto de como ejecutarlo
└── src/
    ├── main/java/com/nttdata/testing/
    │   ├── models/                            # Modelos de datos de la documentacion de swguer
    │   ├── questions/                         # Lectura de respuestas ya sea del code del body y del header)
    │   └── tasks/                             # Peticiones HTTP atómicas osea el CRUD(PostPet, GetPetById, PutPet, etc.)
    └── test/
        ├── java/com/nttdata/testing/
        │   ├── RunnerTest.java                # Runner de Cucumber para levantar el test con Serenity
        │   └── stepDefinitions/               # Mapeo de steps Gherkin y configuración(driver)
        └── resources/
            ├── serenity.conf                  # Configuración de la apy que es opcional y del la variable de entorno(enviroments)
            ├── features/APIPet.feature        # Escenarios en Gherkin (CP01 al CP09)
            └── files/manchitas.png            # Imagen para prueba para el metodo que acepta upload
```

---

## Cómo Ejecutar

### 1. Ejecución rápida desde el IDE (RunnerTest)
Para correr el proyecto sin usar comandos de terminal:
1. Abre la clase `RunnerTest.java` (en `src/test/java/com/nttdata/testing/RunnerTest.java`).
2. Dale clic al botón que esta al costado de la clase (o clic derecho ➔ **Run 'RunnerTest'**).
3. En la consola y en la pestaña de JUnit de IntelliJ verás todos los tests ejecutados pasando en verde.

### 2. Corrida completa y generación del reporte HTML
Para compilar, ejecutar todos los escenarios y generar automáticamente el reporte consolidado de Serenity:

```bash
mvn clean verify
```

*(Tipsaso: Si ya ejecutaste los tests desde el `RunnerTest` y solo quieres generar o actualizar el reporte HTML sin volver a correr todas las pruebas, puedes ejecutar: `mvn serenity:aggregate`)*.

### 3. Ejecutar el caso de defecto conocido (@DefectoConocido)
El escenario **CP08** valida que la API responda `405` ante datos con formato inválido. Como el servidor de Petstore responde `500`, este caso está aislado para no romper la suite principal. Para correrlo de forma independiente y evidenciar el fallo:

```bash
mvn test "-Dcucumber.filter.tags=@DefectoConocido"
```

*(Resultado esperado de esta corrida: debe fallar se espera `expected: 405 but was: 500`).*

---
## Ubicación del Reporte de Serenity

Al terminar la ejecución de `mvn clean verify`, el reporte HTML se genera automáticamente en:

```text
target/site/serenity/index.html
```

Ruta absoluta:
```text
C:\Users\SALAS\Documents\SpringToolSuite\java-projects\petstore-api-test\target\site\serenity\index.html
```

---

## Decisiones de Diseño

1. **Patrón Screenplay (Desacoplamiento de capas)**:
   Las llamadas HTTP van exclusivamente en `tasks/` (una petición por clase, sin validaciones ni aserciones). Las `questions/` solo leen la respuesta recibida, y las verificaciones residen únicamente en los `stepDefinitions/` usando AssertJ. Esto mantiene el código ordenado, mantenible y fácil de reutilizar.

2. **Independencia de escenarios con preparación reutilizable**:
   El paso `el actor crea una mascota con id ... nombre ... y estado ...` se reutiliza tanto como acción principal (`When`) en la creación, como paso previo de preparación (`And`) en las pruebas de consulta, actualización y eliminación. Así, ningún escenario depende del orden ni del estado de pruebas anteriores.

3. **Manejo de defectos conocidos mediante Tags en el Runner**:
   En lugar de alterar la aserción de CP08 para que espere 500 (lo cual escondería el bug del backend), mantuvimos la validación estricta del contrato (405) y aislamos el caso con `@DefectoConocido` en `RunnerTest.java`. Esto permite que la integración continua pase en verde sin dejar de documentar y automatizar el defecto real.
