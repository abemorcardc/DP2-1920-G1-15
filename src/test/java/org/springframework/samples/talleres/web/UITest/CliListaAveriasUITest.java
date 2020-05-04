
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

public class CliListaAveriasUITest {

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

	public void testLoginManolo() throws Exception {
		driver.get("http://localhost:8080/");
		driver.findElement(By.linkText("LOGIN")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("manolo");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("manolo");
		driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
	}

	public void testLoginManoli() throws Exception {
		driver.get("http://localhost:8080/");
		driver.findElement(By.linkText("LOGIN")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("manoli");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("manoli");
		driver.findElement(By.id("password")).sendKeys(Keys.ENTER);

	}
	@Test
	public void testCliListaAverias() throws Exception {
		testLoginManolo();
		
		this.driver.findElement(By.linkText("MIS VEHICULOS")).click();
		this.driver.findElement(By.linkText("Ver Averias")).click();
		assertEquals("BAJA", this.driver.findElement(By.xpath("//table[@id='averiasClienteTable']/tbody/tr/td[3]")).getText());
		assertEquals("No", this.driver.findElement(By.xpath("//table[@id='averiasClienteTable']/tbody/tr/td[4]")).getText());
	}
	
	@Test
	public void testMostrarVehiculoNegativo() throws Exception {
		testLoginManoli();
		driver.get("http://localhost:8080/cliente/vehiculos/1/averias");
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
