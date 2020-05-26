package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class MecMuestraVehiculo extends Simulation {

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
			.headers(headers_0)
			.resources(http("request_1")
			.get("/")
			.headers(headers_1)))
		.pause(19) 
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_3")
			.get("/login")
			.headers(headers_1),
            http("request_4")
			.get("/login")
			.headers(headers_4))
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(30)
			.exec(http("Loged")
			.post("/login")
			.headers(headers_5)
			.formParam("username", "paco")
			.formParam("password", "paco")
			.formParam("_csrf", "${stoken}")
			.resources(http("request_6")
			.get("/")
			.headers(headers_1)))
		.pause(17)

	}

	object Login2 {
		val login2 = exec(http("Login2")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_3")
			.get("/login")
			.headers(headers_1),
            http("request_4")
			.get("/login")
			.headers(headers_4))
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(30)
			.exec(http("Loged")
			.post("/login")
			.headers(headers_5)
			.formParam("username", "lolo")
			.formParam("password", "lolo")
			.formParam("_csrf", "${stoken}")
			.resources(http("request_6")
			.get("/")
			.headers(headers_1)))
		.pause(17)

	}

	object ListarCitas {
		val listarcitas = exec(http("ListarCitas")
			.get("/mecanicos/citas")
			.headers(headers_0)
			.resources(http("request_12")
			.get("/mecanicos/citas")
			.headers(headers_1)))
		.pause(18)
	}
		
	object MostrarVehiculo {
		val mostrarVehiculo = exec(http("MostrarVehiculo")
			.get("/mecanicos/vehiculos/1")
			.headers(headers_0)
			.resources(http("request_14")
			.get("/mecanicos/vehiculos/1")
			.headers(headers_1)))
		.pause(12)
	}

	object MostrarVehiculoNegativo {
		val mostrarVehiculoNegativo = exec(http("MostrarVehiculoNegativo")
			.get("/mecanicos/vehiculos/3")
			.headers(headers_0)
			.resources(http("request_14")
			.get("/mecanicos/vehiculos/3")
			.headers(headers_1)))
		.pause(12)
	}
		
	val mostrarPositivo = scenario("paco").exec(Home.home, Login.login, ListarCitas.listarcitas, MostrarVehiculo.mostrarVehiculo)
	val mostrarNegativo = scenario("lolo").exec(Home.home, Login2.login2, ListarCitas.listarcitas, MostrarVehiculoNegativo.mostrarVehiculoNegativo)
	

	setUp(mostrarPositivo.inject(atOnceUsers(1)), mostrarNegativo.inject(atOnceUsers(1))).protocols(httpProtocol)

	//setUp(scn.inject(rampUsers(100) during (30 seconds)))
	//.protocols(httpProtocol)
	//Codigo de comprobacion de eficacia
	/*
	.assertions(global.responseTime.max.lt(5000),
	global.responseTime.mean.lt(1000),
	global.successfulRequests.percent.gt(95))
	*/
}