package Steps;

import Base.BaseUtil;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class ArticleStep extends BaseUtil {

    private BaseUtil base;

    public ArticleStep(BaseUtil base) {
        this.base = base;
    }

    @Given("^I navigate to url straits times home page$")
    public void iNavigateToUrlStraitstimesHomePage() throws Throwable {
        base.Driver.navigate().to("http://www.straitstimes.com/");
        base.Driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
    }

    @And("^close advertisement pop out$")
    public void closeAdvertisementPopOut() throws Throwable {
        //base.Driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        base.Driver.switchTo().frame(base.Driver.findElement(By.xpath("//iframe[contains(@id,'_expand_iframe_')]")));
        base.Driver.findElement(By.xpath("//button[@id='close-button']")).click();
    }

    @And("^I click on login button$")
    public void iClickOnLoginButton() throws Throwable {
        base.Driver.findElement(By.xpath("//a[text()='Login']")).click();
    }

    @And("^I enter ID and password$")
    public void iEnterIDAndPassword() throws Throwable {
        File XmlFile = new File("C:\\TestAutomation\\TestConfig.xml");
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(XmlFile);
        String username = doc.getElementsByTagName("username").item(0).getTextContent();
        String password = doc.getElementsByTagName("password").item(0).getTextContent();
        base.Driver.findElement(By.xpath("//input[@id='j_username']")).sendKeys(username);
        base.Driver.findElement(By.xpath("//input[@id='j_password']")).sendKeys(password);
    }

    @When("^I click sign in button$")
    public void iClickSignInButton() throws Throwable {
        base.Driver.findElement(By.xpath("//button[@class='btn' and text()='Sign in!']")).click();
    }

    @Then("^I should see the home page and my ID displayed$")
    public void iShouldSeeTheHomePageAndMyIDDisplayed() throws Throwable {
        File XmlFile = new File("C:\\TestAutomation\\TestConfig.xml");
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(XmlFile);
        String username = doc.getElementsByTagName("username").item(0).getTextContent();
        WebDriverWait wait = new WebDriverWait(base.Driver, 15);
        wait.until(w -> ((JavascriptExecutor) base.Driver).executeScript("return (window.jQuery != null) && (jQuery.active === 0);"));
        base.Driver.findElement(By.xpath("//a[@name='login-user-name' and text()='" + username + "']"));
        //Assert.assertEquals("Username is not showing on home page", true, );
    }

    @And("^I should also see picture attached for main article$")
    public void iShouldAlsoSeePictureAttachedForMainArticle() throws Throwable {
        Assert.assertEquals("Image or video source is not found",true, ArticleStep.verifyIMGActive(base.Driver.findElement(By.xpath("//div[@data-vr-zone='Top Stories 0']//img")),"src"));
    }

    @And("^I click on main article$")
    public void iClickOnMainArticle() throws Throwable {
        base.Driver.findElement(By.xpath("//div[@data-vr-zone='Top Stories 0']")).click();
    }

    @Then("^I should see picture or video displayed in the article$")
    public void iShouldSeePictureOrVideoDisplayedInTheArticle() throws Throwable {
        Assert.assertEquals("Image or video source is not found",true, ArticleStep.verifyIMGActive(base.Driver.findElement(By.xpath("//div[@class='media-group fadecount0']//img")),"srcset"));
    }

    @And("^I logout to prevent account lock for another login$")
    public void iLogoutToPreventAccountLockForAnotherLogin() throws Throwable {
        base.Driver.findElement(By.xpath("//a[text()='Logout']")).click();
        WebDriverWait wait = new WebDriverWait(base.Driver, 30);
        wait.until(w -> ((JavascriptExecutor) base.Driver).executeScript("return (window.jQuery != null) && (jQuery.active === 0);"));
    }

    private static boolean verifyIMGActive(WebElement img, String attribute) {
        try {
            HttpResponse response = HttpClientBuilder.create().build().execute(new HttpGet(img.getAttribute(attribute).trim()));
            System.out.println(response.getStatusLine().getStatusCode());
            return response.getStatusLine().getStatusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
