package br.com.frontendproject.support;

import br.com.frontendproject.apis.frontendprojectServiceAPI;
import br.com.frontendproject.utils.ScreenShotUtils;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static br.com.frontendproject.utils.PropertieUtils.getValue;

public class TestBase {

    static Logger log = Logger.getLogger(TestBase.class.getName());

    private WebDriver driver;
    private String cpf;
    private String produto;
    private String email;
    private String codUrl;

    public TestBase() {
        this.driver = new Browsers().setupSeleniumGrid();

        this.driver.manage().window().maximize();
        this.driver.get(getValue("urlBase"));

        this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public WebDriver getDriver() {
        return this.driver;
    }

    public String getCpf() {
        return this.cpf;
    }

    public void setCpf(final String cpf) {
        this.cpf = cpf;
    }

    public String getProduto() {
        return this.produto;
    }

    public void setProduto(final String produto) {
        this.produto = produto;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getCodUrl() {
        return this.codUrl;
    }

    public void setCodUrl(final String codUrl) {
        this.codUrl = codUrl;
    }

    public void tearDown(final Scenario scenario) {
        final byte[] screenshot = new ScreenShotUtils(this.driver).captureEvidence();
        scenario.attach(screenshot, "image/png", scenario.getName());

        this.driver.quit();

        final String atendimento = new frontendprojectServiceAPI().getCustomersDocumentType(this.getCpf(), "number");
        log.info("* * * NUMERO ATENDIMENTO: " + atendimento + " * * *");

        final String frontend = new frontendprojectServiceAPI().getCustomersDocumentType(this.getCpf(), "origination.id");
        log.info("* * * NUMERO frontend: " + frontend + " * * *");
    }

}
