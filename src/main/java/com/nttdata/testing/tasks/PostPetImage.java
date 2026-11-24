package com.nttdata.testing.tasks;

import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.io.File;
import java.net.URL;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class PostPetImage implements Task {

    private final String petId;
    private final String fileName;
    private final String additionalMetadata;

    public PostPetImage(String petId, String fileName, String additionalMetadata) {
        this.petId = petId;
        this.fileName = fileName;
        this.additionalMetadata = additionalMetadata;
    }

    public static PostPetImage withFile(String petId, String fileName) {
        return instrumented(PostPetImage.class, petId, fileName, null);
    }

    public static PostPetImage withFileAndMetadata(String petId, String fileName, String additionalMetadata) {
        return instrumented(PostPetImage.class, petId, fileName, additionalMetadata);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        File file = new File("src/test/resources/files/" + fileName);
        if (!file.exists()) {
            URL resource = getClass().getClassLoader().getResource("files/" + fileName);
            if (resource != null) {
                file = new File(resource.getFile());
            }
        }

        File finalFile = file;
        actor.attemptsTo(
                Post.to("/pet/{petId}/uploadImage")
                        .with(request -> {
                            request.header("Accept", "application/json")
                                   .contentType(ContentType.MULTIPART)
                                   .pathParam("petId", petId)
                                   .multiPart("file", finalFile);
                            if (additionalMetadata != null) {
                                request.formParam("additionalMetadata", additionalMetadata);
                            }
                            return request;
                        })
        );
    }
}
