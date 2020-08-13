package samples;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import samples.pages.FindPage;
import samples.pages.HomePage;
import samples.pages.OwnersPage;

@Testcontainers
class SeleniumTest {

	public static Network network = Network.newNetwork();

	@Container
	public static GenericContainer service = new GenericContainer<>("spring-petclinic:2.3.1.BUILD-SNAPSHOT")
			.withExposedPorts(8080)
			.withNetwork(network)
			.withNetworkAliases("service")
			.waitingFor(Wait.forHttp("/"));

	@Container
	public BrowserWebDriverContainer chrome = new BrowserWebDriverContainer<>()
			.withNetwork(network)
			.withNetworkAliases("chrome")
			.withCapabilities(new ChromeOptions())
			.withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, new File("./target/"));

	@Test
	void testFindPage() {
		WebDriver driver = this.chrome.getWebDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		HomePage home = HomePage.to(driver, HomePage.class).assertAt();
		FindPage find = home.clickFindOwners(driver, FindPage.class).assertAt();


		find.search("Davis").submit(OwnersPage.class).assertAt().expectedNumberOfRecords(2);

		driver.quit();
	}
}
