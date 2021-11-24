package br.com.frontendproject.support;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class JSWait {

    private JavascriptExecutor executor;

    public JSWait(final WebDriver driver) {
        this.executor = (JavascriptExecutor) driver;
        this.setWait(3);
    }

    public JSWait clickJSExecutor(final WebElement element) {
        this.executor.executeScript("arguments[0].click();", element);
        return this;
    }

    public String getText(final WebElement element) {
        return (String) this.executor.executeScript("return arguments[0].innerText;", element);
    }

    public JSWait pageJSExecutor(final String coordenate) {
        this.executor.executeScript("window.scrollBy(0," + coordenate + ")");
        return this;
    }

    public JSWait moveToElementJSExecutor(final WebElement element) {
        this.executor.executeScript("arguments[0].scrollIntoView(true);", element);
        return this;
    }

    public JSWait setWait(final int timeOut) {
        try {
            Thread.sleep(timeOut * 1000);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return this;
    }

}
