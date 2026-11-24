package com.nttdata.testing;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

/**
 * Punto de arranque: ejecuta los .feature con Cucumber + Serenity (JUnit).
 * Apunta a src/test/resources/features y al paquete com.nttdata.testing (glue).
 */
@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        plugin = {"pretty"},
        features = "src/test/resources/features",
        glue = "com.nttdata.testing",
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class RunnerTest {
}
