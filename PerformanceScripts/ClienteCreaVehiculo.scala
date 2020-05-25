package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ClienteCreaVehiculo extends Simulation {

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
			.formParam("username", "manolo")
			.formParam("password", "manolo")
			.formParam("_csrf", "${stoken}")
			.resources(http("request_6")
			.get("/")
			.headers(headers_1)))
		.pause(17)

	}
		
	object ListarVehiculos {
		val listarVehiculos = exec(http("ListarVehiculos")
			.get("/cliente/vehiculos")
			.headers(headers_0)
			.resources(http("request_8")
			.get("/cliente/vehiculos")
			.headers(headers_1)))
		.pause(13)
	}

	object CrearVehiculo {
		val crearVehiculo = exec(http("FormVehiculo")
			.get("/cliente/vehiculos/crear")
			.headers(headers_0)
			.resources(http("request_5")
			.get("/cliente/vehiculos/crear")
			.headers(headers_1))
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(36)
			.exec(http("CrearVehiculo")
			.post("/cliente/vehiculos/crear")
			.headers(headers_5)
			.formParam("matricula", "1234ASD")
			.formParam("fechaMatriculacion", "2020-05-03")
			.formParam("modelo", "kjhg")
			.formParam("kilometraje", "100")
			.formParam("tipoVehiculo", "todoterreno")
			.formParam("activo", "true")
			.formParam("_csrf", "${stoken}")
			.resources(http("request_7")
			.get("/cliente/vehiculos")
			.headers(headers_1)))
		.pause(10)
	}
		
	val crear = scenario("CrearVehiculo").exec(Home.home, Login.login, ListarVehiculos.listarVehiculos, CrearVehiculo.crearVehiculo)	

	setUp(crear.inject(atOnceUsers(1))).protocols(httpProtocol)
}