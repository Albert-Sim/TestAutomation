package Steps;

import Base.BaseUtil;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Hook extends BaseUtil {

    private BaseUtil base;
    private ExtentTest test;
    private static Boolean first = false;
    private static String TestStartTime;
    private ExtentHtmlReporter htmlReporter;
    private ExtentReports extent;

    public Hook(BaseUtil base) {
        this.base = base;
    }

    @Before
    public void Setup(Scenario scenario) {
        System.out.println("Test set up");

        if (!first) {
            TestStartTime = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(Calendar.getInstance().getTime());
            htmlReporter = new ExtentHtmlReporter("C:\\TestAutomation\\result\\" + TestStartTime + "\\ExtentReport.html");
            htmlReporter.setAppendExisting(true);
            extent = new ExtentReports();
            extent.attachReporter(htmlReporter);
            first = true;
        }

        this.test = extent.createTest(scenario.getName());
        for (String tag: scenario.getSourceTagNames()) {
            this.test.assignCategory(tag);
        }

        InitializeChromeDriver();
    }

    @After
    public void TearDown(Scenario scenario) {

        if (scenario.isFailed()) {
            htmlReporter.loadXMLConfig(new File("C:\\TestAutomation\\extent-config.xml"));
            try {
                base.Driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
                File src = ((TakesScreenshot)base.Driver).getScreenshotAs(OutputType.FILE);
                String ScreenshotFile = "C:\\TestAutomation\\result\\" + TestStartTime + "\\screenshots\\" + scenario.getName() + ".png";
                FileUtils.copyFile(src, new File(ScreenshotFile));
                this.test.fail(scenario.getName()).addScreenCaptureFromPath(ScreenshotFile);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            this.test.pass(scenario.getName());
        }
        extent.flush();

        base.Driver.close();
    }

    private void InitializeChromeDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\TestAutomation\\chromedriver.exe");
        base.Driver = new ChromeDriver();
        base.Driver.manage().window().maximize();
    }
}
