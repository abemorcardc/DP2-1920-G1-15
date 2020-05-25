package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class MecPendientesAcepta2 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources()
		.acceptHeader("*/*")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_1 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9",
		"Proxy-Connection" -> "keep-alive")

	val headers_2 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9",
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_5 = Map(
		"MS-CV" -> "py0Fpw1wCkOuXEJC.1.1.5.1.251.2.3.1.1.1",
		"Proxy-Connection" -> "Keep-Alive",
		"Range" -> "bytes=0-1",
		"User-Agent" -> "Microsoft-Delivery-Optimization/10.0")

	val headers_6 = Map(
		"MS-CV" -> "py0Fpw1wCkOuXEJC.1.1.5.1.251.2.3.2.1.1",
		"Proxy-Connection" -> "Keep-Alive",
		"Range" -> "bytes=0-1",
		"User-Agent" -> "Microsoft-Delivery-Optimization/10.0")

	val headers_7 = Map(
		"MS-CV" -> "py0Fpw1wCkOuXEJC.1.1.5.1.251.2.3.2.1.2",
		"Proxy-Connection" -> "Keep-Alive",
		"Range" -> "bytes=0-1048575",
		"User-Agent" -> "Microsoft-Delivery-Optimization/10.0")

	val headers_8 = Map(
		"MS-CV" -> "py0Fpw1wCkOuXEJC.1.1.5.1.251.2.3.1.1.2",
		"Proxy-Connection" -> "Keep-Alive",
		"Range" -> "bytes=1048576-2097151",
		"User-Agent" -> "Microsoft-Delivery-Optimization/10.0")

	val headers_9 = Map(
		"MS-CV" -> "py0Fpw1wCkOuXEJC.1.1.5.1.251.2.3.2.1.3",
		"Proxy-Connection" -> "Keep-Alive",
		"Range" -> "bytes=2097152-2323764",
		"User-Agent" -> "Microsoft-Delivery-Optimization/10.0")

    val uri2 = "http://au.download.windowsupdate.com/c/msdownload/update/software/updt/2020/01/windows10.0-kb4497165-v4-x64_d20e6c08d3604463db51f5b55160298f4ff361d5.cab"

	val scn = scenario("MecPendientesAcepta2")
		.exec(http("request_0")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_1")
			.get("/favicon.ico")
			.headers(headers_1)))
		.pause(11)
		// getlogin
		.exec(http("request_2")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "paco")
			.formParam("password", "paco")
			.formParam("_csrf", "e4aa826d-a108-4df9-a926-1e7e603c7e26"))
		.pause(11)
		// logged
		.exec(http("request_3")
			.get("/mecanicos/citasPendientes")
			.headers(headers_0))
		.pause(10)
		// getcitasP
		.exec(http("request_4")
			.get("/mecanicos/citas/4/aceptar")
			.headers(headers_0))
		.pause(7)
		.exec(http("request_5")
			.get(uri2)
			.headers(headers_5)
			.resources(http("request_6")
			.get(uri2)
			.headers(headers_6),
            http("request_7")
			.get(uri2)
			.headers(headers_7),
            http("request_8")
			.get(uri2)
			.headers(headers_8),
            http("request_9")
			.get(uri2)
			.headers(headers_9)))
		.pause(15)
		// getAceptar
		.exec(http("request_10")
			.post("/mecanicos/citas/4/aceptar")
			.headers(headers_2)
			.formParam("id", "4")
			.formParam("_csrf", "d0e4c19e-7029-4f82-a48f-acad34b99b03"))
		.pause(7)
		// postAceptar

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}