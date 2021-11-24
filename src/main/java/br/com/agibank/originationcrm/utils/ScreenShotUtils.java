package br.com.frontendproject.utils;

import io.cucumber.java.Scenario;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.io.File;
import java.io.IOException;

public class ScreenShotUtils {

    private WebDriver driver;

    public ScreenShotUtils(final WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public byte[] captureEvidence() {
        return ((TakesScreenshot) this.driver).getScreenshotAs(OutputType.BYTES);
    }

    public void saveImage(final Scenario scenario) {
        final String filePath = "target/screenshot/" + scenario.getName() + ".png";
        try {
            final File screenshot = ((TakesScreenshot) this.driver)
                    .getScreenshotAs(OutputType.FILE);
            final File file = new File(filePath);
            FileUtils.copyFile(screenshot, file);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

}
