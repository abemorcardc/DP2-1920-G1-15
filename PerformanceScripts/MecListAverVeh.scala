package talleres_paco

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class MecListAverVeh extends Simulation {

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
		.pause(6)
	}

	object LoginPaco {
		val login = exec(http("LoginPaco")
			.get("/login")
			.headers(headers_0)
			.resources(http("LoginPacoResources")
			.get("/login")
			.headers(headers_1))
			.check(css("input[name=_csrf]", "value").saveAs("stoken1")))
		.pause(13)

		.exec(http("LoggedPaco")
			.post("/login")
			.headers(headers_5)
			.formParam("username", "paco")
			.formParam("password", "paco")
			.formParam("_csrf", "${stoken1}") 
			.resources(http("LoggedPacoResources")
			.get("/")
			.headers(headers_1)))
		.pause(9)
	}

	object LoginLolo {
		val login = exec(http("LoginLolo")
			.get("/login")
			.headers(headers_0)
			.resources(http("LoginLoloResources")
			.get("/login")
			.headers(headers_1))
			.check(css("input[name=_csrf]", "value").saveAs("stoken2")))
		.pause(13)

		.exec(http("LoggedLolo")
			.post("/login")
			.headers(headers_5)
			.formParam("username", "lolo")
			.formParam("password", "lolo")
			.formParam("_csrf", "${stoken2}") 
			.resources(http("LoggedLoloResources")
			.get("/")
			.headers(headers_1)))
		.pause(11)
	}

	object CitasList {
		var citasList = exec(http("CitasList")
			.get("/mecanicos/citas")
			.headers(headers_0)
			.resources(http("CitasListResources")
			.get("/mecanicos/citas")
			.headers(headers_1)))
		.pause(11)
	}

	object AveriasList {
		var averiasList = exec(http("AverList")
			.get("/mecanicos/vehiculos/1/averia")
			.headers(headers_0)
			.resources(http("AverListResources")
			.get("/mecanicos/vehiculos/1/averia")
			.headers(headers_1)))
		.pause(17)
	}

	object AverListError {
		var averiasList = exec(http("AverListError")
			.get("/mecanicos/vehiculos/1/averia")
			.headers(headers_0)
			.resources(http("AverListErrorResources")
			.get("/mecanicos/vehiculos/1/averia")
			.headers(headers_1)))
		.pause(15)
	}

	val PacoScn = scenario("Paco").exec(Home.home, LoginPaco.login, CitasList.citasList, AveriasList.averiasList)

	val LoloScn = scenario("Lolo").exec(Home.home, LoginLolo.login, CitasList.citasList, AverListError.averiasList)

	setUp(PacoScn.inject(atOnceUsers(1)),
	LoloScn.inject(atOnceUsers(1))
	).protocols(httpProtocol)

	/*
	setUp(
		PacoScn.inject(rampUsers(5000) during (100 seconds)),
		LoloScn.inject(rampUsers(5000) during (100 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
	 )
	 */
}