package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ListarVehiculos extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.talleres-paco.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Purpose" -> "prefetch",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_1 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_3 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_4 = Map(
		"Origin" -> "http://www.talleres-paco.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")



           object Home {
                  val home = exec(http("Home")
			.get("/")
			.headers(headers_0)
			.resources(http("request_1")
			.get("/")
			.headers(headers_1)))
		.pause(10)
}

           object Login {
                  val login = exec(http("Login")
			.get("/login")
			.headers(headers_1)
			.resources(http("request_3")
			.get("/login")
			.headers(headers_3)))
		.pause(16)
}

           object Logged {
                  val logged = exec(http("Logged")
			.post("/login")
			.headers(headers_4)
			.formParam("username", "manolo")
			.formParam("password", "manolo")
			.formParam("_csrf", "0d517037-e02c-4eaf-ba4e-e84653120bfc"))
		.pause(8)
}

           object ListarVehiculos {
                  val listarVehiculos = exec(http("ListarVehiculos")
			.get("/cliente/vehiculos")
			.headers(headers_1))
		.pause(10)
}


           object Logged2 {
                  val logged2 = exec(http("Logged2")
			.post("/login")
			.headers(headers_4)
			.formParam("username", "manoli")
			.formParam("password", "manoli")
			.formParam("_csrf", "14094579-9018-4e70-b0ee-f4373a56a28d"))
		.pause(8)
}


	val listar1 = scenario("Manolo").exec(Home.home, Login.login, Logged.logged, ListarVehiculos.listarVehiculos)
        val listar2 = scenario("Manoli").exec(Home.home, Login.login, Logged2.logged2, ListarVehiculos.listarVehiculos)
		

	setUp(
            listar1.inject(atOnceUsers(1)),
            listar2.inject(atOnceUsers(1))
            ).protocols(httpProtocol)
}