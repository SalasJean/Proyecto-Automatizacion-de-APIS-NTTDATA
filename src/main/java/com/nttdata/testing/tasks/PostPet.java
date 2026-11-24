package com.nttdata.testing.tasks;

import com.nttdata.testing.models.Pet;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class PostPet implements Task {

    private final String id;
    private final String name;
    private final String status;

    public PostPet(String id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public static PostPet withParams(String id, String name, String status) {
        return instrumented(PostPet.class, id, name, status);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        Pet pet = Pet.from(id, name, status);
        actor.attemptsTo(
                Post.to("/pet")
                        .with(request -> request
                                .header("Accept", "application/json")
                                .contentType(ContentType.JSON)
                                .body(pet))
        );
    }
}
