package org.springframework.samples.talleres.web.UITest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

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
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class VehiculoControllerUITest {
	@LocalServerPort
	private int port;
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@BeforeEach
	public void setUp() throws Exception {
		System.setProperty("webdriver.gecko.driver", System.getenv("webdriver.gecko.driver"));
		this.driver = new FirefoxDriver();
		this.baseUrl = "http://localhost:" + this.port;
		this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	public void testLoginMecanico1() throws Exception {
		driver.get(baseUrl);
		driver.findElement(By.linkText("LOGIN")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("paco");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("paco");
		driver.findElement(By.id("password")).sendKeys(Keys.ENTER);

	}
	
	public void testLoginMecanico2() throws Exception {
		driver.get(baseUrl);
		driver.findElement(By.linkText("LOGIN")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("lolo");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("lolo");
		driver.findElement(By.id("password")).sendKeys(Keys.ENTER);

	}

	public void testLoginCliente1() throws Exception {
		driver.get(baseUrl);
		driver.findElement(By.linkText("LOGIN")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("manolo");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("manolo");
		driver.findElement(By.id("password")).sendKeys(Keys.ENTER);

	}

	public void testLoginCliente2() throws Exception {
		driver.get(baseUrl);
		driver.findElement(By.linkText("LOGIN")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("manoli");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("manoli");
		driver.findElement(By.id("password")).sendKeys(Keys.ENTER);

	}

	public void testLoginCliente3() throws Exception {
		driver.get(baseUrl);
		driver.findElement(By.linkText("LOGIN")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("david");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("david");
		driver.findElement(By.id("password")).sendKeys(Keys.ENTER);

	}

	@Test
	public void testListarVehiculosTestCase() throws Exception {
		testLoginCliente1();
		driver.findElement(By.linkText("MIS VEHICULOS")).click();
		assertEquals("Mercedes A",
				driver.findElement(By.xpath("//table[@id='vehiculosTable']/tbody/tr/td[3]")).getText());
		assertEquals("Mercedes AX",
				driver.findElement(By.xpath("//table[@id='vehiculosTable']/tbody/tr[2]/td[3]")).getText());

	}

	@Test
	public void testMostrarVehiculo() throws Exception {
		testLoginCliente1();
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
		driver.get(baseUrl + "/cliente/vehiculos/1");
		assertEquals("Something happened...", driver.findElement(By.xpath("//h2")).getText());
	}
	
	@Test
	  public void testMostrarMecanicoVehiculo() throws Exception {
	    testLoginMecanico2();
	    driver.findElement(By.linkText("MIS CITAS")).click();
	    driver.findElement(By.linkText("Peugeot 307: 5125DRF")).click();
	    assertEquals("Turismo", driver.findElement(By.xpath("//td")).getText());
	    assertEquals("Peugeot 307", driver.findElement(By.xpath("//tr[2]/td")).getText());
	    assertEquals("5125DRF", driver.findElement(By.xpath("//tr[3]/td")).getText());
	    assertEquals("15000", driver.findElement(By.xpath("//tr[5]/td")).getText());
	    assertEquals("Detalles de su vehículo", driver.findElement(By.xpath("//h2")).getText());
	  }
	
	@Test
	public void testMostrarMecanicoVehiculoNegativo() throws Exception {
		testLoginCliente1();
		driver.get(baseUrl + "/cliente/vehiculos/2");
		assertEquals("Something happened...", driver.findElement(By.xpath("//h2")).getText());
	}

	@Test
	public void testCrearVehiculo() throws Exception {
		testLoginCliente2();
		driver.get(baseUrl);
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
				driver.findElement(By.xpath("//table[@id='vehiculosTable']/tbody/tr[3]/td[3]")).getText());
		assertEquals("1234TYU",
				driver.findElement(By.xpath("//table[@id='vehiculosTable']/tbody/tr[3]/td[4]")).getText());
	}

	@Test
	public void testCrearVehiculoNegativo() throws Exception {
		testLoginCliente2();
		driver.get(baseUrl + "/cliente/vehiculos");
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

	@Test
	public void testActualizarVehiculo() throws Exception {
		testLoginCliente3();
		driver.findElement(By.linkText("MIS VEHICULOS")).click();
		driver.findElement(By.linkText("Editar Vehiculo")).click();
		driver.findElement(By.id("matricula")).click();
		driver.findElement(By.id("matricula")).clear();
		driver.findElement(By.id("matricula")).sendKeys("4656FCV");
		driver.findElement(By.id("modelo")).click();
		driver.findElement(By.id("modelo")).clear();
		driver.findElement(By.id("modelo")).sendKeys("Seat Ibiza");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("Seat Ibiza",
				driver.findElement(By.xpath("//table[@id='vehiculosTable']/tbody/tr/td[3]")).getText());
		assertEquals("4656FCV", driver.findElement(By.xpath("//table[@id='vehiculosTable']/tbody/tr/td[4]")).getText());
	}

	@Test
	public void testActualizarVehiculoNegativo() throws Exception {
		testLoginCliente3();
		driver.findElement(By.linkText("MIS VEHICULOS")).click();
		driver.findElement(By.linkText("Editar Vehiculo")).click();
		driver.findElement(By.id("kilometraje")).click();
		driver.findElement(By.id("kilometraje")).clear();
		driver.findElement(By.id("kilometraje")).sendKeys("-1000");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("Actualizar", driver.findElement(By.xpath("//button[@type='submit']")).getText());
	}

	@Test
	public void testDarDeBaja() throws Exception {
		testLoginCliente2();
		driver.findElement(By.linkText("MIS VEHICULOS")).click();
		assertEquals("Peugeot 307",
				driver.findElement(By.xpath("//table[@id='vehiculosTable']/tbody/tr/td[3]")).getText());
		assertEquals("5125DRF", driver.findElement(By.xpath("//table[@id='vehiculosTable']/tbody/tr/td[4]")).getText());
		driver.findElement(By.linkText("Ver en detalle")).click();
		driver.findElement(By.linkText("Dar de baja vehiculo")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("Peugeot 200",
				driver.findElement(By.xpath("//table[@id='vehiculosTable']/tbody/tr/td[3]")).getText());
		assertEquals("1789JNB", driver.findElement(By.xpath("//table[@id='vehiculosTable']/tbody/tr/td[4]")).getText());
	}

	@Test
	public void testDarDeBajaNegativo() throws Exception {
		testLoginCliente1();
		driver.findElement(By.linkText("MIS VEHICULOS")).click();
		driver.findElement(By.linkText("Ver en detalle")).click();
		driver.findElement(By.linkText("Dar de baja vehiculo")).click();
		assertEquals("No puedes dar de baja un vehículo con citas pendientes",
				driver.findElement(By.xpath("//form[@id='vehiculo']/h3")).getText());
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