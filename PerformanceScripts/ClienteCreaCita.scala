package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ClienteCreaCita extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.ico""", """.*.png""", """.*.css""", """.*.js"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9,en;q=0.8,gl;q=0.7,de;q=0.6")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

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

	object Home{
		val home= exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(11)
		// Home
	}
	
	object Login{
		val login= exec(
			http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
		).pause(16)
		.exec(
		  http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "manolo")
			.formParam("password", "manolo")
			.formParam("_csrf", "${stoken}")
		).pause(15)
		// Login
	}

	object ListCitas{
		val listCitas= exec(http("ListCitas")
			.get("/cliente/citas")
			.headers(headers_0))
		.pause(15)
		// ListCitas
	}

	object CrearCita{
		val crearCita= exec(
		  http("CrearCita")
			.get("/cliente/citas/pedir")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
		).pause(15)
		.exec(
		  http("EscogerVehiculo")
			.get("/cliente/citas/vehiculo")
			.headers(headers_0)
		
		).pause(10)// EscogerVehiculo
		.exec(
	 	  http("VehiculoEscogido")
			.get("/cliente/citas/pedir?vehiculoId=1")
			.headers(headers_0))
		.pause(31)// VehiculoEscogido
		.exec(http("CitaCreada")
			.post("/cliente/citas/pedir?vehiculoId=1")
			.headers(headers_3)
			.formParam("estadoCita", "pendiente")
			.formParam("fechaCita", "30/05/2020 10:10")
			.formParam("descripcion", "hola hola")
			.formParam("coste", "0.0")
			.formParam("tiempo", "0")
			.formParam("esUrgente", "TRUE")
			.formParam("tipo", "revision")
			.formParam("_csrf", "${stoken}"))
		.pause(23)
		// CitaCreada
		
	}

	object CrearCitaSinDescripcion{
		val crearCitaSinDescripcion= exec(
		  http("CrearCitaSinDecripcion")
			.get("/cliente/citas/pedir")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
		).pause(15)
		.exec(
		  http("EscogerVehiculo")
			.get("/cliente/citas/vehiculo")
			.headers(headers_0)
		
		).pause(10)// EscogerVehiculo
		.exec(
	 	  http("VehiculoEscogido")
			.get("/cliente/citas/pedir?vehiculoId=1")
			.headers(headers_0))
		.pause(31)// VehiculoEscogido
		.exec(http("CitaCreada")
			.post("/cliente/citas/pedir?vehiculoId=1")
			.headers(headers_3)
			.formParam("estadoCita", "pendiente")
			.formParam("fechaCita", "29/05/2020 19:22")
			.formParam("descripcion", "")
			.formParam("coste", "0.0")
			.formParam("tiempo", "0")
			.formParam("esUrgente", "TRUE")
			.formParam("tipo", "revision")
			.formParam("_csrf", "${stoken}"))
		.pause(23)
		// CitaNoCreada
		
	}


	val CliCreaCitaScn = scenario("ClienteCreaCita").exec(
		Home.home,
		Login.login,
		ListCitas.listCitas,
		CrearCita.crearCita
		)

	val CliCreaCitaScnNegativo = scenario("ClienteCreaCitaNegativo").exec(
		Home.home,		
		Login.login,
		ListCitas.listCitas, 
		CrearCitaSinDescripcion.crearCitaSinDescripcion
	)
		

	setUp(
		CliCreaCitaScn.inject(atOnceUsers(1)),
		CliCreaCitaScnNegativo.inject(atOnceUsers(1))
		).protocols(httpProtocol)
}