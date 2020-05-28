package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ClienteMuestraAveria extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.ico""", """.*.png""", """.*.js"""), WhiteList())
		.acceptHeader("*/*")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

	val headers_0 = Map(
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive")

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

	object Login2 {
		val login2 = exec(http("Login2")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(30)
			.exec(http("Loged")
			.post("/login")
			.headers(headers_5)
			.formParam("username", "manoli")
			.formParam("password", "manoli")
			.formParam("_csrf", "${stoken}"))
		.pause(17)

	}
		
	object ListarVehiculos {
		val listarVehiculos = exec(http("ListarVehiculos")
			.get("/cliente/vehiculos")
			.headers(headers_0))
		.pause(13)
	}

	object ListarAverias {
		val listarAverias = exec(http("ListarAverias")
			.get("/cliente/vehiculos/1/averias")
			.headers(headers_0))
		.pause(23)
	}
		
	object MostrarAveria {
		val mostrarAveria = exec(http("MostrarAveria")
			.get("/cliente/averia/1")
			.headers(headers_0))
		.pause(70)
	}
		
	object MostrarAveriaNegativo {
		val mostrarAveriaNegativo = exec(http("MostrarAveriaNegativo")
			.get("/cliente/averia/1")
			.headers(headers_0))
		.pause(27)
	}

	val mostrarPositivo = scenario("manolo").exec(Home.home, Login.login, ListarVehiculos.listarVehiculos, ListarAverias.listarAverias, MostrarAveria.mostrarAveria)
	val mostrarNegativo = scenario("manoli").exec(Home.home, Login2.login2, MostrarAveriaNegativo.mostrarAveriaNegativo)	

	setUp(mostrarPositivo.inject(rampUsers(300000) during (30 seconds)), mostrarNegativo.inject(rampUsers(300000) during (30 seconds))).protocols(httpProtocol)
	//Codigo de comprobacion de eficacia
	/*
	.assertions(global.responseTime.max.lt(5000),
	global.responseTime.mean.lt(1000),
	global.successfulRequests.percent.gt(95))
	*/
}