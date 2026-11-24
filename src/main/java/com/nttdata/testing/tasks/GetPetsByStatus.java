package com.nttdata.testing.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

import static net.serenitybdd.screenplay.Tasks.instrumented;


public class GetPetsByStatus implements Task {

    private final String status;
    //buscamos por estado vendido o no disponible
    public GetPetsByStatus(String status) {
        this.status = status;
    }

    public static GetPetsByStatus withStatus(String status) {
        return instrumented(GetPetsByStatus.class, status);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Get.resource("/pet/findByStatus")
                        .with(request -> request
                                .header("Accept", "application/json")
                                .queryParam("status", status))
        );
    }
}
