package samples.pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.assertj.core.api.Assertions.assertThat;

public class OwnersPage {

	@FindBy(tagName = "h2")
	private WebElement title;

	@FindBy(xpath = ".//*[@id=\"owners\"]/tbody/tr/td[1]")
	private List<WebElement> owners;

	private WebDriver driver;

	public OwnersPage(WebDriver driver) {
		this.driver = driver;
	}

	public OwnersPage assertAt() {
		assertThat(this.title.getText()).isEqualTo("Owners");
		return this;
	}

	public void expectedNumberOfRecords(int numberOfRecords) {
		assertThat(this.owners.size()).isEqualTo(numberOfRecords);
	}

}
