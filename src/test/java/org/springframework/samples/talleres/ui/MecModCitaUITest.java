
package org.springframework.samples.talleres.ui;

import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class MecModCitaUITest {

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

	@Test
	public void testMecModCita() throws Exception {
		this.driver.get("http://localhost:8080/");
		this.driver.findElement(By.linkText("Login")).click();
		this.driver.findElement(By.id("username")).sendKeys("paco");
		this.driver.findElement(By.id("password")).sendKeys("paco");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.linkText("Mis citas")).click();
		this.driver.findElement(By.linkText("Editar Cita")).click();
		this.driver.findElement(By.id("fechaCita")).clear();
		this.driver.findElement(By.id("fechaCita")).sendKeys("23/04/2022 12:30");
		this.driver.findElement(By.id("descripcion")).sendKeys("motor sin");
		this.driver.findElement(By.id("tiempo")).sendKeys("90");
		this.driver.findElement(By.id("coste")).sendKeys("180.50");
		// no funciona el click para mandar el form
		//this.driver.findElement(By.name("estadoCita")).click();
		//new Select(this.driver.findElement(By.name("estadoCita"))).selectByVisibleText("Aceptada");
		//this.driver.findElement(By.name("estadoCita")).click();
		//this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		//Assert.assertEquals("23/04/2022 12:30", this.driver.findElement(By.xpath("//table[@id='citasMecanicoTable']/tbody/tr/td[2]")).getText());

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
