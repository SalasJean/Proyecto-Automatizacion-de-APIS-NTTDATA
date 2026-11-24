package com.nttdata.testing.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Task: GET /pet/{petId}
 * Consulta una mascota por su id.
 */
public class GetPetById implements Task {

    private final String petId;

    public GetPetById(String petId) {
        this.petId = petId;
    }

    public static GetPetById withId(String petId) {
        return instrumented(GetPetById.class, petId);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Get.resource("/pet/{petId}")
                        .with(request -> request.pathParam("petId", petId))
        );
    }
}
