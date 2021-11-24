package br.com.frontendproject.objects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DadosDocumentoObj {

    @FindBy(id = "documentNumber")
    public WebElement numeroDocumentoInput;
    @FindBy(css = "[aria-label='Tipo de Documento:']")
    public WebElement tipoDocumentoInput;
    @FindBy(id = "issuingDate")
    public WebElement dataExpedicaoInput;
    @FindBy(id = "issuingAuthority")
    public WebElement orgaoExpedidorInput;
    @FindBy(id = "email")
    public WebElement emailInput;
    @FindBy(id = "phone")
    public WebElement telefoneInput;
    @FindBy(css = "[data-testid='save-button']")
    public WebElement salvarInformacoesButton;
    @FindBy(css = "[data-testid='btn-confirm']")
    public WebElement confirmarButton;
    @FindBy(id = "profession")
    public WebElement profissaoInput;
    @FindBy(className = "autocomplete-options")
    public WebElement autocompleteText;

    public DadosDocumentoObj(final WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

}
