package com.nttdata.testing.tasks;

import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import static net.serenitybdd.screenplay.Tasks.instrumented;

//actualiza con formulario como muestra el contrato de la documentacion
public class PostPetWithForm implements Task {

    private final String petId;
    private final String name;
    private final String status;

    public PostPetWithForm(String petId, String name, String status) {
        this.petId = petId;
        this.name = name;
        this.status = status;
    }

    public static PostPetWithForm withParams(String petId, String name, String status) {
        return instrumented(PostPetWithForm.class, petId, name, status);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Post.to("/pet/{petId}")
                        .with(request -> request
                                .header("Accept", "application/json")
                                .contentType(ContentType.URLENC)
                                .pathParam("petId", petId)
                                .formParam("name", name)
                                .formParam("status", status))
        );
    }
}
