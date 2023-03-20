package hello_word_rest;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class HelloWordTest {
	
	@Test
	public void testHelloWord() {
		Response response = RestAssured.request(Method.GET, "https://restapi.wcaquino.me/ola");
		Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
		Assert.assertTrue("O status code deveria ser 200", response.statusCode() == 200);
		Assert.assertEquals(200, response.statusCode());		
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
	}
	
	@Test
	public void deveConhecerOutrasFormas() {				
		given() 
		.when()
			.get("https://restapi.wcaquino.me/ola")
		.then()
			.statusCode(200);
			
	}
	
	@Test
	public void devoConhecerMatchersHamcrest() {
		
		// Documentation: https://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matchers.html

		// Check equality string
		assertThat("Maria", Matchers.is("Maria"));

		// Check equality number
		assertThat(128, Matchers.is(128));

		// Check number type is integer
		assertThat(128, Matchers.isA(Integer.class));

		// Check number type is a double
		assertThat(128d, Matchers.isA(Double.class));

		// Check is number is greater than
		assertThat(128d, Matchers.greaterThan(120d));

		// Check is number is less than
		assertThat(128d, Matchers.lessThan(130d));
		
		// ################## Create array ##################
		List<Integer> impares = Arrays.asList(1,3,5,7,9);
		
		// ### Check array size
		assertThat(impares, Matchers.hasSize(5));
		
		// ### Check contains in array
		assertThat(impares, contains(1,3,5, 7, 9));
		
		// ### Check contains in any order in array
		assertThat(impares, containsInAnyOrder(1,3,5, 7, 9));
		
		// ### Check contain item
		assertThat(impares, hasItem(3));
		
		// ### Check contains items
		assertThat(impares, hasItems(1, 3));
		
		// Check NOT
		assertThat("Maria", not("João"));
		
		// Check OR
		assertThat("Maria", anyOf(is("Joaquina"), is("Maria")));
		
		// Check AND
		assertThat("Joaquina", allOf(startsWith("Joa"), endsWith("ina"), containsString("qui")));
	}	 
	
	@Test
	public void devoValidarBody() {				
		given() 
		.when()
			.get("https://restapi.wcaquino.me/ola")
		.then()
			.statusCode(200)
			.body(is("Ola Mundo!"))
			.body(containsString("Mundo"))
			.body(is(not(nullValue())));
	}
}
