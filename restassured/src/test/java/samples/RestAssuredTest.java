package samples;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;

@Testcontainers
class RestAssuredTest {

	@Container
	public GenericContainer service = new GenericContainer<>("spring-petclinic:2.3.1.BUILD-SNAPSHOT")
			.withExposedPorts(8080)
			.waitingFor(Wait.forHttp("/"));

	@Test
	void test() {
		RestAssured.baseURI = "http://" + this.service.getHost();
		RestAssured.port = this.service.getMappedPort(8080);

		when().get("/vets")
				.then().body("vetList.id.size()", equalTo(6));
	}
}
