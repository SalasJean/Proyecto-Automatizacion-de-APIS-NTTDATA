package com.nttdata.testing.stepDefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

//aqui preparamos el escenario recuerdaloo
public class ParameterDefinition {

    @Before
    public void setTheStage() {
        OnStage.setTheStage(new OnlineCast());
    }

    @ParameterType("el actor|[A-Za-z]+")
    public Actor actor(String actorName) {
        return OnStage.theActorCalled(actorName);
    }
}
