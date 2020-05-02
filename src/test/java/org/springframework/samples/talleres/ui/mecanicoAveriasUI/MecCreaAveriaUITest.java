package org.springframework.samples.talleres.ui.mecanicoAveriasUI;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
public class MecCreaAveriaUITest {
	@LocalServerPort
	private int port;
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@BeforeEach
	public void setUp() throws Exception {
		this.driver = new FirefoxDriver();
		this.baseUrl = "http://localhost:" + this.port;
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testMecCreaAveriaUI() throws Exception {
		this.driver.get(this.baseUrl);
		this.driver.findElement(By.linkText("LOGIN")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("paco");
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("paco");
		this.driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
		this.driver.findElement(By.linkText("MIS CITAS")).click();
		this.driver.findElement(By.linkText("Ver Averias")).click();
		this.driver.findElement(By.linkText("Nueva Avería")).click();
		this.driver.findElement(By.linkText("Primero elija la cita correspondiente")).click();
		try {
			Assertions.assertEquals("Problemas con el motor",
					this.driver.findElement(By.xpath("//table[@id='vehiculosTable']/tbody/tr/td[2]")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		this.driver.findElement(By.linkText("Elegir Cita")).click();
		this.driver.findElement(By.id("nombre")).clear();
		this.driver.findElement(By.id("nombre")).sendKeys("Averia");
		try {
			Assertions.assertEquals("Nombre",
					this.driver.findElement(By.xpath("//form[@id='add-cita-form']/div/div/label")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		try {
			Assertions.assertEquals("Averia", this.driver.findElement(By.id("nombre")).getAttribute("value"));
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		this.driver.findElement(By.id("descripcion")).clear();
		this.driver.findElement(By.id("descripcion")).sendKeys("Averia descripcion");
		this.driver.findElement(By.id("coste")).clear();
		this.driver.findElement(By.id("coste")).sendKeys("20.0");
		this.driver.findElement(By.id("tiempo")).clear();
		this.driver.findElement(By.id("tiempo")).sendKeys("10");
		this.driver.findElement(By.id("piezasNecesarias")).clear();
		this.driver.findElement(By.id("piezasNecesarias")).sendKeys("1");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.xpath("(//a[contains(text(),'Detalles')])[2]")).click();
		try {
			Assertions.assertEquals("Averia", this.driver.findElement(By.xpath("//td")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		try {
			Assertions.assertEquals("Averia descripcion", this.driver.findElement(By.xpath("//tr[2]/td")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		try {
			Assertions.assertEquals("20.0", this.driver.findElement(By.xpath("//tr[5]/td")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.driver.quit();
		String verificationErrorString = this.verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			Assertions.fail(verificationErrorString);
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
