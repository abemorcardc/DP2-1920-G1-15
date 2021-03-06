package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ClienteDaDeBajaVehiculo extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("*/*")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_1 = Map("Proxy-Connection" -> "keep-alive")

	val headers_4 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_5 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")



	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(19) 
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(30)
			.exec(http("Loged")
			.post("/login")
			.headers(headers_5)
			.formParam("username", "manolo")
			.formParam("password", "manolo")
			.formParam("_csrf", "${stoken}"))
		.pause(17)

	}
		
	object ListarVehiculos {
		val listarVehiculos = exec(http("ListarVehiculos")
			.get("/cliente/vehiculos")
			.headers(headers_0))
		.pause(13)
	}

	object MostrarVehiculo {
		val mostrarVehiculo = exec(http("MostrarVehiculo")
			.get("/cliente/vehiculos/1")
			.headers(headers_0))
		.pause(9)
	}
		
	object DarDeBaja {
		val darDeBaja = exec(http("FormDarDeBaja")
			.get("/cliente/vehiculos/4/disable")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(35)
			.exec(http("DarDeBaja")
			.post("/cliente/vehiculos/4/disable")
			.headers(headers_5)
			.formParam("id", "4")
			.formParam("activo", "true")
			.formParam("matricula", "0345FCL")
			.formParam("fechaMatriculacion", "2012-09-01 02:00:00.0")
			.formParam("modelo", "Mercedes AX")
			.formParam("tipoVehiculo", "turismo")
			.formParam("kilometraje", "1000")
			.formParam("_csrf", "${stoken}"))
		.pause(9)
	}

	object DarDeBajaNegativo {
		val darDeBajaNegativo = exec(http("FormDarDeBajaNegativo")
			.get("/cliente/vehiculos/1/disable")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(35)
	}

	val darDeBajaVehiculoPositivo = scenario("manolo").exec(Home.home, Login.login, ListarVehiculos.listarVehiculos, MostrarVehiculo.mostrarVehiculo, DarDeBaja.darDeBaja)	
	val darDeBajaVehiculoNegativo = scenario("manolo2").exec(Home.home, Login.login, ListarVehiculos.listarVehiculos, MostrarVehiculo.mostrarVehiculo, DarDeBajaNegativo.darDeBajaNegativo)	

	setUp(darDeBajaVehiculoPositivo.inject(rampUsers(300000) during (30 seconds)), darDeBajaVehiculoNegativo.inject(rampUsers(300000) during (30 seconds))).protocols(httpProtocol)
	//Codigo de comprobacion de eficacia
	/*
	.assertions(global.responseTime.max.lt(5000),
	global.responseTime.mean.lt(1000),
	global.successfulRequests.percent.gt(95))
	*/
}