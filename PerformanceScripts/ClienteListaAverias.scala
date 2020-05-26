package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ClienteListaAverias extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("*/*")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9,en;q=0.8")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

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
			.resources(http("HomeResources")
			.get("/")
			.headers(headers_1)))
		.pause(7)
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("LoginResources")
			.get("/login")
			.headers(headers_1))
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			)
		.pause(14)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_5)
			.formParam("username", "manolo")
			.formParam("password", "manolo")
			.formParam("_csrf", "${stoken}") 
			.resources(http("LoggedResources")
			.get("/")
			.headers(headers_1)))
		.pause(11)
	}

	object VehiculoList {
		var vehiculoList = exec(http("VehiculosList")
			.get("/cliente/vehiculos")
			.headers(headers_0)
			.resources(http("VehiculosListResources")
			.get("/cliente/vehiculos")
			.headers(headers_1)))
		.pause(12)
	}

	object AveriasList {
		var averiasList = exec(http("AveriasList")
			.get("/cliente/vehiculos/1/averias")
			.headers(headers_0)
			.resources(http("AveriasListResources")
			.get("/cliente/vehiculos/1/averias")
			.headers(headers_1)))
		.pause(11)
	}

	val scn = scenario("ClienteListaAverias").exec(Home.home, Login.login, VehiculoList.vehiculoList, AveriasList.averiasList)
	
	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)

	/*
	setUp(scn.inject(rampUsers(100) during (30 seconds)))
	.protocols(httpProtocol)
	*/
	//Codigo de comprobacion de eficacia
	/*
	.assertions(global.responseTime.max.lt(5000),
	global.responseTime.mean.lt(1000),
	global.successfulRequests.percent.gt(95))
	*/

}