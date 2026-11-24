package com.nttdata.testing.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

/**
 * Question: lee el codigo de estado HTTP de la ultima respuesta.
 * Se reutiliza en todos los escenarios.
 */
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

    //recuerda nunca devemos olvidar que el trabajo de ResponseCode es de sellar el sobre para evitar problemas de seguridad es el que nos da el codigo de estado
    //osea desde el 100 hasta el 500
    //aqui no solo deve existir el response code sino tambien el response header ya que cada api trae headers nisiquiera headers sino scrips tokens y demas
    //para ello y ver mas de eso ir a postman para verlo si?
    //ahora no todo deveria de ir en el response code sino tambien en la clase ResponseField
    //que es lo que va en el responsefield?
    //pues en este response field va los datos del body en formato json o en los datos que que el cliente tenga recuerda no solo deven o podrian ser en json no lo olvides porfavor si?

    //en resumen esto funciona asi:
    /*
    El  response code se encarga de leer el codigo de estado de la ultima respuesta que dio la api su principal trabajo es simplemente si esto salio bien o no
    solo comprueba estas dos cosas salio bien o no nada mas
    y cual es la chamba de la clase ResponseHeader?  su trabajo de este es solo leer un header en concreto por ejemplo el content type que responde?
    pues responde simplemente en que formato viene la respuesta?
    ahora nos preguntas si responde code verifica los estados u responseHeader verifica el contenct Type
    cual es su trabajo del RESPONSEFIELD?
     pues este lle el dato dentro del json como name status revifica si vino el dato correcto o no si? ese es su trabajo
     RECUERDA QUE ESTAS TRES CLASES SOLO LEEN NADA MAS NO ENVIAN NADA NI TAMPOCO COMPARAR NAA
     AHORA QUE HACE LA CLASE PET? LA CLASE PET SOLO ACTUA COMO UN MOLDE
     OSEA ES MASOMENTOS UN DTO, OSEA YA QUE SE TIENE DATOS DEFINIDOS SE USA ESTE PARA USARLO COMO MOLDE
     EVITA EL CODIGO REPETITIVO OSEA EN LA CONSTRUCCION DEL JSON YA QUE VARIOS METODOS O CLASES USAN LA MISMA CONSTRUCCION
     este garantiza solo lo obligatorio, osea el contrato exige por ejemplo name y phootourls y este molde solo asegura de que seimpre vayan
     esto nos permite la escalabilidad y el desacoplamiento si otro dia los requerimientos de los campos cambian no lo olvides si?
    * */
}
