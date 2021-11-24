package br.com.frontendproject.objects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DocumentosDistanciaObj {

    @FindBy(xpath = "//h1[text()='Documentos à distância']")
    public WebElement documentosDistanciaText;
    @FindBy(css = ".qrcode")
    public WebElement qrcodeImg;
    @FindBy(xpath = "//span[text()='Entendi']")
    public WebElement entendiButton;
    @FindBy(xpath = "//h1[text()='Dados da simulação']/parent::div/p")
    public WebElement dadosSimulacaoText;
    @FindBy(xpath = "//span[text()='Reenviar mensagem']")
    public WebElement reenviarMensagemLink;
    @FindBy(xpath = "//span[text()='Continuar']")
    public WebElement continuarButton;
    @FindBy(css = "[title='Simulador']")
    public WebElement simuladorFrame;
    @FindBy(xpath = "//span[text()='Copiar link']")
    public WebElement copiarLink;

    public DocumentosDistanciaObj(final WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

}
