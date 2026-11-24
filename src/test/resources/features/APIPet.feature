@Pet
Feature: Gestionar mascotas en la tienda de mascotas (Petstore)

  # Contrato: https://petstore.swagger.io/v2/swagger.json (basePath /v2)
  # OJO: el swagger solo documenta errores para POST /pet, PUT /pet, POST /pet/{id} y DELETE.
  # El codigo de exito (normalmente 200) hay que confirmarlo en Postman antes de ejecutar.
  # Petstore es una API publica y compartida: se usan ids altos y distintos por escenario.

  @Crear @HappyPath
  Scenario Outline: Crear una mascota nueva
    Given el actor establece la url base de la tienda de mascotas
    When el actor envia una solicitud POST para crear la mascota con id "<id>" nombre "<name>" y estado "<status>"
    Then el codigo de respuesta deberia ser 200
    And el campo "name" de la respuesta deberia ser "<name>"
    And el campo "status" de la respuesta deberia ser "<status>"

    Examples:
      | id    | name     | status    |
      | 90001 | firulais | available |
      | 90002 | michi    | pending   |

  @Consultar @HappyPath
  Scenario Outline: Consultar una mascota por su id
    Given el actor establece la url base de la tienda de mascotas
    And existe una mascota registrada con id "<id>" y nombre "<name>"
    When el actor consulta la mascota con el id "<id>"
    Then el codigo de respuesta deberia ser 200
    And el header "Content-Type" de la respuesta deberia ser "application/json"
    And el campo "name" de la respuesta deberia ser "<name>"

    Examples:
      | id    | name  |
      | 90011 | rocky |

  @Consultar @HappyPath
  Scenario Outline: Consultar mascotas por estado
    Given el actor establece la url base de la tienda de mascotas
    When el actor consulta las mascotas con estado "<status>"
    Then el codigo de respuesta deberia ser 200
    And todas las mascotas de la lista deberian tener el estado "<status>"

    Examples:
      | status    |
      | available |
      | pending   |
      | sold      |

  @Actualizar @HappyPath
  Scenario Outline: Actualizar una mascota existente con JSON
    Given el actor establece la url base de la tienda de mascotas
    And existe una mascota registrada con id "<id>" y nombre "<name>"
    When el actor actualiza la mascota con id "<id>" nombre "<newName>" y estado "<newStatus>"
    Then el codigo de respuesta deberia ser 200
    And el campo "name" de la respuesta deberia ser "<newName>"
    And el campo "status" de la respuesta deberia ser "<newStatus>"

    Examples:
      | id    | name  | newName  | newStatus |
      | 90021 | bruno | bruno jr | sold      |

  @Actualizar @HappyPath
  Scenario Outline: Actualizar una mascota con datos de formulario
    Given el actor establece la url base de la tienda de mascotas
    And existe una mascota registrada con id "<id>" y nombre "<name>"
    When el actor actualiza con formulario la mascota con id "<id>" nombre "<newName>" y estado "<newStatus>"
    Then el codigo de respuesta deberia ser 200

    Examples:
      | id    | name | newName    | newStatus |
      | 90031 | luna | luna nueva | pending   |

  @Eliminar @HappyPath
  Scenario Outline: Eliminar una mascota
    Given el actor establece la url base de la tienda de mascotas
    And existe una mascota registrada con id "<id>" y nombre "<name>"
    When el actor elimina la mascota con id "<id>"
    Then el codigo de respuesta deberia ser 200

    Examples:
      | id    | name |
      | 90041 | toby |

  @Consultar @Negativo
  Scenario Outline: Consultar una mascota que no existe
    Given el actor establece la url base de la tienda de mascotas
    When el actor consulta la mascota con el id "<id>"
    Then el codigo de respuesta deberia ser 404

    Examples:
      | id        |
      | 987654321 |

  @Avanzado @HappyPath
  Scenario Outline: Subir una imagen a una mascota (opcional)
    Given el actor establece la url base de la tienda de mascotas
    And existe una mascota registrada con id "<id>" y nombre "<name>"
    When el actor sube la imagen "<archivo>" a la mascota con id "<id>"
    Then el codigo de respuesta deberia ser 200

    Examples:
      | id    | name   | archivo     |
      | 90051 | pelusa | mascota.png |
