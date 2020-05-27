
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class MecListaPAcepta2 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

	val headers_0 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_1 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_2 = Map(
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_3 = Map(
		"Accept" -> "text/css,*/*;q=0.1",
		"Proxy-Connection" -> "keep-alive")



	object GetLogin {
		val getLogin = exec(http("GetLogin")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_1")
			.get("/favicon.ico")
			.headers(headers_1)))
		.pause(3)
	}
	object PostLogin{
		val postLogin = exec(http("PostLogin")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "paco")
			.formParam("password", "paco")
			.formParam("_csrf", "10beccc7-75e5-4094-8f4e-e7d901b91009"))
			
		.pause(10)
		// logged
	}
	object GetCitasP {
		val getCitasP = exec(http("GetCitasP")
			.get("/mecanicos/citasPendientes")
			.headers(headers_0))
			
		.pause(11)
		// getcitasp
	}
	object GetAcep{
		val getAcep = exec(http("GetAcep")
			.get("/mecanicos/citas/4/aceptar")
			.headers(headers_0))

		.pause(6)
		// getacep
		}
	object Aceptado{
		val aceptado = exec(http("Aceptado")
			.post("/mecanicos/citas/4/aceptar")
			.headers(headers_2)
			.formParam("id", "4")
			.formParam("_csrf", "c25b409a-f9ed-49d3-a0d2-9b3e2f0edbc5"))
			
		.pause(4)
		// aceptado
	}
	object GetAcepN{
		val getAcepN = exec(http("GetAcepN")
			.get("/mecanicos/citas/9/aceptar")
			.headers(headers_0))

		.pause(6)
		// getacep
	}
	object AceptadoN{
		val aceptadoN = exec(http("AceptadoN")
			.post("/mecanicos/citas/9/aceptar")
			.headers(headers_2)
			.formParam("id", "9")
			.formParam("_csrf", "c25b409a-f9ed-49d3-a0d2-9b3e2f0edbc5"))
			
		.pause(4)
		// aceptado
	}

val scn = scenario("MecListaPAcepta").exec(GetLogin.getLogin,PostLogin.postLogin, GetCitasP.getCitasP, GetAcep.getAcep, Aceptado.aceptado)
val scn2 = scenario("MecListaPAceptaNeg").exec(GetLogin.getLogin,PostLogin.postLogin, GetCitasP.getCitasP, GetAcepN.getAcepN, AceptadoN.aceptadoN)
	setUp(scn.inject(atOnceUsers(1)), 
		scn2.inject(atOnceUsers(1)))
	.protocols(httpProtocol)
}