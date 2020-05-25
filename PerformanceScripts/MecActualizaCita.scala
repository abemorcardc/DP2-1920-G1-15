package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class MecactualizaCita extends Simulation {

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
			.formParam("username", "paco")
			.formParam("password", "paco")
			.formParam("_csrf", "${stoken}") 
			.resources(http("LoggedResources")
			.get("/")
			.headers(headers_1)))
		.pause(8)
	}

	object CitasList {
		var citasList = exec(http("CitasList")
			.get("/mecanicos/citas")
			.headers(headers_0)
			.resources(http("CitasListResources")
			.get("/mecanicos/citas")
			.headers(headers_1)))
		.pause(10)
	}

	object EditCita {
		var editCita = exec(http("EditCitaForm")
			.get("/mecanicos/citas/1/edit")
			.headers(headers_0)
			.resources(http("EditCitaFormRsources")
			.get("/mecanicos/citas/1/edit")
			.headers(headers_1))
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(100)
		.exec(http("CitaUpdated")
			.post("/mecanicos/citas/1/edit")
			.headers(headers_5)
			.formParam("fechaCita", "14/03/2021 12:30")
			.formParam("descripcion", "Problemas con el motor del coche")
			.formParam("tiempo", "40")
			.formParam("coste", "150.0")
			.formParam("estadoCita", "aceptada")
			.formParam("_csrf", "${stoken}") 
			.resources(http("CitaUpdatedResources")
			.get("/mecanicos/citas/")
			.headers(headers_1)))
		.pause(11)
	}

	val scn = scenario("MecActualizaCita").exec(Home.home, Login.login, CitasList.citasList, EditCita.editCita)

	setUp(scn.inject(rampUsers(100) during (30 seconds)))
	.protocols(httpProtocol)
	//Codigo de comprobacion de eficacia
	/*
	.assertions(global.responseTime.max.lt(5000),
	global.responseTime.mean.lt(1000),
	global.successfulRequests.percent.gt(95))
	*/
}