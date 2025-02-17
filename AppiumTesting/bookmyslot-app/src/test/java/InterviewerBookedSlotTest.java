import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import io.appium.java_client.MobileBy;

public class InterviewerBookedSlotTest extends BaseTest {
	
	@Test
	public void CheckSlotDetailsTest() throws Exception{
		loginInterviewer();
		Thread.sleep(3000);
		
		driver.findElement(By.id("seeyourslot")).click();
		
		WebElement details=driver.findElement(By.id("slotDetails"));
		if(details.isDisplayed()) {
			System.out.println("details: "+ details.getText());
		}
		
	}
	
	@Test
	public void EditSlotButtonTest() throws InterruptedException {
		loginInterviewer();
		Thread.sleep(3000);
		driver.findElement(By.id("seeyourslot")).click();
		driver.findElement(By.id("editButton")).click();
		Thread.sleep(3000);//manually change value
		driver.findElement(MobileBy.xpath("//*[@text='UPDATE']")).click();//click to update in firebasestore
	}
	@Test
	public void DeleteSlotButtonTest() throws Exception {
		loginInterviewer();
		Thread.sleep(3000);
		driver.findElement(By.id("seeyourslot")).click();
		driver.findElement(By.id("deleteButton")).click();
        WebElement toast7 = driver.findElement(MobileBy.xpath("//android.widget.Toast"));
        
        assertEquals("Slot removed successfully", toast7.getText());
	}
	@After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
