package com.nttdata.testing.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class ResponseHeader implements Question<String> {

    private final String headerName;

    public ResponseHeader(String headerName) {
        this.headerName = headerName;
    }

    public static ResponseHeader of(String headerName) {
        return new ResponseHeader(headerName);
    }

    @Override
    public String answeredBy(Actor actor) {
        return SerenityRest.lastResponse().getHeader(headerName);
    }
}
