package com.nttdata.testing.tasks;

import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Delete;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

import static net.serenitybdd.screenplay.Tasks.instrumented;

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
        EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
        String apiKey = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("restapi.api_key");

        actor.attemptsTo(
                Delete.from("/pet/{petId}")
                        .with(request -> {
                            request.header("Accept", "application/json")
                                   .pathParam("petId", petId);
                            if (apiKey != null && !apiKey.trim().isEmpty()) {
                                request.header("api_key", apiKey);
                            }
                            return request;
                        })
        );
    }
}
