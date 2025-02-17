import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;

public class BaseTest {
    protected AndroidDriver driver;

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, "motorola motorola edge50 fusion"); // Use actual device ID
        caps.setCapability("appPackage", "com.example.bookmyslot");
        caps.setCapability("appActivity", "com.example.bookmyslot.ui.LoginActivity");// Provide APK path
        caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");

        driver = new AndroidDriver(new URL("http://localhost:4723"), caps);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public void loginInterviewer() {
    	driver.findElement(By.id("loginEmail")).sendKeys("golujaat0818@gmail.com");
        driver.findElement(By.id("loginpassword")).sendKeys("golu123");
        driver.findElement(By.id("loginButton")).click();
    }
    public void loginTagTeam() {
    	driver.findElement(By.id("loginEmail")).sendKeys("ravijeetchoudhary0818@gmail.com");
        driver.findElement(By.id("loginpassword")).sendKeys("ravi123");
        driver.findElement(By.id("loginButton")).click();
    }
}
