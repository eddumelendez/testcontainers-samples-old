package samples.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class FindPage {

	@FindBy(tagName = "h2")
	private WebElement title;

	@FindBy(id = "lastName")
	private WebElement lastname;

	@FindBy(xpath = "//*[@id=\"search-owner-form\"]/div[2]/div/button")
	private WebElement findOwner;

	private WebDriver driver;

	public FindPage(WebDriver driver) {
		this.driver = driver;
	}

	public FindPage assertAt() {
		assertThat(this.title.getText()).isEqualTo("Find Owners");
		return this;
	}

	public FindPage search(String keys) {
		this.lastname.sendKeys(keys);
		return this;
	}

	public <T> T  submit(Class<T> page) {
		this.findOwner.click();
		return PageFactory.initElements(this.driver, page);
	}
}
