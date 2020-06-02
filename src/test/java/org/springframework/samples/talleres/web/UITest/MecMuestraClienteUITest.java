
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
public class MecMuestraClienteUITest {

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

	//Login del mecanico Paco
	public void testLoginPaco() throws Exception {
		this.driver.get(this.baseUrl);
		this.driver.findElement(By.linkText("LOGIN")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("paco");
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("paco");
		this.driver.findElement(By.id("password")).sendKeys(Keys.ENTER);

	}

	//Login del mecanico Lolo
	public void testLoginLolo() throws Exception {
		this.driver.get(this.baseUrl);
		this.driver.findElement(By.linkText("LOGIN")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("lolo");
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("lolo");
		this.driver.findElement(By.id("password")).sendKeys(Keys.ENTER);

	}

	// ---------------------------------------------------------
	//TESTS METODOS MECANICOS-CLIENTES

	/*
	 * Historia 16: Mecánico muestra Cliente
	 *
	 * Como mecánico quiero poder ver la información de un cliente para poder obtener más información acerca de éste.
	 */
	//ESCENARIO POSITIVO
	@Test
	public void testMecMuestraCliente() throws Exception {
		this.testLoginPaco();

		this.driver.findElement(By.linkText("Mis citas")).click();
		this.driver.findElement(By.linkText("Ver Cita")).click();
		this.driver.findElement(By.linkText("Manolo Martin")).click();
		Assert.assertEquals("Manolo Martin", this.driver.findElement(By.xpath("//td")).getText());
	}

	/*
	 * ESCENARIO NEGATIVO
	 *
	 * Se intenta acceder a un cliente que no existe
	 */
	@Test
	public void testMecNoAutorizadoShowCliente() throws Exception {
		this.testLoginLolo();

		this.driver.get("http://localhost:8080/mecanicos/cliente/1");
		Assert.assertEquals("Something happened...", this.driver.findElement(By.xpath("//h2")).getText());
	}

	/*
	 * ESCENARIO NEGATIVO
	 *
	 * Se intenta acceder a un cliente que no existe
	 */
	@Test
	public void testMecShowClienteNoExiste() throws Exception {
		this.testLoginLolo();

		this.driver.get("http://localhost:8080/mecanicos/cliente/20");
		Assert.assertEquals("Something happened...", this.driver.findElement(By.xpath("//h2")).getText());
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
