package com.nttdata.testing.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class ResponseCode implements Question<Integer> {

    public static ResponseCode was() {
        return new ResponseCode();
    }

    public static ResponseCode getStatus() {
        return new ResponseCode();
    }

    @Override
    public Integer answeredBy(Actor actor) {
        return SerenityRest.lastResponse().statusCode();
    }

    //aqui devemos preguntarnos que hace el response code? para que sirve? y que funcion cumple?
    //recuerda que cada empresa tiene su propia forma de trabajar tanto en formacion a los calichines tanto en arquitectura no lo olvides porfavor

    //recuerda nunca devemos olvidar que el trabajo de ResponseCode: lee el codigo de estado HTTP de la ultima respuesta (200, 404...).
    // * Solo lee; la comparacion con el valor esperado se hace en el step definition.
    //aqui no solo deve existir el response code sino tambien el response header ya que cada api trae headers nisiquiera headers sino scrips tokens y demas
    //para ello y ver mas de eso ir a postman para verlo si?
    //ahora no todo deveria de ir en el response code sino tambien en la clase ResponseField
    //que es lo que va en el responsefield?
    //pues en este response field va los datos del body en formato json o en los datos que que el cliente tenga recuerda no solo deven o podrian ser en json no lo olvides porfavor si?

    //en resumen esto funciona asi:
    /**
     * ResponseCode: lee el codigo de estado HTTP de la ultima respuesta (200, 404...).
     * Solo lee; la comparacion con el valor esperado se hace en el step definition.
     */

    /**
     * ResponseHeader: lee un header concreto de la ultima respuesta
     * (por ejemplo Content-Type, que indica el formato del cuerpo).
     */

    /**
     * ResponseField: lee un campo del cuerpo JSON de la ultima respuesta
     * mediante una ruta JSON (por ejemplo "name" o "status").
     */
}
