package com.nttdata.testing.tasks;

import com.nttdata.testing.models.Pet;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Put;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class PutPet implements Task {

    private final Pet pet;
    private final String rawJson;
    //estamos usando isntancia de los models en forma de objeto
    public PutPet(Pet pet) {
        this.pet = pet;
        this.rawJson = null;
    }

    public PutPet(String rawJson) {
        this.pet = null;
        this.rawJson = rawJson;
    }

    public static PutPet withParams(String id, String name, String status) {
        Pet pet = Pet.from(id, name, status);
        return instrumented(PutPet.class, pet);
    }

    public static PutPet withCustomBody(String rawJson) {
        return instrumented(PutPet.class, rawJson);
    }

    public static PutPet withInvalidPhotoUrls(String id, String name, String status, String photoUrls) {
        String json = String.format(
                "{\"id\": %s, \"name\": \"%s\", \"photoUrls\": \"%s\", \"status\": \"%s\"}",
                id, name, photoUrls, status
        );
        return instrumented(PutPet.class, json);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Put.to("/pet")
                        .with(request -> {
                            request.header("Accept", "application/json")
                                   .contentType(ContentType.JSON);
                            if (rawJson != null) {
                                request.body(rawJson);
                            } else {
                                request.body(pet);
                            }
                            return request;
                        })
        );
    }
}
