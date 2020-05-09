
package org.springframework.samples.talleres.web.UITest;

import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class MecMuestraCitaUITest {

	private WebDriver		driver;
	private String			baseUrl;
	private boolean			acceptNextAlert		= true;
	private StringBuffer	verificationErrors	= new StringBuffer();


	@BeforeEach
	public void setUp() throws Exception {
		System.setProperty("webdriver.gecko.driver", System.getenv("webdriver.gecko.driver"));

		this.driver = new FirefoxDriver();
		this.baseUrl = "https://www.google.com/";
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}
	
	public void testLoginPepe() throws Exception {
		driver.get("http://localhost:8080/");
		driver.findElement(By.linkText("LOGIN")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("pepe");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("pepe");
		driver.findElement(By.id("password")).sendKeys(Keys.ENTER);

	}
	
	public void testLoginLolo() throws Exception {
		driver.get("http://localhost:8080/");
		driver.findElement(By.linkText("LOGIN")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("lolo");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("lolo");
		driver.findElement(By.id("password")).sendKeys(Keys.ENTER);

	}

	@Test
	public void testMecMuestraCita() throws Exception {
		testLoginPepe();
		
		this.driver.findElement(By.linkText("MIS CITAS")).click();
		this.driver.findElement(By.linkText("Ver Cita")).click();

		assertEquals("puerta mal", this.driver.findElement(By.xpath("//tr[2]/td")).getText());
		assertEquals("Revision", this.driver.findElement(By.xpath("//tr[3]/td")).getText());
		assertEquals("150", this.driver.findElement(By.xpath("//tr[4]/td")).getText());
		assertEquals("200.0", this.driver.findElement(By.xpath("//tr[5]/td")).getText());
	}

	@Test
	public void testMostrarCitaNegativo() throws Exception {
		testLoginLolo();
		driver.get("http://localhost:8080/mecanicos/citas/1");
		assertEquals("Something happened...", driver.findElement(By.xpath("//h2")).getText());
	}
	
	@AfterEach
	public void tearDown() throws Exception {
		this.driver.quit();
		String verificationErrorString = this.verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
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
