package com.nttdata.testing.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

/**
 * Question: lee un campo del body JSON de la ultima respuesta usando una ruta JSON.
 * Ejemplos: "name", "status" (si el body es una lista, devuelve la lista de valores).
 */
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
