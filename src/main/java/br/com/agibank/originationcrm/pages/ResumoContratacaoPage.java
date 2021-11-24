package br.com.frontendproject.pages;

import br.com.frontendproject.objects.ResumoContratacaoObj;
import br.com.frontendproject.support.DriverWait;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class ResumoContratacaoPage {

    private DriverWait wait;
    private ResumoContratacaoObj resumoContratacao;

    public ResumoContratacaoPage(final WebDriver driver) {
        this.wait = new DriverWait(driver);
        this.resumoContratacao = new ResumoContratacaoObj(driver);
        PageFactory.initElements(driver, this);
    }

    public void continuar() {
        this.wait.waitForClickableButton(this.resumoContratacao.continuarButton).click();
    }

}
