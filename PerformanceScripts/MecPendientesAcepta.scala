package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class MecPendientesAcepta extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources()
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

	val headers_0 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")



	val scn = scenario("MecPendientesAcepta")
		.exec(http("request_0")
			.get("/")
			.headers(headers_0))
		.pause(4)
		.exec(http("request_1")
			.get("/logout")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/favicon.ico")
			.headers(headers_2)
			.check(status.is(403))))
		.pause(1)
		.exec(http("request_3")
			.post("/logout")
			.headers(headers_3)
			.formParam("_csrf", "178de5ad-067b-41de-a349-25bf02225063"))
		.pause(34)
		// de cliente
		// mecanico
		.exec(http("request_4")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_5")
			.get("/favicon.ico")
			.headers(headers_2)))
		.pause(5)
		.exec(http("request_6")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "paco")
			.formParam("password", "paco")
			.formParam("_csrf", "ba0864fb-60cc-47c4-bf31-ea03c65be07c"))
		.pause(1)
		.exec(http("request_7")
			.get("/mecanicos/citasPendientes")
			.headers(headers_0))
		.pause(1)
		.exec(http("request_8")
			.get("/mecanicos/citas/4/aceptar")
			.headers(headers_0))
		.pause(1)
		.exec(http("request_9")
			.post("/mecanicos/citas/4/aceptar")
			.headers(headers_3)
			.formParam("id", "4")
			.formParam("_csrf", "f64789ea-663b-4619-8b5f-0a8a5373a4a9"))
		.pause(11)
		// ya

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}