package com.nttdata.testing.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Delete;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Task: DELETE /pet/{petId}
 * Elimina una mascota por su id.
 */
public class DeletePet implements Task {

    private final String petId;

    public DeletePet(String petId) {
        this.petId = petId;
    }

    public static DeletePet withId(String petId) {
        return instrumented(DeletePet.class, petId);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Delete.from("/pet/{petId}")
                        .with(request -> request
                                .header("api_key", "special-key")
                                .pathParam("petId", petId))
        );
    }
}
