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
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class MecanicoAveriaControllerUITest {
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
		this.driver.get(baseUrl);
		this.driver.findElement(By.linkText("LOGIN")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("paco");
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("paco");
		this.driver.findElement(By.id("password")).sendKeys(Keys.ENTER);

	}

	public void testLoginMecanico2() throws Exception {
		this.driver.get(baseUrl);
		this.driver.findElement(By.linkText("LOGIN")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("lolo");
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("lolo");
		this.driver.findElement(By.id("password")).sendKeys(Keys.ENTER);

	}

	@Test
	public void testActualizarAveria() throws Exception {
		this.testLoginMecanico1();
		this.driver.findElement(By.linkText("MIS CITAS")).click();
		this.driver.findElement(By.linkText("Listar Averias")).click();
		Assert.assertEquals("cambio de bujia",
			this.driver.findElement(By.xpath("//table[@id='averiasMecanicoTable']/tbody/tr/td[2]")).getText());
		Assert.assertEquals("BAJA",
			this.driver.findElement(By.xpath("//table[@id='averiasMecanicoTable']/tbody/tr/td[3]")).getText());
		Assert.assertEquals("No",
			this.driver.findElement(By.xpath("//table[@id='averiasMecanicoTable']/tbody/tr/td[4]")).getText());
		this.driver.findElement(By.linkText("Editar Averia")).click();
		this.driver.findElement(By.id("descripcion")).click();
		this.driver.findElement(By.id("descripcion")).clear();
		this.driver.findElement(By.id("descripcion")).sendKeys("cambio de bujias");
		this.driver.findElement(By.name("estaReparada")).click();
		new Select(this.driver.findElement(By.name("estaReparada"))).selectByVisibleText("Si");
		this.driver.findElement(By.name("complejidad")).click();
		new Select(this.driver.findElement(By.name("complejidad"))).selectByVisibleText("Media");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		Assert.assertEquals("cambio de bujias",
			this.driver.findElement(By.xpath("//table[@id='averiasMecanicoTable']/tbody/tr/td[2]")).getText());
		Assert.assertEquals("MEDIA",
			this.driver.findElement(By.xpath("//table[@id='averiasMecanicoTable']/tbody/tr/td[3]")).getText());
		Assert.assertEquals("Si",
			this.driver.findElement(By.xpath("//table[@id='averiasMecanicoTable']/tbody/tr/td[4]")).getText());
	}

	@Test
	public void testActualizarAveriaNegativo() throws Exception {
		this.testLoginMecanico2();
		this.driver.findElement(By.linkText("MIS CITAS")).click();
		this.driver.findElement(By.linkText("Listar Averias")).click();
		this.driver.findElement(By.linkText("Editar Averia")).click();
		this.driver.findElement(By.id("tiempo")).click();
		this.driver.findElement(By.id("tiempo")).clear();
		this.driver.findElement(By.id("tiempo")).sendKeys("-10");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		Assert.assertEquals("Actualizar", this.driver.findElement(By.xpath("//button[@type='submit']")).getText());
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