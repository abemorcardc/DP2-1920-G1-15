package org.springframework.samples.talleres.web.UITest;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
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
public class ClienteCitaControllerUITest {
	@LocalServerPort
	private int port;
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@BeforeEach
	public void setUp() throws Exception {
		// String pathToGeckoDriver =
		// "C:\\Users\\abrah\\OneDrive\\Escritorio\\Universidad";
		// System.setProperty("webdriver.gecko.driver", "webdriver.gecko.driver");1
		this.driver = new FirefoxDriver();
		this.baseUrl = "http://localhost:" + this.port;
		this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Test
	public void aTestClienteListaCitas() throws Exception {

		this.driver.get(this.baseUrl);
		this.driver.findElement(By.linkText("LOGIN")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("manolo");
		this.driver.findElement(By.id("password")).click();
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("manolo");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.linkText("MIS CITAS")).click();
		Assertions.assertEquals("Ver cita", this.driver.findElement(By.linkText("Ver cita")).getText());
		Assertions.assertEquals("Pedir Cita", this.driver.findElement(By.linkText("Pedir Cita")).getText());
	}

	@Test
	public void bTestClienteMuestraCita() throws Exception {
		this.driver.get(this.baseUrl);
		this.driver.findElement(By.linkText("LOGIN")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("manolo");
		this.driver.findElement(By.id("password")).click();
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("manolo");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.get(this.baseUrl + "/cliente/citas");
		this.driver.findElement(By.linkText("Ver cita")).click();
		try {
			Assertions.assertEquals("Detalles de la cita", this.driver.findElement(By.xpath("//h2")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
	}

	@Test
	public void testClienteCancelaCitaPendiente() throws Exception {
		this.driver.get(this.baseUrl);
		this.driver.findElement(By.linkText("LOGIN")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("manolo");
		this.driver.findElement(By.id("password")).click();
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("manolo");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.get(this.baseUrl + "/cliente/citas");
		this.driver.findElement(By.linkText("Ver cita")).click();
		this.driver.findElement(By.linkText("Cancelar")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.get(this.baseUrl + "/cliente/citas/1");
		try {
			Assertions.assertEquals("cancelada", this.driver.findElement(By.xpath("//tr[7]/td")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		this.driver.findElement(By.linkText("Volver")).click();
	}

	@Test
	public void cTestClienteCreaCitaFechaErronea() throws Exception {
		this.driver.get(this.baseUrl);
		this.driver.findElement(By.linkText("LOGIN")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("manolo");
		this.driver.findElement(By.id("password")).click();
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("manolo");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.get(this.baseUrl + "/cliente/citas");
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		this.driver.findElement(By.linkText("Pedir Cita")).click();
		this.driver.findElement(By.linkText("Primero escoge tu vehículo")).click();
		this.driver.findElement(By.linkText("Elegir Vehiculo")).click();
		this.driver.findElement(By.id("fechaCita")).click();
		this.driver.findElement(By.linkText("29")).click();
		// this.driver.findElement(By.id("fechaCita")).clear();
		// this.driver.findElement(By.id("fechaCita")).sendKeys("08/05/2020");
		this.driver.findElement(By.id("descripcion")).clear();
		this.driver.findElement(By.id("descripcion")).sendKeys("tttt");
		// this.driver.findElement(By.linkText("descripcion")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		try {
			Assertions.assertEquals("29/05/2020 hh:mm",
					this.driver.findElement(By.id("fechaCita")).getAttribute("value"));
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		this.driver.findElement(By.linkText("MIS CITAS")).click();
		try {
			Assertions.assertEquals("2021-03-14T12:00",
					this.driver.findElement(By.xpath("//table[@id='citasTable']/tbody/tr/td[2]")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
	}

	@Test
	public void dTestClienteCreaCitaSinCoche() throws Exception {
		this.driver.get(this.baseUrl);
		this.driver.findElement(By.linkText("LOGIN")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("manolo");
		this.driver.findElement(By.id("password")).click();
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("manolo");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.get(this.baseUrl + "/cliente/citas/");
		this.driver.findElement(By.linkText("MIS CITAS")).click();
		this.driver.findElement(By.linkText("Pedir Cita")).click();
		this.driver.findElement(By.id("fechaCita")).click();
		this.driver.findElement(By.id("fechaCita")).clear();
		this.driver.findElement(By.id("fechaCita")).sendKeys("23/04/2020 10:10");
		this.driver.findElement(By.id("descripcion")).click();
		this.driver.findElement(By.id("descripcion")).clear();
		this.driver.findElement(By.id("descripcion")).sendKeys("nn");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		try {
			Assertions.assertEquals("Vehiculos", this.driver.findElement(By.xpath("//h2")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
	}

	@Test
	public void testClienteCreaCita() throws Exception {
		this.driver.get(this.baseUrl);
		this.driver.findElement(By.linkText("LOGIN")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("manolo");
		this.driver.findElement(By.id("password")).click();
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("manolo");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.get(this.baseUrl + "/cliente/citas");
		this.driver.findElement(By.linkText("Pedir Cita")).click();
		this.driver.findElement(By.linkText("Primero escoge tu vehículo")).click();
		this.driver.findElement(By.linkText("Elegir Vehiculo")).click();
		this.driver.findElement(By.id("fechaCita")).click();
		this.driver.findElement(By.linkText("30")).click();
		this.driver.findElement(By.id("descripcion")).clear();
		this.driver.findElement(By.id("descripcion")).sendKeys("ff");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
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
