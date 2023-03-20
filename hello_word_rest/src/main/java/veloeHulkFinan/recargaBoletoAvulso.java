package veloeHulkFinan;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.BeforeClass;
import org.junit.Test;
import java.time.LocalDate;

public class recargaBoletoAvulso {
	
	@BeforeClass
	public static void setup() {
		baseURI = "http://gestao-creditos.app.hml.veloe.com.br/";
		basePath = "/v1";
	}
	
	@Test
	public void Cenário_01_Recarga_com_conta_PRE_PAGO() {
		LocalDate localDate = LocalDate.now();
		String dateIncrement = localDate.plusDays(7).toString();
		
		given()
			.log().all()
			.contentType("application/json")
			.body("{\r\n"
					+ "    \"idConta\": \"172709\",\r\n"
					+ "    \"origem\": \"VP\",\r\n"
					+ "    \"valor\": 30\r\n"
					+ "}")
			
		.when()
			.post("/recarga/boleto-avulso")
			
		.then()
			.statusCode(200)
			.body("status", is("SUCESSO"))		
			.body("mensagem", containsString("Documento criado com sucesso:"))
			.body("nsu", is(notNullValue()))
			.body("meioPagamento", is("BOLETO"))
			.body("dataVencimento", equalTo(dateIncrement))
			.body("valorOriginal", is(30))
			.body("boleto.linhaDigitavel", is(notNullValue()))
			.body("boleto.link", containsString("https://s3.amazonaws.com/download-boleto"))
		;
	}
	
	@Test
	public void Cenário_02_Recarga_com_conta_diferente_de_PRE_PAGO() {	
		given()
			.log().all()
			.contentType("application/json")
			.body("{\r\n"
					+ "    \"idConta\": \"196263\",\r\n"
					+ "    \"origem\": \"VP\",\r\n"
					+ "    \"valor\": 150\r\n"
					+ "}")	
			
		.when()
			.post("/recarga/boleto-avulso")	
			
		.then()
			.statusCode(400)
			.body("title", is("Ação não permitida. Conta 196263 informada não é referente a um plano PRE-PAGO."))
			.body("timestamp", is(notNullValue()))
		;
	}

}
