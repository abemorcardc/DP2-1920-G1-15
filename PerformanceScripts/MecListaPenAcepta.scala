
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class MecListaPenAcepta extends Simulation {

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

	
	object GetLogin{
	val getLogin = exec(http("GetLogin")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_1")
			.get("/favicon.ico")
			.headers(headers_1)))
		.pause(11)
		}

object PostLogin{
		val postLogin = exec(http("PostLogin")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "paco")
			.formParam("password", "paco")
			.formParam("_csrf", "e1a0ba33-9d7f-4005-9482-f8c2aa469df7"))
		.pause(7)
		}

object GetCitasP{
		val getCitasP = exec(http("GetCitasP")
			.get("/mecanicos/citasPendientes")
			.headers(headers_0))
		.pause(7)
}
object GetAceptar{
		 val getAceptar = exec(http("GetAceptar")
			.get("/mecanicos/citas/4/aceptar")
			.headers(headers_0))
		.pause(6)
		}
		
		object PostAceptar{
		val postAceptar = exec(http("PostAceptar")
			.post("/mecanicos/citas/4/aceptar")
			.headers(headers_2)
			.formParam("id", "4")
			.formParam("_csrf", "afc3ac33-3140-4a23-b44f-6751bf42087f"))
		.pause(6)
}

val scn = scenario("MecListaPenAcepta").exec(GetLogin.getLogin, PostLogin.postLogin, GetCitasP.getCitasP, GetAceptar.getAceptar, PostAceptar.postAceptar)


	setUp(scn.inject(rampUsers(100) during (30 seconds)))
	.protocols(httpProtocol)
	//Codigo de comprobacion de eficacia
	/*
	.assertions(global.responseTime.max.lt(5000),
	global.responseTime.mean.lt(1000),
	global.successfulRequests.percent.gt(95))
	*/
}