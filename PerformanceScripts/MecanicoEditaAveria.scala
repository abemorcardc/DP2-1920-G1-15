package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class MecanicoEditaAveria extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.ico""", """.*.png""", """.*.css""", """.*.js"""), WhiteList())
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8,gl;q=0.7,de;q=0.6",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8,gl;q=0.7,de;q=0.6",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8,gl;q=0.7,de;q=0.6",
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_4 = Map(
		"A-IM" -> "x-bm,gzip",
		"Proxy-Connection" -> "keep-alive")

    //val uri1 = "http://clientservices.googleapis.com/chrome-variations/seed"

	object Home{
		val home= exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(11)
		// Home
	}

	object Login{
		val login= exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
		).pause(17)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "paco")
			.formParam("password", "paco")
			.formParam("_csrf", "${stoken}"))
		.pause(19)
		// Logged
		// Login
	}

	object ListaAverias{
		val listaAverias= exec(http("ListaAverias")
			.get("/mecanicos/vehiculos/1/averia")
			.headers(headers_0))
		.pause(13)
		// ListaAverias
	}

	object EditarAveria{
		val editarAveria= exec(http("EditarAveria")
			.get("/mecanicos/vehiculos/1/averia/1/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
		).pause(22)
		.exec(http("AveriaEditada")
			.post("/mecanicos/vehiculos/1/averia/1/edit")
			.headers(headers_3)
			.formParam("id", "1")
			.formParam("nombre", "coche de manolo")
			.formParam("descripcion", "cambio de bujia")
			.formParam("coste", "5000.0")
			.formParam("tiempo", "100")
			.formParam("piezasNecesarias", "1")
			.formParam("estaReparada", "true")
			.formParam("complejidad", "BAJA")
			.formParam("_csrf", "${stoken}"))
		.pause(18)
		// EditarAveria
	}

	object EditarAveriaNegativo{
		val editarAveriaNegativo= exec(http("EditarAveria")
			.get("/mecanicos/vehiculos/1/averia/1/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
		).pause(22)
		. exec(http("AveriaEditadaNegativo")
			.post("/mecanicos/vehiculos/1/averia/1/edit")
			.headers(headers_3)
			.formParam("id", "1")
			.formParam("nombre", "coche de manolo")
			.formParam("descripcion", "")
			.formParam("coste", "5000.0")
			.formParam("tiempo", "100")
			.formParam("piezasNecesarias", "1")
			.formParam("estaReparada", "true")
			.formParam("complejidad", "BAJA")
			.formParam("_csrf", "${stoken}"))
		.pause(18)
		// AveriaEditada
	}


	val MecUpdateAverScn = scenario("MecanicoActualizaAveria").exec(
		Home.home,
		Login.login,
		ListaAverias.listaAverias,
		EditarAveria.editarAveria
	)

	val MecUpdateAverScnNegativo = scenario("MecanicoActualizaAveriaNegativo").exec(
		Home.home,
		Login.login,
		ListaAverias.listaAverias,
		EditarAveriaNegativo.editarAveriaNegativo
	)

		
	setUp(
		MecUpdateAverScn.inject(atOnceUsers(1)),
		MecUpdateAverScnNegativo.inject(atOnceUsers(1))
		).protocols(httpProtocol)
}