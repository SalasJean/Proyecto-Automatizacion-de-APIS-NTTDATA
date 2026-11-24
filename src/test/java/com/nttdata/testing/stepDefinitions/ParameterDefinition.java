package com.nttdata.testing.stepDefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

/**
 * Define los parametros personalizados de Cucumber y la configuracion del escenario.
 */
public class ParameterDefinition {

    @Before
    public void setTheStage() {
        OnStage.setTheStage(new OnlineCast());
    }

    @ParameterType(".+")
    public Actor actor(String actorName) {
        return OnStage.theActorCalled(actorName);
    }
}
