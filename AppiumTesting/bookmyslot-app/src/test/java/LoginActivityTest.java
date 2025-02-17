import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;

public class LoginActivityTest {
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
    

    @Test
    public void testLoginWithValidDataOfInterviewer() throws Exception {
        // Locate and interact with UI elements using resource-id or xpath
        driver.findElement(By.id("com.example.bookmyslot:id/loginEmail")).sendKeys("golujaat0818@gmail.com");
        driver.findElement(By.id("com.example.bookmyslot:id/loginpassword")).sendKeys("golu123");
        driver.findElement(By.id("com.example.bookmyslot:id/loginButton")).click();
        
        Thread.sleep(5000);

        // Verify successful login with interviewer valid credentials
        WebElement specialization=driver.findElement(By.id("com.example.bookmyslot:id/specialization"));
        if(specialization.isDisplayed()) {
        	System.out.println("Successfully login with interviewer login credentials and navigates to interviewer app");
        }else {
        	System.out.println("login failed with valid credentials");
        }
    }
    
    @Test
    public void testLoginWithValidDataOfTagTeam() throws Exception {
       
        driver.findElement(By.id("com.example.bookmyslot:id/loginEmail")).sendKeys("ravijeetchoudhary0818@gmail.com");
        driver.findElement(By.id("com.example.bookmyslot:id/loginpassword")).sendKeys("ravi123");
        driver.findElement(By.id("com.example.bookmyslot:id/loginButton")).click();
        
        Thread.sleep(5000);

        // Verify successful login with tag team valid credentials
        WebElement title=driver.findElement(By.id("com.example.bookmyslot:id/tvTitle"));
        if(title.isDisplayed()) {
        	System.out.println("Successfully login with tag team login credentials and navigates to tag team app");
        }else {
        	System.out.println("Not able to open app for tag team");
        }
    }
    
    @Test
    public void testLoginWithInavlidData() throws InterruptedException {
    	driver.findElement(By.id("com.example.bookmyslot:id/loginEmail")).sendKeys("golujaat@gmail.com");
        driver.findElement(By.id("com.example.bookmyslot:id/loginpassword")).sendKeys("golu1234");
        driver.findElement(By.id("com.example.bookmyslot:id/loginButton")).click();
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        WebElement toast = wait.until(ExpectedConditions.presenceOfElementLocated(
                MobileBy.xpath("//android.widget.Toast")
            ));
        
        assertEquals("Authentication failed due to invalid credentials", toast.getText());
    }
    
    
    @Test
    public void testforgotpasswordfunctionality() {
    	driver.findElement(By.id("com.example.bookmyslot:id/forgotPassword")).click();
    	WebElement dialogbox=driver.findElement(By.id("com.example.bookmyslot:id/forgotTitle"));
    	if(dialogbox.isDisplayed()) {
    		System.out.println("dialog box is apper on clicking forgot password");
    	}
    	
    }
    @Test
    public void NotRegisterYetfunctionalities()  {
    	driver.findElement(By.id("com.example.bookmyslot:id/signupText")).click();
    	WebElement signup=driver.findElement(By.id("com.example.bookmyslot:id/signupName"));
        if(signup.isDisplayed()) {
        	System.out.println("Successfully navigates to register activity");
        }
    	
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
