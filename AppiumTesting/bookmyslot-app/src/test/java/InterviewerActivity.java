import static org.junit.Assert.assertEquals;

import java.time.Duration;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
public class InterviewerActivity extends BaseTest {
	
	
	@Test
	//enter interviewer detail, slot and submit
	public void EnterSlotDeatil() throws Exception {
		loginInterviewer();
		Thread.sleep(3000);
		driver.findElement(By.id("specialization")).sendKeys("Java");
		driver.findElement(By.id("picDate")).click();
		driver.findElement(MobileBy.xpath("//*[@text='20']")).click(); // Selects 16th Feb
		driver.findElement(MobileBy.xpath("//*[@text='OK']")).click(); 
		driver.findElement(By.id("starttime")).click();
		Thread.sleep(2000);//manually select start time
		driver.findElement(MobileBy.xpath("//*[@text='OK']")).click();
		driver.findElement(By.id("endtime")).click();
		Thread.sleep(2000); //manually select end time
		driver.findElement(MobileBy.xpath("//*[@text='OK']")).click();
		driver.findElement(By.id("email")).sendKeys("golujaat0818@gmail.com");
		driver.findElement(By.id("addslotbtn")).click();
		Thread.sleep(5000);
		driver.findElement(By.id("submitbtn")).click();
		
		
        WebElement toast1 = driver.findElement(MobileBy.xpath("//android.widget.Toast"));
        
        assertEquals("Slots updated successfully", toast1.getText());
		
	}
	
	@Test
	public void ViewSlots() throws Exception {
		loginInterviewer();
		Thread.sleep(3000);
		driver.findElement(By.id("seeyourslot")).click();
		WebElement viewslots = driver.findElement(By.id("recycleViewSlots"));
		if(viewslots.isDisplayed()) {
			System.out.println("Successfully navigates to View slots ");
		}else {
			System.out.println("Not navigates to View slots ");
		}
	}
	@After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
	
	
}
