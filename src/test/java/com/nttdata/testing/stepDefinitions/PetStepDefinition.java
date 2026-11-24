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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Traductor este convierte codigo de negocio en codigo maquina para su automatizacion
 */
public class PetStepDefinition {

    private EnvironmentVariables environmentVariables;

    @Given("{actor} establece el endpoint de pet")
    public void elActorEstableceElEndpointDePet(Actor actor) {
        if (environmentVariables == null) {
            environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
        }
        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("restapi.baseurl");
        actor.whoCan(CallAnApi.at(baseUrl));
    }

    @When("{actor} crea una mascota con id {string} nombre {string} y estado {string}")
    public void elActorCreaUnaMascota(Actor actor, String id, String name, String status) {
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

    @When("{actor} actualiza la mascota con id {string} enviando photoUrls {string} en lugar de una lista")
    public void elActorActualizaLaMascotaConPhotoUrlsInvalido(Actor actor, String id, String photoUrls) {
        actor.attemptsTo(
                PutPet.withInvalidPhotoUrls(id, "rocky", "available", photoUrls)
        );
    }

    @When("{actor} sube la imagen {string} a la mascota con id {string}")
    public void elActorSubeLaImagenALaMascota(Actor actor, String archivo, String id) {
        actor.attemptsTo(
                PostPetImage.withFile(id, archivo)
        );
    }

    @Then("el codigo de respuesta debe ser {int}")
    public void elCodigoDeRespuestaDebeSer(int expectedStatusCode) {
        assertThat(OnStage.theActorInTheSpotlight().asksFor(ResponseCode.getStatus()))
                .isEqualTo(expectedStatusCode);
    }

    @Then("el campo {string} de la respuesta debe ser {string}")
    public void elCampoDeLaRespuestaDebeSer(String field, String expectedValue) {
        Object actualValue = OnStage.theActorInTheSpotlight().asksFor(ResponseField.of(field));
        assertThat(String.valueOf(actualValue))
                .isEqualTo(expectedValue);
    }

    @Then("el header {string} de la respuesta debe ser {string}")
    public void elHeaderDeLaRespuestaDebeSer(String headerName, String expectedValue) {
        String actualHeader = OnStage.theActorInTheSpotlight().asksFor(ResponseHeader.of(headerName));
        assertThat(actualHeader)
                .contains(expectedValue);
    }

    @Then("todas las mascotas de la lista deben tener el estado {string}")
    public void todasLasMascotasDeLaListaDebenTenerElEstado(String expectedStatus) {
        Object result = OnStage.theActorInTheSpotlight().asksFor(ResponseField.of("status"));
        assertThat(result)
                .isInstanceOf(List.class);
        List<?> statuses = (List<?>) result;
        assertThat(statuses)
                .isNotEmpty()
                .allMatch(status -> expectedStatus.equals(String.valueOf(status)));
    }
}
