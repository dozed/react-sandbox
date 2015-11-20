package reactsandbox

import org.http4s.dsl._
import org.http4s.headers.`Content-Type`
import org.http4s.server._
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.{MediaType, Response}

import scalaz.concurrent.Task
import scalaz.stream.Process
import scalaz.stream.io._

object Starter extends App {

  val webSource = "target/web/stage"
  val webPort = 8102

  def staticResource(file: String): Task[Response] = {

    val f = new java.io.File(f"$webSource/$file")

    val bytes = Process.constant(8*1024)
      .toSource
      .through(chunkR(new java.io.FileInputStream(f)))
      .runLog
      .run
      .map(_.toArray)
      .toArray
      .flatten

    val mime = {
      val parts = file.split('.')
      if (parts.length > 0) MediaType.forExtension(parts.last)
        .getOrElse(MediaType.`application/octet-stream`)
      else MediaType.`application/octet-stream`
    }

    Ok(bytes).putHeaders(`Content-Type`(mime))
  }

  val service: HttpService = HttpService {

    case r @ GET -> "app" /: "react.html" /: _ =>
      staticResource("react.html")

    case r @ GET -> "app" /: name =>
      staticResource(name.toString.drop(1))

  }

  val s = BlazeBuilder
    .bindHttp(webPort, "localhost")
    .mountService(service)
    .run
    .awaitShutdown

}
