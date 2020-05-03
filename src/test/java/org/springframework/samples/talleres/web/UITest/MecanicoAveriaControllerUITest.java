package org.springframework.samples.talleres.web.UITest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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
import org.springframework.boot.web.server.LocalServerPort;

public class MecanicoAveriaControllerUITest {
	@LocalServerPort
	private int port;
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@BeforeEach
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = "http://localhost:" + this.port;
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	public void testLoginMecanico1() throws Exception {
		driver.get("http://localhost:8080/");
		driver.findElement(By.linkText("LOGIN")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("paco");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("paco");
		driver.findElement(By.id("password")).sendKeys(Keys.ENTER);

	}

	public void testLoginMecanico2() throws Exception {
		driver.get("http://localhost:8080/");
		driver.findElement(By.linkText("LOGIN")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("lolo");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("lolo");
		driver.findElement(By.id("password")).sendKeys(Keys.ENTER);

	}

	@Test
	public void testActualizarAveria() throws Exception {
		testLoginMecanico1();
		driver.findElement(By.linkText("MIS CITAS")).click();
		driver.findElement(By.linkText("Listar Averias")).click();
		assertEquals("cambio de bujia",
				driver.findElement(By.xpath("//table[@id='citasMecanicoTable']/tbody/tr/td[2]")).getText());
		assertEquals("BAJA",
				driver.findElement(By.xpath("//table[@id='citasMecanicoTable']/tbody/tr/td[3]")).getText());
		assertEquals("No", driver.findElement(By.xpath("//table[@id='citasMecanicoTable']/tbody/tr/td[4]")).getText());
		driver.findElement(By.linkText("Editar Averia")).click();
		driver.findElement(By.id("descripcion")).click();
		driver.findElement(By.id("descripcion")).clear();
		driver.findElement(By.id("descripcion")).sendKeys("cambio de bujias");
		driver.findElement(By.name("estaReparada")).click();
		new Select(driver.findElement(By.name("estaReparada"))).selectByVisibleText("Si");
		driver.findElement(By.name("complejidad")).click();
		new Select(driver.findElement(By.name("complejidad"))).selectByVisibleText("Media");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("cambio de bujias",
				driver.findElement(By.xpath("//table[@id='citasMecanicoTable']/tbody/tr/td[2]")).getText());
		assertEquals("MEDIA",
				driver.findElement(By.xpath("//table[@id='citasMecanicoTable']/tbody/tr/td[3]")).getText());
		assertEquals("Si", driver.findElement(By.xpath("//table[@id='citasMecanicoTable']/tbody/tr/td[4]")).getText());
	}

	@Test
	public void testActualizarAveriaNegativo() throws Exception {
		testLoginMecanico2();
		driver.findElement(By.linkText("MIS CITAS")).click();
		driver.findElement(By.linkText("Listar Averias")).click();
		driver.findElement(By.linkText("Editar Averia")).click();
		driver.findElement(By.id("tiempo")).click();
		driver.findElement(By.id("tiempo")).clear();
		driver.findElement(By.id("tiempo")).sendKeys("-10");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("Actualizar", driver.findElement(By.xpath("//button[@type='submit']")).getText());
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