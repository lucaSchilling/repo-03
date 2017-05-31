package chat.test;

import javax.ws.rs.core.MediaType;
import static io.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class TestLoginServer {

	public static void start() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.basePath = "/";
		RestAssured.port=5001;
	}

	public static void stop() {
	}
	
	public static Response login(String name, String password){
		Response resp = expect().statusCode(200).contentType(MediaType.APPLICATION_JSON).body("token", notNullValue())
				.body("expire-date", notNullValue()).given().contentType(MediaType.APPLICATION_JSON)
				.body(("{'user': '"+name+"@web.de', 'password':"+password+", 'pseudonym': "+name+"}".replace('\'', '"')))
				.when().post("/login");
		return resp;
	}
}
