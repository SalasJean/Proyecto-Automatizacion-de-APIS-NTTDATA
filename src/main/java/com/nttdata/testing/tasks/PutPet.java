package com.nttdata.testing.tasks;

import com.nttdata.testing.models.Pet;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Put;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Task: PUT /pet
 * Actualiza una mascota existente (envia el objeto Pet completo en JSON).
 */
public class PutPet implements Task {

    private final String id;
    private final String name;
    private final String status;

    public PutPet(String id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public static PutPet withParams(String id, String name, String status) {
        return instrumented(PutPet.class, id, name, status);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        Pet pet = Pet.from(id, name, status);
        actor.attemptsTo(
                Put.to("/pet")
                        .with(request -> request
                                .contentType(ContentType.JSON)
                                .body(pet))
        );
    }
}
