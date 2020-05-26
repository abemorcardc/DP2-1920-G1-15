
package org.springframework.samples.talleres.web.UITest;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class MecListaCitasPYAceptaUITest {

	@LocalServerPort
	private int				port;
	private WebDriver		driver;
	private String			baseUrl;
	private boolean			acceptNextAlert		= true;
	private StringBuffer	verificationErrors	= new StringBuffer();


	@BeforeEach
	public void setUp() throws Exception {
		System.setProperty("webdriver.gecko.driver", System.getenv("webdriver.gecko.driver"));
		this.driver = new FirefoxDriver();
		this.baseUrl = "http://localhost:" + this.port;
		this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	public void testLoginPepe() throws Exception {
		this.driver.get(this.baseUrl);
		this.driver.findElement(By.linkText("LOGIN")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("pepe");
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("pepe");
		this.driver.findElement(By.id("password")).sendKeys(Keys.ENTER);

	}

	@Test
	public void testMecListaCitasPendientes() throws Exception {
		this.testLoginPepe();

		this.driver.findElement(By.linkText("CITAS PENDIENTES")).click();
		driver.findElement(By.linkText("Aceptar")).click();
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
		Assert.assertEquals("Aceptada", this.driver.findElement(By.xpath("//table[@id='citasMecanicoTable']/tbody/tr[2]/td[5]")).getText());
		Assert.assertEquals("Preparación ITV", this.driver.findElement(By.xpath("//table[@id='citasMecanicoTable']/tbody/tr[2]/td[4]")).getText());

	}

	@AfterEach
	public void tearDown() throws Exception {
		this.driver.quit();
		String verificationErrorString = this.verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			Assert.fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(final By by) {
		try {
			this.driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			this.driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = this.driver.switchTo().alert();
			String alertText = alert.getText();
			if (this.acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			this.acceptNextAlert = true;
		}
	}
}
