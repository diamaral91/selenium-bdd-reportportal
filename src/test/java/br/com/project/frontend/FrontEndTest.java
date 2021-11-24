package br.com.frontend;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        tags = {"@frontend"},
        glue = {"br/com/frontend/steps"},
        monochrome = true, dryRun = false, strict = true,
        plugin = {"pretty", "html:target/cucumber"})
public class FrontEndTest {

    public FrontEndTest() {

    }
}
