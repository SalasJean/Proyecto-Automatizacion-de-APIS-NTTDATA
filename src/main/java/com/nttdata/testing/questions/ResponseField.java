package com.nttdata.testing.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class ResponseField implements Question<Object> {

    private final String fieldPath;

    public ResponseField(String fieldPath) {
        this.fieldPath = fieldPath;
    }

    public static ResponseField of(String fieldPath) {
        return new ResponseField(fieldPath);
    }

    @Override
    public Object answeredBy(Actor actor) {
        return SerenityRest.lastResponse().jsonPath().get(fieldPath);
    }
}
