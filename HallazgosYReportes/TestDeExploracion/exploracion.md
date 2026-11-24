# Registro de Pruebas Exploratorias — Petstore API (recurso `pet`)

**Fecha:** 2026-11-23 / 2026-11-24  
**Entorno de pruebas:** API pública `https://petstore.swagger.io/v2`  
**Herramientas:** Postman, curl  
**Referencia del contrato:** https://petstore.swagger.io/v2/swagger.json  

---

## 1. Objetivo de la exploración

Validar de forma manual y sistemática el comportamiento de cada endpoint del recurso `/pet` frente a lo documentado en el contrato de Swagger (OpenAPI v2), identificando discrepancias, códigos de estado reales, validaciones de entrada y posibles fallas del servidor antes de automatizar.

---

## 2. Matriz de Pruebas Exploratorias

| # | Endpoint / Método | Entrada / Caso probado | Esperado según Swagger | Obtenido en la API | Veredicto | Observación / Defecto |
|---|---|---|---|---|:---------:|---|
| 01 | `POST /pet` | JSON con `id`, `name`, `photoUrls`, `status: "available"` | 200 OK (Swagger solo lista 405) | `200 OK` con el JSON creado |   Pasa    | El contrato no documenta explícitamente el código 200 de éxito. |
| 02 | `POST /pet` | JSON omitiendo `photoUrls` (campo requerido en el contrato) | `405 Invalid input` | `400 Bad Request` (`{"code":400,"message":"bad input"}`) | Discrepo  | Responde 400 en lugar del 405 que documenta el contrato. |
| 03 | `GET /pet/{id}` | Id numérico existente (`90011`) | `200 OK` con datos de la mascota | `200 OK` con JSON de la mascota |   Pasa    | Cabecera `Content-Type: application/json` confirmada. |
| 04 | `GET /pet/{id}` | Id numérico que no existe (`90061`) | `404 Pet not found` | `404 Not Found` (`{"code":1,"message":"Pet not found"}`) |   Pasa    | Comportamiento correcto según contrato. |
| 05 | `GET /pet/{id}` | Id alfanumérico / no numérico (`/pet/abc`) | `400 Invalid ID supplied` | `404 Not Found` con mensaje de excepción Java |   Falla   | **DEF-001** (código 404 en vez de 400) y **DEF-002** (expone NumberFormatException). |
| 06 | `GET /pet/findByStatus` | Estado válido (`?status=available`) | `200 OK` con lista de mascotas | `200 OK` con arreglo JSON |   Pasa    | Devuelve mascotas con el estado solicitado. |
| 07 | `GET /pet/findByStatus` | Estado inválido (`?status=desconocido`) | `400 Invalid status value` | `200 OK` con lista vacía `[]` |   Falla   | No valida el valor contra el enum `[available, pending, sold]`. |
| 08 | `PUT /pet` | JSON completo con nombre o estado actualizado | `200 OK` (Swagger solo lista errores) | `200 OK` con datos actualizados |   Pasa    | Actualiza correctamente la mascota en el servidor. |
| 09 | `PUT /pet` | `photoUrls` enviado como texto `"abc"` en vez de lista | `405 Validation exception` | `500 Server Error` (`"something bad happened"`) |   Falla   | **DEF-003**: Error no controlado en backend ante tipo de dato incorrecto (caso CP08). |
| 10 | `POST /pet/{id}` | Formulario (`application/x-www-form-urlencoded`) con `name` y `status` | `200 OK` (Swagger solo lista 405) | `200 OK` (`{"code":200,"message":"{id}"}`) |   Pasa    | Actualiza por form-data correctamente. |
| 11 | `DELETE /pet/{id}` | Id existente con cabecera `api_key: special-key` | `200 OK` (Swagger solo lista 400) | `200 OK` (`{"code":200,"message":"{id}"}`) |   Pasa    | Elimina el registro y confirma con el id en el campo `message`. |
| 12 | `DELETE /pet/{id}` | Id ya eliminado / inexistente | `404 Pet not found` | `404 Not Found` |   Pasa    | Comportamiento correcto de idempotencia. |
| 13 | `POST /pet/{id}/uploadImage` | Petición `multipart/form-data` con archivo de imagen | `200 OK` con mensaje de confirmación | `200 OK` (`code: 200, message: "...bytes"`) |   Pasa    | Sube el archivo binario sin problemas. |
| 14 | `GET /pet/findByTags` | Consulta por etiquetas | `200 OK` | `200 OK` |    Nota   | Endpoint marcado como `deprecated` en el contrato Swagger. |

---

## 3. Conclusiones de la exploración

1. **Documentación incompleta en Swagger:** Los endpoints principales (`POST`, `PUT`, `DELETE` y `POST formulario`) no tienen documentado el código de éxito `200 OK`. Se confirmó experimentalmente que todos responden 200.
2. **Defectos encontrados para reporte:**
   - **DEF-001:** `GET /pet/{id}` con id tipo texto responde 404 en lugar del 400 documentado.
   - **DEF-002:** Se expone la traza interna `java.lang.NumberFormatException` en el JSON de respuesta.
   - **DEF-003:** `PUT /pet` se cae con error `500` en lugar de responder `405` cuando se envían tipos de datos incompatibles en `photoUrls`.
3. **Decisión para la automatización:**
   - La suite de pruebas debe enviar siempre `Accept: application/json` para asegurar el formato de respuesta.
   - El defecto DEF-003 se automatizó como el caso CP08 con el tag `@DefectoConocido`, manteniéndolo fuera de la corrida principal para evidenciar el bug sin romper el build de la integracion continua mi king.
