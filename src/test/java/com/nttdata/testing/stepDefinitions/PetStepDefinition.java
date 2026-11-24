package com.nttdata.testing.stepDefinitions;

import com.nttdata.testing.questions.ResponseCode;
import com.nttdata.testing.questions.ResponseField;
import com.nttdata.testing.questions.ResponseHeader;
import com.nttdata.testing.tasks.DeletePet;
import com.nttdata.testing.tasks.GetPetById;
import com.nttdata.testing.tasks.GetPetsByStatus;
import com.nttdata.testing.tasks.PostPet;
import com.nttdata.testing.tasks.PostPetImage;
import com.nttdata.testing.tasks.PostPetWithForm;
import com.nttdata.testing.tasks.PutPet;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

import java.util.List;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

/**
 * Traductor de las frases de APIPet.feature hacia las Tasks y Questions de Screenplay.
 */
public class PetStepDefinition {

    private EnvironmentVariables environmentVariables;

    @Given("{actor} establece la url base de la tienda de mascotas")
    public void elActorEstableceLaUrlBaseDeLaTiendaDeMascotas(Actor actor) {
        if (environmentVariables == null) {
            environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
        }
        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("restapi.baseurl");
        actor.whoCan(CallAnApi.at(baseUrl));
    }

    @Given("existe una mascota registrada con id {string} y nombre {string}")
    public void existeUnaMascotaRegistradaConIdYNombre(String id, String name) {
        OnStage.theActorInTheSpotlight().attemptsTo(
                PostPet.withParams(id, name, "available")
        );
    }

    @When("{actor} envia una solicitud POST para crear la mascota con id {string} nombre {string} y estado {string}")
    public void elActorEnviaUnaSolicitudPostParaCrearLaMascota(Actor actor, String id, String name, String status) {
        actor.attemptsTo(
                PostPet.withParams(id, name, status)
        );
    }

    @When("{actor} consulta la mascota con el id {string}")
    public void elActorConsultaLaMascotaConElId(Actor actor, String id) {
        actor.attemptsTo(
                GetPetById.withId(id)
        );
    }

    @When("{actor} consulta las mascotas con estado {string}")
    public void elActorConsultaLasMascotasConEstado(Actor actor, String status) {
        actor.attemptsTo(
                GetPetsByStatus.withStatus(status)
        );
    }

    @When("{actor} actualiza la mascota con id {string} nombre {string} y estado {string}")
    public void elActorActualizaLaMascota(Actor actor, String id, String newName, String newStatus) {
        actor.attemptsTo(
                PutPet.withParams(id, newName, newStatus)
        );
    }

    @When("{actor} actualiza con formulario la mascota con id {string} nombre {string} y estado {string}")
    public void elActorActualizaConFormularioLaMascota(Actor actor, String id, String newName, String newStatus) {
        actor.attemptsTo(
                PostPetWithForm.withParams(id, newName, newStatus)
        );
    }

    @When("{actor} elimina la mascota con id {string}")
    public void elActorEliminaLaMascota(Actor actor, String id) {
        actor.attemptsTo(
                DeletePet.withId(id)
        );
    }

    @When("{actor} sube la imagen {string} a la mascota con id {string}")
    public void elActorSubeLaImagenALaMascota(Actor actor, String archivo, String id) {
        actor.attemptsTo(
                PostPetImage.withFile(id, archivo)
        );
    }

    @Then("el codigo de respuesta deberia ser {int}")
    public void elCodigoDeRespuestaDeberiaSer(int expectedStatusCode) {
        OnStage.theActorInTheSpotlight().should(
                seeThat("el codigo de respuesta", ResponseCode.getStatus(), equalTo(expectedStatusCode))
        );
    }

    @Then("el campo {string} de la respuesta deberia ser {string}")
    public void elCampoDeLaRespuestaDeberiaSer(String field, String expectedValue) {
        OnStage.theActorInTheSpotlight().should(
                seeThat("el campo " + field, ResponseField.of(field), hasToString(expectedValue))
        );
    }

    @Then("el header {string} de la respuesta deberia ser {string}")
    public void elHeaderDeLaRespuestaDeberiaSer(String headerName, String expectedValue) {
        OnStage.theActorInTheSpotlight().should(
                seeThat("el header " + headerName, ResponseHeader.of(headerName), containsString(expectedValue))
        );
    }

    @Then("todas las mascotas de la lista deberian tener el estado {string}")
    public void todasLasMascotasDeLaListaDeberianTenerElEstado(String expectedStatus) {
        Object result = OnStage.theActorInTheSpotlight().asksFor(ResponseField.of("status"));
        assertThat(result)
                .isInstanceOf(List.class);
        List<?> statuses = (List<?>) result;
        assertThat(statuses)
                .isNotEmpty()
                .allMatch(status -> expectedStatus.equals(String.valueOf(status)));
    }
}
