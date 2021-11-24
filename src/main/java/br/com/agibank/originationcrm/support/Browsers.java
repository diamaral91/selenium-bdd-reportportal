package br.com.frontendproject.support;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class Browsers {

    public WebDriver setupChromeDriver() {
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver();
    }

    public WebDriver setupSeleniumGrid() {
        URL urlGrid = null;
        ChromeOptions chromeOptions = null;
        try {
            urlGrid = new URL("http://selenium-hub-infraestrutura.agibank-hom.in:80/wd/hub");
            chromeOptions = new ChromeOptions();
            chromeOptions.setCapability("platformName", "LINUX");
            chromeOptions.setCapability("browserName", "chrome");
            chromeOptions.addArguments("--start-maximized");
        } catch (final MalformedURLException e) {
            e.printStackTrace();
        }
        return new RemoteWebDriver(urlGrid, chromeOptions);
    }

}
