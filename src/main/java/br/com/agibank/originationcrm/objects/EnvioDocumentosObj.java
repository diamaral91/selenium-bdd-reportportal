package br.com.frontendproject.objects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class EnvioDocumentosObj {

    @FindBy(xpath = "//h1[text()='Envio de documentos']")
    public WebElement envioDeDocumentosText;
    @FindBy(xpath = "//div[contains(@class,'centralize-radio-circle')]//h1")
    public List<WebElement> metodoCadastroDocumentosCheckbox;
    @FindBy(css = "[data-testid='button-continue']")
    public WebElement continuarButton;
    @FindBy(xpath = "//h1[text()='Ocorreu um erro inesperado']")
    public WebElement alertaText;
    @FindBy(xpath = "//button/span[text()='Entendi']")
    public WebElement entendiButton;
    @FindBy(css = "[title='Simulador']")
    public WebElement simuladorFrame;

    public EnvioDocumentosObj(final WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
}
