package talleres_paco

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class MecActualizaCita extends Simulation {

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
		.pause(9)
	}

	object LoginPaco {
		val login = exec(http("LoginPaco")
			.get("/login")
			.headers(headers_0))
		.pause(1)
		.exec(http("LoginPaco")
			.get("/login")
			.headers(headers_1)
			.resources(http("LoginPacoResources")
			.get("/login")
			.headers(headers_4))
			.check(css("input[name=_csrf]", "value").saveAs("stoken1")))
		.pause(10)

		.exec(http("LoggedPaco")
			.post("/login")
			.headers(headers_5)
			.formParam("username", "paco")
			.formParam("password", "paco")
			.formParam("_csrf", "${stoken1}") 
			.resources(http("LoggedPaco")
			.get("/")
			.headers(headers_1)))
		.pause(12)
	}

	object LoginLolo {
		val login = exec(http("LoginLolo")
			.get("/login")
			.headers(headers_0))
		.pause(1)
		.exec(http("LoginLolo")
			.get("/login")
			.headers(headers_1)
			.resources(http("LoginLoloResources")
			.get("/login")
			.headers(headers_4))
			.check(css("input[name=_csrf]", "value").saveAs("stoken2")))
		.pause(10)

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
		.pause(13)
	}

	object EditCita {
		var editCita = exec(http("EditCitaForm")
			.get("/mecanicos/citas/1/edit")
			.headers(headers_0)
			.resources(http("EditCitaFormResources")
			.get("/mecanicos/citas/1/edit")
			.headers(headers_1))
			.check(css("input[name=_csrf]", "value").saveAs("stoken3")))
		.pause(33)

		.exec(http("CitaUpdated")
			.post("/mecanicos/citas/1/edit")
			.headers(headers_5)
			.formParam("fechaCita", "14/03/2021 13:00")
			.formParam("descripcion", "Problemas con el motor desconocido")
			.formParam("tiempo", "100")
			.formParam("coste", "130.0")
			.formParam("estadoCita", "aceptada")
			.formParam("_csrf", "${stoken3}") 
			.resources(http("CitaUpdatedResources")
			.get("/mecanicos/citas/")
			.headers(headers_1)))
		.pause(26)
	}

	object EditCitaError {
		var editCita = exec(http("EditCitaError")
			.get("/mecanicos/citas/1/edit")
			.headers(headers_0)
			.resources(http("EditCitaErrorResources")
			.get("/mecanicos/citas/1/edit")
			.headers(headers_1)))
		.pause(13)
	}

	val PacoScn = scenario("Paco").exec(Home.home, LoginPaco.login, CitasList.citasList, EditCita.editCita)

	val LoloScn = scenario("Lolo").exec(Home.home, LoginLolo.login, CitasList.citasList, EditCitaError.editCita)

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