package hello_word_rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;

public class userJsonTest {
	
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "https://restapi.wcaquino.me";
//		RestAssured.port = 443;
//		RestAssured.basePath = "/v2";
	}
	
	@Test
	public void deveVerificarPrimeiroNivel() {
		given()
		.when()
			.get("/users/1")
		.then()
			.statusCode(200)
			.body("id", is(1))
			.body("name", containsString("Silva"))
			.body("age", greaterThan(18))
		;
	}
	
	
	@Test
	public void deveVerificarSegundoNivel() {
		given()
		.when()
			.get("/users/2")
		.then()
			.statusCode(200)
			.body("name", containsString("Joaquina"))
			.body("endereco.rua", is("Rua dos bobos"))
		;
	}
	
	
	@Test
	public void deveVerificarUmaLista() {
		given()
		.when()
			.get("/users/3")
		.then()
			.statusCode(200)
			.body("name", containsString("Ana"))
			.body("filhos", hasSize(2))
			.body("filhos[0].name", is("Zezinho"))
			.body("filhos[1].name", is("Luizinho"))
			.body("filhos.name", hasItem("Zezinho"))
			.body("filhos.name", hasItems("Zezinho", "Luizinho"))				
		;
	}
	
	
	@Test
	public void deveRetornarErroUsuarioInexistente() {
		given()
		.when()
			.get("/users/4")
		.then()
			.statusCode(404)
			.body("error", is("Usuário inexistente"))
		;
	}
	
	
	@Test
	public void deveVerificarListaNaRaiz() {
		given()
		.when()
			.get("/users")
		.then()
			.statusCode(200)
			.body("$", hasSize(3))
			.body("name", hasItems("João da Silva", "Maria Joaquina", "Ana Júlia"))
			.body("age[1]", is(25))
			.body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho")))
			.body("salary", contains(1234.5678f, 2500, null))
		;
	}

	
	@Test
	public void devoFazerVerificaçõesAvançadas() {
		given()
		.when()
			.get("/users")
		.then()
			.statusCode(200)
			.body("$", hasSize(3))
			
			// find by age <= 25 and check size (2)
			.body("age.findAll {it <= 25}.size()", is(2)) 
			
			// find by age <= 25 && > 20 and check size (1)
			.body("age.findAll {it <= 25 && it > 20}.size()", is(1)) 
			
			// find by all and define it.age in {} check if .name array has item
			.body("findAll {it.age <= 25 && it.age > 20}.name", hasItem("Maria Joaquina"))
			
			// find by all and define it.age in {} define array position [0] "first" to check name
			.body("findAll {it.age <= 25}[0].name", is("Maria Joaquina"))
			
			// find by all and define it.age in {} define array position [-1] "last" to check name
			.body("findAll {it.age <= 25}[-1].name", is("Ana Júlia"))
			
			// find and define it.age in {} check the first item find
			.body("find {it.age <= 25}.name", is("Maria Joaquina"))
			
			// find by all object it.name with contain 'n'
			.body("findAll {it.name.contains('n')}.name", hasItems("Maria Joaquina", "Ana Júlia"))
			
			// find by all object it.name with contain >10 characters 
			.body("findAll {it.name.length() > 10}.name", hasItems("João da Silva", "Maria Joaquina"))

			// collect object .name and change to UpperCase 
			.body("name.collect{it.toUpperCase()}", hasItems("MARIA JOAQUINA"))
			
			// collect object .age and add (* 2) 
			.body("age.collect{it * 2}", hasItems(60, 50, 40))
		;
	}
}
