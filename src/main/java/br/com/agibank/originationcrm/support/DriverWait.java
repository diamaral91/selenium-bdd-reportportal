package br.com.frontendproject.support;

import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class DriverWait {

    private WebDriver driver;
    private int timeOut = 30;

    public DriverWait(final WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    private Wait<WebDriver> setWait(){
        return new FluentWait<>(this.driver)
                .withTimeout(Duration.ofSeconds(this.timeOut))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementClickInterceptedException.class);
    }

    public WebElement waitForClickableButton(final WebElement element){
        return this.setWait().until(ExpectedConditions.elementToBeClickable(element));
    }

    public WebElement waitButtonPopUp(final WebElement element){
        return new WebDriverWait(this.driver, 5).until(ExpectedConditions.elementToBeClickable(element));
    }

    public WebElement waitForVisibleElement(final WebElement element){
        return this.setWait().until(ExpectedConditions.visibilityOf(element));
    }

    public boolean waitForPresentText(final WebElement element, final String text){
        return this.setWait().until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    public WebDriver waitIframe(final WebElement element){
        return this.setWait().until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(element));
    }

    public WebElement waitElementInListByText(final List<WebElement> elements, final String text){
        this.waitElementList(elements);
        for(final WebElement element : elements){
            this.setWait(1);
            if(text.equalsIgnoreCase(element.getText())){
                return this.setWait().until(ExpectedConditions.elementToBeClickable(element));
            }
        }
        throw new RuntimeException(text + " não encontrado");
    }

    public List<WebElement> waitElementList(final List<WebElement> elements) {
        return this.setWait().until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    public DriverWait setWait(final int timeOut){
        try {
            Thread.sleep(timeOut * 1000);
        } catch (final Exception e){
            e.printStackTrace();
        }
        return this;
    }
}
