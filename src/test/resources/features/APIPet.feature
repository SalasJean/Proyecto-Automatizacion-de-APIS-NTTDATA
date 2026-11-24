@Mascotas
Feature: Gestionar mascotas en la tienda de mascotas (Petstore)

  Como usuario de la tienda de mascotas Petstore
  Quiero gestionar las mascotas de la tienda
  Para poder crear, consultar, actualizar y eliminar sus registros

  # Contrato: https://petstore.swagger.io/v2/swagger.json
  # OJO: Ademas recuerda algo super importante, no necesriamente esta bien o mal usar escenarios otutline o normal dependera mucho del negocio "Depende :)"
  #asi que por eso un scenario solo se usa cuando un caso es unico


  @CP01_Pet @Post_Pet @Mascota @HappyPath
  Scenario Outline: Crear una mascota exitosamente
    Given el actor establece el endpoint de pet
    When el actor crea una mascota con id "<id>" nombre "<name>" y estado "<status>"
    Then el codigo de respuesta debe ser 200
    And el campo "name" de la respuesta debe ser "<name>"
    And el campo "status" de la respuesta debe ser "<status>"

    Examples:
      | id    | name     | status    |
      | 90001 | firulais | available |
      | 90002 | michi    | pending   |
      | 90003 | martin   | sold   |


  @CP02_Pet @Get_Pet @Mascota @HappyPath
  Scenario Outline: Consultar una mascota por su id exitosamente
    Given el actor establece el endpoint de pet
    And el actor crea una mascota con id "<id>" nombre "<name>" y estado "available"
    When el actor consulta la mascota con el id "<id>"
    Then el codigo de respuesta debe ser 200
    And el header "Content-Type" de la respuesta debe ser "application/json"
    And el campo "name" de la respuesta debe ser "<name>"

    Examples:
      | id    | name  |
      | 90011 | rocky |


  @CP03_Pet @Get_Pet @Mascota @HappyPath
  Scenario Outline: Consultar mascotas por estado exitosamente
    Given el actor establece el endpoint de pet
    When el actor consulta las mascotas con estado "<status>"
    Then el codigo de respuesta debe ser 200
    And todas las mascotas de la lista deben tener el estado "<status>"

    Examples:
      | status    |
      | available |
      | pending   |
      | sold      |


  @CP04_Pet @Put_Pet @Mascota @HappyPath
  Scenario Outline: Actualizar una mascota con JSON exitosamente
    Given el actor establece el endpoint de pet
    And el actor crea una mascota con id "<id>" nombre "<name>" y estado "available"
    When el actor actualiza la mascota con id "<id>" nombre "<newName>" y estado "<newStatus>"
    Then el codigo de respuesta debe ser 200
    And el campo "name" de la respuesta debe ser "<newName>"
    And el campo "status" de la respuesta debe ser "<newStatus>"

    Examples:
      | id    | name  | newName  | newStatus |
      | 90021 | bruno | bruno jr | sold      |


  @CP05_Pet @Post_Pet @Mascota @HappyPath
  Scenario Outline: Actualizar una mascota con formulario exitosamente
    Given el actor establece el endpoint de pet
    And el actor crea una mascota con id "<id>" nombre "<name>" y estado "available"
    When el actor actualiza con formulario la mascota con id "<id>" nombre "<newName>" y estado "<newStatus>"
    Then el codigo de respuesta debe ser 200

    Examples:
      | id    | name | newName    | newStatus |
      | 90031 | luna | luna nueva | pending   |


  @CP06_Pet @Delete_Pet @Mascota @HappyPath
  Scenario Outline: Eliminar una mascota exitosamente
    Given el actor establece el endpoint de pet
    And el actor crea una mascota con id "<id>" nombre "<name>" y estado "available"
    When el actor elimina la mascota con id "<id>"
    Then el codigo de respuesta debe ser 200
    And el campo "message" de la respuesta debe ser "<id>"

    Examples:
      | id    | name |
      | 90041 | toby |


  @CP07_Pet @Get_Pet @Mascota @UnHappyPath
  Scenario: Consultar una mascota que no existe
    Given el actor establece el endpoint de pet
    And el actor crea una mascota con id "90061" nombre "tobi" y estado "available"
    And el actor elimina la mascota con id "90061"
    When el actor consulta la mascota con el id "90061"
    Then el codigo de respuesta debe ser 404
    And el campo "message" de la respuesta debe ser "Pet not found"


  # Se espera el 405 que documenta el contrato de oepen api con swager .
  # Pero Hallazgo esta en  pendiente de aislar: la API respondio 500. Excluir de la corrida principal pasara como casi controlado osea Defecto conocido.

  @CP08_Pet @Put_Pet @Mascota @UnHappyPath @DefectoConocido
  Scenario Outline: Actualizar una mascota con photoUrls en formato invalido
    Given el actor establece el endpoint de pet
    And el actor crea una mascota con id "<id>" nombre "<name>" y estado "available"
    When el actor actualiza la mascota con id "<id>" enviando photoUrls "<photoUrls>" en lugar de una lista
    Then el codigo de respuesta debe ser 405

    Examples:
      | id    | name  | photoUrls |
      | 98001 | rocky | abc       |


  @CP09_Pet @Upload_Pet @Mascota @Avanzado @HappyPath
  Scenario Outline: Subir una imagen a una mascota exitosamente
    Given el actor establece el endpoint de pet
    And el actor crea una mascota con id "<id>" nombre "<name>" y estado "available"
    When el actor sube la imagen "<archivo>" a la mascota con id "<id>"
    Then el codigo de respuesta debe ser 200

    Examples:
      | id    | name   | archivo       |
      | 90051 | pelusa | manchitas.png |
