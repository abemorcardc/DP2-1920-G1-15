
package org.springframework.samples.talleres.ui;

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
import org.openqa.selenium.support.ui.Select;

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
	public void testMecModCita() throws Exception {
		testLoginLolo();

		this.driver.findElement(By.linkText("MIS CITAS")).click();
		this.driver.findElement(By.linkText("Editar Cita")).click();

		driver.findElement(By.id("descripcion")).clear();
		this.driver.findElement(By.id("descripcion")).sendKeys("motor bien");
		driver.findElement(By.id("tiempo")).clear();
		this.driver.findElement(By.id("tiempo")).sendKeys("90");
		driver.findElement(By.id("coste")).clear();
		this.driver.findElement(By.id("coste")).sendKeys("180.50");
		driver.findElement(By.name("estadoCita")).click();
		new Select(driver.findElement(By.name("estadoCita"))).selectByVisibleText("Aceptada");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("motor bien", this.driver.findElement(By.xpath("//table[@id='citasMecanicoTable']/tbody/tr/td[4]")).getText());
		assertEquals("Aceptada", this.driver.findElement(By.xpath("//table[@id='citasMecanicoTable']/tbody/tr/td[6]")).getText());

	}

	@Test
	public void testMecModCitaNeg() throws Exception {
		testLoginLolo();

		this.driver.findElement(By.linkText("MIS CITAS")).click();
		this.driver.findElement(By.linkText("Editar Cita")).click();

		driver.findElement(By.id("fechaCita")).clear();
		this.driver.findElement(By.id("fechaCita")).sendKeys("14/03/2011 12:00");
		
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("La fecha debe debe ser futura", this.driver.findElement(By.xpath("//form[@id='update-visit-form']/div/div[3]/div/div/span[2]")).getText());

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
