package org.springframework.samples.talleres.web.UITest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class VehiculoControllerUITest {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@BeforeEach
	public void setUp() throws Exception {
		String pathToGeckoDriver = "C:\\Users\\javig\\Desktop\\DP 2";
		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver + "\\geckodriver.exe");

		driver = new FirefoxDriver();
		baseUrl = "https://www.google.com/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	public void testLoginMecanico() throws Exception {
		driver.get("http://localhost:8080/");
		driver.findElement(By.linkText("LOGIN")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("paco");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("paco");
		driver.findElement(By.id("password")).sendKeys(Keys.ENTER);

	}

	public void testLoginCliente() throws Exception {
		driver.get("http://localhost:8080/");
		driver.findElement(By.linkText("LOGIN")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("manolo");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("manolo");
		driver.findElement(By.id("password")).sendKeys(Keys.ENTER);

	}

	public void testLoginCliente2() throws Exception {
		driver.get("http://localhost:8080/");
		driver.findElement(By.linkText("LOGIN")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("manoli");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("manoli");
		driver.findElement(By.id("password")).sendKeys(Keys.ENTER);

	}

	@Test
	public void testListarVehiculosTestCase() throws Exception {
		testLoginCliente();
		driver.findElement(By.linkText("MIS VEHICULOS")).click();
		assertEquals("Mercedes A",
				driver.findElement(By.xpath("//table[@id='vehiculosTable']/tbody/tr/td[2]")).getText());
		assertEquals("Mercedes AX",
				driver.findElement(By.xpath("//table[@id='vehiculosTable']/tbody/tr[2]/td[2]")).getText());

	}

	@Test
	public void testMostrarVehiculo() throws Exception {
		testLoginCliente();
		driver.findElement(By.linkText("MIS VEHICULOS")).click();
		driver.findElement(By.linkText("Ver en detalle")).click();
		assertEquals("Turismo", driver.findElement(By.xpath("//td")).getText());
		assertEquals("Mercedes A", driver.findElement(By.xpath("//tr[2]/td")).getText());
		assertEquals("2345FCL", driver.findElement(By.xpath("//tr[3]/td")).getText());
		assertEquals("2012-09-04 00:00:00.0", driver.findElement(By.xpath("//tr[4]/td")).getText());
		assertEquals("10000", driver.findElement(By.xpath("//tr[5]/td")).getText());
	}

	@Test
	public void testMostrarVehiculoNegativo() throws Exception {
		testLoginCliente2();
		driver.get("http://localhost:8080/cliente/vehiculos/1");
		assertEquals("Something happened...", driver.findElement(By.xpath("//h2")).getText());
	}

	@Test
	public void testCrearVehiculo() throws Exception {
		testLoginCliente2();
		driver.get("http://localhost:8080/");
		driver.findElement(By.linkText("MIS VEHICULOS")).click();
		driver.findElement(By.linkText("Crear")).click();
		driver.findElement(By.id("matricula")).click();
		driver.findElement(By.id("matricula")).clear();
		driver.findElement(By.id("matricula")).sendKeys("1234TYU");
		driver.findElement(By.id("fechaMatriculacion")).click();
		driver.findElement(By.id("fechaMatriculacion")).clear();
		driver.findElement(By.id("fechaMatriculacion")).sendKeys("2000-10-10");
		driver.findElement(By.id("modelo")).click();
		driver.findElement(By.id("modelo")).clear();
		driver.findElement(By.id("modelo")).sendKeys("Mercedes X");
		driver.findElement(By.id("kilometraje")).click();
		driver.findElement(By.id("kilometraje")).clear();
		driver.findElement(By.id("kilometraje")).sendKeys("10000");
		driver.findElement(By.name("tipoVehiculo")).click();
		new Select(driver.findElement(By.name("tipoVehiculo"))).selectByVisibleText("Furgoneta");
		driver.findElement(By.name("tipoVehiculo")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("Mercedes X",
				driver.findElement(By.xpath("//table[@id='vehiculosTable']/tbody/tr[2]/td[2]")).getText());
		assertEquals("1234TYU",
				driver.findElement(By.xpath("//table[@id='vehiculosTable']/tbody/tr[2]/td[3]")).getText());
		assertEquals("2020-03-01 01:00:00.0",
				driver.findElement(By.xpath("//table[@id='vehiculosTable']/tbody/tr[2]/td[4]")).getText());
	}

	@Test
	public void testCrearVehiculoNegativo() throws Exception {
		testLoginCliente2();
		driver.get("http://localhost:8080/");
		driver.findElement(By.linkText("MIS VEHICULOS")).click();
		driver.findElement(By.linkText("Crear")).click();
		driver.findElement(By.id("matricula")).click();
		driver.findElement(By.id("matricula")).clear();
		driver.findElement(By.id("matricula")).sendKeys("1234TYU");
		driver.findElement(By.id("fechaMatriculacion")).click();
		driver.findElement(By.id("fechaMatriculacion")).clear();
		driver.findElement(By.id("fechaMatriculacion")).sendKeys("2000-10-10");
		driver.findElement(By.id("modelo")).click();
		driver.findElement(By.id("modelo")).clear();
		driver.findElement(By.id("modelo")).sendKeys("Mercedes X");
		driver.findElement(By.id("kilometraje")).click();
		driver.findElement(By.id("kilometraje")).clear();
		driver.findElement(By.id("kilometraje")).sendKeys("-10000");
		driver.findElement(By.name("tipoVehiculo")).click();
		new Select(driver.findElement(By.name("tipoVehiculo"))).selectByVisibleText("Furgoneta");
		driver.findElement(By.name("tipoVehiculo")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("Crear", driver.findElement(By.xpath("//button[@type='submit']")).getText());
	}

	@AfterEach
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}