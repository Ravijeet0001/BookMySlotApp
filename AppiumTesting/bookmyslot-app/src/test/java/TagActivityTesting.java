import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import io.appium.java_client.MobileBy;

public class TagActivityTesting extends BaseTest{
	
	@Test
	public void checkInterviewerSlotIsVisible() throws InterruptedException {
		loginTagTeam();
		Thread.sleep(3000);
		WebElement email=driver.findElement(By.id("intemail"));
		if(email.isDisplayed()) {
			System.out.println("Slots are visible for : "+ email.getText());
		}
	}
	
	@Test
	public void BookButtonFunctionTest() throws Exception {
		loginTagTeam();
		Thread.sleep(3000);
		WebElement status=driver.findElement(By.id("intStatus"));
		
		
		if(status.equals("pending")) {
			driver.findElement(By.id("btnBook")).click();
			WebElement toast2 = driver.findElement(MobileBy.xpath("//android.widget.Toast"));
	        
	        assertEquals("Slot has been booked", toast2.getText());
		}else {
			driver.findElement(By.id("btnBook")).click();
            WebElement toast3 = driver.findElement(MobileBy.xpath("//android.widget.Toast"));
	        
	        assertEquals("Slot already booked", toast3.getText());
		}
        
		
	}
	
	@Test
	public void ReleaseButtonFunctionTest() throws Exception {
		loginTagTeam();
		Thread.sleep(3000);
		WebElement status=driver.findElement(By.id("intStatus"));
		
		
		if(status.equals("pending")) {
			driver.findElement(By.id("btnRelease")).click();
			WebElement toast4 = driver.findElement(MobileBy.xpath("//android.widget.Toast"));
	        
	        assertEquals("Slot is already pending", toast4.getText());
		}else {
			driver.findElement(By.id("btnRelease")).click();
            WebElement toast5 = driver.findElement(MobileBy.xpath("//android.widget.Toast"));
	        
	        assertEquals("Slot has been released", toast5.getText());
		}
        
		
	}
	
	
	@After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
