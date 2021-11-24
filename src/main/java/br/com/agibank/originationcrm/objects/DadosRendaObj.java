package br.com.frontendproject.objects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DadosRendaObj {

    @FindBy(id = "grossIncome")
    public WebElement rendaBrutaInput;
    @FindBy(id = "patrimony")
    public WebElement patrimonioInput;

    public DadosRendaObj(final WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
}
