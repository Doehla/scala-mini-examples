package apiWithHttp4s

import cats.effect.{IOApp, IO, ExitCode}
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonOf, jsonEncoderOf}
import io.circe.syntax.EncoderOps
import com.typesafe.scalalogging.Logger

import example.User


object Main
  extends IOApp {
  val logger = Logger("Main")

  implicit val userDecoder: EntityDecoder[IO, User] = jsonOf[IO, User]

  val httpApp = HttpRoutes.of[IO] {
    case req@POST -> Root / "hello" => for {
      usr <- req.as[User]
      _ = doThing(usr)
      res <- Ok(usr.asJson.noSpaces)
    } yield res
  }.orNotFound

  def doThing(usr: User): Unit = {
    logger.info(s"${usr.toString}; ${usr.asJson.noSpaces}")
  }

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}
