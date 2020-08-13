package samples.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class HomePage {

	@FindBy(tagName = "h2")
	private WebElement title;

	@FindBy(xpath = ".//*[@id=\"main-navbar\"]/ul/li[2]/a/span[2]")
	private WebElement findOwners;

	private WebDriver driver;

	public HomePage(WebDriver driver) {
		this.driver = driver;
	}

	public HomePage assertAt() {
		assertThat(this.title.getText()).isEqualTo("Welcome");
		return this;
	}

	public <T> T clickFindOwners(WebDriver driver, Class<T> page) {
		this.findOwners.click();
		return PageFactory.initElements(driver, page);
	}

	public static <T> T to(WebDriver driver, Class<T> page) {
		driver.get("http://service:8080/");
		return PageFactory.initElements(driver, page);
	}

}
